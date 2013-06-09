package frc3128.PneumaticsManager;

public class PistonID {
    private int        value = -1;
    private boolean inverted = false;
    
    protected PistonID(int value) {this.value = value;}
    public void flipPiston() {this.inverted = !this.inverted;}

    protected int     getID()        {return this.value;}
    protected boolean getInversion() {return this.inverted;}
}