package dct;



import dct.Matrix;

import dct.PrintPrecision;


public class DctMatrixTest {

	/**

	 * Test method.

	 * @param args command arguments

	 */

	public static void main(String[] args) {

		PrintPrecision.set(5);

		float[][] A = makeDctMatrix(8, 8);

		System.out.println("A = " + Matrix.toString(A));

		System.out.println();



		float[][] At = Matrix.transpose(A);

		System.out.println("At = " + Matrix.toString(At));

		System.out.println();

		

		Dct2d dct = new Dct2d();

		float[][] g = {

				{ 49,  61,  69,  61,  78,  89, 100, 112},

				{ 68,  60,  51,  42,  62,  69,  80,  89},

				{ 90,  81,  58,  49,  69,  72,  68,  69},

				{100,  91,  79,  72,  69,  68,  59,  58},
				
				{111, 100, 101,  91,  82,  71,  59,  49},
				
				{131, 119, 120, 102,  90,  90,  81,  59},
				
				{148, 140, 129,  99,  92,  78,  59,  39},
				
				{151, 140, 142, 119,  98,  90,  72,  39}};

		float[][] q = {

				{ 16,  11,  10,  16,  24,  42,  51,  61},

				{ 12,  12,  14,  19,  26,  58,  60,  55},

				{ 14,  13,  16,  24,  40,  57,  69,  56},

				{ 14,  17,  22,  29,  51,  87,  80,  62},
				
				{ 18,  22,  37,  58,  68, 109,  123, 77},
				
				{ 24,  35,  55,  64,  81, 104, 113,  92},
				
				{ 49,  64,  78,  87, 103, 121, 120, 101},
				
				{ 72,  92,  95,  98, 122, 100, 103,  99}};		

		float[][] r = {

				{ 42,  9,  1,  1,  -1,  0,  0,  0},

				{ -9, -12, 3,  0,  0,  0,   0,  0},

				{ 2,  -2,  0,  0,  0,  0,   0,  0},

				{ 2,  -1,  0,  0,  0,  0,   0,  0},
				
				{ 1,   0,  0,  0,  0,  0,   0,  0},
				
				{ 0,   0,  0,  0,  0,  0,   0,  0},
				
				{ 0,   0,  0,  0,  0,  0,   0,  0},
				
				{ 0,   0,  0,  0,  0,  0,   0,  0}};	
		
		float[][] o = {

				{ 672,  99,  10,  16,  -24,  0,  0,  0},

				{ -108, -144, 42,  0,  0,  0,   0,  0},

				{ 24,  -26,  0,  0,  0,  0,   0,  0},

				{ 14,  0,  0,  0,  0,  0,   0,  0},
				
				{ 0,   0,  0,  0,  0,  0,   0,  0},
				
				{ 0,   0,  0,  0,  0,  0,   0,  0},
				
				{ 0,   0,  0,  0,  0,  0,   0,  0},
				
				{ 0,   0,  0,  0,  0,  0,   0,  0}};	

		float[][] g1 = Matrix.duplicate(g);

		dct.DCT(g1);

		System.out.println("G1 = " + Matrix.toString(g1));

		System.out.println();

		
        // DCT =>  AxGxAT
		float[][] g2 = Matrix.duplicate(g);

		float[][] G2 = Matrix.multiply(A, Matrix.multiply(g2, At));

		System.out.println("G2 = " + Matrix.toString(G2));

		System.out.println();

		
		float[][] Qt = Matrix.transpose(q);
		float[][] J2 = Matrix.multiply(At,Matrix.multiply(r, A));

		System.out.println("J2 = " + Matrix.toString(J2));

		System.out.println();		
		
		
        // IDCT  => ATxG(DCTed)xA
		float[][] g2r = Matrix.multiply(At, Matrix.multiply(o, A));

		System.out.println("g2r = " + Matrix.toString(g2r));

		

		float[][] I1 = Matrix.multiply(At, A);

		System.out.println("I1 = " + Matrix.toString(I1));

		

		float[][] I2 = Matrix.multiply(A, At);

		System.out.println("I2 = " + Matrix.toString(I2));

	}

	

	static float[][] makeDctMatrix(int M, int N) {

		float[][] A = new float[M][N];

		for (int i = 0; i < M; i++) {

			double c_i = (i == 0) ? 1.0 / Math.sqrt(2) : 1;

			for (int j = 0; j < N; j++) {

				A[i][j] = (float)

//						\sqrt{\tfrac{2}{N}} \cdot c_i \cdot 

//						\cos\Bigl(\frac{\pi \cdot (2j + 1) \cdot i}{2M}\Bigr) ,

					(Math.sqrt(2.0/N) * c_i * Math.cos(Math.PI * (2*j + 1) * i / (2.0 * M)) );

			}

		}

		

		return A;

	}



}
