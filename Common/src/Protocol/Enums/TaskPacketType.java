package Protocol.Enums;

import com.sun.org.apache.bcel.internal.classfile.Unknown;

/**
 * Created by nekho on 01-Oct-16.
 */
public class TaskPacketType {
    public static final byte NewTask = 0x01;
    public static final byte NewTaskAck = 0x02;
    public static final byte TaskCompleted = 0x03;
    public static final byte TaskCompletedAck = 0x04;
    public static final byte TaskError = 0x05;
    public static final byte TaskErrorAck = 0x06;
    public static final byte Unknown = 0x07;
}
