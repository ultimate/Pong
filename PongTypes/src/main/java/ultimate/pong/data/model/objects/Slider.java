package ultimate.pong.data.model.objects;

import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.Position;
import ultimate.pong.enums.EnumObjectType;

public class Slider extends MapObject
{
	protected Position	start;
	protected Position	end;

	public Slider()
	{
		this(new Position(), new Position());
	}

	public Slider(Position start, Position end)
	{
		super(EnumObjectType.slider);
		this.setStart(start);
		this.setEnd(end);
	}

	public Position getStart()
	{
		return start;
	}

	public void setStart(Position start)
	{
		this.start = start;
	}

	public Position getEnd()
	{
		return end;
	}

	public void setEnd(Position end)
	{
		this.end = end;
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
		Slider other = (Slider) obj;
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
}
