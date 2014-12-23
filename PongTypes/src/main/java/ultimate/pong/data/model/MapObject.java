package ultimate.pong.data.model;

import java.util.ArrayList;
import java.util.List;

import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;
import ultimate.pong.enums.EnumObjectType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapObject
{
	protected Integer				id;
	protected String				name;
	protected EnumObjectType		type;
	protected Color					color;
	protected List<EnumObjectType>	collisions	= new ArrayList<EnumObjectType>();

	public MapObject()
	{
		this(null);
	}

	public MapObject(EnumObjectType type)
	{
		super();
		this.type = type;
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

	public EnumObjectType getType()
	{
		return type;
	}

	public List<EnumObjectType> getCollisions()
	{
		return collisions;
	}

	public void setCollisions(List<EnumObjectType> collisions)
	{
		this.collisions = collisions;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MapObject other = (MapObject) obj;
		if(color == null)
		{
			if(other.color != null)
				return false;
		}
		else if(!color.equals(other.color))
			return false;
		if(id == null)
		{
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		if(name == null)
		{
			if(other.name != null)
				return false;
		}
		else if(!name.equals(other.name))
			return false;
		if(type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "MapObject [id=" + id + ", name=" + name + ", type=" + type + ", color=" + color + "]";
	}
	
	@JsonCreator
	public static MapObject fromType(@JsonProperty("type") EnumObjectType type)
	{
		switch(type)
		{
			case ball:
				return new Ball();
			case slider:
				return new Slider();
			case wall:
				return new Wall();
			default:
				return new MapObject();
		}
	}
}
