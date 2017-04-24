package cs567.rep.simple;

import java.awt.image.BufferedImage;

import cs567.utils.ImageUtils;

public class SimpleFrame {
	private int[][] pixels;
	
	public SimpleFrame(int[][] px) {
		this.pixels = px;
	}
	
	public SimpleFrame(int width, int height, byte[] bytes) {
		pixels = ImageUtils.bytesToRGB(width, height, bytes);
	}
	
	public byte[] toByteArray() {
		return ImageUtils.RGBToBytes(getWidth(), getHeight(), pixels);
	}
	
	public int getWidth() {
		return pixels.length;
	}
	
	public int getHeight() {
		return pixels[0].length;
	}
	
	private BufferedImage img;
	public BufferedImage getBufferedImage() {
		if(img == null) {
			img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			
			for(int y = 0; y < getHeight(); y++){
				for(int x = 0; x < getWidth(); x++){
					img.setRGB(x, y, pixels[x][y]);
				}
			}
		}
		return img;
	}
	
	public int[][] getPixels() {
		return pixels;
	}
	
}
