package CorePackage.Events;

import CorePackage.ProcessingWorkingNode;
import DataModel.WorkingNodeState;

/**
 * Created by nekho on 01-Oct-16.
 */
public interface IWorkingNodeStateChangedListener {
    void WorkingNodeStateChanged(ProcessingWorkingNode sender, WorkingNodeState newState);
}
