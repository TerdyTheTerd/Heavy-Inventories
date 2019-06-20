package superscary.heavyinventories.compat.mods.wearablebackpacks;

import net.minecraftforge.fml.common.Loader;

public class HIWearableBackpacks
{

    public static boolean isLoaded()
    {
        return Loader.isModLoaded("wearablebackpacks");
    }

}
