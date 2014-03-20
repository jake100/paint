package program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class Program_Base extends Window
{
	public Program_Base()
	{
		super();
		actions();
		repaint();
	}
	public void actions()
	{
		mainPanel.addMouseListener(new MouseAdapter()
		{
		   public void mousePressed(MouseEvent e)
		   {
			   drawing = true;
			   e.consume();
		   }
		   public void mouseReleased(MouseEvent e)
		   {
			   drawing = false;
			   e.consume();
		   }
		});
		mainPanel.addMouseMotionListener(new MouseAdapter()
		{
		     public void mouseDragged(MouseEvent e)
		     {
		    	lastX = drawX;
		    	lastY = drawY;
				drawX = e.getX();
				drawY = e.getY();
		    	dragged = true;
			    e.consume();
		     }
		     public void mouseMoved(MouseEvent e)
		     {
		    	 lastX = drawX;
		    	 lastY = drawY;
				 drawX = e.getX();
				 drawY = e.getY();
				 e.consume();
		     }
		     public void mouseReleased(MouseEvent e) 
		     {
		         dragged = false;
		     }
		});
		drawButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				drawButton.setSelected(true);
				selectButton.setSelected(false);
			}
		});
		selectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				selectButton.setSelected(true);
				drawButton.setSelected(false);
			}
		});
	}
}
