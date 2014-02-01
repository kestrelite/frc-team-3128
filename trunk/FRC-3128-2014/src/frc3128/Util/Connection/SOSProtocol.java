package frc3128.Util.Connection;

/**
 *
 * @author Jamie
 */
public class SOSProtocol {
    static char START_TRANSMISSION = 0x01;
    static char START_ID = 0x05;
    static char END_ID = 0x0A;
    static char START_OPCODE = 0x0D;
    static char END_OPCODE = 0x04;
    static char START_SHORT_TRANSMISSION = 0x09;
    static char END_SHORT = 0x0B;
    static char START_STRING_TRANSMISSION = 0x02;
    static char END_STRING = 0x03;
    static char END_TRANSMISSION = 0x06;
}
