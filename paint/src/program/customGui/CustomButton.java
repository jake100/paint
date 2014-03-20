package program.customGui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class CustomButton extends JButton
{
	private boolean selected = false;
	private Icon icon;
	public CustomButton(ImageIcon icon, boolean selected)
	{
		super(icon);
		this.icon = icon;
		this.selected = selected;
		setContentAreaFilled(false);
		setBorderPainted(false);
		setRolloverEnabled(false);
		setFocusPainted(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		if(selected)g.drawRect(16, 4, icon.getIconWidth() + 1, icon.getIconHeight() + 1);
		super.paintComponent(g);
	}
	public boolean isSelected() {return selected;}
	public void setSelected(boolean selected) {this.selected = selected;}
}

