package moapi;

/**
* Boolean Option API
*
* @author	Clinton Alexander
* @author	Jonathan Brazell
* @version	1.0.0
* @since	0.1
*/
public class ModOptionBoolean extends ModOption<Boolean> {
	private String onVal 	= "On";
	private String offVal 	= "Off";
	
	//==============
	// Constructors
	//==============
	
	/**
	* Creates an On/Off boolean
	*
	* @param	name		Name of this option
	*/
	public ModOptionBoolean(String name) {
		this(name, name, false);
	}

	/**
	* Creates an On/Off boolean with the given ID
	*
	* @param	id			ID for this option to use
	* @param	name		Name of this option
	* @param  value   Default value for option	
	*/
	public ModOptionBoolean(String id, String name, boolean value) {
		super(id, name);
		this.value = value;
		this.localValue = value;
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
    return setValue(Boolean.valueOf(strValue), scope);
  }


	/**
	* Set the display values for this boolean
	*
	* @param   onVal   String to display if boolean is true
	* @param   offVal  String to display if boolean is false
	* @return  the option for further operations  	
	*/
	public ModOptionBoolean setLabels(String onVal, String offVal) {
		this.onVal = onVal;
		this.offVal = offVal;
		return this;
	}

	//==============
	// Getters
	//==============
	
	/**
	* Get the display string for the option.  Value is local value if scope is
	* true, otherwise global.
	*
	* @since 1.0.0	
	* @param	localMode	True if use local value
	* @return	display string
	*/
	public String getDisplayString(boolean scope) {
		if ((!scope) && (useGlobalValue())) {
		  return getName() + ": GLOBAL";
		}
		return getName() + ": " + getStringValue(getValue(scope));
	}
	
	/**
	* Returns the string value of boolean based on this option
	*
	* @param	value	Value to get string for
	* @return	String representation
	*/
	public String getStringValue(boolean value) {
    return ((value) ? onVal : offVal);
	}
}