/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opdracht_drie;

import java.util.Random;

/**
 *
 * @author Nick
 */
public class asdads {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Random rand = new Random();
        char c = (char) (rand.nextInt(26) + 'A');
        System.out.println(c);
    }

}
