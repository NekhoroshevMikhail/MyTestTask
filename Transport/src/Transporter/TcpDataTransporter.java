package Transporter;

import Exceptions.SideOfTransporterNotRealizedException;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by nekho on 28-Sep-16.
 */
public class TcpDataTransporter implements IDataTransporter {

    private final int SOCKET_ACCEPT_TIMEOUT = 10000;

    private String _address;
    private int _port;
    private TransporterSide _createdSide;
    private Socket _socket;

    private InputStream _inputStream;
    private OutputStream _outputStream;
    private Boolean _isConnected;
    private ArrayList<IDataReceivedListener> _listeners;

    public TcpDataTransporter(String address, int port, TransporterSide whereTransporterCreated) {
        _address = address;
        _port = port;
        _createdSide = whereTransporterCreated;
        _isConnected = false;
        _listeners = new ArrayList<IDataReceivedListener>();
    }

    public Boolean IsConnected(){
        return _isConnected;
    }

    @Override
    public Boolean TryConnect()
            throws SideOfTransporterNotRealizedException
        {
        switch (_createdSide) {
            case Client:
                try {
                    _socket = new Socket(InetAddress.getByName(_address), _port);
                    _isConnected = true;
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case Server:
                try{
                    ServerSocket serverSocket = new ServerSocket(_port, 1, InetAddress.getByName(_address));
                    serverSocket.setSoTimeout(SOCKET_ACCEPT_TIMEOUT);
                    _socket = serverSocket.accept();
                    _inputStream = _socket.getInputStream();
                    _outputStream = _socket.getOutputStream();
                    _isConnected = true;
                    StartListenData();
                }catch (IOException ex) {
                   ex.printStackTrace();
                }
                break;
            default:
                throw new SideOfTransporterNotRealizedException("You must realize creation of this transporter on new way");
        }
        return _isConnected;
    }

    @Override
    public void Disconnect() {
        try{
            _socket.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        _isConnected = false;
    }

    @Override
    public void SendPacket(byte[] data) {
        try {
            _outputStream.write(data, 0, data.length);
            _outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void AddDataReceivedListener(IDataReceivedListener listener) {
        synchronized (_listeners) {
            if (_listeners != null) {
                _listeners.add(listener);
            }
        }
    }

    @Override
    public void RemoveDataReceivedListener(IDataReceivedListener listener) {
        synchronized (_listeners) {
            if (_listeners != null && _listeners.contains(listener)) {
                _listeners.remove(listener);
            }
        }
    }

    private void StartListenData(){
        Thread listenDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(_isConnected) {
                    int availalbe;
                    try {
                        availalbe = _inputStream.available();
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                    if (availalbe > 0) {
                        byte[] receivedData = new byte[availalbe];
                        try {
                            _inputStream.read(receivedData, 0, receivedData.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                            continue;
                        }
                        synchronized (_listeners) {
                            for (IDataReceivedListener someDataListener: _listeners) {
                                someDataListener.DataReceived(receivedData);
                            }
                        }
                    }
                }
            }
        });
        listenDataThread.start();
    }
}
