/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Character;

/**
 *
 * @author Kian
 */
public class Person {
    public String name;
    
    boolean gender; // false = female; true = male;
    
    public int age;
    
    public float BMI;
    
    public String eyeColor, hairColor, hairType, favoriteCharacter, skinColor, ethnicity, generalPersonality,
                            favoriteHobbie, favoriteBook, favoriteMovie, favoriteArtistf, favoriteArtism;
    
    public String hometown, currentLiving, currentJob, previousJob, biggestGoal, workingStatus, positionAtWorkit;
    
    public String[] loveintrest, friend, bestFriend, enemies, family, frenemies, averageMood; 
    
    
    public int weight = 0;
    public int height = 0;
    private boolean weightset = false;
    private boolean heightset = false;
    
    public String want;
    
    public Person(){
    }
    
    public String weightInLbs(){
        return weight+"lbs";
    }
    
    public String weightInKilo(){
        return (2.2*weight)+"kg";
    }
    
    public String heightInCm(){
        return (2.54*height)+"cm";
    }
    
    public void setBMI(){
        if(BMICheck()) BMI = (float)((weight * 703.0)/(height*height));
        //else *if one exists for women only
    }
    
    private boolean BMICheck(){
        if(!weightset&&!weightset) throw new Error("Set a proper Weight and Height of "+name+"!");
        else{
            if(!weightset) throw new Error("Set proper Weight of "+name+"!");
            if(!heightset) throw new Error("Set proper Height of "+name+"!");
            if(heightset&&weightset)return true;
            else return false;
        }
    }
    
    public void setWeight(int w){
        if(weight <= 0) weightset = false;
        else{
             weight = w;
             weightset = true;
        }
    }
    
    public void setHeight(int h){
        if(weight <= 0) weightset = false;
        else{
            height = h;
            heightset = true;
        }
    }
    
    public String heightInInches(){
        return height+" inches";
    }
    
    public String heightInFeet(){
        if(height%12 == 0)return (height/12)+"'";
        else return (height/12)+"' "+(height%12)+"\"";
        
    }
    
    
    
}
