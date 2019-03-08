package superscary.heavyinventories.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import superscary.heavyinventories.client.event.ClientEventHandler;
import superscary.heavyinventories.client.event.PumpingIronHandler;
import superscary.heavyinventories.common.CommonProxy;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.configs.weights.MinecraftConfig;

public class ClientProxy extends CommonProxy
{

	@Override
	public void preInit()
	{
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.register(new MinecraftConfig());
		MinecraftForge.EVENT_BUS.register(new HeavyInventoriesConfig());
		MinecraftForge.EVENT_BUS.register(new PumpingIronHandler());
	}

	@Override
	public void init()
	{
		super.init();
	}

	@Override
	public void postInit()
	{

	}

	public static EntityPlayer getPlayerFromContext(MessageContext context)
	{
		return Minecraft.getMinecraft().player;
	}

}
