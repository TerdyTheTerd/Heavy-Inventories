package superscary.heavyinventories.weight;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.Toolkit;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;

public class CustomLoader
{

	private static final double DEFAULT_WEIGHT = HeavyInventoriesConfig.DEFAULT_WEIGHT;

	/**
	 * stores item weights that have been picked up. reduces lag.
	 */
	public static HashMap<Item, Double> loadedWeights = new HashMap<>();
	public static HashMap<Item, Double> loadedOffsets = new HashMap<>();

	/**
	 * Returns the weight of the item from the custom configs
	 * @param modid
	 * @param item
	 * @return
	 */
	public static double getItemWeight(String modid, Item item, JsonUtils.Type type)
	{
		if (type == JsonUtils.Type.WEIGHT)
		{
			if (loadedWeights.containsKey(item)) return loadedWeights.get(item);
			else
			{
				File jsonFile = new File(HeavyInventories.getWeightFileDirectory(), modid + ".json");
				if (jsonFile.isFile() && jsonFile.exists())
				{
					loadedWeights.put(item, JsonUtils.readJson(jsonFile, item, type));
					return loadedWeights.get(item);
				}
			}
		}
		else if (type == JsonUtils.Type.OFFSET)
		{
			if (loadedOffsets.containsKey(item)) return loadedOffsets.get(item);
			else
			{
				File jsonFile = new File(HeavyInventories.getWeightFileDirectory(), modid + ".json");
				if (jsonFile.isFile() && jsonFile.exists())
				{
					loadedOffsets.put(item, JsonUtils.readJson(jsonFile, item, type));
					return loadedOffsets.get(item);
				}
			}
		}

		/*if (ConfigReader.getConfig(modid + ".cfg") != null)
		{
			return ConfigReader.getConfig(modid + ".cfg").get(Configuration.CATEGORY_GENERAL, item.getRegistryName().getResourcePath(), DEFAULT_WEIGHT).getDouble();
		}*/

		return DEFAULT_WEIGHT;
	}

	public static double getItemWeight(String modid, ItemStack stack, @Nonnull JsonUtils.Type type)
	{
		return getItemWeight(modid, stack.getItem(), type);
	}

	public static double getItemWeight(ItemStack stack, @Nonnull JsonUtils.Type type)
	{
		return getItemWeight(Toolkit.getModName(stack), stack, type);
	}

	public static void reloadAll()
	{
		loadedOffsets.clear();
		loadedWeights.clear();
	}

}
