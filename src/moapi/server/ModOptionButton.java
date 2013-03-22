package moapi.server;

import java.awt.event.*;
import javax.swing.*;
import moapi.*;

/**
* A MOAPI button representation to allow the storage of the option inside
*
* @author Jonathan Brazell
* @version	1.4.7
*/
public class ModOptionButton extends JButton {
	/** The option for this button */
	protected ModOption option = null;
	
	public ModOptionButton(ModOption option, ActionListener action) {
		super();
		this.option = option;
		setText(option.getDisplayString(false));
	    addActionListener(action);
	}

	/**
	* Get the option this button represents
	*
	* @return	option this button represents
	*/
	public ModOption getOption() {
		return option;
	}
}
