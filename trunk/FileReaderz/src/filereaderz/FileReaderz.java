
package filereaderz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robo
 */
public class FileReaderz {

    private static BufferedReader fr;
    private static ArrayList<String> fileStringThing = new ArrayList<String>();
    
    public static void main(String[] args){
        System.out.println("Main started!");
        try {
            //       String[] output = split("hello,my,name,is,blue", ',');
            //       for(int i = 0; i < output.length; i++){
            //           System.out.println(output[i]);
            //       }
            System.out.println("About to load!");
                    ScriptReader.load("C:\\Users\\Robo\\Documents\\NetBeansProjects\\FileReaderz\\src\\filereaderz\\config.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileReaderz.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Loaded!");
        System.out.println(ScriptReader.variables.get("mars"));
        System.out.println(ScriptReader.variables.get("hashtag"));
        System.out.println(((Double)ScriptReader.variables.get("mars")).doubleValue() + ((Double)ScriptReader.variables.get("hashtag")).doubleValue());
    }
    
    
    private static String[] split(String str, char r){
        boolean done = false;
        String s = str;
        int c = 0;
//        while(s.indexOf(r) != -1) {
//            c++;
//            s = s.substring(s.indexOf(r));
//        }
        for(int i = 0; i < str.length(); i++){
            if(s.charAt(i) == r)
                c++;
        }
        String[] splitString = new String[c];
        
        for(int i = 0; i < c; i++){
            splitString[i] = s.substring(s.indexOf(r)).replaceAll(",", "");
            s = s.substring(0, s.indexOf(r));
        }
        
        return splitString;
        
    }
}
