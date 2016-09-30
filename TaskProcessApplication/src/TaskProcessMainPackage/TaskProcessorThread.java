package TaskProcessMainPackage;

import Exceptions.SideOfTransporterNotRealizedException;
import Exceptions.TransorterNotRealizedExceptioin;
import Factory.PossibleTransport;
import Factory.TransporterCreator;
import TransportCommon.IDataReceivedListener;
import TransportCommon.IDataTransporter;
import TransportCommon.TransporterSide;

import javax.swing.*;
import java.awt.*;

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
            _dataTransporter = TransporterCreator.CreateDataTransporter(PossibleTransport.Tcp, "localhost", portNumber, TransporterSide.Server);
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
        Frame fr = new Frame();
        Label label = new Label("123123123");
        fr.add(label);
        fr.setSize(300,300);
        fr.setVisible(true);
    }
}
