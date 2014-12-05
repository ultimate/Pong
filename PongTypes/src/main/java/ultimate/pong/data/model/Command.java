package ultimate.pong.data.model;

public class Command
{
	protected double	sliderPosition;
	protected boolean	release;

	public Command()
	{
		super();
	}

	public double getSliderPosition()
	{
		return sliderPosition;
	}

	public void setSliderPosition(double sliderPosition)
	{
		this.sliderPosition = sliderPosition;
	}

	public boolean isRelease()
	{
		return release;
	}

	public void setRelease(boolean release)
	{
		this.release = release;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (release ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(sliderPosition);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if(release != other.release)
			return false;
		if(Double.doubleToLongBits(sliderPosition) != Double.doubleToLongBits(other.sliderPosition))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Command [sliderPosition=" + sliderPosition + ", release=" + release + "]";
	}
}
