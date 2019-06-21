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
import java.nio.file.Files;
import java.nio.file.Paths;
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

    /**
     * Writes data to a json file
     * @param object
     * @param path
     * @param modid
     * @throws Exception
     * TODO: somehow, an unexpected character ($) is being written into this. not sure whats causing this.
     */
    public static void writeJsonToFile(JSONObject object, File path, String modid) throws Exception
    {
        File tester = new File(path, modid);
        Logger.log(Level.INFO, tester.getName());
        if (tester.exists() && tester.isFile()) return; //returns if the file exists so it isn't overridden with a default value
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tester), StandardCharsets.UTF_8);
        try
        {
            writer.write(makePretty(object.toJSONString()));
            Logger.log(Level.INFO, object.toJSONString());
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
        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
        {
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file.getAbsolutePath())));
            while (jsonReader.hasNext())
            {
                double w = Double.valueOf(String.valueOf(jsonObject.get(key)));
                jsonReader.close();
                return w;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
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

    public static void writeNew(File file, Item item, double newWeight)
    {
        try
        {
            JSONObject object = new JSONObject();
            object.put(item.getRegistryName().getResourcePath(), newWeight);

            Files.write(Paths.get(file.getAbsolutePath()), object.toJSONString().getBytes());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
