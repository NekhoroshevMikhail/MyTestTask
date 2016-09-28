package Factory;

import Exceptions.TransorterNotRealizedExceptioin;
import TransportCommon.IDataTransporter;
import Transporter.UdpDataTransporter;

/**
 * Created by nekho on 28-Sep-16.
 */
public class TransporterCreator {
    public static IDataTransporter CreateDataProcessor(PossibleTransport transportType, String address, int port)
            throws TransorterNotRealizedExceptioin
    {
        switch (transportType)
        {
            case Udp:
                return new UdpDataTransporter(address, port);
            default:
                throw new TransorterNotRealizedExceptioin("you must realize transporter");
        }
    }
}
