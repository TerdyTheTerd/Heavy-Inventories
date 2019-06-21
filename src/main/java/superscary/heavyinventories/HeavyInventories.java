package superscary.heavyinventories;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import superscary.heavyinventories.client.command.HeavyInventoriesCommandRegistry;
import superscary.heavyinventories.common.CommonProxy;
import superscary.heavyinventories.compat.CompatLoader;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.configs.PumpingIronCustomOffsetConfig;
import superscary.heavyinventories.configs.builder.ConfigBuilder;
import superscary.heavyinventories.util.Constants;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;
import superscary.supercore.SuperCore;
import superscary.supercore.info.Generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static superscary.heavyinventories.util.Constants.*;

@SuppressWarnings("unused")
@Mod(modid = MODID, version = VERSION, name = NAME, guiFactory = "superscary.heavyinventories.client.gui.ModGuiFactory", dependencies = SuperCore.SET_REQUIRED_AFTER, updateJSON = "https://raw.githubusercontent.com/SuperScary/Heavy-Inventories/master/update.json")
public class HeavyInventories
{

    private static File readerDirectory;

    private static ArrayList<String> itemMods = new ArrayList<>();

    @SidedProxy(serverSide = PROXY_SERVER, clientSide = PROXY_CLIENT)
    public static CommonProxy proxy;

    @Mod.Instance
    public static HeavyInventories instance;

    public static SimpleNetworkWrapper networkWrapper;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Logger.setLogger(event.getModLog());

        Logger.info("PreInit...");

        Generator.Info.create(Constants.class, event);
        HeavyInventoriesConfig.init(event.getModConfigurationDirectory());
        PumpingIronCustomOffsetConfig.init(event.getModConfigurationDirectory());

        readerDirectory = event.getModConfigurationDirectory();

        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "superscary.heavyinventories.compat.mods.theoneprobe.HITheOneProbe");
        FMLInterModComms.sendMessage("Waila", "register", "superscary.heavyinventories.compat.mods.waila.HIWaila");

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Logger.info("Init...");

        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        Logger.info("PostInit...");

        findItemMods();

        if (HeavyInventoriesConfig.autoGenerateWeightConfigFiles)
        {
            Logger.log(Level.INFO, "Auto Weight Generation is enabled!");
            ProgressManager.ProgressBar heavyBar = ProgressManager.push("Loading", itemMods.size());

            for (String s : itemMods)
            {
                ConfigBuilder.buildJson(s);
                heavyBar.step("Building: " + s + ".json");
            }

            ProgressManager.pop(heavyBar);
        }

        CompatLoader.build();

        proxy.postInit();
    }

    private void findItemMods()
    {
        for (Object item : Item.REGISTRY)
        {
            Item i = (Item) item;
            if (!itemMods.contains(Toolkit.getModName(i)))
            {
                itemMods.add(Toolkit.getModName(i));
            }
        }

        for (Object block : Block.REGISTRY)
        {
            Block b = (Block) block;
            if (!itemMods.contains(Toolkit.getModName(b)))
            {
                itemMods.add(Toolkit.getModName(b));
            }
        }

        Collections.sort(itemMods);
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        new HeavyInventoriesCommandRegistry(event);
    }

    public static SimpleNetworkWrapper getNetwork()
    {
        return networkWrapper;
    }

    public static File getReaderDirectory()
    {
        return readerDirectory;
    }

    public static File getWeightFileDirectory()
    {
        return new File(getReaderDirectory() + File.separator + "Heavy Inventories" + File.separator + "Weights");
    }

    public static ArrayList<String> getItemMods()
    {
        return itemMods;
    }

}
