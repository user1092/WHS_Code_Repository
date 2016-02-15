/**
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
 * @author ch1092
 *
 */
public class ServerGui {
	
	Boolean openSockets = false;
	static Server server;

	public ServerGui() {
		JButton openSocketButton;
		JFrame serverFrame = new JFrame(); 
		
		// Setup window title and size
		serverFrame.setTitle("YourChoice Server");
		serverFrame.setSize(600, 400);
		serverFrame.setVisible(true);
		serverFrame.setResizable(false);
		JPanel contentPanel = new JPanel();
		serverFrame.add(contentPanel);
		contentPanel.setLayout(new BorderLayout());
		
		// Create buttons to open and close the sockets
		JPanel buttonPanel = new JPanel();
		//JButton openSocketButton;
		openSocketButton = new JButton("Open Sockets");
		buttonPanel.add(openSocketButton, BorderLayout.NORTH);
		serverFrame.add(buttonPanel);
		
		openSocketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!openSockets) {					
					server.openSockets();
					openSockets = true;
				} else {
					JOptionPane.showMessageDialog(null, "Sockets already open!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}          
	    });
		
		JButton closeSocketButton;
		closeSocketButton = new JButton("Close Sockets");
		buttonPanel.add(closeSocketButton, BorderLayout.SOUTH);
		
		
		closeSocketButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (openSockets) {
					server.closeSockets();
					openSockets = false;
					
				} else {
					JOptionPane.showMessageDialog(null, "Sockets already closed!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}          
	    });
		
		serverFrame.validate();
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ServerGui();

	}

	/**
	 * @return
	 */
	protected Boolean areSocketsOpen() {
		return openSockets;
	}

}
