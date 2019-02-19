package superscary.heavyinventories.compat.mods.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import superscary.heavyinventories.calc.PlayerWeightCalculator;
import superscary.heavyinventories.common.capability.offsets.IOffset;
import superscary.heavyinventories.common.capability.offsets.OffsetProvider;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.Toolkit;

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
public class HITOPInfoProvider implements IProbeInfoProvider
{

	@Override
	public String getID()
	{
		return MODID + "Weight";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state, IProbeHitData data)
	{
		Block block = state.getBlock();
		double blockWeight = Toolkit.roundDouble(getWeightOfBlock(block), 1);
		info.horizontal().text(getColorText(player, blockWeight) + " stone", info.defaultTextStyle());
	}

	/**
	 * Returns the text with the color in respect to the players weight ({@link WeightProvider})
	 * @param player the player that is looking
	 * @param blockWeight the block the player is looking at
	 * @return
	 */
	public String getColorText(EntityPlayer player, double blockWeight)
	{
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		IOffset offset = player.getCapability(OffsetProvider.OFFSET_CAPABILITY, null);
		double maxPlayerWeight = weighable.getMaxWeight() + offset.getOffset();

		/*
			checks if player is in creative mode
		 */
		if (player.isCreative() && HeavyInventoriesConfig.allowInCreative)
		{
			return TextFormatting.ITALIC + "" + blockWeight;
		}

		/*
			if the player weight + block weight is less than the max allowed weight
		 */
		if (weighable.getWeight() + blockWeight < maxPlayerWeight)
		{
			return "" + blockWeight;
		}
		/*
			if the player weight + block weight is more than the max or equal to the max allowed weight
		 */
		else if (weighable.getWeight() + blockWeight >= maxPlayerWeight)
		{
			return TextFormatting.RED + "" + blockWeight;
		}

		return null;

	}

	/**
	 * Gets the weight of the block
	 * @param block
	 * @return
	 */
	private double getWeightOfBlock(Block block)
	{
		String modid = Toolkit.getModNameFromBlock(block);
		if (modid.equalsIgnoreCase("minecraft"))
		{
			return PlayerWeightCalculator.getWeight(new ItemStack(block));
		}
		else
		{
			return PlayerWeightCalculator.getWeight(modid, block);
		}
	}

}
