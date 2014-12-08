package ultimate.pong.math;

import java.util.List;

public interface Polygon
{
	public List<Vector> getPoints();

	public class Impl implements Polygon
	{
		private List<Vector>	points;

		public Impl(List<Vector> points)
		{
			super();
			this.points = points;
		}

		@Override
		public List<Vector> getPoints()
		{
			return this.points;
		}
	}
}
