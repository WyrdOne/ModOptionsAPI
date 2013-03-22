package moapi;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import net.minecraft.server.*;
import net.minecraft.src.*;

/**
* Controller class for the API. 
*
* @author Clinton Alexander
* @author Jonathan Brazell
* @version 1.5
*/
public class ModOptionsAPI {
	/** Instance to force class to initialize */
	private static ModOptionsAPI instance = new ModOptionsAPI();
	/** True if running on standalone server */
	private static boolean isServer;
	/** Stores the World Name, Server Name, or IP address */
	private static String worldName;
	/** Stores a list of each mod added with their options */    
	private static TreeMap<String, ModOptions> modOptions = new TreeMap<String, ModOptions>();
  
	private ModOptionsAPI() {
		try {
			Class.forName("net.minecraft.client.Minecraft", false, this.getClass().getClassLoader());
			isServer = false;
			worldName = "";
		} catch (Exception e) {
			isServer = true;
			worldName = MinecraftServer.getServer().getFolderName();
			if (worldName==null) { // Server settings not loaded yet
			  PropertyManager settings = new PropertyManager(new File("server.properties"), MinecraftServer.getServer().getLogAgent());
        worldName = settings.getProperty("level-name", "world");
      }
		}
	}
  
	/**
	* Gets the current MOAPI version
	*
	* @return version of MOPAPI
	*/
	public static String getVersion() {
		return "1.5";
	}          

	/**
	* Check if this is the server
	*
	* @return	True if this is the server
	*/
	public static boolean isServer() {
		return isServer;
	}

	/*
	* Gets the name of the current game or server
	* 
	* @return name of the current world name, server name, or IP address
	*/
	public static String getWorldName() {
		return worldName;
	}

	/**
	* Adds a mods set of options to a menu in the ModOptions menu. (Add your mod's options here)
	*
	* @param	ooptions  A set of options for a mod
	*/
	public static void addMod(ModOptions options) {
		modOptions.put(options.getID(), options);
	}

	/**
	* Adds a mods set of options to a menu in the ModOptions menu. (Add your mod's options here)
	*
	* @param	id    The id of the mod
	* @param	name  The display text for the mod
	*/
	public static ModOptions addMod(String id, String name) {
		ModOptions options = new ModOptions(id, name);
		modOptions.put(id, options);
		return options;
	}
  
	/**
	* Adds a mods set of options to a menu in the ModOptions menu. (Add your mod's options here)
	*
	* @param	name  The display text for the mod
	*/
	public static ModOptions addMod(String name) {
		return addMod(name, name);
	}
  
	/**
	* Determines the folder where Minecraft is located.
	*
	* @return	Path to the Minecraft folder
	*/
	public static String getMinecraftFolder() {
		if (isServer()) {
			try {
				return (new File(".")).getCanonicalPath() + "/";
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
				path = appData + "/.minecraft/";
			} else {
				path = home + "/.minecraft/";
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

	/**
	* Loads the option values from disk
	*
	* @param	id    The id of the mod
	*/
	public static void loadOptions(String id) {
		getModOptions(id).loadValues("");
	}

	/**
	* Loads the options for the selected mods
	* 	
	* @param  name  Server/world name to use for loading
	* @param  mods  array of the mods to load options for     
	*/
	public static void loadOptions(String pWorldName, ModOptions[] mods) {
		worldName = pWorldName;
		for(ModOptions options : mods) {
			options.loadValues(worldName);
		}
	}

	/**
	* Saves the option values to disk
	*
	* @param	id    The id of the mod
	*/
	public static void save(String id) {
		getModOptions(id).saveValues("");
	}

	/**
	* Gets all mods that have been added
	*
	* @return	Array of all sets of options for all mods
	*/
	public static ModOptions[] getMods(Boolean server) {
		Set<Map.Entry<String, ModOptions>> options = modOptions.entrySet();
		List<ModOptions> mods = new ArrayList<ModOptions>();
    
		for (Map.Entry<String, ModOptions> entry : options) {
			if (server==null) { // All Mods
				mods.add(entry.getValue());
			} else if (server.booleanValue()) { // Server Mods
				if (!entry.getValue().isClientMode())
					mods.add(entry.getValue());
			} else { // Client Mods
				if (entry.getValue().isClientMode())
					mods.add(entry.getValue());
			}
		}
		ModOptions[] modOptions = mods.toArray(new ModOptions[mods.size()]);
		return modOptions;
	}
	
	/**
	* Gets all mods that have been added
	*
	* @return	Array of all sets of options for all mods
	*/
	public static ModOptions[] getAllMods() {
	  return getMods(null);
	}
	
	/**
	* Returns all mods that identify as a server mod
	*
	* @return	Array of all sets of options for server world mods
	*/
	public static ModOptions[] getServerMods() {
	  return getMods(true);
	}
	
	/**
	* Returns all mods that identify as a client mod
	*
	* @return	Array of all sets of options for client world mods
	*/
	public static ModOptions[] getClientMods() {
	  return getMods(false);
	}
	
	/**
	* Returns a set of options for a mod by the name of the mod.
	* 
	* @param	id		ID of the mod
	* @return	Set of options for the mod named in the parameters
	*/
	public static ModOptions getModOptions(String id) {
		return modOptions.get(id);
	}
}
