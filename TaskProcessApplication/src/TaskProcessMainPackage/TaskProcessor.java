package TaskProcessMainPackage;

import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransorterNotRealizedExceptioin;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

import java.awt.*;

/**
 * Created by nekho on 30-Sep-16.
 */
public class TaskProcessor implements Runnable, IDataReceivedListener {
    private String _machineName;
    private int _port;
    private IDataTransporter _dataTransporter;

    public TaskProcessor(String machineName, int port) {
        _machineName = machineName;
        _port = port;
    }

    @Override
    public void run() {
         try {
            _dataTransporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Tcp, "localhost", _port, TransporterSide.Server);
            _dataTransporter.AddDataReceivedListener(this);
            _dataTransporter.TryConnect();
            _dataTransporter.StartListenIncomingData();
        } catch (TransorterNotRealizedExceptioin ex) {
            ex.printStackTrace();
        } catch (SideOfTransporterNotRealizedException e) {
            e.printStackTrace();
        }

        if (_dataTransporter != null) {
            while(_dataTransporter.IsConnected()) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Frame fr;
    @Override
    public void DataReceived(byte[] data) {
        if (fr == null) {
            fr = new Frame();
            Label label = new Label("123123123");
            fr.add(label);
            fr.setSize(300,300);
            fr.setVisible(true);
        }
    }

    private void Stop()
    {
        _dataTransporter.RemoveDataReceivedListener(this);
        _dataTransporter.Disconnect();
    }
}
