package moapi.clientgui;

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.client.Minecraft;
import moapi.api.*;

/**
* GUI for getText() field within MOAPI
*
* @author	Clinton Alexander
* @version	1.0.0.0
* @since	0.7
*/
public class KeyBindingField extends TextInputField {
	/**
	* If this is true, then the text area will take up two
	* positions on the grid
	*/
	private boolean wide = false;
	
  public KeyBindingField(int id, GuiScreen guiscreen, FontRenderer fontrenderer, int x, int y, ModKeyOption option, boolean global) {
		super(id, x, y, fontrenderer);
    enabled = true;
    parentGuiScreen = guiscreen;
    xPosition = x;
    yPosition = y;
    width 		= 70;
    height 		= 20;
		this.option = option;
		this.global = global;
  }
	
	/**
	* Set wide
	*
	* @param	wide	Whether we are to use a wide name area
	*/
	public void setWide(boolean wide) {
		wide = true;
	}

	/**
	* The new value to give this button and the option supporting it
	*
	* @param	c	Character to set
	*/
	private void setKey(Integer c) {
		((ModKeyOption) option).setValue(c, global);
	}
	
	/**
	* Get the current key bound fo this field
	*
	* @return	Character for this field
	*/
	private Integer getKey() {
		return ((ModKeyOption) option).getValue(global);
	}
	
	/**
	* Method for handling keyboard input
	*
	* @param	c	Character types
	* @param	i	Integer representation of c
	*/
  public void textboxKeyTyped(char c, int i) {
		Integer val = new Integer(i);
    if(enabled && isFocused()) {
      if(c == '\026') {
			// Enter a char
			} else if(!ModKeyOption.isKeyBound(val)) {
				setKey(val);
				setFocused(false);
			} else if(val == ((ModKeyOption) option).getValue(global)) {
				setFocused(false);
			}
		}
  }

	/**
	* Draw a textarea with a label inside and an editable text space
	*/
  public void drawButton(Minecraft minecraft, int i, int j) {
		Integer	key			= getKey();
		String 	text		= option.getDisplayString(!global);
		String	name		= option.getName();
		int		nameWidth 	= wide ? parentGuiScreen.width / 2 - width : parentGuiScreen.width - width;
		boolean blink		= (getCursorCounter() / 6) % 2 == 0;
		
		if((!isFocused()) || (blink)) {
			displayString = text;
		} else {
			displayString = "> " + text + " <";
		}
		super.drawButton(minecraft, i, j);
		drawString(fontRenderer, option.getName(), xPosition + 85, yPosition + (height - 8) / 2, 0xFFFFFF);
  }
}
