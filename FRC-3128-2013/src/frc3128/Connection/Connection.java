package frc3128.Connection;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc3128.DebugLog;
import frc3128.EventManager.Event;

public class Connection extends Event {   
    public static double distToGoal = 0, xOffset = 0;
    public Connection() {
    }
    public void execute() {
        distToGoal = NetworkTable.getTable("camera").getNumber("distance");
        xOffset = NetworkTable.getTable("camera").getNumber("xoffset");
    }
}