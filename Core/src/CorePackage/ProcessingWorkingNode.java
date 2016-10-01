package CorePackage;

import CorePackage.Events.IWorkingNodeStateChangedListener;
import CorePackage.Exceptions.WorkingNodeIsBusyException;
import DataModel.MyTask;
import DataModel.WorkingNode;
import DataModel.WorkingNodeState;
import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransorterNotRealizedExceptioin;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nekho on 01-Oct-16.
 */
public class ProcessingWorkingNode extends WorkingNode implements IDataReceivedListener{
    private Boolean _isBusy;
    private IDataTransporter _dataTransporter;
    private MyTask _currentTask;
    private WorkingNodeState _currentNodeState;
    private ArrayList<IWorkingNodeStateChangedListener> _listeners;

    private int _port;

    public ProcessingWorkingNode(WorkingNode node) {
        super(node);
        _isBusy = false;
        _currentTask = null;
        _currentNodeState = WorkingNodeState.Idle;
        _listeners = new ArrayList<>();
    }

    public void Run(){
        RunWorkingNode();
        ConnectAndCreateTransporter();
    }

    public void AddNodeStateChangedListener(IWorkingNodeStateChangedListener newListener) {
        synchronized (_listeners) {
            _listeners.add(newListener);
        }
    }

    public void RemoveStateChangedListener(IWorkingNodeStateChangedListener listenerForRemove) {
        synchronized (_listeners) {
            _listeners.remove(listenerForRemove);
        }
    }

    private void RunWorkingNode() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "TaskProcessApplication.jar", name, Integer.toString(port));
            Process p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ConnectAndCreateTransporter() {
        IDataTransporter transporter = null;
        try {
            transporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Tcp, "localhost", _port, TransporterSide.Client);
            transporter.AddDataReceivedListener(this);
        } catch (TransorterNotRealizedExceptioin transorterNotRealizedExceptioin) {
            transorterNotRealizedExceptioin.printStackTrace();
        }
        if (transporter != null) {
            try {
                transporter.TryConnect();
                transporter.StartListenIncomingData();
            } catch (SideOfTransporterNotRealizedException e) {
                e.printStackTrace();
            }
        }
        _dataTransporter = transporter;
    }

    public void Stop() {
        _dataTransporter.Disconnect();
        _currentNodeState = WorkingNodeState.Idle;
    }

    public void SetNewTask(MyTask task) throws WorkingNodeIsBusyException {
        if (_isBusy) {
            throw new WorkingNodeIsBusyException("You can not set new task when working Node is Busy");
        }
        _currentTask = task;
        _dataTransporter.SendPacket(new byte[] {1,2,3,4,5});
    }

    public void StopCurrentTask() {
        //todo: realize this function
    }

    @Override
    //todo: измегить реализацию событий, чтобы они наружу не торчали.
    public void DataReceived(byte[] data) {
        //todo: parseData and processData;
    }
}


    /*IDataTransporter transporter = null;
        try {
                transporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Tcp, "localhost", 123, TransporterSide.Client);
                } catch (TransorterNotRealizedExceptioin transorterNotRealizedExceptioin) {
                transorterNotRealizedExceptioin.printStackTrace();
                }
                if (transporter != null) {
                try {
                transporter.TryConnect();
                transporter.StartListenIncomingData();
                } catch (SideOfTransporterNotRealizedException e) {
                e.printStackTrace();
                }
                while(true) {
                if (transporter.IsConnected()) {
                transporter.SendPacket(new byte[] {1,2,3});
                }
                try {
                Thread.sleep((long) 60000);
                } catch (InterruptedException e) {
                e.printStackTrace();
                }
                }

                }
                transporter.Disconnect();*/