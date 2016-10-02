package TaskProcessMainPackage.Events;

import TaskProcessMainPackage.Enums.TaskThreadState;

/**
 * Created by nekho on 02-Oct-16.
 */
public interface ITaskThreadStateChangedListener {
    void TaskThreadStateChanged(TaskThreadState previousState, TaskThreadState newState);
}
