package aplication;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import libproject.shapes.DrawDrag;
import libproject.shapes.DrawSemiCircule;
import libproject.shapes.DrawGraphProfit;
import libproject.shapes.DrawRectGrap;
import libproject.utilities.ProjectFunction;

public class Aplication extends JFrame implements ActionListener {

	ImagePanel image;
	StockLevelPanel stockLevel;
	SetValuesPanel setValues;
	MyPanelAplication mainPanel;
	ProfitGrapPanel profitGrap;
	RectGrapPanel rectGrap;

	PrinterJob pj;

	JFileChooser fc = new JFileChooser();

	public static void main(String[] args) {
		JFrame frame = new Aplication();
		frame.setTitle("DashBoard");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new MyPanelAplication();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);

	}

	public Aplication() {

		mainPanel = new MyPanelAplication();
		profitGrap = new ProfitGrapPanel();
		image = new ImagePanel();
		stockLevel = new StockLevelPanel();
		rectGrap = new RectGrapPanel();
		setValues = new SetValuesPanel();

		setBorderToPanel(mainPanel, 3);
		setBorderToPanel(profitGrap, 3);
		setBorderToPanel(image, 3);
		setBorderToPanel(stockLevel, 3);
		setBorderToPanel(rectGrap, 3);
		setBorderToPanel(setValues, 3);

		JLayeredPane layeredPane = getLayeredPane();

		layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(profitGrap, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(image, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(stockLevel, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(rectGrap, JLayeredPane.PALETTE_LAYER);

		layeredPane.add(setValues, JLayeredPane.PALETTE_LAYER);

		int xStart = 75;
		int yStart = 100;
		int panelWidth = 250;
		int panelHeight = 250;
		int spacing = 25;
		int setValuesHeight = 400;
		int setValuesWidth = 350;

		profitGrap.setBounds(xStart, yStart, 2 * panelWidth + spacing, panelHeight);
		image.setBounds(xStart + 2 * panelWidth + 2 * spacing, yStart, panelWidth, panelHeight);
		stockLevel.setBounds(xStart, yStart + panelHeight + spacing, panelWidth, panelHeight);
		rectGrap.setBounds(xStart + panelWidth + spacing, yStart + panelHeight + spacing, 2 * panelWidth + spacing,
				panelHeight);
		setValues.setBounds(xStart + 3 * panelWidth + 3 * spacing, yStart, setValuesWidth, setValuesHeight + spacing);

		// addd menus
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);

		// file menu
		JMenu menu = new JMenu("File");
		JMenuItem mi = new JMenuItem("Insert Product Image");
		mi.addActionListener(this);
		menu.add(mi);

		mi = new JMenuItem("Save Image");
		mi.addActionListener(this);
		menu.add(mi);

		menu.addSeparator();
		mi = new JMenuItem("Exit");
		mi.addActionListener(this);
		menu.add(mi);

		mb.add(menu);

		// Edit product image menu
		menu = new JMenu("Edit product image");
		mi = new JMenuItem("Contrast");
		mi.addActionListener(this);
		menu.add(mi);
		mi = new JMenuItem("Brighten");
		mi.addActionListener(this);
		menu.add(mi);
		mi = new JMenuItem("Darken");
		mi.addActionListener(this);
		menu.add(mi);
		mb.add(menu);

		// Print menu
		menu = new JMenu("Print");
		mi = new JMenuItem("Graph 1");
		mi.addActionListener(this);
		menu.add(mi);
		mi = new JMenuItem("Graph 2");
		mi.addActionListener(this);
		menu.add(mi);
		mi = new JMenuItem("Graph 3");
		mi.addActionListener(this);
		menu.add(mi);

		mb.add(menu);

	}

	private void setBorderToPanel(JPanel panel, int borderWidth) {
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderWidth));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Insert Product Image")) {
			int returnVale = fc.showOpenDialog(this);
			if (returnVale == JFileChooser.APPROVE_OPTION) {
				try {
					File selectedFile = fc.getSelectedFile();
					// System.out.println("Selected File: " + selectedFile.getAbsolutePath());
					BufferedImage bi = ImageIO.read(fc.getSelectedFile());
					image.setImage(bi);
					pack();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		} else if (cmd.equals("Save Image")) {
			int returnVale = fc.showSaveDialog(this);
			if (returnVale == JFileChooser.APPROVE_OPTION) {
				try {
					ImageIO.write(image.getImage(), "png", fc.getSelectedFile());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		} else if (cmd.equals("Exit")) {
			System.exit(0);
		} else
			process(cmd);

	}

	private void process(String cmd) {
		BufferedImageOp op = null;
		if (cmd.equals("Contrast")) {
			float[] data = { 0f, -1f, 0f, -1f, 5f, -1f, 0f, -1f, 0f };
			Kernel k = new Kernel(3, 3, data);
			op = new ConvolveOp(k);
			pack();
		} else if (cmd.equals("Brighten")) {
			image.setImage(WhiteMode(image.getImage()));
			pack();

		} else if (cmd.equals("Darken")) {
			image.setImage(BlackMode(image.getImage()));
			pack();

		} else
			process2(cmd);

		if (op != null) {
			image.setImage(op.filter(image.getImage(), null));
			pack();

		}
		pack();
	}

	private void process2(String cmd) {

		pj = PrinterJob.getPrinterJob();
		if (cmd.equals("Graph 1")) {

			pj.setPrintable(profitGrap);
			if (pj.printDialog()) {
				try {
					pj.print();

				} catch (PrinterException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error during printing: " + ex.getMessage(), "Printing Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
			pack();
		} else if (cmd.equals("Graph 2")) {
			pj.setPrintable(stockLevel);
			if (pj.printDialog()) {
				try {
					pj.print();

				} catch (PrinterException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error during printing: " + ex.getMessage(), "Printing Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			pack();
		} else if (cmd.equals("Graph 3")) {

			pj.setPrintable(rectGrap);
			if (pj.printDialog()) {
				try {
					pj.print();
				} catch (PrinterException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error during printing: " + ex.getMessage(), "Printing Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			pack();
		}
	}

	private BufferedImage WhiteMode(BufferedImage imgIn) {
		BufferedImage imgOut = new BufferedImage(imgIn.getWidth(), imgIn.getHeight(), imgIn.getType());
		WritableRaster rasterImgIn = imgIn.getRaster();
		WritableRaster rasterImgOut = imgOut.getRaster();
		int[] rgba = new int[4];
		for (int x = 0; x < imgIn.getWidth(); x++) {
			for (int y = 0; y < imgIn.getHeight(); y++) {
				rasterImgIn.getPixel(x, y, rgba);

				rgba[0] = (int) (rgba[0] * 1.1);
				rgba[1] = (int) (rgba[1] * 1.1);
				rgba[2] = (int) (rgba[2] * 1.1);

				if (rgba[0] > 255) {
					rgba[0] = 255;
				}

				if (rgba[1] > 255) {
					rgba[1] = 255;
				}

				if (rgba[2] > 255) {
					rgba[2] = 255;
				}

				rasterImgOut.setPixel(x, y, rgba);
			}

		}
		return imgOut;
	}

	private BufferedImage BlackMode(BufferedImage imgIn) {
		BufferedImage imgOut = new BufferedImage(imgIn.getWidth(), imgIn.getHeight(), imgIn.getType());
		WritableRaster rasterImgIn = imgIn.getRaster();
		WritableRaster rasterImgOut = imgOut.getRaster();
		int[] rgba = new int[4];
		for (int x = 0; x < imgIn.getWidth(); x++) {
			for (int y = 0; y < imgIn.getHeight(); y++) {
				rasterImgIn.getPixel(x, y, rgba);
				rgba[0] = (int) (rgba[0] * 0.9);
				rgba[1] = (int) (rgba[1] * 0.9);
				rgba[2] = (int) (rgba[2] * 0.9);

				rasterImgOut.setPixel(x, y, rgba);
			}
		}
		return imgOut;
	}

}

class MyPanelAplication extends JPanel implements KeyListener, Runnable, MouseListener, MouseMotionListener {

	int[] rules = { AlphaComposite.SRC_OVER, AlphaComposite.CLEAR, AlphaComposite.SRC_IN, AlphaComposite.DST_OUT,
			AlphaComposite.SRC };
	int ruleIndex;
	int position;
	float opacity = 0.4f;
	int Gradient = 0;
	Shape rectangle;
	Point2D.Double[] pts = new Point2D.Double[100];

	public MyPanelAplication() {
		setPreferredSize(new Dimension(1350, 625));
		setBackground(Color.white);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);

		Random random = new Random();

		for (int i = 0; i < pts.length; i++) {
			pts[i] = new Point2D.Double(Math.random(), Math.random());
		}

		Thread thread = new Thread(this);
		thread.start();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		requestFocus();

		Color startColor = new Color(0, 159, 224);
		Color finishedColor = new Color(2, 9, 111);

		GradientPaint gp = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), finishedColor, false);
		g2.setPaint(gp);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.black);

		for (int i = 0; i < pts.length; i++) {
			int x = (int) (1350 * pts[i].x);
			int y = (int) (625 * pts[i].y);
			int size = (int) (50 * Math.random());

			GeneralPath diamond = new GeneralPath();
			diamond.moveTo(x, y + size);
			diamond.lineTo(x - size / 2, y);
			diamond.lineTo(x, y - size);
			diamond.lineTo(x + size / 2, y);
			diamond.closePath();

			g2.draw(diamond);
		}

		rectangle = new Rectangle2D.Double(75, 10, 525, 60);
		Rectangle2D rectangle2 = new Rectangle2D.Double(85, 20, 505, 40);

		Area areaRectangle = new Area(rectangle);
		Area areaRectangle2 = new Area(rectangle2);

		areaRectangle.subtract(areaRectangle2);

		switch (Gradient) {
		case 0: {
			GradientPaint gp2 = new GradientPaint(75, 10, Color.black, 100, 35, Color.gray, true);
			g2.setPaint(gp2);
			g2.fill(areaRectangle);
			break;
		}

		case 1: {
			GradientPaint gp2 = new GradientPaint(75, 10, Color.white, 100, 35, Color.gray, true);
			g2.setPaint(gp2);
			g2.fill(areaRectangle);
			break;
		}

		}

		String text = "Product Dashboard  December";
		Font font2 = new Font("Arial", Font.BOLD, 34);
		FontRenderContext fcr2 = g2.getFontRenderContext();

		g2.setFont(font2);
		g2.setColor(Color.white);
		g2.drawString(text, 90, 50);
		AlphaComposite ac = AlphaComposite.getInstance(rules[ruleIndex], opacity);
		g2.setComposite(ac);

		Shape ellipse = new Ellipse2D.Double(1050, 510, 110, 110);
		g2.setClip(ellipse);

		BufferedImage texture = ProjectFunction.getImage(this, "Images/box.png");
		Shape box = new Rectangle2D.Double(1050, 510, 110, 110);
		AffineTransform tx = new AffineTransform();
		tx.scale(box.getBounds().getWidth() / texture.getWidth(), box.getBounds().getHeight() / texture.getHeight());
		TexturePaint tp = new TexturePaint(texture, box.getBounds());
		g2.setPaint(tp);
		g2.fill(box);

		g2.setClip(null);

		String product = "Product 1";
		Font font = new Font("Serif", Font.BOLD, 50);
		g2.setFont(font);
		g2.setColor(Color.red);
		g2.drawString(product, 900, 580);

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		switch (keyCode) {

		case KeyEvent.VK_UP: {
			ruleIndex++;
			ruleIndex %= rules.length;
			break;
		}
		case KeyEvent.VK_DOWN: {
			ruleIndex--;
			if (ruleIndex < 0)
				ruleIndex = rules.length - 1;
			break;
		}

		case KeyEvent.VK_LEFT: {
			opacity -= 0.1f;
			if (opacity < 0)
				opacity = 0;
			break;
		}

		case KeyEvent.VK_RIGHT: {
			opacity += 0.1f;
			if (opacity > 1)
				opacity = 1;
			break;
		}

		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		while (true) {
			for (int i = 0; i < pts.length; i++) {
				double x = pts[i].getX();
				double y = pts[i].getY();
				y += 0.05 * Math.random();
				if (y > 1) {
					y = 0.15 * Math.random();
					x = Math.random();
				}
				pts[i].setLocation(x, y);
			}
			repaint();
			try {
				Thread.sleep(250);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (rectangle.contains(e.getPoint())) {
			if (Gradient == 0)
				Gradient = 1;
			else
				Gradient = 0;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

class SetValuesPanel extends JPanel implements MouseListener, MouseMotionListener {

	int spacing = 70;
	int height = 50;
	int thickness = 10;

	int width = 230;

	public static float percentual;
	public static float percentual2;
	public static float percentual3;

	Shape obj = new Rectangle2D.Double(67, 90, 15, 40);
	Shape obj1 = new Rectangle2D.Double(67, 90 + spacing + height, 15, 40);
	Shape obj2 = new Rectangle2D.Double(67, 90 + 2 * spacing + height * 2, 15, 40);

	AffineTransform at = new AffineTransform();

	Point position0[] = new Point[47];
	Point position1[] = new Point[47];
	Point position2[] = new Point[47];
	int r = 4;
	int maxDist = 5;

	int button = -1;
	Boolean selected = false;
	int firstX = 0;
	int firstY = 0;
	int deltaX = 0;
	int deltaY = 0;

	int firstX1 = 0;
	int firstY1 = 0;
	int deltaX1 = 0;
	int deltaY1 = 0;

	int firstX2 = 0;
	int firstY2 = 0;
	int deltaX2 = 0;
	int deltaY2 = 0;

	JButton myButton;

	public SetValuesPanel() {

		setPreferredSize(new Dimension(300, 400));
		setBackground(Color.white);
		addMouseListener(this);
		addMouseMotionListener(this);

		setLayout(null);
		myButton = new JButton("Clear");
		myButton.setBounds(0, 395, 350, 30);
		myButton.setBackground(new Color(255, 50, 50));
		myButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		myButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetObjects();
			}
		});

		add(myButton);

		for (int i = 0; i < 47; i++) {
			position0[i] = new Point(5 * i + spacing, 110);
		}

		for (int i = 0; i < 47; i++) {
			position1[i] = new Point(5 * i + spacing, 110 + spacing + height);
		}

		for (int i = 0; i < 47; i++) {
			position2[i] = new Point(5 * i + spacing, 110 + 2 * spacing + height * 2);
		}

	}

	private void resetObjects() {
		at.setToTranslation(position0[0].x - obj.getBounds().getCenterX(), 0);
		obj = at.createTransformedShape(obj);

		at.setToTranslation(position1[0].x - obj1.getBounds().getCenterX(), 0);
		obj1 = at.createTransformedShape(obj1);

		at.setToTranslation(position2[0].x - obj2.getBounds().getCenterX(), 0);
		obj2 = at.createTransformedShape(obj2);

		percentual = 0;
		percentual2 = 0;
		percentual3 = 0;

		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Limits objs
		int minX = 70;
		int maxX = 285;

		if (button == 0) {
			deltaX = e.getX() - firstX;

			if (firstX + deltaX < minX) {
				deltaX = minX - firstX;
			} else if (firstX + deltaX > maxX) {
				deltaX = maxX - firstX;
			}
			percentual = (float) (firstX + deltaX - minX) / (maxX - minX);

			float degree = percentual * 180;

			at.setToTranslation(deltaX, deltaY);
			obj = at.createTransformedShape(obj);
			firstX = firstX + deltaX;
			firstY = firstY + deltaY;
		}
		if (button == 1) {

			deltaX1 = e.getX() - firstX1;

			if (firstX1 + deltaX1 < minX) {
				deltaX1 = minX - firstX1;
			} else if (firstX1 + deltaX1 > maxX) {
				deltaX1 = maxX - firstX1;
			}
			percentual2 = (float) (firstX1 + deltaX1 - minX) / (maxX - minX);
			at.setToTranslation(deltaX1, deltaY1);
			obj1 = at.createTransformedShape(obj1);
			firstX1 = firstX1 + deltaX1;
			firstY1 = firstY1 + deltaY1;
		}

		if (button == 2) {

			deltaX2 = e.getX() - firstX2;

			if (firstX2 + deltaX2 < minX) {
				deltaX2 = minX - firstX2;
			} else if (firstX2 + deltaX2 > maxX) {
				deltaX2 = maxX - firstX2;
			}
			percentual3 = (float) (firstX2 + deltaX2 - minX) / (maxX - minX);
			at.setToTranslation(deltaX2, deltaY2);
			obj2 = at.createTransformedShape(obj2);
			firstX2 = firstX2 + deltaX2;
			firstY2 = firstY2 + deltaY2;
		}

		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1) {
			if (obj.contains(getMousePosition())) {
				selected = true;
				firstX = e.getX();
				button = 0;
			} else if (obj1.contains(getMousePosition())) {
				selected = true;
				firstX1 = e.getX();
				button = 1;
			} else if (obj2.contains(getMousePosition())) {
				selected = true;
				firstX2 = e.getX();

				button = 2;
			} else {
				selected = false;
				button = -1;
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		selected = false;
		if (button == 0) {
			int p = getNearestPosition();
			if (p != -1) {
				at.setToTranslation(position0[p].x - obj.getBounds().getCenterX(), 0);
				obj = at.createTransformedShape(obj);
				repaint();
			}
		}

		if (button == 1) {
			int p = getNearestPosition1();
			if (p != -1) {
				at.setToTranslation(position1[p].x - obj1.getBounds().getCenterX(), 0);
				obj1 = at.createTransformedShape(obj1);
				repaint();
			}
		}

		if (button == 2) {
			int p = getNearestPosition2();
			if (p != -1) {
				at.setToTranslation(position2[p].x - obj2.getBounds().getCenterX(), 0);
				obj2 = at.createTransformedShape(obj2);
				repaint();
			}
		}

	}

	private int getNearestPosition() {
		int p = -1;
		float min = 9999999.9f;
		double dist = 0f;

		for (int i = 0; i < 47; i++) {
			dist = position0[i].distance(obj.getBounds().getCenterX(), obj.getBounds().getCenterY());
			if (dist <= maxDist) {
				if (dist < min) {
					min = (float) dist;
					p = i;
				}
			}
		}

		return p;
	}

	private int getNearestPosition1() {
		int p = -1;
		float min = 9999999.9f;
		double dist1 = 0f;
		for (int i = 0; i < 47; i++) {

			dist1 = position1[i].distance(obj1.getBounds().getCenterX(), obj1.getBounds().getCenterY());
			if (dist1 <= maxDist) {
				if (dist1 < min) {
					min = (float) dist1;
					p = i;
				}
			}
		}

		return p;
	}

	private int getNearestPosition2() {
		int p = -1;
		float min = 9999999.9f;
		double dist2 = 0f;
		for (int i = 0; i < 47; i++) {

			dist2 = position2[i].distance(obj2.getBounds().getCenterX(), obj.getBounds().getCenterY());
			if (dist2 <= maxDist) {
				if (dist2 < min) {
					min = (float) dist2;
					p = i;
				}
			}
		}

		return p;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.gray);
		g2.translate(175, 40 + spacing);

		Shape drag1 = new DrawDrag(0, 0, width, height, thickness);
		g2.fill(drag1);
		g2.translate(0, spacing + height);

		Shape drag2 = new DrawDrag(0, 0, width, height, thickness);
		g2.fill(drag2);

		g2.translate(0, spacing + height);

		Shape drag3 = new DrawDrag(0, 0, width, height, thickness);
		g2.fill(drag3);

		g2.translate(-175, -(40 + 3 * spacing + 2 * height));

		g2.setColor(Color.blue);
		g2.fill(obj);
		g2.fill(obj1);
		g2.fill(obj2);

		String title = "Insert the information";
		Font font21 = new Font("Sans-Serif", Font.BOLD, 30);
		FontRenderContext fcr2 = g2.getFontRenderContext();
		g2.setFont(font21);
		g2.setColor(Color.black);
		ProjectFunction.drawCenteredText(g2, font21, fcr2, title, 175, 35);

		Font font1 = new Font("Sans-Serif", Font.BOLD, 20);
		String tile = "Stock Level: ";
		String tile2 = "Orders: ";
		String tile3 = "Returns: ";
		g2.setFont(font1);
		g2.setColor(Color.blue);
		g2.drawString(tile, 60, spacing + height - 50);
		g2.drawString(tile2, 60, 2 * spacing + 2 * height - 50);
		g2.drawString(tile3, 60, 3 * spacing + 3 * height - 50);

	}

}

class ImagePanel extends JPanel {

	int panelWidth;
	int panelHeight;
	BufferedImage Image;

	public ImagePanel() {
		setPreferredSize(new Dimension(250, 250));
		setBackground(Color.white);
		panelWidth = getPreferredSize().width;
		panelHeight = getPreferredSize().height;
		BufferedImage Image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
		fillImage(Image);
		setImage(Image);

	}

	public void setImage(BufferedImage bi) {
		Image = bi;
		setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
		invalidate();
		repaint();

	}

	private BufferedImage fillImage(BufferedImage imgIn) {
		int[] rgba = new int[4];
		WritableRaster rasterImgIn = imgIn.getRaster();

		for (int x = 0; x < imgIn.getWidth(); x++) {
			for (int y = 0; y < imgIn.getHeight(); y++) {

				rgba[0] = 127;
				rgba[1] = 127;
				rgba[2] = 127;
				rgba[3] = 255;

				if ((x / 15 + y / 15) % 2 == 0) {
					rgba[0] = 0;
					rgba[1] = 0;
					rgba[2] = 0;
				}

				rasterImgIn.setPixel(x, y, rgba);
			}
		}
		return imgIn;
	}

	public BufferedImage getImage() {

		return Image;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, panelWidth, panelHeight);

		if (Image != null) {
			Image scaledImage = Image.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
			g2.drawImage(scaledImage, 0, 0, this);
		}
	}
}

class ProfitGrapPanel extends JPanel implements Runnable, Printable {

	int height = 240;
	int height3;
	int height2;
	float percentual1, percentual2, percentual3;
	int[] ValuesY;
	int[] ValuesX;
	int thickness = 5;
	Shape grap;
	Shape lineColision = new Rectangle2D.Double(469, 55, 525, 1);

	public ProfitGrapPanel() {
		setPreferredSize(new Dimension(250, 525));
		setBackground(Color.white);

		ValuesY = new int[13];
		ValuesX = new int[13];
		int x = 50;
		Random random = new Random();

		for (int i = 0; i < 13; i++) {
			if (i == 12) {
				ValuesY[i] = height;
			} else {
				ValuesY[i] = random.nextInt(90) + 60;
			}

			ValuesX[i] = x + i * 35;
		}
		Thread thread = new Thread(this);
		thread.start();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		String month[] = new String[13];
		String percentual[] = new String[6];

		month[0] = "Jan";
		month[1] = "Feb";
		month[2] = "Mar";
		month[3] = "Abr";
		month[4] = "Mai";
		month[5] = "Jun";
		month[6] = "Jul";
		month[7] = "Ago";
		month[8] = "Set";
		month[9] = "Out";
		month[10] = "Nov";
		month[11] = "Dec";

		percentual[4] = "100%";
		percentual[3] = " 75%";
		percentual[2] = " 50%";
		percentual[1] = " 25%";
		percentual[0] = " 0%";

		for (int i = 0; i < 12; i++) {
			Font font = new Font("Sans-Serif", Font.BOLD, 14);

			g2.setFont(font);
			g2.setColor(Color.black);
			g2.drawString(month[i], 75 + 35 * i, 240);

		}

		for (int i = 0; i < 5; i++) {
			Font font = new Font("Sans-Serif", Font.BOLD, 14);

			g2.setFont(font);
			g2.setColor(Color.black);
			g2.drawString(percentual[i], 20, 220 - i * 40);

		}

		grap = new DrawGraphProfit(60, 220, ValuesX, ValuesY);
		g2.setColor(Color.blue);
		g2.fill(grap);

		g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(new Color(192, 217, 217, 188));
		g2.draw(grap);

		// 100% proffit
		if (lineColision.intersects(grap.getBounds2D())) {

			g2.setColor(Color.green);
			g2.fill(grap);

			g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.setColor(new Color(144, 238, 144, 188));
			g2.draw(grap);

		}

		String title = "Monthly Product Profit";
		Font font21 = new Font("Sans-Serif", Font.BOLD, 30);
		FontRenderContext fcr2 = g2.getFontRenderContext();
		g2.setFont(font21);
		g2.setColor(Color.black);

		FontRenderContext fcr = g2.getFontRenderContext();
		ProjectFunction.drawCenteredText(g2, font21, fcr, title, 275, 30);

		float thickness = 3.0f;
		g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawLine(50, 220, 500, 220);
		g2.drawLine(495, 215, 500, 220);
		g2.drawLine(495, 225, 500, 220);
		g2.drawLine(60, 230, 60, 35);
		g2.drawLine(55, 40, 60, 35);
		g2.drawLine(65, 40, 60, 35);

	}

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0)

			return Printable.NO_SUCH_PAGE;

		Graphics2D g2 = (Graphics2D) graphics;
		double sclaceX = pageFormat.getImageableWidth() / getWidth();
		double sclaceY = pageFormat.getImageableHeight() / getHeight();
		if (sclaceX < sclaceY)
			g2.scale(sclaceX, sclaceY);
		else
			g2.scale(sclaceY, sclaceX);
		paintComponent(g2);
		return Printable.PAGE_EXISTS;
	}

	@Override
	public void run() {
		while (true) {

			percentual2 = aplication.SetValuesPanel.percentual2;
			percentual3 = aplication.SetValuesPanel.percentual3;

			height2 = (int) ((int) 170 * (-percentual2));
			height3 = (int) ((int) 170 * (percentual3));

			int newHeight = (int) (height2 + height3 + 220);

			if (newHeight > 220)
				newHeight = 220;

			if (height != newHeight) {
				if (height < newHeight) {

					height = (height + 1);
					ValuesY[12] = height;

				} else {
					height = (height - 1);
					ValuesY[12] = height;
				}

			}
			repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

class RectGrapPanel extends JPanel implements Runnable, Printable {
	BufferedImage image = null;
	String quantity[] = new String[6];

	int newHeight, newHeight2, newHeight3;
	float percentual1, percentual2, percentual3;
	int sizerect = 40;
	int space = 30;
	int thickness = 4;
	int quantity1, quantity2, quantity3;

	Shape rect1;
	Shape rect2;
	Shape rect3;
	Shape lineColision = new Rectangle2D.Double(90, 70, 400, 1);

	public RectGrapPanel() {
		setPreferredSize(new Dimension(250, 525));
		setBackground(Color.white);
		Thread thread = new Thread(this);
		thread.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int panelWidth = getWidth(); // Largura do painel
		int panelHeight = getHeight(); // Altura do painel

		quantity[4] = "1000";
		quantity[3] = " 750";
		quantity[2] = " 500";
		quantity[1] = " 250";
		quantity[0] = "   0";

		Font font = new Font("Sans-Serif", Font.BOLD, 14);
		g2.setFont(font);

		for (int i = 0; i < 5; i++) {
			g2.drawString(quantity[i], 20, 220 - i * 36);
		}

		g2.drawString(Integer.toString(quantity1), 3 + (space + sizerect), 235);
		g2.drawString(Integer.toString(quantity2), 3 + (space + sizerect) * 2, 235);
		g2.drawString(Integer.toString(quantity3), 3 + (space + sizerect) * 3, 235);

		rect1 = new DrawRectGrap(sizerect + space, 220, sizerect, newHeight);
		rect2 = new DrawRectGrap(2 * sizerect + 2 * space, 220, sizerect, newHeight2);
		rect3 = new DrawRectGrap(3 * sizerect + 3 * space, 220, sizerect, newHeight3);

		g2.setColor(Color.red);
		g2.fill(rect1);
		g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(new Color(255, 105, 97, 188));
		g2.draw(rect1);

		g2.setColor(Color.yellow);
		g2.fill(rect2);
		g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(new Color(253, 253, 150, 188));
		g2.draw(rect2);

		g2.setColor(Color.green);
		g2.fill(rect3);
		g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(new Color(144, 238, 144, 188));
		g2.draw(rect3);

		g2.setColor(Color.black);

		float thickness2 = 3.0f;
		g2.setStroke(new BasicStroke(thickness2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawLine(50, 220, 290, 220);
		g2.drawLine(285, 215, 290, 220);
		g2.drawLine(285, 225, 290, 220);
		g2.drawLine(60, 230, 60, 35);
		g2.drawLine(55, 40, 60, 35);
		g2.drawLine(50, 220, 290, 220);
		g2.drawLine(50, 220, 290, 220);
		g2.drawLine(65, 40, 60, 35);

		String title = "Product Quantity";
		Font font21 = new Font("Sans-Serif", Font.BOLD, 30);
		FontRenderContext fcr2 = g2.getFontRenderContext();
		g2.setFont(font21);
		g2.setColor(Color.black);
		ProjectFunction.drawCenteredText(g2, font21, fcr2, title, 275, 30);

		Shape Frame = new Rectangle2D.Double(285, 75, 220, 115);
		BufferedImage texture = ProjectFunction.getImage(this, "Images/Frame.png");
		AffineTransform tx = new AffineTransform();
		tx.scale(Frame.getBounds().getWidth() / texture.getWidth(),
				Frame.getBounds().getHeight() / texture.getHeight());
		TexturePaint tp = new TexturePaint(texture, Frame.getBounds());
		g2.setPaint(tp);
		g2.fill(Frame);
		g2.setPaint(null);

		Font font2 = new Font("Sans-Serif", Font.BOLD, 18);

		g2.setFont(font2);
		g2.setColor(Color.black);
		g2.drawString("Legend: ", 305, 100);

		Font font3 = new Font("Sans-Serif", Font.BOLD, 16);
		g2.setFont(font3);
		g2.drawString("Number on Stock  ", 325, 124);
		g2.drawString("Number of Orders", 325, 149);
		g2.drawString("Number of Returns", 325, 174);

		g2.setStroke(new BasicStroke(thickness2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		Shape RectRed = new Rectangle2D.Double(305, 112, 12, 12);
		g2.setColor(Color.red);
		g2.fill(RectRed);
		g2.setColor(Color.black);
		g2.draw(RectRed);

		Shape RectYellow = new Rectangle2D.Double(305, 137, 12, 12);
		g2.setColor(Color.yellow);
		g2.fill(RectYellow);
		g2.setColor(Color.black);
		g2.draw(RectYellow);

		Shape RectGreen = new Rectangle2D.Double(305, 162, 12, 12);
		g2.setColor(Color.green);
		g2.fill(RectGreen);
		g2.setColor(Color.black);
		g2.draw(RectGreen);

	}

	@Override
	public void run() {

		while (true) {

			percentual1 = aplication.SetValuesPanel.percentual;
			percentual2 = aplication.SetValuesPanel.percentual2;
			percentual3 = aplication.SetValuesPanel.percentual3;

			quantity1 = (int) (percentual1 * 1000);
			quantity2 = (int) (percentual2 * 1000);
			quantity3 = (int) (percentual3 * 1000);

			newHeight = (int) ((int) 155 * (percentual1));
			newHeight2 = (int) ((int) 155 * (percentual2));
			newHeight3 = (int) ((int) 155 * (percentual3));

			if (rect1 != null && lineColision.intersects(rect1.getBounds2D())) {
				newHeight = 150;
			}

			if (rect2 != null && lineColision.intersects(rect2.getBounds2D())) {
				newHeight2 = 150;
			}

			if (rect3 != null && lineColision.intersects(rect3.getBounds2D())) {
				newHeight3 = 150;
			}

			if (newHeight3 < 10)
				newHeight3 = 10;

			if (newHeight3 != ((int) 155 * (percentual3))) {
				if (newHeight3 < ((int) 155 * (percentual3))) {

					newHeight3 = (newHeight3 + 1);

				}
				if (newHeight3 > ((int) 155 * (percentual3))) {

					newHeight3 = (newHeight3 - 1);

				}

			}

			if (newHeight2 < 10)
				newHeight2 = 10;

			if (newHeight2 != ((int) 155 * (percentual2))) {
				if (newHeight2 < ((int) 155 * (percentual2))) {

					newHeight2 = (newHeight2 + 1);

				}
				if (newHeight2 > ((int) 155 * (percentual2))) {

					newHeight2 = (newHeight2 - 1);

				}

			}

			if (newHeight < 10)
				newHeight = 10;

			if (newHeight != ((int) 155 * (newHeight))) {
				if (newHeight < ((int) 155 * (percentual1))) {

					newHeight = (newHeight + 1);

				}
				if (newHeight > ((int) 155 * (percentual1))) {

					newHeight = (newHeight - 1);

				}

			}

			repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0)
			return Printable.NO_SUCH_PAGE;

		Graphics2D g2 = (Graphics2D) graphics;
		double sclaceX = pageFormat.getImageableWidth() / getWidth();
		double sclaceY = pageFormat.getImageableHeight() / getHeight();
		if (sclaceX < sclaceY)
			g2.scale(sclaceX, sclaceY);
		else
			g2.scale(sclaceY, sclaceX);
		paintComponent(g2);
		return Printable.PAGE_EXISTS;
	}

}

class StockLevelPanel extends JPanel implements Runnable, Printable {
	String formattedStockPercentual;
	float degreeActual;

	AffineTransform at = new AffineTransform();

	public StockLevelPanel() {
		setPreferredSize(new Dimension(250, 250));
		setBackground(Color.white);
		Thread thread = new Thread(this);
		thread.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// 125,175
		Shape SemiCirculeExt = new DrawSemiCircule(0, 0, 100);
		Shape SemiCirculeInt = new DrawSemiCircule(0, 0, 75);

		Area Areagrap4Ext = new Area(SemiCirculeExt);
		Area Areagrap4Int = new Area(SemiCirculeInt);

		Areagrap4Ext.subtract(Areagrap4Int);

		g2.translate(125, 175);
		g2.setColor(Color.GRAY);
		g2.fill(Areagrap4Ext);

		g2.translate(-100, -10);
		// g2.translate(25, 165);

		Shape ellipse = new Ellipse2D.Double(0, 0, 100, 20);

		at.setToRotation(Math.toRadians(degreeActual), 100, 10);
		ellipse = at.createTransformedShape(ellipse);
		g2.setColor(Color.blue);
		g2.fill(ellipse);

		g2.translate(100, 20);
		Shape SemiCircule = new DrawSemiCircule(0, 0, 45);
		g2.setColor(Color.gray);
		g2.fill(SemiCircule);

		g2.translate(-100, 5);
		// g2.translate(25,190 );

		Shape rect = new Rectangle2D.Double(0, 0, 200, 30);
		g2.setPaint(Color.black);
		g2.fill(rect);
		g2.translate(-25, -190);
		Font font = new Font("Sans-Serif", Font.BOLD, 30);
		FontRenderContext fcr = g2.getFontRenderContext();
		g2.setFont(font);
		g2.setColor(Color.black);
		ProjectFunction.drawCenteredText(g2, font, fcr, "Stock level", 125, 50);

		Font font2 = new Font("Sans-Serif", Font.BOLD, 30);
		FontRenderContext fcr2 = g2.getFontRenderContext();
		g2.setFont(font);
		g2.setColor(Color.white);

		ProjectFunction.drawCenteredText(g2, font2, fcr2, formattedStockPercentual + "%", 125, 215);
	}

	@Override
	public void run() {

		while (true) {
			float percentual1 = aplication.SetValuesPanel.percentual;
			float StockPercentual = percentual1 * 100;
			formattedStockPercentual = String.format("%.1f", StockPercentual);

			if (degreeActual != Math.abs(percentual1 * 180)) {
				if (degreeActual < Math.abs(percentual1 * 180)) {
					degreeActual = (degreeActual + 2f);

				}
				if (degreeActual > Math.abs(percentual1 * 180)) {
					degreeActual = (degreeActual - 2f);

				}

			}
			repaint();

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0)
			return Printable.NO_SUCH_PAGE;

		Graphics2D g2 = (Graphics2D) graphics;
		double sclaceX = pageFormat.getImageableWidth() / getWidth();
		double sclaceY = pageFormat.getImageableHeight() / getHeight();
		if (sclaceX < sclaceY)
			g2.scale(sclaceX, sclaceY);
		else
			g2.scale(sclaceY, sclaceX);
		paintComponent(g2);
		return Printable.PAGE_EXISTS;
	}

}
