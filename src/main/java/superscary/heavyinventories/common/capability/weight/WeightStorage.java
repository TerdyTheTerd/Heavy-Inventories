package superscary.heavyinventories.common.capability.weight;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class WeightStorage implements Capability.IStorage<IWeighable>
{

	@Override
	public NBTBase writeNBT(Capability<IWeighable> capability, IWeighable instance, EnumFacing side)
	{
		return new NBTTagDouble(instance.getWeight());
	}

	@Override
	public void readNBT(Capability<IWeighable> capability, IWeighable instance, EnumFacing side, NBTBase nbtBase)
	{
		instance.setWeight(((NBTPrimitive) nbtBase).getDouble());
	}

}
