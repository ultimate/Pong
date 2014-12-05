package ultimate.pong.data.model;

import ultimate.pong.enums.EnumType;

public class MapObject
{
	protected Integer	id;
	protected String	name;
	protected EnumType	type;
	protected Color		color;

	public MapObject()
	{
		this(null);
	}

	public MapObject(EnumType type)
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

	public EnumType getType()
	{
		return type;
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

}
