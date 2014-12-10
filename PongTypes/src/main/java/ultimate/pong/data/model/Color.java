package ultimate.pong.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Color
{
	public final static Color	WHITE		= new Color(255, 255, 255);
	public final static Color	LIGHT_GRAY	= new Color(192, 192, 192);
	public final static Color	GRAY		= new Color(128, 128, 128);
	public final static Color	DARK_GRAY	= new Color(64, 64, 64);
	public final static Color	BLACK		= new Color(0, 0, 0);
	public final static Color	RED			= new Color(255, 0, 0);
	public final static Color	PINK		= new Color(255, 175, 175);
	public final static Color	ORANGE		= new Color(255, 200, 0);
	public final static Color	YELLOW		= new Color(255, 255, 0);
	public final static Color	GREEN		= new Color(0, 255, 0);
	public final static Color	MAGENTA		= new Color(255, 0, 255);
	public final static Color	CYAN		= new Color(0, 255, 255);
	public final static Color	BLUE		= new Color(0, 0, 255);

	protected int				r;
	protected int				g;
	protected int				b;

	public Color()
	{
		this(0, 0, 0);
	}

	public Color(int r, int g, int b)
	{
		super();
		this.setR(r);
		this.setG(g);
		this.setB(b);
	}

	public int getR()
	{
		return r;
	}

	public void setR(int r)
	{
		if(r < 0 || r > 255)
			throw new IllegalArgumentException("r must be within range of 0 to 255");
		this.r = r;
	}

	public int getG()
	{
		return g;
	}

	public void setG(int g)
	{
		if(g < 0 || g > 255)
			throw new IllegalArgumentException("g must be within range of 0 to 255");
		this.g = g;
	}

	public int getB()
	{
		return b;
	}

	public void setB(int b)
	{
		if(b < 0 || b > 255)
			throw new IllegalArgumentException("b must be within range of 0 to 255");
		this.b = b;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
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
		Color other = (Color) obj;
		if(b != other.b)
			return false;
		if(g != other.g)
			return false;
		if(r != other.r)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Color [r=" + r + ", g=" + g + ", b=" + b + "]";
	}
}
