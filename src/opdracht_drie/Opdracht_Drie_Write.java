/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opdracht_drie;


import java.sql.Connection;
import java.util.Date;
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
public class Opdracht_Drie_Write {

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
            //4.2.1.1 - Start een transactie

            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Opdracht_Drie", "anl", "anl");
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            // handle any errors
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
                            long bT = System.currentTimeMillis();
                            //4.2.1.2 - Voeg student toe
                            stmt = conn.createStatement();
                            stmt2 = conn.createStatement();
                            Random rand = new Random();
                            stmt.executeUpdate("INSERT INTO students VALUES (" + studentNummer1 + ", '" + randomNameGenerator() + "', '" + randomNameGenerator() + "', '', 19, 'man', 'Groen van Prinstererstraat', '3354BB', 'Papendrecht', '0646784561' )");

                            //4.2.1.3 - Voeg met een kans van 1/30 een nieuwe klas toe. (Indien er nog geen klas is, maak dan een klas aan.)						
                            if (thread1count == 0) {
                                char c = (char) (rand.nextInt(26) + 'a');
                                String klas = "INF" + (rand.nextInt(4) + 1) + "" + c;
                                Date beginTijd = new Date();

                                Date eindTijd = createFutureDate();
                                stmt.executeUpdate("INSERT INTO klassen VALUES ('" + klas + "', '" + beginTijd + "', '" + eindTijd + "')");
                            }

                            int n = rand.nextInt(30) + 1;
                            if (n == 1) {
                                char c = (char) (rand.nextInt(26) + 'a');
                                String klas = "INF" + (rand.nextInt(4) + 1) + "" + c;
                                Date beginTijd = new Date();

                                Date eindTijd = createFutureDate();
                                stmt.executeUpdate("INSERT INTO klassen VALUES ('" + klas + "', '" + beginTijd + "', '" + eindTijd + "')");
                            }

                            //4.2.1.4 - Koppel de student aan een willekeurige klas,
                            rs = stmt.executeQuery("SELECT klas FROM klassen OFFSET random() * (SELECT COUNT(*) FROM klassen) LIMIT 1");
                            rs = stmt.executeQuery("SELECT klas FROM klassen");
                            String randomClass = "";
                            List<String> klassen = new ArrayList();

                            while (rs.next()) {
                                klassen.add(rs.getString(1));
                            }

                            n = rand.nextInt(klassen.size());

                            randomClass = klassen.get(n);
                            //System.out.println(randomClass);
                            stmt.executeUpdate("INSERT INTO student_klas (studentnr, klas) VALUES (" + studentNummer1 + ", '" + randomClass + "')");
                            long eT = System.currentTimeMillis();
                            totalTime = totalTime + (eT - bT);

