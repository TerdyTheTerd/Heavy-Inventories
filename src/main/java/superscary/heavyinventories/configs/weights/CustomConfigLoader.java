package superscary.heavyinventories.configs.weights;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import superscary.heavyinventories.configs.builder.ConfigBuilder;
import superscary.heavyinventories.configs.reader.ConfigReader;

import java.util.ArrayList;

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
		if (ConfigReader.getConfig(modid + ".cfg") != null)
		{
			return ConfigReader.getConfig(modid + ".cfg").get(Configuration.CATEGORY_GENERAL, item.getRegistryName().getResourcePath(), DEFAULT_WEIGHT).getDouble();
		}

		return DEFAULT_WEIGHT;
	}

	public static double getItemWeight(String modid, ItemStack stack)
	{
		return getItemWeight(modid, stack.getItem());
	}

}
