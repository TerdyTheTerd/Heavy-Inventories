package superscary.heavyinventories.weight;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.tiviacz.travellersbackpack.items.ItemTravellersBackpack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.CapabilityItemHandler;
import superscary.heavyinventories.compat.mods.travellersbackpack.HITravellersBackpack;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.PlayerHelper;
import superscary.heavyinventories.util.Toolkit;

public class WeightCalculator
{

	/**
	 * Calculates the weight of the player's entire inventory
	 * @param player the player to weigh
	 * @return the weight of the player
	 */
	public static double calculateWeight(PlayerHelper player)
	{
		double weight = 0;
		for (int i = 0; i < player.getInventory().getSizeInventory(); i++)
		{
			ItemStack stack = player.getInventory().getStackInSlot(i);
			if (stack != null)
			{
				weight += (getWeight(Toolkit.getModName(stack.getItem()), stack.getItem()) * stack.getCount());
				if (HeavyInventoriesConfig.addItemInventoryWeight)
				{
					if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
					{
						weight += ItemInventoryWeight.getWeight(stack);
					}
					else if (HITravellersBackpack.isLoaded() && stack.getItem() instanceof ItemTravellersBackpack)
					{
						weight += ItemInventoryWeight.getWeight(stack);
					}
				}
			}
		}

		if (Loader.isModLoaded("baubles")) weight += calculateWeightForBaublesInventory(player);
		return weight;
	}

	/**
	 * Only used for calculating the weight for Baubles slots (0-7)
	 * @param player
	 */
	public static double calculateWeightForBaublesInventory(PlayerHelper player)
	{
		double weight = 0;
		IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player.getPlayer());
		for (int i = 0; i < 7; i++)
		{
			ItemStack stack = handler.getStackInSlot(i);
			if (handler.getStackInSlot(i) != null)
			{
				weight += (getWeight(stack) * stack.getCount());
			}
		}
		return Toolkit.roundDouble(weight);
	}

	/**
	 * Gets the weight for custom items
	 * @param modid the modid of the item
	 * @param item the item to be weighed
	 * @return
	 */
	public static double getWeight(String modid, Item item)
	{
		return CustomLoader.getItemWeight(modid, item, JsonUtils.Type.WEIGHT);
	}

	/**
	 * Gets the weight for custom itemstacks
	 * @param modid
	 * @param stack
	 * @return
	 */
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

	/**
	 * Gets the weight for custom items (auto-fetches the modid)
	 * @param item
	 * @return
	 */
	public static double getWeight(Item item)
	{
		return getWeight(Toolkit.getModName(item), item);
	}

	/**
	 * Gets the weight for custom itemstacks (auto-fetches the modid)
	 * @param stack
	 * @return
	 */
	public static double getWeight(ItemStack stack)
	{
		return getWeight(Toolkit.getModName(stack), stack);
	}

	/**
	 * Gets the weight for custom blocks (auto-fetches the modid)
	 * @param block
	 * @return
	 */
	public static double getWeight(Block block)
	{
		return getWeight(Toolkit.getModName(block), block);
	}

}
