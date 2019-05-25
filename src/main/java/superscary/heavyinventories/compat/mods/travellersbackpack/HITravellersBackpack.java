package superscary.heavyinventories.compat.mods.travellersbackpack;

import net.minecraftforge.fml.common.Loader;

public class HITravellersBackpack
{

    public static boolean isLoaded()
    {
        return Loader.isModLoaded("travellersbackpack");
    }

}
