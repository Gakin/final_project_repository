package cs567.rep.base;

public abstract class BaseBlock {
	boolean foreground;
	public boolean isForeground() {
		return foreground;
	}

	public void setForeground(boolean foreground) {
		this.foreground = foreground;
	}
	
	public abstract int getBlockSize();
}
