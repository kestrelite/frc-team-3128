package edu.wpi.first.wpilibj.templates.PneumaticsManager;

public class PistonID {
    private int value = -1;

    protected PistonID(int value) {this.value = value;}
    
    protected int getID() {
        return this.value;
    }
}
