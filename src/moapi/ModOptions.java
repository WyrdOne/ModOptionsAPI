package moapi;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import net.minecraft.server.*;

/**
* An abstract representation of a set of options for a single mod
*
* @author   Clinton Alexander
* @author   Jonathan Brazell
* @version	1.5
* @since    0.1
*/
public class ModOptions extends ModOption<LinkedHashMap<String, ModOption>> {
	/** Parent of this set of options (null implies it's a mod package)	*/
	private ModOptions parent = null;

	/**
	* Whether this set of options is client specific, rendering, etc.
	* @note If set false, all children will act as false
	*/
	private boolean client = false;

	/**
	* The pattern matcher to detect illegal filenames
	*
	* @since	0.8
	*/
	private static final Pattern illegalNamePattern = Pattern.compile("[/\\?%*:|\"<>]+");
	
	/**
	* Used to allow external connections for GuiAPI and CaptionAPI
	*
	* @since	1.5
	*/
	protected Object externalCommand = null; 
	

	//=========================
	// Constructors
	//=========================
	
	/**
	* Create an option menu/submenu
	*
	* @since	0.8
	* @param	id		ID of this option list
	* @param	name	Name of this option set
	*/
	public ModOptions(String id, String name) {
		super(id, name);
		wide = true;
		Matcher matcher = illegalNamePattern.matcher(id);
		if (matcher.matches()) {
			throw new PatternSyntaxException("(ModOptions) Please do not use special characters for ID","",0);
		}
		value = new LinkedHashMap<String, ModOption>();
	}

	/**
	* Create an option menu/submenu
	*
	* @since	0.8
	* @param	name	Name of this option set
	*/
	public ModOptions(String name) {
		this(name, name);
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
		value.put(option.getID(), option);
		return option;
	}
	
	/**
	* Add a toggle/boolean selector
	*
	* @param	id		  ID of boolean option
	* @param	name		Display text of boolean option
	* @param	value		Default value for option
	* @return	Returns the option just added for further operations
	*/
  public ModOption addBooleanOption(String id, String name, boolean value) {
    return addOption(new ModOptionBoolean(id, name, value));
  }
  
	/**
	* Add a toggle/boolean selector
	*
	* @param	name		ID/Display text of boolean option
	* @param	value		Default value for option
	* @return	Returns the option just added for further operations
	*/
  public ModOption addBooleanOption(String name, boolean value) {
    return addBooleanOption(name, name, value);
  }

	/**
	* Add a toggle/boolean selector
	*
	* @param	name		ID/Display text of boolean option
	* @return	Returns the option just added for further operations
	*/
  public ModOption addBooleanOption(String name) {
    return addBooleanOption(name, name, false);
  }

	/**
	* Add a multiple selector
	*
	* @param	id		  ID of option
	* @param	name		Display text of option
	* @param	values  Set of values to display
	* @return	Returns the option just added for further operations
	* @note The first value is the default	
	*/
	public ModOption addMultiOption(String id, String name, String[] values) {
		return addOption(new ModOptionMulti(id, name, values));
	}

	/**
	* Add a multiple selector
	*
	* @param	name	   Name of selector
	* @param	values	 Set of values to display
	* @return	Returns  the option just added for further operations
	* @note The first value is the default	
	*/
	public ModOption addMultiOption(String name, String[] values) {
		return addMultiOption(name, name, values);
	}
	
	/**
	* Add a text option with max length and a default value
	* 
	* @param	id		  ID of option
	* @param	name    Name of text input
	* @param	maxlen	Maximum length the user can input. 0 or less is infinite
	* @param	value   Default value for input
	* @return	Returns the option just added for further operations
	*/
	public ModOption addTextOption(String id, String name, int maxlen, String value) {
		return addOption((new ModOptionText(id, name, maxlen)).setValue(value));
	}
	
	/**
	* Add a text option with max length and a default value
	* 
	* @param	name    Name of text input
	* @param	maxlen	Maximum length the user can input. 0 or less is infinite
	* @param	value   Default value for input
	* @return	Returns the option just added for further operations
	*/
	public ModOption addTextOption(String name, int maxlen, String value) {
		return addTextOption(name, name, maxlen, value);
	}

	/**
	* Add a text option with max length
	* 
	* @param	name    Name of text input
	* @param	maxlen	Maximum length the user can input. 0 or less is infinite
	* @return	Returns the option just added for further operations
	*/
	public ModOption addTextOption(String name, int maxlen) {
		return addTextOption(name, name, maxlen, "");
	}

	/**
	* Add a text option
	* 
	* @param	name    Name of text input
	* @return	Returns the option just added for further operations
	*/
	public ModOption addTextOption(String name) {
		return addTextOption(name, name, 0, "");
	}
	
	/**
	* Adds a mapped option
	*
	* @param	id		  ID of option
	* @param	name    Name of selector
	* @param	values	Values for selector
	* @param	keys    Keys for selector
	* @return	Returns the option just added for further operations
	*/
	public ModOption addMappedOption(String id, String name, String[] values, int[] keys) {
  	return addOption(new ModOptionMapped(id, name, values, keys));
	}

