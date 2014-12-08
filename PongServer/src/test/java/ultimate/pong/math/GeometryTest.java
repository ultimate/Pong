package ultimate.pong.math;

import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;

public class GeometryTest extends TestCase
{
	public static final double	TOLERANCE	= Vector.TOLERANCE;

	public void testGetSectorSize() throws Exception
	{
		// corners = 4
		assertEquals(Math.PI / 2, Geometry.getSectorSize(4), TOLERANCE);
		// corners = 6
		assertEquals(Math.PI / 3, Geometry.getSectorSize(6), TOLERANCE);
		// corners = 8
		assertEquals(Math.PI / 4, Geometry.getSectorSize(8), TOLERANCE);
	}

	public void testGetStartCornerPoint() throws Exception
	{
		// corners = 4
		assertEquals(new Vector(+0.7071, -0.7071), Geometry.getStartCornerPoint(4, 0));
		assertEquals(new Vector(+0.7071, +0.7071), Geometry.getStartCornerPoint(4, 1));
		assertEquals(new Vector(-0.7071, +0.7071), Geometry.getStartCornerPoint(4, 2));
		assertEquals(new Vector(-0.7071, -0.7071), Geometry.getStartCornerPoint(4, 3));
		// corners = 6
		assertEquals(new Vector(+0.8660, -0.5), Geometry.getStartCornerPoint(6, 0));
		assertEquals(new Vector(+0.8660, +0.5), Geometry.getStartCornerPoint(6, 1));
		assertEquals(new Vector(0.0, +1.0), Geometry.getStartCornerPoint(6, 2));
		assertEquals(new Vector(-0.8660, +0.5), Geometry.getStartCornerPoint(6, 3));
		assertEquals(new Vector(-0.8660, -0.5), Geometry.getStartCornerPoint(6, 4));
		assertEquals(new Vector(0.0, -1.0), Geometry.getStartCornerPoint(6, 5));
		// corners = 8
		assertEquals(new Vector(+0.9239, -0.3827), Geometry.getStartCornerPoint(8, 0));
		assertEquals(new Vector(+0.9239, +0.3827), Geometry.getStartCornerPoint(8, 1));
		assertEquals(new Vector(+0.3827, +0.9239), Geometry.getStartCornerPoint(8, 2));
		assertEquals(new Vector(-0.3827, +0.9239), Geometry.getStartCornerPoint(8, 3));
		assertEquals(new Vector(-0.9239, +0.3827), Geometry.getStartCornerPoint(8, 4));
		assertEquals(new Vector(-0.9239, -0.3827), Geometry.getStartCornerPoint(8, 5));
		assertEquals(new Vector(-0.3827, -0.9239), Geometry.getStartCornerPoint(8, 6));
		assertEquals(new Vector(+0.3827, -0.9239), Geometry.getStartCornerPoint(8, 7));
	}

	public void testGetEndCornerPoint() throws Exception
	{
		// Geometry.getStartCornerPoint(corners, i) = Geometry.getEndCornerPoint(corners, i-1)

		// corners = 4
		assertEquals(Geometry.getStartCornerPoint(4, 1), Geometry.getEndCornerPoint(4, 0));
		assertEquals(Geometry.getStartCornerPoint(4, 2), Geometry.getEndCornerPoint(4, 1));
		assertEquals(Geometry.getStartCornerPoint(4, 3), Geometry.getEndCornerPoint(4, 2));
		assertEquals(Geometry.getStartCornerPoint(4, 0), Geometry.getEndCornerPoint(4, 3));
		// corners = 6
		assertEquals(Geometry.getStartCornerPoint(6, 1), Geometry.getEndCornerPoint(6, 0));
		assertEquals(Geometry.getStartCornerPoint(6, 2), Geometry.getEndCornerPoint(6, 1));
		assertEquals(Geometry.getStartCornerPoint(6, 3), Geometry.getEndCornerPoint(6, 2));
		assertEquals(Geometry.getStartCornerPoint(6, 4), Geometry.getEndCornerPoint(6, 3));
		assertEquals(Geometry.getStartCornerPoint(6, 5), Geometry.getEndCornerPoint(6, 4));
		assertEquals(Geometry.getStartCornerPoint(6, 0), Geometry.getEndCornerPoint(6, 5));
		// corners = 8
		assertEquals(Geometry.getStartCornerPoint(8, 1), Geometry.getEndCornerPoint(8, 0));
		assertEquals(Geometry.getStartCornerPoint(8, 2), Geometry.getEndCornerPoint(8, 1));
		assertEquals(Geometry.getStartCornerPoint(8, 3), Geometry.getEndCornerPoint(8, 2));
		assertEquals(Geometry.getStartCornerPoint(8, 4), Geometry.getEndCornerPoint(8, 3));
		assertEquals(Geometry.getStartCornerPoint(8, 5), Geometry.getEndCornerPoint(8, 4));
		assertEquals(Geometry.getStartCornerPoint(8, 6), Geometry.getEndCornerPoint(8, 5));
		assertEquals(Geometry.getStartCornerPoint(8, 7), Geometry.getEndCornerPoint(8, 6));
		assertEquals(Geometry.getStartCornerPoint(8, 0), Geometry.getEndCornerPoint(8, 7));
	}

