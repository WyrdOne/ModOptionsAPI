package net.minecraft.src;

/**
* Interface class for GuiAPI. 
*
* @author Jonathan Brazell (WyrdOne)
* @version 0.9.1
*/
public class GuiAPILink {
	/**
	* Calls GuiAPI.  Comment code out to compile without GuiAPI installed.
	*
	* @author	Jonathan Brazell (WyrdOne)
	* @version	0.9.1
	*/
  public static final void CallGuiAPI(GuiScreen screen) {
    ModSettingScreen.guiContext = "";
    WidgetSetting.updateAll();
    GuiModScreen.show(new GuiModSelect(screen));
  }
}
