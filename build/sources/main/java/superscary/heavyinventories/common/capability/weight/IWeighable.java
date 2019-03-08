package superscary.heavyinventories.common.capability.weight;

public interface IWeighable
{

	double getWeight();
	double getMaxWeight();
	boolean isEncumbered();
	boolean isOverEncumbered();
	double getRelativeWeight();
	double getStamina();
	double getMaxStamina();
	double getSpeed();
	double getMaxSpeed();

	void increaseMaxWeight(double weight);
	void decreaseMaxWeight(double weight);
	void setMaxWeight(double weight);

	void increaseWeight(double weight);
	void decreaseWeight(double weight);
	void setWeight(double weight);

	void setEncumbered(boolean bool);
	void setOverEncumbered(boolean bool);

}
