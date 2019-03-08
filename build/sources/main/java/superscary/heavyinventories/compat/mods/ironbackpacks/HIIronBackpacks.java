package superscary.heavyinventories.compat.mods.ironbackpacks;

import net.minecraftforge.common.config.Configuration;
import superscary.heavyinventories.HeavyInventories;

import java.io.File;

public class HIIronBackpacks
{

	private static final File CONFIG_DIR = new File(HeavyInventories.getReaderDirectory() + "/Heavy Inventories/Mod Configs", "Iron Backpacks.cfg");
	public static Configuration IR_CONFIG = new Configuration(CONFIG_DIR);

	public HIIronBackpacks()
	{
		registerConfig();
	}

	public static void registerConfig()
	{
		if (IR_CONFIG == null)
		{
			IR_CONFIG = new Configuration(CONFIG_DIR);
		}
		loadConfig();
	}

	public static void loadConfig()
	{

	}

}
