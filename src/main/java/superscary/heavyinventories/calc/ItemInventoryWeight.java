package superscary.heavyinventories.calc;

import com.tiviacz.travellersbackpack.gui.inventory.InventoryTravellersBackpack;
import com.tiviacz.travellersbackpack.items.ItemTravellersBackpack;
import net.mcft.copy.backpacks.block.BlockBackpack;
import net.mcft.copy.backpacks.misc.BackpackCapability;
import net.mcft.copy.backpacks.misc.BackpackDataItems;
import net.mcft.copy.backpacks.misc.BackpackSize;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import superscary.heavyinventories.compat.mods.travellersbackpack.HITravellersBackpack;
import superscary.heavyinventories.compat.mods.wearablebackpacks.HIWearableBackpacks;
import superscary.heavyinventories.util.PlayerHelper;
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
        //gets Wearable Backpacks compatibility (returns the entire weight of the contents)
        else if (HIWearableBackpacks.isLoaded() && PlayerHelper.getDefaultHelper().hasCapability(BackpackCapability.CAPABILITY))
        {
            stored += getWearableBackpacksWeight(stack);
        }
        else if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            int slots = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots();
            for (int i = 0; i < slots; i++)
            {
                ItemStack item = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(i);
                stored += WeightCalculator.getWeight(Toolkit.getModName(item), item) * item.getCount();
            }
        }
        else if (!stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            return WeightCalculator.getWeight(Toolkit.getModName(stack.getItem()), stack.getItem()) * stack.getCount();
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
            InventoryTravellersBackpack backpack = new InventoryTravellersBackpack(stack, PlayerHelper.getDefaultHelper().getPlayer());
            NBTTagList list = backpack.getTagCompound(stack).getTagList("Items", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound entry = list.getCompoundTagAt(i);
                int index = entry.getByte("Slot") & 255;

                if(index >= 0 && index < backpack.getSizeInventory())
                {
                    ItemStack newStack = new ItemStack(entry);
                    stored += Toolkit.roundDouble(WeightCalculator.getWeight(Toolkit.getModName(newStack), newStack) * newStack.getCount());
                }
            }
        }

        return stored;
    }

    /**
     * Gets the weight of a WearableBackpack's backpack
     * @param stack
     * @return
     */
    public static double getWearableBackpacksWeight(ItemStack stack)
    {
        double stored = 0;

        PlayerHelper helper = PlayerHelper.getDefaultHelper();

        if (helper.hasCapability(BackpackCapability.CAPABILITY))
        {
            if (stack.getItem() instanceof ItemBlock)
            {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockBackpack)
                {
                    NBTTagCompound compound = stack.getTagCompound();
                    if (compound.hasKey("size"))
                    {
                        BackpackSize size = BackpackSize.parse(compound.getTag("size"));
                        if (compound.hasKey("items"))
                        {
                            BackpackDataItems dataItems = new BackpackDataItems(size);
                            dataItems.getItems().deserializeNBT(compound.getCompoundTag("items"));

                            for (int i = 0; i < dataItems.getItems().getSlots(); i++)
                            {
                                stored += Toolkit.roundDouble(WeightCalculator.getWeight(Toolkit.getModName(stack), dataItems.getItems().getStackInSlot(i)) * dataItems.getItems().getStackInSlot(i).getCount());
                            }
                        }
                    }

                }
            }
        }

        return stored;
    }

}
