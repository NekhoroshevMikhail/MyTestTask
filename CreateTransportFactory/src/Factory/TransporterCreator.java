package Factory;

import Exceptions.TransorterNotRealizedExceptioin;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;
import Transporter.TcpDataTransporter;

/**
 * Created by nekho on 28-Sep-16.
 */
public class TransporterCreator {
    public static IDataTransporter CreateDataTransporter(PossibleTransport transportType, String address, int port, TransporterSide side)
            throws TransorterNotRealizedExceptioin
    {
        switch (transportType)
        {
            case Tcp:
                return new TcpDataTransporter(address, port, side);
            default:
                throw new TransorterNotRealizedExceptioin("you must realize transporter");
        }
    }
}
