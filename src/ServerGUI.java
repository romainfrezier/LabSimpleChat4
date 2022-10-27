import common.ChatIF;
import server.EchoServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerGUI extends JFrame implements ChatIF {

    static final int DEFAULT_PORT = 5555;

    EchoServer sv;

    private JTextArea displayMessage = new JTextArea("", 24, 50);
    private JTextField textField = new JTextField();
    private JScrollPane areaScrollPane;
    private Color green = new Color(155, 161, 123);
    private Color skin = new Color(207, 185, 151);

    public ServerGUI(int port) {
        JFrame frame = new JFrame("Simple Chat 4 - Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.setSize(new Dimension(700,500));
        frame.setVisible(true);
        frame.setResizable(false);
        this.sv = new EchoServer(port, this);
    }

    public void addComponentsToPane(Container pane) {

        // ------------------------------------------- PAGE_START ------------------------------------------- //
        FlowLayout flowLayout = new FlowLayout();
        JPanel northPanel = new JPanel(flowLayout);
        northPanel.setBackground(green);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sv.handleMessageFromServerUI("#start");
            }
        });
        startButton.setBackground(skin);
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sv.handleMessageFromServerUI("#stop");
            }
        });
        stopButton.setBackground(skin);
        JButton getPortButton = new JButton("Get Port");
        getPortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sv.handleMessageFromServerUI("#getport");
            }
        });
        getPortButton.setBackground(skin);
        JButton closeButton = new JButton("Close Connection");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = textField.getText();
                sv.handleMessageFromServerUI("#close");
                textField.setText("");
            }
        });
        closeButton.setBackground(skin);
        JButton setPortButton = new JButton("Set Port");
        setPortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String port = textField.getText();
                sv.handleMessageFromServerUI("#setport " + port);
                textField.setText("");
            }
        });
        setPortButton.setBackground(skin);

        northPanel.add(startButton);
        northPanel.add(stopButton);
        northPanel.add(closeButton);
        northPanel.add(getPortButton);
        northPanel.add(setPortButton);

        pane.add(northPanel, BorderLayout.PAGE_START);
        // ------------------------------------------ PAGE_START END ------------------------------------------ //

        // ------------------------------------------- CENTER ------------------------------------------- //
        FlowLayout center = new FlowLayout();
        JPanel centerPanel = new JPanel(center);
        centerPanel.setBackground(green);
        displayMessage.setBackground(skin);
        displayMessage.setBorder(new EmptyBorder(5,5,5,5));
        displayMessage.setLineWrap(true);
        areaScrollPane = new JScrollPane(displayMessage);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        centerPanel.add(areaScrollPane);
        pane.add(centerPanel, BorderLayout.CENTER);
        // ------------------------------------------ CENTER END ------------------------------------------ //

        // ------------------------------------------- PAGE_END ------------------------------------------- //
        JPanel southPanel = new JPanel();
        southPanel.setBackground(green);
        textField.setColumns(30);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stringToSend = textField.getText();
                if (stringToSend != null){
                    sv.handleMessageFromServerUI(stringToSend);
                }
                textField.setText("");
            }
        });
        JButton buttonSend = new JButton("Send");
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stringToSend = textField.getText();
                if (stringToSend != null){
                    sv.handleMessageFromServerUI(stringToSend);
                }
                textField.setText("");
            }
        });
        JButton buttonQuit = new JButton("Quit");
        buttonQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sv.handleMessageFromServerUI("#quit");
            }
        });
        buttonQuit.setBackground(skin);
        buttonSend.setBackground(skin);
        southPanel.add(textField);
        southPanel.add(buttonSend);
        southPanel.add(buttonQuit);
        pane.add(southPanel, BorderLayout.PAGE_END);
        // ------------------------------------------ PAGE_END END ------------------------------------------ //
    }

    public void accept()
    {
        try
        {
            BufferedReader fromConsole =
                    new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true)
            {
                message = fromConsole.readLine();
                sv.handleMessageFromServerUI(message);
            }
        }
        catch (Exception ex)
        {
            System.out.println
                    ("Unexpected error while reading from console!");
        }
    }

    /**
     * This method is responsible for the creation of
     * the server instance (there is no UI in this phase).
     *
     * @param args [0] The port number to listen on.  Defaults to 5555
     *          if no argument is entered.
     */
    public static void main(String[] args)
    {
        int port = 0; //Port to listen on

        try
        {
            port = Integer.parseInt(args[0]); //Get port from command line
        }
        catch(Throwable t)
        {
            port = DEFAULT_PORT; //Set port to 5555
        }

        ServerGUI chat = new ServerGUI(port);

        try
        {
            chat.accept();
        }
        catch (Exception ex)
        {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }

    @Override
    public void display(String message) {
        displayMessage.append(message + "\n");
        JScrollBar jScrollBar = this.areaScrollPane.getVerticalScrollBar();
        jScrollBar.setValue(jScrollBar.getMaximum());
    }
}
