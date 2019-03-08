package superscary.heavyinventories.common.capability.offsets;

public class WeightOffset implements IOffset
{

	private double offset;

	@Override
	public double setOffset(double newOffset)
	{
		offset = newOffset;
		return newOffset;
	}

	@Override
	public double getOffset()
	{
		return offset;
	}

}
