package Protocol.Arguments;

import Protocol.Enums.TaskPacketArgumentType;
import Protocol.Interfaces.IPacketArgument;

import java.nio.ByteBuffer;

/**
 * Created by nekho on 01-Oct-16.
 */
public class MinimumDurationArgumentBody implements IPacketArgument {
    private int _minimumDuration;
    @Override
    public byte GetArgumentType() {
        return TaskPacketArgumentType.MinimumDurationArgument;
    }

    public int GetMinimumDuration() {
        return _minimumDuration;
    }

    public MinimumDurationArgumentBody() {
    }

    public MinimumDurationArgumentBody(int minimumDuration) {
        _minimumDuration = minimumDuration;
    }

    @Override
    public byte[] Serialize() {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(_minimumDuration);
        return b.array();
    }

    @Override
    public void Parse(byte[] data, int offset, int length) {
        byte[] valueBytes = new byte[4];
        System.arraycopy(data, offset, valueBytes, 0, 4);
        ByteBuffer b = ByteBuffer.wrap(valueBytes);
        _minimumDuration = b.getInt();
    }
}
