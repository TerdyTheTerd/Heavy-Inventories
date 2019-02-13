package superscary.heavyinventories.client.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.calc.PlayerWeightCalculator;
import superscary.heavyinventories.client.gui.InventoryWeightText;
import superscary.heavyinventories.client.gui.Toast;
import superscary.heavyinventories.client.gui.meter.GuiDrawMeter;
import superscary.heavyinventories.common.capability.offsets.IOffset;
import superscary.heavyinventories.common.capability.offsets.OffsetProvider;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.configs.reader.ConfigReader;
import superscary.heavyinventories.configs.weights.CustomConfigLoader;
import superscary.heavyinventories.util.EnumTagID;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;
import superscary.supercore.tools.EnumColor;

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

@SuppressWarnings("all")
public class ClientEventHandler
{

	/**
	 * For adding the weights to the items tooltip
	 * @param event
	 */
	@SubscribeEvent
	public void mouseOverTooltip(ItemTooltipEvent event)
	{
		if (HeavyInventoriesConfig.isEnabled)
		{
			ItemStack stack = event.getItemStack();
			if (stack != null)
			{
				if (stack.getItem().getRegistryName().toString().split(":")[0].equalsIgnoreCase("minecraft"))
				{
					double weight = PlayerWeightCalculator.getWeight(stack);
					event.getToolTip().add(form(weight));
					if (stack.getCount() > 1)
					{
						event.getToolTip().add(I18n.format("hi.gui.weight") + " " + (weight * stack.getCount()) + " Stone");
					}

					if (Minecraft.getMinecraft().currentScreen != null)
					{
						if (tooltipKeyCheck())
						{
							addShiftTip(event, stack, weight);
						}
						else
						{
							addNoShift(event);
						}
					}
				}
				/**
				 * Custom reader
				 */
				else if (ConfigReader.getLoadedMods().contains(Toolkit.getModNameFromItem(stack.getItem()) + ".cfg"))
				{
					String modid = Toolkit.getModNameFromItem(stack.getItem());

					double weight = CustomConfigLoader.getItemWeight(modid, stack.getItem());
					event.getToolTip().add(form(weight));
					if (stack.getCount() > 1)
					{
						event.getToolTip().add(I18n.format("hi.gui.weight") + " " + (weight * stack.getCount()) + " Stone");
					}

					if (Minecraft.getMinecraft().currentScreen != null)
					{
						if (tooltipKeyCheck())
						{
							addShiftTip(event, stack, weight);
						}
						else
						{
							addNoShift(event);
						}
					}
				}
				else
				{
					addShiftTip(event, stack, 0.1);
					if (tooltipKeyCheck())
					{
						addTextToTooltip(event, I18n.format("hi.defaultWeight"));
					}
					else
					{
						addNoShift(event);
					}
				}
			}
		}
	}

	/**
	 * Adds the tooltip to items when shift is being pressed
	 * @param event
	 * @param stack
	 * @param weight
	 */
	private void addShiftTip(ItemTooltipEvent event, ItemStack stack, double weight)
	{
		event.getToolTip().add(I18n.format("hi.gui.maxStackWeight", stack.getMaxStackSize()) + " " + (weight * stack.getMaxStackSize()) + " Stone");
	}

	/**
	 * Adds the tooltip to items when shift is not being pressed
	 * @param event
	 */
	private void addNoShift(ItemTooltipEvent event)
	{
		event.getToolTip().add(I18n.format("hi.gui.shift", EnumColor.YELLOW + "SHIFT" + EnumColor.GREY));
	}

	/**
	 * Adds text to an item tooltip (for convenience)
	 * @param event
	 * @param message
	 */
	private void addTextToTooltip(ItemTooltipEvent event, String message)
	{
		event.getToolTip().add(message);
	}

	/**
	 * Checks if either shift keys are pressed (for tooltips)
	 * @return
	 */
	private boolean tooltipKeyCheck()
	{
		return Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
	}

	/**
	 * Default text form for tooltips
	 * @param weight
	 * @return
	 */
	private String form(double weight)
	{
		return ChatFormatting.BOLD + "" + ChatFormatting.WHITE + "Weight: " + weight + " Stone";
	}

	private double playersCalculatedWeight = -1;

	/**
	 * Sends message if the player is encumbered
	 * @param event
	 */
	@SubscribeEvent
	public void handleEncumberance(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);

		if (weighable.getWeight() != playersCalculatedWeight)
		{
			playersCalculatedWeight = getPlayersCalculatedWeight(player);
			weighable.setWeight(playersCalculatedWeight);
		}

