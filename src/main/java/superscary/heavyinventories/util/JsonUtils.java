package superscary.heavyinventories.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class JsonUtils
{

    public static String getJSONStringFromFile(String path)
    {
        try
        {
            Scanner scanner;
            InputStream in = FileHandle.inputStreamFromFile(path);
            scanner = new Scanner(in);
            String json = scanner.useDelimiter("\\Z").next();
            scanner.close();
            in.close();

            return json;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean objectExists(JSONObject jsonObject, String key)
    {
        Object o;

        try
        {
            o = jsonObject.get(key);
        } catch (Exception e)
        {
            return false;
        }

        return o != null;
    }

    public static void writeJsonToFile(JSONObject object, File path, String modid) throws Exception
    {
        File tester = new File(path, modid);
        Logger.log(Level.INFO, tester.getName());
        if (tester.exists() && tester.isFile()) return;
        //FileWriter writer = new FileWriter(tester);
        OutputStreamWriter writer1 = new OutputStreamWriter(new FileOutputStream(tester), StandardCharsets.UTF_8);
        try
        {
            writer1.write(makePretty(object.toJSONString()));
            Logger.log(Level.INFO, object.toJSONString());
            writer1.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            writer1.close();
        }
    }

    /**
     * Makes the Json file easier to read (only makes the file easier to be read by humans, machines don't care).
     * @param string
     * @return
     */
    public static String makePretty(String string)
    {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(string).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    /**
     * Reads the value of of the key (item/block name) from the mods json file
     * @param file
     * @param key
     * @return
     */
    private static double readJson(File file, String key)
    {
        JSONParser parser = new JSONParser();

        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(file), "UTF-8")))
        {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonReader.getPath().split("$")[0]);
            while (jsonReader.hasNext())
            {
                double w = Double.valueOf(String.valueOf((double) jsonObject.get(key)));
                return w;
            }
        } catch (Exception e)
        {
            //e.printStackTrace();
        }

        parser.reset();

        return HeavyInventoriesConfig.DEFAULT_WEIGHT;
    }

    private static double readJson(File file, Item item)
    {
        return readJson(file, item.getRegistryName().getResourcePath());
    }

    private static double readJson(File file, ItemStack stack)
    {
        return readJson(file, stack.getItem().getRegistryName().getResourcePath());
    }

    private static double readJson(File file, Block block)
    {
        return readJson(file, block.getRegistryName().getResourcePath());
    }

    /**
     * Actual method to get the weight of an object
     * @param file
     * @param object
     * @return
     */
    public static double readJson(File file, Object object)
    {
        if (object instanceof Item) return readJson(file, (Item) object);
        else if (object instanceof ItemStack) return readJson(file, (ItemStack) object);
        else if (object instanceof Block) return readJson(file, (Block) object);
        else return HeavyInventoriesConfig.DEFAULT_WEIGHT;
    }

}
