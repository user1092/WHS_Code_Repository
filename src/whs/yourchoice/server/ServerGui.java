/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;

/**
 * Class for the server's front end controlling the back end through buttons. 
 * 
 * @author		ch1092, skq501, cd828
 * @version		v0.7 20/05/2016
 */
public class ServerGui {
	
	private Server server;
	
	private JFrame serverFrame;
	private JPanel buttonPanel;
	private JPanel passwordPanel;
	private JPanel upperPanel;
	
	private Boolean serverSocketIsOpen = false;
	
	private JPasswordField passwordField;

	/**
	 * Constructor
	 * The constructor will automatically create and populate a server window.
	 */
	public ServerGui() {
		server = new Server();
		
		setupGui();
		
		setupButtons();
		
		serverFrame.validate();
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Main Method to start a new ServerGui. 
	 * @param args
	 */
	public static void main(String[] args) {
		new ServerGui();
	}

	/**
	 * Method to create a new JFrame and set the relevant options
	 */
	private void setupGui() {
		//Create a new frame
		serverFrame = new JFrame();
		
		// Setup window title and size
		serverFrame.setTitle("YourChoice Server");
		serverFrame.setSize(600, 400);
		serverFrame.setVisible(true);
		serverFrame.setResizable(false);
	}

	/**
	 * Method to setup all buttons on the server window
	 */
	private void setupButtons() {
		// Create button panel for the open and close socket buttons
		upperPanel = new JPanel();
		buttonPanel = new JPanel();
		serverFrame.add(upperPanel);
		upperPanel.add(buttonPanel);
		buttonPanel.setLayout(new BorderLayout());
		
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new BorderLayout());
		buttonPanel.add(passwordPanel, BorderLayout.SOUTH);
		
		setupOpenSocketButton();
		
		setupCloseSocketButton();
		
		setupPasswordField();
		
		setupSetPasswordButton();
	}

	/**
	 * This method will setup a button for opening the socket to listen for clients
	 */
	private void setupOpenSocketButton() {
		JButton openSocketButton;
		openSocketButton = new JButton("Open Sockets");
		buttonPanel.add(openSocketButton, BorderLayout.WEST);
				
		openSocketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!serverSocketIsOpen) {					
					server.openSocket();
					serverSocketIsOpen = !server.serverSocket.isClosed();
					server.checkAndAcceptClientConnections();
				} else {
					JOptionPane.showMessageDialog(null, "Sockets already open!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}          
	    });
	}

	/**
	 * This method will setup a button for closing the socket to listen for clients
	 */
	private void setupCloseSocketButton() {
		JButton closeSocketButton;
		closeSocketButton = new JButton("Close Sockets");
		buttonPanel.add(closeSocketButton, BorderLayout.EAST);
		
		closeSocketButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (serverSocketIsOpen) {
					server.closeSocket();
					serverSocketIsOpen = !server.serverSocket.isClosed();
				} else {
					JOptionPane.showMessageDialog(null, "Sockets already closed!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}          
	    });
	}
	
	/**
	 * Method to create a field to enter an administrator password
	 */
	private void setupPasswordField() {
		passwordField = new JPasswordField(20);
		
		// Limit the password length to 20 characters
		PlainDocument document = (PlainDocument) passwordField.getDocument();
        document.setDocumentFilter(new DocumentFilter() {

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String string = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;

                if (string.length() <= 20) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

        });
        
		passwordPanel.add(passwordField, BorderLayout.CENTER);
	}
	
	/**
	 * This method will setup a button for setting the password
	 */
	private void setupSetPasswordButton() {
		JButton setPasswordButton;
		setPasswordButton = new JButton("Set Administrator Password");
		passwordPanel.add(setPasswordButton, BorderLayout.SOUTH);
				
		setPasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Password Set");
				server.setAdminPassword(String.valueOf(passwordField.getPassword()));
			}
	    });
	}

	/**
	 * This method will return the state of the server sockets.
	 * 
	 * @return serverSocketIsOpen	-	True if the socket is open, else false.
	 */
	protected Boolean areSocketsOpen() {
		return serverSocketIsOpen;
	}
		
}
