package frc3128.Util.Connection;

/**
 * Constants for the robot command protocol.
 * 
 * A protocol description is available in the server
 * and client sources.
 * 
 * @author Jamie
 */
public class SOSProtocol {
    static byte START_TRANSMISSION = 0x01;
    static byte START_ID = 0x05;
    static byte END_ID = 0x0A;
    static byte START_OPCODE = 0x0D;
    static byte END_OPCODE = 0x04;
    static byte START_PARAM = 0x09;
    static byte END_PARAM = 0x0B;
    static byte START_STRING = 0x02;
    static byte END_STRING = 0x03;
    static byte END_TRANSMISSION = 0x06;
}
