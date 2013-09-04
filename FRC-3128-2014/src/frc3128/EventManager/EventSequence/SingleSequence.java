package frc3128.EventManager.EventSequence;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public abstract class SingleSequence extends SequenceEvent {
    /**
     * Exits immediately; will always return true.
     * 
     * @return true
     */
    public final boolean exitConditionMet() {return true;}
}