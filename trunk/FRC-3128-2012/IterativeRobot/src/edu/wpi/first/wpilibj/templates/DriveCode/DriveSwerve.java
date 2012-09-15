package edu.wpi.first.wpilibj.templates.DriveCode;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.templates.MotorControl.MotorController;
import edu.wpi.first.wpilibj.templates.PneumaticsManager.PneumaticManager;
import edu.wpi.first.wpilibj.templates.XBoxController.ButtonMap;
import edu.wpi.first.wpilibj.templates.XBoxController.DriveAbstract;
import edu.wpi.first.wpilibj.templates.g;

public class DriveSwerve extends DriveAbstract {

    MotorController mLB, mLBTurn, mRB, mRBTurn, mLF, mLFTurn, mRF, mRFTurn;
    double l = 23.0;
    double w = 19.5;
    //Gyro rotGyro;

    public DriveSwerve(MotorController mLB, MotorController mLBTurn,
            MotorController mRB, MotorController mRBTurn,
            MotorController mLF, MotorController mLFTurn,
            MotorController mRF, MotorController mRFTurn) throws Exception {
        this.mLB = mLB;
        this.mRB = mRB;
        this.mLF = mLF;
        this.mRF = mRF;
        this.mLBTurn = mLBTurn;
        this.mRBTurn = mRBTurn;
        this.mLFTurn = mLFTurn;
        this.mRFTurn = mRFTurn;
        //this.mLBTurn.startIterable();
        this.mLFTurn.startIterable();
        //this.mRBTurn.startIterable();
        this.mRFTurn.startIterable();
    }

    private int detQuad(double x1, double y1) {
        int quadrant = -1;
        if (x1 > 0) {
            if (y1 > 0) {
                quadrant = 1;
            }
            if (y1 < 0) {
                quadrant = 4;
            }
        } else {
            if (y1 > 0) {
                quadrant = 2;
            }
            if (y1 < 0) {
                quadrant = 3;
            }
        }
        return quadrant;
    }

    public double sqr(double x) {
        return MathUtils.pow(x, 2);
    }

