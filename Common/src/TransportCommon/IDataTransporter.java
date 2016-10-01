package TransportCommon;

import Exceptions.SideOfTransporterNotRealizedException;

import java.io.IOException;

/**
 * Created by nekho on 28-Sep-16.
 */
public interface IDataTransporter {
    Boolean TryConnect() throws SideOfTransporterNotRealizedException;
    void Disconnect();
    void StartListenIncomingData();
    void SendPacket(byte[] data);
    void AddDataReceivedListener(IDataReceivedListener listener);
    void RemoveDataReceivedListener(IDataReceivedListener listener);
    Boolean IsConnected();
}
