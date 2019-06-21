package com.tiviacz.travellersbackpack.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidMilk extends Fluid
{
	public FluidMilk(String fluidName, ResourceLocation still, ResourceLocation flowing) 
	{
		super(fluidName, still, flowing);
		
		this.setUnlocalizedName(fluidName);
		this.setGaseous(false);
		this.setDensity(1200);
		this.setViscosity(1200);
		this.setLuminosity(0);
	}
}