package CorePackage;
import DAF.AvailableDataFormats;
import DAF.DataAccessorCreator;
import DataProcessor.IDataAccessor;
import Exceptions.DataAccessorNotDefinedException;

/**
 * Created by nekho on 28-Sep-16.
 */
public class Main {
    public static void main(String[] args) {

        IDataAccessor accessor = null;
        try{
            accessor = DataAccessorCreator.CreateDataProcessor(AvailableDataFormats.Xml);
        }catch (DataAccessorNotDefinedException ex) {
            //// TODO: добавить логгер
            System.exit(0);
            return;
        }

        WorkingNodesManager manager = new WorkingNodesManager(accessor.GetAllWorkingNodes());
        manager.RunAllNodes();

        System.exit(0);

    }
}
