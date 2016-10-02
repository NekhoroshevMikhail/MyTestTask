package Transporter;

import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransporterIncorrectStateException;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;

/**
 * Created by nekho on 02-Oct-16.
 */
public class ServerSideTransporterMock implements IDataTransporter {
    @Override
    public Boolean TryConnect() throws SideOfTransporterNotRealizedException {
        return null;
    }

    @Override
    public void Disconnect() {

    }

    @Override
    public void StartListenIncomingData() {

    }

    @Override
    public void SendPacket(byte[] data) throws TransporterIncorrectStateException {

    }

    @Override
    public void AddDataReceivedListener(IDataReceivedListener listener) {

    }

    @Override
    public void RemoveDataReceivedListener(IDataReceivedListener listener) {

    }

    @Override
    public Boolean IsConnected() {
        return null;
    }
}
