package superscary.heavyinventories.configs.builder;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Level;
import org.json.simple.JSONObject;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.Toolkit;

import java.util.ArrayList;

public class ConfigBuilder
{

    public static void buildJson(String modid)
    {
        Logger.log(Level.INFO, modid);

        JSONObject object = new JSONObject();

        ArrayList<Object> objects = Toolkit.getAllItemsFromMod(modid);

        for (Object o : objects)
        {
            if (o instanceof Item) object.put(((Item) o).getRegistryName().getResourcePath(), "" + HeavyInventoriesConfig.DEFAULT_WEIGHT);
            if (o instanceof Block) object.put(((Block) o).getRegistryName().getResourcePath(), "" + HeavyInventoriesConfig.DEFAULT_WEIGHT);
        }

        try
        {
            JsonUtils.writeJsonToFile(object, HeavyInventories.getWeightFileDirectory(), modid + ".json");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
