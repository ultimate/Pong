package ultimate.pong.math;

import junit.framework.TestCase;

public class VectorTest extends TestCase
{
	public void testEqual() throws Exception
	{
		assertTrue(new Vector(0.5, 0.0).equals(new Vector(0.500001, 0.0)));
		assertTrue(new Vector(0.5, 0.0).equals(new Vector(0.50001, 0.0)));
		assertTrue(new Vector(0.5, 0.0).equals(new Vector(0.5001, 0.0)));
		assertFalse(new Vector(0.5, 0.0).equals(new Vector(0.501, 0.0)));
		assertFalse(new Vector(0.5, 0.0).equals(new Vector(0.51, 0.0)));

		assertTrue(new Vector(0.500001, 0.0).equals(new Vector(0.5, 0.0)));
		assertTrue(new Vector(0.50001, 0.0).equals(new Vector(0.5, 0.0)));
		assertTrue(new Vector(0.5001, 0.0).equals(new Vector(0.5, 0.0)));
		assertFalse(new Vector(0.501, 0.0).equals(new Vector(0.5, 0.0)));
		assertFalse(new Vector(0.51, 0.0).equals(new Vector(0.5, 0.0)));

		assertTrue(new Vector(0.0, 0.5).equals(new Vector(0.0, 0.500001)));
		assertTrue(new Vector(0.0, 0.5).equals(new Vector(0.0, 0.50001)));
		assertTrue(new Vector(0.0, 0.5).equals(new Vector(0.0, 0.5001)));
		assertFalse(new Vector(0.0, 0.5).equals(new Vector(0.0, 0.501)));
		assertFalse(new Vector(0.0, 0.5).equals(new Vector(0.0, 0.51)));

		assertTrue(new Vector(0.0, 0.500001).equals(new Vector(0.0, 0.5)));
		assertTrue(new Vector(0.0, 0.50001).equals(new Vector(0.0, 0.5)));
		assertTrue(new Vector(0.0, 0.5001).equals(new Vector(0.0, 0.5)));
		assertFalse(new Vector(0.0, 0.501).equals(new Vector(0.0, 0.5)));
		assertFalse(new Vector(0.0, 0.51).equals(new Vector(0.0, 0.5)));
	}

	public void testRound() throws Exception
	{
		assertEquals(new Vector(0.49, 0.51), new Vector(0.49, 0.51).round());
		assertEquals(new Vector(0.499, 0.501), new Vector(0.499, 0.501).round());
		
		assertEquals(new Vector(0.5, 0.5), new Vector(0.4999, 0.5001).round());
		assertEquals(new Vector(0.5, 0.5), new Vector(0.49999, 0.50001).round());
		assertEquals(new Vector(0.5, 0.5), new Vector(0.499999, 0.500001).round());
		assertEquals(new Vector(0.5, 0.5), new Vector(0.4999999, 0.5000001).round());
		assertEquals(new Vector(0.5, 0.5), new Vector(0.49999999, 0.50000001).round());
	}

	public void testScale() throws Exception
	{
		assertEquals(new Vector(2.0, -4.0), new Vector(1.0, -2.0).scale(2.0));
		assertEquals(new Vector(-2.0, 4.0), new Vector(1.0, -2.0).scale(-2.0));
		assertEquals(new Vector(0.5, -1.0), new Vector(1.0, -2.0).scale(0.5));
		assertEquals(new Vector(-0.5, 1.0), new Vector(1.0, -2.0).scale(-0.5));
	}

	public void testAdd() throws Exception
	{
		assertEquals(new Vector(2.0, 4.0), new Vector(1.0, 2.0).add(new Vector(1.0, 2.0)));
		assertEquals(new Vector(0.0, 0.0), new Vector(1.0, 2.0).add(new Vector(-1.0, -2.0)));
		assertEquals(new Vector(0.0, 0.0), new Vector(-1.0, -2.0).add(new Vector(1.0, 2.0)));
		assertEquals(new Vector(-2.0, -4.0), new Vector(-1.0, -2.0).add(new Vector(-1.0, -2.0)));
	}

	public void testCompare() throws Exception
	{
		assertEquals(0, new Vector(0.5, 0.0).compareTo(new Vector(0.500001, 0.0)));
		assertEquals(0, new Vector(0.5, 0.0).compareTo(new Vector(0.50001, 0.0)));
		assertEquals(0, new Vector(0.5, 0.0).compareTo(new Vector(0.5001, 0.0)));
		assertEquals(-1, new Vector(0.5, 0.0).compareTo(new Vector(0.501, 0.0)));
		assertEquals(-1, new Vector(0.5, 0.0).compareTo(new Vector(0.51, 0.0)));

		assertEquals(0, new Vector(0.500001, 0.0).compareTo(new Vector(0.5, 0.0)));
		assertEquals(0, new Vector(0.50001, 0.0).compareTo(new Vector(0.5, 0.0)));
		assertEquals(0, new Vector(0.5001, 0.0).compareTo(new Vector(0.5, 0.0)));
		assertEquals(1, new Vector(0.501, 0.0).compareTo(new Vector(0.5, 0.0)));
		assertEquals(1, new Vector(0.51, 0.0).compareTo(new Vector(0.5, 0.0)));

		assertEquals(0, new Vector(0.0, 0.5).compareTo(new Vector(0.0, 0.500001)));
		assertEquals(0, new Vector(0.0, 0.5).compareTo(new Vector(0.0, 0.50001)));
		assertEquals(0, new Vector(0.0, 0.5).compareTo(new Vector(0.0, 0.5001)));
		assertEquals(-1, new Vector(0.0, 0.5).compareTo(new Vector(0.0, 0.501)));
		assertEquals(-1, new Vector(0.0, 0.5).compareTo(new Vector(0.0, 0.51)));

		assertEquals(0, new Vector(0.0, 0.500001).compareTo(new Vector(0.0, 0.5)));
		assertEquals(0, new Vector(0.0, 0.50001).compareTo(new Vector(0.0, 0.5)));
		assertEquals(0, new Vector(0.0, 0.5001).compareTo(new Vector(0.0, 0.5)));
		assertEquals(1, new Vector(0.0, 0.501).compareTo(new Vector(0.0, 0.5)));
		assertEquals(1, new Vector(0.0, 0.51).compareTo(new Vector(0.0, 0.5)));
	}
}
