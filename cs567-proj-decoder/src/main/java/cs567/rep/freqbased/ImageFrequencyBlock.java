package cs567.rep.freqbased;

import java.awt.Color;

import org.jtransforms.dct.FloatDCT_2D;

import cs567.rep.base.BaseBlock;
import cs567.rep.blockbased.ImageBlock;

public class ImageFrequencyBlock extends BaseBlock {
	FrequencyChannels channels;	
	
	private FloatDCT_2D dct;
	
	public ImageFrequencyBlock(boolean foreground) {
		setForeground(foreground);
		this.channels = new FrequencyChannels();
	}
	
	public ImageFrequencyBlock(boolean foreground, FrequencyChannels channels) {
		setForeground(foreground);
		this.channels = channels;
	}
	
	public ImageFrequencyBlock(ImageBlock block) {
		setForeground(block.isForeground());
		channels = new FrequencyChannels();
		
		dct = new FloatDCT_2D(block.getBlockSize(), block.getBlockSize());
		channels.red = convertToFrequency(block.getChannels().red);
		
		dct = new FloatDCT_2D(block.getBlockSize(), block.getBlockSize());
		channels.green = convertToFrequency(block.getChannels().green);
		
		dct = new FloatDCT_2D(block.getBlockSize(), block.getBlockSize());
		channels.blue = convertToFrequency(block.getChannels().blue);
	}
	
	private float[][] convertToFrequency(short[][] arr) {
		float[][] f = new float[arr.length][arr.length];
		
		for(int y = 0; y < arr[0].length; y++) {
			for(int x = 0; x < arr.length; x++) { 
				f[x][y] = arr[x][y];
			}
		}
		
		dct.forward(f, true);
		
		return f;
	}
	
	@Override
	public int getBlockSize() {
		return channels.red.length;
	}
	
	public int[][] convertToPixels(int q) {
		float[][] fr = new float[channels.red.length][];
		float[][] fg = new float[channels.green.length][];
		float[][] fb = new float[channels.blue.length][];
		
		for (int i = 0; i < channels.red.length; i++) {
			fr[i] = channels.red[i].clone();
			fg[i] = channels.green[i].clone();
			fb[i] = channels.blue[i].clone();
		}
		
		if (q > 1) {
			for (int y = 0; y < channels.red.length; y++) {
				for (int x = 0; x < channels.red[0].length; x++) {
					fr[x][y] = Math.round(fr[x][y] / q) * q;
					fg[x][y] = Math.round(fg[x][y] / q) * q;
					fb[x][y] = Math.round(fb[x][y] / q) * q;
				}
			}
		}
		
		dct = new FloatDCT_2D(channels.red.length, channels.red.length);
		dct.inverse(fr, true);
		
		dct = new FloatDCT_2D(channels.red.length, channels.red.length);
		dct.inverse(fg, true);
		
		dct = new FloatDCT_2D(channels.red.length, channels.red.length);
		dct.inverse(fb, true);
		
		int[][] pixels = new int[channels.red.length][channels.red.length];
		
		for (int y = 0; y < pixels[0].length; y++) {
			for (int x = 0; x < pixels.length; x++) {
				Color c = new Color(correctColor(fr[x][y]), correctColor(fg[x][y]), correctColor(fb[x][y]));
				pixels[x][y] = c.getRGB();
			}
		}
		return pixels;
	}
	
	private int correctColor(float f) {
		if (f > 255) {
			return 255;
		}
		
		if (f < 0) {
			return 0;
		}
		
		return (int)f;
	}
	
	public void debug() {
		System.out.println("Freq-Red");
		printArray2D(channels.red);
		
		float[][] fr = new float[channels.red.length][];
		for (int i = 0; i < channels.red.length; i++) {
			fr[i] = channels.red[i].clone();
		}
		
		dct = new FloatDCT_2D(channels.red.length, channels.red.length);
		dct.inverse(fr, true);
		
		System.out.println("Inversed-Red");
		printArray2D(fr);
	}
	
	private void printArray2D(float[][] a) {
		for (int y = 0; y < a[0].length; y++) {
			for (int x = 0; x < a.length; x++) {
				System.out.print(a[x][y] + "\t");
			}
			System.out.println();
		}
	}
}
