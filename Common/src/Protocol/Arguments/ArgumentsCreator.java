package Protocol.Arguments;

import Protocol.Enums.TaskPacketArgumentType;
import Protocol.Exceptions.ArgumentNotImplementedException;
import Protocol.Interfaces.IPacketArgument;

/**
 * Created by nekho on 02-Oct-16.
 */
public class ArgumentsCreator {
    public static IPacketArgument CreateArgumentByType(byte argumentType) throws ArgumentNotImplementedException {
        switch (argumentType) {
            case TaskPacketArgumentType.MaximumDurationArgument:
                return new MaximumDurationArgumentBody();
            case TaskPacketArgumentType.MinimumDurationArgument:
                return new MinimumDurationArgumentBody();
            case TaskPacketArgumentType.MachineNameArgument:
                return new MachineNameArgumentBody();

            default:
                throw new ArgumentNotImplementedException("you must implement new type of argument");
        }
    }
}
