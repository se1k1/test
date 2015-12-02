import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class hw4_task3_test {

	// @Test
	public void test_readFilesFromADirectory()
	{
		Task3 t3 = new Task3();
		File directory = new File( "IDB" );
		File[] imageData = directory.listFiles();
		List<File> images = new ArrayList<File>();
		for ( File file : imageData ) {
			images.add( file );
		}
	}

	// @Test
	public void test_getIndexOfTargetImage() throws InterruptedException,
			IOException
	{
		Task3 t3 = new Task3();
		String directoryName = "IDB";
		String imgNameT = "Walk_005.ppm";
		File directory = new File( directoryName );
		File[] imageData = directory.listFiles();
		System.out.println( t3.getIndexOfTargetImage( imgNameT, imageData ) );

		// getIndexOfTargetImage
	}

	@Test
	public void test_getTop3() throws InterruptedException, IOException
	{
		Task3 t3 = new Task3();
//		t3.getTop3SimilarFrames( 4, 16 );
		t3.getTop3_test(  4, 16 );

		}

	// @Test
	public void test_getTop3_sml() throws InterruptedException, IOException
	{
		Task3 t3 = new Task3();
		int targetNum = 59;

		t3.getTop3SimilarFrames( 4, 16 );

		// for ( Similarity similarity : sims ) {
		// System.out.println( similarity.toString() );
		// }
	}

	// @Test
	public void test_measureSimilarities() throws InterruptedException,
			IOException
	{
		Prep pp = new Prep();
		String imgNameT = "Walk_060.ppm";
		String imgNameRef = "Walk_057.ppm";
		// String targetName = "Walk_022.ppm";
		// String refName = "Walk_022.ppm";
		int macroBlkSize = 16;
		ImageJr targetImg = new ImageJr( imgNameT );
		ImageJr referenceImg = new ImageJr( imgNameRef );
		int[] paddedSize = new int[2];
		targetImg.paddedSize( macroBlkSize, paddedSize );
		ImageJr residual = new ImageJr( paddedSize[0], paddedSize[1] );
		int[][][] motionCompensation = new int[paddedSize[1]][paddedSize[0]][3];

		Task3 t3 = new Task3();
		int targetNum = 59, frameIdxR = 10, p = 4;

		Similarity[] sims = new Similarity[3];
		pp.MC( targetImg, imgNameT, referenceImg, imgNameRef, p, 0, residual,
				motionCompensation, macroBlkSize );
		System.out.println( t3.measureSimilarity( motionCompensation, p,
				frameIdxR ) );
		// for ( Similarity similarity : sims ) {
		// similarity.toString() );
		// }
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
