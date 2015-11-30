import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class hw4_task3_test {

	@Test
	public void test_getTop3()
	{
		Task3 t3 = new Task3();
		Prep pp = new Prep();
		int targetNum = 59;

		Similarity[] sims = new Similarity[3];
		sims = t3.getTop3SimilarFrames( 4, 16, targetNum );
	}

	// @Test
	public void test_deepcopy2dArray()
	{
		Task3 t3 = new Task3();
		Prep pp = new Prep();

		int[][] a = new int[randomNumGen( 5, 10 )][randomNumGen( 2, 4 )];
		autoFill2dArray( a, 0, 15 );
		int[][] b = t3.deepCopy2dArray( a );

		System.out.println( "a:" );
		pp.print2DArray( a );
		System.out.println( "b:" );
		pp.print2DArray( b );
	}

	public void autoFill2dArray( int[][] array, int min, int max )
	{
		for ( int i = 0; i < array.length; i++ ) {
			for ( int j = 0; j < array[0].length; j++ ) {
				array[i][j] = randomNumGen( min, max );

			}
		}
	}

	public void autoFill3dArray( int[][][] array, int minDiff, int maxDiff,
			int minMV, int maxMV )
	{

		for ( int i = 0; i < array.length; i++ ) {
			for ( int j = 0; j < array[0].length; j++ ) {
				for ( int j2 = 0; j2 < array[0][0].length; j2++ ) {
					if ( j2 > 0 ) {
						// array[i][j][j2] = randomNumGen( minMV, maxMV );
						array[i][j][j2] = 0;
					} else {
						array[i][j][j2] = randomNumGen( minDiff, maxDiff );
					}
				}
			}
		}
	}

	public int randomNumGen( int min, int max )
	{
		Random rd = new Random();
		return rd.nextInt( max ) + min;
	}

}
