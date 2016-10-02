package CorePackage;
import DAF.AvailableDataFormats;
import DAF.DataAccessorCreator;
import DataModel.MyTask;
import DataModel.MyTaskList;
import DataModel.WorkingNode;
import DataModel.WorkingNodeList;
import DataProcessor.IDataAccessor;
import Exceptions.*;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

/**
 * Created by nekho on 28-Sep-16.
 */
public class Main {
    public static void main(String[] args) throws IncorrectRangeException, OutOfPossibleRangeException, BelowZeroException, EmptyNameException {

        IDataAccessor accessor = null;
        try{
            accessor = DataAccessorCreator.CreateDataProcessor(AvailableDataFormats.Xml);
        }catch (DataAccessorNotDefinedException ex) {
            //// TODO: добавить логгер
            System.exit(0);
            return;
        }

        WorkingNodesManager manager = new WorkingNodesManager(accessor.GetAllWorkingNodes(), accessor.GetAllTasks());
        manager.StartWork();
        /*WorkingNodeList list = accessor.GetAllWorkingNodes();
        MyTaskList tasks = accessor.GetAllTasks();
        FOR_REMOVE_WorkingNodeThread t = new FOR_REMOVE_WorkingNodeThread(list.get(0));
        t.SetNewTask(tasks.get(0));
        t.Start();*/
    }
}
