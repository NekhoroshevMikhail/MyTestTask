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
import com.sun.org.apache.xpath.internal.operations.Bool;

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
            if (args.length == 1 && IsHelpRequest(args[0])) {
                System.out.println("");
                System.out.println("USAGE:");
                System.out.println("\t java -jar Core.jar <arg1> <arg2> [args]");
                System.out.println("\t <arg1> - path to xml with working nodes config");
                System.out.println("\t <arg2> - path to xml with tasks config");
                System.out.println("\t [args] - all arguments after first and second are ignored");
                System.out.println("");
                System.out.println("EXAMPLE:");
                System.out.println("\t java -jar Core.jar C:\\TempDirectory\\WorkingNodes.xml D:\\Test\\Tasks.xml");
                return;
            }
            if (args.length < 2) {
                System.out.println("Incorrect parameters number!");
                return;
            }

            starter.StartWork(args[0], args[1]);
        }
    }

    private static boolean IsHelpRequest(String arg) {
        Boolean isRequestStart = arg.startsWith("-") || arg.startsWith("/");

        String parseString = arg.substring(1);
        Boolean containsHelpString = parseString.startsWith("help") || parseString.startsWith("h");
        return isRequestStart && containsHelpString;
    }
}
