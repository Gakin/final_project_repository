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
import javax.swing.SwingConstants;

import project1.DisplayFrame;
import project1.MotionVectors;

public class MyEncoder {

	public static void main(String[] args) {
		int width  = 960;
		int height = 540;
		byte[]	RBytes;
		byte[]  IBytes;	

		//Trial
		JFrame frame;


		IBytes = new byte[width * height * 3];
		RBytes = new byte[width * height * 3];

		int numFrames = 564537600/(width * height * 3);

		try {

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
				//while (nfrm < 1){
				is.read(RBytes, 0, RBytes.length);
				MyFrame RefFrame = new MyFrame(RBytes,width,height);
				RefFrame.convertRGB2YUV();
				nfrm++;
				MotionVectors mvec = new MotionVectors(IBytes,RBytes,width,height);
				mvec.SumAbsoluteDifference();	

				is.read(IBytes, 0, IBytes.length);

				MyFrame IFrame = new MyFrame(IBytes,width,height);
				IFrame.convertRGB2YUV();
				nfrm++;


				int h=0;
				for (int z=0;z<34;z++){
					for(int s=0;s<60;s++){
						for (int t =0;t<16;t++){
							for (int f=0;f<16;f++){
								h = (z * 16 + t) * width + s * 16 + f;
								if (h < (width * height)) {		
									RBytes[h] = mvec.refFrame.iBlocks[s][z].iBlock[f][t] ;	
								}
								else{
									break;
								}
							}
						}
					}
				}

				RefFrame.convertYUV2RGB();
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

			//			MotionVectors mvec = new MotionVectors(IBytes,RBytes,width,height);
			//			mvec.SumAbsoluteDifference();



			//			DisplayFrame view = new DisplayFrame(RBytes,width,height);
			//			view.pack();
			//			view.setVisible(true);









		}
		catch (FileNotFoundException e) {
			System.out.println("ERROR - File Not Found");
		}catch (IOException e) {
			System.out.println("ERROR - File Length Error");
		} 
	}
}