    public void buttonPressEnable(int button) {
        //System.out.println("[RBT]: PRESSED: " + button);
        if (button == ButtonMap.X) {
            try {
                PneumaticManager.getSolenoid(0).extend();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if(button == ButtonMap.A) {dir *= -1;}
        
        if (button == ButtonMap.B) {
            g.rotGyro.reset();
        }

        if (button == ButtonMap.A) {
            if (this.lockWheels) {
                this.unlockWheels();
            } else {
                this.lockWheels();
                //this.mLBTurn.setTargetAngle(45);
                //this.mLFTurn.setTargetAngle(-45);
                 //this.mRBTurn.setTargetAngle(-45);
                //this.mRFTurn.setTargetAngle(45);
            }
        }
        if (button == ButtonMap.RB) {
            try {
                PneumaticManager.getSolenoid(1).retract();
                //g.catapultTimer.enableTargetTime(CatapultHandler.determineTime());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void lockWheels() {
        lockWheels = true;
        System.out.println("Wheels LOCKED");
    }

    private void unlockWheels() {
        lockWheels = false;
        System.out.println("Wheels UNLOCKED");
    }

    public void buttonPressDisable(int button) {
        //System.out.println("[RBT]: RELEASED: " + button);
        if (button == ButtonMap.X) {
            try {
                PneumaticManager.getSolenoid(0).retract();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (button == ButtonMap.RB) {
            try {
                PneumaticManager.getSolenoid(1).extend();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    private boolean lockWheels = false;

    public void resetAll() {
        this.mLB.set(0);
        this.mRB.set(0);
        this.mLF.set(0);
        this.mRF.set(0);
        //this.mLBTurn.setTargetAngle(0);
        //this.mRBTurn.setTargetAngle(0);
        //this.mLFTurn.setTargetAngle(0);
        //this.mRFTurn.setTargetAngle(0);
        //this.mBAD1.set(0);
        //this.mBAD2.set(0);

        System.out.println("[RBT]: AllMotorsReset");
    }

    /*
     * public void driveHandler(double x1, double y1, double triggers, double
     * x2, double y2) { //System.out.println("[RBT]: Y1: " + y1);
     * //System.out.println("[RBT]: dH:rotGyroAngle: " + g.rotGyro.getAngle());
     * double fwd = -y1; double str = x1; double rcw = x2;
     *
     * //for(int i = 0; i < 50; i++) // System.out.println("Enable gyro
     * rotation!");
     *
     * //double temp = fwd * Math.cos(g.rotGyro.getAngle()) + str *
     * Math.sin(g.rotGyro.getAngle()); //str = -1 * fwd *
     * Math.sin(g.rotGyro.getAngle()) + str * Math.cos(g.rotGyro.getAngle());
     * //fwd = temp;
     *
     * double r, ax, bx, cx, dx, ws1, ws2, ws3, ws4, wa1, wa2, wa3, wa4, wl1,
     * wl2, wl3, wl4, max; r = Math.sqrt(sqr(l) + sqr(w)); ax = str - rcw * (l /
     * r); bx = str + rcw * (l / r); cx = fwd - rcw * (w / r); dx = fwd + rcw *
     * (w / r); ws1 = Math.sqrt(sqr(bx) + sqr(cx)); ws2 = Math.sqrt(sqr(bx) +
     * sqr(dx)); ws3 = Math.sqrt(sqr(ax) + sqr(dx)); ws4 = Math.sqrt(sqr(ax) +
     * sqr(cx)); wa1 = MathUtils.atan2(bx, cx) * 180 / Math.PI; wa2 =
     * MathUtils.atan2(bx, dx) * 180 / Math.PI; wa3 = MathUtils.atan2(ax, dx) *
     * 180 / Math.PI; wa4 = MathUtils.atan2(ax, cx) * 180 / Math.PI; wl1 =
     * mRFTurn.getEncVal(); wl2 = mLFTurn.getEncVal(); wl3 =
     * mLBTurn.getEncVal(); wl4 = mRBTurn.getEncVal(); { double d1 =
     * g.normalize(wl1 - wa1); if (Math.abs(d1) > 90) { wa1 = g.normalize(wa1 +
     * 180); ws1 *= -1; } } { double d1 = g.normalize(wl2 - wa2); if
     * (Math.abs(d1) > 90) { wa2 = g.normalize(wa2 + 180); ws2 *= -1; } } {
     * double d1 = g.normalize(wl3 - wa3); if (Math.abs(d1) > 90) { wa3 =
     * g.normalize(wa3 + 180); ws3 *= -1; } } { double d1 = g.normalize(wl4 -
     * wa4); if (Math.abs(d1) > 90) { wa4 = g.normalize(wa4 + 180); ws4 *= -1; }
     * }
     *
     * max = Math.abs(ws1); if (Math.abs(ws2) > max) { max = Math.abs(ws2); } if
     * (Math.abs(ws3) > max) { max = Math.abs(ws3); } if (Math.abs(ws4) > max) {
     * max = Math.abs(ws4); } if (max > 1) { ws1 /= max; ws2 /= max; ws3 /= max;
     * ws4 /= max; }
     *
     * if (!lockWheels) { //mLF.set(ws2); //mRF.set(ws1); //mLB.set(ws3);
     * //mRB.set(ws4); //System.out.println("mLF: " + g.round(ws2, 2) + ",
     * \tmRF: " + g.round(ws1, 2) + ", \tmLB: " + g.round(ws3, 2) + ", \tmRB: "
     * + g.round(ws4, 2)); //System.out.println("mLFA: " + g.round(wa2, 2) + ",
     * \tmRFA: " + g.round(wa1, 2) + ", \tmLBA: " + g.round(wa3, 2) + ", \tmRBA:
     * " + g.round(wa4, 2)); mLFTurn.setTargetAngle(wa2);
     * //mRFTurn.setTargetAngle(wa1); //mLBTurn.setTargetAngle(wa3);
     * //mRBTurn.setTargetAngle(wa4); System.out.println("Target: " + wa2 + ",
     * Actual: " + mLFTurn.getEncVal()); } else { mLF.set(0); mRF.set(0);
     * mLB.set(0); mRB.set(0); } }
     */
    static double thresh = .1;

    public double thresholdX(double x1, double y1) {
        double d = Math.sqrt(sqr(x1) + sqr(y1));
        if (d > thresh) {
            return x1;
        }
        return 0;
    }

    public double thresholdY(double x1, double y1) {
        double d = Math.sqrt(sqr(x1) + sqr(y1));
        if (d > thresh) {
            return x1;
        }
        return -.001;
    }

    private double dir = 1.0;
    
    public void driveHandler(double x1, double y1, double triggers, double x2, double y2) {
        //System.out.println("[RBT]: Y1: " + y1);
        //System.out.println("[RBT]: dH:rotGyroAngle: " + g.rotGyro.getAngle());
        //System.out.println("\n\nx1: " + round(x1,2) + ", y1: " + y1);
        //System.out.println("X1: " + x1 + ", Y1: " + y1 + ", X2: " + x2);
        x1 = thresholdX(x1, y1);
        y1 = thresholdY(y1, x1);
        if (Math.abs(x2) < thresh) {
            x2 = 0;
        }
        //System.out.println("X1: " + x1 + ", Y1: " + y1 + ", X2: " + x2);
        System.out.println(g.rotGyro.getAngle());
        //System.out.println("x1: " + round(x1,2) + ", y1: " + y1);
        double fwd = -y1;
        double str = x1;
        double rcw = x2;

        //for(int i = 0; i < 50; i++)
        //    System.out.println("Enable gyro rotation!");

        
        //FIELD ORIENTATION
        //double temp = fwd * Math.cos(g.rotGyro.getAngle()) + str * Math.sin(g.rotGyro.getAngle());
        //str = -1 * fwd * Math.sin(g.rotGyro.getAngle()) + str * Math.cos(g.rotGyro.getAngle());
        //fwd = temp;

        double r, ax, bx, cx, dx, ws1, ws2, ws3, ws4, wa1, wa2, wa3, wa4, max;
        r = Math.sqrt(sqr(l) + sqr(w));
        ax = str - rcw * (l / r);
        bx = str + rcw * (l / r);
        //System.out.println("fwd: " + fwd + ", oth: " + (rcw*(w/r)));
        cx = fwd - rcw * (w / r);
        dx = fwd + rcw * (w / r);
        //System.out.println("bx: " + bx + ", cx: " + cx);
        ws1 = Math.sqrt(sqr(bx) + sqr(cx));
        ws2 = Math.sqrt(sqr(bx) + sqr(dx));
        ws3 = Math.sqrt(sqr(ax) + sqr(dx));
        ws4 = Math.sqrt(sqr(ax) + sqr(cx));
        wa1 = MathUtils.atan2(bx, cx) * 180 / Math.PI;
        wa2 = MathUtils.atan2(bx, dx) * 180 / Math.PI;
        wa3 = MathUtils.atan2(ax, dx) * 180 / Math.PI;
        wa4 = MathUtils.atan2(ax, cx) * 180 / Math.PI;
        max = Math.abs(ws1);
        if (Math.abs(ws2) > max) {
            max = Math.abs(ws2);
        }
        if (Math.abs(ws3) > max) {
            max = Math.abs(ws3);
        }
        if (Math.abs(ws4) > max) {
            max = Math.abs(ws4);
        }
        if (max > 1) {
            ws1 /= max;
            ws2 /= max;
            ws3 /= max;
            ws4 /= max;
        }
        //System.out.println("A1: " + round(wa1, 2) + ", A2: " + round(wa2, 2) + ", A3: " + round(wa3, 2) + ", A4: " + round(wa4, 2));
        //System.out.println("P1: " + round(ws1, 2) + ", P2: " + round(ws2, 2) + ", P3: " + round(ws3, 2) + ", P4: " + round(ws4, 2));
        //if (!lockWheels) {
            
            mLF.set(ws2*dir);
            mRF.set(ws1*dir);
            mLB.set(ws3*dir);
            mRB.set(ws4*dir);
            mLFTurn.setTargetAngle(-wa2);
            mRFTurn.setTargetAngle(-wa1);
            //mLBTurn.setTargetAngle(-wa3); //Comment for car
            //mRBTurn.setTargetAngle(-wa4); //Comment for car
            
            System.out.println("Current1: " + mRFTurn.getEncVal() + ", Expected1: " + -wa1);
            System.out.println("Current2: " + mLFTurn.getEncVal() + ", Expected2: " + -wa2);
            System.out.println("Current3: " + mLBTurn.getEncVal() + ", Expected3: " + -wa3);
            System.out.println("Current4: " + mRBTurn.getEncVal() + ", Expected4: " + -wa4);
        //} else {
        //    mLF.set(0);
        //    mRF.set(0);
        //    mLB.set(0);
        //    mRB.set(0);
        //    mLFTurn.setTargetAngle(-45);
        //    mRFTurn.setTargetAngle(45);
        //    mLBTurn.setTargetAngle(45);
            //mRBTurn.setTargetAngle(-45);
        //}
    }

    private double round(double a, int places) {
        return MathUtils.pow(10, places) * Math.floor((a / MathUtils.pow(10, places)) + .5);
    }

    public void releaseLocks() {
    }

    public void resetDrive() {
        this.mLB.set(0);
        this.mRB.set(0);
        this.mLF.set(0);
        this.mRF.set(0);
        //this.mBAD1.set(0);
        //this.mBAD2.set(0);
        //this.mLBTurn.setTargetAngle(0); this.mRBTurn.setTargetAngle(0); this.mLFTurn.setTargetAngle(0);
        //this.mRFTurn.setTargetAngle(0);

        System.out.println("[RBT]: ResetDrive");
    }

    public boolean isMoving() {
        double total = Math.abs(mLB.get()) + Math.abs(mLF.get()) + Math.abs(mRB.get()) + Math.abs(mRF.get());
        //System.out.println(total/4);
        if (total / 4 > .06) {
            return true;
        }
        return false;
    }

    public void driveHandlerTest(double x1, double y1, double triggers, double x2, double y2) {
        mRFTurn.setTargetAngle(90);
        mRBTurn.setTargetAngle(90);
        mLFTurn.setTargetAngle(90);
        mLBTurn.setTargetAngle(90);
        //System.out.println(mRFTurn.getEncVal());
    }
}