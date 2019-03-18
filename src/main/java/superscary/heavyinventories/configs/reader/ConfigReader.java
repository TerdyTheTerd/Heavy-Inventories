package superscary.heavyinventories.configs.reader;

import net.minecraftforge.common.config.Configuration;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigReader
{

	/**
	 * This is used to read through this mods config directory to find any mods that have integration with this mod.
	 */

	private static ArrayList<String> loadedMods = new ArrayList<>();
	private static ArrayList<Configuration> configurations = new ArrayList<>();
	private static HashMap<String, Configuration> configs = new HashMap<>();

	private static final String DIR = HeavyInventories.getReaderDirectory() + File.separator + "Heavy Inventories" + File.separator + "Weights" + File.separator;

	public static void addToHandshake(String mod)
	{
		if (!loadedMods.contains(mod))
		{
			loadedMods.add(mod);
		}
		readIntoMemory();
	}

	public static void handshake()
	{
		ArrayList<String> list = new ArrayList<>();
		for (String s : HeavyInventoriesConfig.ignoredMods)
		{
			list.add(s);
		}

		if (HeavyInventories.getItemMods().size() > 0)
		{
			for (String s : HeavyInventories.getItemMods())
			{
				if (!loadedMods.contains(s) && !list.contains(s))
				{
					s += ".cfg";
					loadedMods.add(s);
				}
			}
		}
		readIntoMemory();
	}

	/**
	 * Reads the files into the memory
	 */
	public static void readIntoMemory()
	{
		for (String s : loadedMods)
		{
			Configuration theConfig = new Configuration(new File(DIR + s));
			if (!configurations.contains(theConfig)) configurations.add(theConfig);
			if (!configs.containsKey(s)) configs.put(s, theConfig);
		}
		addItems();
	}

	public static void addItems()
	{
		for (String mod : loadedMods)
		{
			configs.get(mod).load();
		}
	}

	public static Configuration getConfig(String config)
	{
		return configs.containsKey(config) ? configs.get(config) : null;
	}

	public static void addConfig(String modid, Configuration configuration)
	{
		configs.put(modid, configuration);
		addToHandshake(modid.contains(".cfg") ? modid : modid + ".cfg");
		handshake();
	}

	public static ArrayList<String> getLoadedMods()
	{
		return loadedMods;
	}

}
