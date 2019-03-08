package superscary.heavyinventories.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerEncumberedMessage implements IMessage
{

	private boolean isEncumbered;

	public PlayerEncumberedMessage()
	{
	}

	public PlayerEncumberedMessage(boolean isEncumbered)
	{

		this.isEncumbered = isEncumbered;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(isEncumbered);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		isEncumbered = buf.readBoolean();
	}

	public boolean isEncumbered()
	{
		return isEncumbered;
	}

}
