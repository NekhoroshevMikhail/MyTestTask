package TaskProcessMainPackage;

import Exceptions.TransorterNotRealizedExceptioin;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import TransportCommon.IDataTransporter;

import java.awt.*;

/**
 * Created by nekho on 29-Sep-16.
 */
public class Main {
    public static void main(String[] args) {
        TaskProcessorThread thread = new TaskProcessorThread(args[0], Integer.decode(args[1]));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
