package superscary.heavyinventories.server;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import superscary.heavyinventories.client.event.PumpingIronHandler;
import superscary.heavyinventories.common.CommonProxy;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;

public class ServerProxy extends CommonProxy
{

	@Override
	public void preInit()
	{
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new HeavyInventoriesConfig());
		MinecraftForge.EVENT_BUS.register(new PumpingIronHandler());
	}

	@Override
	public void init()
	{

	}

	@Override
	public void postInit()
	{

	}

	@Mod.EventHandler
	private static void setupNetwork(FMLPreInitializationEvent event)
	{

	}

}
