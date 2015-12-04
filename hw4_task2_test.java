import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

public class hw4_task2_test {
	//@Test
	public void test_task2_2b() throws InterruptedException, IOException
	{
		Task2 t2 = new Task2();
		Prep pp = new Prep();
//		String imgNameT = "IDB\\Walk_060.ppm";
//		String imgNameRef = "IDB\\Walk_058.ppm";
		 String imgNameT = "IDB\\Walk_042.ppm";
		 String imgNameRef = "IDB\\Walk_040.ppm";

		ImageJr targetImg = new ImageJr( imgNameT );
		ImageJr referenceImg = new ImageJr( imgNameRef );
		int[] paddedSize = new int[2];
		int macroBlkSize = 16, p = 12;
		targetImg.paddedSize( macroBlkSize, paddedSize );
		int[][][] MC = new int[paddedSize[1]][paddedSize[0]][3];

		// pp.MC( targetImg, imgNameT, referenceImg, imgNameRef, p, 0,
		// residualImg, MC, macroBlkSize, thresholds );

		t2.removeMovingObj02( "IDB/",targetImg, referenceImg, imgNameT, imgNameRef,
				macroBlkSize, p );
		// pp.print3DArray( MC );
		System.out
				.println( "motion compensation[][](outside the MC() method):" );
		for ( int i = 0; i < MC.length; i += macroBlkSize ) {
			for ( int j = 0; j < MC[0].length; j += macroBlkSize ) {
				System.out.print( "[ " + MC[i][j][0] + ", " + MC[i][j][1]
						+ ", " + MC[i][j][2] + " (" + j + "," + i + ")] " );
			}
			System.out.println();
		}

	}
	// @Test
	public void test_task2_2() throws InterruptedException, IOException
	{
		Task2 t2 = new Task2();
		String imgNameT = "Walk_060.ppm";
		String imgNameRef = "Walk_057.ppm";
		// String targetName = "Walk_022.ppm";
		// String refName = "Walk_022.ppm";

		ImageJr targetImg = new ImageJr( imgNameT );
		ImageJr referenceImg = new ImageJr( imgNameRef );
		int[] paddedSize = new int[2];
		int macroBlkSize = 16, p = 12;
		targetImg.paddedSize( macroBlkSize, paddedSize );
		int[][][] MC = new int[paddedSize[1]][paddedSize[0]][3];

		// pp.MC( targetImg, imgNameT, referenceImg, imgNameRef, p, 0,
		// residualImg, MC, macroBlkSize, thresholds );

		//t2.removeMovingObj02( targetImg, referenceImg, imgNameT, imgNameRef,
		//s		MC, macroBlkSize, p );
		// pp.print3DArray( MC );
		System.out
				.println( "motion compensation[][](outside the MC() method):" );
		for ( int i = 0; i < MC.length; i += macroBlkSize ) {
			for ( int j = 0; j < MC[0].length; j += macroBlkSize ) {
				System.out.print( "[ " + MC[i][j][0] + ", " + MC[i][j][1]
						+ ", " + MC[i][j][2] + " (" + j + "," + i + ")] " );
			}
			System.out.println();
		}

	}

	//@Test
	public void test_task2_1() throws InterruptedException, IOException
	{
		Task2 t2 = new Task2();
		Prep pp = new Prep();
		String imgNameT = "IDB\\Walk_060.ppm";
		String imgNameRef = "IDB\\Walk_058.ppm";
		// String targetName = "Walk_022.ppm";
		// String refName = "Walk_022.ppm";

		ImageJr targetImg = new ImageJr( imgNameT );
		ImageJr referenceImg = new ImageJr( imgNameRef );
		ImageJr residualImg = new ImageJr();
		int[] paddedSize = new int[2], thresholds = new int[3];
		int macroBlkSize = 16, p = 12;
		targetImg.paddedSize( macroBlkSize, paddedSize );
		int[][][] MC = new int[paddedSize[1]][paddedSize[0]][3];

		// pp.MC( targetImg, imgNameT, referenceImg, imgNameRef, p, 0,
		// residualImg, MC, macroBlkSize, thresholds );

		t2.removeMovingObj01( targetImg, referenceImg, imgNameT, imgNameRef,
				macroBlkSize, p );
		// pp.print3DArray( MC );
		System.out
				.println( "motion compensation[][](outside the MC() method):" );
		for ( int i = 0; i < MC.length; i += macroBlkSize ) {
			for ( int j = 0; j < MC[0].length; j += macroBlkSize ) {
				System.out.print( "[ " + MC[i][j][0] + ", " + MC[i][j][1]
						+ ", " + MC[i][j][2] + " (" + j + "," + i + ")] " );
			}
			System.out.println();
		}

	}

	// @Test
	public void test_obtainingThresholds()
	{
		Task2 t2 = new Task2();
		Prep pp = new Prep();
		int[][][] array = new int[10][10][3];
		autoFill3dArray( array, 5, 10, 0, 3 );
		pp.print3DArrayI( array );
		int macroBlkSize = 1;

		// test_highLowPercentileValues()
		// System.out.println(t2.getSpecifiedPercentileLowerMVValue( array,
		// (float).1 ));
		// System.out.println(t2.getSpecifiedPercentileLowerDifValue( array,
		// (float).25 ));

		int[] values = t2.getThresholds( array, (float) .9, (float) .1,
				(float) .3, macroBlkSize );
		System.out.println( values[0] );
		System.out.println( values[1] );
		System.out.println( values[2] );
	}

	// @Test
	public void test_sequentiallySearchStaticNeighborBlk()
	{
		Task2 t2 = new Task2();
		Prep pp = new Prep();
		int[][][] array = new int[5][5][3];
		autoFill3dArray( array, 1, 25, 0, 0 );
		pp.print3DArrayI( array );
		int macroBlkSize = 1, threshold = 3;
		int tx0 = 1, ty0 = 1, p = 10;
		float[] coordinate = t2.sequentiallySearchStaticNeighborBlk( array,
				tx0, ty0, p, macroBlkSize, threshold );
		System.out.println( coordinate[0] );// x
		System.out.println( coordinate[1] );// y
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
