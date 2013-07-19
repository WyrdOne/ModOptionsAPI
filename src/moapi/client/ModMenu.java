package moapi.client;

import java.util.List;
import net.minecraft.src.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import moapi.*;

/**
* A scrollable screen for Mod Options API
*
* @author	Clinton Alexander (with credit to Mojang for the drawscreen code)
* @author Jonathan Brazell
* @version 1.4.7
* @since	0.6
*/
public class ModMenu extends GuiScreen {
	/**
	* Currently selected button
	*/
	protected GuiButton curButton = null;
	/**
	* Slider positional value
	*/
	private int sliderMiddle = -1;
	/**
	* True if slider dragging in progress
	*/
	private boolean draggingSlider = false;
	/**
	* Title of this menu
	*/
  protected String screenTitle;
  private GuiScreen parentScreen;
  private GameSettings options;
	
	/**
	* Current set of options that we are displaying
	*/
	private ModOptions modOptions 	= null;
	
	/**
	* Whether we are in world specific mode or not
	*/
	private boolean	worldMode = false;
	/**
	* World name
	*/
	private String worldName;

	/**
	* Initialise settings
	*/
	private ModMenu(GuiScreen parent, String title, boolean world) {
		super();
		parentScreen 		= parent;
		worldMode 			= world;
		screenTitle			= title;
		
		if(!Keyboard.areRepeatEventsEnabled()) {
			Keyboard.enableRepeatEvents(true);
		}
	}
	
	/**
	* Initilise options menu gui screen
	*/
  public ModMenu(GuiScreen guiscreen) {
		this(guiscreen, "Mod Options List", false);
  }
	
	/**
	* Initialise world options menu with a named world
	*/
	public ModMenu(GuiIngameMenu guiscreen, String name) {
		this(guiscreen, "World Specific Mod Options", true);
		worldName = name;
	}
	
	/**
	* Initialise a particular set of options gui
	* 
	* @param	name	Name of world loaded
	*/
	public ModMenu(GuiScreen guiscreen, ModOptions options, String name) {
    this(guiscreen, options.getName() + " Options", true);
		modOptions 		= options;
		worldName		= name;
	}
	
	/**
	* Initialise a particular set of options gui
	*/
	public ModMenu(GuiScreen guiscreen, ModOptions options) {
    this(guiscreen, options.getName() + " Options", false);
		modOptions 		= options;
	}
	
  public void initGui() {
		if (modOptions == null) {
			// Load only relevant mods
			ModOptions[] options = new ModOptions[0];
			if (Minecraft.getMinecraft().isIntegratedServerRunning() || !worldMode) {
				options = ModOptionsAPI.getAllMods();
			} else {
				options = ModOptionsAPI.getClientMods();
      }
			loadModList(options);
		} else {
			loadModOptions();
		}
  }
	
	/**
	* Loads the initial list of mods
	*
	* @param	options		List of mods to display
	*/
	private void loadModList(ModOptions[] options) {
		int xPos = width / 2 - 152;
		int yPos;
		
		for(int i = 0; i < options.length; i++) {
			yPos = 41 + (i *24);
			buttonList.add(new ModOptionButton(i, xPos, yPos, 304, 20, options[i], worldMode));
		}
		buttonList.add(new GuiButton(200, width / 2 - 100, height - 28, 200, 20, "Done"));
	}
	
	/**
	* Loads a set of mod options buttons
	*/
	private void loadModOptions() {
		screenTitle = modOptions.getName();
		ModOption[] options;
		if (Minecraft.getMinecraft().isIntegratedServerRunning() || !worldMode) {
			options = modOptions.getOptions();
		} else {
			options = modOptions.getClientOptions();
		}
		int id = 0;
		int pos = 0;
		for (ModOption option : options) {
			if (option.isWide() && (pos % 2)!=0) {
				pos++;
			}
			addModOptionButton(option, id, pos);
			if (option.isWide()) {
				pos += 2;
			} else {
				pos++;
			}
			id++;
		}
		buttonList.add(new GuiButton(200, width / 2 - 100, height - 28, 200, 20, "Done"));
	}
	
