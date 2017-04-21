package project1;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.SwingConstants;

//import project1.DisplayFrame;
import project1.MotionVectors;
import project1.IndexConverter;

import project1.Dct2d;
import project1.Matrix;

public class MyEncoder {

	public static void main(String[] args) {
		int width           = 960;
		int height          = 540;
		int megaBlockSize   = 16;
		int dctBlockSize    = 8;

		byte[]	RBytes;
		byte[]  IBytes;	


		//Trial
		JFrame frame;

		IBytes = new byte[width * height * 3];
		RBytes = new byte[width * height * 3];

		//Magic Number must corrected
		int numFrames = 564537600/(width * height * 3);

		try {
			//To Display Frames.
			GridBagConstraints c = new GridBagConstraints();
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			frame = new JFrame();
			GridBagLayout gLayout = new GridBagLayout();
			frame.getContentPane().setLayout(gLayout);

			JLabel lbIm1 = new JLabel(new ImageIcon(img));
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 0;
			c.gridy = 0;
			frame.getContentPane().add(lbIm1, c);
			frame.pack();
			frame.setVisible(true);	

			byte a = 0;
			int size = width * height;			

			File imageFile = new File(args[0]);
			InputStream is = new FileInputStream(imageFile);
			is.read(IBytes, 0, IBytes.length);
			MyFrame CurFrame = new MyFrame(IBytes,width,height);
			CurFrame.convertRGB2YUV();			

			int nfrm = 0;
			while (nfrm <  numFrames){
				is.read(RBytes, 0, RBytes.length);
				MyFrame RefFrame = new MyFrame(RBytes,width,height);

				RefFrame.convertRGB2YUV();
				nfrm++;

				MotionVectors mvec = new MotionVectors(IBytes,RBytes,width,height,megaBlockSize);
				mvec.SumAbsoluteDifference();

				IndexConverter converter = new IndexConverter(width,height,dctBlockSize);
				IndexConverter conv = new IndexConverter(width,height,megaBlockSize);

				//Before DCT convert R
				RefFrame.convertYUV2RGB();

				for (int rY=0; rY < RefFrame.height;rY = rY + dctBlockSize ){
					for (int rX = 0; rX < RefFrame.width;rX = rX + dctBlockSize){

						//System.out.print(conv.getBlockXIndex(rX, rY) + "   " + conv.getBlockYIndex(rX, rY));
						//Write to the output file
						if (mvec.refFrame.iBlocks[conv.getBlockXIndex(rX, rY)][conv.getBlockYIndex(rX, rY)].background) {
							//System.out.print("B ");
						}
						else{
							//System.out.print("F ");
						}

						//DCT Red Part
						Dct2d dctR = new Dct2d();
						float [][] dctRM = Matrix.createFloatMatrix(dctBlockSize, dctBlockSize);						
						for (int my=0;my<dctBlockSize;my++){
							for (int mx=0;mx<dctBlockSize;mx++){
								if (((rX + mx) < width) && ((rY + my) < height)){								
									dctRM[mx][my] = RBytes[converter.getFrameIndex(rX + mx, rY + my)];
								}
							}
						}
						//System.out.print("R ");
						//System.out.println("G1 = " + Matrix.toString(dctRM)); //to be deleted
						dctR.DCT(dctRM);
						//Write to the output file
						//System.out.println("G2 = " + Matrix.toString(dctRM)); //to be deleted


						//DCT Green Part
						Dct2d dctG = new Dct2d();
						float [][] dctGM = Matrix.createFloatMatrix(dctBlockSize, dctBlockSize);						
						for (int my=0;my<dctBlockSize;my++){
							for (int mx=0;mx<dctBlockSize;mx++){
								if (((rX + mx) < width) && ((rY + my) < height)){								
									dctGM[mx][my] = RBytes[converter.getFrameIndex(rX + mx, rY + my) + width * height];
								}
							}
						}
						//System.out.print("G ");
						//System.out.println("G1 = " + Matrix.toString(dctGM)); //to be deleted
						dctG.DCT(dctGM);
						//Write to the output file
						//System.out.println("G2 = " + Matrix.toString(dctGM)); //to be deleted

						//DCT Blue Part
						Dct2d dctB = new Dct2d();
						float [][] dctBM = Matrix.createFloatMatrix(dctBlockSize, dctBlockSize);						
						for (int my=0;my<dctBlockSize;my++){
							for (int mx=0;mx<dctBlockSize;mx++){
								if (((rX + mx) < width) && ((rY + my) < height)){								
									dctBM[mx][my] = RBytes[converter.getFrameIndex(rX + mx, rY + my) + width * height * 2];
								}
							}
						}
						//System.out.print("B ");
						//System.out.println("G1 = " + Matrix.toString(dctBM)); //to be deleted
						dctB.DCT(dctBM);
						//Write to the output file
						//System.out.println("G2 = " + Matrix.toString(dctBM)); //to be deleted						
					}
					System.out.println();
				}				


				is.read(IBytes, 0, IBytes.length);
				MyFrame IFrame = new MyFrame(IBytes,width,height);
				IFrame.convertRGB2YUV();
				nfrm++;



				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						byte r = RBytes[width * y + x];
						byte g = RBytes[width * y + x + size];
						byte b = RBytes[width * y + x + size *2];
						int pix  = ((a << 24) + (r << 16) + (g << 8) + b);
						img.setRGB(x,y,pix);
					}
				} 

				lbIm1.setIcon(new ImageIcon(img));

			}
			is.close();

		}
		catch (FileNotFoundException e) {
			System.out.println("ERROR - File Not Found");
		}catch (IOException e) {
			System.out.println("ERROR - File Length Error");
		} 
	}



}
//		IndexConverter conv = new IndexConverter(width,height,16);
//		
//		for (int i = 0;i<(width * 2);i++){
//			conv.calculateBlockIndexes(i);
//			int h = conv.getFrameIndex(conv.blkX, conv.blkY, conv.imgX, conv.imgY);
//			
//			System.out.println(i + " : (" + conv.blkX + "," + conv.blkY +") - (" + conv.imgX + "," + conv.imgY +")  -->  " + h);
//
//		}	
//
//----------------------------------------------------------------------------------------------------
//			MotionVectors mvec = new MotionVectors(IBytes,RBytes,width,height);
//			mvec.SumAbsoluteDifference();

//			DisplayFrame view = new DisplayFrame(RBytes,width,height);
//			view.pack();
//			view.setVisible(true);


