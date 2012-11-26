package moapi.clientgui;

import java.util.*;
import java.lang.reflect.*;
import net.minecraft.src.*;
import net.minecraft.client.*;

/**
* Class to add and handle options in the Minecraft Client GUI 
*
* @author Jonathan Brazell (WyrdOne)
* @version 1.0.0
*/
public class ClientGui {
  private static StringTranslate translate = StringTranslate.getInstance();
  private static boolean needInit = true;
  private static boolean runningGuiAPI = false;

  private static void init() {
    needInit = false;
    runningGuiAPI = true;
    try {
      Class.forName("net.minecraft.src.ModSettings", false, ClientGui.class.getClassLoader());
    } catch (ClassNotFoundException e) {
      runningGuiAPI = false;
    }
  }

  public static void modifyGuiOptions(GuiScreen screen, List controlList) {
    if (needInit) {init();}
    if (runningGuiAPI) {
      // Remove current snooper button 
  		Iterator<?> itr = controlList.iterator();
		  while (itr.hasNext()) {
		    GuiButton button = (GuiButton)itr.next();
        if (button.id==104) {
					itr.remove();
        }
		  }
  		controlList.add(new GuiButton(301, screen.width / 2 - 152, screen.height / 6 + 144 - 6, 98, 20, "MOAPI Options"));
      controlList.add(new GuiButton(300, screen.width / 2 -  49, screen.height / 6 + 144 - 6, 98, 20, "GUIAPI Settings"));
      controlList.add(new GuiButton(104, screen.width / 2 +  54, screen.height / 6 + 144 - 6, 98, 20, translate.translateKey("options.snooper.view")));
    } else {
  		controlList.add(new GuiButton(301, screen.width / 2 - 152, screen.height / 6 + 144 - 6, 150, 20, "Mod Options"));
      controlList.add(new GuiButton(104, screen.width / 2 + 2,   screen.height / 6 + 144 - 6, 150, 20, translate.translateKey("options.snooper.view")));
    }
  }
  
  public static void handleCommands(Minecraft mc, GuiScreen screen, GuiButton button) {
    switch (button.id) {
      case 300: // GuiOptions -> GuiAPI Options
  			mc.gameSettings.saveOptions();
  			CallGuiAPI(screen);
        break;
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

  public static void modifyGuiInGameMenu(GuiScreen screen, List controlList) {
    controlList.add(new GuiButton(302, screen.width / 2 - 100, screen.height / 4 + 72 - 16, "Mod World Options"));		
  }

	/**
	* Opens the GuiAPI option menu.
	*
	* @author	Jonathan Brazell (WyrdOne)
	* @version	1.0.0
	*/
  public static final void CallGuiAPI(GuiScreen screen) {
    // Original non-reflection code is commented
    //ModSettingScreen.guiContext = "";
    try {
      Class guiapiModSettingScreen = Class.forName("net.minecraft.src.ModSettingScreen", false, ClientGui.class.getClassLoader());
      Field guiContext = guiapiModSettingScreen.getDeclaredField("guiContext");
      guiContext.set(guiapiModSettingScreen, (Object)"");
    } catch (Exception e) {
      return;
    }
    //WidgetSetting.updateAll();
    try {
      Class guiapiWidgetSetting = Class.forName("net.minecraft.src.WidgetSetting", false, ClientGui.class.getClassLoader());
      Method method = guiapiWidgetSetting.getMethod("updateAll");
      method.invoke(null);
    } catch (Exception e) {
      return;
    }
    //GuiModScreen.show(new GuiModSelect(screen));
    try {
      Constructor constructor = Class.forName("net.minecraft.src.GuiModSelect", false, ClientGui.class.getClassLoader()).getClass().getConstructor(screen.getClass());
      Object guiapiGuiModSelect = constructor.newInstance(screen);
      Class guiapiGuiModScreen = Class.forName("net.minecraft.src.GuiModScreen", false, ClientGui.class.getClassLoader());
      Method method = guiapiGuiModScreen.getMethod("show", guiapiGuiModSelect.getClass());
      method.invoke(null, guiapiGuiModSelect);
    } catch (Exception e) {
      return;
    }
  }
}