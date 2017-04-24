package cs567.rep.freqbased;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import cs567.rep.base.BaseBlockFrame;
import cs567.rep.blockbased.ImageBlock;
import cs567.rep.blockbased.ImageBlockFrame;

public class ImageFrequencyBlockFrame extends BaseBlockFrame {
	List<ImageFrequencyBlock> blocks;
	
	public ImageFrequencyBlockFrame() {
		blocks = new ArrayList<>();
	}
	
	public ImageFrequencyBlockFrame(ImageBlockFrame frame) {
		this();
		setWidth(frame.getWidth());
		setHeight(frame.getHeight());
		
		for(ImageBlock ib : frame.getBlockList()) {
			ImageFrequencyBlock ifb = new ImageFrequencyBlock(ib);
			this.blocks.add(ifb);
		}
	}
	
	public ImageFrequencyBlockFrame(int width, int height, byte[] b, int numBlocks, int blockSize) {
		this();
		setWidth(width);
		setHeight(height);
		
		fromByteArray(b, numBlocks, blockSize);
	}

	@Override
	public int getBlockSize() {
		return blocks.get(0).getBlockSize();
	}
	
	@Override
	public int getBlockCount() {
		return blocks.size();
	}
	
	public void addBlock(ImageFrequencyBlock b) {
		blocks.add(b);
	}
	
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(getBlockCount() * (1 + (3 * 4 * getBlockSize() * getBlockSize()))).order(ByteOrder.BIG_ENDIAN);
		
		for (ImageFrequencyBlock block : blocks) {
			if(block.isForeground()) {
				buffer.put((byte)1);
			} else {
				buffer.put((byte)0);
			}
			
			putCoefficient(buffer, block.channels.red);
			putCoefficient(buffer, block.channels.green);
			putCoefficient(buffer, block.channels.blue);
		}
//		System.out.println("Frame Buffer size = " + buffer.capacity() + " Remaining = " + buffer.remaining());
		return buffer.array();
	}
	
	private void fromByteArray(byte[] b, int numBlocks, int blockSize) {
		int offset = 0;
//		System.out.println("Frame byte size = " + b.length);
		for(int i = 0; i < numBlocks; i++) {
			boolean foreground = false;
			FrequencyChannels channels = new FrequencyChannels();
			
			if (b[offset] == (byte)1) {
				foreground = true;
			}
			
			offset++;
			
			channels.red = readCoefficient(b, offset, blockSize);
			offset += 4 * blockSize * blockSize;
			
			channels.green = readCoefficient(b, offset, blockSize);
			offset += 4 * blockSize * blockSize;
			
			channels.blue = readCoefficient(b, offset, blockSize);
			offset += 4 * blockSize * blockSize;
			
			
			ImageFrequencyBlock block = new ImageFrequencyBlock(foreground, channels);
			addBlock(block);
		}
	}
	
	private float[][] readCoefficient(byte[] b, int offset, int blockSize) {
		float[][] cf = new float[blockSize][blockSize];
		
		for (int y = 0; y < blockSize; y++) {
			for (int x = 0; x < blockSize; x++) {
				float f = ByteBuffer.wrap(b, offset, 4).order(ByteOrder.BIG_ENDIAN).getFloat();
				cf[x][y] = f;
				offset += 4;
			}
		}
		
		return cf;
	}
	
	private void putCoefficient(ByteBuffer buffer, float[][] a) {
		for (int y = 0; y < a[0].length; y++) {
			for (int x = 0; x < a.length; x++) {
				buffer.putFloat(a[x][y]);
			}
		}
	}
}
