package superscary.heavyinventories.client.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;

import static superscary.heavyinventories.util.Constants.MODID;

public class HIItemRegistry
{

    public static ArrayList<Item> bakery = new ArrayList<>();

    public static ItemUpgradeToken upgradeToken;

    public static void get()
    {
        upgradeToken = new ItemUpgradeToken("itemUpgradeToken");
    }

    public static void register(Item item)
    {
        ForgeRegistries.ITEMS.register(item);
    }

    @SubscribeEvent
    public static void bakeModels(ModelRegistryEvent event)
    {
        for (Item item : bakery)
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    @SubscribeEvent
    public static void lootTable(LootTableLoadEvent event)
    {
        if (event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT))
        {
            addToLootTable(event, upgradeToken);
        }
        else if (event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID))
        {
            addToLootTable(event, upgradeToken);
        }
        else if (event.getName().equals(LootTableList.CHESTS_END_CITY_TREASURE))
        {
            addToLootTable(event, upgradeToken);
        }
        else if (event.getName().equals(LootTableList.CHESTS_IGLOO_CHEST))
        {
            addToLootTable(event, upgradeToken);
        }
        else if (event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE))
        {
            addToLootTable(event, upgradeToken);
        }
        else if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON))
        {
            addToLootTable(event, upgradeToken);
        }
        else if (event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH))
        {
            addToLootTable(event, upgradeToken);
        }
        else if (event.getName().equals(LootTableList.CHESTS_WOODLAND_MANSION))
        {
            addToLootTable(event, upgradeToken);
        }
    }

    public static void addToLootTable(LootTableLoadEvent event, Item item)
    {
        final LootPool pool = event.getTable().getPool("pool2");
        if (pool != null)
        {
            pool.addEntry(new LootEntryItem(item, 10, 0, new LootFunction[] { new SetCount(new LootCondition[0], new RandomValueRange(1, 3))}, new LootCondition[0], MODID + "loot_table"));
        }

        event.getTable().addPool(pool);
    }

}
