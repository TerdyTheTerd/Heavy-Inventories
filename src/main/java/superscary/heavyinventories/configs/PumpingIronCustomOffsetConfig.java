package superscary.heavyinventories.configs;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class PumpingIronCustomOffsetConfig
{

    public static Configuration configuration;

    public static final String GENERAL = Configuration.CATEGORY_GENERAL;

    public static void init(File file)
    {
        File config = new File(file + "/Heavy Inventories/", "Pumping Iron Offsets.cfg");
        if (configuration == null)
        {
            configuration = new Configuration(config);
        }

        loadConfig();
    }

    public static void loadConfig()
    {
        configuration.load();
        configuration.save();
    }

    public static boolean hasItem(Item item)
    {
        String dir = getSuit(item);

        float f = configuration.getFloat(dir, GENERAL, HeavyInventoriesConfig.pumpingIronWeightIncrease, 0.1f, Float.MAX_VALUE, "Default weight offset for " + dir);

        if (f == 0.0f) return false;

        return true;
    }

    public static float getOffsetFor(Item item)
    {
        String dir = getSuit(item);
        float f = configuration.getFloat(dir, GENERAL, HeavyInventoriesConfig.pumpingIronWeightIncrease, 0.1f, Float.MAX_VALUE, "Default weight offset for " + dir);
        return f;
    }

    public static float getOffsetFor(ItemStack stack)
    {
        return getOffsetFor(stack.getItem());
    }

    public static float getOffsetFor(Block block)
    {
        return getOffsetFor(Item.getItemFromBlock(block));
    }

    public static Configuration getConfiguration()
    {
        return configuration;
    }

    public static String getSuit(Item item)
    {
        return item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath();
    }

}
