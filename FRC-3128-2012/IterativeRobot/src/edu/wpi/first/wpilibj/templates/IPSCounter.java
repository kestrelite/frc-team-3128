package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;

public class IPSCounter implements EventInterface {

    private int sum = 0;
    private int thisIter = 0;
    private double startTime;
    private double timeDiff;
    private int lastSecond = 0;
    private boolean isLimiter = false;
    private int limitingIps;

    public IPSCounter() {
        startTime = System.currentTimeMillis();
    }

    public IPSCounter(int limitingNumber) {
        startTime = System.currentTimeMillis();
        isLimiter = true;
        limitingIps = limitingNumber;
    }
    private double wait = 1;

    public boolean eventIterable() throws Exception {
        sum++;
        thisIter++;
        timeDiff = ((double) System.currentTimeMillis() - startTime) / 1000;
        if ((int) Math.floor(timeDiff) - lastSecond >= 1) {
            lastSecond = (int) Math.floor(timeDiff);
            if (this.isLimiter) {
                if((double)thisIter/(double)limitingIps > .13) wait *= (double)thisIter / (double)this.limitingIps;
                //System.out.println("[IPSCounter] This: " + thisIter + ", wait: " + wait);
            }
            thisIter = 0;
        }
        Thread.sleep((int) Math.floor(wait));
        return true;
    }

    public void eventProcessor() throws Exception {
    }

    public void eventRegisterSelf() throws Exception {
    }
}
