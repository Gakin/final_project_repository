package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import cs567.decode.ProgressiveBufferedDecoder;
import cs567.player.VideoPlayer;


public class TestProgressiveDecoder {
	private static long start;
	private static long end;
	
	
	public static void main(String[] args) {
//		String cmpFileName = "c:\\cs576\\two_people_moving_background.cmp";
//		String cmpFileName = "c:\\cs576\\two_people.cmp";
		String cmpFileName = args[0];
		int width = 960;
		int height = 540;
		int fps = 30;
		int fg_q = 1;
		int bg_q = 256;
		int blockSize = 8;
		boolean gazeControl = true;
		
		
		start = now();
		
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
		reportTime();
		
		System.out.println("Init Video Player");
		VideoPlayer vp = new VideoPlayer();
		vp.setApplyGazeControl(gazeControl);
		vp.setDecoder(null, decoder, fps);
		reportTime();
		
		System.out.println("Play video");
		vp.playVideo();
		reportTime();
	}
	
	
	private static long now() {
		return Calendar.getInstance().getTimeInMillis();
	}
	
	private static void reportTime() {
		end = now();
		System.out.println("--" + (end - start) + " ms \n");
		start = end;
	}
}
