package TaskProcessMainPackage;

import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransorterNotRealizedExceptioin;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

import javax.swing.*;

/**
 * Created by nekho on 30-Sep-16.
 */
public class TaskProcessorThread extends Thread implements IDataReceivedListener {
    private String _machineName;
    private int _port;
    private IDataTransporter _dataTransporter;

    public TaskProcessorThread(String machineName, int port) {
        super();
        _machineName = machineName;
        _port = port;
    }

    @Override
    public synchronized void start() {
        short portNumber = (short)_port;
        try {
            _dataTransporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Tcp, "127.0.0.1", portNumber, TransporterSide.Server);
            _dataTransporter.TryConnect();
        } catch (TransorterNotRealizedExceptioin ex) {
            ex.printStackTrace();
        } catch (SideOfTransporterNotRealizedException e) {
            e.printStackTrace();
        }

        if (_dataTransporter != null && _dataTransporter.IsConnected()) {
            super.start();
        }
    }

    private void Stop(){
        _dataTransporter.Disconnect();
    }

    @Override
    public void DataReceived(byte[] data) {
        JFrame frame = new JFrame("test");
        frame.setSize(300,300);
        JLabel label = new JLabel(data.toString());
        frame.add(label);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
