import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Prep {
	// private int macroBlkSize = 16;
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
	 * 
	 * @throws InterruptedException
	 * */
	public void MC_per_macroBlk_debug( ImageJr targetImg, String imgnamgeT,
			ImageJr referenceImg, String imgnamgeRef, int p,
			int matchingCriteria, ImageJr residualImg,
			int[][][] motionCompensation, int macroBlkSize )
			throws InterruptedException
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		/* [0] = error value, [1] = motion vector x, [2] = motion vector y */
		motionCompensation = new int[padTarget.getH()][padTarget.getW()][3];
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();

		for ( int y = 0; y < targetFrm.length; y++ ) {
			for ( int x = 0; x < targetFrm[y].length; x++ ) {

				if ( x % macroBlkSize == 0 && y % macroBlkSize == 0 ) {

					// find predicted block
					bestMatch = sequentialSearchMSD( targetFrm, referenceFrm,
							x, y, p, macroBlkSize );

					// store motion vector x
					motionCompensation[y][x][1] = x - bestMatch.getxTopLeft();

					// store motion vector y
					motionCompensation[y][x][2] = y - bestMatch.getyTopLeft();
				}

				// store error_pixel_value
				if ( ( bestMatch.getxTopLeft() + x % macroBlkSize ) < referenceFrm[0].length
						&& ( bestMatch.getxTopLeft() + x % macroBlkSize ) < referenceFrm.length ) {
					motionCompensation[y][x][0] = Math.abs( targetFrm[y][x]
							- referenceFrm[bestMatch.getxTopLeft() + y
									% macroBlkSize][bestMatch.getxTopLeft() + x
									% macroBlkSize] );
				}
				// DEBUG
				// System.out.println( "MC(): x,y=" + x + "," + y );
				// System.out.println( "Best Match: " + bestMatch );

				errorImg.setPixel( x, y, motionCompensation[y][x][0] );
			}
		}

		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
				targetImg.getH() );
		// avgPixValue = getAvgPixValue( errorDepadded );
		ImageJr mappedError = mapResidual( errorDepadded );
		/*
		 * - use appropriate search criteria to get the best matching macroblock
		 * from the reference frame -
		 */

		System.out.println( "# Name: Kae Sawada" + "\n# Target image name: "
				+ imgnamgeT + "\n# Reference image name: " + imgnamgeRef
				+ "\n# Number of target macro blocks: " + targetFrm[0].length
				/ macroBlkSize + " x " + targetFrm.length / macroBlkSize
				+ " (image size is " + targetImg.getW() + " x "
				+ targetImg.getH() + ")" );

		// DEBUG
		for ( int i = 0; i < motionCompensation.length; i += macroBlkSize ) {
			for ( int j = 0; j < motionCompensation[0].length; j += macroBlkSize ) {
				System.out.print( "[ " + motionCompensation[i][j][1] + ", "
						+ motionCompensation[i][j][2] + " ] " );
			}
			System.out.println();
		}

		/*
		 * [ 0, 0] [ 1, 1] [ 10, 10] [ 0, 0] [ 0, 1] [ 1, 1] [ 0, 0] [ 0, -1] [
		 * -1, 0] [ 1, 1] [ 10, 0] [ -4, 1]
		 */
		// DEBUG
		for ( int i = 0; i < motionCompensation.length; i++ ) {
			for ( int j = 0; j < motionCompensation[0].length; j++ ) {
				System.out.print( motionCompensation[i][j][0] + " "
						+ motionCompensation[i][j][1] + ", "
						+ motionCompensation[i][j][2] + " | " );
			}
			System.out.println();
		}
		mappedError.display( "error image" );
		Thread.sleep( 5000 );
	}// note that macroblock sizse would affect the compression ratio

	public void MC( ImageJr targetImg, String imgnamgeT, ImageJr referenceImg,
			String imgnamgeRef, int p, int matchingCriteria,
			ImageJr residualImg, int[][][] motionCompensation, int macroBlkSize )
			throws InterruptedException
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		/* [0] = error value, [1] = motion vector x, [2] = motion vector y */
		// motionCompensation = new int[padTarget.getH()][padTarget.getW()][3];
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();
		int debugX = 100, debugY = 90;
		System.out.println( "targetFrm Demension: " + targetFrm[0].length
				+ " x " + targetFrm.length );
		for ( int y = 0; y < targetFrm.length; y += macroBlkSize ) {
			for ( int x = 0; x < targetFrm[y].length; x += macroBlkSize ) {

				// find predicted block
				bestMatch = sequentialSearchMSD( targetFrm, referenceFrm, x, y,
						p, macroBlkSize );
				// DEBUG
				if ( x > debugX && y > debugY && x < debugX + 10
						&& y < debugY + 10 ) {
					System.out.println( "\n[@x,y=" + x + "," + y + "]:\t"
							+ bestMatch.toString() );
				}

				// store motion vector x
				System.out.print( "dx = " + ( x - bestMatch.getxTopLeft() ) );
				motionCompensation[y][x][1] = x - bestMatch.getxTopLeft();
				System.out
						.println( "\tdy = " + ( y - bestMatch.getyTopLeft() ) );
				motionCompensation[y][x][2] = y - bestMatch.getyTopLeft();

				// store each error_pixel_value
				for ( int i = 0; i < macroBlkSize; i++ ) {
					for ( int j = 0; j < macroBlkSize; j++ ) {

						if ( ( bestMatch.getxTopLeft() + j ) < referenceFrm[0].length
								&& ( bestMatch.getyTopLeft() + i ) < referenceFrm.length
								&& ( x + j ) < referenceFrm[0].length
								&& ( y + i ) < referenceFrm.length ) {

							motionCompensation[y + i][x + j][0] = Math
									.abs( targetFrm[y + i][x + j]
											- referenceFrm[bestMatch
													.getyTopLeft() + i][bestMatch
													.getxTopLeft() + j] );

							errorImg.setPixel( x + j, y + i,
									motionCompensation[y + i][x + j][0] );
							if ( ( x + j ) > 190 ) {
								System.out.println( "current x+j, y+i = "
										+ ( x + j ) + ", " + ( y + i ) );
							}

						}
					}
				}

			}// loop col ends
		}// loop row ends
		System.out.println( "avg: " + getAvgPixValue( errorImg ) );
		System.out.println( "mean: " + getMeanPixValue( errorImg ) );

		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
				targetImg.getH() );
		ImageJr mappedError = mapResidual( errorDepadded );
		/*
		 * - use appropriate search criteria to get the best matching macroblock
		 * from the reference frame -
		 */

		System.out.println( "# Name: Kae Sawada" + "\n# Target image name: "
				+ imgnamgeT + "\n# Reference image name: " + imgnamgeRef
				+ "\n# Number of target macro blocks: "
				+ ( targetFrm[0].length / macroBlkSize ) + " x "
				+ targetFrm.length / macroBlkSize + " (image size is "
				+ targetImg.getW() + " x " + targetImg.getH() + ")"
				+ "\n# Macroblock size = " + macroBlkSize + "\t" + "p=" + p );

		// DEBUG
		// System.out.println( "motion compensation[][]:" );
		// print3DArray( motionCompensation );

		mappedError.display( "error image" );
		mappedError.write2PPM( "out.ppm" );
		Thread.sleep( 10000 );

		// DEBUG
		HelperMethod h = new HelperMethod();
		// h.printImageJrPixelValues( mappedError );
		// h.printImageJrPixelValues( errorDepadded );

		// mappedError.display( "error" );
		// Thread.sleep( 4000 );
	}// note that macroblock sizse would affect the compression ratio

	public void displayImgJrPixelValues( ImageJr img )
	{
		for ( int i = 0; i < img.getH(); i++ ) {
			for ( int j = 0; j < img.getW(); j++ ) {
				img.displayPixelValue( j, i );
			}
		}
	}

	public int[] logarithmicSearch( int[][] prev, int[][] target )
	{

		int[] bestMatch = new int[2];
		/*
		 * [x-1][y-1] [x][y-1] [x+1][y-1] [x-1][y] [x][y] [x+1][y] [x-1][y+1]
		 * [x][y+1] [x+1][y+1]
		 */

		return bestMatch;
	}

	/** Search for best matching block using sequential search and MSD */
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
		ReferenceFrameBlock sameLoc = new ReferenceFrameBlock( tx0, ty0,
				meanAbsDiff( target, reference, tx0, ty0, tx0, ty0,
						macroBlkSize ) );
		// if ( sameLoc.getDiffValue() < threshold ) {
		// System.out.println("same block error < threashold");
		// return sameLoc;
		// }

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();

		for ( int i = 1; i <= p; i++ ) {

			if ( tx0 - p > -1 && tx0 - p < reference.length && ty0 - p > -1
					&& ty0 - p < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
						meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0
								- p, macroBlkSize ) ) );
				System.out.println( "compare 1" );
			}
			if ( ty0 - p > -1 && ty0 - p < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0, ty0 - p,
						meanAbsDiff( target, reference, tx0, ty0, tx0, ty0 - p,
								macroBlkSize ) ) );
				System.out.println( "compare 2" );
			}
			if ( tx0 + p > -1 && tx0 + p < reference.length && ty0 - p > -1
					&& ty0 - p < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 - p,
						meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0
								- p, macroBlkSize ) ) );
				System.out.println( "compare 3" );
			}
			if ( tx0 - p > -1 && tx0 - p < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 - p, ty0,
						meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0,
								macroBlkSize ) ) );
				System.out.println( "compare 4" );
			}

			diffs.add( sameLoc );

			if ( tx0 + p > -1 && tx0 + p < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 + p, ty0,
						meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0,
								macroBlkSize ) ) );
				System.out.println( "compare 6" );
			}
			if ( tx0 - p > -1 && tx0 - p < reference.length && ty0 + p > -1
					&& ty0 + p < reference.length ) {

				diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 + p,
						meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0
								+ p, macroBlkSize ) ) );
				System.out.println( "compare 7" );
			}

			if ( ty0 + p > -1 && ty0 + p < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0, ty0 + p,
						meanAbsDiff( target, reference, tx0, ty0, tx0, ty0 + p,
								macroBlkSize ) ) );
				System.out.println( "compare 8" );
			}
			if ( tx0 + p > -1 && tx0 + p < reference.length && ty0 + p > -1
					&& ty0 + p < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 + p,
						meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0
								+ p, macroBlkSize ) ) );
				System.out.println( "compare 9" );
			}
		}
		System.out.println( diffs );
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
		float threshold = (float) 3;
		ReferenceFrameBlock sameLoc = new ReferenceFrameBlock( tx0, ty0,
				meanSquareDiff( target, reference, tx0, ty0, tx0, ty0,
						macroBlkSize ) );
		if ( sameLoc.getDiffValue() < threshold ) {
			System.out.println( "same block error < threashold" );
			return sameLoc;
		}

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();

		for ( int i = 1; i <= p; i++ ) {

			if ( tx0 - i > -1 && tx0 - i < reference[0].length && ty0 - i > -1
					&& ty0 - i < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 - i, ty0 - i,
						meanSquareDiff( target, reference, tx0, ty0, tx0 - i,
								ty0 - i, macroBlkSize ) ) );
				// System.out.println( "compare 1" );
			}
			if ( ty0 - i > -1 && ty0 - i < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0, ty0 - i,
						meanSquareDiff( target, reference, tx0, ty0, tx0, ty0
								- i, macroBlkSize ) ) );
				// System.out.println( "compare 2" );
			}
			if ( tx0 + i > -1 && tx0 + i < reference[0].length && ty0 - i > -1
					&& ty0 - i < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 + i, ty0 - i,
						meanSquareDiff( target, reference, tx0, ty0, tx0 + i,
								ty0 - i, macroBlkSize ) ) );
				// System.out.println( "compare 3" );
			}
			if ( tx0 - i > -1 && tx0 - i < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 - i, ty0,
						meanSquareDiff( target, reference, tx0, ty0, tx0 - i,
								ty0, macroBlkSize ) ) );
				// System.out.println( "compare 4" );
			}

			diffs.add( sameLoc );

			if ( tx0 + i > -1 && tx0 + i < reference[0].length ) {
				diffs.add( new ReferenceFrameBlock( tx0 + i, ty0,
						meanSquareDiff( target, reference, tx0, ty0, tx0 + i,
								ty0, macroBlkSize ) ) );
				// System.out.println( "compare 6" );
			}
			if ( tx0 - i > -1 && tx0 - i < reference[0].length && ty0 + i > -1
					&& ty0 + i < reference.length ) {

				diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 + i,
						meanSquareDiff( target, reference, tx0, ty0, tx0 - i,
								ty0 + i, macroBlkSize ) ) );
				// System.out.println( "compare 7" );
			}

			if ( ty0 + i > -1 && ty0 + i < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0, ty0 + i,
						meanSquareDiff( target, reference, tx0, ty0, tx0, ty0
								+ i, macroBlkSize ) ) );
				// System.out.println( "compare 8" );
			}
			if ( tx0 + i > -1 && tx0 + i < reference[0].length && ty0 + i > -1
					&& ty0 + i < reference.length ) {
				diffs.add( new ReferenceFrameBlock( tx0 + i, ty0 + i,
						meanSquareDiff( target, reference, tx0, ty0, tx0 + i,
								ty0 + i, macroBlkSize ) ) );
				// System.out.println( "compare 9" );
			}
		}
		// debug
		// System.out.print( "[@x,y=" + tx0 + "," + ty0 + "]:" );
		// System.out.println( diffs );
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

	public void print2DArray( ReferenceFrameBlock[][] a )
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

	public void print3DArray( int[][][] a )
	{
		for ( int i = 0; i < a.length; i++ ) {
			for ( int j = 0; j < a[i].length; j++ ) {
				System.out.print( a[i][j][0] + " " + a[i][j][1] + " "
						+ a[i][j][2] + "\t" );
			}
			System.out.println();
		}
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
		int counter = 0;
		for ( int p = 0; p < macroBlkSizeIn; p++ ) {
			for ( int q = 0; q < macroBlkSizeIn; q++ ) {
				if ( ry0 + p < ref.length && rx0 + q < ref.length
						&& ty0 + p < target.length && tx0 + q < target.length ) {
					sum += Math.abs( target[ty0 + p][tx0 + q]
							- ref[ry0 + p][rx0 + q] );
					counter++;
				}
			}
		}
		// System.out.println( "sum, counter = " + sum + ", " + counter );
		sum *= ( 1. / counter );
		return sum;
	}

	/** Mean Square Difference A: current frame, B: reference frame */
	public float meanSquareDiff( int[][] target, int[][] ref, int tx0, int ty0,
			int rx0, int ry0, int macroBlkSizeIn )
	{
		float sum = 0;
		int counter = 0;
		for ( int p = 0; p < macroBlkSizeIn; p++ ) {
			for ( int q = 0; q < macroBlkSizeIn; q++ ) {
				if ( ry0 + p < ref.length && rx0 + q < ref[0].length
						&& ty0 + p < target.length
						&& tx0 + q < target[0].length ) {
					sum += Math.pow( target[ty0 + p][tx0 + q]
							- ref[ry0 + p][rx0 + q], 2 );
					counter++;
				}

				// if ( tx0 == 2 && ty0 == 2 && rx0 == 1 && ry0 == 3 ) {
				// System.out.println( "sum = " + sum );
				// System.out.println( "counter = " + counter );
				// System.out.println( "target[ty0 + p][tx0 + q]"
				// + target[ty0 + p][tx0 + q] );
				// System.out.println( "ref[ry0 + p][rx0 + q]"
				// + ref[ry0 + p][rx0 + q] );
				// }
			}
		}

		sum *= ( 1. / counter );

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

	public void display_macroBlock( int[][] frm, ReferenceFrameBlock ref,
			int macroBlkSize, int enlargeFactor ) throws InterruptedException
	{
		ImageJr img = new ImageJr( macroBlkSize, macroBlkSize );
		int[] rgb = new int[3];
		for ( int i = 0; i < macroBlkSize; i++ ) {
			for ( int j = 0; j < macroBlkSize; j++ ) {
				System.out.println( "x,y=" + ref.getxTopLeft() + j + ", "
						+ ref.getyTopLeft() + i );
				for ( int j2 = 0; j2 < rgb.length; j2++ ) {
					rgb[j2] = frm[ref.getxTopLeft() + j][ref.getyTopLeft() + i];
				}

				img.setPixel( j, i, rgb );
			}
		}
		ImageJr large = (ImageJr) img.enlargeImg( enlargeFactor );
		large.display( "macroblock (" + ref.getxTopLeft() + ", "
				+ ref.getyTopLeft() + ")" );
		Thread.sleep( 3000 );
	}

	public int[][] computeError( ReferenceFrameBlock bestMatch, int[][] target )
	{
		return target;
		// error_pixel_value = |pixel_in_target_block –
		// corresponding_pixel_in_the predicted_block|
	}

	public float getAvgPixValue( int[][] residual )
	{
		float sum = 0;
		for ( int i = 0; i < residual.length; i++ ) {
			for ( int j = 0; j < residual[i].length; j++ ) {
				sum += residual[i][j];
			}
		}

		return sum / ( residual[0].length * residual.length );
	}

	public float getAvgPixValue( int[][][] residual )
	{
		float sum = 0;
		for ( int i = 0; i < residual.length; i++ ) {
			for ( int j = 0; j < residual[i].length; j++ ) {
				sum += residual[i][j][0];
			}
		}
		return sum / ( residual[0].length * residual.length );
	}

	public int getMeanPixValue( ImageJr residual )
	{
		List<Integer> list = new LinkedList<Integer>();

		for ( int y = 0; y < residual.getH(); y++ ) {
			for ( int x = 0; x < residual.getW(); x++ ) {
				list.add( residual.getR( x, y ) );
			}
		}
		Collections.sort( list );
		// DEBUG
		for ( Integer integer : list ) {
			System.out.print( integer + " " );
		}
		System.out.println();
		return list.get( list.size() / 2 );
	}

	public int getSpecifiedPercentileValue( ImageJr residual, float percentile )
	{
		List<Integer> list = new LinkedList<Integer>();
		int sum = 0;

		for ( int y = 0; y < residual.getH(); y++ ) {
			for ( int x = 0; x < residual.getW(); x++ ) {
				list.add( residual.getR( x, y ) );
			}
		}
		Collections.sort( list );
		int startIdx = (int) Math.round( list.size() * ( percentile / 100. ) );
		for ( int i = startIdx; i < list.size(); i++ ) {
			sum += list.get( i );
		}

		// DEBUG
		// for ( int i = startIdx; i < list.size(); i++ ) {
		// if(i%10==0){System.out.println();}
		// System.out.print(list.get( i ) + " " );
		// }
		System.out.println();
		System.out.println( list.size() );
		System.out.println( list.size() * ( percentile / 100. ) );
		System.out.println( "sum: " + sum );
		System.out.println( "startIdx: " + startIdx );

		return sum / ( list.size() - startIdx );

	}

	public float getAvgPixValue( ImageJr residual )
	{
		float sum = 0;
		for ( int i = 0; i < residual.getH(); i++ ) {
			for ( int j = 0; j < residual.getW(); j++ ) {

				sum += residual.getR( j, i );
			}
		}
		return sum / ( residual.getH() * residual.getW() );
	}

	/* stil uses avg value */
	public ImageJr mapResidual( int[][][] residual )
	{
		ImageJr mappedResidual = new ImageJr( residual[0].length,
				residual.length );
		float avg = getAvgPixValue( residual );
		int value = 0;
		for ( int i = 0; i < residual.length; i++ ) {
			for ( int j = 0; j < residual[0].length; j++ ) {
				value = residual[i][j][0] > avg ? 255 : 0;
				mappedResidual.setPixel( j, i, value );
			}
		}
		return mappedResidual;
	}

	/* uses mean value */
	public ImageJr mapResidual( ImageJr residual )
	{
		ImageJr mappedResidual = new ImageJr( residual.getW(), residual.getH() );
		// float mean = getMeanPixValue( residual );
		float threshold = getSpecifiedPercentileValue( residual, (float) 90/* 98.5 */);
		System.out.println( threshold );

		int value = 0;
		for ( int i = 0; i < residual.getH(); i++ ) {
			for ( int j = 0; j < residual.getW(); j++ ) {
				value = residual.getR( j, i ) > threshold ? 255 : 0;
				mappedResidual.setPixel( j, i, value );
			}
		}
		return mappedResidual;
	}

	public void writeToAFile()
	{
		Writer writer;
		try {
			writer = new BufferedWriter( new OutputStreamWriter(
					new FileOutputStream( "task1_out.txt" ), "utf-8" ) );

			writer.write( "something" );
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
