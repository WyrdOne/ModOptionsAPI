package moapi.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class OptionButton extends GuiButton {
	public OptionButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int par2, int par3) {
    if (enabled && drawButton && par2>=xPosition && par3>=yPosition && par2<xPosition+width && par3<yPosition+height) {
      ClientGui.handleCommands(mc, mc.currentScreen, this);
     	return true;
    }
    return false;
  }
}
