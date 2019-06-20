package superscary.heavyinventories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.Logger;

import java.io.*;
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
        FileWriter writer = new FileWriter(tester);
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

    public static String makePretty(String string)
    {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(string).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    public static double readJson(File file, String key)
    {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(file))
        {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            double weight = (Double) jsonObject.get(key);
            return weight;
        } catch (Exception e)
        {
            //e.printStackTrace();
        }

        return HeavyInventoriesConfig.DEFAULT_WEIGHT;
    }

}
