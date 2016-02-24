/**
 * ServerGui.java		v0.6 23/02/2016
 * 
 * 
 */

package com.whs.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Class for the server's front end controlling the back end through buttons. 
 * 
 * @author		user1092, guest501
 * @version		v0.6 23/02/2016
 */
public class ServerGui {
	
	private Server server;
	
	private JFrame serverFrame;
	private JPanel buttonPanel;
	private JPanel upperPanel;
	
	private Boolean serverSocketIsOpen = false;

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
		serverFrame.add(upperPanel, BorderLayout.NORTH);
		upperPanel.add(buttonPanel, BorderLayout.CENTER);
		buttonPanel.setLayout(new BorderLayout());
		
		setupOpenSocketButton();
		
		setupCloseSocketButton();
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
	 * This method will return the state of the server sockets.
	 * 
	 * @return serverSocketIsOpen	-	True if the socket is open, else false.
	 */
	protected Boolean areSocketsOpen() {
		return serverSocketIsOpen;
	}
		
}
