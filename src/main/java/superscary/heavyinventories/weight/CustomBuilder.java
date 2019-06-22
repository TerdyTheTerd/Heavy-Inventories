package superscary.heavyinventories.weight;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;

import java.util.ArrayList;

public class CustomBuilder
{

    public static void buildJson(String modid)
    {
        Logger.log(Level.INFO, modid);

        JSONObject mainObject = new JSONObject();

        ArrayList<Object> objects = Toolkit.getAllItemsFromMod(modid);

        for (Object o : objects)
        {
            if (o instanceof Item)
            {
                //((Item) o).getRegistryName().getResourcePath()
                JSONArray array = new JSONArray();
                JSONObject object = new JSONObject();
                object.put("weight", "" + HeavyInventoriesConfig.DEFAULT_WEIGHT);
                object.put("offset", "" + HeavyInventoriesConfig.DEFAULT_OFFSET);
                array.add(object);
                mainObject.put(((Item) o).getRegistryName().getResourcePath(), array);
            }
            else if (o instanceof Block)
            {
                //((Block) o).getRegistryName().getResourcePath()
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

    }

}
