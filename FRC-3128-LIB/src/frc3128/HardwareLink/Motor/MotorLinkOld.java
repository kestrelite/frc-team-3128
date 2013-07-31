package frc3128.HardwareLink.Motor;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;

public class MotorLinkOld {
    
    
    public Jaguar mot;
    public AnalogChannel enc;
    
    public MotorLinkOld(Jaguar a){
        mot = a;  
    }
    
    public MotorLinkOld(Jaguar a, AnalogChannel b){
        mot = a;
        enc = b;   
    }
    
    public double getAngle() {
        double voltage = 0, value = 0;
        for(char i = 0; i<10; i++) {
            voltage += enc.getVoltage();
            value += enc.getValue();
        }
        voltage /= 10; value /= 10;
        return voltage/5.0*360;
    }
    
    public double setAngle(double deg){
        double larger, smaller, diff;
        boolean positive;
        
        if(deg-getAngle()>getAngle()-deg){
            larger = deg-getAngle();
            smaller = getAngle()-deg;
            positive = true;
        }
        else if(deg-getAngle()<getAngle()-deg){
            smaller = deg-getAngle();
            larger = getAngle()-deg;
            positive = false;
        }
        else {
            return 0;
        }
        
        diff = larger-smaller;
        
        if(positive){
            mot.set(diff<180?(1):(-1));
        }
        else{
            mot.set(diff<180?(-1):(1));
        }
        return diff;
    }
}