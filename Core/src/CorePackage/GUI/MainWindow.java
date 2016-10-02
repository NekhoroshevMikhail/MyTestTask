package CorePackage.GUI;

import CorePackage.Interfaces.IStartWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by nekho on 02-Oct-16.
 */
public class MainWindow extends JFrame {

    public MainWindow(String title, IStartWorker workRunner) throws HeadlessException {
        super(title);
        setResizable(false);
        setLayout(null);
        pack();
        setSize(300,200);
        setLocationRelativeTo(null);
        Container pane = getContentPane();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton button = new JButton();
        button.setBounds(pane.getWidth()/2 - 50,pane.getHeight()/2 - 15,100,30);
        button.setText("Start Work");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(false);
                        workRunner.StartWork();
                    }
                });
                t.start();
            }
        });
        add(button);
    }
}
