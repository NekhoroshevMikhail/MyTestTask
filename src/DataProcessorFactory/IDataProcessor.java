package DataProcessorFactory;

import DataModel.MyTaskList;
import DataModel.WorkingNodeList;

/**
 * Created by nekho on 27-Sep-16.
 */
public interface IDataProcessor {
    MyTaskList GetAllTasks();
    void SaveAllTasks(MyTaskList listOfTasks);

    WorkingNodeList GetAllWorkingNodes();
    void SaveAllWorkingNodes(WorkingNodeList listOfWorkingNodes);
}
