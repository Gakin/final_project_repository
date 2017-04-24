package test;

import java.util.Calendar;

import cs567.decode.BufferedDecoder;
import cs567.player.VideoPlayer;
import cs567.rep.freqbased.ImageFrequencyBlockVideo;
import cs567.utils.VideoUtils;

public class TestDecoder {
	private static long start;
	private static long end;
	
	
	public static void main(String[] args) {
//		String cmpFileName = "c:\\cs576\\two_people_moving_background.cmp";
		String cmpFileName = "c:\\cs576\\two_people.cmp";
		int width = 960;
		int height = 540;
		int fps = 30;
		int fg_q = 1;
		int bg_q = 25600;
		int blockSize = 8;
		boolean gazeControl = true;
		
		
		start = now();
		
		/* Read compressed video */
		System.out.println("Read compressed video");
		ImageFrequencyBlockVideo freqVideo = VideoUtils.readFrequencyBlockVideo(cmpFileName, width, height, blockSize);
		reportTime();
		
		System.out.println("Create decoder");
		BufferedDecoder decoder = new BufferedDecoder(fg_q, bg_q);
		reportTime();
		
		System.out.println("Prepare decoder (pre-decoding to buffer)");
		VideoPlayer vp = new VideoPlayer();
		vp.setApplyGazeControl(gazeControl);
		vp.setDecoder(freqVideo, decoder, fps);
		freqVideo.destruct();
		freqVideo = null;
		System.gc();
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
