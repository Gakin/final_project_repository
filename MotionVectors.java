package project1;


class Block{
	protected int N;
	protected int XVector;
	protected int YVector;
	protected double VLength;
	protected int VDirection;
	protected boolean background;

	public Block(int Size) {   
		this.N = Size;
	}

}

class BlockList{
	protected int w;
	protected int h;
	protected int N;
	protected int numX;
	protected int numY;
	protected Block [][] iBlocks;

	public BlockList(int Size,int width, int height) {
		this.w  = width;
		this.h  = height;
		this.N  = Size;
		this.numY = (int)((double)height/(double)Size+0.5);
		this.numX = (int)((double)width/(double)Size+0.5);
		this.iBlocks = new Block [numX][numY] ;

		for (int i=0;i<this.numY;i++){
			for (int j=0;j<this.numX;j++){
				iBlocks[j][i] = new Block(N);
			}
		}
	}
}

public class MotionVectors {
	protected int    width;
	protected int    height;
	protected int    blockSize;
	protected int    length;
	protected byte [] IFrame;
	protected byte [] RFrame;
	protected BlockList curFrame;
	protected BlockList refFrame;


	public MotionVectors(byte[] IFrame, byte[]  RFrame,int width, int height,int blockSize) {
		this.width  = width;
		this.height = height;
		this.blockSize = blockSize;
		this.length = width * height;
		this.IFrame = IFrame;
		this.RFrame = RFrame;		
		refFrame =  new BlockList(16,width,height);
	}

	public void SumAbsoluteDifference(){
		int SAD = 0;
		int min = 100000;
		int SearchSize = 8;
		int vecX = 0;
		int vecY = 0;
		IndexConverter conv  = new IndexConverter(this.width,this.height,this.blockSize);
		
		for (int rY=0; rY < refFrame.numY; rY++){
			for (int rX = 0; rX < refFrame.numX; rX++){		
				min = 10000;

				int startY = (rY * 16) - SearchSize; 
				int startX = (rX * 16) - SearchSize;
				int endY   = (rY * 16) + SearchSize;
				int endX   = (rX * 16) + SearchSize;

				if (startY < 0) {endY = endY - startY;startY = 0;};
				if (startX < 0) {endX = endX - startX;startX = 0;};
				if (endY > refFrame.h) {endY = refFrame.h;};
				if (endX > refFrame.w) {endX = refFrame.w;};

				for (int cY = startY; cY < endY; cY++){
					for (int cX = startX; cX < endX; cX++){
						SAD = 0;						
						for (int py=0;py<16;py++){
							for (int px=0;px<16;px++){
								SAD = SAD + Math.abs(Math.abs((int)this.IFrame[conv.getFrameIndex(cX + px,cY+py)]) - Math.abs((int)this.RFrame[conv.getFrameIndex(rX, rY, px, py)]));								
							}
						}	
						if (min > SAD){
							min = SAD;
							vecX = cX;
							vecY = cY;
							refFrame.iBlocks[rX][rY].XVector = vecX - (rX * 16);
							refFrame.iBlocks[rX][rY].YVector = vecY - (rY * 16);
						}
					}
				}

				//Calculate Vector Length
				double vx = (double) refFrame.iBlocks[rX][rY].XVector;
				double vy = (double) refFrame.iBlocks[rX][rY].YVector;
				refFrame.iBlocks[rX][rY].VLength = Math.sqrt(vx *vx + vy *vy);
				
				//Calculate Vector Direction
				if ((refFrame.iBlocks[rX][rY].YVector == 0) && (refFrame.iBlocks[rX][rY].XVector > 0)){
					refFrame.iBlocks[rX][rY].VDirection = 90;
				}
				else if ((refFrame.iBlocks[rX][rY].YVector == 0) && (refFrame.iBlocks[rX][rY].XVector < 0)){
					refFrame.iBlocks[rX][rY].VDirection = 270;
				}
				else if ((refFrame.iBlocks[rX][rY].YVector == 0) && (refFrame.iBlocks[rX][rY].XVector == 0)){
					refFrame.iBlocks[rX][rY].VDirection = 0;
				}
				else if (((refFrame.iBlocks[rX][rY].XVector > 0) && (refFrame.iBlocks[rX][rY].YVector > 0)) ||
				    ((refFrame.iBlocks[rX][rY].XVector > 0) && (refFrame.iBlocks[rX][rY].YVector < 0))){
					refFrame.iBlocks[rX][rY].VDirection = (int)(90 -((Math.atan(refFrame.iBlocks[rX][rY].XVector/refFrame.iBlocks[rX][rY].YVector) / Math.PI) * 180));
				}
				else if (refFrame.iBlocks[rX][rY].YVector != 0){
					refFrame.iBlocks[rX][rY].VDirection = (int)(270 -((Math.atan(refFrame.iBlocks[rX][rY].XVector/refFrame.iBlocks[rX][rY].YVector) / Math.PI) * 180));				
				}

				
				//Decision for background or foreground need to be updated camera move compansation ????
				if (refFrame.iBlocks[rX][rY].VLength < 2.0){
					refFrame.iBlocks[rX][rY].background = true;	
				}
				else{
					refFrame.iBlocks[rX][rY].background = false;
					
					//Added to see the foregorund blocks will be removed
					for (int py=0;py<16;py++){
						for (int px=0;px<16;px++){
							RFrame[conv.getFrameIndex(rX, rY, px,py)]= -127;
						}
					}
					//End of block	
					
				}
			}
		}
	}
}


