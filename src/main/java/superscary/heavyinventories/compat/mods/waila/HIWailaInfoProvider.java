package superscary.heavyinventories.compat.mods.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import superscary.heavyinventories.calc.WeightCalculator;
import superscary.heavyinventories.util.Toolkit;

import java.util.List;

public class HIWailaInfoProvider implements IWailaDataProvider
{

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        double weight = WeightCalculator.getWeight(Toolkit.getModName(itemStack.getItem()), itemStack.getItem());
        tooltip.add("Weight: " + weight);
        return tooltip;
    }

}
