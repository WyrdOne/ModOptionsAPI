package moapi.api;

/**
* A text input field option for modoptionsapi
* with customisable maximum length
*
* @author	Clinton Alexander
* @author	Jonathan Brazell
* @version	1.0.1
* @since	0.7
*/
public class ModTextOption extends ModOption<String> {
	/**
	* Maximum length of text specified
	* 0 = No limit
	*
	* @since	0.7
	*/
	private int maxLength = 0;
	
	//==============
	// Constructors
	//==============
	
	/**
	* Constructor for the text option
	*
	* @since	0.7
	* @param	name	Name of option
	*/
	public ModTextOption(String name) {
		this(name, name, 0);
	}
	
	/**
	* Constructor with a given name and ID
	*
  * @version 1.0.0
	* @since	0.8
	* @param	id		ID of option
	* @param	name	Name of option
	*/
	public ModTextOption(String id, String name) {
		this(id, name, 0);
	}
	
	/**
	* Constructor for the text option specifying max length
	*
	* @since	0.7
	* @param	name	Name of option
	* @param	maxLen	Maximum length of text entered
	*/
	public ModTextOption(String name, Integer maxLen) {
		this(name, name, (int) maxLen);
	}
	
	/**
	* Constructor for the text option specifying max length,
	* name and ID
	*
	* @since	0.8
	* @param	id		ID of option
	* @param	name	Name of option
	* @param	maxLen	Maximum length of text entered
	*/
	public ModTextOption(String id, String name, Integer maxLen) {
		this(id, name, (int) maxLen);
	}
	
	/**
	* Constructor for the text option specifying max length
	*
	* @since	0.7
	* @param	name	Name of option
	* @param	maxLen	Maximum length of text entered
	*/
	public ModTextOption(String name, int maxLen) {
		this(name, name, maxLen);
	}
	
	/**
	* Constructor for text option with given name and ID and
	* a given max length
	*
	* @since	0.8
	* @param	id		ID of this option
	* @param	name	Name of this option
	*/
	public ModTextOption(String id, String name, int maxLen) {
		super(id, name);
		setGlobalValue("");
		setMaxLength(maxLen);
		wide = true;
	}
	
	//==============
	// Setters
	//==============

	/**
	* Set the current value from a string with the given scope
	*
	* @since	1.0.0
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
	* @since	0.7
	* @param	maxlen	Maximum length inputtable. 0 or less is infinite
	*/
	public ModTextOption setMaxLength(int maxlen) {
		if(maxlen < 0) { 
			maxlen = 0;
		}
		maxLength = maxlen;
		return this;
	}
	
	/**
	* Set maximum length for the input
	*
	* @since	0.7
	* @param	maxlen	Maximum length inputtable
	*/
	public ModTextOption setMaxLength(Integer maxlen) {
    return setMaxLength(maxlen.intValue());
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
