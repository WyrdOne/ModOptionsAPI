package moapi.server;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import moapi.*;

public class ServerGuiOptions extends JFrame implements ActionListener {
	private final DedicatedServer dedicatedServer;
	private JPanel contentPane = (JPanel)this.getContentPane();
	private JPanel optionPanel;
	private ModOptions modOptions 	= null;
	private String worldName;
	private boolean	worldMode = false;

	public ServerGuiOptions(DedicatedServer dedicatedServer) {
		this.dedicatedServer = dedicatedServer;
		worldName = dedicatedServer.getFolderName(); 
		// Set window defaults
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(300, 400);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		contentPane.setLayout(new BorderLayout());
		// Add option panel
		optionPanel = new JPanel();
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
		contentPane.add(new JScrollPane(optionPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		// Add Done Button
		JButton button = new JButton();
		button.setText("Done");
	    button.addActionListener(this);
		contentPane.add(button, BorderLayout.PAGE_END);
		// Add options to panel
		initGui();        
	}
	
  	public void initGui() {
  		optionPanel.removeAll();
		if (modOptions == null) {
			loadModList();
		} else {
			loadModOptions();
		}
		optionPanel.repaint();
  	}

  	private void loadModList() {	  
		ModOptions[] optionArray = ModOptionsAPI.getServerMods();
		setTitle("Mod Options List");
  		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
  		if (optionArray.length==0) {
  			optionPanel.add(new JLabel("No mod options found."));
  		} else {
  			for (int idx=0; idx<optionArray.length; idx++) {
  				ModOptionButton button = new ModOptionButton(optionArray[idx], this);
  				button.setMinimumSize(new Dimension(250, 30));
  				button.setPreferredSize(new Dimension(290, 30));
  				button.setMaximumSize(new Dimension(290, 30));
  				optionPanel.add(button);
  			}
  		}
  		setVisible(true);
  	}
  
  	private void loadModOptions() {
		setTitle("Options for " + modOptions.getName());
  		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
		ModOption[] options = modOptions.getServerOptions();
		boolean found = false;
		for (ModOption option : options) {
			found = true;
			option.setGlobal(false); // Server uses local values
			JPanel panel = new JPanel();
		    if (option instanceof ModOptionSlider) {
		    	JLabel label = new JLabel(((ModOption)option).getName(), JLabel.CENTER);
		    	ModOptionGuiSlider slider = new ModOptionGuiSlider(option);
		    	label.setLabelFor(slider);
		    	panel.add(label);
		    	panel.add(slider);
		    } else if (option instanceof ModOptionText) {
		    	JLabel label = new JLabel(((ModOption)option).getName(), JLabel.RIGHT);
		    	ModOptionGuiTextField textField = new ModOptionGuiTextField(option);
		    	label.setLabelFor(textField);
		    	panel.add(label);
		    	panel.add(textField);
			} else {
				panel.add(new ModOptionButton(option, this));
			}
		    optionPanel.add(panel);
		}
		if (!found) {
  			optionPanel.add(new JLabel("No mod options found."));
		}
  		setVisible(true);
	}

  	public void actionPerformed(ActionEvent event) {
  	    JButton button = (JButton)event.getSource();
  	    if (button.getText().equals("Done")) {
  	    	if (modOptions==null) {
  	    	    dispose();
  	    	} else if (modOptions.getParent()!=null) {
  	    		modOptions.saveValues(MinecraftServer.getServer().getFolderName());
  	    		modOptions = modOptions.getParent();
  	    		initGui();
  	    	} else  {
  	    		modOptions.saveValues(MinecraftServer.getServer().getFolderName());
  	    		modOptions = null;
  	    		initGui();
  	    	}
  	    } else if (button instanceof ModOptionButton) {
  	    	ModOption option = ((ModOptionButton)button).option; 
  	    	if (option instanceof ModOptions) {
  	    		modOptions = (ModOptions)option;
  	    		initGui();
  	    	}
  			if ((!option.hasCallback()) || (option.getCallback().onClick(option))) {
	  	    	if (option instanceof ModOptionMulti) {
					String nextVal = ((ModOptionMulti)option).getNextValue(((ModOptionMulti)option).getLocalValue());
		    		option.setLocalValue(nextVal);
				} else if (option instanceof ModOptionBoolean) {
					option.setValue(!((ModOptionBoolean)option).getValue());
				} else if (option instanceof ModOptionMapped) {
	    			int nextVal = ((ModOptionMapped)option).getNextValue(((ModOptionMapped)option).getLocalValue());
	    			option.setLocalValue(nextVal);
				}
  			}
			button.setText(option.getDisplayString(false));
			button.repaint();
  	    }
  	}
}
