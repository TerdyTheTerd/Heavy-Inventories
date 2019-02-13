package superscary.heavyinventories;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
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
import superscary.heavyinventories.configs.builder.ConfigBuilder;
import superscary.heavyinventories.configs.reader.ConfigReader;
import superscary.heavyinventories.configs.weights.MinecraftConfig;
import superscary.heavyinventories.util.Constants;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;
import superscary.supercore.SuperCore;
import superscary.supercore.info.Generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static superscary.heavyinventories.util.Constants.*;

/**
 * Copyright (c) 2018 by SuperScary(ERBF)
 * <p>
 * All rights reserved. No part of this software may be reproduced,
 * distributed, or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods,
 * without the prior written permission of the publisher, except in
 * the case of brief quotations embodied in critical reviews and
 * certain other noncommercial uses permitted by copyright law.
 */

@SuppressWarnings("unused")
@Mod(modid = MODID, version = VERSION, name = NAME, guiFactory = "superscary.heavyinventories.client.gui.ModGuiFactory", dependencies = SuperCore.SET_REQUIRED_AFTER)
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
        MinecraftConfig.init(event.getModConfigurationDirectory());
        ConfigBuilder.setFile(event.getModConfigurationDirectory());
        HeavyInventoriesConfig.init(event.getModConfigurationDirectory());

        readerDirectory = event.getModConfigurationDirectory();

        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "superscary.heavyinventories.compat.mods.theoneprobe.HITheOneProbe");

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

        for (String s : itemMods)
        {
            Logger.log(Level.INFO, "Building %s", s);
            ConfigBuilder.build(s);
        }

        CompatLoader.build();

        proxy.postInit();
    }

    private void findItemMods()
    {
        for (Object item : Item.REGISTRY)
        {
            Item i = (Item) item;
            if (!itemMods.contains(Toolkit.getModNameFromItem(i)))
            {
                itemMods.add(i.getRegistryName().getResourceDomain());
            }
        }

        for (Object block : Block.REGISTRY)
        {
            Block b = (Block) block;
            if (!itemMods.contains(Toolkit.getModNameFromBlock(b)))
            {
                itemMods.add(b.getRegistryName().getResourceDomain());
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

    public static ArrayList<String> getItemMods()
    {
        return itemMods;
    }

}
