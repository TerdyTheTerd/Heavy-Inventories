package superscary.heavyinventories.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import superscary.heavyinventories.calc.WeightCalculator;
import superscary.heavyinventories.util.Toolkit;

import java.util.ArrayList;

public class ObjectInventory
{

    private TileEntity tileEntity;
    private IItemHandler handler;

    public ObjectInventory(TileEntity tileEntity)
    {
        this.tileEntity = tileEntity;
        this.handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    public ObjectInventory(IItemHandler handler)
    {
        this.handler = handler;
    }

    /**
     * Gets the inventory of the tile entity
     * @return
     */
    public ArrayList<ItemStack> getInventory()
    {
        //if (!tileEntityHasInventory()) return null;

        ArrayList<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < handler.getSlots(); i++)
        {
            if (handler.getStackInSlot(i) != null)
            {
                ItemStack stack = handler.getStackInSlot(i);
                list.add(stack);
            }
        }
        return list;
    }

    /**
     * Gets the weight of the inventory
     * @return
     */
    public double getWeightOfInventory()
    {
        ArrayList<ItemStack> list = getInventory();
        double weight = 0;

        for (ItemStack stack : list)
        {
            weight += WeightCalculator.getWeight(Toolkit.getModNameFromItem(stack), stack) * stack.getCount();
        }

        return weight;
    }

    public boolean tileEntityHasInventory()
    {
        if (tileEntity == null) return false;
        return tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

}
