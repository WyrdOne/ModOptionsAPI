package moapi;

/**
* A text input field option for modoptionsapi
* with customisable maximum length
*
* @author	Clinton Alexander
* @author	Jonathan Brazell
* @version	1.0.1
* @since	0.7
*/
public class ModOptionText extends ModOption<String> {
	/**
	* Maximum length of text specified
	* @note 0 = No limit
	*
	* @since	0.7
	*/
	private int maxLength = 0;
	
	//==============
	// Constructors
	//==============
	
	/**
	* Constructor for text option with given name and ID and
	* a given max length
	*
	* @param	id		ID of this option
	* @param	name	Name of this option
	*/
	public ModOptionText(String id, String name, int maxLen) {
		super(id, name);
		setGlobalValue("");
		setLocalValue("");
		setMaxLength(maxLen);
		wide = true;
	}
	
	/**
	* Constructor for the text option
	*
	* @param	name	Name of option
	*/
	public ModOptionText(String name) {
		this(name, name, 0);
	}
	
	/**
	* Constructor with a given name and ID
	*
	* @param	id		ID of option
	* @param	name	Name of option
	*/
	public ModOptionText(String id, String name) {
		this(id, name, 0);
	}
	
	/**
	* Constructor for the text option specifying max length
	*
	* @param	name	Name of option
	* @param	maxLen	Maximum length of text entered
	*/
	public ModOptionText(String name, int maxLen) {
		this(name, name, maxLen);
	}
	
	//==============
	// Setters
	//==============

	/**
	* Set the current value from a string with the given scope
	*
	* @param	value	New value for scope
	* @param	scope	Scope value. True for global
	* @return  the option for further operations  	
	*/
	public ModOption fromString(String strValue, boolean scope) {
	  return setValue(strValue, scope);
  }
	
	/**
	* Set maximum length for the input
	*
	* @param	maxlen	Maximum length inputtable. 0 or less is infinite
	*/
	public ModOptionText setMaxLength(int maxlen) {
		if(maxlen < 0) { 
			maxlen = 0;
		}
		maxLength = maxlen;
		return this;
	}
	
	//==============
	// Getters
	//==============
	
	/**
	* Get the maximum value for the input length
	*
	* @since	0.7
	* @return	Maximum length value
	*/
	public int getMaxLength() {
		return maxLength;
	}
}
