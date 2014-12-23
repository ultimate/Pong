package ultimate.pong.data.model.objects;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ultimate.pong.data.model.MapObject;
import ultimate.pong.enums.EnumObjectType;
import ultimate.pong.math.Polygon;
import ultimate.pong.math.Vector;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wall extends MapObject implements Polygon, Damper
{
	protected Vector	start;
	protected Vector	end;
	
	@JsonIgnore
	protected double damping;

	public Wall()
	{
		this(new Vector(), new Vector());
	}

	public Wall(Vector start, Vector end)
	{
		super(EnumObjectType.wall);
		this.setStart(start);
		this.setEnd(end);
	}

	public Vector getStart()
	{
		return start;
	}

	public void setStart(Vector start)
	{
		this.start = start;
	}

	public Vector getEnd()
	{
		return end;
	}

	public void setEnd(Vector end)
	{
		this.end = end;
	}

	public double getDamping()
	{
		return damping;
	}

	public void setDamping(double damping)
	{
		this.damping = damping;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		Wall other = (Wall) obj;
		if(end == null)
		{
			if(other.end != null)
				return false;
		}
		else if(!end.equals(other.end))
			return false;
		if(start == null)
		{
			if(other.start != null)
				return false;
		}
		else if(!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Slider [start=" + start + ", end=" + end + "]";
	}

	@JsonIgnore
	@Override
	public List<Vector> getPoints()
	{
		return Arrays.asList(new Vector[] {start, end});
	}

	@JsonIgnore
	@Override
	public boolean isOpen()
	{
		return true;
	}
}
