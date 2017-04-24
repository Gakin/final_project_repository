package cs567.decode;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs567.player.VideoPlayer;
import cs567.rep.base.BaseVideo;
import cs567.rep.freqbased.ImageFrequencyBlockVideo;

public class BufferedDecoder extends BaseDecoder {
	
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
		
		frameCount = v.getFrameCount();
		initPreparedFrame();
	}
	
	protected void initPreparedFrame() {
		preparedFrame = new BufferedImage[getFrameCount()];
		for (int i = 0; i < getFrameCount(); i++) {
			preparedFrame[i] = deepCopy(compressedFrames.get(i));
		}
	}

	@Override
	public void prepareFrame(int frame, Map params) {
		copySrcIntoDstAt(compressedFrames.get(frame), preparedFrame[frame], 0, 0);
		
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
	
	private void copySrcIntoDstAt(final BufferedImage src, final BufferedImage dst, final int dx, final int dy) {
	    int[] srcbuf = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
	    int[] dstbuf = ((DataBufferInt) dst.getRaster().getDataBuffer()).getData();
	    int width = src.getWidth();
	    int height = src.getHeight();
	    int dstoffs = dx + dy * dst.getWidth();
	    int srcoffs = 0;
	    for (int y = 0 ; y < height ; y++ , dstoffs+= dst.getWidth(), srcoffs += width ) {
	        System.arraycopy(srcbuf, srcoffs , dstbuf, dstoffs, width);
	    }
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

	protected List<BufferedImage> getCompressedFrames() {
		return compressedFrames;
	}

	protected void setCompressedFrames(List<BufferedImage> compressedFrames) {
		this.compressedFrames = compressedFrames;
	}

	protected List<BufferedImage> getUncompressedFrames() {
		return uncompressedFrames;
	}

	protected void setUncompressedFrames(List<BufferedImage> uncompressedFrames) {
		this.uncompressedFrames = uncompressedFrames;
	}

	
	protected void setWidth(int width) {
		this.width = width;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

	protected void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	protected BufferedImage[] getPreparedFrame() {
		return preparedFrame;
	}

	protected void setPreparedFrame(BufferedImage[] preparedFrame) {
		this.preparedFrame = preparedFrame;
	}
	
}
