package superscary.heavyinventories.client.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.tiviacz.travellersbackpack.items.ItemTravellersBackpack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.input.Keyboard;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.calc.ItemInventoryWeight;
import superscary.heavyinventories.calc.WeightCalculator;
import superscary.heavyinventories.client.gui.InventoryWeightText;
import superscary.heavyinventories.client.gui.Toast;
import superscary.heavyinventories.client.gui.meter.GuiDrawMeter;
import superscary.heavyinventories.common.capability.offsets.IOffset;
import superscary.heavyinventories.common.capability.offsets.OffsetProvider;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.common.network.PlayerEncumberedMessage;
import superscary.heavyinventories.common.network.PlayerNotEncumberedMessage;
import superscary.heavyinventories.common.network.PlayerOverEncumberedMessage;
import superscary.heavyinventories.compat.mods.travellersbackpack.HITravellersBackpack;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.configs.PumpingIronCustomOffsetConfig;
import superscary.heavyinventories.configs.reader.ConfigReader;
import superscary.heavyinventories.util.*;
import superscary.supercore.tools.EnumColor;

@SuppressWarnings("all")
public class ClientEventHandler
{

	public static String label = Toolkit.checkFormatOfRenderText(HeavyInventoriesConfig.weightText);

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
				if (ConfigReader.getLoadedMods().contains(Toolkit.getModNameFromItem(stack) + ".cfg") && !isIgnored(Toolkit.getModNameFromItem(stack)))
				{
					String modid = Toolkit.getModNameFromItem(stack.getItem());

					double weight = Toolkit.getWeightFromStack(stack);
					event.getToolTip().add(form(weight));
					if (stack.getCount() > 1)
					{
						event.getToolTip().add(I18n.format("hi.gui.weight") + " " + (weight * stack.getCount()) + label);
					}

					if (Minecraft.getMinecraft().currentScreen != null)
					{
						if (tooltipKeyCheck(event.getItemStack()))
						{
							addShiftTip(event, stack, weight);
							doInventoryWeightCalc(event);
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
					if (tooltipKeyCheck(event.getItemStack()))
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

	private boolean isIgnored(String modid)
	{
		for (String s : HeavyInventoriesConfig.ignoredMods)
		{
			if (s.equalsIgnoreCase(modid)) return true;
		}
		return false;
	}

	/**
	 * Adds the tooltip to items when shift is being pressed
	 * @param event
	 * @param stack
	 * @param weight
	 */
	private void addShiftTip(ItemTooltipEvent event, ItemStack stack, double weight)
	{
		if (stack.getMaxStackSize() > 1) event.getToolTip().add(I18n.format("hi.gui.maxStackWeight", stack.getMaxStackSize()) + " " + (weight * stack.getMaxStackSize()) + label);
		if (HeavyInventoriesConfig.pumpingIron && PumpingIronCustomOffsetConfig.hasItem(stack.getItem())) event.getToolTip().add(I18n.format("hi.gui.offset", PumpingIronCustomOffsetConfig.getOffsetFor(stack) + label));
	}

	/**
	 * Adds the tooltip to items when shift is not being pressed
	 * @param event
	 */
	private void addNoShift(ItemTooltipEvent event)
	{
		Item item = event.getItemStack().getItem();
		if (item.getItemStackLimit(event.getItemStack()) >= 1)
		{
			event.getToolTip().add(I18n.format("hi.gui.shift", EnumColor.YELLOW + "SHIFT" + EnumColor.GREY));
		}
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
	private boolean tooltipKeyCheck(ItemStack stack)
	{
		Item item = stack.getItem();
		return (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) && (item.getItemStackLimit(stack) >= 1);
	}

	/**
	 * Default text form for tooltips
	 * @param weight
	 * @return
	 */
	private String form(double weight)
	{
		return ChatFormatting.BOLD + "" + ChatFormatting.WHITE + "Weight: " + weight + label;
	}

	/**
	 * Calculates weight of an items inventory
	 * @param event
	 */
	private void doInventoryWeightCalc(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
		if (stack != null)
		{
			if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
			{
				double invWeight = ItemInventoryWeight.getWeight(stack, 1);
				event.getToolTip().add(I18n.format("hi.gui.invWeight", invWeight) + label);
			}
			else if (HITravellersBackpack.isLoaded() && stack.getItem() instanceof ItemTravellersBackpack)
			{
				double invWeight = ItemInventoryWeight.getWeight(stack, 1);
				event.getToolTip().add(I18n.format("hi.gui.invWeight", invWeight) + label);
			}
		}
	}

	private double playersCalculatedWeight = -1;

	/**
	 * Sends message if the player is encumbered
	 * @param event
	 */
	@SubscribeEvent
	public void handleEncumberance(TickEvent.PlayerTickEvent event)
	{
		PlayerHelper player = new PlayerHelper(event.player);

		if (player.getWeight() != playersCalculatedWeight)
		{
			playersCalculatedWeight = getPlayersCalculatedWeight(player);
			player.setWeight(playersCalculatedWeight);
		}

		if (player.getRelativeWeight() >= 1.0)
		{
			player.setOverEncumbered(true);
			HeavyInventories.getNetwork().sendToServer(new PlayerOverEncumberedMessage(player.isOverEncumbered()));
		}
		else if (player.getRelativeWeight() < 1.0 && player.getRelativeWeight() >= 0.85)
		{
			player.setEncumbered(true);
			player.setOverEncumbered(false);
			HeavyInventories.getNetwork().sendToServer(new PlayerEncumberedMessage(player.isEncumbered()));
		}
		else
		{
			player.setEncumbered(false);
			HeavyInventories.getNetwork().sendToServer(new PlayerNotEncumberedMessage(player.isNotEncumbered()));
		}
	}

	public double getPlayersCalculatedWeight(PlayerHelper player)
	{
		return WeightCalculator.calculateWeight(player.getPlayer());
	}

	/**
	 * Checks if the player is encumbered and fixes whether or not they can move (and changes their move speed)
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerMove(TickEvent.PlayerTickEvent event)
	{
		PlayerHelper player = new PlayerHelper(event.player);
		player.getWeightCapability().setWeight(WeightCalculator.calculateWeight(player.getPlayer()));

		if (!player.getPlayer().isCreative())
		{
			reduceSpeed(player);
		}

		if (HeavyInventoriesConfig.allowInCreative == true)
		{
			reduceSpeed(player);
		}
	}

	/**
	 * Method for changing players move speed
	 * @param weighable
	 * @param player
	 */
	public void reduceSpeed(PlayerHelper player)
	{
		if (player.isOverEncumbered())
		{
			player.getPlayer().capabilities.setPlayerWalkSpeed(HeavyInventoriesConfig.overEncumberedSpeed);
		}
		else if (player.isEncumbered())
		{
			player.getPlayer().capabilities.setPlayerWalkSpeed(HeavyInventoriesConfig.encumberedSpeed / 10);
		}
		else
		{
			player.getPlayer().capabilities.setPlayerWalkSpeed((float) 0.1);
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
			PlayerHelper player = new PlayerHelper((EntityPlayer) event.getEntityLiving());

			if (!player.getPlayer().isCreative())
			{
				disableJumping(player);
				player.getPlayer().addExhaustion(HeavyInventoriesConfig.jumpExhaustion);
			}

			if (HeavyInventoriesConfig.allowInCreative && player.getPlayer().isCreative() && player.isOverEncumbered())
			{
				disableJumping(player);
			}
		}
	}

	/**
	 * Method to stop the player from jumping
	 * @param weighable
	 * @param player
	 */
	@SideOnly(Side.CLIENT)
	public void disableJumping(PlayerHelper player)
	{
		if (player.isOverEncumbered())
		{
			player.sendToast("hi.splash.noJump");
			player.getPlayer().motionY = 0D;
			player.getPlayer().jumpMovementFactor = 0;
		}
		else if (player.isEncumbered())
		{
			player.sendToast("hi.splash.noJumpEncumbered");
			player.getPlayer().motionY /= 5;
		}

	}

	/**
	 * Allows/disallows the player from sleeping (checks config)
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerSleep(PlayerSleepInBedEvent event)
	{
		PlayerHelper player = new PlayerHelper(event.getEntityPlayer());

		if (!HeavyInventoriesConfig.canSleepWhileOverEncumbered && player.isOverEncumbered())
		{
			event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
			player.sendToast(new TextComponentTranslation("hi.splash.loseWeightMax"));
		}
		else if (!HeavyInventoriesConfig.canSleepWhileEncumbered && player.isEncumbered())
		{
			event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
			player.sendToast(new TextComponentTranslation("hi.splash.loseWeight"));
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
		if (!HeavyInventoriesConfig.loseOffsetOnDeath) offset.setOffset(offsetOld.getOffset());
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
				if (Minecraft.getMinecraft().player.isCreative() && HeavyInventoriesConfig.allowInCreative)
				{
					new GuiDrawMeter(Minecraft.getMinecraft());
				}
				else
				{
					new GuiDrawMeter(Minecraft.getMinecraft());
				}
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
		PlayerHelper player = new PlayerHelper(event.player);

		if (player.isOverEncumbered() && isPlayerMoving(player.getPlayer()))
		{
			player.getPlayer().addExhaustion(HeavyInventoriesConfig.overEncumberedExhaustion);
		}
		else if (player.isEncumbered() && isPlayerMoving(player.getPlayer()))
		{
			player.getPlayer().addExhaustion(HeavyInventoriesConfig.encumberedExhaustion);
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
		PlayerHelper player = new PlayerHelper(event.player);

		if (player.getPlayer().isSprinting() && !player.getPlayer().isCreative() && (player.isOverEncumbered() || player.isEncumbered()))
		{
			player.getPlayer().setSprinting(canPlayerRun(player.getPlayer()));

			if (!sendRunMessage && canPlayerRun(player.getPlayer()))
			{
				new Toast(new TextComponentTranslation("hi.splash.noRun"));
				sendRunMessage = true;
			}
		}
		else if (!player.getPlayer().isSprinting() && sendRunMessage)
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

		weighable.setMaxWeight(Toolkit.roundDouble(weighable.getMaxWeight()));
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
			PlayerHelper player = new PlayerHelper((EntityPlayer) event.getEntityMounting());

			if (player.isEncumbered() || player.isOverEncumbered())
			{
				event.setResult(Event.Result.DENY);
				player.sendToast(new TextComponentTranslation("hi.splash.entityMount"));
			}
		}
	}

	@SubscribeEvent
	public void playerTeleportEnderPearl(EnderTeleportEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			Coords thrownCoords = new Coords(event);
			Coords playerCoords = new Coords(player.posX, player.posY, player.posZ);
			PlayerHelper helper = new PlayerHelper(player);
			double distance = Toolkit.getDistance(thrownCoords, playerCoords);


		}
	}

}
