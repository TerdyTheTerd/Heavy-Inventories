package superscary.heavyinventories.common.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import superscary.heavyinventories.util.PlayerHelper;

@Mod.EventBusSubscriber
public class CommonEventHandler
{

    @SubscribeEvent
    public void playerAttacked(LivingAttackEvent event)
    {
        Entity attacker = event.getSource().getTrueSource();
        Entity victim = event.getEntity();
        if (victim instanceof EntityPlayer)
        {
            PlayerHelper helper = new PlayerHelper((EntityPlayer) victim);
            double playerWeight = helper.getWeight();
        }
    }

}
