package superscary.heavyinventories.weight;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.ObjectSortingComparator;
import superscary.heavyinventories.util.Toolkit;

import java.util.ArrayList;
import java.util.Collections;

public class CustomBuilder
{

    /**
     * builds the json file for a given modid. this is automatically called by {@link HeavyInventories#postInit(FMLPostInitializationEvent)}
     * @param modid the modid for a given mod. this will also become the files name.
     */
    public static void buildJson(String modid)
    {
        Logger.log(Level.INFO, modid);

        JSONObject mainObject = new JSONObject();

        ArrayList<Object> objects = Toolkit.getAllItemsFromMod(modid);
        Collections.sort(objects, new ObjectSortingComparator());

        for (Object o : objects)
        {
            if (o instanceof Item)
            {
                JSONArray array = new JSONArray();
                JSONObject object = new JSONObject();
                object.put("weight", "" + HeavyInventoriesConfig.DEFAULT_WEIGHT);
                object.put("offset", "" + HeavyInventoriesConfig.DEFAULT_OFFSET);
                array.add(object);
                mainObject.put(((Item) o).getRegistryName().getResourcePath(), array);
            }
            else if (o instanceof Block)
            {
                JSONArray array = new JSONArray();
                JSONObject object = new JSONObject();
                object.put("weight", "" + HeavyInventoriesConfig.DEFAULT_WEIGHT);
                object.put("offset", "" + HeavyInventoriesConfig.DEFAULT_OFFSET);
                array.add(object);
                mainObject.put(((Block) o).getRegistryName().getResourcePath(), array);
            }
        }

        try
        {
            JsonUtils.writeJsonToFile(mainObject, HeavyInventories.getWeightFileDirectory(), modid + ".json");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        Logger.log(Level.INFO, "Building %s", modid);

    }

}
