package ultimate.pong.math;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;
import ultimate.pong.math.Geometry.Intersection;

public abstract class Physics
{
	protected transient static final Logger	logger				= LoggerFactory.getLogger(Physics.class);

	public static final double				ELLIPSIS_RATIO_A	= 1.0;
	public static final double				ELLIPSIS_RATIO_B	= 0.5;

	private Physics()
	{

	}

	public static void interact(Ball ball, List<MapObject> objects)
	{
		ball.getPath().add(new Vector(ball.getPosition()).add(ball.getDirection())); // theoretic ball end
		
		List<Intersection> tmpIntersections;
		Intersection intersection;
		MapObject intersectingObject;
		
		// process objects the following way:
		// 1. get the nearest intersection
		// if(intersection)
		//   2. handle
		//	 3. go to 1
		// else
		// 	 end
		do
		{
			intersection = null;
			intersectingObject = null;
			
			// consider last section of ball line, only
			Vector sectionStart = ball.getPath().get(ball.getPath().size()-2);
			Vector sectionEnd = ball.getPath().get(ball.getPath().size()-1);
			Polygon section = new Polygon.Impl(Arrays.asList(new Vector[] {sectionStart, sectionEnd}), true);
			
			for(MapObject other: objects)
			{
				if(other instanceof Polygon)
				{
					tmpIntersections = Geometry.intersect2(section, (Polygon) other);
					
					for(Intersection i: tmpIntersections)
					{
						logger.debug("intersection found @ " + i.getPoint() + " (p=" + i.getPosition1() + ")");
						if(intersection == null || (intersection != null && i.getPosition1() < intersection.getPosition1()))
						{
							intersection = i;
							intersectingObject = other;
						}
					}
				}
				// TODO other kind of objects (e.g. Ball)
				
			}			
			
			// handle intersection
			if(intersectingObject != null)
			{
				Vector newPosition = null;
				Vector newDirection = null;
				
				if(intersectingObject instanceof Wall)
				{
					Wall wall = (Wall) intersectingObject;
					Vector wallVector = new Vector(wall.getEnd()).sub(wall.getStart());
	
					// motion parts before and after collision
					Vector part1 = new Vector(intersection.getPoint()).sub(sectionStart); // start -> intersection
					Vector part2 = new Vector(sectionEnd).sub(intersection.getPoint()); // intersection -> end
	
					// new direction
					newDirection = Geometry.mirror(ball.getDirection(), wallVector);
	
					// new position
					Vector part2Mirrored = Geometry.mirror(part2, wallVector); // rebounce
					newPosition = new Vector(sectionStart).add(part1).add(part2Mirrored);
				}
				else if(intersectingObject instanceof Slider)
				{
					Slider slider = (Slider) intersectingObject;

					// theoretical slider vector if it was a wall
					Vector sliderVector = new Vector(slider.getEnd()).sub(slider.getStart());
					Vector sliderNormal = Geometry.normal(sliderVector);

					// motion parts before and after collision
					Vector part1 = new Vector(intersection.getPoint()).sub(sectionStart); // start -> intersection
					Vector part2 = new Vector(sectionEnd).sub(intersection.getPoint()); // intersection -> end

					// slider mimics behavior of an ellipsis with
					// x = tangential direction (of slider)
					// y = normal direction (of slider)
					// ball is reflected in fixed direction independently from incoming direction
					// only depending on position of collision

					// get offset of intersection point from slider center
					Vector sliderCenter = Geometry.center(slider.getStart(), slider.getEnd());
					Vector collisionOffset = new Vector(intersection.getPoint()).sub(sliderCenter);
					// get tangential component (x) = length of vector
					double x = collisionOffset.length();
					double a = sliderVector.length() / 2 * ELLIPSIS_RATIO_A;
					double b = a * ELLIPSIS_RATIO_B;
					// theoretical point on ellipsis at dt
					// x^2/a^2 + y^2/b^2 = 1
					double y = Math.sqrt((1 - (x * x) / (a * a)) * (b * b));
					// consider on which side of the slider the ball is!
					if(collisionOffset.dot(sliderVector) < 0)
						x = -x; // collision point is in "left half" of slider
					if(part2.dot(sliderNormal) > 0)
						y = -y; // ball comes from "below" the slider

					// calculate normal vector at dt|dn
					Vector normal = new Vector(b * x / a, a * y / b);
					// get normal for real Normal
					// realNormal = t_x * normed(sliderVector) + t_y * normed(sliderNormal)
					Vector realNormal = new Vector(sliderVector).norm().scale(normal.getX()).add(new Vector(sliderNormal).norm().scale(normal.getY()));

					// new direction
					newDirection = new Vector(realNormal).norm().scale(ball.getDirection().length());

					// new position
					Vector part2Normal = new Vector(realNormal).norm().scale(part2.length());
					newPosition = new Vector(ball.getPosition()).add(part1).add(part2Normal);
				}
				// TODO other kind of objects (e.g. Ball)
				
				// update ball path
				ball.getPath().remove(ball.getPath().size()-1); // remove last
				ball.getPath().add(intersection.getPoint()); // add intersection point
				if(newPosition != null)
					ball.getPath().add(newPosition); // add new end of motion
				
				// update direction
				if(newDirection != null)
					ball.setDirection(newDirection);
			}			
		} while(intersectingObject != null);
		
		// update ball position to end of path
		ball.setPosition(ball.getPath().get(ball.getPath().size()-1));
	}
}
