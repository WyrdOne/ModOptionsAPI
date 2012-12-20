package moapi.api;

import java.io.*;
import java.util.*;

/**
* Controller class for the API. 
*
* @author Clinton Alexander
* @author Jonathan Brazell
* @version 1.0.0
*/
public class ModOptionsAPI {
  /**
  * Stores a list of each mod added with their options
  */    
	private static TreeMap<String, ModOptions> modOptions = new TreeMap<String, ModOptions>();
	
	/**
	* True if the player is on a multiplayer server
	*/
	private static boolean multiplayerWorld = false;
	
	/**
	* True if this is the server
	*/
	private static boolean isServer = false;
	
	/**
	* Stores the World Name, Server Name, or IP address
	*/
	private static String gameName = "";
	
	//=========================
	// Checking and Validation
	//=========================
	
	/**
	* Check if the world is a multiplayer world
	*
	* @return	True if multiplayer world
	*/
	public static boolean isMultiplayerWorld() {
		return multiplayerWorld;
	}

	/**
	* Check if this is the server
	*
	* @return	True if this is the server
	*/
	public static boolean isServer() {
		return isServer;
	}
	
	//=========================
	// Setters
	//=========================
	
	/**
	* Set that we are in a multiplayer world
	*
	* @param	serverName  Name of server being used
	*/
	public static void joinedMultiplayerWorld(String serverName) {
		loadOptions(serverName, true, ModOptionsAPI.getMultiplayerMods());
	}
	
	/**
	* Set that we are in a singleplayer world
	*
	* @param	worldName	World name
	*/
	public static void selectedWorld(String worldName) {
		loadOptions(worldName, false, ModOptionsAPI.getSingleplayerMods());
	}
	
	/**
	* Set that we are running on the server
	*
	* @param	worldName	World name
	*/
	public static void setServer(String worldName) {
		loadOptions(worldName, true, ModOptionsAPI.getMultiplayerMods());
		isServer = true;
	}    
	
	//=========================
	// Getters
	//=========================

	/**
	* Gets the current MOAPI version
	*
	* @since  1.0.0   
	* @return version of MOPAPI
	*/
	public static String getVersion() {
		return "1.0.0";
	}          

	/*
	* Gets the name of the current game or server
	* 
	* @since 1.0.0
	* @return name of the current game, server name, or IP address
	*/
	public static String getGameName() {
		return gameName;
	}
	
	/**
	* Loads the options for the mods
	* 	
	* @since  1.0.0
	* @param  name  Server/world name to use for loading
	* @param  type  true if multiplayer    
	* @param  mods  array of the mods to load options for     
	*/
	public static void loadOptions(String name, boolean type, ModOptions[] mods) {
		multiplayerWorld = type;
		gameName = name;
		for(ModOptions options : mods) {
			options.loadValues(name, type);
		}
	}

	/**
	* Gets all mods that have been added
	*
	* @since  1.0.0	
	* @return	Array of all sets of options for all mods
	*/
	public static ModOptions[] getMods(Boolean multiplayer, Set<Map.Entry<String, ModOptions>> options) {
		ModOptions m[] = new ModOptions[options.size()];
		int i = 0;
		for(Map.Entry<String, ModOptions> e : options) {
		  if (multiplayer==null) {
			  m[i] = e.getValue();
			  i++;
		  } else if (multiplayer.booleanValue()) {
  			if(e.getValue().isMultiplayerMod()) {
  				m[i] = e.getValue();
  				i++;
  			}
		  } else {
  			if(e.getValue().isSingleplayerMod()) {
  				m[i] = e.getValue();
  				i++;
  			}
		  }  
		}
		return m;
	}
	
	/**
	* Gets all mods that have been added
	*
	* @return	Array of all sets of options for all mods
	*/
	public static ModOptions[] getAllMods() {
	  return getMods(null, modOptions.entrySet());
	}
	
	/**
	* Returns all mods that identify as a multiplayer mod
	*
	* @return	Array of all sets of options for multiplayer world mods
	*/
	public static ModOptions[] getMultiplayerMods() {
	  return getMods(true, modOptions.entrySet());
	}
	
	/**
	* Returns all mods that identify as a singleplayer mod
	*
	* @return	Array of all sets of options for singleplayer world mods
	*/
	public static ModOptions[] getSingleplayerMods() {
	  return getMods(false, modOptions.entrySet());
	}
	
	/**
	* Returns a set of options for a mod by the name of the mod.
	* 
	* @since	0.1
	* @param	name		Name of the mod
	* @return	Set of options for the mod named in the parameters
	*/
	public static ModOptions getModOptions(String name) {
		return modOptions.get(name);
	}
	
	//=========================
	// Adder Methods
	//=========================
	
	/**
	* Adds a mods set of options to a menu in the ModOptions
	* menu. (Add your mod's options here)
	*
	* @since	0.1
	* @param	o		A set of options for a mod
	*/
	public static void addMod(ModOptions o) {
		modOptions.put(o.getID(), o);
	}
	
	//=========================
	// Utility Methods
	//=========================

	/**
	* Determines the folder where Minecraft is located.
	*
	* @since	1.0.0
	* @return	Path to the Minecraft folder
	*/
  public static String getMinecraftFolder() {
    if (isServer()) {
      try {
        return (new File(".")).getCanonicalPath();
      } catch (Exception error) {
        return null;
      }
    }
    String home = System.getProperty("user.home", ".");
    String os = System.getProperty("os.name").toLowerCase();
    String path = "";
    if (os.contains("win")) {
      String appData = System.getenv("APPDATA");
      if (appData != null) {
        path = appData + "/minecraft/";
      } else {
        path = home + "/minecraft/";
      }
    } else if (os.contains("solaris") || os.contains("sunos") || os.contains("linux") || os.contains("unix")) {
      path = home + "/.minecraft/";
    } else if (os.contains("mac")) {
      path = home + "/Library/Application Support/minecraft/";
    } else {
      path = home + "/minecraft/";
    }
    return path;
  }
}
