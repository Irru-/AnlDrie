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
import java.util.Random;

/**
 *
 * @author Nick
 */
public class experiment {

    protected static Statement stmt = null;
    protected static Connection conn = null;
    protected static ResultSet rs = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        try {
            //4.2.1.1 - Start een transactie
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Opdracht1", "anl", "anl");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        try {

            //4.2.1.4 - Koppel de student aan een willekeurige klas,
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT klas FROM klassen OFFSET random() * (SELECT COUNT(*) FROM klassen) LIMIT 1");
            String s = "";

            while (rs.next()) {
                s = rs.getString(1);
            }

            System.out.println(s);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        Random rand = new Random();
        
        for (int i = 0; i < 100; i++)
        {
            System.out.println(rand.nextInt(100) + 1);
        }
        
    }

}
