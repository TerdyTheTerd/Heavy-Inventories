package superscary.heavyinventories.compat;

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
public enum EnumCompatMods
{

	BAUBLES("baubles"),
	THE_ONE_PROBE("theoneprobe"),
	WAILA("waila");

	private final String compatModid;
	EnumCompatMods(String compatModid)
	{
		this.compatModid = compatModid;
	}

	public final String getCompatModid()
	{
		return compatModid;
	}

}