package Transporter;

import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransporterIncorrectStateException;
import Protocol.Enums.TaskPacketType;
import Protocol.TaskPacket;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nekho on 02-Oct-16.
 */
public class ClientSideTransporterMock implements IDataTransporter {

    private ArrayList<IDataReceivedListener> _listeners;
    private Object _lockObject;
    private byte[] fakePacket = null;
    private Boolean _isConnected = false;

    public ClientSideTransporterMock() {
        _listeners = new ArrayList<>();
        _lockObject = new Object();
    }

    @Override
    public Boolean TryConnect() throws SideOfTransporterNotRealizedException {
        _isConnected = true;
        return true;
    }

    @Override
    public void Disconnect() {
        _isConnected = false;
    }

    @Override
    public void StartListenIncomingData() {
        Thread listenDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    byte[] fakeData = GetFakeNewTaskPacket();
                    if (fakeData != null) {
                        synchronized (_listeners) {
                            for (IDataReceivedListener listener: _listeners) {
                                listener.DataReceived(fakeData);
                            }
                        }
                    }
                }
            }
        });
        listenDataThread.start();
    }

    @Override
    public void SendPacket(byte[] data) throws TransporterIncorrectStateException
    {
        /*TaskPacket packet = new TaskPacket();
        packet.Parse(data);
        switch (packet.GetPacketType()) {
            case TaskPacketType.NewTask:

                TaskPacket fakeReplyPacket = new TaskPacket();
                fakeReplyPacket.SetType(TaskPacketType.NewTaskAck);
                synchronized (_lockObject) {
                    fakePacket = fakeReplyPacket.Serialize();
                }

                /*TaskPacket taskCompletedPacket = new TaskPacket();
                taskCompletedPacket.SetType(TaskPacketType.TaskCompleted);
                synchronized (_lockObject) {
                    fakePacket = taskCompletedPacket.Serialize();
                }

                break;
        }*/
        TaskPacket fakeReplyPacket = new TaskPacket();
        fakeReplyPacket.SetType(TaskPacketType.NewTaskAck);
        fakePacket = fakeReplyPacket.Serialize();
    }

    @Override
    public void AddDataReceivedListener(IDataReceivedListener listener) {
        synchronized (_listeners) {
            _listeners.add(listener);
        }
    }

    @Override
    public void RemoveDataReceivedListener(IDataReceivedListener listener) {
        synchronized (_listeners) {
            _listeners.remove(listener);
        }
    }

    private byte[] GetFakeNewTaskPacket() {
        synchronized (_lockObject) {
                return fakePacket;
        }

    }

    @Override
    public Boolean IsConnected() {
        return true;
    }
}
