package cs567.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cs567.rep.freqbased.ImageFrequencyBlockFrame;
import cs567.rep.freqbased.ImageFrequencyBlockVideo;
import cs567.rep.simple.SimpleFrame;
import cs567.rep.simple.SimpleVideo;

public class VideoUtils {

	public static SimpleVideo readSimpleVideo(String fileName, int width, int height) {
		SimpleVideo video = new SimpleVideo();
		
		File file = new File(fileName);
		
		try (InputStream is = new FileInputStream(file)){
			int frameByteLength = width * height * 3;
			
			byte[] bytes = new byte[(int) frameByteLength];
			while(is.available() > 0) {
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
					offset += numRead;
				}
				
				SimpleFrame frame = new SimpleFrame(width, height, bytes);
				video.addFrame(frame);
				
//				if (video.getFrameCount() >= 60) {
//					break;
//				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException");
		}
		
		return video;	
	}
	
	public static void saveVideo(String fileName, SimpleVideo vdo) {
		try (FileOutputStream fos = new FileOutputStream(fileName)){
			for(int i=0; i < vdo.getFrameCount(); i++) {
				SimpleFrame f = vdo.getFrame(i);
				byte[] b = f.toByteArray();
				fos.write(b);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException");
		}
		
	}
	
	
	public static ImageFrequencyBlockVideo readFrequencyBlockVideo(String fileName, int width, int height, int blockSize) {
		ImageFrequencyBlockVideo video = new ImageFrequencyBlockVideo();
		File file = new File(fileName);
		
		try (InputStream is = new FileInputStream(file)){
			int numBlocksPerRow = (int)Math.ceil(1.0 * width/blockSize);
			int numRowsOfBlocks = (int)Math.ceil(1.0 * height/blockSize);
			int numBlocks = numBlocksPerRow * numRowsOfBlocks;
			
			int frameByteLength = numBlocks * (1 + (3 * 4 * blockSize * blockSize));
			
			byte[] bytes = new byte[(int) frameByteLength];
			while(is.available() > 0) {
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
					offset += numRead;
				}
				
				ImageFrequencyBlockFrame frame = new ImageFrequencyBlockFrame(width, height, bytes, numBlocks, blockSize);
				video.addFrame(frame);
			}
			
			video.setWidth(width);
			video.setHeight(height);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException");
		}
		
		
		return video;
	}
	
	public static void saveFrequencyBlockVideo(String fileName, ImageFrequencyBlockVideo vdo) {
		try (FileOutputStream fos = new FileOutputStream(fileName)){
			for(int i=0; i < vdo.getFrameCount(); i++) {
				ImageFrequencyBlockFrame f = vdo.getFrame(i);
				byte[] b = f.toByteArray();
				fos.write(b);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException");
		}
	}
}
