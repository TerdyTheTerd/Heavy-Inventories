package superscary.heavyinventories.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerNotEncumberedMessage implements IMessage
{

	private boolean isNotEncumbered;

	public PlayerNotEncumberedMessage()
	{
	}

	public PlayerNotEncumberedMessage(boolean isNotEncumbered)
	{

		this.isNotEncumbered = isNotEncumbered;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(isNotEncumbered);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		isNotEncumbered = buf.readBoolean();
	}

	public boolean isNotEncumbered()
	{
		return isNotEncumbered;
	}

}