package net.minecraft.src; // Has to be here for ModLoader

import java.lang.reflect.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.src.*;
import moapi.*;
import moapi.client.*;

/**
* Base mod file for Minecraft client, uses ModLoader/Forge to load.
*
* @author Jonathan Brazell
* @version 1.4.7
*/
public class mod_MOAPI extends BaseMod {
  // Copyright/license info
  private static final String Name = "Mod Options API (MOAPI)";
  private static final String Version = ModOptionsAPI.getVersion() + " (For use with Minecraft 1.5)";
  // Other variables
  private boolean cacheGuiOptions = false;
  private boolean cacheGuiIngameMenu = false;

  public void load() {
	if (ModOptionsAPI.isServer())
      return;
    ModLoader.setInGameHook(this, true, false);
    ModLoader.setInGUIHook(this, true, false);
  }

  public boolean onTickInGame(float f, Minecraft mc) {
    if (mc.isIntegratedServerRunning()) {
      ModOptionsAPI.loadOptions(mc.getIntegratedServer().getWorldName(), ModOptionsAPI.getAllMods());
    } else {
        String serverName = mc.getServerData().serverName;
        if (serverName==null || serverName.length()==0)
          serverName = mc.getServerData().serverIP;
	    ModOptionsAPI.loadOptions(serverName, ModOptionsAPI.getClientMods());
    }
    return false; // Only run once
  }

  private List getControlList(GuiScreen screen) {
    List controlList = null;
    
    try {
			Field[] fields = GuiScreen.class.getDeclaredFields();
			for (int i=0; i<fields.length; i++) {
				if (fields[i].getType()==List.class) {
					fields[i].setAccessible(true);
			    controlList = (List)fields[i].get(screen);
					break;
				}
			}
		} catch (Exception e) {
		  // No need to do anything, null will be returned if the field was not found
		}
    return controlList;  
  }
  
  public boolean onTickInGUI(float ticks, Minecraft mc, GuiScreen screen) {
    if (screen instanceof GuiMainMenu) {
	    ModOptionsAPI.loadOptions("", ModOptionsAPI.getAllMods());
      ModLoader.setInGameHook(this, true, false); // Reset for next game
      cacheGuiOptions = false;
      cacheGuiIngameMenu = false;
    } else if (screen instanceof GuiOptions) {
      cacheGuiIngameMenu = false;
      if (!cacheGuiOptions) {
        cacheGuiOptions = true;
        ClientGui.modifyGuiOptions(screen, getControlList(screen));
      }
    } else if (screen instanceof GuiIngameMenu) {
      cacheGuiOptions = false;
      if (!cacheGuiIngameMenu) {
        cacheGuiIngameMenu = true;
        ClientGui.modifyGuiInGameMenu(screen, getControlList(screen));
      }
    } else {
      cacheGuiOptions = false;
      cacheGuiIngameMenu = false;
    } 
    return true;
  }

//  public String getPriorities() {
//	return "after:GuiAPI";
//  }
  
  public String getName() {
    return Name;
  }
	
  public String getVersion() {
    return Version;
  }
}
