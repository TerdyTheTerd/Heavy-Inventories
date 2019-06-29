package superscary.heavyinventories.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.PlayerHelper;
import superscary.heavyinventories.util.Toolkit;

@SideOnly(Side.CLIENT)
public class InventoryWeightText extends Gui
{

	private static String label = Toolkit.checkFormatOfRenderText(HeavyInventoriesConfig.weightText);

	public static void renderText(Minecraft minecraft)
	{
		IWeighable weighable = PlayerHelper.getDefaultHelper().getWeightCapability();
		ScaledResolution scaledResolution = new ScaledResolution(minecraft);
		int attackIndicator = minecraft.gameSettings.attackIndicator;

		if (attackIndicator == EnumAttackIndicator.CROSSHAIR.getAttackIndicator())
		{
			renderTextToScreen(minecraft, weighable, scaledResolution, 15, Toolkit.roundDouble(weighable.getMaxWeight()));
		}
		else if (attackIndicator == EnumAttackIndicator.BAR.getAttackIndicator())
		{
			renderTextToScreen(minecraft, weighable, scaledResolution, 30, Toolkit.roundDouble(PlayerHelper.getDefaultHelper().getMaxWeight()));
		}
	}

	public static void renderTextToScreen(Minecraft minecraft, IWeighable weighable, ScaledResolution scaledResolution, int scaledResolutionOffset, double display)
	{
		minecraft.fontRenderer.drawString("" + Toolkit.roundDouble(weighable.getWeight()) + "/" + display + label, scaledResolution.getScaledWidth() / 2 + 97, scaledResolution.getScaledHeight() - scaledResolutionOffset, Toolkit.getWeightColor(Minecraft.getMinecraft().player), true);
	}

	public enum EnumAttackIndicator
	{
		CROSSHAIR(1),
		BAR(2);

		private int pos;
		EnumAttackIndicator(int pos)
		{
			this.pos = pos;
		}

		public int getAttackIndicator()
		{
			return pos;
		}
	}

}
