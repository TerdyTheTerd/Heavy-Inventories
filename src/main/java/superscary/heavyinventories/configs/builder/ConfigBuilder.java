package superscary.heavyinventories.configs.builder;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Level;
import org.json.simple.JSONObject;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.JsonUtils;
import superscary.heavyinventories.util.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ConfigBuilder
{

    public static void buildJson(String modid)
    {
        Logger.log(Level.INFO, modid);

        JSONObject object = new JSONObject();

        for (Item item : Item.REGISTRY)
        {
            if (item.getRegistryName().getResourceDomain().equalsIgnoreCase(modid))
            {
                object.putIfAbsent(item.getRegistryName().getResourcePath(), "" + HeavyInventoriesConfig.DEFAULT_WEIGHT);
            }
        }

        for (Block block : Block.REGISTRY)
        {
            if (block.getRegistryName().getResourceDomain().equalsIgnoreCase(modid))
            {
                object.putIfAbsent(block.getRegistryName().getResourcePath(), "" + HeavyInventoriesConfig.DEFAULT_WEIGHT);
            }
        }

        try
        {
            JsonUtils.writeJsonToFile(object, HeavyInventories.getWeightFileDirectory(), modid + ".json");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static JSONObject getAllItemsFromMod(String modid)
    {
        JSONObject object = new JSONObject();
        return null;
    }

    public static JSONObject read(File file) throws Exception
    {
        JSONObject object = new JSONObject();
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        try
        {
            writer.write(object.toJSONString());
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            writer.close();
        }

        return null;
    }

}
