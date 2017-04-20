package test;

import java.util.Calendar;

import cs567.rep.blockbased.ImageBlockFrame;
import cs567.rep.blockbased.ImageBlockVideo;
import cs567.rep.freqbased.ImageFrequencyBlockVideo;
import cs567.rep.simple.SimpleFrame;
import cs567.rep.simple.SimpleVideo;
import cs567.utils.VideoUtils;

public class TestSaveCompressedVideo {
	private static long start;
	private static long end;
	
	public static void main(String[] args) {
		String filename = "c:\\cs576\\oneperson_960_540.rgb";
		int width = 960;
		int height = 540;
		int blockSize = 8;
		String saveFileTo = "c:\\cs576\\oneperson_960_540.cmp";
		
		start = now();
		
		/* Read file as simple video */
		System.out.println("Reading .rgb file");
		SimpleVideo video = VideoUtils.readSimpleVideo(filename, width, height);
		reportTime();
		
		/* Do fg-bg separation and convert to block-based video */
		System.out.println("Do fg-bg separation and convert to block-based video");
		ImageBlockVideo blockVideo = new ImageBlockVideo();
		blockVideo.setWidth(width);
		blockVideo.setHeight(height);
		
		for(int i = 0; i < video.getFrameCount(); i++) {
			SimpleFrame frame = video.getFrame(i);
			
			/* Convert each frame to multiple blocks frame */
			ImageBlockFrame ibf = new ImageBlockFrame(frame.getPixels(), blockSize);
			
			/* Mock - Set first top half of each frame as background, bottom half as foreground  */
			for(int j = ibf.getBlockList().size()/2; j < ibf.getBlockList().size(); j++) {
				ibf.getBlockList().get(j).setForeground(true);
			}
			
			blockVideo.addFrame(ibf);
		}
		reportTime();
		
		/* Convert to frequency-based video */
		System.out.println("Convert to frequency-based video");
		ImageFrequencyBlockVideo freqVideo = new ImageFrequencyBlockVideo(blockVideo);
		blockVideo.destruct();
		blockVideo = null;
		System.gc();
		reportTime();
		
		/* Save to cmp file */
		System.out.println("Save to " + saveFileTo);
		VideoUtils.saveFrequencyBlockVideo(saveFileTo, freqVideo);
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
