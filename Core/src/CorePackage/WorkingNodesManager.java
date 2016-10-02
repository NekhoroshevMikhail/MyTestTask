package CorePackage;

import CorePackage.Events.IWorkFinishedListener;
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

import static DataModel.WorkingNodeState.*;

/**
 * Created by nekho on 29-Sep-16.
 */
public class WorkingNodesManager implements IWorkingNodeStateChangedListener{

    private ArrayList<ProcessingWorkingNode> _processingNodes;
    private MyTaskList _listOfTasks;
    private Boolean _isStarted;
    private ArrayList<IWorkFinishedListener> _listeners;

    public WorkingNodesManager(WorkingNodeList nodesToRun, MyTaskList listOfTasks)
    {
        _processingNodes = new ArrayList<>();
        FillProcessingNodesList(nodesToRun);
        _listOfTasks = listOfTasks;
        _isStarted = false;
        _listeners = new ArrayList<>();
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
        ProcessingWorkingNode node = GetFirstNotBusyNode();
        while(node != null) {
            MyTask taskWithHighestPriority = GetMostUrgentTask();
            if (taskWithHighestPriority != null) {
                try {
                    node.SetNewTask(taskWithHighestPriority);
                } catch (WorkingNodeIsBusyException e) {
                    e.printStackTrace();
                }
            }
            else {
                break;
            }
            node = GetFirstNotBusyNode();
        }
    }

    private MyTask GetMostUrgentTask() {
        MyTask result = null;
        if (_listOfTasks.size() == 0) {
            return null;
        }

        _listOfTasks.sort(new TaskPriorityComparator());
        result = _listOfTasks.get(0);
        _listOfTasks.remove(result);
        return result;
    }

    private ProcessingWorkingNode GetFirstNotBusyNode(){
        for (int i =0; i < _processingNodes.size(); i++) {
            ProcessingWorkingNode node = _processingNodes.get(i);
            if (node.CanPerformNewTask()){
                return node;
            }
        }
        return null;
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
        switch (newState){
            case Connected:
            case WaitingNewTask:
                LetsManageTasks();
                break;
        }
        Thread checkWorkFinishedThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (_listOfTasks.size() == 0 && AllNodesFree()) {
                            for (IWorkFinishedListener listener :  _listeners) {
                                listener.WorkFinished();
                            }
                        }
                    }
                }
        );
        checkWorkFinishedThread.start();

    }

    private boolean AllNodesFree() {
        for(ProcessingWorkingNode node : _processingNodes) {
            if (!node.CanPerformNewTask()){
                return false;
            }
        }
        return true;
    }

    public void AddWorkFinishedListener(IWorkFinishedListener workFinishedListener) {
        _listeners.add(workFinishedListener);
    }
}
