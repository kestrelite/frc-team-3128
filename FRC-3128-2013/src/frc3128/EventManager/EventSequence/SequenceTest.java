package frc3128.EventManager.EventSequence;

class AutoEvent1 extends SequenceEvent {
    public boolean exitConditionMet() {
        if (this.getRunTimeMillis() > 1000) return true;
        return false;
    }

    public void execute() {
        System.out.println("Hello from 1");
    }
}

class AutoEvent2 extends SequenceEvent {
    public boolean exitConditionMet() {
        if(this.getRunTimeMillis() > 1000) return true;
        return false;
    }

    public void execute() {
        System.out.println("Hello from 2");
    }
}

class AutoEvent3 extends SequenceEvent {
    public boolean exitConditionMet() {
        return true;
    }
    
    public void execute() {
        System.out.println("Hello from 3");
    }
}

public class SequenceTest {
    public EventSequencer as = new EventSequencer();
    public SequenceTest() {
        as.addEvent((new AutoEvent1()));
        as.addEvent((new AutoEvent2()));
        as.addEvent((new AutoEvent3()));
        as.startSequence();
    }
}
