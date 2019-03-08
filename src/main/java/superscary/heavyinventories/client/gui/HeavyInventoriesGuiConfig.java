package superscary.heavyinventories.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;

@SideOnly(Side.CLIENT)
public class HeavyInventoriesGuiConfig extends GuiConfig
{
	public HeavyInventoriesGuiConfig(GuiScreen parent)
	{
		super(parent, (new ConfigElement(HeavyInventoriesConfig.getConfig().getCategory("Setup"))).getChildElements(),
				"HeavyInventories", false, true, "Heavy Inventories Config");
	}
}
