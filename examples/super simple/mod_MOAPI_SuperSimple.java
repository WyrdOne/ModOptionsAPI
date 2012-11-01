package net.minecraft.src;

import java.lang.reflect.*;
import net.minecraft.client.Minecraft;
import moapi.*;
import moapi.gui.*;

public class mod_MOAPI_SuperSimple extends BaseMod {
  // Copyright/license info
  private static final String Name = "MOAPI Super Simple Example";
  private static final String Version = "0.1 (For use with Minecraft 1.4.2)";
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
    // Load options from file (file needs to be in the /ModOptions/ folder)
    // in this case <Minecraft folder>/ModOptions/MOAPI Super Simple Example/MOAPI Super Simple Example.modoptions
    try {
      options = ModOptionsAPI.addMod(Name);
    } catch (Exception ingored) {}
    // Now use the load command to retrieve current option values which will
    // overwrite any defaults unless there is no value
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
