package moapi.clientgui;

import net.minecraft.src.GuiButton;
import moapi.api.*;

/**
* A MOAPI button representation to allow the storage of the option's ID inside
*
* @author	Clinton Alexander
* @version	1.0
* @since	0.8
*/
public class Button extends GuiButton {
	/**
	* The option for this button
	*
  * @version	1.0
	* @since	0.8
	*/
	private ModOption option = null;
	/**
	* The options menu for this button
	*
  * @version	1.0
	* @since	0.8
	*/
	private ModOptions options = null;
	
	/**
	* Create button with the given option
	*
  * @version	1.0
	* @since	0.8
	* @param	op		Option
	*/
  public Button(int id, int x, int y, int width, int height, ModOption option, boolean worldMode) {
    super(id, x, y, width, height, option.getDisplayString(worldMode));
		this.option = option;
  }
	
	/**
	* Create button with the given option
	* 
  * @version	1.0
	* @since	0.8
	* @param	op		Option
	*/
  public Button(int id, int x, int y, ModOption option, boolean worldMode) {
    this(id, x, y, 200, 20, option, worldMode);
  }

	/**
	* Create button with the given options menu
	*
  * @version	1.0
	* @since	0.8
	* @param	op		Options menu
	*/
  public Button(int id, int x, int y, int width, int height, ModOptions option) {
    super(id, x, y, width, height, option.getName());
		this.options = option;
  }
	
	/**
	* Create button with the given options menu
	* 
  * @version	1.0
	* @since	0.8
	* @param	op		Options menu
	*/
  public Button(int id, int x, int y, ModOptions option) {
    this(id, x, y, 200, 20, option);
  }

	/**
	* Get the ID of the option this button represents
	*
  * @version	1.0
	* @since	0.8
	* @return	ID this button represents
	*/
	public String getID() {
		if(option != null) {
			return option.getID();
		} else {
			return options.getID();
		}
	}
}