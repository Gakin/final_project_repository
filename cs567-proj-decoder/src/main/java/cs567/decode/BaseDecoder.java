package cs567.decode;

import java.awt.image.BufferedImage;
import java.util.Map;

import cs567.rep.base.BaseVideo;

public abstract class BaseDecoder {
	public abstract void init(BaseVideo v);
	public abstract void prepareFrame(int frame, Map params);
	public abstract BufferedImage getFrame(int frame);
	
	public abstract int getHeight();
	public abstract int getWidth();
	public abstract int getFrameCount();
}
