package project1;

//import Hw1.Quantizer;

public class MyFrame {
	protected int    width;
	protected int    height;
	protected int    size;
	protected byte[] data;

	public MyFrame(byte[] imageBuffer,int width, int height) {
		this.width  = width;
		this.height = height;
		size = width * height;
		data = imageBuffer;
	}

	public int    getWidth()  { return width; }
	public int    getHeight() { return height; }
	public int    getSize()   { return size; }
	public byte[] getData()   { return data; }

	
	public void convertYUV2RGB() {
		byte by = (byte)128;
		byte bu = (byte)128;
		byte bv = (byte)128;
		byte [] RGB = new byte[3];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				by = data[width * y + x];
				bu = data[width * y + x + size];
				bv = data[width * y + x + size *2];
				RGB = convertPixelYUV2RGB(by, bu, bv);
				data[width * y + x] = RGB[0];
				data[width * y + x + size] = RGB[1];
				data[width * y + x + size *2] = RGB[2];
			}
		}        	
	}

	protected byte[] convertPixelYUV2RGB(byte y, byte u, byte v) {
		int iy = y & 0xff;
		int iu = u & 0xff;
		int iv = v & 0xff;
        byte [] RGB = new byte [3];
        
		float fr = 1.164f * (iy-16)                     + 1.596f * (iv-128);
		float fg = 1.164f * (iy-16) - 0.391f * (iu-128) - 0.813f * (iv-128);
		float fb = 1.164f * (iy-16) + 2.018f * (iu-128)                    ;

		int ir = (int)(fr > 255 ? 255 : fr < 0 ? 0 : fr);
		int ig = (int)(fg > 255 ? 255 : fg < 0 ? 0 : fg);
		int ib = (int)(fb > 255 ? 255 : fb < 0 ? 0 : fb);
        
		RGB[0] = (byte) ir;
		RGB[1] = (byte) ig;
		RGB[2] = (byte) ib;
		
		return RGB;
	}	
	
	public void convertRGB2YUV() {
		byte br = (byte)128;
		byte bg = (byte)128;
		byte bb = (byte)128;
        int [] YCbCr = new int [3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				br = data[width * y + x];
				bg = data[width * y + x + size];
				bb = data[width * y + x + size *2];
				YCbCr = convertPixelRGB2YUV(br, bg, bb);
				data[width * y + x] = (byte)YCbCr[0];
				data[width * y + x + size] = (byte)YCbCr[1];
				data[width * y + x + size *2] = (byte)YCbCr[2];				
//				data[width * y + x + size] = (byte)-127;
//				data[width * y + x + size *2] = (byte)-127;				
			}
		}        	
	}


	protected int[] convertPixelRGB2YUV(byte r, byte g, byte b) {
		int ir = r & 0xff;
		int ig = g & 0xff;
		int ib = b & 0xff;
		int[] YCbCr = new int[3];
		
	    double delta = 128.0;
	    
	    double Y  =  0.257 * ir + 0.504 * ig + 0.098 * ib + 16;
	    double Cb = -0.148 * ir - 0.291 * ig + 0.439 * ib + delta;
	    double Cr =  0.439 * ir - 0.368 * ig - 0.071 * ib + delta;

	    YCbCr[0] = (int) Y;
	    YCbCr[1] = (int) Cb;
	    YCbCr[2] = (int) Cr;
	    return YCbCr;
	}
}