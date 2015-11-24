import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class hw4_test {
	
	@Test
	public void test_imageJrToarray2D() throws InterruptedException
	{

		ImageJr img = new ImageJr( "Ducky.ppm" );
		int[][] imgArray = img.imageJrToarray2D( img );
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

}
