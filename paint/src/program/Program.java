package program;
import java.util.Random;

import program.tools.Brush;

public class Program extends Program_Base implements Runnable
{
	private static Thread thread;
	public Program()
	{
		super();
	}
	public synchronized void start()
	{
		thread = new Thread(this, "Timer");
		mainPanel.start();
		thread.start();
	}
	public void run() 
	{
		while(true)
		{
			update();
			render();
		}
	}
	public void render()
	{
		//smallBrush.render(mainPanel.getScreen());
		brush.render(mainPanel.getScreen());
		drawButton.repaint();
		selectButton.repaint();
	}
	public void update()
	{
		if(!drawing)
		{
			lastX = drawX;
			lastY = drawY;
		}
		if(drawing)
		{
			double offset = .0000075, multiplier = 1, maxMultiplier;
			int points = 5000;
			int x[] = new int[points], y[] = new int[points];
			for(int i = 0; i < points; i++)
			{
				x[i] = drawX;
				y[i] = drawY;
			}
			if(lastX < drawX)
			{
				for(int i = 0; i < points; i++)
				{
					multiplier++;
					x[i] = drawX + (int)((drawX - lastX) * (offset * multiplier));
				}
				multiplier = 1;
			}
			if(lastX > drawX)
			{
				for(int i = 0; i < points; i++)
				{
					multiplier++;
					x[i] = drawX - (int)((lastX - drawX) * (offset * multiplier));
				}
				multiplier = 1;
			}
			if(lastY < drawY)
			{
				for(int i = 0; i < points; i++)
				{
					multiplier++;
					y[i] = drawY + (int)((drawY - lastY) * (offset * multiplier));
				}
				multiplier = 1;
			}
			if(lastY > drawY)
			{
				for(int i = 0; i < points; i++)
				{
					multiplier++;
					y[i] = drawY - (int)((lastY - drawY) * (offset * multiplier));
				}
				multiplier = 1;
			}
			smallBrush.stage = Brush.Stage.Draw;
			brush.stage = Brush.Stage.Draw;
			for(int i = 0; i < points; i++)
			{
				int start = 1, bigStart = 2, chance = 5, bigChance = 5;
				if(i < points / start && new Random().nextInt(chance) == 0) brush.addPoint(x[i], y[i]);
				else if(i < points / bigStart && new Random().nextInt(bigChance) == 0) mediumBrush.addPoint(x[i], y[i]);
				else smallBrush.addPoint(x[i], y[i]);
			}
		}
		else
		{
			smallBrush.stage = Brush.Stage.Wait;
		}
	}
	public static void main(String args[])
	{
		Program w = new Program();
		w.start();
	}
}
