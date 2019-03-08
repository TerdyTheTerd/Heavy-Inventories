package superscary.heavyinventories.client.command.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import superscary.heavyinventories.client.gui.HeavyInventoriesGuiConfig;
import superscary.supercore.tools.gui.DisplayDelayedGui;

public class HeavyInventoriesOpenConfigGui extends CommandBase
{

	@Override
	public String getName()
	{
		return "heavyinventories";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "hi.commands.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
	{
		try
		{
			new DisplayDelayedGui(2, new HeavyInventoriesGuiConfig(null));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
