package superscary.heavyinventories.compat.mods.ironbackpacks;

import net.minecraftforge.common.config.Configuration;
import superscary.heavyinventories.HeavyInventories;

import java.io.File;

/**
 * Copyright (c) 2017 by SuperScary(ERBF) http://codesynced.com
 * <p>
 * All rights reserved. No part of this software may be reproduced,
 * distributed, or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods,
 * without the prior written permission of the publisher, except in
 * the case of brief quotations embodied in critical reviews and
 * certain other noncommercial uses permitted by copyright law.
 */
public class HIIronBackpacks
{

	private static final File CONFIG_DIR = new File(HeavyInventories.getReaderDirectory() + "/Heavy Inventories/Mod Configs", "Iron Backpacks.cfg");
	public static Configuration IR_CONFIG = new Configuration(CONFIG_DIR);

	public HIIronBackpacks()
	{
		registerConfig();
	}

	public static void registerConfig()
	{
		if (IR_CONFIG == null)
		{
			IR_CONFIG = new Configuration(CONFIG_DIR);
		}
		loadConfig();
	}

	public static void loadConfig()
	{

	}

}
