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
        Short portNumber = Short.parseShort(args[1]);
        try {
            IDataTransporter dataTransporter = TransporterCreator.CreateDataProcessor(PossibleTransport.Tcp, "localhost", portNumber.shortValue());
        } catch (TransorterNotRealizedExceptioin ex) {
            ex.printStackTrace();
        }
    }
}
