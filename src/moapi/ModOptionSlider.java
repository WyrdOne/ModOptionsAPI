package moapi;

/**
* Provides an interface to create a simple bounded slider
*
* @author	Clinton Alexander
* @author	Jonathan Brazell
* @version	1.0.0
* @since	0.1
*/
public class ModOptionSlider extends ModOption<Integer> {
	/**
	* Lowest value of slider
	*/
	private int low = 0;

	/**
	* Highest value of slider
	*/
	private int high = 100;
	
	//==============
	// Constructors
	//==============
	
	/**
	* Create a slider option with a given id and name
	*
	* @param	id		ID value for this slider
	* @param	name	Name of this slider
	*/
	public ModOptionSlider(String id, String name) {
		super(id, name);
		value = 1;
		localValue = 1;
	}
	
	/**
	* Create a slider with given name
	*
	* @param	name	Name of this slider
	*/
	public ModOptionSlider(String name) {
		this(name, name);
	}
	
	/**
	* Create a bounded slider with a given ID and name
	*
	* @param	id		ID of this option
	* @param	name	Name of slider
	* @param	low		Lowest value of slider
	* @param	high	Highest value of slider
	*/
	public ModOptionSlider(String id, String name, int low, int high) {
		this(id, name);
		this.low = low;
		this.high = high;
	}
	
	/**
	* Create a bounded slider with a given name
	*
	* @param	name	Name of slider
	* @param	low		Lowest value of slider
	* @param	high	Highest value of slider
	*/
	public ModOptionSlider(String name, int low, int high) {
		this(name, name, low, high);
	}
	
	//==============
	// Getters
	//==============
	
	/**
	* Get the highest value of the slider
	*
	* @return	Highest value of the slider
	*/
	public int getHighVal() {
		return high;
	}
	
	/**
	* Get the lowest value of the slider
	*
	* @return	Lowest value of the slider
	*/
	public int getLowVal() {
		return low;
	}
	
	/**
	* Returns a bounded value
	*
	* @param	value	Unbounded value
	* @param	lower	Lower bound
	* @param	upper	Upper bound
	* @return	Bounded value
	*/
	private int getBoundedValue(int value, int lower, int upper) {
		if (value < lower) {
			return lower;
		} else if (value > upper) {
			return upper;
		} else {
			return value;
		}
	}
	
	//==============
	// Setters
	//==============
	
	/**
	* Set the value
	*
	* @param	value	Value being set
	*/
	public ModOption setValue(int value) {
		return super.setValue(getBoundedValue(value, low, high));
	}
	
	/**
	* Set the current value used for the given scope
	*
	* @param	value	New value for scope
	* @param	scope	Scope value. True for global
	* @return  the option for further operations  	
	*/
	public ModOption setValue(int value, boolean scope) {
		return super.setValue(getBoundedValue(value, low, high), scope);
	}
	
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
}