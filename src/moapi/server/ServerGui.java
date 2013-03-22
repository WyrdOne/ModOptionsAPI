package moapi.server;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import javax.swing.*;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import moapi.*;

/**
* Class to add and handle options in the Minecraft Server GUI 
*
* @author Jonathan Brazell (WyrdOne)
* @version 1.4.7
*/
public class ServerGui implements ActionListener {
	private static ServerGui instance = new ServerGui();
	private static ServerGUI serverGUI;
	private static DedicatedServer dedicatedServer;

	public static ServerGui getInstance() {
		return instance;
	}
  
	public JComponent addGuiButtons(JComponent parent, DedicatedServer serverInstance) {
		serverGUI = (ServerGUI)parent;
		dedicatedServer = serverInstance;
		JPanel jpanel = new JPanel(new FlowLayout(0));
		JButton modOptionsButton = new JButton("Mod Options");
		modOptionsButton.addActionListener(instance);
		jpanel.add(modOptionsButton);
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(instance);
		jpanel.add(exitButton);
		return jpanel;
	}

	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton)event.getSource();
		if (button.getText().equals("Mod Options")) {
			new ServerGuiOptions(dedicatedServer);
		} else if (button.getText().equals("Exit")) {
			dedicatedServer.initiateShutdown();
		}
	}
}
