package ultimate.pong.ui;

import java.awt.Color;

import javax.swing.JComponent;

public class Blinker extends Thread
{
	private JComponent	component;
	private int			counter;
	private Color		blinkColor;
	private Color		originalColor;
	private int			interval;

	public Blinker(JComponent component, int times, Color blinkColor, int interval)
	{
		this.component = component;
		this.originalColor = component.getBackground();
		this.blinkColor = blinkColor;
		this.counter = times * 2;
		this.interval = interval;
	}

	@Override
	public void run()
	{
		while(counter > 0)
		{
			counter--;
		
			if(counter % 2 == 1)
				component.setBackground(blinkColor);
			else
				component.setBackground(originalColor);
			
			try
			{
				Thread.sleep(interval/2);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
