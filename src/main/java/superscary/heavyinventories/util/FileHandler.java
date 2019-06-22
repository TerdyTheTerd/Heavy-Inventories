package superscary.heavyinventories.util;

import java.io.File;
import java.io.InputStream;

public class FileHandler
{

    public static InputStream inputStreamFromFile(String path)
    {
        try
        {
            InputStream inputStream = FileHandler.class.getResourceAsStream(path);
            return inputStream;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream inputStreamFromFile(File file)
    {
        return inputStreamFromFile(file.getPath());
    }

}
