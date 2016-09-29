package Transporter;

import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

/**
 * Created by nekho on 28-Sep-16.
 */
public class UdpDataTransporter implements IDataTransporter {

    private String _address;
    private int _port;
    private TransporterSide _createdSide;

    public UdpDataTransporter(String address, int port, TransporterSide whereTransporterCreated) {
        _address = address;
        _port = port;
        _createdSide = whereTransporterCreated;
    }


    @Override
    public void Connect(short port) {

    }

    @Override
    public void Disconnect() {

    }

    @Override
    public void SendPacket(byte[] data) {

    }

    @Override
    public void AddDataReceivedListener(IDataReceivedListener listener) {

    }

    @Override
    public void RemoveDataReceivedListener(IDataReceivedListener listener) {

    }
}
