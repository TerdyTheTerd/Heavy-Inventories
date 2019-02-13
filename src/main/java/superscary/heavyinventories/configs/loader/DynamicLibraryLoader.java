package superscary.heavyinventories.configs.loader;

import net.minecraftforge.common.config.Configuration;

public class DynamicLibraryLoader
{

    public static void dynLoadConfig(Configuration configuration)
    {
        configuration.load();
    }

    public static void dynClearMemory(Configuration configuration)
    {
        configuration = null;
    }

}
