package gui.view;

import javax.swing.*;

public class MainFrame extends JFrame implements Runnable{

    public MainFrame(JTabbedPane jTabbedPane, String title)
    {
        setTitle(title);
        setSize(600,400);
        getContentPane().add(jTabbedPane);
//        pack();
    }

    public void run()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
