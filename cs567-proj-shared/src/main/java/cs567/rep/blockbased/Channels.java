package cs567.rep.blockbased;

public class Channels {
	public short[][] red;
	public short[][] green;
	public short[][] blue;
	
	public void average(int rad) {
		this.red = averageChannel(red, rad);
		this.green = averageChannel(green, rad);
		this.blue = averageChannel(blue, rad);
	}

	private short[][] averageChannel(short[][] s, int rad) {
		int width = s.length;
		int height = s[0].length;
		
		short[][] ns = new short[width][height]; 
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int sum = 0;
				int count = 0;
				
				for(int ny = Math.max(0, y-rad); ny < Math.min(height, y+rad); ny++) {
					for(int nx = Math.max(0, x-rad); nx < Math.min(width, x+rad); nx++) {
						sum += s[nx][ny];
						count++;
					}
				}
				
				sum += (count/3) * s[x][y];
				count += (count/3);
				
				short avg = (short) (1.0 * sum / count);
				ns[x][y] = avg;
			}
		}
		
		return ns;
	}
	
}
