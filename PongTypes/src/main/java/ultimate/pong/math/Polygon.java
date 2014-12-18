package ultimate.pong.math;

import java.util.List;

public interface Polygon
{
	public List<Vector> getPoints();

	public boolean isOpen();

	public class Impl implements Polygon
	{
		private List<Vector>	points;
		private boolean			open;

		public Impl(List<Vector> points)
		{
			this(points, false);
		}

		public Impl(List<Vector> points, boolean open)
		{
			super();
			this.points = points;
			this.open = open;
		}

		@Override
		public List<Vector> getPoints()
		{
			return this.points;
		}

		@Override
		public boolean isOpen()
		{
			return this.open;
		}
	}
}
