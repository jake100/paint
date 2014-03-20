package program.tools;


import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Random;

import program.Screen;

public class Brush
{
	public static enum Stage{Wait, Draw}
	public static enum Shape{Rect, Circle}
	public static Stage stage = Stage.Draw;
	public static Shape shape = Shape.Circle;
	private int col;
	private int size;
	private Rectangle[] tips;
	private Line2D.Double line;
	private int counter = 0, maxPoints, chance = 1, scatterDistance;
	public Brush(int col, int size, int density)
	{
		this.size = size;
		scatterDistance = size;
		maxPoints = density;
		this.col = col;
		tips = new Rectangle[maxPoints];
		line = new Line2D.Double();
		for(int i = 0;i < maxPoints;i++)
		{
			tips[i] = new Rectangle();
		}
	}
	public void addPoint(int x, int y)
	{
		counter++;
		if(counter % chance == 0)
		{
			int points = new Random().nextInt(maxPoints);
			for(int i = 0;i < points;i++)
			{
				int xscatter = new Random().nextInt(scatterDistance);
				int yscatter = new Random().nextInt(scatterDistance);
				if(new Random().nextBoolean()) xscatter = -xscatter;
				if(new Random().nextBoolean()) yscatter = -yscatter;
				tips[i].setBounds(x + xscatter, y + yscatter, size, size);
			}
		}
	}
	public void render(Screen screen)
	{
		if(stage == Stage.Draw)
		{
			for(int xx = 0;xx < screen.getWidth();xx++)
			{
				for(int yy = 0;yy < screen.getHeight();yy++)
				{
					for(int i = 0;i < maxPoints;i++)
					{
						if(tips[i].contains(xx, yy))screen.changePixel(xx, yy, col);
					}
				}
			}
		}
	}
	public int getCol() {return col;}
	public void setCol(int col) {this.col = col;}
	
}
