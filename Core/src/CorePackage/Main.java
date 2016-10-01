package CorePackage;
import DAF.AvailableDataFormats;
import DAF.DataAccessorCreator;
import DataProcessor.IDataAccessor;
import Exceptions.DataAccessorNotDefinedException;
import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransorterNotRealizedExceptioin;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

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

        WorkingNodesManager manager = new WorkingNodesManager(accessor.GetAllWorkingNodes(), accessor.GetAllTasks());
        manager.StartWork();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        manager.FinishWork();
        System.exit(0);

    }
}
