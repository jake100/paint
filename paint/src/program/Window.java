package program;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import program.customGui.CustomButton;
import program.customGui.CustomLabel;
import program.customGui.CustomTextLabel;
import program.tools.Brush;

public abstract class Window extends JFrame
{
	public static final int WIDTH = 700, HEIGHT = WIDTH * 9 / 16, TOOL_HEIGHT = 20, INFO_HEIGHT = 20;
	protected DrawingArea mainPanel;
	protected JMenuBar menuBar;
	protected JMenu file, view, brushSettings, image, effects;
	protected JMenuItem load, save, saveAs;
	protected JPanel toolPanel = new JPanel(new FlowLayout());
	protected JPanel infoPanel = new JPanel(new FlowLayout());
	//adding to infoPanel
	protected JPanel textPanel = new JPanel(new FlowLayout());
	protected JPanel iconPanel = new JPanel(new FlowLayout());
	
	protected ImageIcon drawIcon = new ImageIcon("res/draw_off.png");
	protected ImageIcon lineIcon = new ImageIcon("res/line.png");
	protected ImageIcon selectIcon = new ImageIcon("res/select_off.png");
	protected ImageIcon colorIcon = new ImageIcon("res/color.png");
	protected ImageIcon colorIndicatorIcon = new ImageIcon("res/color_indicator.png");
	protected ImageIcon transparancyIcon = new ImageIcon("res/transparancy.png");
	
	protected CustomButton drawButton = new CustomButton(drawIcon, true);
	protected CustomButton lineButton = new CustomButton(lineIcon, false);
	protected CustomButton selectButton = new CustomButton(selectIcon, false);
	protected CustomButton colorButton = new CustomButton(colorIcon, false);
	protected CustomButton transparancyButton = new CustomButton(transparancyIcon, false);
	
	protected CustomLabel transLabel = new CustomLabel(colorIndicatorIcon, "Color");
	protected CustomTextLabel colorLabel = new CustomTextLabel("Color: 0, 0, 0");
	protected int col;
	protected int lastX = 0, lastY = 0, drawX = 0, drawY = 0;
	protected Brush smallBrush, brush, mediumBrush;
	protected boolean drawing = false, dragged = false;
	public Window()
	{
		menuBar = new JMenuBar();
		file = new JMenu("File");
		view = new JMenu("View");
		brushSettings = new JMenu("Brush_Settings");
		
		image = new JMenu("Image");
		effects = new JMenu("Effects");
		load = new JMenuItem("Load");
		save = new JMenuItem("Save");
		saveAs = new JMenuItem("Save As");
		mainPanel = new DrawingArea();
		setSize(new Dimension(WIDTH, HEIGHT + TOOL_HEIGHT + INFO_HEIGHT));
		
		col = 0;
		smallBrush = new Brush(col, 1, 50);
		brush = new Brush(col, 2, 7);
		mediumBrush = new Brush(col, 3, 3);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("res/draw_off.png"));
		setDefaultCloseOperation(3);
		setVisible(true);
		setResizable(true);
		setComponents();
		setMenu();
		addComponents();
	}
	public void addComponents()
	{
		setJMenuBar(menuBar);
		menuBar.add(file);
		file.add(load);
		file.add(save);
		file.add(saveAs);
		menuBar.add(view);
		menuBar.add(brushSettings);
		menuBar.add(image);
		menuBar.add(effects);
		
		toolPanel.add(drawButton);
		toolPanel.add(lineButton);
		toolPanel.add(selectButton);
		toolPanel.add(colorButton);
		toolPanel.add(transparancyButton);
		
		infoPanel.add(textPanel, BorderLayout.WEST);
		infoPanel.add(iconPanel, BorderLayout.EAST);
		iconPanel.add(transLabel);
		textPanel.add(colorLabel);
		
		add(toolPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(infoPanel, BorderLayout.SOUTH);
	}
	public void setMenu()
	{
		
	}
	public void setComponents()
	{
		mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
}
