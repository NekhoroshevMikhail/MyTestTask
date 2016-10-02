package CorePackage;

import CorePackage.Interfaces.IStartWorker;
import DAF.AvailableDataFormats;
import DAF.DataAccessorCreator;
import DataProcessor.IDataAccessor;
import Exceptions.DataAccessorNotDefinedException;

/**
 * Created by nekho on 02-Oct-16.
 */
public class WorkStarter implements IStartWorker {

    private IDataAccessor GetDataAccessor(){
        IDataAccessor accessor = null;
        try{
            accessor = DataAccessorCreator.CreateDataAccessor(AvailableDataFormats.Xml);
        }catch (DataAccessorNotDefinedException ex) {
            //// TODO: добавить логгер
            System.exit(0);
            return null;
        }
        return accessor;
    }
    @Override
    public void StartWork(String workingNodesFilePath, String tasksFilePath) {
        IDataAccessor accessor = GetDataAccessor();

        WorkingNodesManager manager = new WorkingNodesManager(accessor.GetAllWorkingNodes(workingNodesFilePath), accessor.GetAllTasks(tasksFilePath));
        manager.StartWork();
    }

    public void StartWork() {
        IDataAccessor accessor = GetDataAccessor();

        WorkingNodesManager manager = new WorkingNodesManager(accessor.GetAllWorkingNodes(), accessor.GetAllTasks());
        manager.StartWork();
    }
}