	public void testGetCenterPoint() throws Exception
	{
		// corners = 4
		assertEquals(new Vector(+0.7071, 0.0), Geometry.getCenterPoint(4, 0));
		assertEquals(new Vector(0.0, +0.7071), Geometry.getCenterPoint(4, 1));
		assertEquals(new Vector(-0.7071, 0.0), Geometry.getCenterPoint(4, 2));
		assertEquals(new Vector(0.0, -0.7071), Geometry.getCenterPoint(4, 3));
		// corners = 6
		assertEquals(new Vector(+0.8660, 0.0), Geometry.getCenterPoint(6, 0));
		assertEquals(new Vector(+0.4330, +0.75), Geometry.getCenterPoint(6, 1));
		assertEquals(new Vector(-0.4330, +0.75), Geometry.getCenterPoint(6, 2));
		assertEquals(new Vector(-0.8660, 0.0), Geometry.getCenterPoint(6, 3));
		assertEquals(new Vector(-0.4330, -0.75), Geometry.getCenterPoint(6, 4));
		assertEquals(new Vector(+0.4330, -0.75), Geometry.getCenterPoint(6, 5));
		// corners = 8
		assertEquals(new Vector(+0.9239, 0.0), Geometry.getCenterPoint(8, 0));
		assertEquals(new Vector(+0.6533, +0.6533), Geometry.getCenterPoint(8, 1));
		assertEquals(new Vector(0.0, +0.9239), Geometry.getCenterPoint(8, 2));
		assertEquals(new Vector(-0.6533, +0.6533), Geometry.getCenterPoint(8, 3));
		assertEquals(new Vector(-0.9239, 0.0), Geometry.getCenterPoint(8, 4));
		assertEquals(new Vector(-0.6533, -0.6533), Geometry.getCenterPoint(8, 5));
		assertEquals(new Vector(-0.0, -0.9239), Geometry.getCenterPoint(8, 6));
		assertEquals(new Vector(+0.6533, -0.6533), Geometry.getCenterPoint(8, 7));
	}

