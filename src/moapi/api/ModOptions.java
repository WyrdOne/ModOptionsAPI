package moapi.api;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
* An abstract representation of a set of options for a single mod
*
* @author	Clinton Alexander
* @author	Jonathan Brazell
* @version	1.0.0
* @since	0.1
*/
public class ModOptions {
	/**
	* Map of option selectors
	*/ 
	private LinkedHashMap<String, ModOption> options = new LinkedHashMap<String, ModOption>();

	/**
	* Map of sub option menus for this menu
	*/
	private LinkedHashMap<String, ModOptions> subOptions = new LinkedHashMap<String, ModOptions>();
	
	/**
	* ID of this menu, which is used
	* for the file structure and to avoid coupling
	* the display name to the underlying values
	*
	* @since	0.8
	*/
	private String id;

	/**
	* Name of this menu
	*/
	private String name;

	/**
	* Parent of this set of options (null implies it's a mod package)
	*/
	private ModOptions	parent = null;

	/**
	* Whether this set of options can have server specific options
	* @note If false, all children will act as false
	*/
	private boolean multiplayer = false;

	/**
	* Whether this set of options can have single player world options
	* @note If set false, all children will act as false
	*/
	private boolean singleplayer = true;
	
	/**
	* The pattern matcher to detect illegal filenames
	*
	* @since	0.8
	*/
	private static final Pattern illegalNamePattern = Pattern.compile("[/\\?%*:|\"<>]+");
	
	/**
	* Create a new set of options with no parent. A mod package
	*
	* @throws 	PatternSyntaxException	When an invalid folder name is specified
	* @param	name	Name of option list (menu name). Valid folder names only.
	*/
	public ModOptions(String name) {
		this(name, name);
	}
	
	/**
	* Create a sub options menu for a mod package
	*
	* @throws 	PatternSyntaxException	When an invalid folder name is specified
	* @param	name	Name of this option list
	* @param	p		Parent of this option list
	*/
	public ModOptions(String name, ModOptions p) {
		this(name, name);
		parent 		= p;
	}
	
	/**
	* Create a sub option menu with no parent
	*
	* @since	0.8
	* @throws 	PatternSyntaxException	When an invalid folder name is specified
	* @param	id		ID of this option list
	* @param	name	Name of this option set
	*/
	public ModOptions(String id, String name) {
		setID(id);
		this.name = name;
	}
	
	//=========================
	// Adding Options Methods
	//=========================
	
	/**
	* Add a manually created option to this menu
	*
	* @param	option		Option selector to add
	* @return	Returns the option just added for further operations
	*/
	public ModOption addOption(ModOption option) {
		options.put(option.getID(), option);
		return option;
	}
	
	/**
	* Add a text option with infinite max length
	*
	* @param	name	Name of text input
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name) {
		return addOption(new ModTextOption(name));
	}	
	
	/**
	* Add a text option with infinite max length
	* with a default value
	* 
	* @param	name	Name of text input
	* @param	value	Default value of input
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name, String value) {
		return addOption((new ModTextOption(name)).setGlobalValue(value));
	}
	
	/**
	* Add a text option with infinite max length
	* 
	* @param	name	Name of text input
	* @param	maxlen	Maximum length the user can input. 0 or less is infinite
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name, int maxlen) {
		return addOption(new ModTextOption(name, maxlen));
	}
	
	/**
	* Add a text option with infinite max length with a default
	* value
	* 
	* @param	name	Name of text input
	* @param	value	Default value for input
	* @param	maxlen	Maximum length the user can input. 0 or less is infinite
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name, String value, int maxlen) {
		return addOption((new ModTextOption(name, maxlen)).setGlobalValue(value));
	}
	
	/**
	* Add a multiple selector
	*
	* @param	name	Name of selector
	* @param	values	Set of values to display
	* @return	Returns the option just added for further operations
	*/
	public ModOption addMultiOption(String name, String[] values) {
		return addOption(new ModMultiOption(name, values));
	}
	
	/**
	* Adds a mapped option
	*
	* @throws 	IndexOutOfBoundsException
	* @param	name	Name of selector
	* @param	keys	Keys for selector
	* @param	values	Values for selector
	* @return	Returns the option just added for further operations
	*/
	public ModOption addMappedOption(String name, int[] keys, String[] values) throws IndexOutOfBoundsException {
		if(keys.length != values.length) {
			throw new IndexOutOfBoundsException("Arrays are not same length");
		} else {
			return addOption(new ModMappedOption(name, keys, values));
		}
	}
	
	/**
	* Add a toggle/boolean selector
	*
	* @param	name		Name of boolean selector
	* @return	Returns the option just added for further operations
	*/
	public ModOption addToggle(String name) {
		return addOption(new ModBooleanOption(name));
	}
	
