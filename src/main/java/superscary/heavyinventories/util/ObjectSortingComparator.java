package superscary.heavyinventories.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.Comparator;

public class ObjectSortingComparator implements Comparator<Object>
{

    @Override
    public int compare(Object o1, Object o2)
    {
        if (o1 instanceof Block)
        {
            Block block = (Block) o1;

            if (o2 instanceof Item)
            {
                return block.getRegistryName().getResourcePath().compareToIgnoreCase(((Item) o2).getRegistryName().getResourcePath());
            }
            else if (o2 instanceof Block)
            {
                return block.getRegistryName().getResourcePath().compareToIgnoreCase(((Block) o2).getRegistryName().getResourcePath());
            }
        }
        else if (o1 instanceof Item)
        {
            Item item = (Item) o1;

            if (o2 instanceof Block)
            {
                return item.getRegistryName().getResourcePath().compareToIgnoreCase(((Block) o2).getRegistryName().getResourcePath());
            }
            else if (o2 instanceof Item)
            {
                return item.getRegistryName().getResourcePath().compareToIgnoreCase(((Item) o2).getRegistryName().getResourcePath());
            }
        }
        return -1;
    }

}
