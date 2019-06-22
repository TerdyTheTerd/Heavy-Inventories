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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class JsonUtils
{

    public static String getJSONStringFromFile(String path)
    {
        try
        {
            Scanner scanner;
            InputStream in = FileHandler.inputStreamFromFile(path);
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
     * Writes new data to a file
     * @param file
     * @param item
     * @param newWeight
     * @param type
     * TODO: not writing the array correctly
     */
    public static void writeNew(File file, Item item, double newWeight, Type type)
    {
        ArrayList<Object> objects = Toolkit.getAllItemsFromMod(item.getRegistryName().getResourceDomain());

        JSONParser parser = new JSONParser();

        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
        {
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file.getAbsolutePath())));
            while (jsonReader.hasNext())
            {
                JSONArray jsonArray = (JSONArray) jsonObject.get(item.getRegistryName().getResourcePath());
                Iterator iterator = jsonArray.iterator();

                while (iterator.hasNext())
                {
                    if (type == Type.WEIGHT)
                    {
                        JSONObject object = (JSONObject) iterator.next();
                        object.put("weight", "" + newWeight);
                        object.put("offset", "" + readJson(file, item.getRegistryName().getResourcePath(), Type.OFFSET));
                        jsonArray.add(object);
                    }
                    else if (type == Type.OFFSET)
                    {
                        JSONObject object = (JSONObject) iterator.next();
                        object.put("offset", "" + newWeight);
                        object.put("weight", "" + readJson(file, item.getRegistryName().getResourcePath(), Type.WEIGHT));
                        jsonArray.add(object);
                    }
                }

                jsonObject.replace(item.getRegistryName().getResourcePath(), jsonArray);
                iterator.remove();
            }

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            try
            {
                writer.write(makePretty(jsonObject.toJSONString()));
                Logger.log(Level.INFO, jsonObject.toJSONString());
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

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Reads the value of of the key (item/block name) from the mods json file
     * @param file
     * @param key
     * @return
     */
    private static double readJson(File file, String key, Type type)
    {
        JSONParser parser = new JSONParser();
        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
        {
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file.getAbsolutePath())));
            while (jsonReader.hasNext())
            {
                JSONArray array = (JSONArray) jsonObject.get(key);
                Iterator iterator = array.iterator();
                double returner = 0;
                while (iterator.hasNext())
                {
                    if (type == Type.WEIGHT)
                    {
                        JSONObject weightObject = (JSONObject) iterator.next();
                        returner = Double.valueOf(String.valueOf(weightObject.get("weight")));
                    }
                    else if (type == Type.OFFSET)
                    {
                        JSONObject offsetObject = (JSONObject) iterator.next();
                        returner = Double.valueOf(String.valueOf(offsetObject.get("offset")));
                    }
                }
                jsonReader.close();
                return returner;
            }
        } catch (Exception e)
        {}

        parser.reset();

        return HeavyInventoriesConfig.DEFAULT_WEIGHT;
    }

    private static double readJson(File file, Item item, Type type)
    {
        return readJson(file, item.getRegistryName().getResourcePath(), type);
    }

    private static double readJson(File file, ItemStack stack, Type type)
    {
        return readJson(file, stack.getItem().getRegistryName().getResourcePath(), type);
    }

    private static double readJson(File file, Block block, Type type)
    {
        return readJson(file, block.getRegistryName().getResourcePath(), type);
    }

    /**
     * Actual method to get the weight of an object
     * @param file
     * @param object
     * @return
     */
    public static double readJson(File file, Object object, Type type)
    {
        if (object instanceof Item) return readJson(file, (Item) object, type);
        else if (object instanceof ItemStack) return readJson(file, (ItemStack) object, type);
        else if (object instanceof Block) return readJson(file, (Block) object, type);
        else return HeavyInventoriesConfig.DEFAULT_WEIGHT;
    }

    public enum Type
    {
        OFFSET("offset"),
        WEIGHT("weight");

        private String s;
        Type(String s)
        {
            this.s = s;
        }

        public String getString()
        {
            return s;
        }

    }

}
