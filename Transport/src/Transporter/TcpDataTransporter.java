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
    private int _id;
    private static int _storedId = 0;
    private Object _lockObject;

    public TcpDataTransporter(String address, int port, TransporterSide whereTransporterCreated) {
        _lockObject = new Object();
        _address = address;
        _port = port;
        _createdSide = whereTransporterCreated;
        _isConnected = false;
        _listeners = new ArrayList<IDataReceivedListener>();
        _id = _storedId;
        _storedId++;
    }

    private void Log(String message)
    {
        //System.out.println("TcpTR"+_id +" " + message);
    }

    public Boolean IsConnected(){
        return _isConnected;
    }

    @Override
    public Boolean TryConnect()
            throws SideOfTransporterNotRealizedException
        {
        Log("TryConnect");
        switch (_createdSide) {
            case Client:
                try {
                    Log("case Client:");
                    _socket = new Socket(InetAddress.getByName(_address), _port);
                    _inputStream = new DataInputStream(_socket.getInputStream());
                    _outputStream = new DataOutputStream(_socket.getOutputStream());
                    _isConnected = true;
                }catch (IOException ex) {
                    Log("IOException ex");
                    ex.printStackTrace();
                }
                break;
            case Server:
                try{
                    Log("case Server:");
                    ServerSocket serverSocket = new ServerSocket(_port);

                    serverSocket.setSoTimeout(SOCKET_ACCEPT_TIMEOUT);
                    _socket = serverSocket.accept();
                    _inputStream = new DataInputStream(_socket.getInputStream());
                    _outputStream = new DataOutputStream(_socket.getOutputStream());
                    _isConnected = true;
                }catch (IOException ex) {
                    Log("IOException ex");
                   ex.printStackTrace();
                }
                break;
            default:
                throw new SideOfTransporterNotRealizedException("You must realize creation of this transporter on new way");
        }
        Log("TryConnect finish");
        return _isConnected;
    }

    @Override
    public void Disconnect() {
        Log("Disconnect()");
        try{
            _inputStream.close();
            _outputStream.close();
            _socket.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        _isConnected = false;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append("0x" + String.format("%02x", b & 0xff)+", ");
        return sb.toString();
    }

    @Override
    public void SendPacket(byte[] data) throws TransporterIncorrectStateException {
        Log("SendPacket()");
        synchronized (_lockObject) {
            try {
                if (_outputStream == null) {
                    Log("_outputStream == null");
                    throw new TransporterIncorrectStateException("Can not send data, because output stream is null");
                }
                Log("Send data - " + byteArrayToHex(data));
                _outputStream.write(data, 0, data.length);
                _outputStream.flush();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                Log("catch (IOException e)");
                e.printStackTrace();
                Log(e.getMessage());
            }
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
        Log("StartListenIncomingData");
        Thread listenDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _socket.setSoTimeout(50);
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
                            Log("StartListenIncomingData - EOFException ex");
                        }
                        catch (SocketTimeoutException ex) {
                            Log("StartListenIncomingData - SocketTimeoutException ex");
                        }
                        catch (SocketException ex) {
                            Log("StartListenIncomingData - SocketException ex");
                            ex.printStackTrace();
                            _isConnected = false;
                        }
                    } catch (IOException e) {
                        Log("StartListenIncomingData - IOException e");
                        e.printStackTrace();
                        continue;
                    }

                    if (bytesReadedFromStream > 0) {
                        Log("StartListenIncomingData - bytesReadedFromStream > 0");
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
