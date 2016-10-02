package TaskProcessMainPackage;

import DataModel.MyTask;
import Exceptions.ArgumentNullException;
import TaskProcessMainPackage.Enums.TaskThreadState;
import TaskProcessMainPackage.Events.ITaskThreadStateChangedListener;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nekho on 02-Oct-16.
 */
public class TaskProcessor implements Runnable {

    private ArrayList<ITaskThreadStateChangedListener> _listeners;
    private MyTask _taskToPerform;
    private int _calculatedTaskDuration;
    private TaskThreadState _previousState;
    private TaskThreadState _currentState;

    public TaskProcessor() {
        _listeners = new ArrayList<>();
        _previousState = TaskThreadState.Idle;
        _currentState = TaskThreadState.Idle;
    }

    public void SetTask(MyTask taskToPerform) throws ArgumentNullException {
        if (taskToPerform == null) {
            throw new ArgumentNullException("you need to specify task!");
        }
        _taskToPerform = taskToPerform;
        CalculateTaskDuration();
    }

    private void CalculateTaskDuration() {
        Random rand = new Random();
        int max = _taskToPerform.getMaximumTaskTime();
        int min = _taskToPerform.getMinimumTaskTime();
        _calculatedTaskDuration = (rand.nextInt((max - min) + 1) + min) * 1000;
    }

    public int GetCalculatedTaskDuration() {
        return _calculatedTaskDuration;
    }

    public TaskThreadState GetCurrentState() {
        return _currentState;
    }

    public void AddTaskStateChangedListener(ITaskThreadStateChangedListener listener) {
        synchronized (_listeners) {
            _listeners.add(listener);
        }
    }

    public void RemoveTaskStateChangedListener(ITaskThreadStateChangedListener listener) {
        synchronized (_listeners) {
            _listeners.remove(listener);
        }
    }

    @Override
    public void run() {
        ChangeState(TaskThreadState.InProgress);
        try {
            Thread.sleep(_calculatedTaskDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
            ChangeState(TaskThreadState.Error);
        }
        ChangeState(TaskThreadState.Idle);
    }

    private void ChangeState(TaskThreadState newState) {
        _previousState =_currentState;
        _currentState = newState;

        synchronized (_listeners) {
            for (ITaskThreadStateChangedListener listener : _listeners) {
                listener.TaskThreadStateChanged(_previousState, _currentState);
            }
        }
    }

    public String GetCurrentTaskName() {
        return _taskToPerform.getName();
    }

    public int GetCurrentTaskDuration() {
        return _calculatedTaskDuration;
    }
}
