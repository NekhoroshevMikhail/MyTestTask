package CorePackage;

import CorePackage.Events.IWorkingNodeStateChangedListener;
import CorePackage.Exceptions.WorkingNodeIsBusyException;
import DataModel.MyTask;
import DataModel.WorkingNode;
import DataModel.WorkingNodeState;
import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransorterNotRealizedExceptioin;
import Exceptions.TransporterIncorrectStateException;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import Protocol.Arguments.MachineNameArgumentBody;
import Protocol.Arguments.MaximumDurationArgumentBody;
import Protocol.Arguments.MinimumDurationArgumentBody;
import Protocol.Enums.TaskPacketType;
import Protocol.TaskPacket;
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
    private IDataTransporter _dataTransporter;
    private MyTask _currentTask;
    private WorkingNodeState _currentNodeState;
    private ArrayList<IWorkingNodeStateChangedListener> _listeners;

    public ProcessingWorkingNode(WorkingNode node) {
        super(node);
        _currentTask = null;
        _listeners = new ArrayList<>();
    }

    public Boolean CanPerformNewTask(){
        return _currentNodeState == WorkingNodeState.WaitingTask || _currentNodeState == WorkingNodeState.Idle;
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
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "TaskProcessApplication.jar", Integer.toString(port));
            Process p = pb.start();
            _currentNodeState = WorkingNodeState.Idle;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ConnectAndCreateTransporter() {
        IDataTransporter transporter = null;
        try {
            transporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Tcp, "localhost", port, TransporterSide.Client);
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
        if (_currentNodeState != WorkingNodeState.Idle && _currentNodeState != WorkingNodeState.WaitingTask) {
            throw new WorkingNodeIsBusyException("You can not set new task when working Node is Busy");
        }
        _currentTask = task;
        _currentNodeState = WorkingNodeState.InitializingTask;
        PrepareAndSendPacketWithCurrentTask();
    }

    private void PrepareAndSendPacketWithCurrentTask() {
        TaskPacket packet = new TaskPacket();
        packet.SetType(TaskPacketType.NewTask);
        MachineNameArgumentBody machineName = new MachineNameArgumentBody(_currentTask.getName());
        MaximumDurationArgumentBody maximumDurationArgumentBody = new MaximumDurationArgumentBody(_currentTask.getMaximumTaskTime());
        MinimumDurationArgumentBody minimumDurationArgumentBody = new MinimumDurationArgumentBody(_currentTask.getMinimumTaskTime());
        packet.AddArgument(machineName);
        packet.AddArgument(maximumDurationArgumentBody);
        packet.AddArgument(minimumDurationArgumentBody);
        byte[] dataForSend = packet.Serialize();
        try{
            _dataTransporter.SendPacket(dataForSend);
        }catch (TransporterIncorrectStateException ex) {
            ex.printStackTrace();
            _currentNodeState = WorkingNodeState.Idle;
        }

    }

    public void StopCurrentTask() {
        //todo: realize this function
    }

    @Override
    //todo: измегить реализацию событий, чтобы они наружу не торчали.
    public void DataReceived(byte[] data) {
        TaskPacket receivedPacket = new TaskPacket();
        receivedPacket.Parse(data);

        if (receivedPacket.IsCorrect()) {
            switch (receivedPacket.GetPacketType())
            {
                case TaskPacketType.NewTaskAck:
                    _currentNodeState = WorkingNodeState.InProgress;
                    break;
                case TaskPacketType.TaskCompleted:
                    SendTaskCompletedAck();
                    _currentNodeState = WorkingNodeState.WaitingTask;
                    break;
                case TaskPacketType.TaskError:
                    SendTaskErrorAck();
                    _currentNodeState = WorkingNodeState.Error;
                     break;
            }
        }
    }

    private void SendTaskCompletedAck() {
        TaskPacket taskCompletedAck = new TaskPacket();
        taskCompletedAck.SetType(TaskPacketType.TaskCompletedAck);
        byte[] dataForSend = taskCompletedAck.Serialize();

        try {
            _dataTransporter.SendPacket(dataForSend);
        } catch (TransporterIncorrectStateException e) {
            e.printStackTrace();
        }
    }

    private void SendTaskErrorAck() {
        TaskPacket taskCompletedAck = new TaskPacket();
        taskCompletedAck.SetType(TaskPacketType.TaskErrorAck);
        byte[] dataForSend = taskCompletedAck.Serialize();
        try {
            _dataTransporter.SendPacket(dataForSend);
        } catch (TransporterIncorrectStateException e) {
            e.printStackTrace();
        }
    }

}