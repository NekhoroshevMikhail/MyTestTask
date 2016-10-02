package CorePackage;

import CorePackage.Events.IWorkingNodeStateChangedListener;
import CorePackage.Exceptions.WorkingNodeIsBusyException;
import DataModel.*;
import Exceptions.BelowZeroException;
import Exceptions.IncorrectRangeException;
import Exceptions.OutOfPossibleRangeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;

/**
 * Created by nekho on 29-Sep-16.
 */
public class WorkingNodesManager implements IWorkingNodeStateChangedListener{

    private ArrayList<ProcessingWorkingNode> _processingNodes;
    private MyTaskList _listOfTasks;
    private Boolean _isStarted;


    public WorkingNodesManager(WorkingNodeList nodesToRun, MyTaskList listOfTasks)
    {
        _processingNodes = new ArrayList<>();
        FillProcessingNodesList(nodesToRun);
        _listOfTasks = listOfTasks;
        _isStarted = false;
    }

    public void StartWork() {
        _isStarted = true;
        RunAllNodes();
        LetsManageTasks();
    }

    public void FinishWork(){
        _isStarted = false;
        for (int i =0; i < _processingNodes.size(); i++) {
            ProcessingWorkingNode node = _processingNodes.get(i);
            node.RemoveStateChangedListener(this);
            node.Stop();
        }
    }

    private void LetsManageTasks(){
        for (int i =0; i < _processingNodes.size(); i++) {
            ProcessingWorkingNode node = _processingNodes.get(i);
            try {
                MyTask task = new MyTask();
                try {
                    task.setMinimumTaskTime(6);
                    task.setMaximumTaskTime(10);
                } catch (OutOfPossibleRangeException e) {
                    e.printStackTrace();
                } catch (IncorrectRangeException e) {
                    e.printStackTrace();
                }
                try {
                    task.setPriority((short)2);
                } catch (BelowZeroException e) {
                    e.printStackTrace();
                }
                node.SetNewTask(task);
            } catch (WorkingNodeIsBusyException e) {
                e.printStackTrace();
            }

        }
    }

    private void RunAllNodes() {
        for (int i =0; i < _processingNodes.size(); i++) {
            _processingNodes.get(i).AddNodeStateChangedListener(this);
            _processingNodes.get(i).Run();
        }
    }

    private void FillProcessingNodesList(WorkingNodeList list) {
        for (int i =0; i < list.size(); i++) {
            _processingNodes.add(new ProcessingWorkingNode(list.get(i)));
        }
    }

    @Override
    public void WorkingNodeStateChanged(ProcessingWorkingNode sender, WorkingNodeState newState) {
        //todo: выдавать новое задание. не забыть лок
    }

    /*private void RunNode(String name, String port) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "TaskProcessApplication.jar", name, port);
            Process p = pb.start();
            ConnectWithWorkingNode(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void ConnectWithWorkingNode(int portOfNode){

    }


    private void ManageTasks() {
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

    }


}