	/**
	* Adds an option button to the GUI
	*
	* @param	op 	Option
	* @param	id	id for buttn
	* @param	pos	Used for positioning a button
	*/
	private void addModOptionButton(ModOption option, int id, int pos) {
		int xPos = (width / 2 - 152) + (pos % 2) * 154;
		int yPos = 41 + ((pos>>1)*24);
		if (option instanceof ModOptionSlider) {
			buttonList.add(new ModOptionGuiSlider(id, xPos, yPos, option, worldMode));
		} else if (option instanceof ModOptionText) {
			buttonList.add(new ModOptionGuiTextField(id, xPos, yPos, option, worldMode));
		} else if (option instanceof ModOptionKey) {
			buttonList.add(new ModOptionGuiKey(id, xPos, yPos, option, worldMode));
		} else {
			buttonList.add(new ModOptionButton(id, xPos, yPos, option, worldMode));
		}
	}
	
	/**
	* Draw our slider based options screen
	*/
  public void drawScreen(int i, int j, float f)  {
    drawDefaultBackground();
    drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
		if (sliderMiddle == -1) {
			setInitialSlider();
		} else if (getSliderTop() > getSliderAreaTop()) {
			sliderMiddle = getUpperSliderBound();
		} else if (getSliderBottom() < getSliderAreaBottom()) {
			setInitialSlider();
		}
		addButtons(i, j);
		int sliderTop 		= getSliderTop();
		int sliderBottom 	= getSliderBottom();
		int sliderLeft 		= getSliderLeft();
		int sliderRight 	= getSliderRight();
		// Draw the container
		drawRect(sliderLeft, getSliderAreaBottom(), sliderRight, getSliderAreaTop(), 0x80000000);
		// Draw the slider
		drawRect(sliderLeft, sliderBottom, sliderRight, sliderTop, 0xffcccccc);
		sliderDragged(i, j);
	}
	
	/**
	* Adds the buttons to the screen
	*
	* @param	i
	* @param	j
	*/
	private void addButtons(int i, int j) {
		// Boundes for content
		int contentTop 		= getContentTop();
		int contentBottom 	= getContentBottom();
		int bottom 			= getSliderAreaBottom();
		int top				= getSliderAreaTop();
		
		for(int k = 0; k < buttonList.size(); k++) {
			GuiButton btn = (GuiButton) buttonList.get(k);
			if(btn.id != 200) {
				int y = btn.yPosition;
				btn.yPosition = y - contentBottom;
				if((btn.yPosition > bottom) && ((btn.yPosition + 20) < top)) {
					btn.drawButton(mc, i, j);
				}
				btn.yPosition = y;
			} else {
				btn.drawButton(mc, i, j);
			}
    }
	}
	
	/**
	* Control the mouse clicks so that we can let right
	* click turn on "global" mode
	*
	* @param	i	x pos
	* @param	j	y pos
	* @param	k	1 for right click, 0 for left
	*/
  protected void mouseClicked(int i, int j, int k) {
		setCurrentButton(null);
		if (k == 0) { // left click
			if ((i > getSliderLeft()) && (i < getSliderRight()) && (j > getSliderBottom()) && (j < getSliderTop())) {
				setSliderMiddle(j);
			} else if ((i > getSliderLeft()) && (i < getSliderRight()) && (j > getSliderAreaBottom()) && (j < getSliderAreaTop())) {
				setSliderMiddle(j);
			} else {
				// Pick a button
				for (int l = 0; l < buttonList.size(); l++) {
					GuiButton guibutton = (GuiButton)buttonList.get(l);
					if (buttonPressed(guibutton, i, j, false)) {
						setCurrentButton(guibutton);
					}
				}
			}
		} else if (k == 1) { // Right click.
			for (int l = 0; l < buttonList.size(); l++) {
				GuiButton guibutton = (GuiButton)buttonList.get(l);
				if (buttonPressed(guibutton, i, j, true)) {
					setCurrentButton(guibutton);
					altActionPerformed(guibutton);
				}
			}
		}
  }
	
	/** Ensures that when escape is pressed that the changed options are saved. */
  protected void keyTyped(char c, int i) {
    if (i==1) {
			changeScreen(null);
    } else if (curButton instanceof ModOptionGuiTextField) {
		  ((ModOptionGuiTextField)curButton).textboxKeyTyped(c, i);
    } else if (curButton instanceof ModOptionGuiKey) {
		  ((ModOptionGuiKey)curButton).textboxKeyTyped(c, i);
		}
  }
		
