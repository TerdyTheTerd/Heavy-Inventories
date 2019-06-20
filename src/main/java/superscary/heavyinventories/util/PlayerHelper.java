package superscary.heavyinventories.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.client.gui.Toast;
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

    public PlayerHelper(EntityPlayerMP playerMP)
    {
        this.player = playerMP;
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

    public boolean isNotEncumbered()
    {
        return !isOverEncumbered() && !isEncumbered();
    }

    public IOffset getWeightOffset()
    {
        return player.getCapability(OffsetProvider.OFFSET_CAPABILITY, null);
    }

    public Block getBlockLookingAt()
    {
        Vec3d vec3d = Minecraft.getMinecraft().objectMouseOver.hitVec;
        return Minecraft.getMinecraft().world.getBlockState(new BlockPos(vec3d)).getBlock();
    }

    @SideOnly(Side.CLIENT)
    public void sendToast(TextComponentTranslation component)
    {
        new Toast(component);
    }

    public void sendToast(String component)
    {
        sendToast(new TextComponentTranslation(component));
    }

    public static PlayerHelper getDefaultHelper()
    {
        return new PlayerHelper(Minecraft.getMinecraft().player);
    }

    public boolean hasCapability(Capability<?> capability)
    {
        return getPlayer().hasCapability(capability, null);
    }

    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return getPlayer().hasCapability(capability, facing);
    }

}
