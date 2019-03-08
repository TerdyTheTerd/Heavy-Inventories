package superscary.heavyinventories.client.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import superscary.heavyinventories.common.capability.offsets.IOffset;
import superscary.heavyinventories.common.capability.offsets.OffsetProvider;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.EnumTagID;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;

public class PumpingIronHandler
{

	@SubscribeEvent
	public void handleAnvilUse(AnvilRepairEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();

		IOffset offset = player.getCapability(OffsetProvider.OFFSET_CAPABILITY, null);

		offset.setOffset(offset.getOffset() + HeavyInventoriesConfig.pumpingIronWeightIncrease);

		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		if (player.getEntityData().hasKey(EnumTagID.WEIGHT.getId()))
		{
			weighable.setMaxWeight(weighable.getMaxWeight() + HeavyInventoriesConfig.pumpingIronWeightIncrease);
			player.getEntityData().setDouble(EnumTagID.WEIGHT.getId(), weighable.getMaxWeight());
			ClientEventHandler.setPlayerWeight(weighable.getMaxWeight());
			Logger.info("Updated Player: %s's Max Carry Weight To: %s %s", player.getName(), weighable.getMaxWeight(), "stone");
		}
		/*else
		{
			Logger.info("\"HIWeight\" is not part of %s's player data! Adding it now...");
		}*/

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
