// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import client.*;
import common.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientGUI extends JFrame implements ChatIF
{
    //Class variables *************************************************

    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;

    //Instance variables **********************************************

    /**
     * The instance of the client that created this ConsoleChat.
     */
    ChatClient client;
    private JTextArea displayMessage = new JTextArea("", 24, 50);
    private JTextField textField = new JTextField();
    private JScrollPane areaScrollPane;
    private Color red = new Color(183, 62, 62);
    private Color skin = new Color(219, 200, 172);

    //Constructors ****************************************************

    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    public ClientGUI(String host, int port)
    {

        JFrame frame = new JFrame("Simple Chat 4 - Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.setSize(new Dimension(700,500));
        frame.setVisible(true);
        frame.setResizable(false);

        try
        {
            client= new ChatClient(host, port, this);
        }
        catch(IOException exception)
        {
            System.out.println("Error: Can't setup connection!"
                    + " Terminating client.");
            System.exit(1);
        }
    }

    public void addComponentsToPane(Container pane) {

        // ------------------------------------------- PAGE_START ------------------------------------------- //
        FlowLayout flowLayout = new FlowLayout();
        JPanel northPanel = new JPanel(flowLayout);
        northPanel.setBackground(red);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pseudo = textField.getText();
                client.handleMessageFromClientUI("#login " + pseudo);
                textField.setText("");
            }
        });
        loginButton.setBackground(skin);
        JButton logoffButton = new JButton("Logoff");
        logoffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.handleMessageFromClientUI("#logoff");
            }
        });
        logoffButton.setBackground(skin);
        JButton getPortButton = new JButton("Get Port");
        getPortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.handleMessageFromClientUI("#getport");
            }
        });
        getPortButton.setBackground(skin);
        JButton getHostButton = new JButton("Get Host");
        getHostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.handleMessageFromClientUI("#gethost");
            }
        });
        getHostButton.setBackground(skin);
        JButton setHostButton = new JButton("Set Host");
        setHostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = textField.getText();
                client.handleMessageFromClientUI("#sethost " + host);
                textField.setText("");
            }
        });
        setHostButton.setBackground(skin);
        JButton setPortButton = new JButton("Set Port");
        setPortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String port = textField.getText();
                client.handleMessageFromClientUI("#setport " + port);
                textField.setText("");
            }
        });
        setPortButton.setBackground(skin);

        northPanel.add(loginButton);
        northPanel.add(logoffButton);
        northPanel.add(getPortButton);
        northPanel.add(setPortButton);
        northPanel.add(getHostButton);
        northPanel.add(setHostButton);
        pane.add(northPanel, BorderLayout.PAGE_START);
        // ------------------------------------------ PAGE_START END ------------------------------------------ //

        // ------------------------------------------- CENTER ------------------------------------------- //
        FlowLayout center = new FlowLayout();
        JPanel centerPanel = new JPanel(center);
        centerPanel.setBackground(red);
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
        southPanel.setBackground(red);
        textField.setColumns(30);
        JButton buttonSend = new JButton("Send");
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stringToSend = textField.getText();
                if (stringToSend != null){
                    client.handleMessageFromClientUI(stringToSend);
                }
                textField.setText("");
            }
        });
        JButton buttonQuit = new JButton("Quit");
        buttonQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.quit();
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



    //Instance methods ************************************************

    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to the client's message handler.
     */
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
                client.handleMessageFromClientUI(message);
            }
        }
        catch (Exception ex)
        {
            System.out.println
                    ("Unexpected error while reading from console!");
        }
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message)
    {
        displayMessage.append(message + "\n");
        JScrollBar jScrollBar = this.areaScrollPane.getVerticalScrollBar();
        jScrollBar.setValue(jScrollBar.getMaximum());
    }


    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of the Client UI.
     *
     * @param args 0 :  the port ; 1 : The host to connect to.
     */
    public static void main(String[] args)
    {
        String host = "";
        int port = 0;  //The port number

        try
        {
            port = Integer.parseInt(args[0]);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            port = DEFAULT_PORT;
        }

        try {
            host = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            host = "localhost";
        }
        ClientGUI chatGUI = new ClientGUI(host, port);
        chatGUI.accept();  //Wait for console data
    }
}
//End of ConsoleChat class
