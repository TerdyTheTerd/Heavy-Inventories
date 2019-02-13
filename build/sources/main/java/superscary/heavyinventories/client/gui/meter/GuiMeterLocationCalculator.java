package superscary.heavyinventories.client.gui.meter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

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
public class GuiMeterLocationCalculator
{

	public static ArrayList<Integer> getBestLocation(Minecraft minecraft)
	{
		ArrayList<Integer> par1 = new ArrayList<>();
		par1.add(getBestLocationX(minecraft));
		par1.add(getBestLocationY(minecraft));
		return par1;
	}

	public static int getBestLocationX(ScaledResolution resolution)
	{
		return -1;
	}

	public static int getBestLocationX(Minecraft minecraft)
	{
		return getBestLocationX(new ScaledResolution(minecraft));
	}

	public static int getBestLocationY(ScaledResolution resolution)
	{
		return -1;
	}

	public static int getBestLocationY(Minecraft minecraft)
	{
		return getBestLocationY(new ScaledResolution(minecraft));
	}

	public static int getScreenSizeX(ScaledResolution resolution)
	{
		return resolution.getScaledWidth();
	}

	public static int getScreenSizeY(ScaledResolution resolution)
	{
		return resolution.getScaledHeight();
	}

}
