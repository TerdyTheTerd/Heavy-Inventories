package superscary.heavyinventories.configs.weights;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.Toolkit;

import java.io.File;

public class CustomConfigLoader
{

	private static final double DEFAULT_WEIGHT = 0.1;

	/**
	 * Returns the weight of the item from the custom configs
	 * @param modid
	 * @param item
	 * @return
	 */
	public static double getItemWeight(String modid, Item item)
	{
		File jsonFile = new File(HeavyInventories.getWeightFileDirectory(), modid + ".json");
		if (jsonFile.isFile() && jsonFile.exists())
		{
			return JsonUtils.readJson(jsonFile, item);
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
