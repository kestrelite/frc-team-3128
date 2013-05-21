/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ownstory;

import Character.Person;

/**
 *
 * @author Kian
 */
public class OwnStory {
    static Person kian = new Person();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       kian.height = 69;
        System.out.println(kian.heightInCm()+"\n"+kian.heightInInches()+"\n"+kian.heightInFeet());
       
    }
}
