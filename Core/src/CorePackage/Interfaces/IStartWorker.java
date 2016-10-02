package CorePackage.Interfaces;

import CorePackage.Events.IWorkFinishedListener;

/**
 * Created by nekho on 02-Oct-16.
 */
public interface IStartWorker {
    void StartWork(String workingNodesFilePath, String tasksFilePath);
    void StartWork(IWorkFinishedListener workFinishedListener);
}
