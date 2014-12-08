package ultimate.pong.data.model;

import java.util.ArrayList;
import java.util.List;

import ultimate.pong.data.model.objects.Slider;

public class Player
{
	protected Integer		id;
	protected String		name;
	protected Color			color;
	protected int			score;
	protected boolean		ready;
	protected boolean		connected;
	protected boolean		ball;
	// protected boolean last;
	protected Slider		slider;
	protected double		sliderSize;
	protected double		sliderPosition;

	protected List<Command>	commands;

	public Player()
	{
		super();
		this.color = new Color();
		this.slider = new Slider();
		this.commands = new ArrayList<Command>();
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public boolean isReady()
	{
		return ready;
	}

	public void setReady(boolean ready)
	{
		this.ready = ready;
	}

	public boolean isConnected()
	{
		return connected;
	}

	public void setConnected(boolean connected)
	{
		this.connected = connected;
	}

	public boolean isBall()
	{
		return ball;
	}

	public void setBall(boolean ball)
	{
		this.ball = ball;
	}

	// public boolean isLast()
	// {
	// return last;
	// }
	//
	// public void setLast(boolean last)
	// {
	// this.last = last;
	// }

	public Slider getSlider()
	{
		return slider;
	}

	public void setSlider(Slider slider)
	{
		this.slider = slider;
	}

	public double getSliderSize()
	{
		return sliderSize;
	}

	public void setSliderSize(double sliderSize)
	{
		this.sliderSize = sliderSize;
	}

	public double getSliderPosition()
	{
		return sliderPosition;
	}

	public void setSliderPosition(double sliderPosition)
	{
		this.sliderPosition = sliderPosition;
	}

	public List<Command> getCommands()
	{
		return commands;
	}

	public void setCommands(List<Command> commands)
	{
		this.commands = commands;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (ball ? 1231 : 1237);
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((commands == null) ? 0 : commands.hashCode());
		result = prime * result + (connected ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		// result = prime * result + (last ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (ready ? 1231 : 1237);
		result = prime * result + score;
		result = prime * result + ((slider == null) ? 0 : slider.hashCode());
		long temp;
		temp = Double.doubleToLongBits(sliderPosition);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sliderSize);
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
		Player other = (Player) obj;
		if(ball != other.ball)
			return false;
		if(color == null)
		{
			if(other.color != null)
				return false;
		}
		else if(!color.equals(other.color))
			return false;
		if(commands == null)
		{
			if(other.commands != null)
				return false;
		}
		else if(!commands.equals(other.commands))
			return false;
		if(connected != other.connected)
			return false;
		if(id == null)
		{
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		// if(last != other.last)
		// return false;
		if(name == null)
		{
			if(other.name != null)
				return false;
		}
		else if(!name.equals(other.name))
			return false;
		if(ready != other.ready)
			return false;
		if(score != other.score)
			return false;
		if(slider == null)
		{
			if(other.slider != null)
				return false;
		}
		else if(!slider.equals(other.slider))
			return false;
		if(Double.doubleToLongBits(sliderPosition) != Double.doubleToLongBits(other.sliderPosition))
			return false;
		if(Double.doubleToLongBits(sliderSize) != Double.doubleToLongBits(other.sliderSize))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Player [id=" + id + ", name=" + name + ", color=" + color + ", score=" + score + ", ready=" + ready + ", connected=" + connected
				+ ", ball=" + ball + ", slider=" + slider + ", sliderSize=" + sliderSize + ", sliderPosition=" + sliderPosition + ", commands="
				+ commands + "]";
	}

	public synchronized void increaseScore(int amount)
	{
		this.score += amount;
	}

	public synchronized void decreaseScore(int amount)
	{
		this.score -= amount;
	}
}
