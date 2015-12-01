import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class hw4_test {

	// @Test
	public void test_mc_with_logSearch() throws InterruptedException,
			IOException
	{
		// int x = 70, y = 70;
		Prep pp = new Prep();
		String targetName = "Walk_060.ppm";
		String refName = "Walk_057.ppm";
		// String targetName = "Walk_022.ppm";
		// String refName = "Walk_022.ppm";

		ImageJr t22 = new ImageJr( targetName );
		ImageJr r20 = new ImageJr( refName );
		ImageJr residual = new ImageJr();
		int[] paddedSize = new int[2];
		int macroBlkSize = 16;
		t22.paddedSize( macroBlkSize, paddedSize );
		System.out.println( "width:" + paddedSize[0] + "\t" + paddedSize[1] );
		int[][][] motionCompensation = new int[paddedSize[1]][paddedSize[0]][3];
		// ij.display_ks( "walk 57" );
		// Thread.sleep( 3000 );

		pp.MC_w_logSearch( t22, targetName, r20, refName, 12, 0, residual,
				motionCompensation, macroBlkSize );
		// pp.print3DArray( motionCompensation );
		// DEBUG
		System.out.println( "motion compensation[][]:" );
		for ( int i = 0; i < motionCompensation.length; i += macroBlkSize ) {
			for ( int j = 0; j < motionCompensation[0].length; j += macroBlkSize ) {
				System.out.print( "[ " + motionCompensation[i][j][0] + ", "
						+ motionCompensation[i][j][1] + ", "
						+ motionCompensation[i][j][2] + " ] " );
			}
			System.out.println();
		}

	}

	// @Test
	public void test_mc() throws InterruptedException, IOException
	{
		// int x = 70, y = 70;
		Prep pp = new Prep();
		String targetName = "Walk_060.ppm";
		String refName = "Walk_058.ppm";
		// String targetName = "Walk_022.ppm";
		// String refName = "Walk_022.ppm";

		ImageJr t22 = new ImageJr( targetName );
		ImageJr r20 = new ImageJr( refName );
		ImageJr residual = new ImageJr();
		int[] paddedSize = new int[2];
		int macroBlkSize = 16;
		t22.paddedSize( macroBlkSize, paddedSize );
		System.out.println( "width:" + paddedSize[0] + "\t" + paddedSize[1] );
		int[][][] motionCompensation = new int[paddedSize[1]][paddedSize[0]][3];
		// ij.display_ks( "walk 57" );
		// Thread.sleep( 3000 );

		pp.MC( t22, targetName, r20, refName, 12, 0, residual,
				motionCompensation, macroBlkSize );

	}

	// @Test
	public void test_sequentialSearchMSD_edges()
	{
		/*
		 * How do I test sequentialSearchMSD()? - create a dummy image 2d
		 * arrays, target[4][4] and ref[4][4] macroBlkSize = 2, - assign simple
		 * pixel values - get the best matcing ref block and printout min, x, y
		 */
		Prep pr = new Prep();
		int tx0 = 2, ty0 = 2, p = 2;
		int macroBlkSize = 2;

		int[][] target =
		{
		{ 1, 2, 3, 4 },
		{ 5, 6, 7, 8 },
		{ 9, 10, 2, 3 },
		{ 13, 14, 6, 10 } };

		int[][] ref =
		{
		{ 6, 7, 2, 3 },
		{ 10, 11, 6, 10 },
		{ 1, 2, 3, 1 },
		{ 1, 6, 7, 3 } };

		System.out.println( pr.sequentialSearchMSD( target, ref, tx0, ty0, p,
				macroBlkSize ).toString() );

	}

	// @Test
	public void test_sequentialSearchMAD_edges()
	{
		/*
		 * How do I test sequentialSearchMSD()? - create a dummy image 2d
		 * arrays, target[4][4] and ref[4][4] macroBlkSize = 2, - assign simple
		 * pixel values - get the best matcing ref block and printout min, x, y
		 */
		Prep pr = new Prep();
		int tx0 = 2, ty0 = 2, p = 1;
		int macroBlkSize = 2;

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

		System.out.println( pr.sequentialSearchMAD( target, ref, tx0, ty0, p,
				macroBlkSize ).toString() );

	}

	// @Test
	public void test_display_macroblock() throws InterruptedException
	{
		int x = 70, y = 70;
		Prep pp = new Prep();
		ImageJr ij = new ImageJr();
		ReferenceFrameBlock r = new ReferenceFrameBlock( x, y );
		pp.display_macroBlock(
				ij.imageJrTo2DArray( new ImageJr( "Ducky.ppm" ) ), r, 16, 10 );
	}

	// @Test

	public void test_imageJrToarray2D() throws InterruptedException
	{

		ImageJr img = new ImageJr( "Ducky.ppm" );
		int[][] imgArray = img.imageJrTo2DArray( img );
		ImageJr gray = (ImageJr) img.array2DtoImageJr( imgArray );
		gray.display( "gray" );
		Thread.sleep( 3000 );
	}

	// @Test
	public void test_sequentialSearchMAD()
	{
		/*
		 * How do I test sequentialSearchMSD()? - create a dummy image 2d
		 * arrays, target[4][4] and ref[4][4] macroBlkSize = 2, - assign simple
		 * pixel values - get the best matcing ref block and printout min, x, y
		 */
		Prep pr = new Prep();
		int tx0 = 1, ty0 = 1, p = 1;
		int macroBlkSize = 2;

		int[][] target =
		{
		{ 1, 3, 1, 1 },
		{ 4, 2, 3, 1 },
		{ 1, 4, 5, 1 },
		{ 1, 1, 1, 1 } };

		int[][] ref =
		{
		{ 1, 2, 2, 1 },
		{ 1, 4, 5, 1 },
		{ 3, 3, 2, 7 },
		{ 4, 5, 4, 6 } };

		System.out.println( pr.sequentialSearchMAD( target, ref, tx0, ty0, p,
				macroBlkSize ).toString() );

	}

	// @Test
	public void test_sequentialSearchMSD()
	{
		/*
		 * How do I test sequentialSearchMSD()? - create a dummy image 2d
		 * arrays, target[4][4] and ref[4][4] macroBlkSize = 2, - assign simple
		 * pixel values - get the best matcing ref block and printout min, x, y
		 */
		Prep pr = new Prep();
		int tx0 = 1, ty0 = 1, p = 1;
		int macroBlkSize = 1;

		int[][] target =
		{
		{ 1, 3, 1, 1 },
		{ 4, 2, 3, 1 },
		{ 1, 4, 5, 1 },
		{ 1, 1, 1, 1 } };

		int[][] ref =
		{
		{ 1, 2, 2, 1 },
		{ 1, 4, 5, 1 },
		{ 2, 3, 2, 7 },
		{ 4, 5, 4, 6 } };

		System.out.println( pr.sequentialSearchMSD( target, ref, tx0, ty0, p,
				macroBlkSize ).toString() );

	}

	// @Test
	public void test_findMinDiff()
	{
		Prep prep = new Prep();
		List<ReferenceFrameBlock> list = new LinkedList<ReferenceFrameBlock>();
		list.add( new ReferenceFrameBlock( 0, 0, 10 ) );
		list.add( new ReferenceFrameBlock( 1, 0, 20 ) );
		list.add( new ReferenceFrameBlock( 2, 0, 3 ) );
		list.add( new ReferenceFrameBlock( 0, 1, 4 ) );
		list.add( new ReferenceFrameBlock( 1, 1, 5 ) );
		list.add( new ReferenceFrameBlock( 2, 1, 6 ) );
		list.add( new ReferenceFrameBlock( 0, 2, 7 ) );
		list.add( new ReferenceFrameBlock( 1, 2, 8 ) );
		list.add( new ReferenceFrameBlock( 2, 2, 9 ) );

		ReferenceFrameBlock min = prep.findMinDiff( list );
		System.out.println( min );
	}

	// @Test
	public void testMAD_SAD()
	{
		Prep p = new Prep();
		int[][] A =
		{
		{ 1, 2, 3, 1 },
		{ 3, 2, 1, 2 },
		{ 1, 1, 2, 2 },
		{ 3, 2, 1, 2 } };
		int[][] B =
		{
		{ 1, 1, 3, 2 },
		{ 1, 1, 2, 1 },
		{ 3, 2, 1, 2 },
		{ 3, 2, 1, 1 } };

		// System.out.println( p.MAD( A, B, 1, 0, 1, 0, 2 ) );
		// System.out.println( p.MSD( A, B, 1, 0, 1, 0, 2 ) );
		// System.out.println( p.MAD( A, B, 0, 0, 1, 2, 2 ) );
		// System.out.println( p.MSD( A, B, 0, 0, 1, 2, 2 ) );
		System.out.println( p.meanAbsDiff( A, B, 0, 2, 0, 0, 2 ) );
		System.out.println( p.meanSquareDiff( A, B, 0, 2, 0, 0, 2 ) );
	}

	// @Test
	public void test_getMotionVector()
	{

		Prep p = new Prep();
		int[] motionVector = new int[2];
		p.getMotionVector( 5, 4, 3, 1, motionVector );
		for ( int i : motionVector ) {
			System.out.print( i + " " );
		}
	}

	// @Test
	public void testMSD()
	{
		Prep p = new Prep();
		int[][] A =
		{
		{ 1, 2, 3, 1 },
		{ 3, 2, 1, 2 },
		{ 1, 1, 2, 2 },
		{ 3, 2, 1, 2 } };
		int[][] B =
		{
		{ 1, 1, 3, 2 },
		{ 1, 1, 2, 1 },
		{ 3, 2, 1, 2 },
		{ 3, 2, 1, 1 } };

		System.out.println( "MSD = " + p.meanSquareDiff( A, B, 1, 0, 1, 0, 2 ) );
	}

	// @Test
	public void test()
	{
		System.out.println( Integer.MAX_VALUE );
		System.out.println( Integer.MIN_VALUE );

		System.out.println( Double.MAX_VALUE );
		System.out.println( Double.MIN_VALUE );

		System.out.println( Float.MAX_VALUE );
		System.out.println( Float.MIN_VALUE );
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

}
