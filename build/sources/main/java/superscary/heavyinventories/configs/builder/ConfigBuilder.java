package superscary.heavyinventories.configs.builder;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.configs.reader.ConfigReader;
import superscary.heavyinventories.util.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigBuilder
{

    private static String mod;

    public static Configuration config;
    public static final String GENERAL = Configuration.CATEGORY_GENERAL;

    private static File file;

    private static ArrayList<String> ignored = new ArrayList<>();

    public static void setFile(File theFile)
    {
        file = theFile;
    }

    private static void buildList()
    {
        String[] i = {"FML", "forge", "heavyinventories", "mcp", "supercore"};
        for (String s : i)
        {
            ignored.add(s);
        }
    }

    /**
     * Builds all files containing weights
     * @param modid the name of the file
     */
    public static void build(String modid)
    {
        ArrayList<String> list = new ArrayList<>();
        for (String s : HeavyInventoriesConfig.ignoredMods)
        {
            list.add(s);
        }

        if (!list.contains(modid))
        {
            if (HeavyInventoriesConfig.autoGenerateWeightConfigFiles)
            {
                if (ignored.size() == 0) buildList();
                mod = modid;
                modid += ".cfg";

                File folder = new File(file + "/Heavy Inventories/Weights/");
                File[] listOfFiles = folder.listFiles();

                File configFile = new File(file + "/Heavy Inventories/Weights/", modid);
                config = new Configuration(configFile);

                if (config != null && ignored.stream().noneMatch(mod::equalsIgnoreCase))
                {
                    for (int i = 0; i < listOfFiles.length; i++)
                    {
                        if (!listOfFiles[i].isFile())
                        {
                            ignored.add(mod);
                        }
                    }
                }
                loadConfig();
            }
        }

    }

    public static void loadConfig()
    {
        ArrayList<String> list = new ArrayList<>();
        for (String s : HeavyInventoriesConfig.ignoredMods)
        {
            list.add(s);
        }

        if (!list.contains(config.getConfigFile().getName()))
        {
            config.load();

            if (!ignored.contains(config.getConfigFile().getName()))
            {
                List<Item> itemList = ImmutableList.copyOf(Item.REGISTRY);
                for (Item item : itemList)
                {
                    if (item.getRegistryName().getResourceDomain().equalsIgnoreCase(mod))
                    {
                        Logger.log(Level.INFO, "Building weight @%s in config %s", item.getRegistryName().toString(), mod);
                        config.getFloat(item.getRegistryName().getResourcePath(), GENERAL, (float) HeavyInventoriesConfig.DEFAULT_WEIGHT, 0, 1000,
                                "Sets the carry weight of item " + item.getRegistryName());
                    }
                }

                List<Block> blockList = ImmutableList.copyOf(Block.REGISTRY);
                for (Block block : blockList)
                {
                    if (block.getRegistryName().getResourceDomain().equalsIgnoreCase(mod))
                    {
                        Logger.log(Level.INFO, "Building weight @%s in config %s", block.getRegistryName().toString(), mod);
                        config.getFloat(block.getRegistryName().getResourcePath(), GENERAL,
                                (float) HeavyInventoriesConfig.DEFAULT_WEIGHT, 0, 1000, "Sets the carry weight of block " + block.getRegistryName());
                    }
                }
            }

            config.save();
            addToConfig();
        }

    }

    private static void checkBuiltFiles(ArrayList<String> list, File directory)
    {
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            for (int j = 0; j < list.size(); j++)
            {
                if (files[i].getName().equalsIgnoreCase(list.get(j)))
                {
                    files[i].delete();
                }
            }
        }
    }

    private static void addToConfig()
    {
        ConfigReader.addConfig(mod + ".cfg", config);
    }

}
