package superscary.heavyinventories.compat.mods;

import net.minecraftforge.common.MinecraftForge;
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
		weighable.setWeight(WeightCalculator.calculateWeight(player));
	}*/

}
