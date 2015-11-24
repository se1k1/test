import java.util.ArrayList;
import java.util.List;

public class Prep {
	private int macroBlkSize = 16;
	static final int MAD = 0;
	static final int MSD = 1;
	static final int SEQUENTIAL_SEARCH = 10;
	static final int LOGARITHMIC_SEARCH = 20;

	/*
	 * motion vectors(dx, dy)
	 */
	public void main( String[] args )
	{

	}

	/**
	 * Arguments: 2 coordinates and one coordinate array of size two to which
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
			int matchingCriteria, int[][] residual, int[] motionVector )
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
	public ReferenceFrameBlock sequentialSearchMAD( int[][] target,
			int[][] reference, int tx0, int ty0, int p )
	{
		/*
		 * If the difference between the target block and the candidate block at
		 * the same position in the past frame is below some threshold then no
		 * motion
		 */
		float threshold = (float) 0.001;
		if ( meanAbsDiff( target, reference, tx0, ty0, tx0, ty0, macroBlkSize ) < threshold ) {
			return new ReferenceFrameBlock( tx0, ty0, meanAbsDiff( target,
					reference, tx0, ty0, tx0, ty0, macroBlkSize ) );
		}

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		for ( int i = 1; i <= p; i++ ) {

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0 - p,
							macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0 - p, macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 - p,
							macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p, meanAbsDiff(
					target, reference, tx0, ty0, tx0 - p, ty0, macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0, macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p, meanAbsDiff(
					target, reference, tx0, ty0, tx0 + p, ty0, macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0 + p,
							macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0 + p, macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 + p,
							macroBlkSize ) ) );

		}

		/*
		 * [x-1][y-1] [x][y-1] [x+1][y-1] [x-1][y] [x][y] [x+1][y] [x-1][y+1]
		 * [x][y+1] [x+1][y+1]
		 */

		return findMinDiff( diffs );
	}

	/** Search for best maching block using sequesntial search and MAD */
	public ReferenceFrameBlock sequentialSearchMAD( int[][] target,
			int[][] reference, int tx0, int ty0, int p, int macroBlkSize )
	{/*
	 * If the difference between the target block and the candidate block at the
	 * same position in the past frame is below some threshold then no motion
	 */
		/*
		 * If the difference between the target block and the candidate block at
		 * the same position in the past frame is below some threshold then no
		 * motion
		 */
		float threshold = (float) 0.001;
		if ( meanAbsDiff( target, reference, tx0, ty0, tx0, ty0, macroBlkSize ) < threshold ) {
			return new ReferenceFrameBlock( tx0, ty0, meanSquareDiff( target,
					reference, tx0, ty0, tx0, ty0, macroBlkSize ) );
		}

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		for ( int i = 1; i <= p; i++ ) {

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0 - p,
							macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0 - p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0 - p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 - p,
							macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0, meanAbsDiff(
					target, reference, tx0, ty0, tx0 - p, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0, meanAbsDiff( target,
					reference, tx0, ty0, tx0, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0, meanAbsDiff(
					target, reference, tx0, ty0, tx0 + p, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 + p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0
							+ p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0 + p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0 + p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 + p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 + p,
							macroBlkSize ) ) );
		}
		return findMinDiff( diffs );
	}

	/** DEBUG Assume the feed is 16 x 16, only process one macro block */
	public ReferenceFrameBlock sequentialSearchMSD( int[][] target,
			int[][] reference, int tx0, int ty0, int p )
	{
		/*
		 * If the difference between the target block and the candidate block at
		 * the same position in the past frame is below some threshold then no
		 * motion
		 */

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		for ( int i = 1; i <= p; i++ ) {

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0
							- p, macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0, ty0 - p,
							macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 + p, ty0
							- p, macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0,
							macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0, ty0,
							macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 + p, ty0,
							macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0
							+ p, macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0, ty0 + p,
							macroBlkSize ) ) );
			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 + p, ty0
							+ p, macroBlkSize ) ) );

		}

		/*
		 * [x-1][y-1] [x][y-1] [x+1][y-1] [x-1][y] [x][y] [x+1][y] [x-1][y+1]
		 * [x][y+1] [x+1][y+1]
		 */

		return findMinDiff( diffs );
	}

	/** Search for best maching block using sequesntial search and MSD */
	public ReferenceFrameBlock sequentialSearchMSD( int[][] target,
			int[][] reference, int tx0, int ty0, int p, int macroBlkSize )
	{
		/*
		 * If the difference between the target block and the candidate block at
		 * the same position in the past frame is below some threshold then no
		 * motion
		 */
		float threshold = (float) 0.001;
		if ( meanAbsDiff( target, reference, tx0, ty0, tx0, ty0, macroBlkSize ) < threshold ) {
			return new ReferenceFrameBlock( tx0, ty0, meanSquareDiff( target,
					reference, tx0, ty0, tx0, ty0, macroBlkSize ) );
		}

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		for ( int i = 1; i <= p; i++ ) {

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0
							- p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0 - p, meanSquareDiff(
					target, reference, tx0, ty0, tx0, ty0 - p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 - p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 + p, ty0
							- p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0, meanSquareDiff(
					target, reference, tx0, ty0, tx0 - p, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0, meanSquareDiff(
					target, reference, tx0, ty0, tx0, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0, meanSquareDiff(
					target, reference, tx0, ty0, tx0 + p, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 + p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0
							+ p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0 + p, meanSquareDiff(
					target, reference, tx0, ty0, tx0, ty0 + p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 + p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 + p, ty0
							+ p, macroBlkSize ) ) );

		}

		/*
		 * [x-1][y-1] [x][y-1] [x+1][y-1] [x-1][y] [x][y] [x+1][y] [x-1][y+1]
		 * [x][y+1] [x+1][y+1]
		 */

		return findMinDiff( diffs );
	}

	public void print2DArray( int[][] a )
	{
		for ( int i = 0; i < a.length; i++ ) {
			for ( int j = 0; j < a[i].length; j++ ) {
				System.out.print( a[i][j] + " " );
			}
			System.out.println();
		}
	}

	/** DEBUG */
	public ReferenceFrameBlock logarithmicSearchMAD( int[][] target,
			int[][] reference, int tx0, int ty0, int p, int macroBlkSize )
	{/*
	 * If the difference between the target block and the candidate block at the
	 * same position in the past frame is below some threshold then no motion
	 */

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		while ( p >= 0 ) {

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0 - p,
							macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0 - p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0 - p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 - p,
							macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0, meanAbsDiff(
					target, reference, tx0, ty0, tx0 - p, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0, meanAbsDiff( target,
					reference, tx0, ty0, tx0, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0, meanAbsDiff(
					target, reference, tx0, ty0, tx0 + p, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 + p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0
							+ p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0 + p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0 + p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 + p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 + p,
							macroBlkSize ) ) );
			p /= 2;
			ReferenceFrameBlock min = findMinDiff( diffs );
			tx0 = min.getxTopLeft();
			ty0 = min.getyTopLeft();
		}
		return findMinDiff( diffs );
	}

	/** DEBUT build a 2d square array */
	public void square2dArray( int p )
	{
		int[][] array = new int[2 * p + 1][2 * p + 1];
		// center = array[p][p]
		array[p][p] = 0;
		for ( int i = 1; i <= p; i++ ) {
			array[p - i][p - i] = i;
			array[p - i][p] = i;
			array[p - i][p + i] = i;
			array[p][p - i] = i;
			array[p][p + i] = i;
			array[p + i][p - i] = i;
			array[p + i][p] = i;
			array[p + i][p + i] = i;
		}
		print2DArray( array );
	}

	/** DEBUG simplified & test version of log search */
	public ReferenceFrameBlock logSearchJr( int[][] target, int[][] reference,
			int tx0, int ty0, int p, int macroBlkSize )
	{/*
	 * If the difference between the target block and the candidate block at the
	 * same position in the past frame is below some threshold then no motion
	 */

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		while ( p >= 0 ) {

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0 - p,
							macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0 - p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0 - p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 - p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 - p,
							macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0, meanAbsDiff(
					target, reference, tx0, ty0, tx0 - p, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0, meanAbsDiff( target,
					reference, tx0, ty0, tx0, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0, meanAbsDiff(
					target, reference, tx0, ty0, tx0 + p, ty0, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 + p,
					meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0
							+ p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0, ty0 + p, meanAbsDiff(
					target, reference, tx0, ty0, tx0, ty0 + p, macroBlkSize ) ) );

			diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 + p,
					meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 + p,
							macroBlkSize ) ) );
			p /= 2;
			ReferenceFrameBlock min = findMinDiff( diffs );
			tx0 = min.getxTopLeft();
			ty0 = min.getyTopLeft();
		}
		return findMinDiff( diffs );
	}

	/**
	 * Assume the feed is 16 x 16, only process one macro block. Returns the
	 * best matching frame
	 */
	public ReferenceFrameBlock findBestMatchMacroBlock( int[][] reference,
			int[][] target, int tx0, int ty0, int macroBlkSize, int searchSize,
			int searchCriteria )
	{

		/*
		 * searchCriteria: 1: sequential search MAD 2: sequential search MSD 3:
		 * logarithmic search MAD 4: logarithmic search MSD
		 */
		// x,y coordinate of the best maching block in reference frame
		ReferenceFrameBlock bestMatch = sequentialSearchMAD( target, reference,
				tx0, ty0, searchSize );

		return bestMatch;
	}

	public int[][] toGray( Image img )
	{
		int gray = 0;
		int[] rgb = new int[3];
		int[][] imgArray = new int[img.getH()][img.getW()];
		for ( int i = 0; i < img.getH(); i++ ) {
			for ( int j = 0; j < img.getW(); j++ ) {
				img.getPixel( j, i, rgb );
				gray = (int) Math.round( 0.299 * rgb[0] + 0.587 * rgb[1]
						+ 0.114 * rgb[2] );
				imgArray[i][j] = gray;
			}
		}
		return imgArray;
	}

	/** Mean Absolute Difference A: current frame, B: reference frame */
	public float meanAbsDiff( int[][] target, int[][] ref, int tx0, int ty0,
			int rx0, int ry0, int macroBlkSizeIn )
	{
		float sum = 0;
		for ( int p = 0; p < macroBlkSizeIn; p++ ) {
			for ( int q = 0; q < macroBlkSizeIn; q++ ) {
				sum += Math.abs( target[ty0 + p][tx0 + q]
						- ref[ry0 + p][rx0 + q] );
			}
		}
		sum *= ( 1. / ( macroBlkSizeIn * macroBlkSizeIn ) );
		return sum;
	}

	/** Mean Square Difference A: current frame, B: reference frame */
	public float meanSquareDiff( int[][] target, int[][] ref, int tx0, int ty0,
			int rx0, int ry0, int macroBlkSizeIn )
	{
		float sum = 0;
		for ( int p = 0; p < macroBlkSizeIn; p++ ) {
			for ( int q = 0; q < macroBlkSizeIn; q++ ) {
				sum += Math.pow( target[ty0 + p][tx0 + q]
						- ref[ry0 + p][rx0 + q], 2 );
			}
		}

		sum *= ( 1. / ( macroBlkSizeIn * macroBlkSizeIn ) );

		return sum;
	}

	public ReferenceFrameBlock findMinDiff( List<ReferenceFrameBlock> diffs )
	{
		ReferenceFrameBlock min = new ReferenceFrameBlock();
		min.setDiffValue( Integer.MAX_VALUE );

		for ( ReferenceFrameBlock i : diffs ) {
			if ( min.getDiffValue() > i.getDiffValue() ) {
				min = i;
			}
		}
		return min;
	}

	public int getMacroBlkSize()
	{
		return macroBlkSize;
	}

	public void setMacroBlkSize( int macroBlkSize )
	{
		this.macroBlkSize = macroBlkSize;
	}

	
}
