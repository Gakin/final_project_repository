package cs567.rep.freqbased;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs567.rep.base.BaseVideo;
import cs567.rep.blockbased.ImageBlockFrame;
import cs567.rep.blockbased.ImageBlockVideo;

public class ImageFrequencyBlockVideo extends BaseVideo {

	public static enum Property {
		FG_Q,
		BG_Q
	}
	
	List<ImageFrequencyBlockFrame> frames;

	public ImageFrequencyBlockVideo() {
		frames = new ArrayList<>();
	}
	
	public ImageFrequencyBlockVideo(ImageBlockVideo v) {
		this();
		setWidth(v.getWidth());
		setHeight(v.getHeight());
		
		for(int i = 0; i < v.getFrameCount(); i++) {
			ImageBlockFrame f = v.getFrame(i);
			addFrame(f);
			
			if (i%10 == 0) {
				System.out.println(":frame " + i);
			}
		}
	}
	
	public void addFrame(ImageBlockFrame frame) {
		ImageFrequencyBlockFrame ifbf = new ImageFrequencyBlockFrame(frame);
		frames.add(ifbf);
	}
	
	public void addFrame(ImageFrequencyBlockFrame frame) {
		frames.add(frame);
	}
	
	public ImageFrequencyBlockFrame getFrame(int i) {
		return frames.get(i);
	}
	
	@Override
	public BufferedImage getFrameImage(int frame, Map params) {
		int fg_q = (int)params.getOrDefault(Property.FG_Q, 1);
		int bg_q = (int)params.getOrDefault(Property.BG_Q, 1);
		
		ImageFrequencyBlockFrame f = frames.get(frame);
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < f.getBlockCount(); i++) {
			ImageFrequencyBlock block = f.blocks.get(i);
			int[][] pixels = block.isForeground() ? block.convertToPixels(fg_q) : block.convertToPixels(bg_q);
			
			int sx = i % f.numBlocksPerRow() * f.getBlockSize();
			int sy = i / f.numBlocksPerRow() * f.getBlockSize();
			int nx = Math.min(sx+f.getBlockSize(), getWidth());
			int ny = Math.min(sy+f.getBlockSize(), getHeight());
			
			for (int y = sy; y < ny; y++) {
				for (int x = sx; x < nx; x++) {
					img.setRGB(x, y, pixels[x-sx][y-sy]);
				}
			}
		}
		
		return img;
	}

	@Override
	public int getFrameCount() {
		return frames.size();
	}

	@Override
	public void destruct() {
		for (ImageFrequencyBlockFrame frame: frames) {
			frame.blocks.clear();
		}
		
		frames.clear();
	}
}
