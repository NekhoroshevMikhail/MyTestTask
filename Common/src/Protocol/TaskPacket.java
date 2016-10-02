package Protocol;

import Protocol.Arguments.ArgumentsCreator;
import Protocol.Enums.TaskPacketArgumentType;
import Protocol.Enums.TaskPacketType;
import Protocol.Exceptions.ArgumentNotImplementedException;
import Protocol.Interfaces.IPacketArgument;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by nekho on 01-Oct-16.
 */
public class TaskPacket {
    private final int CRC_BYTES_COUNT = 8;
    private ArrayList<IPacketArgument> _arguments;
    private byte _packetType;
    private Boolean _isPacketIncorrect = false;

    public TaskPacket() {
        _arguments = new ArrayList<>();
    }

    public void Parse(byte[] data) {
        int length = data[0];
        _packetType = data[1];
        byte[] checkSumValue = new byte[CRC_BYTES_COUNT];
        System.arraycopy(data, data.length - CRC_BYTES_COUNT, checkSumValue, 0, CRC_BYTES_COUNT);
        byte[] dataForCheckSum = new byte[data.length - CRC_BYTES_COUNT];
        System.arraycopy(data, 0, dataForCheckSum, 0, dataForCheckSum.length);
        if (!CheckCRCCorrect(checkSumValue, dataForCheckSum)) {
            _isPacketIncorrect = true;
            return;
        }

        byte[] argumentsData = new byte[data.length - CRC_BYTES_COUNT - 2/*length byte and packet type byte*/];
        System.arraycopy(data, 2, argumentsData, 0, argumentsData.length);
        int i = 0;
        while( i < argumentsData.length) {
            byte argumentType = argumentsData[i];
            i++;
            byte argumentDataLength = argumentsData[i];
            i++;
            IPacketArgument argument = null;
            try {
                argument = ArgumentsCreator.CreateArgumentByType(argumentType);
            } catch (ArgumentNotImplementedException e) {
                e.printStackTrace();
                _isPacketIncorrect = true;
                return;
            }
            byte[] argumentDataToParse = new byte[argumentDataLength];
            System.arraycopy(argumentsData, i, argumentDataToParse, 0, argumentDataLength);
            argument.Parse(argumentDataToParse, 0, argumentDataToParse.length);
            _arguments.add(argument);
            i = i + argumentDataLength;
        }
    }

    public Boolean IsCorrect(){
        return  !_isPacketIncorrect;
    }

    public byte[] Serialize() {
        ArrayList<Byte> result = new ArrayList<Byte>();
        result.add((byte)0); // length
        result.add(_packetType);
        for (int i = 0; i < _arguments.size(); i++) {
            byte argumentType = _arguments.get(i).GetArgumentType();
            byte[] argumentData = _arguments.get(i).Serialize();
            int argumentBodyLength = argumentData.length;

            result.add(argumentType);
            result.add((byte)argumentBodyLength);

            for (int j = 0; j < argumentData.length; j++) {
                result.add(argumentData[j]);
            }
        }
        result.set(0, (byte) result.size());

        byte[] dataForCheckSum = new byte[result.size()];
        for (int i = 0; i < result.size(); i++){
            dataForCheckSum[i] = result.get(i);
        }

        byte[] checkSum = CalcCrc(dataForCheckSum);
        byte[] resultByteArray = new byte[dataForCheckSum.length + checkSum.length];
        System.arraycopy(dataForCheckSum, 0, resultByteArray, 0 ,dataForCheckSum.length);
        System.arraycopy(checkSum, 0, resultByteArray, dataForCheckSum.length, checkSum.length );
        return resultByteArray;
    }

    public void AddArgument(IPacketArgument argument){
        _arguments.add(argument);
    }

    public ArrayList<IPacketArgument> GetArguments() {
        return _arguments;
    }

    public byte GetPacketType() {
        return _packetType;
    }

    public void SetType(byte newPacketType) {
        _packetType = newPacketType;
    }

    private boolean CheckCRCCorrect(byte[] checkSumValue, byte[] checkSumData) {
        byte[] calculatedCrc = CalcCrc(checkSumData);
        for(int i = 0; i < CRC_BYTES_COUNT; i++) {
            if (checkSumValue[i] != calculatedCrc[i]) {
                return false;
            }
        }
        return true;
    }

    private byte[] CalcCrc(byte[] data) {
        Checksum sum = new CRC32();
        sum.update(data, 0, data.length);
        long checksumValue = sum.getValue();
        ByteBuffer b = ByteBuffer.allocate(CRC_BYTES_COUNT);
        b.putLong(checksumValue);
        return b.array();
    }

}
