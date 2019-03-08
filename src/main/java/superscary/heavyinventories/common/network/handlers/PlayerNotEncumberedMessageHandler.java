package superscary.heavyinventories.common.network.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.common.network.PlayerNotEncumberedMessage;

public class PlayerNotEncumberedMessageHandler implements IMessageHandler<PlayerNotEncumberedMessage, IMessage>
{

	@Override
	public IMessage onMessage(PlayerNotEncumberedMessage message, MessageContext context)
	{
		EntityPlayerMP playerMP = context.getServerHandler().player;
		IWeighable weighable = playerMP.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
		if (message.isNotEncumbered())
		{
			weighable.setOverEncumbered(message.isNotEncumbered());
		}

		return null;
	}

}
