package moapi;

import java.util.LinkedList;

/**
* Multiple Selector API
*
* @author	Clinton Alexander
* @author	Jonathan Brazell
* @version	1.0.0
* @since	0.1
*/
public class ModOptionMulti extends ModOption<String> {
	/**
	* Collection of possible values of this selector
	*/
	private LinkedList<String> values = new LinkedList<String>();
	
	//==============
	// Constructors
	//==============
	
	/**
	* Creates a multiple selector with no values and the given name/id
	*
	* @param	id		ID of selector
	* @param	name	Name of selector
	*/
	public ModOptionMulti(String id, String name) {
		super(id, name);
	}
	
	/**
	* Create a multiple selector with no values
	*
	* @param	name	Name of selector
	*/
	public ModOptionMulti(String name) {
		this(name, name);
	}
	
	/**
	* Creates a multiple selector with given values and given name/id
	*
	* @param	id		ID of the selector
	* @param	name	Name of selector
	* @param	values	Values for selector
	*/
	public ModOptionMulti(String id, String name, String[] values) {
		this(id, name);
		if(values.length > 0) {
			for(String value : values) {
	      addValue(value);
			}
		}
	}

	/**
	* Creates a multiple selector with given values
	*
	* @param	name	Name of selector
	* @param	values	Values for selector
	*/
	public ModOptionMulti(String name, String[] values) {
		this(name, name, values);
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

	//==============
	// Adders
	//==============
	
	/**
	* Add a single value to this selector
	* 
	* @param	value	Value to add
	*/
	public ModOptionMulti addValue(String value) {
		if (values.size()==0) {
			this.value = value;
			this.localValue = value;
		}
		this.values.add(value);
		return this;
	}
	
	//==============
	// Getters
	//==============
	
	/**
	* Gets the next value in this selector
	*
	* @param	s	Current value
	* @return	Next value
	*/
	public String getNextValue(String s) {
		int index = 0;
		for(int x = 0; x < values.size(); x++) {
			if(values.get(x).equals(s)) {
				index = x;
			}
		}
		return values.get((index + 1) % values.size());
	}
}