import static org.junit.Assert.*;

import org.junit.Test;

public class testStorage {

	// @Test
	public void test_array_repeats()
	{
		Prep pp = new Prep();
		int k = 2;

		int[][] target =
		{
		{ 11, 22, 33, 44 },
		{ 55, 66, 77, 88 },
		{ 90, 10, 11, 12 },
		{ 13, 14, 15, 16 } };
		int[][] repeats = new int[target.length * k][target[0].length * k];

		for ( int i = 0; i < repeats.length; i++ ) {
			for ( int j = 0; j < repeats[0].length; j++ ) {
				repeats[i][j] = target[i % target.length][j % target[0].length];
			}
		}
		pp.print2DArray( repeats );
	}

	// @Test
	public void test_avg_and_mapping()
	{
		Prep pp = new Prep();
		int[][] resid =
		{
		{ -2, 3, -2 },
		{ 1, 0, 3 },
		{ -4, 3, 2 } };

		System.out.println( pp.getAvgPixValue( resid ) );
		ImageJr res = new ImageJr();

	}

	// @Test
	public void test_tograyRetImgJr() throws InterruptedException
	{

		ImageJr img = new ImageJr( "Ducky.ppm" );
		ImageJr gray = img.toGray();
		gray.display( "gray" );
		Thread.sleep( 3000 );
	}

	// @Test
	public void test_enlarge() throws InterruptedException
	{
		ImageJr img = new ImageJr( "redblack.ppm" );
		Image large = img.enlargeImg( 3 );

		img.display( "original" );
		Thread.sleep( 2000 );
		large.display( "large" );
		Thread.sleep( 2000 );
	}

	// @Test
	public void test()
	{
		fail( "Not yet implemented" );
	}

	// @Test
	public void test_howlarge_is_16by16() throws InterruptedException
	{
		Prep pp = new Prep();
		Image img = new ImageJr( 16, 16 );

		img.display( "16 x 16" );
		Thread.sleep( 2000 );
	}

	// @Test
	public void test_togray() throws InterruptedException
	{
		Prep pp = new Prep();
		Image img = new ImageJr( "Dune.ppm" );
		int[][] ia = pp.toGray( img );
		Image grayimg = ( (ImageJr) img ).array2DtoImageJr( ia );
		grayimg.display( "gray dune" );
		Thread.sleep( 2000 );
	}

	// @Test
	public void test_square2dArray()
	{
		Prep pr = new Prep();
		pr.square2dArray( 6 );
	}

}
