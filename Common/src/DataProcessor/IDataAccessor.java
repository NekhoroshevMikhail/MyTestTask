package DataProcessor;

import DataModel.MyTaskList;
import DataModel.WorkingNodeList;

/**
 * Created by nekho on 27-Sep-16.
 */
public interface IDataAccessor {
    MyTaskList GetAllTasks();
    MyTaskList GetAllTasks(String filePath);
    void SaveAllTasks(MyTaskList listOfTasks);

    WorkingNodeList GetAllWorkingNodes();
    WorkingNodeList GetAllWorkingNodes(String filePath);
    void SaveAllWorkingNodes(WorkingNodeList listOfWorkingNodes);
}
