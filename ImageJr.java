public class ImageJr extends Image {
	int block_size = 8;

	public ImageJr()
	{
		super();
	}

	public ImageJr( int w, int h )
	{
		super( w, h );
		// TODO Auto-generated constructor stub
	}

	public ImageJr( String fileName )
	{
		super( fileName );
		// TODO Auto-generated constructor stub
	}

	public int getR( int x, int y )
	// retreive R values at (x,y)
	{
		int[] rgb = new int[3];
		getPixel( x, y, rgb );
		return rgb[0];
	}

	public int getG( int x, int y )
	// retreive G values at (x,y)
	{
		int[] rgb = new int[3];
		getPixel( x, y, rgb );
		return rgb[1];
	}

	public int getB( int x, int y )
	// retreive B values at (x,y)
	{
		int[] rgb = new int[3];
		getPixel( x, y, rgb );
		return rgb[2];
	}

	public Image deep_copy_image_ks()
	{

		Image clone = new Image( width, height );
		int[] temp_rgb = new int[3];

		for ( int y = 0; y < height; y++ ) {
			for ( int x = 0; x < width; x++ ) {
				getPixel( x, y, temp_rgb );
				clone.setPixel( x, y, temp_rgb );
			}
		}
		return clone;
	}

	public Image padImage()
	{

		int new_w = width + ( block_size - width % block_size );
		int new_h = height + ( block_size - height % block_size );
		Image m8 = new Image( new_w, new_h );
		int[] temp_rgb = new int[3];

		for ( int y = 0; y < height; y++ ) {
			for ( int x = 0; x < width; x++ ) {
				getPixel( x, y, temp_rgb );
				m8.setPixel( x, y, temp_rgb );
			}
		}
		return m8;
	}

	public double[][] padImageArray( double[][] array )
	{

		int new_w = array[0].length
				+ ( block_size - array[0].length % block_size );
		int new_h = array.length + ( block_size - array.length % block_size );
		double[][] adjusted = new double[new_h][new_w];

		for ( int i = 0; i < array.length; i++ ) {
			for ( int j = 0; j < array[0].length; j++ ) {
				adjusted[i][j] = array[i][j];
			}
		}
		return adjusted;
	}

	public ImageJr padImage( ImageJr img )
	{

		int new_w = img.getW() + ( block_size - img.getW() % block_size );
		int new_h = img.getH() + ( block_size - img.getH() % block_size );
		ImageJr m8 = new ImageJr( new_w, new_h );
		int[] temp_rgb = new int[3];

		for ( int y = 0; y < img.getH(); y++ ) {
			for ( int x = 0; x < img.getW(); x++ ) {
				img.getPixel( x, y, temp_rgb );
				m8.setPixel( x, y, temp_rgb );
			}
		}
		return m8;
	}

	public ImageJr depadImage( ImageJr img, int original_w, int original_h )
	{
		int[] temp_rgb = new int[3];
		ImageJr depadded = new ImageJr( original_w, original_h );
		for ( int y = 0; y < original_h; y++ ) {
			for ( int x = 0; x < original_w; x++ ) {
				img.getPixel( x, y, temp_rgb );
				depadded.setPixel( x, y, temp_rgb );
			}
		}
		return depadded;
	}

	public double[][][] rgb_to_ycbcr_w_debug_pout( ImageJr img )
	{
		int test_x = 88, test_y = 98;
		double[][][] ycbcr = new double[img.getH()][img.getW()][3];
		double Y = 0, Cb = 0, Cr = 0;
		double[][] factor =
		{
		{ 0.2990, 0.5870, 0.1140 },
		{ -.1687, -0.3313, 0.5000 },
		{ 0.5000, -0.4187, -0.0813 } };

		for ( int y = 0; y < img.getH(); y++ ) {
			for ( int x = 0; x < img.getW(); x++ ) {
				if ( x == test_x && y == test_y ) {
					System.out.println( img.getR( x, y ) + " "
							+ img.getG( x, y ) + " " + img.getB( x, y ) );
				}
				Y = factor[0][0] * img.getR( x, y ) + factor[0][1]
						* img.getG( x, y ) + factor[0][2] * img.getB( x, y );
				Y = Y > 255 ? 255 : Y;
				Y = Y < 0 ? 0 : Y;
				Y -= 128;

				if ( x == test_x && y == test_y ) {
					System.out.println( factor[0][0] + " * " + img.getR( x, y )
							+ " + " + factor[0][1] + " * " + img.getG( x, y )
							+ " + " + factor[0][2] + " * " + img.getB( x, y )
							+ " = " + Y );
				}

				Cb = factor[1][0] * ( (ImageJr) img ).getR( x, y )
						+ factor[1][1] * img.getG( x, y ) + factor[1][2]
						* img.getB( x, y );
				Cb = Cb > 127.5 ? 127.5 : Cb;
				Cb = Cb < -127.5 ? 0 : Cb;
				Cb -= .5;

				if ( x == test_x && y == test_y ) {
					System.out.println( factor[1][0] + " * " + img.getR( x, y )
							+ " + " + factor[1][1] + " * " + img.getG( x, y )
							+ " + " + factor[1][2] + " * " + img.getB( x, y )
							+ " = " + Cb );
				}

				Cr = factor[2][0] * ( (ImageJr) img ).getR( x, y )
						+ factor[2][1] * img.getG( x, y ) + factor[2][2]
						* img.getB( x, y );
				Cr = Cr > 127.5 ? 127.5 : Cr;
				Cr = Cr < -127.5 ? 0 : Cr;
				Cr -= .5;

				if ( x == test_x && y == test_y ) {
					System.out.println( factor[2][0] + " * " + img.getR( x, y )
							+ " + " + factor[2][1] + " * " + img.getG( x, y )
							+ " + " + factor[2][2] + " * " + img.getB( x, y )
							+ " = " + Cr );
				}

				ycbcr[y][x][0] = Y;
				ycbcr[y][x][1] = Cb;
				ycbcr[y][x][2] = Cr;

				if ( x == test_x && y == test_y ) {
					System.out.println( "y, cb, cr = " + Y + ", " + Cb + ", "
							+ Cr );
					System.out.println( "y, cb, cr = " + ycbcr[y][x][0] + ", "
							+ ycbcr[y][x][1] + ", " + ycbcr[y][x][2] );
				}
			}
		}
		return ycbcr;
	}

	public void subsample_chrominance( double[][][] ycbcr )
	{
		double Cb = 0, Cr = 0;
		System.out.println( "original array dimension: " + ycbcr.length + " "
				+ ycbcr[0].length + " " + ycbcr[0][0].length );

		for ( int i = 0; i < ycbcr.length; i += 2 ) {
			for ( int j = 0; j < ycbcr[i].length; j += 2 ) {
				Cb = ycbcr[i][j][1] * ( 1 / 4. ) + ycbcr[i + 1][j][1]
						* ( 1 / 4. ) + ycbcr[i][j + 1][1] * ( 1 / 4. )
						+ ycbcr[i + 1][j + 1][1] * ( 1 / 4. );
				Cr = ycbcr[i][j][2] * ( 1 / 4. ) + ycbcr[i + 1][j][2]
						* ( 1 / 4. ) + ycbcr[i][j + 1][2] * ( 1 / 4. )
						+ ycbcr[i + 1][j + 1][2] * ( 1 / 4. );

				ycbcr[i][j][1] = Cb;
				ycbcr[i][j][2] = Cr;
				ycbcr[i + 1][j][1] = Cb;
				ycbcr[i + 1][j][2] = Cr;
				ycbcr[i][j + 1][1] = Cb;
				ycbcr[i][j + 1][2] = Cr;
				ycbcr[i + 1][j + 1][1] = Cb;
				ycbcr[i + 1][j + 1][2] = Cr;
			}
		}
	}

	public void subsample_chrominance( double[][][] ycbcr, double[][] cb,
			double[][] cr )
	{

		double Cb = 0, Cr = 0;

		for ( int i = 0; i < ycbcr.length; i += 2 ) {
			for ( int j = 0; j < ycbcr[i].length; j += 2 ) {
				Cb = ycbcr[i][j][1] * ( 1 / 4. ) + ycbcr[i + 1][j][1]
						* ( 1 / 4. ) + ycbcr[i][j + 1][1] * ( 1 / 4. )
						+ ycbcr[i + 1][j + 1][1] * ( 1 / 4. );
				Cr = ycbcr[i][j][2] * ( 1 / 4. ) + ycbcr[i + 1][j][2]
						* ( 1 / 4. ) + ycbcr[i][j + 1][2] * ( 1 / 4. )
						+ ycbcr[i + 1][j + 1][2] * ( 1 / 4. );

				ycbcr[i][j][1] = Cb;
				ycbcr[i][j][2] = Cr;
				ycbcr[i + 1][j][1] = Cb;
				ycbcr[i + 1][j][2] = Cr;
				ycbcr[i][j + 1][1] = Cb;
				ycbcr[i][j + 1][2] = Cr;
				ycbcr[i + 1][j + 1][1] = Cb;
				ycbcr[i + 1][j + 1][2] = Cr;

				cb[i / 2][j / 2] = Cb;
				cr[i / 2][j / 2] = Cr;
			}
		}
	}

	public ImageJr array3d_to_img_jr( double[][][] array )
	{
		ImageJr rgb_img = new ImageJr( array[0].length, array.length );
		int[] rgb = new int[3];

		for ( int y = 0; y < array.length; y++ ) {
			for ( int x = 0; x < array[y].length; x++ ) {

				rgb[0] = (int) Math.round( array[y][x][0] );
				rgb[1] = (int) Math.round( array[y][x][1] );
				rgb[2] = (int) Math.round( array[y][x][2] );

				rgb_img.setPixel( x, y, rgb );
			}
		}
		return rgb_img;
	}

	public ImageJr ycbcr_to_rgb( double[][][] ycbcr )
	{
		int test_x = 88, test_y = 98;
		ImageJr rgb_img = new ImageJr( ycbcr[0].length, ycbcr.length );
		double R = 0, G = 0, B = 0, Y = 0, Cb = 0, Cr = 0;
		int[] rgb = new int[3];
		double[][] factor =
		{
		{ 1.0000, 0, 1.4020 },
		{ 1.0000, -0.3441, -0.7141 },
		{ 1.0000, 1.7720, 0 } };

		for ( int y = 0; y < ycbcr.length; y++ ) {
			for ( int x = 0; x < ycbcr[y].length; x++ ) {

				Y = ycbcr[y][x][0] + 128;
				Cb = ycbcr[y][x][1] + .5;
				Cr = ycbcr[y][x][2] + .5;

				Y = Y > 255 ? 255 : Y;
				Y = Y < 0 ? 0 : Y;

				R = factor[0][0] * Y + factor[0][1] * Cb + factor[0][2] * Cr;

				G = factor[1][0] * Y + factor[1][1] * Cb + factor[1][2] * Cr;

				B = factor[2][0] * Y + factor[2][1] * Cb + factor[2][2] * Cr;

				R = R > 255 ? 255 : R;
				R = R < 0 ? 0 : R;
				G = G > 255 ? 255 : G;
				G = G < 0 ? 0 : G;
				B = B > 255 ? 255 : B;
				B = B < 0 ? 0 : B;

				rgb[0] = (int) Math.round( R );
				rgb[1] = (int) Math.round( G );
				rgb[2] = (int) Math.round( B );

				rgb_img.setPixel( x, y, rgb );

				if ( x == test_x && y == test_y ) {
					rgb_img.displayPixelValue( x, y );
				}
			}
		}
		return rgb_img;
	}

	/* current */
	public double[][][] ycbcr_to_dct_return_3D( double[][][] ycc )
	{
		double[][][] dct = new double[ycc.length][ycc[0].length][ycc[0][0].length];
		double[][][] dct_Y = new double[block_size][block_size][ycc[0][0].length]; // 8
																					// x
																					// 8
		// double[][] dct_cb = new double[block_size][block_size];
		// double[][] dct_cr = new double[block_size][block_size];

		for ( int i = 0; i < ycc.length; i += block_size ) {
			for ( int j = 0; j < ycc[i].length; j += block_size ) {

				dct_Y = compute_dct_8_by_8( ycc, i, j );
				// dct_cb = compute_dct_8_by_8( ycc, i, j );
				// dct_cr = compute_dct_8_by_8( ycc, i, j );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						dct[i + ii][j + jj][0] = dct_Y[ii][jj][0];
						dct[i + ii][j + jj][1] = dct_Y[ii][jj][1];
						dct[i + ii][j + jj][2] = dct_Y[ii][jj][2];
					}
				}
			}
		}
		return dct;
	}

	public double[][] ycbcr_to_dct( double[][] ycc )
	{
		double[][] dct = new double[ycc.length][ycc[0].length];
		double[][] temp_dct = new double[block_size][block_size]; // 8
																	// x
		for ( int i = 0; i < ycc.length; i += block_size ) {
			for ( int j = 0; j < ycc[i].length; j += block_size ) {

				temp_dct = compute_dct_8_by_8( ycc, i, j );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						dct[i + ii][j + jj] = temp_dct[ii][jj];
					}
				}
			}
		}
		return dct;
	}

	public int[][][] quantize( double[][][] dct, int n )
	{

		int[][][] Q = new int[dct.length][dct[0].length][dct[0][0].length];
		double[][][] Q_double = new double[dct.length][dct[0].length][dct[0][0].length];
		int[][][] q = new int[dct.length][dct[0].length][dct[0][0].length];
		for ( int i = 0; i < dct.length; i += block_size ) {
			for ( int j = 0; j < dct[i].length; j += block_size ) {

				q = quantize_8_by_8( dct, i, j, n );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						Q[i + ii][j + jj][0] = q[ii][jj][0];
						Q[i + ii][j + jj][1] = q[ii][jj][1];
						Q[i + ii][j + jj][2] = q[ii][jj][2];

						Q_double[i + ii][j + jj][0] = q[ii][jj][0];
						Q_double[i + ii][j + jj][1] = q[ii][jj][1];
						Q_double[i + ii][j + jj][2] = q[ii][jj][2];
					}
				}
			}
		}

		// ycbcr_to_rgb( dct_to_ycbcr_return_3D( Q_double ) ).display(
		// "inside quantize()" );
		return Q;
	}

	public int[][] quantize_lumi( double[][] dct, int n )
	{

		int[][] Q = new int[dct.length][dct[0].length];
		double[][] Q_double = new double[dct.length][dct[0].length];
		int[][] q = new int[dct.length][dct[0].length];
		for ( int i = 0; i < dct.length; i += block_size ) {
			for ( int j = 0; j < dct[i].length; j += block_size ) {

				q = quantize_8_by_8_lumi( dct, i, j, n );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						Q[i + ii][j + jj] = q[ii][jj];

						Q_double[i + ii][j + jj] = q[ii][jj];
					}
				}
			}
		}

		// ycbcr_to_rgb( dct_to_ycbcr_return_3D( Q_double ) ).display(
		// "inside quantize()" );
		return Q;
	}

	public int[][] quantize_chromi( double[][] dct, int n )
	{

		int[][] Q = new int[dct.length][dct[0].length];
		double[][] Q_double = new double[dct.length][dct[0].length];
		int[][] q = new int[dct.length][dct[0].length];
		for ( int i = 0; i < dct.length; i += block_size ) {
			for ( int j = 0; j < dct[i].length; j += block_size ) {

				q = quantize_8_by_8_chromi( dct, i, j, n );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						Q[i + ii][j + jj] = q[ii][jj];

						Q_double[i + ii][j + jj] = q[ii][jj];
					}
				}
			}
		}

		// ycbcr_to_rgb( dct_to_ycbcr_return_3D( Q_double ) ).display(
		// "inside quantize()" );
		return Q;
	}

	public double[][][] de_quantize_double( double[][][] quantized, int n )
	{
		double[][][] deQ = new double[quantized.length][quantized[0].length][quantized[0][0].length];
		double[][][] de_q = new double[quantized.length][quantized[0].length][quantized[0][0].length];
		for ( int i = 0; i < quantized.length; i += block_size ) {
			for ( int j = 0; j < quantized[i].length; j += block_size ) {

				de_q = de_quantize_8_by_8_double( quantized, i, j, n );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						deQ[i + ii][j + jj][0] = de_q[ii][jj][0];
						deQ[i + ii][j + jj][1] = de_q[ii][jj][1];
						deQ[i + ii][j + jj][2] = de_q[ii][jj][2];
					}
				}
			}
		}
		ycbcr_to_rgb( dct_to_ycbcr_return_3D( deQ ) ).display(
				"inside DE quantize()" );
		return deQ;
	}

	public double[][][] de_quantize( int[][][] quantized, int n )
	{
		double[][][] deQ = new double[quantized.length][quantized[0].length][quantized[0][0].length];
		int[][][] de_q = new int[quantized.length][quantized[0].length][quantized[0][0].length];

		for ( int i = 0; i < quantized.length; i += block_size ) {
			for ( int j = 0; j < quantized[i].length; j += block_size ) {

				de_q = de_quantize_8_by_8( quantized, i, j, n );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						deQ[i + ii][j + jj][0] = de_q[ii][jj][0];
						deQ[i + ii][j + jj][1] = de_q[ii][jj][1];
						deQ[i + ii][j + jj][2] = de_q[ii][jj][2];
					}
				}
			}
		}
		// ycbcr_to_rgb( dct_to_ycbcr_return_3D( deQ ) ).display(
		// "inside DE quantize()" );
		return deQ;
	}

	public double[][] de_quantize_lumi( int[][] quantized, int n )
	{
		double[][] deQ = new double[quantized.length][quantized[0].length];
		int[][] de_q = new int[quantized.length][quantized[0].length];

		for ( int i = 0; i < quantized.length; i += block_size ) {
			for ( int j = 0; j < quantized[i].length; j += block_size ) {

				de_q = de_quantize_8_by_8_lumi( quantized, i, j, n );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						deQ[i + ii][j + jj] = de_q[ii][jj];
					}
				}
			}
		}
		return deQ;
	}

	public double[][] de_quantize_chromi( int[][] quantized, int n )
	{
		double[][] deQ = new double[quantized.length][quantized[0].length];
		int[][] de_q = new int[quantized.length][quantized[0].length];

		for ( int i = 0; i < quantized.length; i += block_size ) {
			for ( int j = 0; j < quantized[i].length; j += block_size ) {

				de_q = de_quantize_8_by_8_chromi( quantized, i, j, n );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						deQ[i + ii][j + jj] = de_q[ii][jj];
					}
				}
			}
		}
		return deQ;
	}

	public int[][][] quantize_8_by_8( double[][][] dct, int start_i,
			int start_j, int n )
	{
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_lumi =
		{
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 8, 8, 16, 16, 32, 32 },
		{ 8, 8, 8, 16, 16, 32, 32, 32, },
		{ 8, 8, 16, 16, 32, 32, 32, 32 },
		{ 16, 16, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		double[][] q_tbl_chromi =
		{
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		int[][][] quantized = new int[block_size][block_size][dct[0][0].length];
		int temp_dct_value = 0;

		for ( int i = 0; i < quantized.length; i++ ) {
			for ( int j = 0; j < quantized.length; j++ ) {
				quantized[i][j][0] = (int) Math.round( dct[start_i + i][start_j
						+ j][0]
						/ ( q_tbl_lumi[i][j] * Math.pow( 2, n ) ) );
				quantized[i][j][1] = (int) Math.round( dct[start_i + i][start_j
						+ j][1]
						/ ( q_tbl_chromi[i][j] * Math.pow( 2, n ) ) );
				quantized[i][j][2] = (int) Math.round( dct[start_i + i][start_j
						+ j][2]
						/ ( q_tbl_chromi[i][j] * Math.pow( 2, n ) ) );
			}
		}
		return quantized;
	}

	public int[][] quantize_8_by_8_lumi( double[][] dct, int start_i,
			int start_j, int n )
	{
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_lumi =
		{
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 8, 8, 16, 16, 32, 32 },
		{ 8, 8, 8, 16, 16, 32, 32, 32, },
		{ 8, 8, 16, 16, 32, 32, 32, 32 },
		{ 16, 16, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		int[][] quantized = new int[block_size][block_size];
		int temp_dct_value = 0;

		for ( int i = 0; i < quantized.length; i++ ) {
			for ( int j = 0; j < quantized.length; j++ ) {
				quantized[i][j] = (int) Math.round( dct[start_i + i][start_j
						+ j]
						/ ( q_tbl_lumi[i][j] * Math.pow( 2, n ) ) );
			}
		}
		return quantized;
	}

	public double[][][] quantize_8_by_8_double( double[][][] dct, int start_i,
			int start_j, int n )
	{
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_lumi =
		{
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 8, 8, 16, 16, 32, 32 },
		{ 8, 8, 8, 16, 16, 32, 32, 32, },
		{ 8, 8, 16, 16, 32, 32, 32, 32 },
		{ 16, 16, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		double[][] q_tbl_chromi =
		{
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		double[][][] quantized = new double[block_size][block_size][dct[0][0].length];

		for ( int i = 0; i < quantized.length; i++ ) {
			for ( int j = 0; j < quantized.length; j++ ) {

				quantized[i][j][0] = ( quantized[i][j][0] > Math.pow( 2, 10 ) ? Math
						.pow( 2, 10 ) : quantized[i][j][0] );
				quantized[i][j][0] = ( quantized[i][j][0] < -1
						* Math.pow( 2, 10 ) ? -1 * Math.pow( 2, 10 )
						: quantized[i][j][0] );

				quantized[i][j][0] = dct[start_i + i][start_j + j][0]
						/ ( q_tbl_lumi[i][j] * Math.pow( 2, n ) );
				quantized[i][j][1] = dct[start_i + i][start_j + j][1]
						/ ( q_tbl_chromi[i][j] * Math.pow( 2, n ) );
				quantized[i][j][2] = dct[start_i + i][start_j + j][2]
						/ ( q_tbl_chromi[i][j] * Math.pow( 2, n ) );
			}
		}

		return quantized;
	}

	public int[][][] de_quantize_8_by_8( int[][][] Q, int start_i, int start_j,
			int n )
	{
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_lumi =
		{
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 8, 8, 16, 16, 32, 32 },
		{ 8, 8, 8, 16, 16, 32, 32, 32, },
		{ 8, 8, 16, 16, 32, 32, 32, 32 },
		{ 16, 16, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		double[][] q_tbl_chromi =
		{
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		int[][][] deQ = new int[block_size][block_size][Q[0][0].length];
		for ( int i = 0; i < deQ.length; i++ ) {
			for ( int j = 0; j < deQ.length; j++ ) {
				deQ[i][j][0] = (int) Math.round( Q[start_i + i][start_j + j][0]
						* q_tbl_lumi[i][j] * Math.pow( 2, n ) );
				deQ[i][j][1] = (int) Math.round( Q[start_i + i][start_j + j][1]
						* q_tbl_chromi[i][j] * Math.pow( 2, n ) );
				deQ[i][j][2] = (int) Math.round( Q[start_i + i][start_j + j][2]
						* q_tbl_chromi[i][j] * Math.pow( 2, n ) );
			}
		}
		return deQ;
	}

	public double[][][] de_quantize_8_by_8_double( double[][][] Q, int start_i,
			int start_j, int n )
	{
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_lumi =
		{
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 8, 8, 16, 16, 32, 32 },
		{ 8, 8, 8, 16, 16, 32, 32, 32, },
		{ 8, 8, 16, 16, 32, 32, 32, 32 },
		{ 16, 16, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		double[][] q_tbl_chromi =
		{
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		double[][][] deQ = new double[block_size][block_size][Q[0][0].length];
		for ( int i = 0; i < deQ.length; i++ ) {
			for ( int j = 0; j < deQ.length; j++ ) {
				deQ[i][j][0] = Q[start_i + i][start_j + j][0]
						* q_tbl_lumi[i][j] * Math.pow( 2, n );
				deQ[i][j][1] = Q[start_i + i][start_j + j][1]
						* q_tbl_chromi[i][j] * Math.pow( 2, n );
				deQ[i][j][2] = Q[start_i + i][start_j + j][2]
						* q_tbl_chromi[i][j] * Math.pow( 2, n );
			}
		}

		if ( start_i == 88 ) {

		}

		return deQ;
	}

	public int[][] de_quantize_8_by_8_lumi( int[][] Q, int start_i,
			int start_j, int n )
	{
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_lumi =
		{
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 8, 8, 16, 16, 32, 32 },
		{ 8, 8, 8, 16, 16, 32, 32, 32, },
		{ 8, 8, 16, 16, 32, 32, 32, 32 },
		{ 16, 16, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		int[][] deQ = new int[block_size][block_size];
		for ( int i = 0; i < deQ.length; i++ ) {
			for ( int j = 0; j < deQ.length; j++ ) {
				deQ[i][j] = (int) Math.round( Q[start_i + i][start_j + j]
						* q_tbl_lumi[i][j] * Math.pow( 2, n ) );
			}
		}

		return deQ;
	}

	public int[][] de_quantize_8_by_8_chromi( int[][] Q, int start_i,
			int start_j, int n )
	{
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_chromi =
		{
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		int[][] deQ = new int[block_size][block_size];
		for ( int i = 0; i < deQ.length; i++ ) {
			for ( int j = 0; j < deQ.length; j++ ) {
				deQ[i][j] = (int) Math.round( Q[start_i + i][start_j + j]
						* q_tbl_chromi[i][j] * Math.pow( 2, n ) );
			}
		}
		return deQ;
	}

	public int[][] quantize_8_by_8_chromi( double[][] dct, int start_i,
			int start_j, int n )
	{
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_chromi =
		{
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		int[][] quantized = new int[block_size][block_size];
		int temp_dct_value = 0;

		for ( int i = 0; i < quantized.length; i++ ) {
			for ( int j = 0; j < quantized.length; j++ ) {
				quantized[i][j] = (int) Math.round( dct[start_i + i][start_j
						+ j]
						/ ( q_tbl_chromi[i][j] * Math.pow( 2, n ) ) );

			}
		}

		return quantized;
	}

	public double[][][] quantize_with_n( double[][][] dct, int n )
	{
		n = (int) Math.pow( 2, n );
		// quantized(F_uv)=round(F_uv/Q_uv)
		double[][] q_tbl_lumi =
		{
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 4, 8, 8, 16, 16, 32 },
		{ 4, 4, 8, 8, 16, 16, 32, 32 },
		{ 8, 8, 8, 16, 16, 32, 32, 32, },
		{ 8, 8, 16, 16, 32, 32, 32, 32 },
		{ 16, 16, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		double[][] q_tbl_chromi =
		{
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 8, 16, 32, 32, 32, 32 },
		{ 8, 8, 16, 32, 32, 32, 32, 32 },
		{ 16, 16, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 },
		{ 32, 32, 32, 32, 32, 32, 32, 32 } };

		double[][] q_tbl_lumi_prime = new double[q_tbl_lumi.length][q_tbl_lumi[0].length];
		double[][] q_tbl_chromi_prime = new double[q_tbl_lumi.length][q_tbl_lumi[0].length];

		for ( int i = 0; i < q_tbl_chromi_prime.length; i++ ) {
			for ( int j = 0; j < q_tbl_chromi_prime[0].length; j++ ) {
				q_tbl_lumi_prime[i][j] = q_tbl_lumi[i][j] / n;
				q_tbl_chromi_prime[i][j] = q_tbl_chromi[i][j] / n;
			}

		}

		double[][][] quantized = new double[dct.length][dct[0].length][dct[0][0].length];
		for ( int i = 0; i < quantized.length; i++ ) {
			for ( int j = 0; j < quantized.length; j++ ) {
				quantized[i][j][0] = dct[i][j][0] / q_tbl_lumi[i][j];
				quantized[i][j][1] = dct[i][j][1] / q_tbl_chromi[i][j];
				quantized[i][j][2] = dct[i][j][2] / q_tbl_chromi[i][j];
			}
		}

		return quantized;
	}

	public double[][][] dct_to_ycbcr_return_3D( double[][][] dct )
	{
		double[][][] ycc = new double[dct.length][dct[0].length][dct[0][0].length];
		/* 8 x 8 x 3 */
		double[][][] ycc_Y = new double[block_size][block_size][dct[0][0].length];

		// System.out.println( "\n\ninside dct_to_ycbcr_return_3D (ycc_Y):" );
		for ( int i = 0; i < ycc.length; i += block_size ) {
			for ( int j = 0; j < ycc[i].length; j += block_size ) {

				ycc_Y = compute_idct_8_by_8( dct, i, j );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						ycc[i + ii][j + jj][0] = ycc_Y[ii][jj][0];
						ycc[i + ii][j + jj][1] = ycc_Y[ii][jj][1];
						ycc[i + ii][j + jj][2] = ycc_Y[ii][jj][2];

					}
				}
			}
		}

		return ycc;
	}

	public double[][] dct_to_ycbcr( double[][] dct )
	{
		double[][] ycbcr = new double[dct.length][dct[0].length];
		/* 8 x 8 x 3 */
		double[][] temp_ycbcr = new double[block_size][block_size];

		for ( int i = 0; i < ycbcr.length; i += block_size ) {
			for ( int j = 0; j < ycbcr[i].length; j += block_size ) {

				temp_ycbcr = compute_idct_8_by_8( dct, i, j );

				for ( int ii = 0; ii < block_size; ii++ ) {
					for ( int jj = 0; jj < block_size; jj++ ) {
						ycbcr[i + ii][j + jj] = temp_ycbcr[ii][jj];
					}
				}
			}
		}
		return ycbcr;
	}

	public void setPixel( int x, int y, int r, int g, int b )
	// set rgb values at (x,y)
	{
		byte[] rgb = new byte[3];

		rgb[0] = (byte) r;
		rgb[1] = (byte) g;
		rgb[2] = (byte) b;

		setPixel( x, y, rgb );
	}

	/* current */
	public double[][][] compute_dct_8_by_8( double[][][] f_of_ij/* YCbCr */,
			int start_i, int start_j )
	{
		int u = 0, v = 0, i = 0, j = 0;
		double[][][] F_of_uv = new double[block_size][block_size][f_of_ij[0][0].length];
		double temp_cos_basis1 = 0, temp_cos_basis2 = 0, sum_y = 0, sum_cb = 0, sum_cr = 0, c_of_u = 1, c_of_v = 1;

		for ( u = 0; u < block_size; u++ ) {
			for ( v = 0; v < block_size; v++ ) {
				for ( i = 0; i < block_size; i++ ) {
					for ( j = 0; j < block_size; j++ ) {

						temp_cos_basis1 = Math
								.cos( ( ( 2 * i + 1 ) * u * Math.PI )
										/ ( 2 * block_size ) );
						temp_cos_basis2 = Math
								.cos( ( ( 2 * j + 1 ) * v * Math.PI )
										/ ( 2 * block_size ) );

						sum_y += temp_cos_basis1 * temp_cos_basis2
								* f_of_ij[start_i + i][start_j + j][0];
						sum_cb += temp_cos_basis1 * temp_cos_basis2
								* f_of_ij[start_i + i][start_j + j][1];
						sum_cr += temp_cos_basis1 * temp_cos_basis2
								* f_of_ij[start_i + i][start_j + j][2];
					}// end j loop
				}// end i loop
				c_of_u = ( u == 0 ? ( 1 / Math.sqrt( 2 ) ) : 1 );
				c_of_v = ( v == 0 ? ( 1 / Math.sqrt( 2 ) ) : 1 );

				F_of_uv[u][v][0] = ( ( c_of_u * c_of_v ) / 4 ) * sum_y;
				F_of_uv[u][v][1] = ( ( c_of_u * c_of_v ) / 4 ) * sum_cb;
				F_of_uv[u][v][2] = ( ( c_of_u * c_of_v ) / 4 ) * sum_cr;

				if ( start_j != 8 ) {
					// System.out.print( F_of_uv[u][v] + " " );
				}
				sum_y = 0;
				sum_cb = 0;
				sum_cr = 0;

			}// end v loop
		}// end u loop
		return F_of_uv;
	}

	public double[][] compute_dct_8_by_8(
			double[][] f_of_ij/* Y || Cb || Cr */, int start_i, int start_j )
	{
		int u = 0, v = 0, i = 0, j = 0;
		double[][] F_of_uv = new double[block_size][block_size];
		double temp_cos_basis1 = 0, temp_cos_basis2 = 0, sum = 0, c_of_u = 1, c_of_v = 1;

		for ( u = 0; u < block_size; u++ ) {
			for ( v = 0; v < block_size; v++ ) {
				for ( i = 0; i < block_size; i++ ) {
					for ( j = 0; j < block_size; j++ ) {

						temp_cos_basis1 = Math
								.cos( ( ( 2 * i + 1 ) * u * Math.PI )
										/ ( 2 * block_size ) );
						temp_cos_basis2 = Math
								.cos( ( ( 2 * j + 1 ) * v * Math.PI )
										/ ( 2 * block_size ) );
						sum += temp_cos_basis1 * temp_cos_basis2
								* f_of_ij[start_i + i][start_j + j];

					}// end j loop
				}// end i loop
				c_of_u = ( u == 0 ? ( 1 / Math.sqrt( 2 ) ) : 1 );
				c_of_v = ( v == 0 ? ( 1 / Math.sqrt( 2 ) ) : 1 );

				F_of_uv[u][v] = ( ( c_of_u * c_of_v ) / 4 ) * sum;

				if ( start_j != 8 ) {

				}
				sum = 0;

			}// end v loop
		}// end u loop
		return F_of_uv;
	}

	public double[][][] compute_idct_8_by_8( double[][][] F_of_uv, int start_u,
			int start_v )
	{

		int u = 0, v = 0, i = 0, j = 0;
		double[][][] f_of_ij = new double[block_size][block_size][F_of_uv[0][0].length];// =
																						// YCbCr
		double temp_cos_basis1 = 0, temp_cos_basis2 = 0, sum_y = 0, sum_cb = 0, sum_cr = 0, c_of_u = 1, c_of_v = 1;

		for ( j = 0; j < block_size; j++ ) {
			for ( i = 0; i < block_size; i++ ) {
				for ( u = 0; u < block_size; u++ ) {
					for ( v = 0; v < block_size; v++ ) {

						c_of_u = ( u == 0 ? 1 / Math.sqrt( 2 ) : 1 );
						c_of_v = ( v == 0 ? 1 / Math.sqrt( 2 ) : 1 );

						temp_cos_basis1 = Math
								.cos( ( ( 2 * i + 1 ) * u * Math.PI )
										/ ( 2 * block_size ) );
						temp_cos_basis2 = Math
								.cos( ( ( 2 * j + 1 ) * v * Math.PI )
										/ ( 2 * block_size ) );

						sum_y += ( ( c_of_u * c_of_v ) / 4 ) * temp_cos_basis1
								* temp_cos_basis2
								* F_of_uv[start_u + u][start_v + v][0];
						sum_cb += ( ( c_of_u * c_of_v ) / 4 ) * temp_cos_basis1
								* temp_cos_basis2
								* F_of_uv[start_u + u][start_v + v][1];
						sum_cr += ( ( c_of_u * c_of_v ) / 4 ) * temp_cos_basis1
								* temp_cos_basis2
								* F_of_uv[start_u + u][start_v + v][2];
					}// end u loop
				}// end v loop

				f_of_ij[i][j][0] = sum_y;
				f_of_ij[i][j][1] = sum_cb;
				f_of_ij[i][j][2] = sum_cr;
				sum_y = 0;
				sum_cb = 0;
				sum_cr = 0;

			}// end i loop
		}// end j loop
		return f_of_ij;
	}

	public double[][] compute_idct_8_by_8( double[][] F_of_uv, int start_u,
			int start_v )
	{

		int u = 0, v = 0, i = 0, j = 0;
		double[][] f_of_ij = new double[block_size][block_size];// =
																// YCbCr
		double temp_cos_basis1 = 0, temp_cos_basis2 = 0, sum = 0, c_of_u = 1, c_of_v = 1;

		for ( j = 0; j < block_size; j++ ) {
			for ( i = 0; i < block_size; i++ ) {
				for ( u = 0; u < block_size; u++ ) {
					for ( v = 0; v < block_size; v++ ) {

						c_of_u = ( u == 0 ? 1 / Math.sqrt( 2 ) : 1 );
						c_of_v = ( v == 0 ? 1 / Math.sqrt( 2 ) : 1 );

						temp_cos_basis1 = Math
								.cos( ( ( 2 * i + 1 ) * u * Math.PI )
										/ ( 2 * block_size ) );
						temp_cos_basis2 = Math
								.cos( ( ( 2 * j + 1 ) * v * Math.PI )
										/ ( 2 * block_size ) );

						sum += ( ( c_of_u * c_of_v ) / 4 ) * temp_cos_basis1
								* temp_cos_basis2
								* F_of_uv[start_u + u][start_v + v];

					}// end u loop
				}// end v loop

				f_of_ij[i][j] = sum;
				sum = 0;
			}// end i loop
		}// end j loop
		return f_of_ij;
	}

	public Image enlargeImg( Image img, int factor )
	{

		Image large = new ImageJr( img.getW() * factor, img.getH() * factor );
		int[] rgb = new int[3];
		for ( int y = 0; y < img.getH() * factor; y++ ) {
			for ( int x = 0; x < img.getW() * factor; x++ ) {
				img.getPixel( x / factor, y / factor, rgb );
				large.setPixel( x, y, rgb );
			}
		}
		return large;
	}

	public Image enlargeImg( int factor )
	{

		Image large = new ImageJr( width * factor, height * factor );
		int[] rgb = new int[3];
		for ( int y = 0; y < height * factor; y++ ) {
			for ( int x = 0; x < width * factor; x++ ) {
				getPixel( x / factor, y / factor, rgb );
				large.setPixel( x, y, rgb );
			}
		}
		return large;
	}

	public Image array2DtoImageJr( int[][] imgArray )
	{

		Image img = new ImageJr( imgArray[0].length, imgArray.length );
		int[] gray = new int[3];
		for ( int i = 0; i < imgArray.length; i++ ) {
			for ( int j = 0; j < imgArray[i].length; j++ ) {
				for ( int j2 = 0; j2 < 3; j2++ ) {
					gray[j2] = imgArray[i][j];
					img.setPixel( j, i, gray );
				}
			}
		}
		return img;
	}

	public int[][] toGray( Image img )
	{
		int gray = 0;
		int[] rgb = new int[3];
		int[][] imgArray = new int[img.getH()][img.getW()];
		for ( int i = 0; i < img.getH(); i++ ) {
			for ( int j = 0; j < img.getW(); j++ ) {
				img.getPixel( j, i, rgb );
				gray = (int) Math.round( 0.299 * rgb[0] + 0.587 * rgb[1]
						+ 0.114 * rgb[2] );
				imgArray[i][j] = gray;
			}
		}
		return imgArray;
	}

	public ImageJr toGray()
	{

		ImageJr gray = (ImageJr) deepCopyImage_ks( this );

		int[] temp_rgb = new int[3];
		int temp = 0;
		for ( int x = 0; x < width; x++ ) {
			for ( int y = 0; y < height; y++ ) {

				getPixel( x, y, temp_rgb );

				temp = (int) Math.round( ( .299 * temp_rgb[0] + .587
						* temp_rgb[1] + .114 * temp_rgb[2] ) );

				temp_rgb[0] = temp;
				temp_rgb[1] = temp;
				temp_rgb[2] = temp;

				gray.setPixel( x, y, temp_rgb );
			}
		}
		return gray;
	}

	public ImageJr deepCopyImage_ks( Image original_img )
	{

		Image clone = new ImageJr( original_img.width, original_img.height );
		int[] temp_rgb = new int[3];

		for ( int y = 0; y < height; y++ ) {
			for ( int x = 0; x < width; x++ ) {
				original_img.getPixel( x, y, temp_rgb );
				clone.setPixel( x, y, temp_rgb );
			}
		}
		return (ImageJr) clone;
	}
	
	public int[][] imageJrTo2DArray( Image img )
	{
		ImageJr grayImg = ( (ImageJr) img ).toGray();
		int[][] imgArray = new int[img.getH()][img.getW()];
		int[] gray = new int[3];
		for ( int i = 0; i < imgArray.length; i++ ) {
			for ( int j = 0; j < imgArray[i].length; j++ ) {

				grayImg.getPixel( j, i, gray );
				imgArray[i][j] = gray[0];
			}
		}
		return imgArray;
	}
	public int[][] imageJrTo2DArray( )
	{
		ImageJr grayImg = toGray();
		int[][] imgArray = new int[height][width];
		int[] gray = new int[3];
		for ( int i = 0; i < imgArray.length; i++ ) {
			for ( int j = 0; j < imgArray[i].length; j++ ) {

				grayImg.getPixel( j, i, gray );
				imgArray[i][j] = gray[0];
			}
		}
		return imgArray;
	}
}