	/**
	* Mouse has left clicked the given button
	*
	* @param	guibutton	Button left clicked
	*/
  protected void actionPerformed(GuiButton guibutton) {
    if (guibutton.id == 200) { // We have clicked the "Done" button
			changeScreen(parentScreen);
    } else if (modOptions==null) { // We are in the mod menu and picking a mod
			ModOptionButton btn = (ModOptionButton)guibutton;
 			ModOptions modOp = (ModOptions)btn.getOption();
		  if ((!modOp.hasCallback()) || (modOp.getCallback().onClick(modOp))) {
  			// Choose the type of mod options
  			if (worldMode) {
  				mc.displayGuiScreen(new ModMenu(this, modOp, worldName));
  			} else {
  				mc.displayGuiScreen(new ModMenu(this, modOp));
  			}
  		}
    } else if ((guibutton.id < 100 && (guibutton instanceof ModOptionGuiSlider))) { // A slider has been dragged	
      // Ignore, handled internally
		} else if(guibutton.id < 100) { // A button has been pressed
      if (((ModOptionButton)guibutton).getOption() instanceof ModOptions) { // Sub menu
  			ModOptionButton btn = (ModOptionButton)guibutton;
  			// Choose the type of mod options
  			ModOptions modOp = (ModOptions)btn.getOption();
  			if (worldMode) {
  				mc.displayGuiScreen(new ModMenu(this, modOp, worldName));
  			} else {
  				mc.displayGuiScreen(new ModMenu(this, modOp));
  			}
      } else		
			  optionButtonPressed(guibutton);
		}
  }
	
	/**
	* Button right clicked on the given button
	*
	* @param	guibutton	button pressed
	*/
	protected void altActionPerformed(GuiButton guibutton) {
		ModOptionButton btn = (ModOptionButton)guibutton;
		if ((modOptions != null) && (guibutton.id < 100) && (worldMode)) {
 			ModOption option = btn.getOption();
			option.setGlobal(!option.useGlobalValue());
			btn.updateDisplayString();
		// User right clicks menu - resets all values to global
		} else if ((worldMode) && ((modOptions==null) || (guibutton.id<200))) {
			ModOptions modOp = (ModOptions)btn.getOption();
			if (modOp != null) {
				// Sets all values to global
				// in applicable menus
				modOp.globalReset(true);
			}
		}
	}
	
	/**
	* More control over the mouse
	* Called when mouse is unclicked
	*
	* @param	i	x pos
	* @param	j	y pos
	* @param	k	1 for right click, 0 for left
	*/
  protected void mouseMovedOrUp(int i, int j, int k) {
		// Text fields need constant focus, so do not set curbutton to null
		// when a textbox is focused
		if (!(curButton instanceof ModOptionGuiTextField) && !(curButton instanceof ModOptionGuiKey) && curButton != null && k == 0) {
      curButton.mouseReleased(i, j);
      curButton = null;
    } else if ((draggingSlider) && (k == 0)){
			draggingSlider = false;
		}
  }
	
	/**
	* What to do on screen updates
	*/
	public void updateScreen() {
		super.updateScreen();
		// Update all text field cursor blinking
		for(Object obj : buttonList) {
		  if (obj instanceof ModOptionGuiTextField)
				((ModOptionGuiTextField)obj).updateCursorCounter();
		  else if (obj instanceof ModOptionGuiKey)
				((ModOptionGuiKey)obj).updateCursorCounter();
		}
	}
	
	/**
	* Change the screen and perform cleanup actions
	*
	* @param	screen
	*/
	public void changeScreen(GuiScreen screen) {
    // Save
		if (modOptions != null) {
			if (worldMode) {
				modOptions.saveValues(worldName);
			} else {
				modOptions.saveValues("");
			}
		}
		mc.displayGuiScreen(screen);
		// Re-disable repeat keypress
		if (!(screen instanceof ModMenu)) {
			Keyboard.enableRepeatEvents(false);
		}
		// If ingame, set to ingame focus
		if (worldMode && (screen == null)) {
			mc.setIngameFocus();
		}
	}
	