		if (weighable.getRelativeWeight() >= 1.0)
		{
			weighable.setOverEncumbered(true);
		}
		else if (weighable.getRelativeWeight() < 1.0 && weighable.getRelativeWeight() >= 0.85)
		{
			weighable.setEncumbered(true);
			weighable.setOverEncumbered(false);
		}
		else
		{
			weighable.setEncumbered(false);
		}
	}

	public double getPlayersCalculatedWeight(EntityPlayer player)
	{
		return PlayerWeightCalculator.calculateWeight(player);
	}

	/**
	 * Checks if the player is encumbered and fixes whether or not they can move (and changes their move speed)
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerMove(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		weighable.setWeight(PlayerWeightCalculator.calculateWeight(player));

		if (!player.isCreative())
		{
			reduceSpeed(weighable, player);
		}

		if (HeavyInventoriesConfig.allowInCreative == true)
		{
			reduceSpeed(weighable, player);
		}
	}

	/**
	 * Method for changing players move speed
	 * @param weighable
	 * @param player
	 */
	public void reduceSpeed(IWeighable weighable, EntityPlayer player)
	{
		if (weighable.isOverEncumbered())
		{
			player.capabilities.setPlayerWalkSpeed(HeavyInventoriesConfig.overEncumberedSpeed);
		}
		else if (weighable.isEncumbered())
		{
			player.capabilities.setPlayerWalkSpeed(HeavyInventoriesConfig.encumberedSpeed / 10);
		}
		else
		{
			player.capabilities.setPlayerWalkSpeed((float) 0.1);
		}
	}

	/**
	 * Allows or disallows the player to jump based on encumberance
	 * @param event
	 */
	@SubscribeEvent
	public void livingJump(LivingEvent.LivingJumpEvent event)
	{
		if (event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);

			if (!player.isCreative())
			{
				disableJumping(weighable, player);
				player.addExhaustion(HeavyInventoriesConfig.jumpExhaustion);
			}

			if (HeavyInventoriesConfig.allowInCreative && player.isCreative() && weighable.isOverEncumbered())
			{
				disableJumping(weighable, player);
			}
		}
	}

	/**
	 * Method to stop the player from jumping
	 * @param weighable
	 * @param player
	 */
	@SideOnly(Side.CLIENT)
	public void disableJumping(IWeighable weighable, EntityPlayer player)
	{
		if (weighable.isOverEncumbered())
		{
			if (player.isServerWorld()) new Toast(new TextComponentTranslation("hi.splash.noJump"));
			player.motionY = 0D;
		}
		else if (weighable.isEncumbered())
		{
			if (player.isServerWorld()) new Toast(new TextComponentTranslation("hi.splash.noJumpEncumbered"));
			player.motionY /= 5;
		}
	}

	/**
	 * Allows/disallows the player from sleeping (checks config)
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerSleep(PlayerSleepInBedEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		if (!HeavyInventoriesConfig.canSleepWhileOverEncumbered && weighable.isOverEncumbered())
		{
			event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
			//event.getEntityPlayer().sendMessage(new TextComponentTranslation("hi.splash.loseWeightMax"));
			new Toast(new TextComponentTranslation("hi.splash.loseWeightMax"));
		}
		else if (!HeavyInventoriesConfig.canSleepWhileEncumbered && weighable.isEncumbered())
		{
			event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
			//event.getEntityPlayer().sendMessage(new TextComponentTranslation("hi.splash.loseWeight"));
			new Toast(new TextComponentTranslation("hi.splash.loseWeight"));
		}
	}

	/**
	 * Transfers weight to a new player when they die (for mods that allow the player to keep their inventory
	 * on death)
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event)
	{
		EntityPlayer player = event.getEntityPlayer();
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		IWeighable weighableOld = event.getOriginal().getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		IOffset offset = player.getCapability(OffsetProvider.OFFSET_CAPABILITY, null);
		IOffset offsetOld = event.getOriginal().getCapability(OffsetProvider.OFFSET_CAPABILITY, null);

		weighable.setWeight(weighableOld.getWeight());
		offset.setOffset(offsetOld.getOffset());
	}

	/**
	 * Draws the on screen elements
	 * @param event
	 */
	@SubscribeEvent
	public void renderHUD(RenderGameOverlayEvent event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
		{
			if (HeavyInventoriesConfig.allowInCreative || !Minecraft.getMinecraft().player.isCreative())
			{
				InventoryWeightText.renderText(Minecraft.getMinecraft());
				Toast.renderText(Minecraft.getMinecraft());
			}
		}
		else if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
		{
			if (HeavyInventoriesConfig.showWeightBar)
			{
				new GuiDrawMeter(Minecraft.getMinecraft());
			}
		}
	}

	/**
	 * Modifies how well a player can attack if they are encumbered
	 * @param event
	 */
	@SubscribeEvent
	public void attackStamina(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);

		if (weighable.isOverEncumbered() && isPlayerMoving(player))
		{
			player.addExhaustion(HeavyInventoriesConfig.overEncumberedExhaustion);
		}
		else if (weighable.isEncumbered() && isPlayerMoving(player))
		{
			player.addExhaustion(HeavyInventoriesConfig.encumberedExhaustion);
		}
	}

	private static boolean sendRunMessage;

	/**
	 * Tick event to change if the player can run
	 * @param event
	 */
	@SubscribeEvent
	public void playerRun(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);

		if (player.isSprinting() && !player.isCreative() && (weighable.isOverEncumbered() || weighable.isEncumbered()))
		{
			player.setSprinting(canPlayerRun(player));

			if (!sendRunMessage && canPlayerRun(player))
			{
				//player.sendMessage(new TextComponentTranslation("hi.splash.noRun"));
				new Toast(new TextComponentTranslation("hi.splash.noRun"));
				sendRunMessage = true;
			}
		}
		else if (!player.isSprinting() && sendRunMessage)
		{
			sendRunMessage = false;
		}
	}

	/**
	 * Checks if player is moving
	 * @param player
	 * @return true if player is moving
	 */
	public static boolean isPlayerMoving(EntityPlayer player)
	{
		return player.moveForward != 0 || player.moveStrafing != 0 || player.moveVertical != 0;
	}

	/**
	 * Checks if the player can move based on weight
	 * @param player
	 * @return
	 */
	public static boolean canPlayerRun(EntityPlayer player)
	{
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		return !weighable.isEncumbered() || !weighable.isOverEncumbered();
	}

	/**
	 * Variable used to display and save/modify the players weight
	 */
	private static double playerWeight;

	/**
	 * Loads the players weight when logged in
	 * @param event
	 */
	@SubscribeEvent
	public void getPlayerWeightOffset(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event)
	{
		EntityPlayer player = event.player;
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		IOffset offset = player.getCapability(OffsetProvider.OFFSET_CAPABILITY, null);

		if (player.getEntityData().hasKey(EnumTagID.WEIGHT.getId()))
		{

			if (player.getEntityData().getDouble(EnumTagID.WEIGHT.getId()) != HeavyInventoriesConfig.maxCarryWeight)
			{
				weighable.setMaxWeight(HeavyInventoriesConfig.maxCarryWeight);
				player.getEntityData().setDouble(EnumTagID.WEIGHT.getId(), HeavyInventoriesConfig.maxCarryWeight);
			}

			Logger.info("Loading key: %s", EnumTagID.WEIGHT.getId());
			weighable.setMaxWeight(player.getEntityData().getDouble(EnumTagID.WEIGHT.getId()));

			Logger.info("Player %s weight = %s", player.getDisplayNameString(), weighable.getMaxWeight());
		}
		else
		{
			player.getEntityData().setDouble(EnumTagID.WEIGHT.getId(), HeavyInventoriesConfig.maxCarryWeight);
			weighable.setMaxWeight(HeavyInventoriesConfig.maxCarryWeight);
		}
		playerWeight = weighable.getMaxWeight();
	}

	/**
	 * Getter for the player's weight
	 * @return
	 */
	public static double getPlayerWeight()
	{
		return playerWeight;
	}

	public static double addPlayerWeight(double add)
	{
		return playerWeight += add;
	}

	public static void setPlayerWeight(double set)
	{
		playerWeight = set;
	}

	/**
	 * Saves the players weight when the player logs out
	 * @param event
	 */
	@SubscribeEvent
	public void savePlayerWeightOffset(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event)
	{
		EntityPlayer player = event.player;
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		Logger.info("Unloading key: %s", EnumTagID.WEIGHT.getId());
		player.getEntityData().setDouble(EnumTagID.WEIGHT.getId(), weighable.getMaxWeight());
		Logger.info("Player %s weight = %s", player.getDisplayNameString(), weighable.getMaxWeight());
	}

	/**
	 * Prevents the player from mounting an entity if they are encumbered or over encumbered
	 * @param event
	 */
	@SubscribeEvent
	public void playerMountEvent(EntityMountEvent event)
	{
		if (event.isMounting() && event.getEntityMounting() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityMounting();
			IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);

			if (weighable.isEncumbered() || weighable.isOverEncumbered())
			{
				event.setResult(Event.Result.DENY);
				new Toast(new TextComponentTranslation("hi.splash.entityMount"));
			}
		}
	}

}
