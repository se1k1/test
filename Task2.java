import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Task2 extends Prep {
	/*
	 * Use IDB and implement the following steps to achieve this task: - Select
	 * the target image. o For the target image, receive from the user a frame
	 * number n between 19 and 179. 19~179 frames in the data set include moving
	 * people. - Select the reference image. o Given the input n, use (n-2)th as
	 * the reference image. Identify dynamic blocks in the target image by
	 * performing the block-based motion estimation and checking motion vector
	 * as you have done in the task1. - Remove moving object from the target
	 * image. Apply two different approaches and produce and display two
	 * different result images: 1. For each dynamic block d, find the closest
	 * static block s in the nth frame and replace d with s. After processing
	 * all dynamic blocks, display the result. 2. The 5th frame of test data set
	 * does not include any moving objects. Utilize the 5th frame to get static
	 * block s. For each dynamic block d, find the static block s from 5th frame
	 * and replace d with s. After processing all dynamic blocks, display the
	 * result.
	 * 
	 * See the sample outputs for the result format.
	 */
	ImageJr targetImg;
	ImageJr referenceImg;

	public Task2()
	{
		super();
	}

	public void getUserInput() throws InterruptedException
	{
		Scanner sc = new Scanner( System.in );
		System.out
				.print( "Please choose  a frame number between 19 and 179  [19-179]:" );
		int n = sc.nextInt();

		String targetName = "Walk_0" + n + ".ppm";
		String refName = "Walk_0" + ( n - 2 ) + ".ppm";

		targetImg = new ImageJr( targetName );
		referenceImg = new ImageJr( refName );

	}

	// scan through the img and replace dynamic blocks with the specified
	// corresponding block
	public void identifyDinamicBlk()
	{

	}

	public int s( int[][][] array, float percentile )
	{
		List<Integer> list = new LinkedList<Integer>();
		int sum = 0;

		for ( int y = 0; y < array.length; y++ ) {
			for ( int x = 0; x < array[0].length; x++ ) {
				list.add( array[y][x][0] );
			}
		}
		Collections.sort( list );
		int stopIdx = (int) Math.round( list.size() * percentile * 1. );
		for ( int i = 0; i < stopIdx; i++ ) {
			sum += list.get( i );
		}
		return sum / ( stopIdx + 1 );
	}

	public int getSpecifiedPercentileLowerMVValue( int[][][] array,
			float percentile )
	{
		List<Integer> list = new LinkedList<Integer>();
		int sum = 0;

		for ( int y = 0; y < array.length; y++ ) {
			for ( int x = 0; x < array[0].length; x++ ) {
				list.add( (int) Math
						.round( ( array[y][x][1] + array[y][x][2] ) / 2. ) );
			}
		}
		Collections.sort( list );
		int stopIdx = (int) Math.round( list.size() * percentile * 1. );
		for ( int i = 0; i < stopIdx; i++ ) {
			sum += list.get( i );
		}

		return sum / ( stopIdx + 1 );
	}

	public int getSpecifiedPercentileLowerDifValue( int[][][] array,
			float percentile )
	{
		List<Integer> list = new LinkedList<Integer>();
		int sum = 0;

		for ( int y = 0; y < array.length; y++ ) {
			for ( int x = 0; x < array[0].length; x++ ) {
				list.add( array[y][x][0] );
			}
		}
		Collections.sort( list );
		int stopIdx = (int) Math.round( list.size() * percentile * 1. );
		for ( int i = 0; i < stopIdx; i++ ) {
			sum += list.get( i );
		}

		return sum / ( stopIdx + 1 );
	}

	public int[] sequentialySearchStaticNeighborBlk(
			int[][][] motionCompensation, int tx0, int ty0, int p,
			int macroBlkSize, int threshold )
	{

		int[] coordinate = new int[2];
		for ( int i = 1; i <= p; i++ ) {

			if ( tx0 - i > -1 && tx0 - i < motionCompensation[0].length
					&& ty0 - i > -1 && ty0 - i < motionCompensation.length
					&& motionCompensation[ty0 - i][tx0 - i][1] == 0
					&& motionCompensation[ty0 - i][tx0 - i][2] == 0
					&& motionCompensation[ty0 - i][tx0 - i][0] < threshold ) {
				coordinate[0] = tx0 - i;
				coordinate[1] = ty0 - i;
				return coordinate;
			}
			if ( ty0 - i > -1 && ty0 - i < motionCompensation.length
					&& motionCompensation[ty0 - i][tx0][1] == 0
					&& motionCompensation[ty0 - i][tx0][2] == 0
					&& motionCompensation[ty0 - i][tx0][0] < threshold ) {
				coordinate[0] = tx0;
				coordinate[1] = ty0 - i;
				return coordinate;

			}
			if ( tx0 + i > -1 && tx0 + i < motionCompensation[0].length
					&& ty0 - i > -1 && ty0 - i < motionCompensation.length
					&& motionCompensation[ty0 - i][tx0 + i][1] == 0
					&& motionCompensation[ty0 - i][tx0 + i][2] == 0
					&& motionCompensation[ty0 - i][tx0 + i][0] < threshold ) {
				coordinate[0] = tx0 + i;
				coordinate[1] = ty0 - i;
				return coordinate;

			}
			if ( tx0 - i > -1 && tx0 - i < motionCompensation.length
					&& motionCompensation[ty0][tx0 - i][1] == 0
					&& motionCompensation[ty0][tx0 - i][2] == 0
					&& motionCompensation[ty0][tx0 - i][0] < threshold ) {
				coordinate[0] = tx0 - i;
				coordinate[1] = ty0;
				return coordinate;
			}

			if ( tx0 + i > -1 && tx0 + i < motionCompensation[0].length
					&& motionCompensation[ty0][tx0 + i][1] == 0
					&& motionCompensation[ty0][tx0 + i][2] == 0
					&& motionCompensation[ty0][tx0 + i][0] < threshold ) {
				coordinate[0] = tx0 + i;
				coordinate[1] = ty0;
				return coordinate;
			}
			if ( tx0 - i > -1 && tx0 - i < motionCompensation[0].length
					&& ty0 + i > -1 && ty0 + i < motionCompensation.length
					&& motionCompensation[ty0 + i][tx0 - i][1] == 0
					&& motionCompensation[ty0 + i][tx0 - i][2] == 0
					&& motionCompensation[ty0 + i][tx0 - i][0] < threshold ) {
				coordinate[0] = tx0 - i;
				coordinate[1] = ty0 + i;
				return coordinate;
			}

			if ( ty0 + i > -1 && ty0 + i < motionCompensation.length
					&& motionCompensation[ty0 + i][tx0][1] == 0
					&& motionCompensation[ty0 + i][tx0][2] == 0
					&& motionCompensation[ty0 + i][tx0][0] < threshold ) {
				coordinate[0] = tx0;
				coordinate[1] = ty0 + i;
				return coordinate;
			}
			if ( tx0 + i > -1 && tx0 + i < motionCompensation[0].length
					&& ty0 + i > -1 && ty0 + i < motionCompensation.length
					&& motionCompensation[ty0 + i][tx0 + i][1] == 0
					&& motionCompensation[ty0 + i][tx0 + i][2] == 0
					&& motionCompensation[ty0 + i][tx0 + i][0] < threshold ) {
				coordinate[0] = tx0 + i;
				coordinate[1] = ty0 + i;
				return coordinate;
			}
		}
		// debug
		// System.out.print( "[@x,y=" + tx0 + "," + ty0 + "]:" );
		// System.out.println( diffs );
		return coordinate;
	}

	public void removeMovingObj01( ImageJr targetImg, ImageJr refImg,
			String imgNameT, String imgNameRef, int[][][] MC, int macroBlkSize,
			int p ) throws InterruptedException, IOException
	{
		MC( targetImg, imgNameT, targetImg, imgNameRef, p, 0, new ImageJr(),
				MC, macroBlkSize );
		// int[][] movingObjCoordinates =
		// sequentialySearchStaticNeighborBlk( MC, tx0, ty0, p, macroBlkSize,
		// threshold )

		// scan through macro blks in MC, and find moving objects
	}

	public void removeMovingObj02()
	{
	}

	public void replaceABlock( ImageJr targetImg, ImageJr refImg, int tx,
			int ty, int rx, int ry, int macroBlkSize )
			throws InterruptedException
	{
		int[] rgb = new int[3];
		for ( int i = 0; i < macroBlkSize; i++ ) {
			for ( int j = 0; j < macroBlkSize; j++ ) {

				refImg.getPixel( rx + j, ry + i, rgb );
				targetImg.setPixel( tx + j, ty + i, rgb );
			}
		}
		targetImg.display( "Altered image" );
		Thread.sleep( 5000 );
	}

	/**
	 * replacement with the block in the given 5th frame
	 * 
	 * @throws InterruptedException
	 */
	public void replaceABlock( ImageJr targetImg, int tx, int ty, int rx,
			int ry, int macroBlkSize ) throws InterruptedException
	{

		ImageJr fifthFrmImg = new ImageJr( "Walk_005.ppm" );
		int[] rgb = new int[3];

		for ( int i = 0; i < macroBlkSize; i++ ) {
			for ( int j = 0; j < macroBlkSize; j++ ) {
				fifthFrmImg.getPixel( rx + j, ry + i, rgb );
				targetImg.setPixel( tx + j, ty + i, rgb );
			}
		}

		targetImg.display( "Altered image" );
		Thread.sleep( 5000 );
	}

	public float getDistance( int x0, int y0, int x1, int y1 )
	{
		return (float) Math.sqrt( Math.pow( x1 - x0, 2 )
				+ Math.pow( y1 - y0, 2 ) );
	}

	/* getters and setters */
	public ImageJr getTargetImg()
	{
		return targetImg;
	}

	public void setTargetImg( ImageJr targetImg )
	{
		this.targetImg = targetImg;
	}

	public ImageJr getReferenceImg()
	{
		return referenceImg;
	}

	public void setReferenceImg( ImageJr referenceImg )
	{
		this.referenceImg = referenceImg;
	}

}
