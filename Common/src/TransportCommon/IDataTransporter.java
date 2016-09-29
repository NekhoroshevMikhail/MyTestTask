package TransportCommon;

/**
 * Created by nekho on 28-Sep-16.
 */
public interface IDataTransporter {
    void Connect(short port);
    void Disconnect();
    void SendPacket(byte[] data);
    void AddDataReceivedListener(IDataReceivedListener listener);
    void RemoveDataReceivedListener(IDataReceivedListener listener);
}
