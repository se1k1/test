import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class Image {
	protected int width; // number of columns
	protected int height; // number of rows
	protected int pixelDepth = 3; // pixel depth in byte
	BufferedImage img; // image array to store rgb values, 8 bits per channel

	public Image()
	{
	}

	public Image( int w, int h )
	// create an empty image with width and height
	{
		width = w;
		height = h;

		img = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
		// System.out.println( "Created an empty image with size " + width + "x"
		// + height );
	}

	public Image( String fileName )
	// Create an image and read the data from the file
	{
		readPPM( fileName );
		// System.out.println( "Created an image from " + fileName +
		// " with size "
		// + width + "x" + height );
	}

	public int getW()
	{
		return width;
	}

	public void setW( int w )
	{
		width = w;
	}

	public int getH()
	{
		return height;
	}

	public void setH( int h )
	{
		height = h;
	}

	public int getSize()
	// return the image size in byte
	{
		return width * height * pixelDepth;
	}

	public void setPixel( int x, int y, byte[] rgb )
	// set rgb values at (x,y)
	{
		int pix = 0xff000000 | ( ( rgb[0] & 0xff ) << 16 )
				| ( ( rgb[1] & 0xff ) << 8 ) | ( rgb[2] & 0xff );
		img.setRGB( x, y, pix );
	}

	public void setPixel( int x, int y, int[] irgb )
	// set rgb values at (x,y)
	{
		byte[] rgb = new byte[3];

		for ( int i = 0; i < 3; i++ )
			rgb[i] = (byte) irgb[i];

		setPixel( x, y, rgb );
	}

	public void setPixel( int x, int y, int gray )
	// set rgb values at (x,y)
	{
		byte[] rgb = new byte[3];

		for ( int i = 0; i < 3; i++ )
			rgb[i] = (byte) gray;

		setPixel( x, y, rgb );
	}

	public void getPixel( int x, int y, byte[] rgb )
	// retreive rgb values at (x,y) and store in the array
	{
		int pix = img.getRGB( x, y );

		rgb[2] = (byte) pix;
		rgb[1] = (byte) ( pix >> 8 );
		rgb[0] = (byte) ( pix >> 16 );
	}

	public void getPixel( int x, int y, int[] rgb )
	// retreive rgb values at (x,y) and store in the array
	{
		int pix = img.getRGB( x, y );

		byte b = (byte) pix;
		byte g = (byte) ( pix >> 8 );
		byte r = (byte) ( pix >> 16 );

		// converts singed byte value (~128-127) to unsigned byte value (0~255)
		rgb[0] = (int) ( 0xFF & r );
		rgb[1] = (int) ( 0xFF & g );
		rgb[2] = (int) ( 0xFF & b );
	}

	public void displayPixelValue( int x, int y )
	// Display rgb pixel value at (x,y)
	{
		int pix = img.getRGB( x, y );

		byte b = (byte) pix;
		byte g = (byte) ( pix >> 8 );
		byte r = (byte) ( pix >> 16 );

		System.out.println( "RGB Pixel value at (" + x + "," + y + "):"
				+ ( 0xFF & r ) + "," + ( 0xFF & g ) + "," + ( 0xFF & b ) );
	}

	public void readPPM( String fileName )
	// read a data from a PPM file
	{
		FileInputStream fis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream( fileName );
			dis = new DataInputStream( fis );

			// System.out.println( "Reading " + fileName + "..." );

			// read Identifier
			if ( !dis.readLine().equals( "P6" ) ) {
				System.err.println( "This is NOT P6 PPM. Wrong Format." );
				System.exit( 0 );
			}

			// read Comment line
			String commentString = dis.readLine();

			// read width & height
			String[] WidthHeight = dis.readLine().split( " " );
			width = Integer.parseInt( WidthHeight[0] );
			height = Integer.parseInt( WidthHeight[1] );

			// read maximum value
			int maxVal = Integer.parseInt( dis.readLine() );

			if ( maxVal != 255 ) {
				System.err.println( "Max val is not 255" );
				System.exit( 0 );
			}

			// read binary data byte by byte
			int x, y;
			// fBuffer = new Pixel[height][width];
			img = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
			byte[] rgb = new byte[3];
			int pix;

			for ( y = 0; y < height; y++ ) {
				for ( x = 0; x < width; x++ ) {
					rgb[0] = dis.readByte();
					rgb[1] = dis.readByte();
					rgb[2] = dis.readByte();
					setPixel( x, y, rgb );
				}
			}
			dis.close();
			fis.close();

			// System.out.println( "Read " + fileName + " Successfully." );

		} // try
		catch ( Exception e ) {
			System.err.println( e.getMessage() );
		}
	}

	public void write2PPM( String fileName )
	// wrrite the image data in img to a PPM file
	{
		FileOutputStream fos = null;
		PrintWriter dos = null;

		try {
			fos = new FileOutputStream( fileName );
			dos = new PrintWriter( fos );

			System.out.println( "Writing the Image buffer into " + fileName
					+ "..." );

			// write header
			dos.print( "P6" + "\n" );
			dos.print( "#CS451" + "\n" );
			dos.print( width + " " + height + "\n" );
			dos.print( 255 + "\n" );
			dos.flush();

			// write data
			int x, y;
			byte[] rgb = new byte[3];
			for ( y = 0; y < height; y++ ) {
				for ( x = 0; x < width; x++ ) {
					getPixel( x, y, rgb );
					fos.write( rgb[0] );
					fos.write( rgb[1] );
					fos.write( rgb[2] );

				}
				fos.flush();
			}
			dos.close();
			fos.close();

			System.out.println( "Wrote into " + fileName + " Successfully." );

		} // try
		catch ( Exception e ) {
			System.err.println( e.getMessage() );
		}
	}

	public void display( String title )
	// display the image on the screen
	{
		// Use a label to display the image
		JFrame frame = new JFrame();
		JLabel label = new JLabel( new ImageIcon( img ) );
		frame.add( label, BorderLayout.CENTER );
		frame.setTitle( title );
		frame.pack();
		frame.setVisible( true );
	}

	public void display_ks( String title )
	// display the image on the screen
	{
		// Use a label to display the image
		JFrame frame = new JFrame();
		JLabel label = new JLabel( new ImageIcon( img ) );
		frame.add( label, BorderLayout.CENTER );
		frame.setTitle( title );
		frame.pack();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setVisible( true );

	}

	public Image deepCopyImageJr( Image original_img )
	{

		Image clone = new Image( original_img.width, original_img.height );
		int[] temp_rgb = new int[3];

		for ( int y = 0; y < height; y++ ) {
			for ( int x = 0; x < width; x++ ) {
				original_img.getPixel( x, y, temp_rgb );
				clone.setPixel( x, y, temp_rgb );
			}
		}
		return clone;
	}
} // Image class