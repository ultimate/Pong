package ultimate.pong.data.model.objects;

import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.Position;
import ultimate.pong.enums.EnumObjectType;

public class Ball extends MapObject
{
	protected Position	position;
	protected Position	direction;

	public Ball()
	{
		super(EnumObjectType.ball);
	}

	public Position getPosition()
	{
		return position;
	}

	public void setPosition(Position position)
	{
		this.position = position;
	}

	public Position getDirection()
	{
		return direction;
	}

	public void setDirection(Position direction)
	{
		this.direction = direction;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(!super.equals(obj))
			return false;
		if(getClass() != obj.getClass())
			return false;
		Ball other = (Ball) obj;
		if(direction == null)
		{
			if(other.direction != null)
				return false;
		}
		else if(!direction.equals(other.direction))
			return false;
		if(position == null)
		{
			if(other.position != null)
				return false;
		}
		else if(!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Ball [position=" + position + ", direction=" + direction + "]";
	}
}
