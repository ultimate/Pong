package ultimate.pong.math;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;
import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;

public class PhysicsTest extends TestCase
{
	protected transient final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final double	TOLERANCE	= Vector.TOLERANCE;

	public void testInteract_Ball_single_Wall() throws Exception
	{
		Wall wall = new Wall(new Vector(-10.0, 1.0), new Vector(+10.0, 1.0));
		List<MapObject> objects = Arrays.asList(new MapObject[] { wall });

		Ball ball;
		Vector position;
		Vector direction;

		// check no collision
		position = new Vector();
		direction = new Vector();
		ball = new Ball(position, direction);

		Physics.interact(ball, objects);
		assertEquals(position, ball.getPosition()); // unchanged
		assertEquals(direction, ball.getDirection()); // unchanged

		// check vertical up
		position = new Vector();
		direction = new Vector(0.0, 2.0);
		ball = new Ball(position, direction);

		Physics.interact(ball, objects);
		assertEquals(position, ball.getPosition()); // unchanged
		assertEquals(direction.scale(-1), ball.getDirection()); // mirrored

		// check 45°
		position = new Vector();
		direction = new Vector(2.0, 2.0);
		ball = new Ball(position, direction);

		Physics.interact(ball, objects);
		assertEquals(new Vector(2.0, 0.0), ball.getPosition()); // x += 2
		assertEquals(new Vector(2.0, -2.0), ball.getDirection()); // mirrored

		// check 45° (faster)
		position = new Vector();
		direction = new Vector(3.0, 3.0);
		ball = new Ball(position, direction);

		Physics.interact(ball, objects);
		assertEquals(new Vector(3.0, -1.0), ball.getPosition()); // x += 3; y -= 1
		assertEquals(new Vector(3.0, -3.0), ball.getDirection()); // mirrored

		// check 45° (slower)
		position = new Vector();
		direction = new Vector(1.5, 1.5);
		ball = new Ball(position, direction);

		Physics.interact(ball, objects);
		assertEquals(new Vector(1.5, 0.5), ball.getPosition()); // x += 1.5; y += 0.5
		assertEquals(new Vector(1.5, -1.5), ball.getDirection()); // mirrored
	}

	public void testInteract_Ball_multi_Wall() throws Exception
	{
		Wall wall1 = new Wall(new Vector(-1.0, -1.0), new Vector(-1.0, 1.0));
		Wall wall2 = new Wall(new Vector(-1.0, 1.0), new Vector(1.0, 1.0));
		Wall wall3 = new Wall(new Vector(1.0, 1.0), new Vector(1.0, -1.0));
		List<MapObject> objects = Arrays.asList(new MapObject[] { wall1, wall2, wall3 });

		// @formatter:off
		/*
		 * Scenario
		 * 
		 *   xxxoxxx
		 *   x o o x
		 *   xo   ox
		 *   o  0  o
		 *   xo   ox
		 *   x o o x
		 *   x  o  x
		 *     o o
		 *    o   o
		 *   S     E
		 *   
		 */
		// @formatter:on
		
		Vector position = new Vector(-1.0, -2.0);
		Vector direction = new Vector(6.0, 6.0);
		Ball ball = new Ball(position, direction);
		
		Vector expectedPosition = new Vector(1.0, -2.0);
		Vector expectedDirection = new Vector(6.0, -6.0);

		Physics.interact(ball, objects);
		assertEquals(expectedPosition, ball.getPosition());
		assertEquals(expectedDirection, ball.getDirection());

		assertEquals(5, ball.getPath().size());
		assertEquals(new Vector(position), ball.getPath().get(0));
		assertEquals(new Vector(1.0, 0.0), ball.getPath().get(1));
		assertEquals(new Vector(0.0, 1.0), ball.getPath().get(2));
		assertEquals(new Vector(-1.0, 0.0), ball.getPath().get(3));
		assertEquals(new Vector(expectedPosition), ball.getPath().get(4));
	}
	
	public void testInteract_Ball_Slider() throws Exception
	{
		Slider slider = new Slider(new Vector(-1.0, 1.0), new Vector(+1.0, 1.0));
		List<MapObject> objects = Arrays.asList(new MapObject[] { slider });

		Ball ball, ball2, ball3;
		Vector position;
		Vector direction;

		// check no collision
		position = new Vector();
		direction = new Vector();
		ball = new Ball(position, direction);

		Physics.interact(ball, objects);
		assertEquals(position, ball.getPosition()); // unchanged
		assertEquals(direction, ball.getDirection()); // unchanged

		// check vertical up // in center
		position = new Vector(0.0, 0.0);
		direction = new Vector(0.0, 2.0);
		ball = new Ball(position, direction);

		Physics.interact(ball, objects);
		assertEquals(position, ball.getPosition()); // unchanged
		assertEquals(direction.scale(-1), ball.getDirection()); // mirrored

		double lastY = Double.POSITIVE_INFINITY;
		for(double x = -9; x < 10; x++)
		{
			// check vertical up // with offset from center
			position = new Vector(x / 10.0, 0.0);
			direction = new Vector(0.0, 2.0);
			ball = new Ball(position, direction);

			Physics.interact(ball, objects);
			logger.debug(ball.getPosition() + " - " + ball.getDirection());

			// check plausibility
			if(x < 0)
			{
				// left of center
				assertTrue(ball.getPosition().getX() < position.getX());
				assertTrue(ball.getPosition().getY() > 0);
				assertTrue(ball.getPosition().getY() < lastY);
			}
			else if(x > 0)
			{
				// right of center
				assertTrue(ball.getPosition().getX() > position.getX());
				assertTrue(ball.getPosition().getY() > 0);
				assertTrue(ball.getPosition().getY() > lastY);
			}
			else
			{
				assertEquals(position, ball.getPosition());
			}

			lastY = ball.getPosition().getY();

			double sqrt05 = Math.sqrt(0.5);

			// check 45° from different position but with same collision point
			// and same remaining part2 after collision as above
			// --> should result in same position (but different speed)

			position = new Vector(x / 10.0 - 1.0, 0.0);
			direction = new Vector(1 + sqrt05, 1 + sqrt05);
			ball2 = new Ball(position, direction);

			Physics.interact(ball2, objects);
			logger.debug(ball2.getPosition() + " - " + ball2.getDirection());

			assertEquals(ball.getPosition(), ball2.getPosition());

			// check -45°
			// --> should result in same position
			// --> and same speed as +45°

			position = new Vector(x / 10.0 + 1.0, 0.0);
			direction = new Vector(-(1 + sqrt05), 1 + sqrt05);
			ball3 = new Ball(position, direction);

			Physics.interact(ball3, objects);
			logger.debug(ball3.getPosition() + " - " + ball3.getDirection());

			assertEquals(ball.getPosition(), ball2.getPosition());
			assertEquals(ball2.getDirection(), ball3.getDirection());

			logger.debug("");
		}
	}
}
