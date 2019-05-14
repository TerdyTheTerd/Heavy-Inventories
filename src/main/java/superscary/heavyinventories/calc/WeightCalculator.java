package superscary.heavyinventories.calc;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import superscary.heavyinventories.configs.weights.CustomConfigLoader;
import superscary.heavyinventories.util.Toolkit;

public class WeightCalculator
{

	/**
	 * Calculates the weight of the player's entire inventory
	 * @param player the player to weigh
	 * @return the weight of the player
	 */
	public static double calculateWeight(EntityPlayer player)
	{
		double weight = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null)
			{
				weight += (getWeight(Toolkit.getModNameFromItem(stack.getItem()), stack.getItem()) * stack.getCount());
			}
		}

		if (Loader.isModLoaded("baubles")) weight += calculateWeightForBaublesInventory(player);
		return weight;
	}

	/**
	 * Only used for calculating the weight for Baubles slots (0-7)
	 * @param player
	 */
	public static double calculateWeightForBaublesInventory(EntityPlayer player)
	{
		double weight = 0;
		IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
		for (int i = 0; i < 7; i++)
		{
			ItemStack stack = handler.getStackInSlot(i);
			if (handler.getStackInSlot(i) != null)
			{
				weight += (getWeight(Toolkit.getModNameFromItem(stack.getItem()), stack.getItem()) * stack.getCount());
			}
		}
		return Toolkit.roundDouble(weight);
	}

	/**
	 * Gets the weight for {@link net.minecraft.client.Minecraft} items
	 * @param stack
	 * @return
	 */
	/*public static double getWeight(ItemStack stack)
	{
		return MinecraftConfig.getConfig().get(Configuration.CATEGORY_GENERAL, stack.getItem().getRegistryName().getResourcePath(), 0.5).getDouble();
	}*/

	/**
	 * Gets the weight for custom items
	 * @param modid the modid of the item
	 * @param item the item to be weighed
	 * @return
	 */
	public static double getWeight(String modid, Item item)
	{
		return CustomConfigLoader.getItemWeight(modid, item);
	}

	public static double getWeight(String modid, ItemStack stack)
	{
		return getWeight(modid, stack.getItem());
	}

	/**
	 * Gets the weight for custom blocks
	 * @param modid the modid of the item
	 * @param block the item to be weighed
	 * @return
	 */
	public static double getWeight(String modid, Block block)
	{
		return getWeight(modid, Item.getItemFromBlock(block));
	}

}
