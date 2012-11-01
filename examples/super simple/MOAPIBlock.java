package net.minecraft.src;

import java.util.Random;

public class MOAPIBlock extends Block {
  protected MOAPIBlock(int par1, Material par2Material) {
    super(par1, par2Material);
  }

  protected MOAPIBlock(int par1, int par2, Material par3Material) {
    this(par1, par3Material);
    this.blockIndexInTexture = par2;
  }

  public int getBlockTextureFromSide(int par1) {
    // Get the desired texture from the options.
    this.blockIndexInTexture = mod_MOAPI_SuperSimple.options.getMappedValue("Block Texture");
    return this.blockIndexInTexture;
  }

  public int quantityDropped(Random par1Random) {
    // Get the quantity to drop from the slider option
    return (int)mod_MOAPI_SuperSimple.options.getSliderValue("Quantity Dropped");
  }
}
