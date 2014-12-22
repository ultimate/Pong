package ultimate.pong.graphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import ultimate.pong.data.model.Map;
import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;
import ultimate.pong.math.Vector;

public class View extends JPanel
{
	private static final long	serialVersionUID	= 1L;

	private Map					map;

	@Override
	protected void paintComponent(Graphics g)
	{
		int w = this.getWidth();
		int h = this.getHeight();

		if(map != null)
		{
			// clear
			g.setColor(map.getColor().toAWTColor());
			g.fillRect(0, 0, w, h);

			// draw objects
			ScreenVector v1, v2;
			for(MapObject o : map.getObjects())
			{
				g.setColor(o.getColor().toAWTColor());

				if(o instanceof Wall)
				{
					Wall wall = (Wall) o;

					v1 = new ScreenVector(wall.getStart());
					v2 = new ScreenVector(wall.getEnd());

					g.drawLine(v1.x, v1.y, v2.x, v2.y);
				}
				else if(o instanceof Slider)
				{
					Slider slider = (Slider) o;

					v1 = new ScreenVector(slider.getStart());
					v2 = new ScreenVector(slider.getEnd());

					g.drawLine(v1.x, v1.y, v2.x, v2.y);
				}
				else if(o instanceof Ball)
				{
					Ball ball = (Ball) o;

					v1 = new ScreenVector(ball.getPosition());

					g.fillOval(v1.x, v1.y, 1, 1);
				}
			}
		}
		else
		{
			g.setColor(Color.black);
			g.fillRect(0, 0, w, h);
		}
	}

	public void update(Map map)
	{
		this.map = map;
		this.repaint();
	}

	private class ScreenVector
	{
		public int	x;
		public int	y;

		public ScreenVector(Vector pos)
		{
			int w = getWidth();
			int h = getWidth();
			int size = Math.min(w, h);
			this.x = (int) ((pos.getX() + 1) * size / 2 + (w - size) / 2);
			this.y = (int) ((pos.getY() + 1) * size / 2 + (h - size) / 2);
		}
	}
}
