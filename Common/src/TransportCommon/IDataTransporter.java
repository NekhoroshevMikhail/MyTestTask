package TransportCommon;

/**
 * Created by nekho on 28-Sep-16.
 */
public interface IDataTransporter {
    Boolean TryConnect();
    void SendPacket(byte[] data);
}
