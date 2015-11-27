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

	public Task2( int n )
	{
		super();

	}

	public void getUserInput()
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

	public void replaceABlock( int[][] targetFrm, int[][] refFrm, int tx,
			int ty, int rx, int ry, int macroBlkSize )
	{
		for ( int i = 0; i < macroBlkSize; i++ ) {
			for ( int j = 0; j < macroBlkSize; j++ ) {
				targetFrm[ty + i][tx + j] = refFrm[ry + i][rx + j];
			}
		}
	}
	
	/** replacement with the block in the given df5th frame */
	public void replaceABlock( int[][] targetFrm, int tx,
			int ty, int rx, int ry, int macroBlkSize )
	{
		for ( int i = 0; i < macroBlkSize; i++ ) {
			for ( int j = 0; j < macroBlkSize; j++ ) {
				targetFrm[ty + i][tx + j] = refFrm[ry + i][rx + j];
			}
		}
	}

}
