package superscary.heavyinventories.client.command.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import superscary.heavyinventories.calc.PlayerWeightCalculator;
import superscary.heavyinventories.configs.reader.ConfigReader;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;

public class HeavyInventoriesSetNewWeight extends CommandBase
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
                if (args[0].equalsIgnoreCase("set") && (args[1] != null && checkNumericalWeight(args[1])))
                {
                    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
                    Item item = player.getHeldItem(player.getActiveHand()).getItem();
                    double currentWeight = PlayerWeightCalculator.getWeight(Toolkit.getModNameFromItem(item), item);
                    double newWeight = checkNumericalWeight(args[1]) ? Double.valueOf(args[1]) : -1;
                    Configuration configuration = ConfigReader.getConfig(Toolkit.getModNameFromItem(item) + ".cfg");

                    configuration.load();

                    configuration.get("general", item.getRegistryName().getResourcePath(), "", null).set(newWeight);

                    configuration.save();

                    Logger.log(Level.INFO, "Player: %s changed %s from weight %s to %s", player.getDisplayNameString(), item.getRegistryName().getResourcePath(), currentWeight, newWeight);
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public boolean checkNumericalWeight(String string)
    {
        return string.matches("^[0-9.]*$");
    }

}
