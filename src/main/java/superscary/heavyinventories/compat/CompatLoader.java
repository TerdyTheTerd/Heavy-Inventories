package superscary.heavyinventories.compat;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import superscary.heavyinventories.compat.mods.HIBaubles;
import superscary.heavyinventories.compat.mods.theoneprobe.HITheOneProbe;
import superscary.heavyinventories.compat.mods.waila.HIWaila;
import superscary.heavyinventories.util.Logger;

import java.util.ArrayList;

public class CompatLoader
{

	// TODO: FINISH COMPATIBILITY LOADER

	/**
	 * This class will be used for default implemented mods to calculate weights of inventories
	 * such as Tinkers' Construct, Applied Energistics 2, and such.
	 */

	private static ArrayList<String> loadableMods = new ArrayList<>();

	/**
	 * Will build all compatibility files
	 */
	public static void build()
	{
		Logger.info("Finding compatible mods...");

		for (ModContainer container : Loader.instance().getActiveModList())
		{
			String modid = container.getModId();
			for (EnumCompatMods mods : EnumCompatMods.values())
			{
				if (modid.equals(mods.getCompatModid()))
				{
					Logger.info("Found %s!", modid);
					loadableMods.add(modid);
				}
			}
		}

		finish();
	}

	/**
	 * Finishes the implementation of the compatibility
	 */
	protected static void finish()
	{
		ArrayList<String> unloaded = new ArrayList<>();

		for (EnumCompatMods mod : EnumCompatMods.values()) unloaded.add(mod.getCompatModid());

		for (String s : loadableMods)
		{
			switch (s)
			{
				case "baubles": HIBaubles.register();
								unloaded.remove(s);
								break;
				case "theoneprobe": new HITheOneProbe();
								unloaded.remove(s);
								break;
				case "waila": new HIWaila();
								unloaded.remove(s);
								break;
				/*case "hwyla": new HIWaila();
				              unloaded.remove(s);
				              break;*/
				case "travellersbackpack":
								unloaded.remove(s);
				//case "wearablebackpacks":
								//unloaded.remove(s);
				default: break;
			}
		}

		for (String s : unloaded)
		{
			Logger.info("Disabled compatibility for %s", s);
		}
	}

}
