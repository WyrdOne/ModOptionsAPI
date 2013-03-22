package moapi.client;

import java.util.*;
import java.lang.reflect.*;
import net.minecraft.src.*;
import net.minecraft.client.*;
import moapi.*;

/**
* Class to add and handle options in the Minecraft Client GUI 
*
* @author Jonathan Brazell (WyrdOne)
* @version 1.0.1
*/
public class ClientGui extends ModOptionCallback {
  private static StringTranslate translate = StringTranslate.getInstance();
  private static ModOptions guiAPI;
  private static GuiButton guiAPIButton;

  public static void modifyGuiOptions(GuiScreen screen, List controlList) {
	if (ModLoader.isModLoaded("GuiAPI") && (guiAPI==null)) {
		guiAPI = ModOptionsAPI.addMod("guiapi", "Mod Options (Gui API)").setClientMode();
		guiAPI.setCallback(new ClientGui());
	}
	int xPos = screen.width / 2 + 5;
	int yPos = screen.height / 6 + 144 - 6;
    for (int idx=0; idx<controlList.size(); idx++) {
        GuiButton btn = (GuiButton)controlList.get(idx);
		if (btn.id==EnumOptions.DIFFICULTY.returnEnumOrdinal()) {
			xPos = btn.xPosition;
		}
		if (btn.id==EnumOptions.TOUCHSCREEN.returnEnumOrdinal()) {
			yPos = btn.yPosition;
		}
        if (btn.id==300) { // GuiAPI
        	guiAPIButton = btn;
        	btn.enabled = false;
        }
	}
    controlList.add(new OptionButton(301, xPos, yPos, 150, 20, "Mod Options (Global)"));
  }
  
  public boolean onClick(ModOption option) {
	  if (option==guiAPI) {
     	guiAPIButton.enabled = true;
		  guiAPIButton.mousePressed(Minecraft.getMinecraft(), guiAPIButton.xPosition+1, guiAPIButton.yPosition+1);
     	guiAPIButton.enabled = false;
	    return false;
	  }
	  return true;
  }

  public static void modifyGuiInGameMenu(GuiScreen screen, List controlList) {
    controlList.add(new OptionButton(302, screen.width / 2 - 100, screen.height / 4 + 72 - 16, 200, 20, "Mod Options (World)"));		
  }
  
  public static void handleCommands(Minecraft mc, GuiScreen screen, GuiButton button) {
    switch (button.id) {
      case 301: // GuiOptions -> ModOptions
  			mc.gameSettings.saveOptions();
  			mc.displayGuiScreen(new ModMenu(screen));
        break;
	    case 302: // GuiInGameMenu -> ModOptions
        if (mc.getServerData() != null) {
  		    // Multiplayer worlds have no name
			    mc.displayGuiScreen(new ModMenu((GuiIngameMenu)screen, mc.getServerData().serverName));
		    } else {
			    // Get the world name
			    String name = mc.getIntegratedServer().getWorldName();
			    mc.displayGuiScreen(new ModMenu((GuiIngameMenu)screen, name));
		    }
		    break;
    }
  }
}
