package moapi;

import java.util.*;

/**
* Abstract base class for individual option classes
*
* @author   Clinton Alexander
* @author   Jonathan Brazell
* @version	1.4.7
* @since    0.1
*/
abstract public class ModOption<E> {
	/**
	* The identifier for this option.
	* 
	* @since	0.8
	*/
	private String id;

	/**
	* Display text for this option.
	*/
	protected String name;

  /**
  * Should the option be displayed wide
  * 
  * @since 1.0.0    
  */    
  protected boolean wide = false;

	/**
	* Global Value of this option selector
	*/
	protected E value;

	/**
	* Local/server value of this option
	*/
	protected E localValue;

	/**
	* If we should use the global value
	*/
	protected boolean global = (ModOptionsAPI.isServer() ? false : true);

	/**
	* The callback object
	*/
	protected ModOptionCallback callback = null;
	
	//==============
	// Constructor
	//==============
	
	/**
	* Default constructor, name only.
	*
	* @since	0.8
	* @param	name  Identifier for this option
	*/
	protected ModOption(String name) {
    this(name, name);
	}
	
	/**
	* Default constructor with id and name.
	*
	* @since	0.8
	* @param	id		Identified for this option
	* @param	name	Name for this option
	*/
	protected ModOption(String id, String name) {
		setID(id);
		setName(name);
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
	* Set the current value used for the given scope
	*
	* @since	0.7
	* @param	value	New value for scope
	* @param	scope	Scope value. True for global
	* @return  the option for further operations  	
	*/
	public ModOption setValue(E value, boolean scope) {
    boolean changed = false;
    
		if (scope) {
		  if (this.value!=value) {
        this.value = value;
        changed = true;
      }
		} else {
			if (localValue!=value) {
			  localValue = value;
        changed = true;
			}
		}
	  if (hasCallback() && changed) {
	    getCallback().onChange(this);
    }
    return this;
	}

	/**
	* Set the current used value of this option selector
	* 
	* @param	value		New value
	* @return  the option for further operations  	
	*/
	public ModOption setValue(E value) {
    return setValue(value, global);
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
	public abstract ModOption fromString(String strValue, boolean scope);
	
	/**
	* Set the callback for this option
	*
	* @param	callback	The callback to set
	* @return  the option for further operations  	
	*/
	public ModOption setCallback(ModOptionCallback callback) {
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
	  return getValue(global);
	}
	
	/**
	* Get the value of this selector from the scope
	* given in the first parameter
	*
	* @param	scope	True for global value
	* @return	Value of this option in the given scope
	*/
	public E getValue(boolean scope) {
		if (scope) {
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
	* Get the display string for the option for a global value
	*
	* @since 1.0.0	
	* @return	display string
	*/
	public String getDisplayString() {
		return getDisplayString(true);
	}
	
	/**
	* Get the display string for the option.  Value is local value if scope is
	* true, otherwise global.
	*
	* @since 1.0.0	
	* @param	scope	True for global value
	* @return	display string
	*/
	public String getDisplayString(boolean scope) {
		if ((!scope) && (useGlobalValue())) {
		  return getName() + ": GLOBAL";
		}
		return getName() + ": " + getValue(scope).toString();
	}
	
	/**
	* Returns the callback
	*
	* @return	callback object
	*/
	public ModOptionCallback getCallback() {
		return callback;
	}
	
	/**
	* Check if this option has a callback
	*
	* @return	True if has callback
	*/
	public boolean hasCallback() {
		return (callback instanceof ModOptionCallback);
	}
}
