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
import superscary.heavyinventories.util.Toolkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigBuilder
{

    private static String mod;

    public static Configuration config;
    public static final String GENERAL = Configuration.CATEGORY_GENERAL;

    /**
     * game directory
     */
    private static File file;

    private static ArrayList<String> ignored = new ArrayList<>();

    /**
     * Sets the games directory
     * @param theFile
     */
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

        File folder = new File(file + "/Heavy Inventories/Weights/");
        File[] listOfFiles = folder.listFiles();

        if (!list.contains(modid))
        {

            if (HeavyInventoriesConfig.autoGenerateWeightConfigFiles)
            {
                if (ignored.size() == 0) buildList();
                mod = modid;
                modid += ".cfg";

                File configFile = new File(file + "/Heavy Inventories/Weights/", modid);
                config = new Configuration(configFile);

                doCheck(listOfFiles);
                loadConfig(true);
            }
        }

    }

    /**
     * TODO: Causes a memory leak somehow
     */
    public static void existing()
    {
        ArrayList<String> list = new ArrayList<>();
        for (String s : HeavyInventoriesConfig.ignoredMods)
        {
            list.add(s);
        }

        if (ignored.size() == 0) buildList();
        File folder = new File(file + "/Heavy Inventories/Weights/");
        File[] files = folder.listFiles();

        for (File file : files)
        {
            if (file.getAbsoluteFile().toString().contains(".cfg"))
            {
                config = new Configuration(new File(folder, file.getAbsoluteFile().getName()));
            }
            else config = new Configuration(new File(file + ".cfg"));
            loadConfig(false);
        }

    }

    /**
     * Loads in the existing files is auto weight generation is disabled
     * @param modid
     * @param listOfFiles
     * @param folder
     */
    public static void buildExisting(String modid, File[] listOfFiles, File folder)
    {
        for (File file : listOfFiles)
        {
            if (ignored.size() == 0) buildList();
            mod = modid;
            //modid += ".cfg";

            config = new Configuration(new File(folder, modid));
            doCheck(listOfFiles);
            loadConfig(false);
        }

    }

    public static void doCheck(File[] listOfFiles)
    {
        if (config != null && ignored.stream().noneMatch(mod::equalsIgnoreCase))
        {
            for (int i = 0; i < listOfFiles.length; i++)
            {
                if (!listOfFiles[i].isFile()) ignored.add(mod);
            }
        }
    }

    /**
     * Loads the config
     * @param bool true if loggers shows building
     */
    public static void loadConfig(boolean bool)
    {
        String log;
        if (bool)
        {
            log = "Building weight @%s in config %s";
        }
        else
        {
            log = "Loading weight @%s in config %s";
        }

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
                        Logger.log(Level.INFO, log, item.getRegistryName().toString(), mod);
                        config.getFloat(item.getRegistryName().getResourcePath(), GENERAL, (float) HeavyInventoriesConfig.DEFAULT_WEIGHT, 0, 1000,
                                "Sets the carry weight of item " + item.getRegistryName());
                    }
                }

                List<Block> blockList = ImmutableList.copyOf(Block.REGISTRY);
                for (Block block : blockList)
                {
                    if (block.getRegistryName().getResourceDomain().equalsIgnoreCase(mod))
                    {
                        Logger.log(Level.INFO, log, block.getRegistryName().toString(), mod);
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
