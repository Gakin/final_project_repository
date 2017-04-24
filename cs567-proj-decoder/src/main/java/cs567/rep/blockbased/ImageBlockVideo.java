package cs567.rep.blockbased;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs567.rep.base.BaseVideo;
import cs567.rep.freqbased.ImageFrequencyBlockFrame;
import cs567.rep.simple.SimpleFrame;
import cs567.rep.simple.SimpleVideo;

public class ImageBlockVideo extends BaseVideo {

	List<ImageBlockFrame> frames;

	public ImageBlockVideo() {
		frames = new ArrayList<>();
	}
	
	public ImageBlockVideo(SimpleVideo v, int blockSize) {
		this();
		setWidth(v.getWidth());
		setHeight(v.getHeight());
		
		for(int i = 0; i < v.getFrameCount(); i++) {
			SimpleFrame f = v.getFrame(i);
			addFrame(f.getPixels(), blockSize);
			
			if (i%10 == 0) {
				System.out.println(":frame " + i);
			}
		}
	}
	
	public void addFrame(ImageBlockFrame frame) {
		frames.add(frame);
	}
	
	public void addFrame(int[][] pixels, int blockSize) {
		ImageBlockFrame ibf = new ImageBlockFrame(pixels, blockSize);
		frames.add(ibf);
	}
	
	public ImageBlockFrame getFrame(int frame) {
		return frames.get(frame);
	}
	
	@Override
	public BufferedImage getFrameImage(int frame, Map params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFrameCount() {
		return frames.size();
	}
	
	@Override
	public void destruct() {
		for (ImageBlockFrame frame: frames) {
			frame.blocks.clear();
		}
		
		frames.clear();
	}

}
