package cs567.rep.base;

import java.awt.image.BufferedImage;
import java.util.Map;

public abstract class BaseVideo {
	
	int width;
	int height;
	
	public abstract BufferedImage getFrameImage(int frame, Map params);
	public abstract int getFrameCount();
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public abstract void destruct();
	
	
}
