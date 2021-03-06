/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opdracht_drie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Nick
 */
public class Opdracht_Drie_Retrieve {

    protected static Statement stmt = null;
    protected static Statement stmt2 = null;
    protected static Connection conn = null;
    protected static ResultSet rs = null;
    protected static ResultSet rs2 = null;
    protected static int studentNummer1 = 1000000;
    protected static int studentNummer2 = 2000000;
    protected static int studentNummer3 = 3000000;
    protected static int runs = 600;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Opdracht_Drie_Twee", "anl", "anl");
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        if (conn != null) {
            new Thread(new Runnable() {
                int thread1count = 0;
                long totalTime = 0;

                @Override
                public void run() {
                    while (thread1count < runs) {
                        try {
                            //System.out.println(thread1count);
                            long bT = System.currentTimeMillis();
                            List<String> vNaam = new ArrayList();
                            List<String> aNaam = new ArrayList();
                            stmt = conn.createStatement();
                            rs = stmt.executeQuery("SELECT vnaam, anaam FROM students");
                            while (rs.next()) {
                                vNaam.add(rs.getString(1));
                                aNaam.add(rs.getString(2));
                            }
                            Random rand = new Random();

                            int random = rand.nextInt(vNaam.size());

                            rs = stmt.executeQuery("SELECT module FROM module_klas WHERE klas = ("
                                    + "SELECT klas FROm student_klas WHERE studentnr = ("
                                    + "SELECT studentnr FROM students WHERE vnaam = '" + vNaam.get(random) + "' AND anaam = '" + aNaam.get(random) + "'))"
                            );
                            conn.commit();
                            long eT = System.currentTimeMillis();
                            totalTime = totalTime + (eT - bT);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        thread1count++;

                    }
                    double average = totalTime / (double) 600;

                    System.out.println(totalTime
                            + "----" + average);

                }

            }, "Thread 1").
                    start();
            new Thread(new Runnable() {
                int thread1count = 0;
                long totalTime = 0;

                @Override
                public void run() {
                    while (thread1count < runs) {
                        try {
                            long bT = System.currentTimeMillis();
                            List<String> vNaam = new ArrayList();
                            List<String> aNaam = new ArrayList();
                            stmt2 = conn.createStatement();
                            rs2 = stmt2.executeQuery("SELECT vnaam, anaam FROM students");
                            while (rs2.next()) {
                                vNaam.add(rs2.getString(1));
                                aNaam.add(rs2.getString(2));
                            }
                            Random rand = new Random();

                            int random = rand.nextInt(vNaam.size());

                            rs2 = stmt2.executeQuery("SELECT module FROM module_klas WHERE klas = ("
                                    + "SELECT klas FROm student_klas WHERE studentnr = ("
                                    + "SELECT studentnr FROM students WHERE vnaam = '" + vNaam.get(random) + "' AND anaam = '" + aNaam.get(random) + "'))"
                            );
                            conn.commit();
                            long eT = System.currentTimeMillis();
                            totalTime = totalTime + (eT - bT);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        thread1count++;
                    }
                    double average = totalTime / (double) 600;

                    System.out.println(totalTime
                            + "----" + average);
                }

            }, "Thread 1").
                    start();

        }

    }
}
