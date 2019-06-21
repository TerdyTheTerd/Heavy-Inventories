package superscary.heavyinventories.util;

import java.io.File;
import java.io.InputStream;

public class FileHandle
{

    public static InputStream inputStreamFromFile(String path)
    {
        try
        {
            InputStream inputStream = FileHandle.class.getResourceAsStream(path);
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
