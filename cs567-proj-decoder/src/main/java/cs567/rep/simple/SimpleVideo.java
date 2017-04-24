package cs567.rep.simple;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs567.rep.base.BaseVideo;

public class SimpleVideo extends BaseVideo {
	private List<SimpleFrame> frames;	
	
	public SimpleVideo() {
		frames = new ArrayList<>();
	}
	
	public void addFrame(SimpleFrame fr) {
		frames.add(fr);
	}
	
	@Override
	public int getFrameCount(){
		return frames.size();
	}
	
	public SimpleFrame getFrame(int f) {
		return frames.get(f);
	}
	
	@Override
	public int getWidth() {
		return getFrame(0).getWidth();
	}
	
	@Override
	public int getHeight() {
		return getFrame(0).getHeight();
	}
	
	@Override
	public BufferedImage getFrameImage(int frame, Map params) {
		return frames.get(frame).getBufferedImage();
	}

	@Override
	public void destruct() {
		frames.clear();
	}
	
}
