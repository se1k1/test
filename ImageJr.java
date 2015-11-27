public class ImageJr extends Image {
	// int block_size = 8;

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

	public ImageJr padImage( int macroBlkSize )
	{
		if ( width % macroBlkSize == 0 && height % macroBlkSize == 0 ) {
			return this;
		}

		int new_w = width, new_h = height;
		if ( width % macroBlkSize != 0 ) {
			new_w = width + ( macroBlkSize - width % macroBlkSize );
		}
		if ( height % macroBlkSize != 0 ) {
			new_h = height + ( macroBlkSize - height % macroBlkSize );
		}
		ImageJr padded = new ImageJr( new_w, new_h );
		int[] temp_rgb = new int[3];

		for ( int y = 0; y < height; y++ ) {
			for ( int x = 0; x < width; x++ ) {
				getPixel( x, y, temp_rgb );
				padded.setPixel( x, y, temp_rgb );
			}
		}
		return padded;
	}

	public double[][] padImageArray( double[][] array, int macroBlkSize )
	{
		if ( width % macroBlkSize == 0 && height % macroBlkSize == 0 ) {
			return array;
		}
		int new_w = array[0].length
				+ ( macroBlkSize - array[0].length % macroBlkSize );
		int new_h = array.length
				+ ( macroBlkSize - array.length % macroBlkSize );
		double[][] adjusted = new double[new_h][new_w];

		for ( int i = 0; i < array.length; i++ ) {
			for ( int j = 0; j < array[0].length; j++ ) {
				adjusted[i][j] = array[i][j];
			}
		}
		return adjusted;
	}

	public void paddedSize( int macroBlkSize, int[] paddedSize )
	{
		paddedSize[0] = width;
		paddedSize[1] = height;
		if ( width % macroBlkSize == 0 && height % macroBlkSize == 0 ) {
			return;
		}
		if ( width % macroBlkSize != 0 )
			paddedSize[0] = width + ( macroBlkSize - width % macroBlkSize );
		if ( height % macroBlkSize != 0 )
			paddedSize[1] = height + ( macroBlkSize - height % macroBlkSize );
	}

	// public ImageJr padImage( ImageJr img, int macroBlkSize )
	// {
	// if ( img.getW() % macroBlkSize == 0 && img.getH() % macroBlkSize == 0 ) {
	// return this;
	// }
	// int new_w = img.getW() + ( macroBlkSize - img.getW() % macroBlkSize );
	// int new_h = img.getH() + ( macroBlkSize - img.getH() % macroBlkSize );
	// ImageJr m8 = new ImageJr( new_w, new_h );
	// int[] temp_rgb = new int[3];
	//
	// for ( int y = 0; y < img.getH(); y++ ) {
	// for ( int x = 0; x < img.getW(); x++ ) {
	// img.getPixel( x, y, temp_rgb );
	// m8.setPixel( x, y, temp_rgb );
	// }
	// }
	// return m8;
	// }

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

	public ImageJr depadImage( int original_w, int original_h )
	{
		int[] temp_rgb = new int[3];
		ImageJr depadded = new ImageJr( original_w, original_h );
		for ( int y = 0; y < original_h; y++ ) {
			for ( int x = 0; x < original_w; x++ ) {
				this.getPixel( x, y, temp_rgb );
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

	public void setPixel( int x, int y, int r, int g, int b )
	// set rgb values at (x,y)
	{
		byte[] rgb = new byte[3];

		rgb[0] = (byte) r;
		rgb[1] = (byte) g;
		rgb[2] = (byte) b;

		setPixel( x, y, rgb );
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

	public int[][] imageJrTo2DArray()
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

	public void displayImageDimension()
	{
		System.out.println( "w x h: " + width + " x " + height );
	}

	{
	}
}
