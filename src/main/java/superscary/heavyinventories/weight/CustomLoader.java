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
	public static HashMap<Item, Double> loadedWeightsCache = new HashMap<>();

	/**
	 * stores item offsets. reduces lag.
	 */
	public static HashMap<Item, Double> loadedOffsetsCache = new HashMap<>();

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
			if (loadedWeightsCache.containsKey(item)) return loadedWeightsCache.get(item);
			else
			{
				File jsonFile = new File(HeavyInventories.getWeightFileDirectory(), modid + ".json");
				if (jsonFile.isFile() && jsonFile.exists())
				{
					loadedWeightsCache.put(item, JsonUtils.readJson(jsonFile, item, type));
					return loadedWeightsCache.get(item);
				}
			}
		}
		else if (type == JsonUtils.Type.OFFSET)
		{
			if (loadedOffsetsCache.containsKey(item)) return loadedOffsetsCache.get(item);
			else
			{
				File jsonFile = new File(HeavyInventories.getWeightFileDirectory(), modid + ".json");
				if (jsonFile.isFile() && jsonFile.exists())
				{
					loadedOffsetsCache.put(item, JsonUtils.readJson(jsonFile, item, type));
					return loadedOffsetsCache.get(item);
				}
			}
		}

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

	/**
	 * Reloads the cache of weights and offsets
	 */
	public static void reloadAll()
	{
		loadedOffsetsCache.clear();
		loadedWeightsCache.clear();
	}

}
