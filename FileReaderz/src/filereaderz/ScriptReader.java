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
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dreeeeeeeew, Aaaliiiinaaaa, Mayaaaaa, Emmmiiiii, Noooahhhhh, Speeeeeeeeeeeeeeeeeeeeeeeencer <3
 */
public class ScriptReader {

    public static Hashtable variables = new Hashtable();

    public static void load(String filePath) throws FileNotFoundException {
        System.out.println(new File(filePath).getAbsoluteFile());
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath).getAbsoluteFile()));
        System.out.println("Got past the part with the files and stuff");
        String s = null;
        try {
            s = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        while (s != null) {
            System.out.println("Read line");
            String[] split = split(s, ",");

            if (split[0].equalsIgnoreCase("var")) {
                if (split[1].equalsIgnoreCase("double")) {
                    set(split[2], new Double(Double.parseDouble(split[3])));
                } else if (split[1].equalsIgnoreCase("int")) {
                    variables.put(split[2], new Integer(Integer.parseInt(split[3])));
                } else if (split[1].equalsIgnoreCase("String")) {
                    variables.put(split[2], split[3]);
                } else if (split[1].equalsIgnoreCase("boolean")) {
                    variables.put(split[2], Integer.parseInt(split[3]) == 1 ? Boolean.TRUE : Boolean.FALSE);
                } else {
                    Class c = null;
                    try {
                        c = Class.forName(split[1]);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ScriptReader.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Class[] params = new Class[split.length - 3];
                    Object[] objectParams = new Object[split.length - 3];
                    for(int i = 3; i < split.length; i++){
                        if(split[i].startsWith("$")){
                            System.out.println(split[i].substring(1));
                            System.out.println(variables.get(split[i].substring(1)));
                            params[i - 3] = get(split[i].substring(1)).getClass();
                            objectParams[i - 3] = get(split[i].substring(1));
                        }
                    }
                    System.out.println(params[0]);
                    try{
                        System.out.println("This isn't right: "+s);
                        set(split[2], c.getConstructor(params).newInstance(objectParams));
                    }
                    catch(Exception e){
                        
                    }
                    
                }
            } else if (split[0].equalsIgnoreCase("execute")) {
                if (split[1].equalsIgnoreCase("print")) {
                    System.out.println(split[2]);
                } else {
                    System.out.println("split: " +split[1].substring(1));
                    Object o = get(split[1].substring(1));
                    System.out.println(get(split[1].substring(1)));

                    Class[] params = new Class[split.length - 3];
                    Object[] objectParams = new Object[split.length - 3];
                    for(int i = 3; i < split.length; i++){
                        if(split[i].startsWith("$")){
                            System.out.println(split[i].substring(1));
                            System.out.println(variables.get(split[i].substring(1)));
                            params[i - 3] = get(split[i].substring(1)).getClass();
                            objectParams[i - 3] = get(split[i].substring(1));
                        }
                    }
                    System.out.println(params[0]);
                    try{
                        System.out.println("METHOD: " + objectParams[0]);
                        o.getClass().getMethod(split[2], params).invoke(o, objectParams[0]);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("Stored a line");
            try {
                s = reader.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ScriptReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Finished parsing");
    }

    public static Object get(String o) {
        return variables.get(o);
    }

    private static void set(String o, Object v) {
        variables.put(o, v);
    }

    public double add(String v1, String v2) {
        return ((Double) get(v1)).doubleValue() + ((Double) get(v2)).doubleValue();
    }

    //Helpful String Things
    private static String[] split(String str, String r) {
        String s = str;
        int c = 1;
//        while(s.indexOf(",") != -1) {
//            
//            c++;
//            s = s.substring(s.indexOf(","));
//        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',') {
                c++;
            }
        }
        s = str;
        String[] splitString = new String[c];

        for (int i = 0; i < c; i++) {
            if (i == c - 1) {
                splitString[i] = s;
            } else {
                splitString[i] = s.substring(0, s.indexOf(','));
            }
            s = s.substring(s.indexOf(',') + 1);
            System.out.println("In string function: " + s);
        }

        return splitString;

    }
//    private static int getAmt(String str, regex){
//        
//    }
}
