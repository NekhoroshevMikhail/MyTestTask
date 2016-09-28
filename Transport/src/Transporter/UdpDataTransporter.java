package Transporter;

import TransportCommon.IDataTransporter;

/**
 * Created by nekho on 28-Sep-16.
 */
public class UdpDataTransporter implements IDataTransporter {

    private String _address;
    private int _port;

    public UdpDataTransporter(String address, int port) {
        _address = address;
        _port = port;
    }

    @Override
    public Boolean TryConnect() {
        return false;
    }

    @Override
    public void SendPacket(byte[] data) {

    }
}
