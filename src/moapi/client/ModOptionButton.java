package moapi.client;

import net.minecraft.src.*;
import moapi.*;

/**
* A MOAPI button representation to allow the storage of the option inside
*
* @author	Clinton Alexander
* @author Jonathan Brazell
* @version	1.4.7
* @since	0.8
*/
public class ModOptionButton extends GuiButton {
	private static final int SMALL_WIDTH = 150;
	private static final int WIDE_WIDTH  = 304;
	
	/**
	* The option for this button
	*
  * @version	1.0
	* @since	0.8
	*/
	protected ModOption option = null;

	/** Value determining whether we are in the local/world scope for option values or not */
	protected boolean worldMode;
	
	/**
	* Create button with the given option
	*
  * @version	1.0
	* @since	0.8
	* @param	op		Option
	*/
  public ModOptionButton(int id, int x, int y, int width, int height, ModOption option, boolean worldMode) {
    super(id, x, y, width, height, option.getDisplayString(!worldMode));
		this.option = option;
		this.worldMode = worldMode;
  }
	
	/**
	* Create button with the given option
	* 
  * @version	1.0
	* @since	0.8
	* @param	op		Option
	*/
  public ModOptionButton(int id, int x, int y, ModOption option, boolean worldMode) {
    this(id, x, y, (option.isWide()) ? WIDE_WIDTH : SMALL_WIDTH, 20, option, worldMode);
  }

  public void updateDisplayString() {
    displayString = option.getDisplayString(!worldMode);
  }

	/**
	* Get the option this button represents
	*
	* @return	option this button represents
	*/
  public ModOption getOption() {
    return option;
  }

	/**
	* Get the ID of the option this button represents
	*
  * @version	1.0
	* @since	0.8
	* @return	ID this button represents
	*/
	public String getID() {
	  return option.getID();
	}
}
