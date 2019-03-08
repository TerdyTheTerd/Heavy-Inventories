package superscary.heavyinventories.common.network.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.common.network.PlayerEncumberedMessage;

public class PlayerEncumberedMessageHandler implements IMessageHandler<PlayerEncumberedMessage, IMessage>
{

	@Override
	public IMessage onMessage(PlayerEncumberedMessage message, MessageContext context)
	{
		EntityPlayerMP playerMP = context.getServerHandler().player;
		IWeighable weighable = playerMP.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		if (message.isEncumbered())
		{
			weighable.setEncumbered(message.isEncumbered());
		}

		return null;
	}

}
