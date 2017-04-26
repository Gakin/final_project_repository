package cs567.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cs567.decode.ProgressiveBufferedDecoder;
import cs567.player.VideoPlayer;

public class MyDecoder {

	public static void main(String[] args) {
		/* Default value */
		String cmpFileName = args[0];
		int width = 960;
		int height = 540;
		int fps = 30;
		int fg_q = 0;
		int bg_q = 8;
		int blockSize = 8;
		boolean gazeControl = true;
		
		width = readPositiveInteger(args[1], width, "width");
		height = readPositiveInteger(args[2], height, "height");
		fps = readPositiveInteger(args[3], fps, "fps");
		
		fg_q = readPositiveInteger(args[4], fg_q, "foreground_quantize");
		bg_q = readPositiveInteger(args[5], bg_q, "background_quantize");
		
		int gaze = readPositiveInteger(args[6], 1, "gaze_control");
		
		gazeControl = gaze != 0;
		
		fg_q = (int)Math.pow(2, fg_q);
		bg_q = (int)Math.pow(2, bg_q);
		
		
		/* Read compressed video */
		System.out.println("File: " + cmpFileName);
		System.out.println("Read compressed video and Prepare decoder");
		ProgressiveBufferedDecoder decoder = null;
		
		try (InputStream is = new FileInputStream(new File(cmpFileName))) {
			decoder = new ProgressiveBufferedDecoder(fg_q, bg_q);
			decoder.preDecode(is, width, height, blockSize);
		} catch (IOException e) {
			System.out.println("Error reading input files");
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Init Video Player");
		VideoPlayer vp = new VideoPlayer();
		vp.setApplyGazeControl(gazeControl);
		vp.setDecoder(null, decoder, fps);
		
		System.out.println("Play video");
		vp.playVideo();
	}
	
	private static int readPositiveInteger(String s, int defaultVal, String name) {
		int v = defaultVal;
		
		try {
			v = Integer.parseInt(s);
			
			if(v < 0) {
				throw new RuntimeException();
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			System.out.println("Error: " + name + " is not positive integer, use default value " + defaultVal);
		}
		
		return v;
	}

}
