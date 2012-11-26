package net.minecraft.src;

import java.lang.reflect.*;
import net.minecraft.client.Minecraft;
import moapi.api.*;
import moapi.clientgui.*;

public class mod_MOAPI_Simple extends BaseMod {
  // Copyright/license info
  private static final String Name = "MOAPI Simple Example";
  private static final String Version = "0.2 (For use with Minecraft 1.4.4)";
	private static final String Copyright = "All original code and images (C) 2012, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
  // Options
  public static ModOptions options;
  // Other variables
  private static Block moapiBlock; 
      	
  public void load() {
    // Set up options menu for mod options
    initModOptionsAPI();
    // Create our block
    moapiBlock = ((Block)(new MOAPIBlock(213, 16, Material.wood))).setBlockName("moapi"); 
    // Register our block
    ModLoader.registerBlock(moapiBlock);
	}

  private void initModOptionsAPI() {
    // Create option screen
    options = new ModOptions(Name);
    // Add option screen to the Mod Options menu
    ModOptionsAPI.addMod(options);
    // Add slider for quantity to drop when block is harvested.
    options.addSlider("Quantity Dropped", 1, 64).setValue(1.0f).addFormatter(StdFormatters.integerSlider);
	  // Add mapped option for the texture to use for the block.
    options.addMappedOption("Block Texture",
      new int[]   {16,            2,      4,        7,       8,     49},       // Values
      new String[]{"Cobblestone", "Dirt", "Planks", "Brick", "TNT", "Glass"}); // Display text
    // Once you have added ALL of your options, use the load command to
    // retrieve current option values which will overwrite any defaults unless
    // there is no value
    options.loadValues();
    // Save current values
    options.save();
  }

  public String getName() {
    return Name;
  }
	
  public String getVersion() {
    return Version;
  }
}