	/**
	* Adds a mapped option
	*
	* @param	name    Name of selector
	* @param	values	Values for selector
	* @param	keys    Keys for selector
	* @return	Returns the option just added for further operations
	*/
	public ModOption addMappedOption(String name, String[] values, int[] keys) {
  	return addMappedOption(name, name, values, keys);
	}

	/**
	* Add a numeric slider with a range
	*
	* @param	id		ID of option
	* @param	name	Name of slider
	* @param	low		Lowest value of slider
	* @param	high	Highest value of slider
	* @return	Returns the option just added for further operations
	*/
	public ModOption addSliderOption(String id, String name, int low, int high) {
		return addOption(new ModOptionSlider(id, name, low, high));
	}

	/**
	* Add a numeric slider with a range
	*
	* @param	name	Name of slider
	* @param	low		Lowest value of slider
	* @param	high	Highest value of slider
	* @return	Returns the option just added for further operations
	*/
	public ModOption addSliderOption(String name, int low, int high) {
		return addOption(new ModOptionSlider(name, name, low, high));
	}

	/**
	* Add a sub menu of options
	*
	* @param	id		ID of sub option
	* @param	name	Name of sub option
	* @return	Returns the option just added for further operations
	*/
	public ModOptions addSubOption(String id, String name) {
		return (ModOptions)addOption((new ModOptions(id, name)).setParent(this));
	}
  
	/**
	* Add a sub menu of options
	*
	* @param	name	Name of sub option
	* @return	Returns the option just added for further operations
	*/
	public ModOptions addSubOption(String name) {
		return addSubOption(name, name);
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
		return value.get(id);
	}
	
	/**
	* Returns a single named option's internal value
	*
	* @param	id		ID of option selector
	* @return	Value of option, as a string
	*/
	public String getOptionValue(String id) {
		return ((ModOption)value.get(id)).getValue().toString();
	}
	
	/**
	* Returns value of a single named boolean value
	*
	* @param	id		ID of option selector
	* @return	Value of a boolean option
	*/
	public boolean getBooleanValue(String id) {
    return ((ModOptionBoolean)value.get(id)).getValue();
	}
	
	/**
	* Returns a single named slider option's value
	*
	* @param	id					ID of the option
	* @return	Value of a slider option
	*/
	public int getSliderValue(String id) {
    return ((ModOptionSlider)value.get(id)).getValue();
	}
	
	/**
	* Returns a single named mapped option's value
	*
	* @param	id					ID of the option
	* @return	Value of a mapped multi option
	*/
	public int getMappedValue(String id) {
		return ((ModOptionMapped)value.get(id)).getValue();
	}
	
	/**
	* Return all option selectors for this menu
	*
	* @return	Array of all option selectors for this menu
	*/
	public ModOption[] getOptions() {
    return value.values().toArray(new ModOption[value.size()]);
	}

	/**
	* Return all client option selectors for this menu
	*
	* @return	Array of all client option selectors for this menu
	*/
	public ModOption[] getClientOptions() {
    ArrayList<ModOption> options = new ArrayList<ModOption>();
    
    for (ModOption option : getOptions()) {
      if (option instanceof ModOptions) {
        if (((ModOptions)option).isClientMode())
          options.add(option);
      } else {
        options.add(option);
      }
    }
    return options.toArray(new ModOption[options.size()]);
	}

	/**
	* Return all server option selectors for this menu
	*
	* @return	Array of all server option selectors for this menu
	*/
	public ModOption[] getServerOptions() {
    ArrayList<ModOption> options = new ArrayList<ModOption>();
    
    for (ModOption option : getOptions()) {
      if (option instanceof ModOptions) {
        if (!((ModOptions)option).isClientMode())
          options.add(option);
      } else {
        options.add(option);
      }
    }
    return options.toArray(new ModOption[options.size()]);
	}

	/**
	* Get all sets of sub-options for this set
	*
	* return	Array of all sub options for this set
	*/
	public ModOptions[] getSubOptions() {
    ArrayList<ModOptions> subOptions = new ArrayList<ModOptions>();
    
    for (ModOption option : getOptions()) {
      if (option instanceof ModOptions) {
        subOptions.add((ModOptions)option);
      }
    }
    return subOptions.toArray(new ModOptions[subOptions.size()]);
	}
	
	/**
	* Get parent for this sub menu
	*
	* @return	Parent for this menu (or null if no parent)
	*/
	public ModOptions getParent() {
		return parent;
	}

	/**
	* Get the display string for the option.
	*
	* @param	scope	True for global value
	* @return	display string
	*/
	public String getDisplayString(boolean scope) {
    return getName();
	}
	
	//=========================
	// Setters
	//=========================
	
