package moapi.api;

import java.util.*;

/**
* Abstract base class for individual option classes
*
* @author	Clinton Alexander
* @author	Jonathan Brazell
* @version	1.0.0
* @since	0.1
*/
abstract public class ModOption<E> {
	/**
	* The identifier for this option.
	* 
	* @since	0.8
	*/
	private String id;

	/**
	* Given name for this option selector
	*/
	protected String name;

  /**
  * Should the option be displayed wide
  * 
  * @since 1.0.0    
  */    
  protected boolean wide = false;

  /**
  * List defining string manipulation callbacks
  * 
  * @since 1.0.0    
  */
	protected LinkedList<DisplayStringFormatter> formatters = new LinkedList<DisplayStringFormatter>();

	/**
	* Global Value of this option selector
	*/
	protected E value;

	/**
	* Local/ server value f this option
	*/
	protected E localValue;

	/**
	* If we should use the global value
	*/
	protected boolean global = true;

	/**
	* The callback object
	*/
	protected MOCallback callback = null;
	
	//==============
	// Constructor
	//==============
	
	/**
	* Default constructor, requires an ID
	*
	* @since	0.8
	* @param	id		Identifier for this option
	*/
	protected ModOption(String id) {
    this(id, id);
	}
	
	/**
	* Default constructor with a name
	*
	* @since	0.8
	* @param	id		Identified for this option
	* @param	name	Name for this option
	*/
	protected ModOption(String id, String name) {
		setID(id);
		setName(name);
	  setFormatter(StdFormatters.defaultFormat);
	}
	
	//==============
	// Setters
	//==============
	
	/**
	* Sets the ID of this option
	*
	* @since	0.8
	* @param id  The id to set the option to
	* @return  the option for further operations  	
	*/
	private final ModOption setID(String id) {
		this.id = id;
		return this;
	}
	
	/**
	* Set the name of this option
	*
	* @param	name	New name for this option
	* @return  the option for further operations  	
	*/
	protected ModOption setName(String name) {
		this.name = name;
		return this;
	}

	/**
	* Sets the option to show as a full width bar instead of the default half-width
	*
	* @since 1.0.0   
	* @param paramWide - true for wide option, false for half-width option  	
	* @return  the option for further operations  	
	*/
	public ModOption setWide(boolean paramWide) {
		wide = paramWide;
    return this;
	}

	/**
	* Set the text formatting class for option's output string and removes all
	* other formatters
	*
	* @since 1.0.0	
	* @param formatter	String formatter
	* @return  the option for further operations  	
	*/
	public ModOption setFormatter(DisplayStringFormatter formatter) {
  	formatters = new LinkedList<DisplayStringFormatter>();
		formatters.add(formatter);
    return this;
	}
	
	/**
	* Add a new formatter for this option's output string in addition to the
	* formatters already in place	
	*
	* @since	0.6.1
	* @param	formatter	Formatter to add to this options format queue
	* @return  the option for further operations  	
	*/
	public ModOption addFormatter(DisplayStringFormatter formatter) {
		formatters.addFirst(formatter);
    return this;
	}
	
	/**
	* Set the current used value of this option selector
	* 
	* @param	value		New value
	* @return  the option for further operations  	
	*/
	public ModOption setValue(E value) {
		if(global) {
			this.value = value;
		} else {
			localValue = value;
		}
    return this;
	}
	
	/**
	* Set the current value used for the given scope
	*
	* @since	0.7
	* @param	value	New value for scope
	* @param	scope	Scope value. True for global
	* @return  the option for further operations  	
	*/
	public ModOption setValue(E value, boolean scope) {
		if(scope) {
			this.value = value;
		} else {
			localValue = value;
		}
    return this;
	}

	/**
	* Sets the local value of this option
	*
	* @param	value		New value
	* @return  the option for further operations  	
	*/
	public ModOption setLocalValue(E value) {
		return setValue(value, false);
	}
	
	/**
	* Sets global value of this option
	*
	* @param	value		New value
	* @return  the option for further operations  	
	*/
	public ModOption setGlobalValue(E value) {
		return setValue(value, true);
	}
	
	/**
	* Set the scope of the value
	*
	* @param	global	True if use global value only
	* @return  the option for further operations  	
	*/
	public ModOption setGlobal(boolean global) {
		this.global = global;
		return this;
	}

	/**
	* Set the current value from a string with the given scope
	*
	* @since	1.0.0
	* @param	value	New value for scope
	* @param	scope	Scope value. True for global
	* @return  the option for further operations  	
	*/
	abstract public ModOption fromString(String strValue, boolean scope);
	
	/**
	* Set the callback for this option
	*
	* @param	callback	The callback to set
	* @return  the option for further operations  	
	*/
	public ModOption setCallback(MOCallback callback) {
		this.callback = callback;
		return this;
	}
	
	//==============
	// Getters
	//==============
	
	/**
	* Get the ID of this option selector
	*
	* @since	0.8
	* @return	ID of this option
	*/
	public final String getID() {
		return id;
	}
	
	/**
	* Return the name of this option selector
	*
	* @return	Name of option selector
	*/
	public String getName() {
		return name;
	}
	
	/**
	* Check if the option is in a wide format
	*
	* @since 1.0.0
	*/
	public boolean isWide() {
    return wide;
	}
	
	/**
	* Get value of this option selector
	*
	* @return	Value of this option selector
	*/
	public E getValue() {
		if(global) {
			return value;
		} else {
			return localValue;
		}
	}
	
	/**
	* Get the value of this selector from the scope
	* given in the first parameter
	*
	* @param	scope	True for global value
	* @return	Value of this option in the given scope
	*/
	public E getValue(boolean scope) {
		if(global) {
			return getGlobalValue();
		} else {
			return getLocalValue();
		}
	}
	
	/**
	* Returs the global value of this option
	*
	* @return	Global value of this option
	*/
	public E getGlobalValue() {
		return value;
	}
	
	/**
	* Get the local value
	*
	* @return	Local value of this option selector
	*/
	public E getLocalValue() {
		return localValue;
	}
	
	/**
	* Set this option to only use the global value
	*
	* @return	True if only using the global value
	*/
	public boolean useGlobalValue() {
		return global;
	}

	/**
	* Get the display string for the option, which will use a string formatter to
	* decide the output of the text for a global value
	*
	* @since 1.0.0	
	* @return	display string
	*/
	public String getDisplayString() {
		return getDisplayString(false);
	}
	
	/**
	* Get the display string for the option, which will use a string formatter to
	* decide the output of the text.  Value is local value if localMode is true,
	* otherwise global.
	*
	* @since 1.0.0	
	* @param	localMode	True if use local value
	* @return	display string
	*/
	public String getDisplayString(boolean localMode) {
		// String formatter
		String value = "Problem loading value";
		
		if((localMode) && (useGlobalValue())) {
			value = "GLOBAL";
		} else {
			value = getValue(!localMode).toString();
		}
		
		for(DisplayStringFormatter s : formatters) {
		  value = s.manipulate(this, value);
		}
		return value;
	}
	
	/**
	* Returns the callback
	*
	* @return	callback object
	*/
	public MOCallback getCallback() {
		return callback;
	}
	
	/**
	* Check if this option has a callback
	*
	* @return	True if has callback
	*/
	public boolean hasCallback() {
		return (callback instanceof MOCallback);
	}
}