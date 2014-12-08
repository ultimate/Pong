package ultimate.pong.math;

import java.util.Arrays;

import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;

public abstract class Physics
{
	private Physics()
	{

	}

	public static Vector getInfluence(MapObject obj, Ball ball)
	{
		Vector influence;

		Vector ballStart = ball.getPosition();
		Vector ballEnd = ball.getDirection();
		Polygon ballLine = new Polygon.Impl(Arrays.asList(new Vector[] {ballStart, ballEnd}));
		
		Set<Vector> intersections;
		
		if(obj instanceof Wall)
		{
			intersections = Geometry.intersect((Wall) obj, ballLine);
			
			// TODO;
		}
		else if(obj instanceof Slider)
		{
			
		}
		else
		{
			influence = new Vector(0.0, 0.0);
		}
		return influence;
	}
}
