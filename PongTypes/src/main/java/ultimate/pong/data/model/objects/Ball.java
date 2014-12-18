package ultimate.pong.data.model.objects;

import java.util.LinkedList;
import java.util.List;

import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.Player;
import ultimate.pong.enums.EnumObjectType;
import ultimate.pong.math.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ball extends MapObject
{
	protected Vector		position;
	protected Vector		direction;

	protected List<Vector>	path;
	// @JsonIgnore
	// protected Vector newPosition;
	// @JsonIgnore
	// protected Vector newDirection;
	@JsonIgnore
	protected Player		lastContact;

	public Ball()
	{
		super(EnumObjectType.ball);
	}

	public Ball(Vector position, Vector direction)
	{
		this();
		this.position = position;
		this.direction = direction;
		this.path = new LinkedList<Vector>();
	}

	public Vector getPosition()
	{
		return position;
	}

	public void setPosition(Vector position)
	{
		this.position = position;
	}

	public Vector getDirection()
	{
		return direction;
	}

	public void setDirection(Vector direction)
	{
		this.direction = direction;
	}

	public List<Vector> getPath()
	{
		return path;
	}

	public void setPath(List<Vector> path)
	{
		this.path = path;
	}

	// public Vector getNewPosition()
	// {
	// return newPosition;
	// }
	//
	// public void setNewPosition(Vector newPosition)
	// {
	// this.newPosition = newPosition;
	// }
	//
	// public Vector getNewDirection()
	// {
	// return newDirection;
	// }
	//
	// public void setNewDirection(Vector newDirection)
	// {
	// this.newDirection = newDirection;
	// }

	public Player getLastContact()
	{
		return lastContact;
	}

	public void setLastContact(Player lastContact)
	{
		this.lastContact = lastContact;
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
