import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class testStorage {

	// @Test
	public void test_mc_macroblkSize_equals_imageSize()
			throws InterruptedException
	{
		Prep pp = new Prep();

		int[][] T =
		{
		{ 1, 2, 3, 4 },
		{ 5, 6, 7, 8 },
		{ 9, 10, 11, 12 },
		{ 13, 14, 15, 16 } };
		// {
		// { 1, 20 },
		// { 5, 6 } };

		int[][] R =
		{
		{ 1, 2, 3, 4 },
		{ 5, 6, 7, 8 },
		{ 9, 10, 1, 12 },
		{ 13, 14, 1, 16 } };
		// {
		// { 1, 5 },
		// { 5, 20 } };
		int p = 2;
		ImageJr residual = new ImageJr();
		ImageJr targetImg = (ImageJr) residual.array2DtoImageJr( T );
		ImageJr refImg = (ImageJr) residual.array2DtoImageJr( R );
		System.out.println( "T:" );
		pp.print2DArray( T );
		System.out.println( "R:" );
		pp.print2DArray( R );
		int[][][] motionCompensation = new int[T.length][T[0].length][3];
		int macroBlkSize = 1;

		pp.MC( targetImg, "target", refImg, "reference", p, 0, residual,
				motionCompensation, macroBlkSize );
		System.out.println( T.length );

		pp.print3DArray( motionCompensation );
	}

	// @Test
	public void test_mc_with_synthetic_values() throws InterruptedException
	{

		int[][] target =
		{
		{ 1, 2, 3, 4 },
		{ 5, 6, 7, 8 },
		{ 9, 10, 11, 12 },
		{ 13, 14, 15, 16 } };

		int[][] ref =
		{
		{ 6, 7, 2, 3 },
		{ 10, 11, 6, 10 },
		{ 1, 2, 3, 1 },
		{ 1, 6, 7, 3 } };

		// int[][] T = new int[target.length][target[0].length], R = new
		// int[ref.length][ref[0].length];
		Prep pp = new Prep();
		int[][] T = repeateArrayContents( target, 2 );
		int[][] R = repeateArrayContents( ref, 2 );

		ImageJr residual = new ImageJr();
		ImageJr targetImg = (ImageJr) residual.array2DtoImageJr( T );
		ImageJr refImg = (ImageJr) residual.array2DtoImageJr( R );
		System.out.println( "T:" );
		pp.print2DArray( T );
		System.out.println( "R:" );
		pp.print2DArray( R );
		int[] paddedSize = new int[2];
		int macroBlkSize = 16;
		targetImg.paddedSize( macroBlkSize, paddedSize );
		int[][][] motionCompensation = new int[paddedSize[0]][paddedSize[1]][3];

		// ij.display_ks( "walk 57" );
		// Thread.sleep( 3000 );

		pp.MC( targetImg, "target", refImg, "reference", 2, 0, residual,
				motionCompensation, macroBlkSize );
		System.out.println( T.length );

	}

	public int[][] repeateArrayContents( int[][] target, int k )
	{
		Prep pp = new Prep();
		int[][] repeats = new int[target.length * k][target[0].length * k];

		for ( int i = 0; i < repeats.length; i++ ) {
			for ( int j = 0; j < repeats[0].length; j++ ) {
				repeats[i][j] = target[i % target.length][j % target[0].length];
			}
		}
		pp.print2DArray( repeats );
		return repeats;
	}

	public int randomNumGen( int min, int max )
	{
		Random rd = new Random();
		return rd.nextInt( max ) + min;
	}

	// @Test
	public void test_getMean()
	{

		List<Integer> list = new LinkedList<Integer>();
		ImageJr img = new ImageJr( 4, 4 );
		int min = 0, max = 50;

		for ( int y = 0; y < 4; y++ ) {
			for ( int i = 0; i < 4; i++ ) {
				img.setPixel( i, y, randomNumGen( min, max ) );
			}

		}
		Prep pp = new Prep();
		System.out.println( pp.getMeanPixValue( img ) );

	}

	@Test
	public void test_mc_macroblkSize_equals_1() throws InterruptedException
	{
		Prep pp = new Prep();

		int[][] T =
		{
		{ 1, 2, 3, 4 },
		{ 5, 6, 7, 8 },
		{ 9, 10, 11, 12 },
		{ 13, 14, 15, 16 } };
		// {
		// { 1, 20 },
		// { 5, 6 } };

		int[][] R =
		{
		{ 1, 2, 3, 4 },
		{ 5, 6, 7, 8 },
		{ 9, 10, 1, 12 },
		{ 13, 14, 1, 16 } };
		// {
		// { 1, 5 },
		// { 5, 20 } };
		int p = 1;
		ImageJr residual = new ImageJr();
		ImageJr targetImg = (ImageJr) residual.array2DtoImageJr( T );
		ImageJr refImg = (ImageJr) residual.array2DtoImageJr( R );
		System.out.println( "T:" );
		pp.print2DArray( T );
		System.out.println( "R:" );
		pp.print2DArray( R );
		int[][][] motionCompensation = new int[T.length][T[0].length][3];
		int macroBlkSize = 1;

		pp.MC( targetImg, "target", refImg, "reference", p, 0, residual,
				motionCompensation, macroBlkSize );
		System.out.println( T.length );

		pp.print3DArray( motionCompensation );
	}

	@Test
	public void test_padSize()
	{
		int[] paddedSize = new int[2];
		ImageJr img = new ImageJr( 4, 15 );
		int macroBlkSize = 3;
		img.paddedSize( macroBlkSize, paddedSize );
		for ( int i = 0; i < paddedSize.length; i++ ) {
			System.err.println( paddedSize[i] );
		}
		ImageJr padded = img.padImage( macroBlkSize );
		padded.displayImageDimension();
	}

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
