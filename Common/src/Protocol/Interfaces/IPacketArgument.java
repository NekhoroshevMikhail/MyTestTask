package Protocol.Interfaces;

import Protocol.Enums.TaskPacketArgumentType;

/**
 * Created by nekho on 01-Oct-16.
 */
public interface IPacketArgument {
    byte GetArgumentType();
    byte[] Serialize();
    void Parse(byte[] data, int offset, int length);

}
