package CorePackage.GUI;

import CorePackage.Events.IWorkFinishedListener;
import CorePackage.Interfaces.IStartWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by nekho on 02-Oct-16.
 */
public class MainWindow extends JFrame implements IWorkFinishedListener{

    private JButton _runButton;

    public MainWindow(String title, IStartWorker workRunner) throws HeadlessException {
        super(title);
        setResizable(false);
        setLayout(null);
        pack();
        setSize(300,200);
        setLocationRelativeTo(null);
        Container pane = getContentPane();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _runButton = new JButton();
        _runButton.setBounds(pane.getWidth()/2 - 50,pane.getHeight()/2 - 15,100,30);
        _runButton.setText("Start Work");
        MainWindow parent = this;
        _runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        _runButton.setEnabled(false);
                        workRunner.StartWork(parent);
                    }
                });
                t.start();
            }
        });
        add(_runButton);
    }

    @Override
    public void WorkFinished() {
        _runButton.setEnabled(true);
    }
}
