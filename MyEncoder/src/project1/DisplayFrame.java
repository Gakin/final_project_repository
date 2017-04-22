package project1;

import java.awt.BorderLayout;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class DisplayFrame extends JFrame {
	protected int[]               pixels;
	protected BufferedImage       image;
	protected MyCanvas            canvas;

	public DisplayFrame(byte[] imgBytes,int width,int height) {
		pixels  = new int[width * height];
		int size = width * height;
		byte a = 0;
		
		MyFrame MFrame = new MyFrame(imgBytes,width,height);
		MFrame.convertYUV2RGB();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				byte r = imgBytes[width * y + x];
				byte g = imgBytes[width * y + x + size];
				byte b = imgBytes[width * y + x + size *2];
                pixels[width * y + x] = ((a << 24) + (r << 16) + (g << 8) + b);
			}
		}  
		
		image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		canvas = new MyCanvas();
		JPanel panelCanvas = new JPanel(new BorderLayout());

		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setSize(width, height);

		panelCanvas.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		panelCanvas.setSize(width, height);

		panelCanvas.add(canvas, BorderLayout.CENTER);
		

		add(panelCanvas, BorderLayout.CENTER);
	
		image.getRaster().setDataElements(0, 0, width, height, pixels);
      
		pack();
		setResizable(false);
	}

	private class MyCanvas extends Canvas {

		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(image,0,0, null);
		}

		public void update(Graphics g) {
			paint(g);
		}
	}
}

