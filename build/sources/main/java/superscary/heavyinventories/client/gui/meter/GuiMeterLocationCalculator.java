package superscary.heavyinventories.client.gui.meter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

public class GuiMeterLocationCalculator
{

	public static ArrayList<Integer> getBestLocation(Minecraft minecraft)
	{
		ArrayList<Integer> par1 = new ArrayList<>();
		par1.add(getBestLocationX(minecraft));
		par1.add(getBestLocationY(minecraft));
		return par1;
	}

	public static int getBestLocationX(ScaledResolution resolution)
	{
		return -1;
	}

	public static int getBestLocationX(Minecraft minecraft)
	{
		return getBestLocationX(new ScaledResolution(minecraft));
	}

	public static int getBestLocationY(ScaledResolution resolution)
	{
		return -1;
	}

	public static int getBestLocationY(Minecraft minecraft)
	{
		return getBestLocationY(new ScaledResolution(minecraft));
	}

	public static int getScreenSizeX(ScaledResolution resolution)
	{
		return resolution.getScaledWidth();
	}

	public static int getScreenSizeY(ScaledResolution resolution)
	{
		return resolution.getScaledHeight();
	}

}
