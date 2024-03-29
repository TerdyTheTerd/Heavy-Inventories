package superscary.heavyinventories.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.weight.CustomLoader;

import java.util.ArrayList;

public class Toolkit
{

	/**
	 * the default location that the config is being read at
	 */
	public static final String SETUP = HeavyInventoriesConfig.SETUP;

	/**
	 * the key used to get the max weight of a player (used in nbt)
	 */
	public static final String DATA_MAXWEIGHT = "maxWeight";

	/**
	 * saves data to a player
	 * @param player
	 * @param dataName
	 * @param data
	 */
	@Deprecated
	public static void saveDataToPlayer(EntityPlayer player, String dataName, float data)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setFloat(dataName, data);
		player.writeToNBT(compound);
	}

	/**
	 * gets data from a player
	 * @param player
	 * @param dataName
	 * @return
	 */
	@Deprecated
	public static float getDataFromPlayer(EntityPlayer player, String dataName)
	{
		NBTTagCompound compound = player.getEntityData();
		return compound.getFloat(dataName);
	}

	/**
	 * Get the modid of an item, itemstack, or block
	 * @param object
	 * @return
	 */
	public static String getModName(Object object)
	{
		if (object instanceof Block) return getModNameFromBlock((Block) object);
		if (object instanceof Item) return getModNameFromItem((Item) object);
		if (object instanceof ItemStack) return getModNameFromItem((ItemStack) object);
		return null;
	}

	/**
	 * Gets the modid in which a Block exists
	 * @param block the block to be checked
	 * @return
	 */
	private static String getModNameFromBlock(Block block)
	{
		return block.getRegistryName().getResourceDomain();
	}

	/**
	 * Gets the modid in which an Item exists
	 * @param item the item to be checked
	 * @return
	 */
	private static String getModNameFromItem(Item item)
	{
		return item.getRegistryName().getResourceDomain();
	}

	/**
	 * Gets the modid from an ItemStack
	 * @param stack
	 * @return
	 */
	private static String getModNameFromItem(ItemStack stack)
	{
		return getModNameFromItem(stack.getItem());
	}

	/**
	 * Gets the translation from a translation key
	 * @param par1Str
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public static String translate(String par1Str)
	{
		return net.minecraft.client.resources.I18n.format(par1Str);
	}

	public static String getString(String name, String category, String defaultValue, String comment)
	{
		return HeavyInventoriesConfig.getConfig().getString(name, category, defaultValue, comment);
	}

	public static String getString(String name, String category, String defaultValue)
	{
		return getString(name, category, defaultValue, "Default comment for value " + name);
	}

	public static float getFloat(String name, String category, float defaultValue, float minValue, float maxValue)
	{
		return HeavyInventoriesConfig.getConfig().getFloat(name, category, defaultValue, minValue, maxValue, name);
	}

	public static float getFloat(String name, float defaultValue, float minValue, float maxValue)
	{
		return getFloat(name, SETUP, defaultValue, minValue, maxValue);
	}

	public static float getFloat(Configuration configuration, String name, String category, float defaultValue, float minValue, float maxValue)
	{
		return configuration.getFloat(name, category, defaultValue, minValue, maxValue, name);
	}

	public static float getFloat(Configuration configuration, String name, float defaultValue, float minValue, float maxValue)
	{
		return getFloat(configuration, name, SETUP, defaultValue, minValue, maxValue);
	}

	public static boolean getBoolean(String name, String category, boolean defaultValue)
	{
		return HeavyInventoriesConfig.getConfig().getBoolean(name, category, defaultValue, name);
	}

	public static boolean getBoolean(String name, boolean defaultValue)
	{
		return getBoolean(name, SETUP, defaultValue);
	}

	public static void increasePlayerMaxWeight(EntityPlayer player, double value)
	{
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);

		player.getEntityData().setDouble("HIWeight", weighable.getMaxWeight() + value);
	}

	public static void decreasePlayerMaxWeight(EntityPlayer player, double value)
	{
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);

		player.getEntityData().setDouble("HIWeight", weighable.getMaxWeight() - value >= 0 ? weighable.getMaxWeight() - value : 0);
	}

	public static void setPlayerMaxCarryWeight(EntityPlayer player, double value)
	{
		player.getEntityData().setDouble("HIWeight", value);
	}

	/**
	 * Plays a sound at the player (reference: http://www.minecraftforum.net/forums/mapping-and-modding-java-edition/mapping-and-modding-tutorials/2567682-all-playsound-names-1-9-1-11-updated)
	 * @param player
	 * @param soundID
	 */
	public static void playSoundAtPlayer(EntityPlayer player, int soundID, float volume, float pitch)
	{
		player.playSound(SoundEvent.REGISTRY.getObjectById(soundID), volume, pitch);
	}

	/**
	 * Rounds a double to a defined number of places
	 * @param value the double to be rounded
	 * @param places the number of digits allowed after the decimal
	 * @return
	 */
	public static double roundDouble(double value, int places)
	{
		if (places == 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value *= factor;
		long temp = Math.round(value);
		return (double) temp / factor;
	}

	/**
	 * Rounds a double to the nearest tenth of a place
	 * @param value the double to be rounded
	 * @return
	 */
	public static double roundDouble(double value)
	{
		return roundDouble(value, 1);
	}

	public static int placesAfterDecimal(double value)
	{
		String string = "" + value;
		if (string.contains("."))
		{
			return string.split(".")[1].length();
		}

		return 1;
	}

	/**
	 * Sets the color of the text on screen based on the players weight
	 * @param player
	 * @return
	 */
	public static int getWeightColor(EntityPlayer player)
	{
		PlayerHelper helper = new PlayerHelper(player);
		if (helper.isOverEncumbered())
		{
			return Integer.parseInt("FF0000", 16);
		}
		else if (helper.isEncumbered())
		{
			return Integer.parseInt("FFFF00", 16);
		}
		else
		{
			return Integer.parseInt("FFFFFF", 16);
		}
	}

	/**
	 * Gets the weight from an ItemStack
	 * @param stack
	 * @return
	 */
	public static double getWeightFromStack(ItemStack stack)
	{
		return CustomLoader.getItemWeight(stack, JsonUtils.Type.WEIGHT);
	}

	/**
	 * Gets the pumping iron offset from an ItemStack
	 * @param stack
	 * @return
	 */
	public static double getOffsetFromStack(ItemStack stack)
	{
		return CustomLoader.getItemWeight(stack, JsonUtils.Type.OFFSET);
	}

	/**
	 * Does the calculation between 2 points in a 3D environment
	 * @param coords1
	 * @param coords2
	 * @return
	 */
	public static Coords distanceHelper(Coords coords1, Coords coords2)
	{
		double distanceX = Math.pow(coords2.getX() - coords1.getX(), 2);
		double distanceY = Math.pow(coords2.getY() - coords1.getY(), 2);
		double distanceZ = Math.pow(coords2.getZ() - coords1.getZ(), 2);

		return new Coords(distanceX, distanceY, distanceZ);
	}

	/**
	 * Calculates the distance between 2 points in a 3D environment
	 * @param coords1
	 * @param coords2
	 * @return
	 */
	public static double getDistance(Coords coords1, Coords coords2)
	{
		Coords coords = distanceHelper(coords1, coords2);
		return Math.sqrt(coords.getX() + coords.getY() + coords.getZ());
	}

	/**
	 * Checks if there is a space at the first character in the custom string
	 * @param string
	 * @return
	 */
	public static String checkFormatOfRenderText(String string)
	{
		if (string.charAt(0) != ' ')
		{
			return " " + string;
		}
		return string;
	}

	/**
	 * Makes sure that an inputted string only contains numbers and decimals
	 * @param string
	 * @return
	 */
	public static boolean checkNumericalWeight(String string)
	{
		return string.matches("^[0-9.]*$");
	}

	/**
	 * Gets all items and blocks for a given mod
	 * @param modid
	 * @return
	 */
	public static ArrayList<Object> getAllItemsFromMod(String modid)
	{
		ArrayList<Object> objects = new ArrayList<>();

		for (Item item : Item.REGISTRY)
		{
			if (item.getRegistryName().getResourceDomain().equalsIgnoreCase(modid))
			{
				objects.add(item);
			}
		}

		for (Block block : Block.REGISTRY)
		{
			if (block.getRegistryName().getResourceDomain().equalsIgnoreCase(modid))
			{
				objects.add(block);
			}
		}

		return objects;
	}

}