	/**
	* Add a numeric slider ranging from 0 to 100
	*
	* @param	name		Name of slider
	* @return	Returns the option just added for further operations
	*/
	public ModOption addSlider(String name) {
		return addOption(new ModSliderOption(name));
	}
	
	/**
	* Add a numeric slider with a range
	*
	* @param	name	Name of slider
	* @param	low		Lowest value of slider
	* @param	high	Highest value of slider
	* @return	Returns the option just added for further operations
	*/
	public ModOption addSlider(String name, int low, int high) {
		return addOption(new ModSliderOption(name, low, high));
	}
	
	//=========================
	// SubOptions Methods
	//=========================
	
	/**
	* Add a sub menu of options
	*
	* @param	m		Set of sub-options
	* @return	The current object, for building
	*/
	public ModOptions addSubOptions(ModOptions m) {
		m.setParent(this);
		subOptions.put(m.getID(), m);
		return this;
	}
	
	/**
	* Check if this mod options has a given sub options menu
	*
	* @param	id of sub options
	* @return	true if sub options exist in this menu
	*/
	public boolean containsSubOptions(String id) {
		return subOptions.containsKey(id);
	}
	
	/**
	* Get all sets of sub-options for this set
	*
	* return	Array of all sub options for this set
	*/
	public ModOptions[] getSubOptions() {
    return ModOptionsAPI.getMods((Boolean)null, subOptions.entrySet());
	}
	
	/**
	* Get all sets of sub-options for this set for multiplayer
	*
	* return	Array of all sub options for this set
	*/
	public ModOptions[] getMultiplayerSubOptions() {
    return ModOptionsAPI.getMods(true, subOptions.entrySet());
	}
	
	/**
	* Get all sets of sub-options for this set for singleplayer
	*
	* return	Array of all sub options for this set
	*/
	public ModOptions[] getSingleplayerSubOptions() {
    return ModOptionsAPI.getMods(false, subOptions.entrySet());
	}
	
	/**
	* Get a named set of sub-options
	*
	* @param	id		ID of sub-options
	* @return	A sub-set of options
	*/
	public ModOptions getSubOption(String id) {
		return subOptions.get(id);
	}
	
	//=========================
	// Mass Option Methods
	//=========================
	
	/**
	* Sets all values global values to the parameter
	*
	* @param	global	New value
	*/
	public void globalReset(boolean global) {
		if(((ModOptionsAPI.isMultiplayerWorld()) && (multiplayer)) 
			|| ((!ModOptionsAPI.isMultiplayerWorld()) && (singleplayer))) {
			// Set all local options
			ModOption[] options = getOptions();
			for(ModOption option : options) {
				option.setGlobal(global);
			}
			// If we're in single player, reset all single player ones
			ModOptions[] subMenus = new ModOptions[0];
			if((!ModOptionsAPI.isMultiplayerWorld()) && (singleplayer)) {
				subMenus = getSingleplayerSubOptions();
				for(ModOptions sub : subMenus) {
					sub.globalReset(global);
				}
			}
			// If we're in multiplayer, reset all multiplayer ones
			if((ModOptionsAPI.isMultiplayerWorld()) && (multiplayer)) {
				subMenus = getMultiplayerSubOptions();
				for(ModOptions sub : subMenus) {
					sub.globalReset(global);
				}
			}
		}
	}
	
	//=========================
	// Direct Option Methods
	//=========================
	
	/**
	* Return a single named option
	*
	* @param	id		ID of option selector
	* @return	Option selector
	*/
	public ModOption getOption(String id) {
		return options.get(id);
	}
	
	/**
	* Returns a single named option's internal value
	*
	* @return	Value of option, as a string
	*/
	public String getOptionValue(String id) {
		ModOption option = options.get(id);
		if(option instanceof ModSliderOption) {
			return  Integer.toString(((ModSliderOption) option).getValue().intValue());
		} else {
			return option.getValue().toString();
		}
	}
	
	/**
	* Returns a single named toggle value
	*
	* @since	0.6.1
	* @param	id						ID of the option
	* @return	Value of a toggle option
	*/
	public boolean getToggleValue(String id) {
    return ((ModBooleanOption)options.get(id)).getValue();
	}
	
	/**
	* Returns a single named slider option's value
	*
	* @since	0.6.1
	* @param	id					ID of the option
	* @return	Value of a slider option
	*/
	public float getSliderValue(String id) {
    return ((ModSliderOption)options.get(id)).getValue().floatValue();
	}
	
