package moapi.server;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import moapi.*;

/**
* A MOAPI slider representation to allow the storage of the option inside
*
* @author Jonathan Brazell
* @version	1.4.7
*/
public class ModOptionGuiSlider extends JSlider implements ChangeListener {
	/** The option for this button */
	protected ModOption option = null;

	public ModOptionGuiSlider(ModOption option) {
		super(((ModOptionSlider)option).getLowVal(), ((ModOptionSlider)option).getHighVal(), ((ModOptionSlider)option).getValue(false));
		int ticks = ((ModOptionSlider)option).getHighVal() - ((ModOptionSlider)option).getLowVal(); 
		setMajorTickSpacing(ticks);
	    setMinorTickSpacing(ticks / 4);
	    setPaintTicks(true);
		setPaintLabels(true);
		this.option = option;
		addChangeListener(this);
	}

	/**
	* Get the option this button represents
	*
	* @return	option this button represents
	*/
	public ModOption getOption() {
		return option;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
	    if (!getValueIsAdjusting()) {
	    	option.setValue((int)getValue(), false);
	    }
	}
}
