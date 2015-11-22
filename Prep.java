public class Prep {
	static int macroBlkSize = 16;

	/*
	 * motion vectors(dx, dy)
	 */
	public void main( String[] args )
	{

	}

	/**
	 * Argumentse: 2 coordinates and one coordinate array of size two to which
	 * the the motion vector will be stored.
	 */
	public void getMotionVector( int x0, int y0, int x1, int y1,
			int[] coordinate )
	{

		coordinate[0] = x1 - x0;
		coordinate[1] = y1 - y0;
	}

	/**
	 * e(x,y) = cn+1(x,y) - cn(x-dx ,y-dy ) k: Search Size i1: reference frame
	 * i2: target frame
	 * */
	public void MC( int[][] i0, int[][] i1, int x, int y, int size, int k,
			int matchingCriteria, float residual, int[] motionVector )
	{
		int[] bestMatch = new int[2];
		float e = 0;
		int dx, dy;

		/*
		 * - use appropriate search criteria to get the best matching macroblock
		 * from the reference frame -
		 */

	}// note that macroblock sizse would affect the compression ratio

	public int[] logarithmicSearch( int[][] prev, int[][] target )
	{

		int[] bestMatch = new int[2];
		/*
		 * [x-1][y-1] [x][y-1] [x+1][y-1] [x-1][y] [x][y] [x+1][y] [x-1][y+1]
		 * [x][y+1] [x+1][y+1]
		 */

		return bestMatch;
	}

	/** Assume the feed is 16 x 16, only process one macro block */
	public int[] sequentialSearch( int[][] reference, int[][] target )
	{
		/*
		 * If the difference between the target block and the candidate block at
		 * the same position in the past frame is below some threshold then no
		 * motion
		 */
		int[] bestMatch = new int[2];
		/*
		 * [x-1][y-1] [x][y-1] [x+1][y-1] [x-1][y] [x][y] [x+1][y] [x-1][y+1]
		 * [x][y+1] [x+1][y+1]
		 */

		return bestMatch;
	}

	/**
	 * Assume the feed is 16 x 16, only process one macro block. Returns the
	 * best matching frame
	 */
	public int[] findBestMatchMacroBlock( int[][] reference, int[][] target,
			int ay0, int bx0, int by0 )
	{
		// x,y coordinate of the best maching block in reference frame
		int[] bestMatch = new int[2];

		for ( int p = 0; p < target.length; p++ ) {

		}
		// [x-1][y-1] [x][y-1] [x+1][y-1] [x-1][y] [x][y] [x+1][y] [x-1][y+1]
		// [x][y+1] [x+1][y+1]

		return bestMatch;
	}

	/** Mean Absolute Difference A: current frame, B: reference frame */
	public float MAD( int[][] A, int[][] B, int ax0, int ay0, int bx0, int by0,
			int macroBlkSizeIn )
	{
		float sum = 0;
		for ( int p = 0; p < macroBlkSizeIn; p++ ) {
			for ( int q = 0; q < macroBlkSizeIn; q++ ) {
				sum += Math.abs( A[ay0 + p][ax0 + q] - B[by0 + p][bx0 + q] );
			}
		}
		sum *= ( 1. / ( macroBlkSizeIn * macroBlkSizeIn ) );
		return sum;
	}

	/** Mean Square Difference A: current frame, B: reference frame */
	public float MSD( int[][] A, int[][] B, int ax0, int ay0, int bx0, int by0,
			int macroBlkSizeIn )
	{
		float sum = 0;
		for ( int p = 0; p < macroBlkSizeIn; p++ ) {
			for ( int q = 0; q < macroBlkSizeIn; q++ ) {
				sum += Math.pow( A[ay0 + p][ax0 + q] - B[by0 + p][bx0 + q], 2 );
			}
		}

		sum *= ( 1. / ( macroBlkSizeIn * macroBlkSizeIn ) );

		return sum;
	}

}