	/**
	* Returns a single named mapped option's value
	*
	* @since	0.6.1
	* @param	id					ID of the option
	* @return	Value of a mapped multi option
	*/
	public int getMappedValue(String id) {
		return ((ModMappedOption)options.get(id)).getValue();
	}
	
	/**
	* Set a single named boolean options global value
	*
	* @param	id			ID of boolean toggle to change
	* @param	value		New value of toggle
	* @return	This object for building
	*/
	public ModOptions setOptionValue(String id, boolean value) {
		((ModBooleanOption)this.getOption(id)).setGlobalValue(value);
		return this;
	}
	
	/**
	* Set a single slider or mapped multi option's global value
	*
	* @param	id			ID of slider option to change
	* @param	value		New value of toggle
	* @return	This object for building
	*/
	public ModOptions setOptionValue(String id, int value) {
    getOption(id).setGlobalValue(value);
		return this;
	}
	
	/**
	* Set a single multi option's global value
	*
	* @param	id			ID of multi toggle to change
	* @param	value		New value of toggle
	* @return	This object for building
	*/
	public ModOptions setOptionValue(String id, String value) {
		ModOption m = this.getOption(id);
		if (m instanceof ModMultiOption) {
			((ModMultiOption)m).setGlobalValue(value);
		} else if(m instanceof ModTextOption) {
			((ModTextOption)m).setGlobalValue(value);
		}
		return this;
	}
	
	/**
	* Return all option selectors for this menu
	*
	* @return	Array of all option selectors for this menu
	*/
	public ModOption[] getOptions() {
		Set<Map.Entry<String, ModOption>> s = options.entrySet();
		ModOption m[] = new ModOption[s.size()];
		int i = m.length - 1;
		for(Map.Entry<String, ModOption> e : s) {
			m[i] = e.getValue();
			i--;
		}
		return m;
	}
	
	//=========================
	// Formatting Related
	//=========================
	
	/**
	* Set a named Option to wide
	*
	* @param 	id	 ID of option to set wide
	* @return	This object for building
	*/
	public ModOptions setWideOption(String id) {
		getOption(id).setWide(true);
		return this;
	}
	
	/**
	* Set a given option to wide
	*
	* @since	0.7
	* @param	option	Option object
	* @return	This object for building
	*/
	public ModOptions setWideOption(ModOption option) {
    option.setWide(true);
		return this;
	}
	
	/**
	* Set an option's string format, will remove any other formatters
	*
	* @param 	id				ID of option
	* @param 	formatter		Formatter
	* @return	This object for building
	*/
	public ModOptions setFormatter(String id, DisplayStringFormatter formatter) {
		getOption(id).setFormatter(formatter);
		return this;
	}
	
	/**
	* Add a formatter to the set of formatters for the given option
	*
	* @since	0.6.1
	* @param	id			ID of option
	* @param	formatter	Formatter to add
	* @return	This object for building
	*/
	public ModOptions addFormatter(String id, DisplayStringFormatter formatter) {
		getOption(id).addFormatter(formatter);
		return this;
	}
	
	/**
	* Set an option's string format, will remove any other formatters
	*
	* @since	0.8
	* @param 	option			Option to add formatter to
	* @param 	formatter		Formatter
	* @return	This object for building
	*/
	public ModOptions setFormatter(ModOption option, DisplayStringFormatter formatter) {
    option.setFormatter(formatter);
		return this;
	}
	
	/**
	* Add a formatter to the set of formatters for the given option
	*
	* @since	0.8
	* @param	option		Option to add formatter to
	* @param	formatter	Formatter to add
	* @return	This object for building
	*/
	public ModOptions addFormatter(ModOption option, DisplayStringFormatter formatter) {
		option.addFormatter(formatter);
		return this;
	}
	
	//=========================
	// Getters
	//=========================
	
	/**
	* Return the ID for this menu
	*
	* @return	ID of this menu
	*/
	public String getID() {
		return id;
	}
	
	/**
	* Return name for this menu
	*
	* @return	Name of this options menu
	*/
	public String getName() {
		return name;
	}
	
	/**
	* Get parent for this menu
	*
	* @return	Parent for this menu (or null if no parent)
	*/
	public ModOptions getParent() {
		return parent;
	}
	
	//=========================
	// Setters
	//=========================
	
	/**
	* Set the ID of this options
	*
	* @since	0.8
	* @throws	PatternSyntaxException 	When ID is invalid
	* @param	id						ID to use
	*/
	private void setID(String id) {
		Matcher m = illegalNamePattern.matcher(id);
		
		if(m.matches()) {
			throw new PatternSyntaxException("(ModOptions) Please do not use special characters for ID","",0);
		} else {
			this.id = id;
		}
	}
	