	/**
	* Set the parent for this menu
	*
	* @param	parent  Parent menu
	* @return	This object for building
	*/
	public ModOptions setParent(ModOptions parent) {
		this.parent = parent;
		return this;
	}

	public ModOption fromString(String strValue, boolean scope) {
    // Really nothing to set.
    return this;
  }

	//===========================
	// Client/Server Verification
	//===========================

	/**
	* Set these options as client specific
	*
	* @return	This object for building
	*/
	public ModOptions setClientMode() {
		client = true;
		return this;
	}

	/**
	* Set these options as able to be used on a server
	*
	* @return	This object for building
	*/
	public ModOptions setServerMode() {
		client = false;
		return this;
	}
	
	/**
	* Checks if this mod is client specific
	*
	* @return	True if this mod is client specific
	*/
	public boolean isClientMode() {
		return client;
	}

	//===========================
	// Save/Load options
	//===========================

	/**
	* Get file associated with this set of options
	*
	* @param name  worldname, blank string for global options
	*/
	private File getFile(String worldName) {
    // Set folder
		String folderName = "";
    if (ModOptionsAPI.isServer()) {
      folderName = ModOptionsAPI.getMinecraftFolder() + worldName + "/";
    } else if (worldName.length()==0) { // Global
      folderName = ModOptionsAPI.getMinecraftFolder() + "config/";
    } else { // Local
      folderName = ModOptionsAPI.getMinecraftFolder() + "saves/" + MinecraftServer.getServer().getFolderName() + "/";
    }
		// Ensure folder exists
		File dir = new File(folderName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
    // Set filename (top level mod name)
    String modName = "";
		ModOptions p = this;
		while (p.getParent() != null) {
			p = p.getParent();
		}
    modName = p.getID();
		return new File(folderName + modName + ".moapi");
	}

	/**
	* Loads values from disk into memory for this
	* and all sub-menus
	*
	* @param	worldName	Name of world/ server to load for
	* @return	This object for building
	*/
  public ModOptions loadValues(String worldName) {
		String line;
		// Also load all children
		for (ModOptions child : this.getSubOptions()) {
			child.loadValues(worldName);
		}
		File file = getFile(worldName);
		String section="[" + getName() + "]";
		if (parent==null) {
		  section = "[toplevel]";
		}
		try {
			BufferedReader reader 	= new BufferedReader(new FileReader(file));
			HashMap map 			= new HashMap<String, String>();
			while ((line = reader.readLine()) != null) {
			  if (section.compareTo(line)==0)
			    break;
			}
			while((line = reader.readLine()) != null) {
        if (line.charAt(0)=='[')
          break;
				String[] 	parts 	= line.split(":", 2);
				String 		id 		= parts[0];
				String 		value 	= parts[1].replace(":", "");
				map.put(id, value);
			}
			boolean global = (worldName.length()==0);
			for (ModOption option : getOptions()) {
        if (map.containsKey(option.getID())) {
          String val = (String)map.get(option.getID());
          try {
            option.fromString(val, global);
          } catch (Exception ignored) {}
          // Turn off global default for this option if set local
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

  public ModOptions loadValues() {
	  if (ModOptionsAPI.isServer())
		  return loadValues(ModOptionsAPI.getWorldName());
	  return loadValues("");
  }

	/**
	* Save options to disk for a particular world or global
	*
	* @param	name			worldname, blank for global
	* @return	This object for building
	*/
  public ModOptions saveValues(String worldName) {
	  // Only save from top level
	  if (getParent()!=null) {
		  getParent().saveValues(worldName);
		  return this;
	  }	
	  boolean global = (worldName.length()==0);
	  // Delete old file, write new
	  File file = getFile(worldName);
	  file.delete();
	  try {
		  PrintWriter printwriter = new PrintWriter(new FileWriter(file));
		  printwriter.println("[toplevel]");
		  this.saveValues(printwriter, global);
		  printwriter.close();
	  } catch (IOException e) {
		  System.err.println("(ModOptionsAPI): Could not save options to " + name);
	  }
	  return this;
  }

  public ModOptions saveValues() {
	  if (ModOptionsAPI.isServer())
		  return saveValues(ModOptionsAPI.getWorldName());
	  return saveValues("");
  }

  protected void saveValues(PrintWriter printwriter, boolean global) {
	  for (ModOption option : this.getOptions()) {
	    if (!(option instanceof ModOptions)) {
	      if (global || !option.useGlobalValue())
    		  printwriter.println(option.getID().replace(":", "") + ":" + option.getValue(global).toString());
	    }
	  }
	  for (ModOptions child : this.getSubOptions()) {
			printwriter.println("[" + child.getName() + "]");
		  child.saveValues(printwriter, global);
	  }
  }
  
	/**
	* Sets all values global values to the parameter
	*
	* @param	global	New value
	*/
	public void globalReset(boolean global) {
		for(ModOption option : getOptions()) {
		  if (option instanceof ModOptions)
	      ((ModOptions)option).globalReset(global);
      else		  
			  option.setGlobal(global);
		}
	}
}
