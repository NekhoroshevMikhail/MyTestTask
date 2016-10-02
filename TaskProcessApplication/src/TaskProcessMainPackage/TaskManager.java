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
import java.awt.*;
import java.nio.charset.StandardCharsets;
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
    JLabel _taskStateLabel;
    private static int _id = 0;
    private int _logId;

    public TaskManager(int port) {
        _port = port;
        _logId = _id;
        _id++;
        _processor = new TaskProcessor();
        _processor.AddTaskStateChangedListener(this);
        CreateFrame();
    }

    @Override
    public void run() {
         try {
            _dataTransporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Tcp, "localhost", _port, TransporterSide.Server);
            _dataTransporter.AddDataReceivedListener(this);
            _dataTransporter.TryConnect();
            _dataTransporter.StartListenIncomingData();
        } catch (TransorterNotRealizedExceptioin ex) {
            ex.printStackTrace();
        } catch (SideOfTransporterNotRealizedException e) {
            e.printStackTrace();
        }
    }

    private void Log(String logMessage) {
        //System.out.println("TaskManager"+_logId+ " " + logMessage);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append("0x" + String.format("%02x", b & 0xff)+", ");
        return sb.toString();
    }

    @Override
    public void DataReceived(byte[] data) {
        Log("DataReceived");
        Log("data - " + byteArrayToHex(data));
        TaskPacket packet = new TaskPacket();
        packet.Parse(data);

        if (packet.IsCorrect()) {
            Log("(packet.IsCorrect())");
            switch (packet.GetPacketType()) {
                case TaskPacketType.NewTask:
                    Log("TaskPacketType.NewTask");
                    MyTask myTask = GetTaskInfoFromPacket(packet);
                    try {
                        Log("ProcessNewTask(myTask); " + myTask.getName());
                        ProcessNewTask(myTask);
                    } catch(StartTaskProcessException ex) {
                        Log("StartTaskProcessException ex ");
                        ex.printStackTrace();
                        SendTaskError();
                        return;
                    }
                    SendTaskAck();
                    break;
                case TaskPacketType.TaskCompletedAck:
                    Log("TaskPacketType.TaskCompletedAck");
                    break;
                case TaskPacketType.TaskErrorAck:
                    Log("TaskPacketType.TaskCompletedAck");
                    break;
            }
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
                        SetTaskInfoToFrame("Idle","",0);
                        break;
                }

                break;
            case InProgress:
                switch (previousState) {
                    case Idle:
                    case Error:
                        SetTaskInfoToFrame("In progress", _processor.GetCurrentTaskName(),_processor.GetCurrentTaskDuration());
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
                        SetTaskInfoToFrame("Error","Some Error Happened",0);
                        SendTaskError();
                        break;
                }
                break;
        }
    }

    private void CreateFrame() {
        if (_displayingFrame == null) {
            _displayingFrame = new JFrame("Task Processor Frame");
            _displayingFrame.setSize(300,200);
            _displayingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _displayingFrame.setLocationByPlatform(true);
            GridLayout grid = new GridLayout(3,1);
            _displayingFrame.setLayout(grid);
            _taskStateLabel = new JLabel("Current task State: Idle");
            _machineNameLabel = new JLabel("Current Machine Name: ");
            _taskDurationLabel = new JLabel("Current Task Duration: ");
            _displayingFrame.add(_taskStateLabel);
            _displayingFrame.add(_machineNameLabel);
            _displayingFrame.add(_taskDurationLabel);
            _displayingFrame.setVisible(true);
        }
    }

    private void SetTaskInfoToFrame(String taskState, String machineName, int taskDuration ) {
        _taskStateLabel.setText("Current task State: " + taskState);
        _machineNameLabel.setText("Current Machine Name: " + machineName);
        _taskDurationLabel.setText("Current Task Duration: " +  taskDuration);
    }

    private void ProcessNewTask(MyTask myTask) throws StartTaskProcessException {
        Log("ProcessNewTask");
        if (myTask == null) {
            Log("if (myTask == null) {");
            throw new StartTaskProcessException("impossible to start incorrect Task");
        }
        if (_processor.GetCurrentState() != TaskThreadState.Idle) {
            Log("if (_processor.GetCurrentState() != TaskThreadState.Idle) {");
            throw new StartTaskProcessException("impossible to start Task, because some task is performing");
        }

        try {
            Log("_processor.SetTask(myTask);");
            _processor.SetTask(myTask);
        } catch (ArgumentNullException e) {
            Log("ArgumentNullException e");
            e.printStackTrace();
        }

        Thread taskProcessThread = new Thread(_processor);
        taskProcessThread.start();
        Log("taskProcessThread.start();");
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
        try {
            _dataTransporter.SendPacket(taskAckPacket.Serialize());
        } catch (TransporterIncorrectStateException e) {
            e.printStackTrace();
        }
    }

    private void SendTaskError() {
        TaskPacket taskAckPacket = new TaskPacket();
        taskAckPacket.SetType(TaskPacketType.TaskError);
        try {
            _dataTransporter.SendPacket(taskAckPacket.Serialize());
        } catch (TransporterIncorrectStateException e) {
            e.printStackTrace();
        }
    }

    private void SendTaskAck() {
        TaskPacket taskAckPacket = new TaskPacket();
        taskAckPacket.SetType(TaskPacketType.NewTaskAck);
        try {
            _dataTransporter.SendPacket(taskAckPacket.Serialize());
        } catch (TransporterIncorrectStateException e) {
            e.printStackTrace();
        }
    }

    private void Stop()
    {
        _dataTransporter.RemoveDataReceivedListener(this);
        _dataTransporter.Disconnect();
    }
}
