import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class hw4_task2_test {
	@Test
	public void test_highLowPercentileValues()
	{
		Task2 t2 = new Task2();
		Prep pp = new Prep();
		int[][][] array = new int[5][5][3];
		autoFill3dArray( array, 1, 10, 0, 3 );
		pp.print3DArray( array );

		System.out.println(t2.getSpecifiedPercentileLowerMVValue( array, (float).1 ));
		System.out.println(t2.getSpecifiedPercentileLowerDifValue( array, (float).25 ));
	}

	// @Test
	public void test_sequentiallySearchStaticNeighborBlk()
	{
		Task2 t2 = new Task2();
		Prep pp = new Prep();
		int[][][] array = new int[5][5][3];
		autoFill3dArray( array, 1, 25, 0, 0 );
		pp.print3DArray( array );
		int macroBlkSize = 1, threshold = 3;
		int tx0 = 1, ty0 = 1, p = 10;
		int[] coordinate = t2.sequentialySearchStaticNeighborBlk( array, tx0,
				ty0, p, macroBlkSize, threshold );
		System.out.println( coordinate[0] );
		System.out.println( coordinate[1] );
	}

	// @Test
	public void test_getDist() throws InterruptedException
	{
		Task2 t2 = new Task2();
		System.out.println( t2.getDistance( 2, 1, 5, 4 ) );
	}

	// @Test
	public void test_replaceABlock() throws InterruptedException
	{
		Task2 t2 = new Task2();
		// t2.setTargetImg( new ImageJr( "Walk_059.ppm" ) );
		t2.setTargetImg( new ImageJr( "Ducky.ppm" ) );
		t2.setReferenceImg( new ImageJr( "Walk_057.ppm" ) );
		int macroBlkSize = 50;
		t2.replaceABlock( t2.getTargetImg(), t2.getTargetImg(), 0, 0, 50, 50,
				macroBlkSize );
		// t2.replaceABlock( t2.getTargetImg(), 0, 0, 50, 50, macroBlkSize );

	}

	// @Test
	public void test_getUserInput() throws InterruptedException
	{
		Task2 t2 = new Task2();
		t2.getUserInput();

	}

	public void autoFill3dArray( int[][][] array, int minDiff, int maxDiff,
			int minMV, int maxMV )
	{

		for ( int i = 0; i < array.length; i++ ) {
			for ( int j = 0; j < array[0].length; j++ ) {
				for ( int j2 = 0; j2 < array[0][0].length; j2++ ) {
					if ( j2 > 0 ) {
						array[i][j][j2] = randomNumGen( minMV, maxMV );
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
