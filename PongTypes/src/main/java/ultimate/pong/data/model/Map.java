package ultimate.pong.data.model;

import java.util.ArrayList;
import java.util.List;

public class Map
{
	protected List<MapObject>	objects;

	public Map()
	{
		super();
		this.objects = new ArrayList<MapObject>();
	}

	public List<MapObject> getObjects()
	{
		return objects;
	}

	public void setObjects(List<MapObject> objects)
	{
		this.objects = objects;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objects == null) ? 0 : objects.hashCode());
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
		Map other = (Map) obj;
		if(objects == null)
		{
			if(other.objects != null)
				return false;
		}
		else if(!objects.equals(other.objects))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Map [objects=" + objects + "]";
	}
}
