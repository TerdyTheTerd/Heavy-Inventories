package superscary.heavyinventories.client.command.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Configuration;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.configs.PumpingIronCustomOffsetConfig;
import superscary.heavyinventories.util.Toolkit;

public class HeavyInventoriesSetPumpingIronOffset extends CommandBase
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
            if (sender instanceof EntityPlayer)
            {
                if (args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("pumpingiron") && Toolkit.checkNumericalWeight(args[2]))
                {
                    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
                    Item item = player.getHeldItem(player.getActiveHand()).getItem();

                    if (item == null) return;

                    float currentOffset = PumpingIronCustomOffsetConfig.getOffsetFor(item);
                    float newOffset = Toolkit.checkNumericalWeight(args[2]) ? Float.valueOf(args[2]) : HeavyInventoriesConfig.pumpingIronWeightIncrease;
                    Configuration configuration = PumpingIronCustomOffsetConfig.getConfiguration();

                    configuration.load();
                    configuration.get(PumpingIronCustomOffsetConfig.GENERAL, PumpingIronCustomOffsetConfig.getSuit(item), "", null).set(newOffset);
                    configuration.save();

                    player.sendMessage(new TextComponentTranslation("hi.splash.setOffset", PumpingIronCustomOffsetConfig.getSuit(item), newOffset));
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

}
