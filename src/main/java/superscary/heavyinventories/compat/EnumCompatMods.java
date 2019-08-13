package superscary.heavyinventories.compat;

/**
 * All mods that have built in compatibility with HI
 */
public enum EnumCompatMods
{

	BAUBLES("baubles"),
	THE_ONE_PROBE("theoneprobe"),
	WAILA("waila"),
	HWYLA("hwyla"),
	TRAVELLERS_BACKPACK("travellersbackpack"),
	WEARABLE_BACKPACKS("wearablebackpacks");

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
