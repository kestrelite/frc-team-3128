package frc3128.EventManager.EventSequence;

public abstract class SingleSequence extends SequenceEvent {
	/**
	 * Exits immediately; will always return true.
	 * 
	 * @return true
	 */
    public final boolean exitConditionMet() {return true;}
}