package moapi.client;

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;
import moapi.*;

/**
* GUI for getText() field within MOAPI
*
* @author	Clinton Alexander
* @author Jonathan Brazell
* @version	1.0.1
* @since	0.7
*/
public class ModOptionGuiTextField extends ModOptionButton {
	/** For rendering text */
  private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

	/** For determining current focus status */
	private boolean isFocused = false;

	/** For allowing cursor to blink */
  private int cursorCounter;

  public ModOptionGuiTextField(int id, int x, int y, int width, int height, ModOption option, boolean worldMode) {
    super(id, x, y, width, height, option, worldMode);
  }

  public ModOptionGuiTextField(int id, int x, int y, ModOption option, boolean worldMode) {
    super(id, x, y, option, worldMode);
  }

	/**
	* Sets the text value for this option
	*
	* @param	s		New string to display and use for option value
	*/
  protected void setText(String s) {
		int maxlen = ((ModOptionText)option).getMaxLength();
		if ((s.length() > maxlen) && (maxlen > 0)) {
			s = s.substring(0, maxlen - 1);
		}
		((ModOptionText)option).setValue(s, !worldMode);
	}
	
  public void textboxKeyTyped(char c, int i) {
		String text = ((ModOptionText)option).getValue(!worldMode);
		String s = GuiScreen.getClipboardString();
		int max = ((ModOptionText)option).getMaxLength();
		int j;
		
    if (enabled && isFocused()) {
      if (c == '\026') {
				if (s == null) {
					s = "";
				}
				j = 32 - text.length();
				if (j > s.length()) {
					j = s.length();
				}
				if (j > 0) {
					text += s.substring(0, j);
				}
			} else if (i == 14 && text.length() > 0) {
				text = text.substring(0, text.length() - 1);
			} else if (ChatAllowedCharacters.allowedCharacters.indexOf(c) >= 0 && ((text.length() < max) || (max == 0))) {
				text += c;
      }
			setText(text);
		}
  }

	/**
	* Draw a textarea with a label inside and an editable text space
	*/
  public void drawButton(Minecraft minecraft, int i, int j) {
		String text = ((ModOptionText)option).getValue(!worldMode);
		String name = option.getName();
		int maxlen = ((ModOptionText)option).getMaxLength();
		int len = text.length();
		int padding = 30;
		String counterStr	= (maxlen > 0) ?  "(" + len + "/" + maxlen + ")" : "";
		int nameWidth	= fontRenderer.getStringWidth(name);
		int textWidth	= fontRenderer.getStringWidth(text);
		int counterWidth = fontRenderer.getStringWidth(counterStr);
		
		// Remove excess padding when there is no counter
		if (maxlen <= 0) {
			padding = padding - 10;
		}
		
		// Reduce string until it is a decent length.
		// Don't worry about optimising too much, can optimise
		// if it becomes an issue
		while (nameWidth + textWidth + counterWidth + padding > width) {	
			len--;
			text = text.substring(1, len);
			textWidth = fontRenderer.getStringWidth(text);
		}
    if (enabled) {
      drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, 0xffa0a0a0);
      drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xff000000);
  		// Add a max length counter
  		if(maxlen > 0) {
  			drawString(fontRenderer, counterStr, xPosition + 300 - 5 - counterWidth, yPosition + (height - 8) / 2, 0x707070);
  		}
    }
		drawString(fontRenderer, option.getName(), xPosition + 4, yPosition + (height - 8) / 2, 0x707070);
    boolean flag = isFocused() && (getCursorCounter() / 6) % 2 == 0;
    drawString(fontRenderer, (new StringBuilder()).append(text).append(flag ? "_" : "").toString(), xPosition + nameWidth + 10, yPosition + (height - 8) / 2, 0xe0e0e0);
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
