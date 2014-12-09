package ultimate.pong.math;

import junit.framework.TestCase;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;

public class PhysicsTest extends TestCase
{
	public static final double	TOLERANCE	= Vector.TOLERANCE;

	public void testInteract_Ball_Wall() throws Exception
	{
		Wall wall = new Wall(new Vector(-10.0, 1.0), new Vector(+10.0, 1.0));

		Ball ball;
		Vector position;
		Vector direction;
		
		// check no collision
		position = new Vector();
		direction = new Vector();
		ball = new Ball(position, direction);
		
		Physics.interact(ball, wall);
		assertEquals(position, ball.getPosition()); // unchanged
		assertEquals(direction, ball.getDirection()); // unchanged
		
		// check vertical up
		position = new Vector();
		direction = new Vector(0.0, 2.0);
		ball = new Ball(position, direction);
		
		Physics.interact(ball, wall);
		assertEquals(position, ball.getPosition()); // unchanged
		assertEquals(direction.scale(-1), ball.getDirection()); // mirrored
		
		// check 45°
		position = new Vector();
		direction = new Vector(2.0, 2.0);
		ball = new Ball(position, direction);
		
		Physics.interact(ball, wall);
		assertEquals(new Vector(2.0, 0.0), ball.getPosition()); // x += 2
		assertEquals(new Vector(2.0, -2.0), ball.getDirection()); // mirrored
		
		// check 45° (faster)
		position = new Vector();
		direction = new Vector(3.0, 3.0);
		ball = new Ball(position, direction);
		
		Physics.interact(ball, wall);
		assertEquals(new Vector(3.0, -1.0), ball.getPosition()); // x += 3; y -= 1
		assertEquals(new Vector(3.0, -3.0), ball.getDirection()); // mirrored
		
		// check 45° (slower)
		position = new Vector();
		direction = new Vector(1.5, 1.5);
		ball = new Ball(position, direction);
		
		Physics.interact(ball, wall);
		assertEquals(new Vector(1.5, 0.5), ball.getPosition()); // x += 1.5; y += 0.5
		assertEquals(new Vector(1.5, -1.5), ball.getDirection()); // mirrored
	}
	
	public void testInteract_Ball_Slider() throws Exception
	{
		Slider slider = new Slider(new Vector(-1.0, 1.0), new Vector(+1.0, 1.0));

		Ball ball, ball2, ball3;
		Vector position;
		Vector direction;
		
		// check no collision
		position = new Vector();
		direction = new Vector();
		ball = new Ball(position, direction);
		
		Physics.interact(ball, slider);
		assertEquals(position, ball.getPosition()); // unchanged
		assertEquals(direction, ball.getDirection()); // unchanged
		
		// check vertical up // in center
		position = new Vector(0.0, 0.0);
		direction = new Vector(0.0, 2.0);
		ball = new Ball(position, direction);
		
		Physics.interact(ball, slider);
		assertEquals(position, ball.getPosition()); // unchanged
		assertEquals(direction.scale(-1), ball.getDirection()); // mirrored
		
		double lastY = Double.POSITIVE_INFINITY;
		for(double x = -9; x < 10; x++)
		{
			// check vertical up // with offset from center
			position = new Vector(x/10.0, 0.0);
			direction = new Vector(0.0, 2.0);
			ball = new Ball(position, direction);
			
			Physics.interact(ball, slider);
			System.out.println(ball.getPosition() + " - " + ball.getDirection());
			
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

			position = new Vector(x/10.0 - 1.0, 0.0);
			direction = new Vector(1 + sqrt05, 1 + sqrt05); 
			ball2 = new Ball(position, direction);
			
			Physics.interact(ball2, slider);
			System.out.println(ball2.getPosition() + " - " + ball2.getDirection());
			
			assertEquals(ball.getPosition(), ball2.getPosition());
			
			// check -45° 
			// --> should result in same position
			// --> and same speed as +45°

			position = new Vector(x/10.0 + 1.0, 0.0);
			direction = new Vector(-(1 + sqrt05), 1 + sqrt05); 
			ball3 = new Ball(position, direction);
			
			Physics.interact(ball3, slider);
			System.out.println(ball3.getPosition() + " - " + ball3.getDirection());
			
			assertEquals(ball.getPosition(), ball2.getPosition());
			assertEquals(ball2.getDirection(), ball3.getDirection());
			
			System.out.println();
		}
	}
}
