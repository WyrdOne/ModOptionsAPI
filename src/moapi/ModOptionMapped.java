package moapi;

import java.util.*;

/**
* Multiple Selector API with Integer -> String mappings
*
* @author	Clinton Alexander
* @author	Jonathan Brazell
* @version	1.0.0
* @since	0.7
*/
public class ModOptionMapped extends ModOption<Integer> {
	/**
	* Collection of possible values of this selector
	*/
	private LinkedHashMap<Integer, String> values = new LinkedHashMap<Integer, String>();
	
	//==============
	// Constructors
	//==============
	
	/**
	* Creates a multi selector with the given name/id and no values
	*
	* @param	id		ID of this option
	* @param	name	Name of this option
	*/
	private ModOptionMapped(String id, String name) {
		super(id, name);
	}
	
	/**
	* Create a multiple selector with no values
	*
	* @param	name	Name of selector
	*/
	private ModOptionMapped(String name) {
		this(name, name);
	}

	/**
	* Create a multiple selector with the given keys and labels
	*
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	name	Name of selector
	* @param	labels	Labels for values
	* @param	keys		Values for the selector
	*/
	public ModOptionMapped(String id, String name, String[] labels, int[] keys) {
		this(id, name);
		if (keys.length != labels.length) {
			throw new IndexOutOfBoundsException("Keys and labels must have same # of entries");
		} else {
			for (int x=0; x<keys.length; x++) {
				addValue(labels[x], keys[x]);
			}
		}
	}

	/**
	* Create a multiple selector with the given keys and labels
	*
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	name	Name of selector
	* @param	labels	Labels for values
	* @param	keys		Values for the selector
	*/
	public ModOptionMapped(String name, String[] labels, int[] keys) {
		this(name, name, labels, keys);
	}
	
	//==============
	// Adders
	//==============
	
	/**
	* Add a single value to this selector
	* 
	* @param	key		Key to add value to
	* @param	value	Value to add
	*/
	public ModOptionMapped addValue(String value, int key) {
		if (values.size()==0) {
			this.value = key;
			localValue = key;
		} 
		this.values.put(key, value);
		return this;
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
    return setValue(Integer.valueOf(strValue), scope);
  }

	//==============
	// Getters
	//==============
	
	/**
	* Gets the string representation
	*
	* @param	key	Key to get string rep of
	* @return	String representation of a value
	*/
	public String getStringValue(int key) {
		return values.get(key);
	}
	
	/**
	* Get the display string for the option.  Value is local value if scope is
	* true, otherwise global.
	*
	* @param	scope	True for global value
	* @return	display string
	*/
	public String getDisplayString(boolean scope) {
		if ((!scope) && (useGlobalValue())) {
		  return getName() + ": GLOBAL";
		}
		return getName() + ": " + getStringValue(getValue(scope));
	}
	
	/**
	* Gets the next value in this selector
	*
	* @param	i	Current value
	* @return	Next value
	*/
	public Integer getNextValue(Integer i) {
		Integer cur			= null;
		boolean found 		= false;
		boolean written		= false;
		// We need the first key due to this being circular
		boolean firstFound 	= false;
		Integer firstKey 	= null;
		
		// Find next value
		Set<Map.Entry<Integer, String>> s = values.entrySet();
		for(Map.Entry<Integer, String> entry : s) {
			// Ensure we have the first key incase of looparouns
			if(!firstFound) {
				firstKey = entry.getKey();
				firstFound = true;
			}
			if(!written) {
				if(found) {
					cur 	= entry.getKey();
					written = true;
				}
				if(entry.getKey().equals(i)) {
					found = true;
				}
			}
		}
		
		// Looparound back to first key
		if(!written) {
			cur = firstKey;
		}
		
		return cur;
	}
	
	/**
	* Gets the next value in this selector
	*
	* @param	i	Current value
	* @return	Next value
	*/
	public Integer getNextValue(int i) {
		return getNextValue(new Integer(i));
	}
}
