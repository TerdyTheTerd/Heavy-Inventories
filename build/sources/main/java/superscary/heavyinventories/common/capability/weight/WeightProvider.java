package superscary.heavyinventories.common.capability.weight;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class WeightProvider implements ICapabilitySerializable<NBTBase>
{

	@CapabilityInject(IWeighable.class)
	public static final Capability<IWeighable> WEIGHABLE_CAPABILITY = null;

	private IWeighable instance = WEIGHABLE_CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == WEIGHABLE_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == WEIGHABLE_CAPABILITY ? WEIGHABLE_CAPABILITY.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT()
	{
		return WEIGHABLE_CAPABILITY.getStorage().writeNBT(WEIGHABLE_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase base)
	{
		WEIGHABLE_CAPABILITY.getStorage().readNBT(WEIGHABLE_CAPABILITY, this.instance, null, base);
	}

}
