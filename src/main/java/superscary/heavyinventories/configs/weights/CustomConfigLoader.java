package superscary.heavyinventories.configs.weights;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.Toolkit;

import java.io.File;
import java.util.HashMap;

public class CustomConfigLoader
{

	private static final double DEFAULT_WEIGHT = 0.1;

	/**
	 * stores item weights that have been picked up. reduces lag.
	 */
	public static HashMap<Item, Double> loadedWeights = new HashMap<>();

	/**
	 * Returns the weight of the item from the custom configs
	 * @param modid
	 * @param item
	 * @return
	 */
	public static double getItemWeight(String modid, Item item)
	{
		if (loadedWeights.containsKey(item)) return loadedWeights.get(item);
		else
		{
			File jsonFile = new File(HeavyInventories.getWeightFileDirectory(), modid + ".json");
			if (jsonFile.isFile() && jsonFile.exists())
			{
				loadedWeights.put(item, JsonUtils.readJson(jsonFile, item));
				return loadedWeights.get(item);
			}
		}

		/*if (ConfigReader.getConfig(modid + ".cfg") != null)
		{
			return ConfigReader.getConfig(modid + ".cfg").get(Configuration.CATEGORY_GENERAL, item.getRegistryName().getResourcePath(), DEFAULT_WEIGHT).getDouble();
		}*/

		return DEFAULT_WEIGHT;
	}

	public static double getItemWeight(String modid, ItemStack stack)
	{
		return getItemWeight(modid, stack.getItem());
	}

	public static double getItemWeight(ItemStack stack)
	{
		return getItemWeight(Toolkit.getModName(stack), stack);
	}

}
