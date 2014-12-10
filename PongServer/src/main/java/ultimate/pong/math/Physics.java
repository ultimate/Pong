package ultimate.pong.math;

import java.util.Arrays;
import java.util.List;

import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;

public abstract class Physics
{
	public static final double ELLIPSIS_RATIO_A = 1.0;
	public static final double ELLIPSIS_RATIO_B = 0.5;
	
	private Physics()
	{

	}
	
	public static void interact(Ball ball, MapObject other)
	{
		Vector newPosition;
		Vector newDirection;

		Vector ballStart = new Vector(ball.getPosition());
		Vector ballEnd = new Vector(ballStart).add(ball.getDirection());
		Polygon ballLine = new Polygon.Impl(Arrays.asList(new Vector[] { ballStart, ballEnd }));

		List<Vector> intersections;

		if(other instanceof Wall)
		{
			Wall wall = (Wall) other;
			// check collision by calculating intersection
			intersections = Geometry.intersect(wall, ballLine);
			// should have 1 or 0 intersections
			if(intersections.size() == 0)
				return; // no collision			
			
			Vector wallVector = new Vector(wall.getEnd()).sub(wall.getStart());
			
			// motion parts before and after collision
			Vector part1 = new Vector(intersections.get(0)).sub(ballStart); // start -> intersection
			Vector part2 = new Vector(ballEnd).sub(intersections.get(0));	// intersection -> end
		
			// new direction
			newDirection = Geometry.mirror(ball.getDirection(), wallVector);
			
			// new position
			Vector part2Mirrored = Geometry.mirror(part2, wallVector);		// rebounce
			newPosition = new Vector(ball.getPosition()).add(part1).add(part2Mirrored);
		}
		else if(other instanceof Slider)
		{
			Slider slider = (Slider) other;
			// check collision by calculating intersection
			intersections = Geometry.intersect(slider, ballLine);
			// should have 1 or 0 intersections
			if(intersections.size() == 0)
				return; // no collision
				
			// theoretical slider vector if it was a wall
			Vector sliderVector = new Vector(slider.getEnd()).sub(slider.getStart());
			Vector sliderNormal = Geometry.normal(sliderVector);
			
			// motion parts before and after collision
			Vector part1 = new Vector(intersections.get(0)).sub(ballStart); // start -> intersection
			Vector part2 = new Vector(ballEnd).sub(intersections.get(0));	// intersection -> end
			
			// slider mimics behavior of an ellipsis with 
			// x = tangential direction (of slider)
			// y = normal direction (of slider)
			// ball is reflected in fixed direction independently from incoming direction
			// only depending on position of collision
			
			// get offset of intersection point from slider center
			Vector sliderCenter = Geometry.center(slider.getStart(), slider.getEnd());
			Vector collisionOffset = new Vector(intersections.get(0)).sub(sliderCenter);
			// get tangential component (x) = length of vector
			double x = collisionOffset.length();
			double a = sliderVector.length() / 2 * ELLIPSIS_RATIO_A;
			double b = a * ELLIPSIS_RATIO_B;
			// theoretical point on ellipsis at dt
			// x^2/a^2 + y^2/b^2 = 1
			double y = Math.sqrt((1 - (x*x)/(a*a))*(b*b));
			// consider on which side of the slider the ball is!
			if(collisionOffset.dot(sliderVector) < 0)
				x = -x; // collision point is in "left half" of slider
			if(part2.dot(sliderNormal) > 0)
				y = -y; // ball comes from "below" the slider
			
			// calculate normal vector at dt|dn
			Vector normal = new Vector(b*x/a, a*y/b);
			// get normal for real Normal
			// realNormal = t_x * normed(sliderVector) + t_y * normed(sliderNormal)
			Vector realNormal = new Vector(sliderVector).norm().scale(normal.getX()).add(new Vector(sliderNormal).norm().scale(normal.getY()));
						
			// new direction
			newDirection = new Vector(realNormal).norm().scale(ball.getDirection().length());
			
			// new position
			Vector part2Normal = new Vector(realNormal).norm().scale(part2.length());
			newPosition = new Vector(ball.getPosition()).add(part1).add(part2Normal);
		}
//		else if(other instanceof Ball)
//		{
//			// check collision
//		}
		else
		{
			return;
		}
		
		ball.setPosition(newPosition);
		ball.setDirection(newDirection);
		ball.getCollisions().add(other.getType());
		
		other.getCollisions().add(ball.getType());
	}

	public static void interact(List<MapObject> objects)
	{
		MapObject obj1, obj2;
		for(int i1 = 0; i1 < objects.size(); i1++)
		{
			obj1 = objects.get(i1);
			for(int i2 = i1 + 1; i2 < objects.size(); i2++)
			{
				obj2 = objects.get(i2);

				if(obj1 instanceof Ball)
				{
					interact((Ball) obj1, obj2);
				}
				else if(obj2 instanceof Ball)
				{
					interact((Ball) obj2, obj1);
				}
			}
		}
	}
}