	/**
	* Drag the slider about
	*
	* @param	i	x pos
	* @param	j	y pos
	*/
	private void sliderDragged(int i, int j) {
		if(draggingSlider) {
			setSliderMiddle(j);
		}
	}
	
	/**
	* Set the currently selected button
	*
	* @param	btn		Button
	*/
	private void setCurrentButton(GuiButton btn) {
	  if (curButton instanceof ModOptionGuiTextField)
			((ModOptionGuiTextField)curButton).setFocused(false);
	  if (curButton instanceof ModOptionGuiKey)
			((ModOptionGuiKey)curButton).setFocused(false);
		curButton = btn;
	  if (curButton instanceof ModOptionGuiTextField)
			((ModOptionGuiTextField)curButton).setFocused(true);
	  if (curButton instanceof ModOptionGuiKey)
			((ModOptionGuiKey)curButton).setFocused(true);
		if (curButton != null) {
			mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			actionPerformed(curButton);
		}
	}
	
	/**
	* Check if a button has been pressed
	*
	* @param	btn		Button
	* @return	true if pressed
	*/
	protected boolean buttonPressed(GuiButton btn, int i, int j) {
		return buttonPressed(btn, i, j, false);
	}
	
	/**
	* Check if a button has been pressed
	*
	* @param	btn			Button
	* @param	rightClick	True if right click
	* @return	true if pressed
	*/
	protected boolean buttonPressed(GuiButton btn, int i, int j, boolean rightClick) {
		int contentBottom 	= getContentBottom();
		int bottom 			= getSliderAreaBottom();
		int top				= getSliderAreaTop();
		boolean flag 		= false;
		
		if (btn.id != 200) {
			int y = btn.yPosition;
			btn.yPosition = y - contentBottom;
			if ((btn.yPosition > bottom) && ((btn.yPosition + 20) < top)) {
				// Need to send the type of click to the slider due to
				// dragging behaviour
				if (btn instanceof ModOptionGuiSlider) {
					flag = ((ModOptionGuiSlider)btn).altMousePressed(mc, i, j, rightClick);
				} else {
					if (btn.mousePressed(mc, i, j)) {
						flag = true;
					}
				}
			}
			btn.yPosition = y;
		} else {
			flag = btn.mousePressed(mc, i, j);
		}
		return flag;
	}
	
	/**
	* Set the initial slider value
	*/
	private void setInitialSlider() {
		sliderMiddle = getLowerSliderBound();
	}
	
	/**
	* Set the slider middle value
	*
	* @param	val		New val
	*/
	private void setSliderMiddle(int val) {
		if(val > getUpperSliderBound()) {
			sliderMiddle = getUpperSliderBound();
		} else if(val < getLowerSliderBound()) {
			sliderMiddle = getLowerSliderBound();
		} else {
			sliderMiddle = val;
		}
		draggingSlider = true;
		setCurrentButton(curButton);
	}
	
	/**
	* Gets the upper slider bound
	*
	* @return	Upper slider bound
	*/
	private int getUpperSliderBound() {
		return getSliderAreaTop() - (getSliderHeight() / 2);
	}
	
	/**
	* Gets the lower slider bound
	*
	* @return	Lower slider bound
	*/
	private int getLowerSliderBound() {
		return getSliderAreaBottom() + (getSliderHeight() / 2);
	}
		
	
	/**
	* Returns the top of the slider
	* 
	* @return	top of slider
	*/
	private int getSliderTop() {
		return sliderMiddle + (getSliderHeight() / 2);
	}
	
	/**
	* Returns the left of the slider
	*
	* @return	left of the slider
	*/
	private int getSliderBottom() {
		return sliderMiddle - (getSliderHeight() / 2);
	}
	
	/**
	* Returns the height of the slider
	*
	* @return	slider height
	*/
	private int getSliderHeight() {
		int contHeight = getContentHeight();
		int areaHeight = getSliderAreaHeight();
		
		if(contHeight < areaHeight) {
			return areaHeight;
		} else {
			return (int) (((double) areaHeight / (double) contHeight) * (double) areaHeight);
		}
	}
	
