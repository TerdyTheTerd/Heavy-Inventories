package superscary.heavyinventories.calc;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import superscary.heavyinventories.util.Toolkit;

public class ItemInventoryWeight
{

    public static double getWeight(ItemStack itemStack)
    {
        if (!itemStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) return WeightCalculator.getWeight(Toolkit.getModNameFromItem(itemStack.getItem()), itemStack.getItem()) * itemStack.getCount();
        int slots = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots();
        double stored = 0;

        for (int i = 0; i < slots; i++)
        {
            ItemStack item = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(i);
            stored += WeightCalculator.getWeight(Toolkit.getModNameFromItem(item), item);
        }

        return WeightCalculator.getWeight(Toolkit.getModNameFromItem(itemStack.getItem()), itemStack.getItem()) * itemStack.getCount();
    }

}
