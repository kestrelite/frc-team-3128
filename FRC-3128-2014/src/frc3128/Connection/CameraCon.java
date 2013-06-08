package frc3128.Connection;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc3128.DebugLog;
import frc3128.EventManager.Event;

public class CameraCon extends Event {
    public static double distToGoal = 0, xOffset = 0;
    public static boolean stateConnected = false;

    public void execute() {
        DebugLog.log(5, referenceName, "dTG:" + distToGoal + ", xOff:" + xOffset);
        if (NetworkTable.getTable("camera").getBoolean("found")) {
            distToGoal = NetworkTable.getTable("camera").getNumber("distance");
            xOffset = NetworkTable.getTable("camera").getNumber("xoffset");
            stateConnected = true;
        } else stateConnected = false;
    }
}