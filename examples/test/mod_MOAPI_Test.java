package net.minecraft.src;

import net.minecraft.client.Minecraft;
import moapi.api.*;
import moapi.clientgui.*;

public class mod_MOAPI_Test extends BaseMod {
  // Copyright/license info
  private static final String Name = "MOAPI Test Example";
  private static final String Version = "0.2 (For use with Minecraft 1.5)";
	private static final String Copyright = "All original code and images (C) 2012-2013, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
  // Options
  public static ModOptions options;
      	
  public void load() {
    // Set up options menu for mod options
    initModOptionsAPI();
	}

  private void initModOptionsAPI() {
    // Create option screen
    options = ModOptionsAPI.addMod(Name);
	  // Add a text option with infinite max length
	  options.addTextOption("Text Option 1");
	  // Add a text option with a max length
    options.addTextOption("Text Option 2", 10);
    // Add a multi option
    options.addMultiOption("Multi Option", new String[]{"Value 1", "Value 2", "Value 3", "Value 4"});
	  // Add mapped option
    options.addMappedOption("Mapped",
      new String[]{"Mapped 1", "Mapped 2", "Mapped 3", "Mapped 4"}, // Display text
      new int[]   {1,          2,          3,          4});         // Values
    // Add toggle (boolean) option (on/off)
	  options.addBooleanOption("Toggle 1");
    // Add Slider with default value
    options.addSliderOption("Slider", 1, 64).setValue(1);
    // Once you have added ALL of your options, use the load command to
    // retrieve current option values which will overwrite any defaults unless
    // there is no value
    options.loadValues();
    // Save current values
    options.saveValues();
    // Add dummy mod options to test the menu scrolling
    ModOptionsAPI.addMod("Dummy Mod 1");
    ModOptionsAPI.addMod("Dummy Mod 2");
    ModOptionsAPI.addMod("Dummy Mod 3");
    ModOptionsAPI.addMod("Dummy Mod 4");
    ModOptionsAPI.addMod("Dummy Mod 5");
    ModOptionsAPI.addMod("Dummy Mod 6");
    ModOptionsAPI.addMod("Dummy Mod 7");
    ModOptionsAPI.addMod("Dummy Mod 8");
    ModOptionsAPI.addMod("Dummy Mod 9");
  }

  public String getPriorities() {
    return "required-after:mod_MOAPI";
  }

  public String getName() {
    return Name;
  }
	
  public String getVersion() {
    return Version;
  }
}
