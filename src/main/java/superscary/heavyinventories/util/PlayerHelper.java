package superscary.heavyinventories.util;

import net.minecraft.entity.player.EntityPlayer;
import superscary.heavyinventories.common.capability.offsets.IOffset;
import superscary.heavyinventories.common.capability.offsets.OffsetProvider;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;

public class PlayerHelper
{

    private EntityPlayer player;

    public PlayerHelper(EntityPlayer player)
    {
        this.player = player;
    }

    public IWeighable getWeightCapability()
    {
        return player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

    public double getWeight()
    {
        return getWeightCapability().getWeight();
    }

    public double getMaxWeight()
    {
        return getWeightCapability().getMaxWeight();
    }

    public void setWeight(double newWeight)
    {
        getWeightCapability().setWeight(newWeight);
    }

    public double getRelativeWeight()
    {
        return getWeightCapability().getRelativeWeight();
    }

    public void setOverEncumbered(boolean bool)
    {
        getWeightCapability().setOverEncumbered(bool);
    }

    public void setEncumbered(boolean bool)
    {
        getWeightCapability().setEncumbered(bool);
    }

    public boolean isOverEncumbered()
    {
        return getWeightCapability().isOverEncumbered();
    }

    public boolean isEncumbered()
    {
        return getWeightCapability().isEncumbered();
    }

    public IOffset getWeightOffset()
    {
        return player.getCapability(OffsetProvider.OFFSET_CAPABILITY, null);
    }

}
