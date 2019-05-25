package superscary.heavyinventories.client.command;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.client.command.commands.HeavyInventoriesCommands;

@SideOnly(Side.CLIENT)
public class HeavyInventoriesCommandRegistry
{

	private FMLServerStartingEvent event;

	public HeavyInventoriesCommandRegistry(FMLServerStartingEvent event)
	{
		this.event = event;
		registerAll();
	}

	private void registerAll()
	{
		register(new HeavyInventoriesCommands());
	}

	private void register(ICommand command)
	{
		event.registerServerCommand(command);
	}

}
