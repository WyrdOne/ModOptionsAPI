package moapi.clientgui;

import java.util.*;
import java.lang.reflect.*;
import net.minecraft.src.*;
import net.minecraft.client.*;

/**
* Class to add and handle options in the Minecraft Client GUI 
*
* @author Jonathan Brazell (WyrdOne)
* @version 1.0.1
*/
public class ClientGui {
  private static StringTranslate translate = StringTranslate.getInstance();

  public static void modifyGuiOptions(GuiScreen screen, List controlList) {
    int yPos = screen.height / 6 + 144 - 6;
    
    for (int idx=0; idx< controlList.size(); idx++) {
      GuiButton btn = (GuiButton)controlList.get(idx);
      if (btn.id==EnumOptions.TOUCHSCREEN.returnEnumOrdinal()) {
        yPos = btn.yPosition;
      }
    }
 		controlList.add(new GuiButton(301, screen.width / 2 + 5, yPos, 150, 20, "Mod Options (Global)"));
  }

  public static void modifyGuiInGameMenu(GuiScreen screen, List controlList) {
    controlList.add(new GuiButton(302, screen.width / 2 - 100, screen.height / 4 + 72 - 16, "Mod Options (World)"));		
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
			    mc.displayGuiScreen(new ModMenu((GuiIngameMenu)screen, mc.getServerData().serverName, true));
		    } else {
			    // Get the world name
			    String name = mc.getIntegratedServer().getWorldName();
			    mc.displayGuiScreen(new ModMenu((GuiIngameMenu)screen, name, false));
		    }
		    break;
    }
  }
}
