package superscary.heavyinventories.common.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import superscary.heavyinventories.common.capability.offsets.OffsetProvider;
import superscary.heavyinventories.common.capability.weight.WeightProvider;

import static superscary.heavyinventories.util.Constants.MODID;

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
public class CapabilityHandler
{

	public static final ResourceLocation WEIGHABLE_CAPABILITY = new ResourceLocation(MODID, "weight");
	public static final ResourceLocation OFFSET_CAPABILITY = new ResourceLocation(MODID, "offset");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof EntityLivingBase)
		{
			event.addCapability(WEIGHABLE_CAPABILITY, new WeightProvider());
			event.addCapability(OFFSET_CAPABILITY, new OffsetProvider());
		}
	}

}
