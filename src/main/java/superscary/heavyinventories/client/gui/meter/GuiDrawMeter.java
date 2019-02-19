package superscary.heavyinventories.client.gui.meter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.common.capability.weight.WeightProvider;
import superscary.heavyinventories.configs.HeavyInventoriesConfig;

/**
 * Copyright (c) 2018 by SuperScary(ERBF)
 * <p>
 * All rights reserved. No part of this software may be reproduced,
 * distributed, or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods,
 * without the prior written permission of the publisher, except in
 * the case of brief quotations embodied in critical reviews and
 * certain other noncommercial uses permitted by copyright law.
 */
@SideOnly(Side.CLIENT)
public class GuiDrawMeter extends Gui
{

    public GuiDrawMeter(Minecraft minecraft)
    {
        renderMeter(minecraft);
    }

    public void renderMeter(Minecraft minecraft)
    {
        if (HeavyInventoriesConfig.showWeightBar)
        {
            ScaledResolution scaledResolution = new ScaledResolution(minecraft);
            ResourceLocation resourceLocation = new ResourceLocation("textures/gui/bars.png");

            minecraft.mcProfiler.startSection("weightMeter");
            minecraft.getTextureManager().bindTexture(resourceLocation);

            IWeighable weighable = minecraft.player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null);
            int i = (int) weighable.getMaxWeight();
            int x = (int) weighable.getWeight();

            if (i > 0 && minecraft.player.getCapability(WeightProvider.WEIGHABLE_CAPABILITY, null).getWeight() > 0)
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
                    if (weighable.isOverEncumbered())
                    {
                        drawTexturedModalRect(scaledResolution.getScaledWidth() / 2 - 91, l, 0, 25, (k + 1) * k, 3);
                    } else if (weighable.isEncumbered())
                    {
                        drawTexturedModalRect(scaledResolution.getScaledWidth() / 2 - 91, l, 0, 45, (k + 1) * k, 3);
                    } else
                    {
                        drawTexturedModalRect(scaledResolution.getScaledWidth() / 2 - 91, l, 0, 35, (k + 1) * k, 3);
                    }
                }
            }

            minecraft.mcProfiler.endSection();

        }
    }

}
