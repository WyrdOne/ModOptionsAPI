package moapi.client;

import net.minecraft.src.*;
import moapi.*;

/**
* GUI for getText() field within MOAPI
*
* @author	Clinton Alexander
* @version	1.0.0.0
* @since	0.7
*/
public class ModOptionGuiKey extends ModOptionButton {
	/** For rendering text */
  private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

	/** For determining current focus status */
	private boolean isFocused = false;

	/** For allowing cursor to blink */
  private int cursorCounter;
	
  public ModOptionGuiKey(int id, int x, int y, int width, int height, ModOption option, boolean worldMode) {
    super(id, x, y, width, height, option, worldMode);
  }

  public ModOptionGuiKey(int id, int x, int y, ModOption option, boolean worldMode) {
    super(id, x, y, option, worldMode);
  }
	
	/**
	* The new value to give this button and the option supporting it
	*
	* @param	c	Character to set
	*/
	private void setKey(int c) {
		((ModOptionKey)option).setValue(c, !worldMode);
	}
	
	/**
	* Get the current key bound fo this field
	*
	* @return	Character for this field
	*/
	private int getKey() {
		return ((int)((ModOptionKey)option).getValue(!worldMode));
	}
	
	/**
	* Method for handling keyboard input
	*
	* @param	c	Character types
	* @param	i	Integer representation of c
	*/
  public void textboxKeyTyped(char c, int i) {
    if (enabled && isFocused()) {
      if (c == '\026') {
			// Enter a char
			} else if (!ModOptionKey.isKeyBound(i)) {
				setKey(i);
				setFocused(false);
			} else if (i==((ModOptionKey)option).getValue(!worldMode)) {
				setFocused(false);
			}
		}
  }

	/**
	* Draw a textarea with a label inside and an editable text space
	*/
  public void drawButton(Minecraft minecraft, int i, int j) {
		String 	text		= option.getDisplayString(!worldMode);
		boolean blink		= (getCursorCounter() / 6) % 2 == 0;
		
		if ((!isFocused()) || (blink)) {
			displayString = text;
		} else {
			displayString = "> " + text + " <";
		}
		super.drawButton(minecraft, i, j);
		drawString(fontRenderer, option.getName(), xPosition + 85, yPosition + (height - 8) / 2, 0xFFFFFF);
  }

	/**
	* Get the current cursor counter value
	*
	* @return	Current cursor counter value
	*/
	protected int getCursorCounter() {
		return cursorCounter;
	}
	
	/**
	* Update the cursor counter value
	*/
	public void updateCursorCounter() {
		cursorCounter++;
	}
	
	/**
	* Set whether the textarea is in focus or not
	*
	* @param	flag	True when focused
	*/
  public void setFocused(boolean flag) {
    if (flag && !isFocused) {
      cursorCounter = 0;
    }
    isFocused = flag;
  }
	
	/**
	* Check whether this text input field is focused or not
	*
	* @return	True when focused
	*/
	public boolean isFocused() {
		return isFocused;
	}
}
