package Transporter;

import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransporterIncorrectStateException;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by nekho on 28-Sep-16.
 */
public class TcpDataTransporter implements IDataTransporter {

    private final int SOCKET_ACCEPT_TIMEOUT = 15000;

    private String _address;
    private int _port;
    private TransporterSide _createdSide;
    private Socket _socket;

    private DataInputStream _inputStream;
    private DataOutputStream _outputStream;
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
                    _inputStream = new DataInputStream(_socket.getInputStream());
                    _outputStream = new DataOutputStream(_socket.getOutputStream());
                    _isConnected = true;
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case Server:
                try{
                    ServerSocket serverSocket = new ServerSocket(_port);

                    serverSocket.setSoTimeout(SOCKET_ACCEPT_TIMEOUT);
                    _socket = serverSocket.accept();
                    _inputStream = new DataInputStream(_socket.getInputStream());
                    _outputStream = new DataOutputStream(_socket.getOutputStream());
                    _isConnected = true;
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
            _inputStream.close();
            _outputStream.close();
            _socket.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        _isConnected = false;
    }

    @Override
    public void SendPacket(byte[] data) throws TransporterIncorrectStateException {
        try {
            if (_outputStream == null) {
                throw new TransporterIncorrectStateException("Can not send data, because transporter has some errors");
            }
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

    public void StartListenIncomingData(){
        Thread listenDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _socket.setSoTimeout(1000);
                } catch (SocketException e) {
                    e.printStackTrace();
                    _isConnected = false;
                }
                while(_isConnected) {
                    byte[] buffer = new byte[1024];
                    int bytesReadedFromStream = 0;
                    try {
                        byte receivedByte;
                        try{
                            while ((receivedByte = _inputStream.readByte()) != -1) {
                                buffer[bytesReadedFromStream] = receivedByte;
                                bytesReadedFromStream++;
                            }
                        }catch (EOFException ex) {
                            ex.printStackTrace();
                        }
                        catch (SocketTimeoutException ex) {
                            ex.printStackTrace();
                        }
                        catch (SocketException ex) {
                            ex.printStackTrace();
                            _isConnected = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }

                    if (bytesReadedFromStream > 0) {
                        byte[] receivedData = new byte[bytesReadedFromStream];
                        System.arraycopy(buffer, 0, receivedData, 0, bytesReadedFromStream);
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
