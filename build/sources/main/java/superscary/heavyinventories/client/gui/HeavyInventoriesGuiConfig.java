package superscary.heavyinventories.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;

import static superscary.heavyinventories.util.Constants.MODID;

@SideOnly(Side.CLIENT)
public class HeavyInventoriesGuiConfig extends GuiConfig
{
	public HeavyInventoriesGuiConfig(GuiScreen parent)
	{
		super(parent, (new ConfigElement(HeavyInventoriesConfig.getConfig().getCategory("Setup"))).getChildElements(),
				MODID, false, true, "Heavy Inventories Config");
	}
}