	/**
	* Returns the content height
	*
	* @return	Content height
	*/
	private int getContentHeight() {
		int height = 1;
		int bottom = getSliderAreaBottom();
		for (int k = 0; k < buttonList.size(); k++) {
			GuiButton guibutton = (GuiButton)buttonList.get(k);
			if (guibutton.id != 200) {
				if (guibutton.yPosition - bottom > height) {
					height = guibutton.yPosition - bottom;
				}
			}
    }
		// An extra 100 for tolerance
		return height + 100;
	}
	
	/**
	* Get the top of the content area
	*
	* @return	top of the content area
	*/
	private int getContentTop() {
		int top = getSliderTop() - getSliderAreaBottom();
		int contHeight = getContentHeight();
		int areaHeight = getSliderAreaHeight();
		
		double prop;
		if (contHeight < areaHeight) {
			prop = 1;
		} else {
			prop = (double) getContentHeight() / (double) getSliderAreaHeight();
		}
		return (int) ((double) top * prop);
	}
	
	/**
	* Get the bottom of the content area
	*
	* @return	bottom of the content area
	*/
	private int getContentBottom() {
		int bot = getSliderBottom() - getSliderAreaBottom();
		int contHeight = getContentHeight();
		int areaHeight = getSliderAreaHeight();
		double prop;
		if (contHeight < areaHeight) {
			prop = 1;
		} else {
			prop = getContentHeight() / getSliderAreaHeight();
		}
		return (int) ((double) bot * prop);
	}
	
	/**
	* Returns the left of the slider
	*
	* @return	left of the slider
	*/
	private int getSliderLeft() {
		return width - 20;
	}
	
	/**
	* Returns the left of the slider
	*
	* @return	left of the slider
	*/
	private int getSliderRight() {
		return width - 10;
	}
	
	/**
	* Returns the slider area top
	*
	* @return	slider area top
	*/
	private int getSliderAreaTop() {
		return height - 40;
	}
	
	/**
	* Returns the slider area bottom
	*
	* @return	slider area bottom
	*/
	private int getSliderAreaBottom() {
		return 40;
	}
	
	/**
	* Returns the slider area height
	*
	* @return	Slider area height
	*/
	private int getSliderAreaHeight() {
		int height = (getSliderAreaTop() - getSliderAreaBottom());
		return (height > 0) ? height : 1;
	}
	
	/**
	* Update a value upon an option button press
	*
	* @param	btn		Button that was pressed
	*/
	private void optionButtonPressed(GuiButton btn) {
    ModOption option = ((ModOptionButton)btn).getOption();
		
		// Callback for when a button is clicked
		if ((!option.hasCallback()) || (option.getCallback().onClick(option))) {
			// If option is global on, turn it to a local enabled value
			if ((worldMode) && (option.useGlobalValue()) && ((!option.hasCallback()) || (option.getCallback().onGlobalChange(false, option)))) {
				option.setGlobal(false);
			}
			if (option instanceof ModOptionMulti) {
    		if (worldMode) {
    			String nextVal = ((ModOptionMulti)option).getNextValue(((ModOptionMulti)option).getLocalValue());
    			option.setLocalValue(nextVal);
    		} else {
    			String nextVal = ((ModOptionMulti)option).getNextValue(((ModOptionMulti)option).getGlobalValue());
    			option.setGlobalValue(nextVal);
    		}
      } else if (option instanceof ModOptionBoolean) {
    		if (worldMode) {
    			option.setLocalValue(!((ModOptionBoolean)option).getLocalValue());
    		} else {
    			option.setGlobalValue(!((ModOptionBoolean)option).getGlobalValue());
    		}
      } else if (option instanceof ModOptionMapped) {
    		if (worldMode) {
    			int nextVal = ((ModOptionMapped)option).getNextValue(((ModOptionMapped)option).getLocalValue());
    			option.setLocalValue(nextVal);
    		} else {
    			int nextVal = ((ModOptionMapped)option).getNextValue(((ModOptionMapped)option).getGlobalValue());
    			option.setGlobalValue(nextVal);
    		}
      }
			((ModOptionButton)btn).updateDisplayString();
		}
	}
}
