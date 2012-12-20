package moapi.clientgui;

import org.lwjgl.input.Keyboard;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import java.util.Hashtable;
import java.util.prefs.InvalidPreferencesFormatException;
import moapi.api.*;

/**
* An option for keybindings
* Enforces the concept of only one key per binding
* type.
*
* @author	Clinton Alexander
* @version	0.9.1
* @since 	0.7
*/
public class ModKeyOption extends ModOption<Integer> {
	/**
	* Current key bindings, implemented as a single value due to the
	* fact that keys can only have one configuration per world
	*
	* @since 	0.7
	*/
	private static Hashtable<Integer, ModOption> bindings = new Hashtable<Integer, ModOption>();
	/**
	* The default Integer
	*
	* @since 	0.7
	*/
	public static final Integer defaultVal = Keyboard.KEY_NONE;
	
	//==============
	// Constructors
	//==============
	
	/**
	* Constructor for key binding option
	*
	* @since 	0.7
	* @param	name	Name of option
	*/
	public ModKeyOption(String name) {
		this(name, name);
	}
	
	/**
	* Constructor with ID for key binding option
	*
	* @since	0.8
	* @param	id		ID of option
	* @param	name	Name of option
	*/
	public ModKeyOption(String id, String name) {
		super(id, name);
		setValue(defaultVal, true);
		setValue(defaultVal, false);
	}
	
	//==============
	// Setters
	//==============
	
	/**
	* Set the current used value of this option selector
	*
	* @since 	0.7
	* @param	value		New value
	*/
	public ModOption setValue(int value) {
		return setValue(new Integer(value), global);
	}
	
	/**
	* Set the current used value of this option selector fr a given 
	* scope
	*
	* @since 	0.7
	* @throws	KeyAlreadyBoundException	When attempting to remind a key
	* @param	value		New value
	*/
	public ModOption setValue(Integer value) {
		return setValue(value, global);
	}
	
	/**
	* Set the current used value of this option selector for a given
	* scope
	*
	* @since 	0.7
	* @throws	KeyAlreadyBoundException	When attempting to remind a key
	* @param	value		New value
	*/
	public ModOption setValue(int value, boolean scope) {
		return setValue(new Integer(value), scope);
	}
	
	/**
	* Set the current used value of this option selector
	*
	* @since 	0.7
	* @param	value		New value
	*/
	public ModOption setValue(Integer value, boolean scope) {
		Integer curVal = getValue(scope);
		if(value == defaultVal) {
			// Dead branch (CBA to refactor)
			bindings.remove(value);
			super.setValue(value, global);
		} else if((getLocalValue() == value && !global)	|| (getGlobalValue() == value && global) || (!isKeyBound(value))) {
			// Remove old value if it exists
			if((curVal != null) && (bindings.containsKey(curVal))) {
				bindings.remove(curVal);
			}
			super.setValue(value, scope);
			bindings.put(value, this);
		}
		return this;
	}
	
	/**
	* Set the current value used for the given scope
	*
	* @since	1.0.0
	* @param	value	New value for scope
	* @param	scope	Scope value. True for global
	* @return  the option for further operations  	
	*/
	public ModOption fromString(String strValue, boolean scope) {
    return setValue(Integer.valueOf(strValue), scope);
	}

	//==============
	// Checks
	//==============
	
	/**
	* Check if a key is already bound
	*
	* @since 	0.7
	* @param	c	Integer to check
	* @return	True if already bound
	*/
	public static boolean isKeyBound(Integer c) {
		return ((!c.equals(defaultVal)) && bindings.containsKey(c));
	}
	
	//==============
	// Getters
	//==============
	
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
			value = getKeyName(getValue(!localMode));
		}
		for(DisplayStringFormatter s : formatters) {
		  value = s.manipulate(this, value);
		}
		return value;
	}

	/**
	* Get a name of a key. "INVALID" for no value
	*
	* @since 	0.7
	* @param	key		Key to get value for
	* @return	String for key
	*/
	public static String getKeyName(Integer key) {
		String val = Keyboard.getKeyName(key);
		
		if(val == null) {
			return "INVALID";
		} else {
			return val;
		}
	}
}