                            //4.2.1.5 - Voeg met een kans van 1/30 een nieuwe module toe,  
                            n = rand.nextInt(30) + 1;
                            if (n == 1) {
                                //System.out.println("4.2.1.5");
                                String moduleCode = createCode();
                                stmt.executeUpdate("INSERT INTO modules VALUES ('" + moduleCode + "', 'Beheerder', 'moduleNaam', '" + new Date() + "', '" + createFutureDate() + "')");

                                //en koppel elke klas met een kans van 15% aan die module.
                                rs = stmt.executeQuery("SELECT klas FROM klassen");
                                while (rs.next()) {
                                    String klas = rs.getString(1);
                                    n = rand.nextInt(100) + 1;
                                    if (n > 0 && n < 16) {
                                        stmt2.executeUpdate("INSERT INTO module_klas (module, klas) VALUES ('" + moduleCode + "', '" + klas + "')");
                                    }
                                }

                            }
                        } catch (SQLException e) {
                            // e.printStackTrace();
                        }

                        //4.2.1.6 - Commit de transactie
                        try {
                            conn.commit();

                            //4.2.1.7 - Wacht vervolgens 100 milliseconden. Pas deze variabele aan indien het te snel/traag gaat. 
                            Thread.sleep(5);
                        } catch (SQLException e) {
                            //e.printStackTrace();
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        studentNummer1++;
                        thread1count++;
                    }
                    double average = totalTime / (double) 600;
                    System.out.println(totalTime + "----" + average);
                }

            }, "Thread 1").start();
            new Thread(new Runnable() {
                int thread2count = 0;
                long totalTime = 0;

                @Override
                public void run() {
                    while (thread2count < runs) {
                        try {
                            long bT = System.currentTimeMillis();
                            //4.2.1.2 - Voeg student toe
                            stmt = conn.createStatement();
                            stmt2 = conn.createStatement();
                            Random rand = new Random();
                            stmt.executeUpdate("INSERT INTO students VALUES (" + studentNummer2 + ", '" + randomNameGenerator() + "', '" + randomNameGenerator() + "', '', 19, 'man', 'Groen van Prinstererstraat', '3354BB', 'Papendrecht', '0646784561' )");

                            //4.2.1.3 - Voeg met een kans van 1/30 een nieuwe klas toe. (Indien er nog geen klas is, maak dan een klas aan.)						
                            if (thread2count == 0) {
                                char c = (char) (rand.nextInt(26) + 'a');
                                String klas = "INF" + (rand.nextInt(4) + 1) + "" + c;
                                Date beginTijd = new Date();

                                Date eindTijd = createFutureDate();
                                stmt.executeUpdate("INSERT INTO klassen VALUES ('" + klas + "', '" + beginTijd + "', '" + eindTijd + "')");
                            }

                            int n = rand.nextInt(30) + 1;
                            if (n == 1) {
                                char c = (char) (rand.nextInt(26) + 'a');
                                String klas = "INF" + (rand.nextInt(4) + 1) + "" + c;
                                Date beginTijd = new Date();

                                Date eindTijd = createFutureDate();
                                stmt.executeUpdate("INSERT INTO klassen VALUES ('" + klas + "', '" + beginTijd + "', '" + eindTijd + "')");
                            }

                            //4.2.1.4 - Koppel de student aan een willekeurige klas,
                            rs = stmt.executeQuery("SELECT klas FROM klassen OFFSET random() * (SELECT COUNT(*) FROM klassen) LIMIT 1");
                            rs = stmt.executeQuery("SELECT klas FROM klassen");
                            String randomClass = "";
                            List<String> klassen = new ArrayList();

                            while (rs.next()) {
                                klassen.add(rs.getString(1));
                            }

                            n = rand.nextInt(klassen.size());

                            randomClass = klassen.get(n);
                            //System.out.println(randomClass);
                            stmt.executeUpdate("INSERT INTO student_klas (studentnr, klas) VALUES (" + studentNummer2 + ", '" + randomClass + "')");
                            long eT = System.currentTimeMillis();
                            totalTime = totalTime + (eT - bT);

                            //4.2.1.5 - Voeg met een kans van 1/30 een nieuwe module toe,  
                            n = rand.nextInt(30) + 1;
                            if (n == 1) {
                                //System.out.println("4.2.1.5");
                                String moduleCode = createCode();
                                stmt.executeUpdate("INSERT INTO modules VALUES ('" + moduleCode + "', 'Beheerder', 'moduleNaam', '" + new Date() + "', '" + createFutureDate() + "')");

                                //en koppel elke klas met een kans van 15% aan die module.
                                rs = stmt.executeQuery("SELECT klas FROM klassen");
                                while (rs.next()) {
                                    String klas = rs.getString(1);
                                    n = rand.nextInt(100) + 1;
                                    if (n > 0 && n < 16) {
                                        stmt2.executeUpdate("INSERT INTO module_klas (module, klas) VALUES ('" + moduleCode + "', '" + klas + "')");
                                    }
                                }

                            }
                        } catch (SQLException e) {
                            //e.printStackTrace();
                        }

                        //4.2.1.6 - Commit de transactie
                        try {
                            conn.commit();

                            //4.2.1.7 - Wacht vervolgens 100 milliseconden. Pas deze variabele aan indien het te snel/traag gaat. 
                            Thread.sleep(5);
                        } catch (SQLException e) {
                            //e.printStackTrace();
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        studentNummer2++;
                        thread2count++;
                    }
                    double average = totalTime / (double) 600;
                    System.out.println(totalTime + "----" + average);
                }

            }, "Thread 2").start();
            new Thread(new Runnable() {
                int thread3count = 0;
                long totalTime = 0;

                @Override
                public void run() {
                    while (thread3count < runs) {
                        try {
                            long bT = System.currentTimeMillis();
                            //4.2.1.2 - Voeg student toe
                            stmt = conn.createStatement();
                            stmt2 = conn.createStatement();
                            Random rand = new Random();
                            stmt.executeUpdate("INSERT INTO students VALUES (" + studentNummer3 + ", '" + randomNameGenerator() + "', '" + randomNameGenerator() + "', '', 19, 'man', 'Groen van Prinstererstraat', '3354BB', 'Papendrecht', '0646784561' )");

                            //4.2.1.3 - Voeg met een kans van 1/30 een nieuwe klas toe. (Indien er nog geen klas is, maak dan een klas aan.)						
                            if (thread3count == 0) {
                                char c = (char) (rand.nextInt(26) + 'a');
                                String klas = "INF" + (rand.nextInt(4) + 1) + "" + c;
                                Date beginTijd = new Date();

                                Date eindTijd = createFutureDate();
                                stmt.executeUpdate("INSERT INTO klassen VALUES ('" + klas + "', '" + beginTijd + "', '" + eindTijd + "')");
                            }

                            int n = rand.nextInt(30) + 1;
                            if (n == 1) {
                                char c = (char) (rand.nextInt(26) + 'a');
                                String klas = "INF" + (rand.nextInt(4) + 1) + "" + c;
                                Date beginTijd = new Date();

                                Date eindTijd = createFutureDate();
                                stmt.executeUpdate("INSERT INTO klassen VALUES ('" + klas + "', '" + beginTijd + "', '" + eindTijd + "')");
                            }

                            //4.2.1.4 - Koppel de student aan een willekeurige klas,
                            rs = stmt.executeQuery("SELECT klas FROM klassen OFFSET random() * (SELECT COUNT(*) FROM klassen) LIMIT 1");
                            rs = stmt.executeQuery("SELECT klas FROM klassen");
                            String randomClass = "";
                            List<String> klassen = new ArrayList();

                            while (rs.next()) {
                                klassen.add(rs.getString(1));
                            }

                            n = rand.nextInt(klassen.size());

                            randomClass = klassen.get(n);
                            //System.out.println(randomClass);
                            stmt.executeUpdate("INSERT INTO student_klas (studentnr, klas) VALUES (" + studentNummer3 + ", '" + randomClass + "')");
                            long eT = System.currentTimeMillis();
                            totalTime = totalTime + (eT - bT);

                            //4.2.1.5 - Voeg met een kans van 1/30 een nieuwe module toe,  
                            n = rand.nextInt(30) + 1;
                            if (n == 1) {
                                //System.out.println("4.2.1.5");
                                String moduleCode = createCode();
                                stmt.executeUpdate("INSERT INTO modules VALUES ('" + moduleCode + "', 'Beheerder', 'moduleNaam', '" + new Date() + "', '" + createFutureDate() + "')");

                                //en koppel elke klas met een kans van 15% aan die module.
                                rs = stmt.executeQuery("SELECT klas FROM klassen");
                                while (rs.next()) {
                                    String klas = rs.getString(1);
                                    n = rand.nextInt(100) + 1;
                                    if (n > 0 && n < 16) {
                                        stmt2.executeUpdate("INSERT INTO module_klas (module, klas) VALUES ('" + moduleCode + "', '" + klas + "')");
                                    }
                                }

                            }
                        } catch (SQLException e) {
                            //e.printStackTrace();
                        }

                        //4.2.1.6 - Commit de transactie
                        try {
                            conn.commit();

                            //4.2.1.7 - Wacht vervolgens 100 milliseconden. Pas deze variabele aan indien het te snel/traag gaat. 
                            Thread.sleep(5);
                        } catch (SQLException e) {
                            //e.printStackTrace();
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        studentNummer3++;
                        thread3count++;
                    }
                    double average = totalTime / (double) 600;
                    System.out.println(totalTime + "----" + average);
                }

            }, "Thread 3").start();

        }
    }

    protected static String createCode() {
        String a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String code = "";
        for (int i = 0; i < 5; i++) {
            Random rand = new Random();
            int n = rand.nextInt(26);
            char letter = a.charAt(n);
            code = code + letter;
        }
        return code;
    }

    protected static Date createFutureDate() {
        long toekomst = System.currentTimeMillis() + (86400 * 7 * 1000);
        return new Date(toekomst);
    }

    protected static String randomNameGenerator() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}
