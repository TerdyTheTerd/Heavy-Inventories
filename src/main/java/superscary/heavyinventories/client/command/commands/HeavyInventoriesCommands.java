package superscary.heavyinventories.client.command.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import superscary.heavyinventories.calc.WeightCalculator;
import superscary.heavyinventories.client.gui.HeavyInventoriesGuiConfig;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.configs.PumpingIronCustomOffsetConfig;
import superscary.heavyinventories.configs.reader.ConfigReader;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;
import superscary.supercore.tools.gui.DisplayDelayedGui;

public class HeavyInventoriesCommands extends CommandBase
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
            if (sender.getCommandSenderEntity() instanceof EntityPlayer)
            {
                if (args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("weight") && args[2] != null && Toolkit.checkNumericalWeight(args[2]))
                {
                    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
                    Item item = player.getHeldItem(player.getActiveHand()).getItem();

                    double currentWeight = WeightCalculator.getWeight(Toolkit.getModNameFromItem(item), item);
                    double newWeight = Toolkit.checkNumericalWeight(args[2]) ? Double.valueOf(args[2]) : -1;
                    Configuration configuration = ConfigReader.getConfig(Toolkit.getModNameFromItem(item) + ".cfg");

                    configuration.load();

                    configuration.get("general", item.getRegistryName().getResourcePath(), "", null).set(newWeight);

                    configuration.save();

                    Logger.log(Level.INFO, "Player: %s changed %s from weight %s to %s", player.getDisplayNameString(), item.getRegistryName().getResourcePath(), currentWeight, newWeight);
                    player.sendMessage(new TextComponentTranslation("hi.splash.setWeight", item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath(), newWeight));
                }
                else if (args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("offset") && Toolkit.checkNumericalWeight(args[2]))
                {
                    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
                    Item item = player.getHeldItem(player.getActiveHand()).getItem();

                    float currentOffset = PumpingIronCustomOffsetConfig.getOffsetFor(item);
                    float newOffset = Toolkit.checkNumericalWeight(args[2]) ? Float.valueOf(args[2]) : HeavyInventoriesConfig.pumpingIronWeightIncrease;
                    Configuration configuration = PumpingIronCustomOffsetConfig.getConfiguration();

                    configuration.load();
                    configuration.get(PumpingIronCustomOffsetConfig.GENERAL, PumpingIronCustomOffsetConfig.getSuit(item), "", null).set(newOffset);
                    configuration.save();

                    player.sendMessage(new TextComponentTranslation("hi.splash.setOffset", PumpingIronCustomOffsetConfig.getSuit(item), newOffset));
                }
                else if (args[0].equalsIgnoreCase("config"))
                {
                    new DisplayDelayedGui(2, new HeavyInventoriesGuiConfig(null));
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

}
