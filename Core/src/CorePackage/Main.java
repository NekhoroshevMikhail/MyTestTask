package CorePackage;
import CorePackage.GUI.MainWindow;
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
    public static void main(String[] args) {
        WorkStarter starter = new WorkStarter();
        if (args.length == 0) {
            MainWindow window = new MainWindow("Starting work window", starter);
            window.setVisible(true);
        } else {
            if (args.length < 2) {
                System.out.println("Incorrect parameters number!");
                return;

            }
            starter.StartWork(args[0], args[1]);
        }

    }
}
