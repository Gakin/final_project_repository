package cs567.decode;

import java.awt.image.BufferedImage;
import java.util.Map;

import cs567.rep.base.BaseVideo;

public class SimpleDecoder extends BaseDecoder {

	BaseVideo video;
	
	public SimpleDecoder() {
		
	}
	
	@Override
	public void init(BaseVideo v) {
		this.video = v;
		for(int i = 0; i < getFrameCount(); i++) {
			prepareFrame(i, null);
		}
	}
	
	@Override
	public void prepareFrame(int frame, Map params) {
		getVideo().getFrameImage(frame, null);
	}

	@Override
	public BufferedImage getFrame(int frame) {
		return getVideo().getFrameImage(frame, null);
	}

	protected BaseVideo getVideo() {
		return video;
	}

	@Override
	public int getHeight() {
		return getVideo().getHeight();
	}

	@Override
	public int getWidth() {
		return getVideo().getWidth();
	}

	@Override
	public int getFrameCount() {
		return getVideo().getFrameCount();
	}

	

}
