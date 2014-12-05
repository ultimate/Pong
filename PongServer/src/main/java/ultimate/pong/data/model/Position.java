package ultimate.pong.data.model;

public class Position
{
	protected double	x;
	protected double	y;

	public Position()
	{
		this(0.0, 0.0);
	}

	public Position(double x, double y)
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
		if(x > 1.0 || x < -1.0)
			throw new IllegalArgumentException("x must be in range of -1.0 to 1.0");
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		if(y > 1.0 || y < -1.0)
			throw new IllegalArgumentException("y must be in range of -1.0 to 1.0");
		this.y = y;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
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
		Position other = (Position) obj;
		if(Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if(Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Position [x=" + x + ", y=" + y + "]";
	}
}
