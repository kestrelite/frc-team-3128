package frc3128.EventManager.EventSequence;

/**
 *
 * @author Noah Sutton-Smolin
 */
public abstract class TimedSequence extends SequenceEvent {
    private final int killAfter;
    
    /**
     * Creates a TimedSequence event, which is used to create events which kill
     * after a certain amount of time.
     * 
     * @param msec the amount of time the event should run
     */
    public TimedSequence(int msec) {killAfter = msec;}
    
    public boolean exitConditionMet() {return (this.getRunTimeMillis() > killAfter);}
}
