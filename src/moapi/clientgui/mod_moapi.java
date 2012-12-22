package net.minecraft.src;

import net.minecraft.client.Minecraft;
import moapi.clientgui.*;
import moapi.api.*;

public class mod_moapi extends BaseMod {
  // Copyright/license info
  private static final String Name = "Mod Options API (MOAPI)";
  private static final String Version = "1.0.2 (For use with Minecraft 1.4.6)";
  // Other variables
  private static Minecraft mc = Minecraft.getMinecraft();
  private boolean cacheGuiOptions = false;
  private boolean cacheGuiIngameMenu = false;
  
  public void load() {
    ModLoader.setInGameHook(this, true, false);
    ModLoader.setInGUIHook(this, true, false);
  }

  public boolean onTickInGame(float f, Minecraft mc) {
    if (mc.isIntegratedServerRunning()) {
      ModOptionsAPI.loadOptions(mc.getIntegratedServer().getWorldName(), false, ModOptionsAPI.getSingleplayerMods());
    } else {
        String serverName = mc.getServerData().serverName;
        if (serverName==null || serverName.length()==0)
          serverName = mc.getServerData().serverIP;
	    ModOptionsAPI.loadOptions(serverName, true, ModOptionsAPI.getMultiplayerMods());
    }
    return false; // Only run once
  }
  
  public boolean onTickInGUI(float ticks, Minecraft mc, GuiScreen screen) {
    if (screen instanceof GuiMainMenu) {
      ModLoader.setInGameHook(this, true, false); // Reset for next game
      cacheGuiOptions = false;
      cacheGuiIngameMenu = false;
    } else if (screen instanceof GuiOptions) {
      cacheGuiIngameMenu = false;
      if (!cacheGuiOptions) {
        cacheGuiOptions = true;
        ClientGui.modifyGuiOptions(screen, screen.controlList);
      }
    } else if (screen instanceof GuiIngameMenu) {
      cacheGuiOptions = false;
      if (!cacheGuiIngameMenu) {
        cacheGuiIngameMenu = true;
        ClientGui.modifyGuiInGameMenu(screen, screen.controlList);
      }
    } else {
      cacheGuiOptions = false;
      cacheGuiIngameMenu = false;
    } 
    return true;
  }

  public String getName() {
    return Name;
  }
	
  public String getVersion() {
    return Version;
  }
}
