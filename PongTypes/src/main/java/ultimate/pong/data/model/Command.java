package ultimate.pong.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Command
{
	@JsonIgnore
	protected Player	player;
	protected double	sliderPosition;
	protected boolean	release;
	@JsonIgnore
	protected int		tick;

	public Command()
	{
		super();
	}

	public Command(Player player, double sliderPosition, boolean release)
	{
		super();
		this.player = player;
		this.sliderPosition = sliderPosition;
		this.release = release;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
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

	public int getTick()
	{
		return tick;
	}

	public void setTick(int tick)
	{
		this.tick = tick;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + (release ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(sliderPosition);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + tick;
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
		if(player == null)
		{
			if(other.player != null)
				return false;
		}
		else if(!player.equals(other.player))
			return false;
		if(release != other.release)
			return false;
		if(Double.doubleToLongBits(sliderPosition) != Double.doubleToLongBits(other.sliderPosition))
			return false;
		if(tick != other.tick)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Command [player=" + player + ", sliderPosition=" + sliderPosition + ", release=" + release + ", tick=" + tick + "]";
	}
}
