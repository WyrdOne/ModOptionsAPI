package moapi.client;

import java.util.Hashtable;
import java.util.prefs.InvalidPreferencesFormatException;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.*;
import moapi.*;

/**
* An option for keybindings
* Enforces the concept of only one key per binding
* type.
*
* @author	Clinton Alexander
* @version	0.9.1
* @since 	0.7
*/
public class ModOptionKey extends ModOption<Integer> {
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
	public static final int defaultVal = Keyboard.KEY_NONE;
	
	//==============
	// Constructors
	//==============
	
	/**
	* Constructor with ID for key binding option
	*
	* @since	0.8
	* @param	id		ID of option
	* @param	name	Name of option
	*/
	public ModOptionKey(String id, String name) {
		super(id, name);
		setValue(defaultVal, true);
		setValue(defaultVal, false);
	}
	
	/**
	* Constructor for key binding option
	*
	* @since 	0.7
	* @param	name	Name of option
	*/
	public ModOptionKey(String name) {
		this(name, name);
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
	public ModOption setValue(int value, boolean scope) {
		int curVal = getValue(scope);
		if (value == defaultVal) {
			// Dead branch (CBA to refactor)
			bindings.remove(value);
			super.setValue(value, scope);
		} else if ((getLocalValue()==value && !scope) || (getGlobalValue()==value && scope) || (!isKeyBound(value))) {
			// Remove old value if it exists
			if (bindings.containsKey(curVal)) {
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
		return getName() + ": " + value;
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
		
		if (val == null) {
			return "INVALID";
		} else {
			return val;
		}
	}
}