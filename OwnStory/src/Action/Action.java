/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import Character.Person;

/**
 *
 * @author Kian
 */
public class Action {
    Person[] subjects;
    
    String verb;
    String intent;
    
    char tense;             //Past == -1; Present == 0; Future == 1
    
    public Action(Person instigator, Person reciever){
        subjects[0] = instigator; subjects[1] = reciever;
    }
}
