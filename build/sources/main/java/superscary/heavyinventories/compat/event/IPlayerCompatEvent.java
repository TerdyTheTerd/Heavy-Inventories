package superscary.heavyinventories.compat.event;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@SuppressWarnings("unused")
public interface IPlayerCompatEvent
{

	void register();

	@Mod.EventHandler
	void playerTickEvent(TickEvent.PlayerTickEvent event);

}
