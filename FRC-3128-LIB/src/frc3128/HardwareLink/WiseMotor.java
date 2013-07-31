package frc3128.HardwareLink;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
/**
 *
 * @author Kian
 */
public class WiseMotor {
    
    
    public Jaguar mot;
    public AnalogChannel enc;
    
    public WiseMotor(Jaguar a){
        mot = a;  
    }
    
    public WiseMotor(Jaguar a, AnalogChannel b){
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
        if(deg-getAngle()>getAngle()-deg){
            if(360+(getAngle()-deg)<deg-getAngle()){
                mot.set((360+(getAngle()-deg)));
            }
        }
        return deg;
    }
}