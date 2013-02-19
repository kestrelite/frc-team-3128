package frc3128.AutoSequencer;

class AutoEvent1 extends AutoEvent {
    public boolean exitConditionMet() {
        if (this.getRunTimeMillis() > 1000) return true;
        return false;
    }

    public void execute() {
        System.out.println("Hello from 1");
    }
}

class AutoEvent2 extends AutoEvent {
    public boolean exitConditionMet() {
        if(this.getRunTimeMillis() > 1000) return true;
        return false;
    }

    public void execute() {
        System.out.println("Hello from 2");
    }
}

class AutoEvent3 extends AutoEvent {
    public boolean exitConditionMet() {
        return true;
    }
    
    public void execute() {
        System.out.println("Hello from 3");
    }
}

public class AutonomousTest {
    public AutoSequencer as = new AutoSequencer();
    public AutonomousTest() {
        as.addEvent((new AutoEvent1()));
        as.addEvent((new AutoEvent2()));
        as.addEvent((new AutoEvent3()));
        as.startAutonomous();
    }
}
