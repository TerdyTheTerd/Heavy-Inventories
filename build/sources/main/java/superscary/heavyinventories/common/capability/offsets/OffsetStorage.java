package superscary.heavyinventories.common.capability.offsets;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class OffsetStorage implements Capability.IStorage<IOffset>
{

	@Override
	public NBTBase writeNBT(Capability<IOffset> capability, IOffset instance, EnumFacing side)
	{
		return new NBTTagDouble(instance.getOffset());
	}

	@Override
	public void readNBT(Capability<IOffset> capability, IOffset instance, EnumFacing side, NBTBase base)
	{
		instance.setOffset(((NBTPrimitive) base).getDouble());
	}

}
