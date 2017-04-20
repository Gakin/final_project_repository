package cs567.rep.blockbased;

import java.util.ArrayList;
import java.util.List;

import cs567.rep.base.BaseBlockFrame;

public class ImageBlockFrame extends BaseBlockFrame {

	List<ImageBlock> blocks;

	@Override
	public int getBlockSize() {
		return blocks.get(0).getBlockSize();
	}
	
	public ImageBlockFrame() {
		blocks = new ArrayList<>();
	}
	
	public ImageBlockFrame(int[][]pixels, int blockSize) {
		this();
		setWidth(pixels.length);
		setHeight(pixels[0].length);
		
		for(int y = 0; y < pixels[0].length; y += blockSize) {
			for(int x = 0; x < pixels.length; x += blockSize) {
				ImageBlock ib = new ImageBlock(pixels, blockSize, x, y);
				blocks.add(ib);
			}
		}
	}
	
	public List<ImageBlock> getBlockList() {
		return blocks;
	}

	@Override
	public int getBlockCount() {
		return blocks.size();
	}
	
}
