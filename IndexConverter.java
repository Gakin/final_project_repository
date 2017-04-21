package project1;

public class IndexConverter {
	protected int iWidth = 960;
	protected int iHeight = 540;
	protected int BlockSize = 16;
	protected int imgX;
	protected int imgY;
	protected int blkX;
	protected int blkY;
	
	public IndexConverter(int width, int height, int BlockSize) {
		this.iWidth  = width;
		this.iHeight = height;
		this.BlockSize = BlockSize;
	}
	
	public int getFrameIndex(int bX, int bY, int iX, int iY){
		int h=((bY * this.BlockSize) + iY) * this.iWidth + bX * this.BlockSize + iX;
		return h;
	}

	public int getFrameIndex(int iX, int iY){
		int h = iY * this.iWidth + iX;;
		return h;
	}
	
	public void calculateImageIndex(int h){
		this.imgX = Math.floorMod(h, this.iWidth);
		this.imgY = (int)(h / iWidth);
	}

	public void calculateBlockIndexes(int h){
		this.imgX = Math.floorMod(h, this.iWidth);
		this.imgY = (int)(h / iWidth);
		
		this.blkX = (int)(Math.floorMod(h, this.iWidth)/this.BlockSize);
		this.blkY = (int)((h / this.iWidth)/this.BlockSize);

		this.imgX = Math.floorMod(Math.floorMod(h, this.iWidth),this.BlockSize);
		this.imgY = Math.floorMod((int)(h / this.iWidth),this.BlockSize);
	}
	
	public int getBlockXIndex(int imgX,int imgY ){
		return (int)(imgX/this.BlockSize);
	}

	public int getBlockYIndex(int imgX,int imgY ){
		return (int)(imgY/this.BlockSize);
	}
}
