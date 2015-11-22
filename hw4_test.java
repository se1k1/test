import static org.junit.Assert.*;

import org.junit.Test;

public class hw4_test {

	@Test
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

//		System.out.println( p.MAD( A, B, 1, 0, 1, 0, 2 ) );
//		System.out.println( p.MSD( A, B, 1, 0, 1, 0, 2 ) );
//		System.out.println( p.MAD( A, B, 0, 0, 1, 2, 2 ) );
//		System.out.println( p.MSD( A, B, 0, 0, 1, 2, 2 ) );
		System.out.println( p.MAD( A, B, 0, 2, 0, 0, 2 ) );
		System.out.println( p.MSD( A, B, 0, 2, 0, 0, 2 ) );
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

		System.out.println( "MSD = " + p.MSD( A, B, 1, 0, 1, 0, 2 ) );
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
