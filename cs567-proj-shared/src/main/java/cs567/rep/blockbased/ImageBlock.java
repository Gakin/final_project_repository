package cs567.rep.blockbased;

import cs567.rep.base.BaseBlock;
import cs567.utils.ImageUtils;

public class ImageBlock extends BaseBlock {	
	Channels channels;	
	
	public ImageBlock(int[][] pixels) {
		this(pixels, pixels.length, 0, 0);
	}
	
	public ImageBlock(int[][] pixels, int blockSize, int startX, int startY) {
		int[][] block = new int[blockSize][blockSize];
		
		for(int y = startY; y < Math.min(startY + blockSize, pixels[0].length); y++) {
			for(int x = startX; x < Math.min(startX + blockSize, pixels.length); x++) {
				block[x - startX][y - startY] = pixels[x][y];
			}
		}
		
		this.channels = ImageUtils.RGBToChannel(blockSize, blockSize, block);
	}
	
	public Channels getChannels() {
		return channels;
	}
	
	@Override
	public int getBlockSize() {
		return channels.red.length;
	}
	
	public void debug() {
		System.out.println("Red");
		printArray2D(channels.red);
	}
	
	private void printArray2D(short[][] a) {
		for (int y = 0; y < a[0].length; y++) {
			for (int x = 0; x < a.length; x++) {
				System.out.print(a[x][y] + "\t");
			}
			System.out.println();
		}
	}
	
}
