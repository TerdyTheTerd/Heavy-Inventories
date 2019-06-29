package superscary.heavyinventories.util;

public enum EnumTagID
{

	WEIGHT("HIWeight"),
	MAX_WEIGHT("HIMaxWeight");

	private String id;
	EnumTagID(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public static String getTagID(EnumTagID enumTagID)
	{
		return enumTagID.getId();
	}

}
