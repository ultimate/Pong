package ultimate.pong.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Map;
import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;
import ultimate.pong.math.Vector;

public class View extends JPanel
{
	private static final long			serialVersionUID	= 1L;

	protected transient final Logger	logger				= LoggerFactory.getLogger(getClass());

	private Map							map;

	private double						rotation			= 0.0;

	@Override
	protected void paintComponent(Graphics g)
	{
		int w = this.getWidth();
		int h = this.getHeight();

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if(this.map != null)
		{
			// clear
			g2d.setColor(this.map.getColor().toAWTColor());
			g2d.fillRect(0, 0, w, h);
			
			g2d.rotate(this.rotation, w/2.0, h/2.0);
			rotation += 0.002;

			// draw objects
			Point2D v1, v2;
			for(MapObject o : this.map.getObjects())
			{
				g.setColor(o.getColor().toAWTColor());

//				logger.debug("drawing " + o.getType() + " id=" + o.getId() + " color=" + g.getColor());

				if(o instanceof Wall)
				{
					Wall wall = (Wall) o;

					v1 = toScreenXY(wall.getStart());
					v2 = toScreenXY(wall.getEnd());

					g2d.draw(new Line2D.Double(v1, v2));
				}
				else if(o instanceof Slider)
				{
					Slider slider = (Slider) o;

					v1 = toScreenXY(slider.getStart());
					v2 = toScreenXY(slider.getEnd());

					g2d.draw(new Line2D.Double(v1, v2));
				}
				else if(o instanceof Ball)
				{
					Ball ball = (Ball) o;

					v1 = toScreenXY(ball.getPosition());

					g2d.fill(new Ellipse2D.Double(v1.getX()-0.5, v1.getY()-0.5, 2, 2));
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

	public void setRotation(double rotation)
	{

	}

	public double getRotation()
	{
		return rotation;
	}

	public Point2D toScreenXY(Vector pos)
	{
		int w = getWidth();
		int h = getHeight();
		int size = Math.min(w, h);
		double x = ((pos.getX() + 1) * size / 2.0 + (w - size) / 2.0);
		double y = ((pos.getY() + 1) * size / 2.0 + (h - size) / 2.0);
		return new Point2D.Double(x, y);
	}
}
