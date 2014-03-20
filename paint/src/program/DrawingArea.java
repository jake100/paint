package program;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class DrawingArea extends Canvas implements Runnable
{
	private static Screen screen;
	private static Thread thread;
	public DrawingArea()
	{
		screen = new Screen(Program.WIDTH, Program.HEIGHT);
	}
	public void update()
	{
		
	}
	public void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		update();
		g.clearRect(0, 0, getWidth(), getHeight());
		screen.render(g);
		g.dispose();
		bs.show();
	}
	public synchronized void start()
	{
		thread = new Thread(this, "Timer");
		thread.start();
	}
	public Screen getScreen() {return screen;}
	public void run() 
	{
		while(true)
		{
			update();
			render();
		}
	}
}
