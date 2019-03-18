package superscary.heavyinventories.compat.mods.waila;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.Level;
import superscary.heavyinventories.util.Logger;

@WailaPlugin
public class HIWaila implements IWailaPlugin
{

    public HIWaila()
    {
        Logger.log(Level.INFO,"Hi there Walia ;)");
    }

    @Override
    public void register(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new HIWailaInfoProvider(), HIWailaInfoProvider.class);
    }

    public Block getBlockLookingAt()
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Vec3d vec = player.getLookVec();
        BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);

        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
    }

}
