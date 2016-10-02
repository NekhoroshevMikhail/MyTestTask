package TaskProcessMainPackage;

import DataModel.MyTask;
import Exceptions.*;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import Protocol.Arguments.MachineNameArgumentBody;
import Protocol.Arguments.MaximumDurationArgumentBody;
import Protocol.Arguments.MinimumDurationArgumentBody;
import Protocol.Enums.TaskPacketArgumentType;
import Protocol.Enums.TaskPacketType;
import Protocol.Interfaces.IPacketArgument;
import Protocol.TaskPacket;
import TaskProcessMainPackage.Enums.TaskThreadState;
import TaskProcessMainPackage.Events.ITaskThreadStateChangedListener;
import TaskProcessMainPackage.Exceptions.StartTaskProcessException;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by nekho on 30-Sep-16.
 */
public class TaskManager implements Runnable, IDataReceivedListener, ITaskThreadStateChangedListener {
    private int _port;
    private IDataTransporter _dataTransporter;
    private TaskProcessor _processor;
    private JFrame _displayingFrame;
    JLabel _machineNameLabel;
    JLabel _taskDurationLabel;

    public TaskManager(int port) {
        _port = port;
        _processor = new TaskProcessor();
        _processor.AddTaskStateChangedListener(this);
        CreateFrame();
    }

    @Override
    public void run() {
         try {
            _dataTransporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Fake, "localhost", _port, TransporterSide.Server);
            _dataTransporter.AddDataReceivedListener(this);
            _dataTransporter.TryConnect();
            _dataTransporter.StartListenIncomingData();
        } catch (TransorterNotRealizedExceptioin ex) {
            ex.printStackTrace();
        } catch (SideOfTransporterNotRealizedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void DataReceived(byte[] data) {
        TaskPacket packet = new TaskPacket();
        packet.Parse(data);

        if (packet.IsCorrect()) {
            switch (packet.GetPacketType()) {
                case TaskPacketType.NewTask:
                    MyTask myTask = GetTaskInfoFromPacket(packet);
                    ShowFrameIfItIsNotVisible();
                    try {
                        ProcessNewTask(myTask); // отдельный поток
                    } catch(StartTaskProcessException ex) {
                        ex.printStackTrace();
                        SendTaskError();
                        return;
                    }
                    SendTaskAck();
                    break;
                case TaskPacketType.TaskCompletedAck:
                    break;
                case TaskPacketType.TaskErrorAck:
                    break;
            }
        }

    }

    private void ShowFrameIfItIsNotVisible() {
        if (!_displayingFrame.isVisible()) {
            _displayingFrame.setVisible(true);
        }
    }

    public void TaskThreadStateChanged(TaskThreadState previousState, TaskThreadState newState) {
        switch (newState) {
            case Idle:
                switch (previousState) {
                    case Idle:
                    case Error:
                        break;
                    case InProgress:
                        SendTaskCompleted();
                        SetTaskInfoToFrame("",0);
                        break;
                }

                break;
            case InProgress:
                switch (previousState) {
                    case Idle:
                    case Error:
                        SetTaskInfoToFrame(_processor.GetCurrentTaskName(),_processor.GetCurrentTaskDuration());
                        SendTaskAck();
                        break;
                    case InProgress:
                        break;
                }
                break;
            case Error:
                switch (previousState) {
                    case Error:
                         break;
                    case Idle:
                    case InProgress:
                        SetTaskInfoToFrame("Some Error Happened",0);
                        SendTaskError();
                        break;
                }
                break;
        }
    }

    private void CreateFrame() {
        if (_displayingFrame == null) {
            _displayingFrame = new JFrame("Task Processor Frame");
            _displayingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _machineNameLabel = new JLabel();
            _taskDurationLabel = new JLabel();
            _displayingFrame.add(_machineNameLabel);
            _displayingFrame.add(_taskDurationLabel);
        }
    }

    private void SetTaskInfoToFrame(String machineName, int taskDuration) {
        _machineNameLabel.setText(String.format("Current Machine Name: {0}", machineName));
        _taskDurationLabel.setText(String.format("Current Task Duration: {0}", taskDuration));
    }

    private void ProcessNewTask(MyTask myTask) throws StartTaskProcessException {
        if (myTask == null) {
            throw new StartTaskProcessException("impossible to start incorrect Task");
        }
        if (_processor.GetCurrentState() != TaskThreadState.Idle) {
            throw new StartTaskProcessException("impossible to start Task, because some task is performing");
        }

        try {
            _processor.SetTask(myTask);
        } catch (ArgumentNullException e) {
            e.printStackTrace();
        }

        Thread taskProcessThread = new Thread(_processor);
        taskProcessThread.start();
    }


    private MyTask GetTaskInfoFromPacket(TaskPacket packet) {
        MyTask result = new MyTask();
        Boolean isTaskInitializedCorrectly = true;
        if (packet.GetPacketType() == TaskPacketType.NewTask) {
            ArrayList<IPacketArgument> packetArguments = packet.GetArguments();
            for(IPacketArgument argument : packetArguments) {
                switch (argument.GetArgumentType()){
                    case TaskPacketArgumentType.MachineNameArgument:
                        try {
                            result.setName(((MachineNameArgumentBody)argument).GetName());
                        } catch (EmptyNameException e) {
                            isTaskInitializedCorrectly = false;
                            e.printStackTrace();
                        }
                        break;
                    case TaskPacketArgumentType.MaximumDurationArgument:
                        try {
                            result.setMaximumTaskTime(((MaximumDurationArgumentBody)argument).GetMaximumDuration());
                        } catch (OutOfPossibleRangeException e) {
                            isTaskInitializedCorrectly = false;
                            e.printStackTrace();
                        } catch (IncorrectRangeException e) {
                            isTaskInitializedCorrectly = false;
                            e.printStackTrace();
                        }
                        break;
                    case TaskPacketArgumentType.MinimumDurationArgument:
                        try {
                            result.setMinimumTaskTime(((MinimumDurationArgumentBody)argument).GetMinimumDuration());
                        } catch (OutOfPossibleRangeException e) {
                            isTaskInitializedCorrectly = false;
                            e.printStackTrace();
                        } catch (IncorrectRangeException e) {
                            isTaskInitializedCorrectly = false;
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }

        if (isTaskInitializedCorrectly) {
            return result;
        }
        return null;
    }

    private void SendTaskCompleted() {
        TaskPacket taskAckPacket = new TaskPacket();
        taskAckPacket.SetType(TaskPacketType.TaskCompleted);
        _dataTransporter.SendPacket(taskAckPacket.Serialize());
    }

    private void SendTaskError() {
        TaskPacket taskAckPacket = new TaskPacket();
        taskAckPacket.SetType(TaskPacketType.TaskError);
        _dataTransporter.SendPacket(taskAckPacket.Serialize());
    }

    private void SendTaskAck() {
        TaskPacket taskAckPacket = new TaskPacket();
        taskAckPacket.SetType(TaskPacketType.NewTaskAck);
        _dataTransporter.SendPacket(taskAckPacket.Serialize());
    }

    private void Stop()
    {
        _dataTransporter.RemoveDataReceivedListener(this);
        _dataTransporter.Disconnect();
    }
}
