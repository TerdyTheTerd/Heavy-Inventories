package superscary.heavyinventories.client.command;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.client.command.commands.HeavyInventoriesOpenConfigGui;

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
@SideOnly(Side.CLIENT)
public class HeavyInventoriesCommandRegistry
{

	private FMLServerStartingEvent event;

	public HeavyInventoriesCommandRegistry(FMLServerStartingEvent event)
	{
		this.event = event;
		registerAll();
	}

	private void registerAll()
	{
		register(new HeavyInventoriesOpenConfigGui());
	}

	private void register(ICommand command)
	{
		event.registerServerCommand(command);
	}

}
