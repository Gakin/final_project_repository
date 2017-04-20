package cs567.decode;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs567.player.VideoPlayer;
import cs567.rep.base.BaseVideo;
import cs567.rep.freqbased.ImageFrequencyBlockVideo;

public class BufferedDecoder extends BaseDecoder{
	
	List<BufferedImage> compressedFrames;
	List<BufferedImage> uncompressedFrames;
	int fg_q = 1;
	int bg_q = 1;
	BufferedImage[] preparedFrame;
	int width;
	int height;
	int frameCount;
	
	public BufferedDecoder(int fg_q, int bg_q) {
		this.fg_q = fg_q;
		this.bg_q = bg_q;
		compressedFrames = new ArrayList<>();
		uncompressedFrames = new ArrayList<>();
	}
	
	@Override
	public void init(BaseVideo v) {
		ImageFrequencyBlockVideo video = (ImageFrequencyBlockVideo) v;
		width = v.getWidth();
		height = v.getHeight();
		frameCount = v.getFrameCount();
		preparedFrame = new BufferedImage[getFrameCount()];
		
		
		for(int i = 0; i < video.getFrameCount(); i++) {
			Map params = new HashMap<>();
			BufferedImage ucf = video.getFrameImage(i, params);
			
			params.put(ImageFrequencyBlockVideo.Property.FG_Q, fg_q);
			params.put(ImageFrequencyBlockVideo.Property.BG_Q, bg_q);
			
			BufferedImage cf = video.getFrameImage(i, params);
			
			compressedFrames.add(cf);
			uncompressedFrames.add(ucf);
			
			if (i%10 == 0) {
				System.out.println(":frame " + i);
			}
		}
	}

	@Override
	public void prepareFrame(int frame, Map params) {
		preparedFrame[frame] = deepCopy(compressedFrames.get(frame));
		
		if (params != null && params.containsKey(VideoPlayer.Property.MX)) {
			int mx = (int) params.get(VideoPlayer.Property.MX);
			int my = (int) params.get(VideoPlayer.Property.MY);
			
			int sx = Math.max(0, mx-32);
			int sy = Math.max(0, my-32);
			int ex = Math.min(mx+32, getWidth());
			int ey = Math.min(my+32, getHeight());
			
			for (int y = sy; y < ey; y++) {
				for (int x = sx; x < ex; x++) {
					preparedFrame[frame].setRGB(x, y, uncompressedFrames.get(frame).getRGB(x, y));
				}
			}
		}
	}

	private BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	@Override
	public BufferedImage getFrame(int frame) {
		return preparedFrame[frame];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getFrameCount() {
		return frameCount;
	}
	
}