	/**
	* Set the parent for this menu
	*
	* @param	o		Parent menu
	* @return	This object for building
	*/
	public ModOptions setParent(ModOptions o) {
		parent 	= o;
		return this;
	}
	
	/**
	* Set multiplayer value
	*
	* @param	multi	True if visible in multiplayer
	* @return	This object for building
	*/
	public ModOptions setMultiplayerMode(boolean multi) {
		multiplayer = multi;
		return this;
	}
	
	/**
	* Set singleplayer value
	*
	* @param	single	True if visible in singleplayer
	* @return	This object for building
	*/
	public ModOptions setSingleplayerMode(boolean single) {
		singleplayer = single;
		return this;
	}
	
	//=========================
	// Verification
	//=========================
	
	/**
	* Checks if this mod is available in single player world menus
	*
	* @return	True if in single player world menus
	*/
	public boolean isSingleplayerMod() {
		return singleplayer;
	}
	
	/**
	* Checks if this mod is available in multiplayer server menus
	*
	* @return	True if in a multiplayer world menu
	*/
	public boolean isMultiplayerMod() {
		return multiplayer;
	}
	
	//=========================
	// Storage
	//=========================
	
	/**
	* Loads global values from disk into memory for this
	* and all sub-menus
	*
	* @return	This object for building
	*/
	public ModOptions loadValues() {
		loadValues("", false);
		return this;
	}
	
	/**
	* Loads values from disk into memory for this
	* and all sub-menus
	*
	* @param	worldName	Name of world/ server to load for
	* @param	multi		True if  multiplayer word
	* @return	This object for building
	*/
	public ModOptions loadValues(String worldName, boolean multi) {
		String line;
		// Also load all children
		for(ModOptions child : this.getSubOptions()) {
			child.loadValues(worldName, multi);
		}
		File file = getFile(worldName, multi);
		try {
			BufferedReader reader 	= new BufferedReader(new FileReader(file));
			HashMap map 			= new HashMap<String, String>();
			while((line = reader.readLine()) != null) {
				String[] 	parts 	= line.split(":", 2);
				String 		id 		= parts[0];
				String 		value 	= parts[1].replace(":", "");
				map.put(id, value);
			}
			boolean global	= (worldName.length() == 0);
			for (ModOption option : getOptions()) {
        if (map.containsKey(option.getID())) {
          String val = (String)map.get(option.getID());
					option.fromString(val, global);
          // Turn off global default for this option
					option.setGlobal(global);
        }
			}
		} catch (FileNotFoundException e) {
			// Ignore, this is expected
		} catch (IOException e) {
			System.out.println("(ModOptionsAPI): IOException occured: " + e.getMessage());
		}
		return this;
	}
	
	/**
	* Save options to disk for a particular world
	*
	* @param	name			worldname
	* @param	multiplayer		Save to a multiplayer file if true
	* @return	This object for building
	*/
	public ModOptions save(String name, boolean multiplayer) {
		boolean global = (name.length() == 0);
		// Delete old file, write new
		File file = getFile(name, multiplayer);
		file.delete();
		try {
			PrintWriter printwriter = new PrintWriter(new FileWriter(file));
			for(ModOption o : this.getOptions()) {
				Object obj = o.getValue(global);
				// Only record if it's a global value or a local non-global reference value
				if((obj != null) && (global || (!o.useGlobalValue()))) {
					// Remove all ":" in the name. 
					printwriter.println(o.getID().replace(":", "") + ":" + obj.toString());
				}
			}
			printwriter.close();
		} catch (IOException e) {
			System.err.println("(ModOptionsAPI): Could not save options to " + name);
		}
		return this;
	}
	
	/**
	* Saves options to disk
	*
	* @return	This object for building
	*/
	public ModOptions save() {
		save("", false);
		return this;
	}
	
	/**
	* Get file associated with this set of options
	*
	* @param	name			worldname
	* @param	multiplayer		Save to a multiplayer file if true
	*/
	private File getFile(String name, boolean multiplayer) {
		// Set folder
		String subDir = getID();
		ModOptions p = parent;
		while (p != null) {
			subDir = p.getID() + "/" + subDir;
			p = p.getParent();
		}
		subDir = ModOptionsAPI.getMinecraftFolder() + "config/" + subDir;
		// Ensure dir exists
		File dir = new File(subDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// Set filename
		String filename = "";
		// Add a prefix for world specifics
		if(name.length() > 0) {
			if(multiplayer) {
				filename = name + ".server.";
			} else {
				filename = name + ".world.";
			}
		}
		filename = filename + "settings.ini";
		return new File(subDir + "/" + filename);
	}
}
