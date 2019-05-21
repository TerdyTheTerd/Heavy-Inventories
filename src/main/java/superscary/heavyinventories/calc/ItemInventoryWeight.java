package superscary.heavyinventories.calc;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import superscary.heavyinventories.util.Toolkit;

public class ItemInventoryWeight
{

    public static double getWeight(ItemStack itemStack)
    {
        double stored = 0;

        if (!itemStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) return WeightCalculator.getWeight(Toolkit.getModNameFromItem(itemStack.getItem()), itemStack.getItem()) * itemStack.getCount();

        if (itemStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            int slots = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots();
            for (int i = 0; i < slots; i++)
            {
                ItemStack item = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(i);
                stored += WeightCalculator.getWeight(Toolkit.getModNameFromItem(item), item) * item.getCount();
            }
        }

        return stored;
    }

}
