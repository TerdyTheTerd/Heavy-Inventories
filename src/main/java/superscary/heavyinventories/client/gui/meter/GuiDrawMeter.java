package superscary.heavyinventories.client.gui.meter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;
import superscary.heavyinventories.util.PlayerHelper;

@SideOnly(Side.CLIENT)
public class GuiDrawMeter extends Gui
{

    public GuiDrawMeter(Minecraft minecraft)
    {
        renderMeter(minecraft);
    }

    public void renderMeter(Minecraft minecraft)
    {
        if ((PlayerHelper.getDefaultHelper().getPlayer().isCreative() && HeavyInventoriesConfig.allowInCreative && HeavyInventoriesConfig.showWeightBar) || HeavyInventoriesConfig.showWeightBar)
        {
            ScaledResolution scaledResolution = new ScaledResolution(minecraft);
            ResourceLocation resourceLocation = new ResourceLocation("textures/gui/bars.png");

            minecraft.mcProfiler.startSection("weightMeter");
            minecraft.getTextureManager().bindTexture(resourceLocation);

            PlayerHelper player = new PlayerHelper(minecraft.player);

            IWeighable weighable = player.getWeightCapability();
            int i = (int) weighable.getMaxWeight();
            int x = (int) weighable.getWeight();

            if (i > 0 && player.getWeightCapability().getWeight() > 0)
            {
                int j = 182;
                int k = 1 + ((x + 99) / 100 * 100) / 100;
                // Resolution minus the offset plus the texture height
                int l = scaledResolution.getScaledHeight() - 28 + 3;

                /*
                 * Moves the color texture
                 */
                if (k > 0)
                {
                    int width = (k + 1) * k < 182 ? (k + 1) * k : 182;
                    if (weighable.isOverEncumbered())
                    {
                        drawTexturedModalRect(scaledResolution.getScaledWidth() / 2 - 91, l, 0, 25, width, 3);
                    }
                    else if (weighable.isEncumbered())
                    {
                        drawTexturedModalRect(scaledResolution.getScaledWidth() / 2 - 91, l, 0, 45, width, 3);
                    }
                    else
                    {
                        drawTexturedModalRect(scaledResolution.getScaledWidth() / 2 - 91, l, 0, 35, width, 3);
                    }
                }
            }

            minecraft.mcProfiler.endSection();

        }
    }

}
