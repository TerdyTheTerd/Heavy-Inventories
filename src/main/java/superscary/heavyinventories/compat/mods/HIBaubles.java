package superscary.heavyinventories.compat.mods;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import superscary.heavyinventories.calc.PlayerWeightCalculator;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.util.Logger;

public class HIBaubles
{

	private static final String NAME = "Baubles";

	public HIBaubles()
	{}

	public static void build()
	{
		Logger.info("Oh why hello there %s :)", NAME);
		Logger.info("Loading compat for %s...", NAME);
		register();
	}

	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(new HIBaubles());
	}

	/*@SubscribeEvent
	public void playerTickEvent(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		IWeighable weighable = player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		weighable.setWeight(PlayerWeightCalculator.calculateWeight(player));
	}*/

}
