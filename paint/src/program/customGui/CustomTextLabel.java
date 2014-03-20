package program.customGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

public class CustomTextLabel extends JLabel
{
	private String s;
	public CustomTextLabel(String s)
	{
		this.s = s;
		this.setPreferredSize(new Dimension(90, 28));
	}
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawString(s, 2, 20);
	}
}
