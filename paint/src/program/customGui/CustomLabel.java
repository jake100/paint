package program.customGui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;

public class CustomLabel extends JLabel
{
	private String s;
	public CustomLabel(Icon icon, String s)
	{
		super(icon);
		this.s = s;
	}
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
		g.setColor(Color.red);
		g.drawString(s, 7, 26);
	}
}
