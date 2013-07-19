package moapi.client; 

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import moapi.*;

/**
* A replacement for the default MC Slider. 
*
* @author	Clinton Alexander
* @author Jonathan Brazell
* @version	1.0.1
* @since	0.5
*/
public class ModOptionGuiSlider extends ModOptionButton {
  public boolean dragging;
	
	/**
	* Construct a gui slider
	*
  * @param  id ID for the control
  * @param  x X position for the control
  * @param  y Y position for the control    
	* @param	op			Option Object this represents
	* @param	worldMode	True when using world values
	*/
  public ModOptionGuiSlider(int id, int x, int y, ModOption option, boolean worldMode) {
		super(id, x, y, option, worldMode);
		dragging = false;
  }
	
	/**
	* Get the value of the slider object for this scope
	*
	* @param	option	Option we want the value for
	* @return	Value
	*/
	private float getInternalValue(ModOptionSlider option) {
		float val;
		if (worldMode) {
			val = option.getLocalValue();
		} else {
		  val = option.getGlobalValue();
		}
		val = transformValue(val, 0, 1);
		return val;
	}
	
	/**
	* Perform action on mouse drag
	*
	* @param	minecraft	Game instance
	* @param	i			x position of drag end
	* @param	j			y position of drag end
	*/
  protected void mouseDragged(Minecraft minecraft, int i, int j) {
		if (drawButton) {
			if (dragging) {
				int value = untransformValue((float)(i - (xPosition + 4)) / (float)(width - 8), 0, 1); 
				if (worldMode) {
					if (option.useGlobalValue()) {
						option.setGlobal(false);
					}
					option.setLocalValue(value);
				} else {
					option.setGlobalValue(value);
				}
		    displayString = option.getDisplayString(!worldMode);
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		  float sliderValue = getInternalValue((ModOptionSlider)option);
			drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)), yPosition, 0, 66, 4, 20);
			drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)) + 4, yPosition, 196, 66, 4, 20);
		}
  }
	
	/**
	* Alternate Mouse press action to avoid issues with the obfuscator and to allow right click checks
	*
	* @param	minecraft	Game instance
	* @param	i			x position of click
	* @param	j			y position of click
	* @param	rightClick	True if a right click
	* @return	Keep changes
	*/
  public boolean altMousePressed(Minecraft minecraft, int i, int j, boolean rightClick) {
		// Avoid the dragging action for right click
		if ((rightClick) && (super.mousePressed(minecraft, i, j))) {
			return true;
		} else if (!rightClick) {
			return mousePressed(minecraft, i, j);
		} else {
			return false;
		}
	}
	
	/**
	* Mouse press action
	*
	* @param	minecraft	Game instance
	* @param	i			x position of click
	* @param	j			y position of click
	* @return	Keep changes
	*/
  public boolean mousePressed(Minecraft minecraft, int i, int j) {
		// Only accept a left click, using callbacks
		if(((!option.hasCallback()) || (option.getCallback().onClick(option))) && (super.mousePressed(minecraft, i, j))) {
			int value = untransformValue((float)(i - (xPosition + 4)) / (float)(width - 8), 0, 1); 
			if (worldMode) {
				if (((!option.hasCallback()) || (option.getCallback().onGlobalChange(false, option)))	&& (option.useGlobalValue())) {
					option.setGlobal(false);
				}
				option.setLocalValue(value);
			} else {
				option.setGlobalValue(value);
			}
  		displayString = option.getDisplayString(!worldMode);
      dragging = true;
      return true;
    } else  {
      return false;
    }
  }
	
  public void mouseReleased(int i, int j) {
    dragging = false;
  }
	
	protected int getHoverState(boolean flag) {
		return 0;
	}
	
	//==============
	// Transformers
	//==============

	/**
	* Returns a bounded value
	*
	* @param	value	Unbounded value
	* @param	lower	Lower bound
	* @param	upper	Upper bound
	* @return	Bounded value
	*/
	private float getBoundedValue(float value, int lower, int upper) {
		if(value < lower) {
			return (float)lower;
		} else if(value > upper) {
			return (float)upper;
		} else {
			return value;
		}
	}

	/**
	* Transforms the given value from it's current upper and lower bounds to the
	* corresponding place in the ones provided
	*
	* @param	value	The value to transform
	* @param	lower	Lower bound to transform to
	* @param	upper	Upper bound to transform to
	* @return	Value transformed from between low to high to between lower to upper
	*/
	public float transformValue(float value, int lower, int upper) {
		value = getBoundedValue(value, ((ModOptionSlider)option).getLowVal(), ((ModOptionSlider)option).getHighVal());
		float base = (value - ((ModOptionSlider)option).getLowVal()) / (((ModOptionSlider)option).getHighVal() - ((ModOptionSlider)option).getLowVal());
		float out = (base * (upper - lower)) + lower;
		return out;
	}
	
	/**
	* Transforms a value back from a given range into the local range
	*
	* @param	value	Transformed value
	* @param	lower	Lower bound to transform from
	* @param	upper	Upper bound to transform from
	* @return 	Value in the range of this slider
	*/
	public int untransformValue(float value, int lower, int upper) {
		value = getBoundedValue(value, lower, upper);
		float base = (value - lower) / (upper - lower);
		float out = (value * (((ModOptionSlider)option).getHighVal() - ((ModOptionSlider)option).getLowVal())) + ((ModOptionSlider)option).getLowVal();
		return (int)out;
	}
	
}
