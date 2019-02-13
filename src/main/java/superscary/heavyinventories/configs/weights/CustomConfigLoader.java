package superscary.heavyinventories.configs.weights;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import superscary.heavyinventories.configs.builder.ConfigBuilder;
import superscary.heavyinventories.configs.reader.ConfigReader;

import java.util.ArrayList;

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
public class CustomConfigLoader
{

	private static final double DEFAULT_WEIGHT = 0.1;
	/**
	 * Returns the weight of the item from the custom configs
	 * @param modid
	 * @param item
	 * @return
	 */
	public static double getItemWeight(String modid, Item item)
	{
		if (ConfigReader.getConfig(modid + ".cfg") != null)
		{
			return ConfigReader.getConfig(modid + ".cfg").get(Configuration.CATEGORY_GENERAL, item.getRegistryName().getResourcePath(), DEFAULT_WEIGHT).getDouble();
		}

		return DEFAULT_WEIGHT;
	}

}
