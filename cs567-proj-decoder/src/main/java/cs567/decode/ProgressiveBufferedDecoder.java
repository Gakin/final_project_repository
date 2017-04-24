package cs567.decode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cs567.rep.base.BaseVideo;
import cs567.rep.freqbased.ImageFrequencyBlockFrame;
import cs567.rep.freqbased.ImageFrequencyBlockVideo;

public class ProgressiveBufferedDecoder extends BufferedDecoder {
	
	public ProgressiveBufferedDecoder(int fg_q, int bg_q) {
		super(fg_q, bg_q);
	}

	public void preDecode(InputStream is, int width, int height, int blockSize) throws IOException {
		setWidth(width);
		setHeight(height);
		
		int numBlocksPerRow = (int)Math.ceil(1.0 * width/blockSize);
		int numRowsOfBlocks = (int)Math.ceil(1.0 * height/blockSize);
		int numBlocks = numBlocksPerRow * numRowsOfBlocks;
		
		int frameByteLength = numBlocks * (1 + (3 * 4 * blockSize * blockSize));
		
		byte[] bytes = new byte[(int) frameByteLength];
		
		Map emptyParams = new HashMap<>();
		
		Map params = new HashMap<>();
		params.put(ImageFrequencyBlockVideo.Property.FG_Q, fg_q);
		params.put(ImageFrequencyBlockVideo.Property.BG_Q, bg_q);
		
		while(is.available() > 0) {
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}
			
			ImageFrequencyBlockFrame frame = new ImageFrequencyBlockFrame(width, height, bytes, numBlocks, blockSize);
			
			ImageFrequencyBlockVideo video = new ImageFrequencyBlockVideo();
			video.addFrame(frame);
			video.setWidth(width);
			video.setHeight(height);
			
			BufferedImage ucf = video.getFrameImage(0, emptyParams);
			BufferedImage cf = video.getFrameImage(0, params);
			
			getCompressedFrames().add(cf);
			getUncompressedFrames().add(ucf);
			
			if (getCompressedFrames().size() %10 == 0) {
				System.out.println(":frame " + getCompressedFrames().size());
			}
		}
	}

	@Override
	public void init(BaseVideo v) {
		setFrameCount(getCompressedFrames().size());
		preparedFrame = new BufferedImage[getFrameCount()];
	}
}
