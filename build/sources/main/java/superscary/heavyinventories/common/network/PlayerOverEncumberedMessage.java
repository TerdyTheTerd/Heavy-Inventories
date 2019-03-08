package superscary.heavyinventories.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerOverEncumberedMessage implements IMessage
{

	private boolean isOverEncumbered;

	public PlayerOverEncumberedMessage()
	{

	}

	public PlayerOverEncumberedMessage(boolean isOverEncumbered)
	{
		this.isOverEncumbered = isOverEncumbered;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(isOverEncumbered);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		isOverEncumbered = buf.readBoolean();
	}

	public boolean isOverEncumbered()
	{
		return isOverEncumbered;
	}

}
