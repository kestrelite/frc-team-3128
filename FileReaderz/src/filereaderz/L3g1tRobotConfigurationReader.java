/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filereaderz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robo <3
 */
public class L3g1tRobotConfigurationReader {
    
    private static String filePath = "C:\\Users\\Robo\\Documents\\NetBeansProjects\\FileReaderz\\src\\filereaderz\\config.txt";
    private static HashMap<String, Double> config = new HashMap();
    
    public static void read(){
        File f = new File(filePath);
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(L3g1tRobotConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String s = "";
        
        try {
            s = reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(L3g1tRobotConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(s != null){
            String[] splitLine = s.split(",");
            config.put(splitLine[0], Double.parseDouble(splitLine[1]));
            try {
                s = reader.readLine();
            } catch (IOException ex) {
                Logger.getLogger(L3g1tRobotConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.print(config);
    }
    
    
            
            
    
    
    
    
}
