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
    int leftPos = screen.width / 2 - 152;
    int rightPos = screen.width / 2 + 2;
    int yPos = screen.height / 6 + 144 - 6;
    int top = yPos;
    boolean taken[][] = new boolean[5][2];
    
    for (int idx=0; idx< controlList.size(); idx++) {
      GuiButton btn = (GuiButton)controlList.get(idx);
      if (btn.id<100)
        continue;
      if (btn.id==200)
        continue;
      if (btn.id==100 || btn.id==101) {
        top = btn.yPosition;
        if (btn.xPosition<(screen.width / 2))
          leftPos = btn.xPosition;
        else   
          rightPos = btn.xPosition;
      }
      if (btn.yPosition<top)
        continue;
      if (btn.xPosition==leftPos) {
        taken[(btn.yPosition - top) / 24][0] = true;
      } else if (btn.xPosition==rightPos) {
        taken[(btn.yPosition - top) / 24][1] = true;
      }  
    }
    int x=0, y=0;
outerloop:
    for (y=0; y<5; y++)
      for (x=0; x<2; x++)
        if (!taken[y][x])
          break outerloop;
    yPos = top + (y * 24);      
    if (x==0)           
      controlList.add(new OptionButton(301, leftPos, yPos, 150, 20, "Mod Options (Global)"));
    else
      controlList.add(new OptionButton(301, rightPos, yPos, 150, 20, "Mod Options (Global)"));
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
