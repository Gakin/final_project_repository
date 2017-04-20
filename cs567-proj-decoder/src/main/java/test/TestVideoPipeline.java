package test;

import java.util.Calendar;

import cs567.decode.BufferedDecoder;
import cs567.player.VideoPlayer;
import cs567.rep.blockbased.ImageBlockFrame;
import cs567.rep.blockbased.ImageBlockVideo;
import cs567.rep.freqbased.ImageFrequencyBlockVideo;
import cs567.rep.simple.SimpleVideo;
import cs567.utils.VideoUtils;

public class TestVideoPipeline {
	
	private static long start;
	private static long end;
	
	public static void main(String[] args) {
		String filename = "c:\\cs576\\oneperson_960_540.rgb";
		int width = 960;
		int height = 540;
		int fps = 30;
		int fg_q = 64;
		int bg_q = 256;
		int blockSize = 8;
		
		start = now();
		
		/* Read file as simple video */
		System.out.println("Reading .rgb file");
		SimpleVideo video = VideoUtils.readSimpleVideo(filename, width, height);
		reportTime();
		
		/* Convert simple video to block-based video */
		System.out.println("Convert to block-based video");
		ImageBlockVideo blockVideo = new ImageBlockVideo(video, blockSize);
		video.destruct();
		video = null;
		System.gc();
		reportTime();
		
		/* Set first top half of each frame as background, bottom half as foreground */
		System.out.println("Mock - set fg-bg");
		mockSetForeground(blockVideo);
		reportTime();
		
		
		/* Convert to frequency-based video */
		System.out.println("Convert to frequency-based video");
		ImageFrequencyBlockVideo freqVideo = new ImageFrequencyBlockVideo(blockVideo);
		blockVideo.destruct();
		blockVideo = null;
		System.gc();
		reportTime();
		
		System.out.println("Create decoder");
		BufferedDecoder decoder = new BufferedDecoder(fg_q, bg_q);
		reportTime();
		
		System.out.println("Prepare decoder (pre-decoding to buffer)");
		VideoPlayer vp = new VideoPlayer();
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
	
	private static void mockSetForeground(ImageBlockVideo v) {
		for(int i = 0; i < v.getFrameCount(); i++) {
			ImageBlockFrame f = v.getFrame(i);
			for(int j = f.getBlockList().size()/2; j < f.getBlockList().size(); j++) {
				f.getBlockList().get(j).setForeground(true);
			}
		}
	}
}
