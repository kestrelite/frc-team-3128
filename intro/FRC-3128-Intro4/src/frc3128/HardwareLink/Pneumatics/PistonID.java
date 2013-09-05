package frc3128.HardwareLink.Pneumatics;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class PistonID {
    private int     value = -1;
    private boolean inverted = false;
    
    protected PistonID(int value) {this.value = value;}
	
	/**
	 * Inverts the piston internally.
	 */
    public void invertPiston() {this.inverted = !this.inverted;}

	/**
	 * 
	 * @return the indexing ID of the piston
	 */
    protected int getID() {return this.value;}
	
	/**
	 * 
	 * @return whether or not the current piston is inverted
	 */
    protected boolean getInversion() {return this.inverted;}
}