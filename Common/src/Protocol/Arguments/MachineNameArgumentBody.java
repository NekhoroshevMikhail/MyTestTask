package Protocol.Arguments;

import Protocol.Enums.TaskPacketArgumentType;
import Protocol.Interfaces.IPacketArgument;

import java.nio.charset.StandardCharsets;

/**
 * Created by nekho on 01-Oct-16.
 */
public class MachineNameArgumentBody implements IPacketArgument{
    private String _name;

    @Override
    public byte GetArgumentType() {
        return TaskPacketArgumentType.MachineNameArgument;
    }

    public MachineNameArgumentBody() {
    }

    public MachineNameArgumentBody(String name) {
        _name = name;
    }

    @Override
    public byte[] Serialize() {
        return _name.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void Parse(byte[] data, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(data, offset, result, 0, length);

        _name = new String(result, StandardCharsets.UTF_8);
    }
}
