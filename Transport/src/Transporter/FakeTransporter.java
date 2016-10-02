package Transporter;

import Exceptions.SideOfTransporterNotRealizedException;
import Protocol.Arguments.MachineNameArgumentBody;
import Protocol.Arguments.MaximumDurationArgumentBody;
import Protocol.Arguments.MinimumDurationArgumentBody;
import Protocol.Enums.TaskPacketType;
import Protocol.TaskPacket;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

import java.util.ArrayList;

/**
 * Created by nekho on 02-Oct-16.
 * This class created for testing system. Testing logic is not completely realized.
 */
public class FakeTransporter implements IDataTransporter {

    private Boolean _isConnected = true;
    private ArrayList<IDataReceivedListener> _listeners;

    public FakeTransporter(String address, int port, TransporterSide whereTransporterCreated) {
        _listeners = new ArrayList<>();
    }

    @Override
    public Boolean TryConnect() throws SideOfTransporterNotRealizedException {
        return _isConnected;
    }

    @Override
    public void Disconnect() {

    }

    @Override
    public void StartListenIncomingData() {
        Thread listenDataThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    byte[] fakeData = GetFakeNewTaskPacket();
                    synchronized (_listeners) {
                        for (IDataReceivedListener listener: _listeners) {
                            listener.DataReceived(fakeData);
                        }
                    }

                    try {
                        Thread.sleep(6000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        listenDataThread.start();
    }

    private byte[] GetFakeNewTaskPacket(){
        TaskPacket fakePacket = new TaskPacket();
        fakePacket.SetType(TaskPacketType.NewTask);
        MachineNameArgumentBody machineNameArgumentBody = new MachineNameArgumentBody("testName");
        MinimumDurationArgumentBody minimumDurationArgumentBody = new MinimumDurationArgumentBody(6);
        MaximumDurationArgumentBody maximumDurationArgumentBody = new MaximumDurationArgumentBody(20);
        fakePacket.AddArgument(machineNameArgumentBody);
        fakePacket.AddArgument(minimumDurationArgumentBody);
        fakePacket.AddArgument(maximumDurationArgumentBody);
        return fakePacket.Serialize();
    }

    @Override
    public void SendPacket(byte[] data) {

    }

    @Override
    public void AddDataReceivedListener(IDataReceivedListener newListener) {
        synchronized (_listeners) {
            _listeners.add(newListener);
        }
    }

    @Override
    public void RemoveDataReceivedListener(IDataReceivedListener listener) {
        synchronized (_listeners) {
            _listeners.add(listener);
        }
    }

    @Override
    public Boolean IsConnected() {
        return null;
    }
}
