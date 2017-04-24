package cs567.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cs567.rep.blockbased.Channels;

public class ImageUtils {
	
	public static void saveBitmap(BufferedImage img, String path) {
		File outputfile = new File(path);
		try {
			ImageIO.write(img, "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int[][] bytesToRGB(int width, int height, byte[] bytes) {
		int[][] pixels = new int[width][height];
		
		int ind = 0;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2]; 

				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				pixels[x][y] = pix;
				ind++;
			}
		}
		
		return pixels;
	}
	
	public static byte[] RGBToBytes(int width, int height, int[][] pixels) {
		byte[] bytes = new byte[width * height * 3];
		
		int ind = 0;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				Color c = new Color(pixels[x][y]);
				
				bytes[ind] = (byte)c.getRed();
				bytes[ind+height*width] = (byte)c.getGreen();
				bytes[ind+height*width*2] = (byte)c.getBlue();
				ind++;
			}
		}
		
		return bytes;
	}
	
	public static Channels RGBToChannel(int width, int height, int[][] pixels)  {
		Channels cn = new Channels();
		
		short[][] r = new short[width][height];
		short[][] g = new short[width][height];
		short[][] b = new short[width][height];
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				Color c = new Color(pixels[x][y]);
				
				r[x][y] = (short)c.getRed();
				g[x][y] = (short)c.getGreen();
				b[x][y] = (short)c.getBlue();
			}
		}
		
		cn.red = r;
		cn.green = g;
		cn.blue = b;
		
		return cn;
	}
	
}

