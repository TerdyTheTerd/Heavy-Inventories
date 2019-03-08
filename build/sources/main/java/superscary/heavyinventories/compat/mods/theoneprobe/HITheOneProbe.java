package superscary.heavyinventories.compat.mods.theoneprobe;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.ITheOneProbe;
import superscary.heavyinventories.util.Logger;

import javax.annotation.Nullable;

public class HITheOneProbe implements Function<ITheOneProbe, Void>
{

	private static final String NAME = "The One Probe";

	public HITheOneProbe()
	{
		Logger.info("Oh I see you, %s", NAME);
		Logger.info("Loading compat for %s...", NAME);
	}

	@Nullable
	@Override
	public Void apply(ITheOneProbe theOneProbe)
	{
		theOneProbe.registerProvider(new HITOPInfoProvider());
		return null;
	}

}
