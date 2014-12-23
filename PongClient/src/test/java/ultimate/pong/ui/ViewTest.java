package ultimate.pong.ui;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import junit.framework.TestCase;
import ultimate.pong.math.Vector;

public class ViewTest extends TestCase
{
	public void testToScreeXY() throws Exception
	{
		final Dimension dim = new Dimension();

		View v = new View() {
			private static final long	serialVersionUID	= 1L;
			public int getWidth()
			{
				return (int) dim.getWidth();
			}
			public int getHeight()
			{
				return (int) dim.getHeight();
			}
		};
		
		dim.setSize(200, 100);
		
		assertEquals(new Point2D.Double(50, 0), v.toScreenXY(new Vector(-1, -1)));
		assertEquals(new Point2D.Double(50, 50), v.toScreenXY(new Vector(-1, 0)));
		assertEquals(new Point2D.Double(50, 100), v.toScreenXY(new Vector(-1, 1)));
		assertEquals(new Point2D.Double(100, 0), v.toScreenXY(new Vector(0, -1)));
		assertEquals(new Point2D.Double(100, 50), v.toScreenXY(new Vector(0, 0)));
		assertEquals(new Point2D.Double(100, 100), v.toScreenXY(new Vector(0, 1)));
		assertEquals(new Point2D.Double(150, 0), v.toScreenXY(new Vector(1, -1)));
		assertEquals(new Point2D.Double(150, 50), v.toScreenXY(new Vector(1, 0)));
		assertEquals(new Point2D.Double(150, 100), v.toScreenXY(new Vector(1, 1)));
		
		dim.setSize(100, 200);
		
		assertEquals(new Point2D.Double(0, 50), v.toScreenXY(new Vector(-1, -1)));
		assertEquals(new Point2D.Double(0, 100), v.toScreenXY(new Vector(-1, 0)));
		assertEquals(new Point2D.Double(0, 150), v.toScreenXY(new Vector(-1, 1)));
		assertEquals(new Point2D.Double(50, 50), v.toScreenXY(new Vector(0, -1)));
		assertEquals(new Point2D.Double(50, 100), v.toScreenXY(new Vector(0, 0)));
		assertEquals(new Point2D.Double(50, 150), v.toScreenXY(new Vector(0, 1)));
		assertEquals(new Point2D.Double(100, 50), v.toScreenXY(new Vector(1, -1)));
		assertEquals(new Point2D.Double(100, 100), v.toScreenXY(new Vector(1, 0)));
		assertEquals(new Point2D.Double(100, 150), v.toScreenXY(new Vector(1, 1)));
	}
}
