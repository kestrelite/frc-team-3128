/*import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.IOException;
import java.util.Scanner;


public class CRIOTEST {
	public static void updateMouse() {b = MouseInfo.getPointerInfo().getLocation();}
	static Point b;
	static PointerInfo ptr = MouseInfo.getPointerInfo();
	
	public static double round(double d, int pl) {return ((double)Math.round(d*Math.pow(10, pl)))/Math.pow(10, pl);}
	
	public static double sqr(double d) {return Math.pow(d,2);}
	
    //double l = 23.0;
    //double w = 19.5;
	static double l = 25;
	static double w = 25;
	static double thresh = .2;
	public static double thresholdX(double x1, double y1)
	{
		double d = Math.sqrt(sqr(x1) + sqr(y1));
		if(d > thresh) return x1;
		return 0;
	}
	
	public static double thresholdY(double x1, double y1)
	{
		double d = Math.sqrt(sqr(x1) + sqr(y1));
		if(d > thresh) return x1;
		return -.001;
	}
	
	public static double gangle = 0;
	
	public static void driveHandler(double x1, double y1, double triggers, double x2, double y2) {
        //System.out.println("[RBT]: Y1: " + y1);
        //System.out.println("[RBT]: dH:rotGyroAngle: " + g.rotGyro.getAngle());
		//System.out.println("\n\nx1: " + round(x1,2) + ", y1: " + y1);
		x1 = thresholdX(x1, y1);
		y1 = thresholdY(y1, x1);
		if(x2 < thresh) x2 = 0;
		
		//System.out.println("x1: " + round(x1,2) + ", y1: " + y1);
        double fwd = -y1;
        double str = x1;
        double rcw = x2;

        //for(int i = 0; i < 50; i++)
        //    System.out.println("Enable gyro rotation!");
        
        double temp = fwd * Math.cos(gangle) + str * Math.sin(gangle);
        str = -1 * fwd * Math.sin(gangle) + str * Math.cos(gangle);
        fwd = temp;

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
        wa1 = Math.atan2(bx, cx) * 180 / Math.PI;
        wa2 = Math.atan2(bx, dx) * 180 / Math.PI;
        wa3 = Math.atan2(ax, dx) * 180 / Math.PI;
        wa4 = Math.atan2(ax, cx) * 180 / Math.PI;
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
        System.out.println("A1: " + round(wa1,2) + ", A2: " + round(wa2,2) + ", A3: " + round(wa3,2) + ", A4: " + round(wa4,2));
        System.out.println("P1: " + round(ws1,2) + ", P2: " + round(ws2,2) + ", P3: " + round(ws3,2) + ", P4: " + round(ws4,2));
        //if (!lockWheels) {
            //mLF.set(ws2);
            //mRF.set(ws1);
            //mLB.set(ws3);
            //mRB.set(ws4);
            //System.out.println("mLF: " + g.round(ws2, 2) + ", \tmRF: " + g.round(ws1, 2) + ", \tmLB: " + g.round(ws3, 2) + ", \tmRB: " + g.round(ws4, 2));
            //System.out.println("mLFA: " + g.round(wa2, 2) + ", \tmRFA: " + g.round(wa1, 2) + ", \tmLBA: " + g.round(wa3, 2) + ", \tmRBA: " + g.round(wa4, 2));
            //mLFTurn.setTargetAngle(wa2);
            //mRFTurn.setTargetAngle(wa1);
            //mLBTurn.setTargetAngle(wa3);
            //mRBTurn.setTargetAngle(wa4);
        //    System.out.println("Target: " + wa2 + ", Actual: " + mLFTurn.getEncVal());
        //} else {
            //mLF.set(0);
            //mRF.set(0);
            //mLB.set(0);
            //mRB.set(0);
        //}
    }
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		
		System.out.println("Bottom left, press enter");
		System.in.read();
		updateMouse();
		double maxx = b.getX() + 1.0;
		double maxy = b.getY() + 1.0;
		System.out.println("X: " + maxx + ", Y: " + maxy);
		int midx = (int)(maxx/2);
		int midy = (int)(maxy/2);
		System.out.println("MidX: " + midx + ", MidY: " + midy);
		//Thread.sleep(3000);
		
		Scanner s = new Scanner(System.in);
		
		while(true)
		{
			System.out.print("\nRotation? ");
			double rot = Double.parseDouble(s.next());
			System.out.print("Gyroscop? ");
			gangle = Double.parseDouble(s.next());
			System.out.println();
			
			updateMouse();
			double x = (b.getX()-midx)/midx;
			double y = (b.getY()-midy)/midy;
			System.out.println("x " + round(x,3) + ", y " + round(y,3));
			driveHandler(x, y, 0, rot, 0);
			//Thread.sleep(1250);
		}
	}
}
*/