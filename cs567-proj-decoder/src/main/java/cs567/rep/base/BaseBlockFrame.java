package cs567.rep.base;

public abstract class BaseBlockFrame {
	int width;
	int height;
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public abstract int getBlockSize();
	public abstract int getBlockCount();
	
	public int numBlocksPerRow() {
		return (int)Math.ceil(1.0 * getWidth()/getBlockSize());
	}
	
	public int numRowsOfBlocks() {
		return (int)Math.ceil(1.0 * getHeight()/getBlockSize());
	}
	
	public int getBlockIndex(int x, int y) {
		int dx = (int)(x/getWidth());
		int dy = (int)(y/getHeight());
		return dy * numBlocksPerRow() + dx;
	}
	
}
