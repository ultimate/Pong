package ultimate.pong.math;

public class Vector implements Comparable<Vector>
{
	public static final double TOLERANCE = 0.0001;
	
	protected double	x;
	protected double	y;

	public Vector()
	{
		this(0.0, 0.0);
	}

	public Vector(double x, double y)
	{
		super();
		this.setX(x);
		this.setY(y);
	}

	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	@Override
	public int hashCode()
	{
		Vector rounded = new Vector(x, y).round();
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(rounded.x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rounded.y);
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
		Vector other = (Vector) obj;
		
		return compareTo(other) == 0;
	}

	@Override
	public String toString()
	{
		return "Vector [x=" + x + ", y=" + y + "]";
	}
	
	public Vector round()
	{
		x = Math.round(x / TOLERANCE) * TOLERANCE;
		y = Math.round(y / TOLERANCE) * TOLERANCE;
		return this;
	}
	
	public Vector scale(double factor)
	{
		x *= factor;
		y *= factor;
		return this;
	}
	
	public Vector add(Vector other)
	{
		x += other.x;
		y += other.y;
		return this;
	}

	@Override
	public int compareTo(Vector other)
	{		
		Vector thisRounded = new Vector(x, y).round();
		Vector otherRounded = new Vector(other.x, other.y).round();
		
		if(Math.abs(thisRounded.x - otherRounded.x) > TOLERANCE)
			return (int) Math.signum(thisRounded.x - otherRounded.x);
		if(Math.abs(thisRounded.y - otherRounded.y) > TOLERANCE)
			return (int) Math.signum(thisRounded.y - otherRounded.y);
		return 0;
	}
}
