package superscary.heavyinventories.common.capability.offsets;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class OffsetProvider implements ICapabilitySerializable<NBTBase>
{

	@CapabilityInject(IOffset.class)
	public static final Capability<IOffset> OFFSET_CAPABILITY = null;

	private IOffset instance = OFFSET_CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == OFFSET_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == OFFSET_CAPABILITY ? OFFSET_CAPABILITY.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT()
	{
		return OFFSET_CAPABILITY.getStorage().writeNBT(OFFSET_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase base)
	{
		OFFSET_CAPABILITY.getStorage().readNBT(OFFSET_CAPABILITY, this.instance, null, base);
	}

}
