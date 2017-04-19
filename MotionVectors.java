package project1;


class Block{
	protected int N;
	protected byte [][] iBlock;
	protected int XVector;
	protected int YVector;
	protected boolean background;

	public Block(int Size) {   
		this.N = Size;
		iBlock = new byte [N][N];
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
	protected int    length;
	protected byte [] IFrame;
	protected BlockList curFrame;
	protected BlockList refFrame;

	public MotionVectors(byte[] IFrame, byte[]  RFrame,int width, int height) {
		this.width  = width;
		this.height = height;
		this.length = width * height;
		this.IFrame = IFrame;

		curFrame =  new BlockList(16,width,height);
		refFrame =  new BlockList(16,width,height);

		int h=0;
		for (int blockY=0;blockY<curFrame.numY;blockY++){
			for(int blockX=0;blockX<curFrame.numX;blockX++){
				for (int indY =0;indY<16;indY++){
					for (int indX=0;indX<16;indX++){
						h = (blockY * 16 + indY) * width + blockX * 16 + indX;
						if (h<length) {						
							curFrame.iBlocks[blockX][blockY].iBlock[indX][indY] = IFrame[h];
							refFrame.iBlocks[blockX][blockY].iBlock[indX][indY] = RFrame[h];
						}
						else{
							break;
						}
					}
				}
			}
		}
	}



	public void SumAbsoluteDifference(){
		int SAD = 0;
		int min = 100000;
		int SearchSize = 16;
		int vecX = 0;
		int vecY = 0;
		for (int rY=0; rY < refFrame.numY; rY++){
			for (int rX = 0; rX < refFrame.numX; rX++){		
				min = 10000;

				int startY = (rY * 16) - SearchSize; 
				int startX = (rX * 16) - SearchSize;
				int endY   = (rY * 16) + SearchSize;
				int endX   = (rX * 16) + SearchSize;
				//	            System.out.println("(" + startX + "," + startY +")     -      " + "(" + endX + "," +  endY +")");

				if (startY < 0) {endY = endY - startY;startY = 0;};
				if (startX < 0) {endX = endX - startX;startX = 0;};
				if (endY > refFrame.h) {endY = refFrame.h;};
				if (endX > refFrame.w) {endX = refFrame.w;};
				//	            System.out.println("C (" + startX + "," + startY +")     -      " + "(" + endX + "," +  endY +")");
				for (int cY = startY; cY < endY; cY++){
					for (int cX = startX; cX < endX; cX++){
						vecX = cX;
						vecY = cY;
						SAD = 0;
						for (int py=0;py<16;py++){
							for (int px=0;px<16;px++){
								SAD = SAD + Math.abs(this.IFrame[cY * this.width + cX] - refFrame.iBlocks[rX][rY].iBlock[px][py]);
							}
						}	            		
					}
				}
				if (min > SAD){
					min = SAD;
					//if (min < 500){
					refFrame.iBlocks[rX][rY].XVector = vecX - rX;
					refFrame.iBlocks[rX][rY].YVector = vecY - rY;								
					//}					
					//System.out.println("SAD :" + SAD + "  Orig : (" + rX + "," + rY+ ")" + " Vec : (" + refFrame.iBlocks[rX][rY].XVector + "," + refFrame.iBlocks[rX][rY].YVector+")");
				}
				if ((refFrame.iBlocks[rX][rY].XVector < 100) && (refFrame.iBlocks[rX][rY].YVector < 100)){
					refFrame.iBlocks[rX][rY].background = false;
					//Added to see the foregorund blocks
					for (int py=0;py<16;py++){
						for (int px=0;px<16;px++){
							refFrame.iBlocks[rX][rY].iBlock[px][py] = -127;
						}
					}
					//End of block
				}
				else{
					refFrame.iBlocks[rX][rY].background = true;

				}
			}
		}

		//				for (int cY = startY; cY < endY; cY++){
		//					for (int cX = startX; cX < endX; cX++){
		//						SAD = 0;
		//						for (int py=0;py<16;py++){
		//							for (int px=0;px<16;px++){
		//								SAD = SAD + Math.abs(curFrame.iBlocks[cX][cY].iBlock[px][py] - refFrame.iBlocks[rX][rY].iBlock[px][py]);
		//							}
		//						}
		//						if (min > SAD){
		//							min = SAD;
		//							//if (min < 500){
		//								refFrame.iBlocks[rX][rY].XVector = cX;
		//								refFrame.iBlocks[rX][rY].YVector = cY;								
		//							//}					
		//						}
		//					}
		//				}




		//	            
		//	            
		//				refFrame.iBlocks[rX][rY].XVector=rX - refFrame.iBlocks[rX][rY].XVector;
		//				refFrame.iBlocks[rX][rY].YVector=rY - refFrame.iBlocks[rX][rY].YVector;	
		//				
		//				if ((refFrame.iBlocks[rX][rY].XVector == 0) && (refFrame.iBlocks[rX][rY].YVector == 0)){
		//					refFrame.iBlocks[rX][rY].background = true;
		//				}
		//				else{
		//					refFrame.iBlocks[rX][rY].background = false;
		//					//Added to see the foregorund blocks
		//					for (int py=0;py<16;py++){
		//						for (int px=0;px<16;px++){
		//							refFrame.iBlocks[rX][rY].iBlock[px][py] = -127;
		//						}
		//					}
		//					//End of block
		//				}

	}
}


