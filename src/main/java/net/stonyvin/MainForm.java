package net.stonyvin;

import net.stonyvin.IRCLib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;

public class MainForm extends JFrame implements WindowListener {
    private JTextArea outputField;
    private JTextField inputField;
    private JButton sendButton;
    private JPanel rootPanel;
    private IRCLib irc;
    private CommandHandler commandHandler;

    public MainForm() {
        super("IRC Client");

        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(this);
        initialize();
        setVisible(true);
    }

    private void initializeLib() {
        String username = JOptionPane.showInputDialog("Enter Username");
        String hostname = JOptionPane.showInputDialog("Enter IRC Server");
        String name = JOptionPane.showInputDialog("Enter Full Name");
        try {
            irc = new IRCLib(hostname, username);
            irc.getConnection().nickCommand();
            irc.getConnection().userCommand(name);
            commandHandler = new CommandHandler(irc);
        } catch (Exception e) {
            Object[] options = { "Retry", "Exit" };
            int n = JOptionPane.showOptionDialog(this, e.getStackTrace(), "Cannot connect",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]);

            if (n == 0) {
                initializeLib();
            } else {
                System.exit(0);
            }
        }
    }

    private void initialize() {
        System.setOut(new PrintStream(new TextAreaOutputStream(outputField)));

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                if (!commandHandler.handle(message)) {
                    irc.getSending().privMsgCommand(irc.getChannel().getJoinedChannels(), message);
                }
            }
        });

        initializeLib();
    }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowClosing(WindowEvent e) {
        if (irc != null) {
            irc.exit();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }
}
