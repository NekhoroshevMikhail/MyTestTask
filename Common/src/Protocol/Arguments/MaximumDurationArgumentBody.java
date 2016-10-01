package Protocol.Arguments;

import Protocol.Enums.TaskPacketArgumentType;
import Protocol.Interfaces.IPacketArgument;

import java.nio.ByteBuffer;

/**
 * Created by nekho on 01-Oct-16.
 */
public class MaximumDurationArgumentBody implements IPacketArgument {
    private int _maximumDuration;
    @Override
    public byte GetArgumentType() {
        return TaskPacketArgumentType.MaximumDurationArgument;
    }

    public MaximumDurationArgumentBody() {
    }

    public MaximumDurationArgumentBody(int maximumDuration) {
        _maximumDuration = maximumDuration;
    }

    @Override
    public byte[] Serialize() {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(_maximumDuration);
        return b.array();
    }

    @Override
    public void Parse(byte[] data, int offset, int length) {
        byte[] valueBytes = new byte[4];
        System.arraycopy(data, offset, valueBytes, 0, 4);
        ByteBuffer b = ByteBuffer.wrap(valueBytes);
        _maximumDuration = b.getInt();
    }
}
