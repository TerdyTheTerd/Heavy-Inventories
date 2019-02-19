package superscary.heavyinventories.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.util.Toolkit;

@SideOnly(Side.CLIENT)
public class Toast extends Gui
{

    private static String theText;
    private static int overlayMessageTime;
    private static int partialTicks = 60;

    public Toast(String text)
    {
        theText = text;
        overlayMessageTime = 160;
        renderText(Minecraft.getMinecraft());
    }

    public Toast(TextComponentTranslation component)
    {
        this(component.getFormattedText());
    }

    public static void renderText(Minecraft minecraft)
    {
        renderTextToScreen(minecraft, theText);
    }

    public static void renderTextToScreen(Minecraft minecraft, String text)
    {
        Minecraft.getMinecraft().mcProfiler.startSection("weightText");
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        int i = scaledResolution.getScaledWidth();
        int j = scaledResolution.getScaledHeight();

        if (overlayMessageTime > 0)
        {
            GlStateManager.enableBlend();

            minecraft.mcProfiler.startSection("overlayMessage");
            float f2 = (float) overlayMessageTime - partialTicks;
            int l1 = (int)(f2 * 255.0F / 20.0F);

            if (l1 > 255)
            {
                l1 = 255;
            }

            if (l1 > 8)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, -Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2, -4, Toolkit.getWeightColor(Minecraft.getMinecraft().player) + (l1 << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            minecraft.mcProfiler.endSection();
        }

        updateTick();
        Minecraft.getMinecraft().mcProfiler.endSection();

    }

    public static void updateTick()
    {
        if (overlayMessageTime > 0)
        {
            --overlayMessageTime;
        }
    }

}
