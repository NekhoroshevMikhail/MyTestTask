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

        WorkingNodesManager manager = new WorkingNodesManager(accessor.GetAllWorkingNodes());
        manager.RunAllNodes();

        IDataTransporter transporter = null;
        try {
            transporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Tcp, "localhost", 123, TransporterSide.Client);
        } catch (TransorterNotRealizedExceptioin transorterNotRealizedExceptioin) {
            transorterNotRealizedExceptioin.printStackTrace();
        }
        if (transporter != null) {
            try {
                transporter.TryConnect();
                transporter.StartListenIncomingData();
            } catch (SideOfTransporterNotRealizedException e) {
                e.printStackTrace();
            }
            while(true) {
                if (transporter.IsConnected()) {
                    transporter.SendPacket(new byte[] {1,2,3});
                }
                try {
                    Thread.sleep((long) 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        transporter.Disconnect();
        System.exit(0);

    }
}
