package frc3128.EventManager.EventSequence;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class DelaySequence extends SequenceEvent {
    private int msec;
    public DelaySequence(int msec) {this.msec = msec;}
    
    public boolean exitConditionMet() {
        return this.getRunTimeMillis() > msec;
    }

    public void execute() {}
}