	public void testAngle() throws Exception
	{
		assertEquals(Math.PI / 4 * 0, Geometry.angle(new Vector(1.0, 0.0)));
		assertEquals(Math.PI / 4 * 1, Geometry.angle(new Vector(1.0, 1.0)));
		assertEquals(Math.PI / 4 * 2, Geometry.angle(new Vector(0.0, 1.0)));
		assertEquals(Math.PI / 4 * 3, Geometry.angle(new Vector(-1.0, 1.0)));
		assertEquals(Math.PI / 4 * 4, Geometry.angle(new Vector(-1.0, 0.0)));
		assertEquals(Math.PI / 4 * -3, Geometry.angle(new Vector(-1.0, -1.0)));
		assertEquals(Math.PI / 4 * -2, Geometry.angle(new Vector(0.0, -1.0)));
		assertEquals(Math.PI / 4 * -1, Geometry.angle(new Vector(1.0, -1.0)));

		Vector v;
		Vector d;
		double facX, facY;
		for(int i = 0; i < 100; i++)
		{
			v = new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);
			d = Geometry.direction(Geometry.angle(v));
			facX = d.x / v.x;
			facY = d.y / v.y;
			assertEquals(facX, facY, TOLERANCE);
		}
	}

	public void testDirection() throws Exception
	{
		assertEquals(new Vector(1.0, 0.0), Geometry.direction(Math.PI / 4 * 0));
		assertEquals(new Vector(0.7071, 0.7071), Geometry.direction(Math.PI / 4 * 1));
		assertEquals(new Vector(0.0, 1.0), Geometry.direction(Math.PI / 4 * 2));
		assertEquals(new Vector(-0.7071, 0.7071), Geometry.direction(Math.PI / 4 * 3));
		assertEquals(new Vector(-1.0, 0.0), Geometry.direction(Math.PI / 4 * 4));
		assertEquals(new Vector(-0.7071, -0.7071), Geometry.direction(Math.PI / 4 * 5));
		assertEquals(new Vector(0.0, -1.0), Geometry.direction(Math.PI / 4 * 6));
		assertEquals(new Vector(0.7071, -0.7071), Geometry.direction(Math.PI / 4 * 7));
	}

	public void testGetSectorIndex() throws Exception
	{
		for(int i = 4; i <= 8; i += 2)
		{
			for(int c = 0; c < i; c++)
			{
				// centerpoint must be within sector
				// corners mark border of sector
				assertEquals(c, Geometry.getSectorIndex(i, Geometry.getCenterPoint(i, c)));
			}
		}
	}

	public void testGetPlayerSectorIndex() throws Exception
	{
		for(int i = 4; i <= 8; i += 2)
		{
			for(int c = 0; c < i / 2; c++)
			{
				// centerpoint & corners must be within player sector
				// intermediate centerpoints mark border of player sectors
				assertEquals(c, Geometry.getPlayerSectorIndex(i, Geometry.getStartCornerPoint(i, c * 2)));
				assertEquals(c, Geometry.getPlayerSectorIndex(i, Geometry.getCenterPoint(i, c * 2)));
				assertEquals(c, Geometry.getPlayerSectorIndex(i, Geometry.getEndCornerPoint(i, c * 2)));
			}
		}
	}

	public void testCenter() throws Exception
	{
		Vector v1, v2, c;
		for(int i = 0; i < 100; i++)
		{
			v1 = new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);
			v2 = new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);
			c = Geometry.center(v1, v2);

			// c*2 = v1+v2
			assertEquals(v1.add(v2), c.add(c));
		}
	}

	public void testDistance() throws Exception
	{
		Vector v1, v2;
		double expected;
		for(int i = 0; i < 100; i++)
		{
			v1 = new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);
			v2 = new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);

			expected = Math.sqrt(v2.x * v2.x + v2.y * v2.y);
			v2.add(v1);

			assertEquals(expected, Geometry.distance(v1, v2));
		}
	}

	public void testDistanceFromCenter() throws Exception
	{
		Vector zero = new Vector();
		Vector v;
		for(int i = 0; i < 100; i++)
		{
			v = new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);

			assertEquals(Geometry.distance(zero, v), Geometry.distanceFromCenter(v));
		}
	}

	public void testIntersect_Lines() throws Exception
	{
		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(1.0, 0.0), new Vector(-1.0, 0.0), new Vector(0.0, 1.0), new Vector(0.0, -1.0)));
		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(-1.0, 0.0), new Vector(1.0, 0.0), new Vector(0.0, -1.0), new Vector(0.0, 1.0)));
		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(0.0, 1.0), new Vector(0.0, -1.0), new Vector(1.0, 0.0), new Vector(-1.0, 0.0)));
		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(0.0, -1.0), new Vector(0.0, 1.0), new Vector(-1.0, 0.0), new Vector(1.0, 0.0)));

		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(1.0, 1.0), new Vector(-1.0, -1.0), new Vector(-1.0, 1.0), new Vector(1.0, -1.0)));
		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(-1.0, -1.0), new Vector(1.0, 1.0), new Vector(1.0, -1.0), new Vector(-1.0, 1.0)));
		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(1.0, -1.0), new Vector(-1.0, 1.0), new Vector(-1.0, -1.0), new Vector(1.0, 1.0)));
		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(-1.0, 1.0), new Vector(1.0, -1.0), new Vector(1.0, 1.0), new Vector(-1.0, -1.0)));

		assertEquals(new Vector(-0.5, -0.5),
				Geometry.intersect(new Vector(-1.0, -1.0), new Vector(1.0, 1.0), new Vector(-1.0, 0.0), new Vector(0.0, -1.0)));
		assertEquals(new Vector(0.5, 0.5),
				Geometry.intersect(new Vector(-1.0, -1.0), new Vector(1.0, 1.0), new Vector(1.0, 0.0), new Vector(0.0, 1.0)));
		assertEquals(new Vector(0.0, 0.0),
				Geometry.intersect(new Vector(-1.0, -1.0), new Vector(1.0, 1.0), new Vector(-0.5, 1.0), new Vector(0.5, -1.0)));
	}

	public void testIntersect_Polygons() throws Exception
	{
		Polygon p1, p2;
		Set<Vector> intersections;
		
		/*
		 *   ooooooo
		 *   o     o
		 * ooxoooo o
		 * o o   o o 
		 * o o 0 o o
		 * o o   o o
		 * o ooooxoo
		 * o     o
		 * ooooooo
		 */

		p1 = new Polygon.Impl(Arrays.asList(new Vector(2.0, 2.0), new Vector(2.0, -1.0), new Vector(-1.0, -1.0), new Vector(-1.0, 2.0)));
		p2 = new Polygon.Impl(Arrays.asList(new Vector(-2.0, -2.0), new Vector(-2.0, 1.0), new Vector(1.0, 1.0), new Vector(1.0, -2.0)));
		
		intersections = Geometry.intersect(p1, p2);
		assertEquals(2, intersections.size());
		assertTrue(intersections.contains(new Vector(1.0, -1.0)));
		assertTrue(intersections.contains(new Vector(-1.0, 1.0)));
	}
}
