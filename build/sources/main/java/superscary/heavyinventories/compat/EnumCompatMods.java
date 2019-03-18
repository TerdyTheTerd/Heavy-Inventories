package superscary.heavyinventories.compat;

public enum EnumCompatMods
{

	BAUBLES("baubles"),
	THE_ONE_PROBE("theoneprobe"),
	WAILA("waila"),
	HWYLA("hwyla");

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
