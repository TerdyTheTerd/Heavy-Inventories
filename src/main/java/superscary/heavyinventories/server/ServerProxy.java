package superscary.heavyinventories.server;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import superscary.heavyinventories.client.event.ClientEventHandler;
import superscary.heavyinventories.client.event.PumpingIronHandler;
import superscary.heavyinventories.common.CommonProxy;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.configs.weights.MinecraftConfig;

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
public class ServerProxy extends CommonProxy
{

	@Override
	public void preInit()
	{
		super.preInit();
		//MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.register(new MinecraftConfig());
		MinecraftForge.EVENT_BUS.register(new HeavyInventoriesConfig());
		MinecraftForge.EVENT_BUS.register(new PumpingIronHandler());
	}

	@Override
	public void init()
	{

	}

	@Override
	public void postInit()
	{

	}

	@Mod.EventHandler
	private static void setupNetwork(FMLPreInitializationEvent event)
	{

	}

}
