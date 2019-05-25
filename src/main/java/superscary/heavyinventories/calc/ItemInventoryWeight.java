package superscary.heavyinventories.calc;

import com.tiviacz.travellersbackpack.gui.inventory.InventoryTravellersBackpack;
import com.tiviacz.travellersbackpack.items.ItemTravellersBackpack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import superscary.heavyinventories.compat.mods.travellersbackpack.HITravellersBackpack;
import superscary.heavyinventories.util.Toolkit;

public class ItemInventoryWeight
{

    public static double getWeight(ItemStack stack)
    {
        double stored = 0;

        // Gets Travelers backpack compatibility (returns the entire weight of the contents)
        if (HITravellersBackpack.isLoaded() && stack.getItem() instanceof ItemTravellersBackpack)
        {
            stored += getTravellersBackpackWeight(stack);
        }
        else if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            int slots = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots();
            for (int i = 0; i < slots; i++)
            {
                ItemStack item = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(i);
                stored += WeightCalculator.getWeight(Toolkit.getModNameFromItem(item), item) * item.getCount();
            }
        }
        else if (!stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            return WeightCalculator.getWeight(Toolkit.getModNameFromItem(stack.getItem()), stack.getItem()) * stack.getCount();
        }

        return stored;
    }

    /**
     * Gets weight of an inventory and rounds it
     * @param stack
     * @param places
     * @return
     */
    public static double getWeight(ItemStack stack, int places)
    {
        return Toolkit.roundDouble(getWeight(stack), places);
    }

    /**
     * Gets the weight of all contents in a backpack from Traveller's Backpacks
     * @param stack
     * @return
     */
    public static double getTravellersBackpackWeight(ItemStack stack)
    {
        double stored = 0;

        if (stack.getItem() instanceof ItemTravellersBackpack)
        {
            InventoryTravellersBackpack backpack = new InventoryTravellersBackpack(stack);
            NBTTagList list = backpack.getTagCompound(stack).getTagList("Items", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound entry = list.getCompoundTagAt(i);
                int index = entry.getByte("Slot") & 255;

                if(index >= 0 && index < backpack.getSizeInventory())
                {
                    ItemStack newStack = new ItemStack(entry);
                    stored += Toolkit.roundDouble(WeightCalculator.getWeight(Toolkit.getModNameFromItem(newStack), newStack) * newStack.getCount());
                }
            }
        }

        return stored;
    }

}
