package superscary.heavyinventories.client.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.common.capability.offsets.IOffset;
import superscary.heavyinventories.common.capability.offsets.OffsetProvider;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.EnumTagID;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;

import java.io.File;

public class PumpingIronHandler
{

	@SubscribeEvent
	public void handleAnvilUse(AnvilRepairEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();

		IOffset offset = player.getCapability(OffsetProvider.OFFSET_CAPABILITY, null);

		offset.setOffset(offset.getOffset() + JsonUtils.readJson(new File(HeavyInventories.getWeightFileDirectory(),  Toolkit.getModName(event.getItemResult()) + ".json"), event.getItemResult(), JsonUtils.Type.OFFSET));

		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		if (player.getEntityData().hasKey(EnumTagID.MAX_WEIGHT.getId()))
		{
			weighable.setMaxWeight(weighable.getMaxWeight() + JsonUtils.readJson(new File(HeavyInventories.getWeightFileDirectory(),  Toolkit.getModName(event.getItemResult()) + ".json"), event.getItemResult(), JsonUtils.Type.OFFSET));
			player.getEntityData().setDouble(EnumTagID.MAX_WEIGHT.getId(), weighable.getMaxWeight());
			ClientEventHandler.setPlayerWeight(weighable.getMaxWeight());
			Logger.info("Updated Player: %s's Max Carry Weight To: %s %s", player.getName(), weighable.getMaxWeight(), "stone");
		}
		else
		{
			Logger.info("\"HIWeight\" is not part of %s's player data! Adding it now...");
			weighable.setMaxWeight(HeavyInventoriesConfig.maxCarryWeight);
			player.getEntityData().setDouble(EnumTagID.MAX_WEIGHT.getId(), HeavyInventoriesConfig.maxCarryWeight);
			ClientEventHandler.setPlayerWeight(HeavyInventoriesConfig.maxCarryWeight);
		}

		Toolkit.playSoundAtPlayer(player, 355, 20, 10);
	}

	@SubscribeEvent
	public void handleToolCraft(PlayerEvent.ItemCraftedEvent event)
	{

	}

	@SubscribeEvent
	public void handleArmorCraft(PlayerEvent.ItemCraftedEvent event)
	{

	}

}
