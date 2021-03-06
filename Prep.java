import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

	/** float motionCompensation */
	public void MC( ImageJr targetImg, String imgNameT, ImageJr referenceImg,
			String imgNameRef, int p, int matchingCriteria,
			ImageJr residualImg, float[][][] motionCompensation,
			int macroBlkSize ) throws InterruptedException, IOException
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();

		for ( int y = 0; y < targetFrm.length; y += macroBlkSize ) {
			for ( int x = 0; x < targetFrm[y].length; x += macroBlkSize ) {

				// find predicted block
				bestMatch = sequentialSearchMSD( targetFrm, referenceFrm, x, y,
						p, macroBlkSize );

				// store motion vector x
				motionCompensation[y][x][1] = x - bestMatch.getxTopLeft();
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
											- referenceFrm[(int) ( bestMatch
													.getyTopLeft() + i )][(int) ( bestMatch
													.getxTopLeft() + j )] );

							errorImg.setPixel( x + j, y + i,
									(int) motionCompensation[y + i][x + j][0] );

						}
					}
				}
			}// loop col ends
		}// loop row ends
		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
				targetImg.getH() );
		ImageJr mappedError = mapResidual( errorDepadded );
		mappedError.display( "error image" );
		mappedError.write2PPM( "out.ppm" );
		Thread.sleep( 10000 );
	}

	public void MCi( ImageJr targetImg, String imgNameT, ImageJr referenceImg,
			String imgNameRef, int p, int matchingCriteria,
			ImageJr residualImg, int[][][] motionCompensation, int macroBlkSize )
			throws InterruptedException, IOException
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();

		for ( int y = 0; y < targetFrm.length; y += macroBlkSize ) {
			for ( int x = 0; x < targetFrm[y].length; x += macroBlkSize ) {

				// find predicted block
				bestMatch = sequentialSearchMSD( targetFrm, referenceFrm, x, y,
						p, macroBlkSize );

				// store motion vector x
				motionCompensation[y][x][1] = (int) ( x - bestMatch
						.getxTopLeft() );
				motionCompensation[y][x][2] = (int) ( y - bestMatch
						.getyTopLeft() );

				// store each error_pixel_value
				for ( int i = 0; i < macroBlkSize; i++ ) {
					for ( int j = 0; j < macroBlkSize; j++ ) {

						if ( ( bestMatch.getxTopLeft() + j ) < referenceFrm[0].length
								&& ( bestMatch.getyTopLeft() + i ) < referenceFrm.length
								&& ( x + j ) < referenceFrm[0].length
								&& ( y + i ) < referenceFrm.length ) {

							motionCompensation[y + i][x + j][0] = Math
									.abs( targetFrm[y + i][x + j]
											- referenceFrm[(int) ( bestMatch
													.getyTopLeft() + i )][(int) ( bestMatch
													.getxTopLeft() + j )] );

							errorImg.setPixel( x + j, y + i,
									(int) motionCompensation[y + i][x + j][0] );

						}
					}
				}
			}// loop col ends
		}// loop row ends
	}

	/** take float[][][] for MC storage */
	public void MC_w_half_pixel_accuracy( ImageJr targetImg, String imgNameT,
			ImageJr referenceImg, String imgNameRef, int p,
			int matchingCriteria, ImageJr residualImg,
			float[][][] motionCompensation, int macroBlkSize )
			throws InterruptedException, IOException
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();

		for ( int y = 0; y < targetFrm.length; y += macroBlkSize ) {
			for ( int x = 0; x < targetFrm[y].length; x += macroBlkSize ) {

				// find predicted block
				bestMatch = sequentialSearchMSD( targetFrm, referenceFrm, x, y,
						p, macroBlkSize );

				// store motion vector x
				motionCompensation[y][x][1] = x - bestMatch.getxTopLeft();
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
											- referenceFrm[(int) bestMatch
													.getyTopLeft() + i][(int) bestMatch
													.getxTopLeft() + j] );

							errorImg.setPixel( x + j, y + i,
									(int) motionCompensation[y + i][x + j][0] );

						}
					}
				}
			}// loop col ends
		}// loop row ends
		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
				targetImg.getH() );
		ImageJr mappedError = mapResidual( errorDepadded );
		mappedError.display( "error image" );
		mappedError.write2PPM( "out.ppm" );
		Thread.sleep( 10000 );
	}

	/** take returns List<MC> storage */
	public List<MotionCompensation> MC_w_half_pixel_accuracy(
			ImageJr targetImg, String imgNameT, ImageJr referenceImg,
			String imgNameRef, int p, int matchingCriteria,
			ImageJr residualImg, int macroBlkSize )
			throws InterruptedException, IOException
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();
		List<MotionCompensation> mc_halfpx = new ArrayList<MotionCompensation>();
		int tempResidual = 0, tempSumResidual = 0;

		for ( int y = 0; y < targetFrm.length; y += macroBlkSize ) {
			for ( int x = 0; x < targetFrm[y].length; x += macroBlkSize ) {

				// find predicted block (best match)
				bestMatch = sequentialSearchMSD_w_halfPixel_accuracy(
						targetFrm, referenceFrm, x, y, p, macroBlkSize );

				// store each error_pixel_value
				tempSumResidual = 0;
				for ( int i = 0; i < macroBlkSize; i++ ) {
					for ( int j = 0; j < macroBlkSize; j++ ) {

						if ( ( bestMatch.getxTopLeft() + j ) < referenceFrm[0].length
								&& ( bestMatch.getyTopLeft() + i ) < referenceFrm.length
								&& ( x + j ) < referenceFrm[0].length
								&& ( y + i ) < referenceFrm.length ) {

							tempResidual = Math.abs( targetFrm[y + i][x + j]
									- referenceFrm[(int) bestMatch
											.getyTopLeft() + i][(int) bestMatch
											.getxTopLeft() + j] );
							tempSumResidual += tempResidual;
							errorImg.setPixel( x + j, y + i, tempResidual );

						}// ends if
					}// ends j iteration
				}// ends i iteration

				// add mc (mv and residual) to the list
				mc_halfpx.add( new MotionCompensation( x
						- bestMatch.getxTopLeft(), y - bestMatch.getyTopLeft(),
						bestMatch.getDiffValue(), macroBlkSize ) );

			}// loop col ends
		}// loop row ends

		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
				targetImg.getH() );
		ImageJr mappedError = mapResidual( errorDepadded );
		mappedError.display( "error image" );
		mappedError.write2PPM( "out.ppm" );
		Thread.sleep( 3000 );
		printTask1ResultToConsole( mc_halfpx, imgNameT, imgNameRef, targetFrm,
				macroBlkSize, targetImg.getW(), targetImg.getH(),
				tempSumResidual );

		return mc_halfpx;
	}

	/** take returns List<MC> storage */
	public List<MotionCompensation> MC_regular( ImageJr targetImg,
			String imgNameT, ImageJr referenceImg, String imgNameRef, int p,
			int matchingCriteria, ImageJr residualImg, int macroBlkSize,
			boolean displayImg ) throws InterruptedException, IOException
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();
		List<MotionCompensation> mc_halfpx = new ArrayList<MotionCompensation>();
		int tempResidual = 0, tempSumResidual = 0;

		for ( int y = 0; y < targetFrm.length; y += macroBlkSize ) {
			for ( int x = 0; x < targetFrm[y].length; x += macroBlkSize ) {

				// find predicted block (best match)
				bestMatch = sequentialSearchMSD( targetFrm, referenceFrm, x, y,
						p, macroBlkSize );

				// store each error_pixel_value
				tempSumResidual = 0;
				for ( int i = 0; i < macroBlkSize; i++ ) {
					for ( int j = 0; j < macroBlkSize; j++ ) {

						if ( ( bestMatch.getxTopLeft() + j ) < referenceFrm[0].length
								&& ( bestMatch.getyTopLeft() + i ) < referenceFrm.length
								&& ( x + j ) < referenceFrm[0].length
								&& ( y + i ) < referenceFrm.length ) {

							tempResidual = Math.abs( targetFrm[y + i][x + j]
									- referenceFrm[(int) bestMatch
											.getyTopLeft() + i][(int) bestMatch
											.getxTopLeft() + j] );
							// tempSumResidual += tempResidual;
							errorImg.setPixel( x + j, y + i, tempResidual );

						}// ends if
					}// ends j iteration
				}// ends i iteration

				// add mc (mv and residual) to the list
				mc_halfpx.add( new MotionCompensation( x
						- bestMatch.getxTopLeft(), y - bestMatch.getyTopLeft(),
						bestMatch.getDiffValue(), macroBlkSize ) );

			}// loop col ends
		}// loop row ends

		if ( displayImg ) {
			ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
					targetImg.getH() );
			ImageJr mappedError = mapResidual( errorDepadded );
			mappedError.display( "error image" );
			mappedError.write2PPM( "out.ppm" );
			Thread.sleep( 3000 );
			printTask1ResultToConsole( mc_halfpx, imgNameT, imgNameRef,
					targetFrm, macroBlkSize, targetImg.getW(),
					targetImg.getH(), tempSumResidual );
		}
		return mc_halfpx;
	}
	
	public void MC_display_error( ImageJr targetImg, String imgNameT,
			ImageJr referenceImg, String imgNameRef, int p,
			int matchingCriteria, ImageJr residualImg,
			float[][][] motionCompensation, int macroBlkSize )
			throws InterruptedException, IOException
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

		for ( int y = 0; y < targetFrm.length; y += macroBlkSize ) {
			for ( int x = 0; x < targetFrm[y].length; x += macroBlkSize ) {

				// find predicted block
				bestMatch = sequentialSearchMSD( targetFrm, referenceFrm, x, y,
						p, macroBlkSize );
				// DEBUG
				if ( x > debugX && y > debugY && x < debugX + 10
						&& y < debugY + 10 ) {
					// System.out.println( "\n[@x,y=" + x + "," + y + "]:\t"
					// + bestMatch.toString() );
				}

				// store motion vector x
				// System.out.print( "dx = " + ( x - bestMatch.getxTopLeft() )
				// );
				motionCompensation[y][x][1] = x - bestMatch.getxTopLeft();
				// System.out
				// .println( "\tdy = " + ( y - bestMatch.getyTopLeft() ) );
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
											- referenceFrm[(int) bestMatch
													.getyTopLeft() + i][(int) bestMatch
													.getxTopLeft() + j] );

							errorImg.setPixel( x + j, y + i,
									(int) motionCompensation[y + i][x + j][0] );

						}
					}
				}

			}// loop col ends
		}// loop row ends

		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
				targetImg.getH() );
		ImageJr mappedError = mapResidual( errorDepadded );
		/*
		 * - use appropriate search criteria to get the best matching macroblock
		 * from the reference frame -
		 */

		mappedError.display( "error image" );
		mappedError.write2PPM( "out.ppm" );
		Thread.sleep( 10000 );

		// DEBUG
		System.out.println( "motion compensation[][]:" );
		for ( int i = 0; i < motionCompensation.length; i += macroBlkSize ) {
			for ( int j = 0; j < motionCompensation[0].length; j += macroBlkSize ) {
				System.out.print( "[ " + motionCompensation[i][j][0] + ", "
						+ motionCompensation[i][j][1] + ", "
						+ motionCompensation[i][j][2] + " ] " );
			}
			System.out.println();
		}
	}

	/* trash */
	public int getLower10Percent( int min, int max )
	{
		return (int) Math.round( max - min * .1 );
	}

	/* trash */
	public int getUpper10Percent( int min, int max )
	{
		return (int) Math.round( max - min * .9 );
	}

	/**
	 * this MC() will calculate the threshold values to aid moving object
	 * removal
	 */
	public void MC( ImageJr targetImg, String imgNameT, ImageJr referenceImg,
			String imgNameRef, int p, int matchingCriteria,
			ImageJr residualImg, int[][][] motionCompensation,
			int macroBlkSize, int[] thresholds )
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();
		int debugX = 100, debugY = 90;

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
				motionCompensation[y][x][1] = (int) ( x - bestMatch
						.getxTopLeft() );
				motionCompensation[y][x][2] = (int) ( y - bestMatch
						.getyTopLeft() );

				// store each error_pixel_value
				for ( int i = 0; i < macroBlkSize; i++ ) {
					for ( int j = 0; j < macroBlkSize; j++ ) {

						if ( ( bestMatch.getxTopLeft() + j ) < referenceFrm[0].length
								&& ( bestMatch.getyTopLeft() + i ) < referenceFrm.length
								&& ( x + j ) < referenceFrm[0].length
								&& ( y + i ) < referenceFrm.length ) {

							motionCompensation[y + i][x + j][0] = Math
									.abs( targetFrm[y + i][x + j]
											- referenceFrm[(int) bestMatch
													.getyTopLeft() + i][(int) bestMatch
													.getxTopLeft() + j] );

							errorImg.setPixel( x + j, y + i,
									(int) motionCompensation[y + i][x + j][0] );

						}
					}
				}

			}// loop col ends
		}// loop row ends

		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
				targetImg.getH() );
		ImageJr mappedError = mapResidual( errorDepadded );

		// mappedError.display( "error image" );
		// mappedError.write2PPM( "out.ppm" );
		// Thread.sleep( 10000 );

		// DEBUG
		// System.out.println( "motion compensation[][]:" );
		// for ( int i = 0; i < motionCompensation.length; i += macroBlkSize ) {
		// for ( int j = 0; j < motionCompensation[0].length; j += macroBlkSize
		// ) {
		// System.out.print( "[ " + motionCompensation[i][j][0] + ", "
		// + motionCompensation[i][j][1] + ", "
		// + motionCompensation[i][j][2] + " ] " );
		// }
		// System.out.println();
		// }

		// mappedError.display( "error" );
		// Thread.sleep( 4000 );
	}// note that macroblock size would affect the compression ratio

	public void MC_w_logSearch( ImageJr targetImg, String imgNameT,
			ImageJr referenceImg, String imgNameRef, int p,
			int matchingCriteria, ImageJr residualImg,
			float[][][] motionCompensation, int macroBlkSize )
			throws InterruptedException, IOException
	{
		ImageJr padTarget = targetImg.padImage( macroBlkSize );
		ImageJr padRef = referenceImg.padImage( macroBlkSize );
		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
		int[][] targetFrm = padTarget.imageJrTo2DArray();
		int[][] referenceFrm = padRef.imageJrTo2DArray();
		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();
		int debugX = 100, debugY = 90;

		for ( int y = 0; y < targetFrm.length; y += macroBlkSize ) {
			for ( int x = 0; x < targetFrm[y].length; x += macroBlkSize ) {

				// find predicted block
				bestMatch = logarithmicSearchMSD( targetFrm, referenceFrm, x,
						y, p, macroBlkSize );

				// store motion vector
				motionCompensation[y][x][1] = x - bestMatch.getxTopLeft();
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
											- referenceFrm[(int) bestMatch
													.getyTopLeft() + i][(int) bestMatch
													.getxTopLeft() + j] );

							errorImg.setPixel( x + j, y + i,
									(int) motionCompensation[y + i][x + j][0] );

						}
					}
				}

			}// loop col ends
		}// loop row ends

		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
				targetImg.getH() );
		ImageJr mappedError = mapResidual( errorDepadded );

		// DEBUG
		System.out.println( "motion compensation[][]:" );
		for ( int i = 0; i < motionCompensation.length; i += macroBlkSize ) {
			for ( int j = 0; j < motionCompensation[0].length; j += macroBlkSize ) {
				System.out.print( "[ " + motionCompensation[i][j][0] + ", "
						+ motionCompensation[i][j][1] + ", "
						+ motionCompensation[i][j][2] + " ] " );
			}
			System.out.println();
		}
		mappedError.write2PPM( "log_search_out.ppm" );
		mappedError.display( "error" );
		Thread.sleep( 4000 );
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

		// return the same block if diff < threshold
		float threshold = (float) 43;
		ReferenceFrameBlock sameLoc = new ReferenceFrameBlock( tx0, ty0,
				meanSquareDiff( target, reference, tx0, ty0, tx0, ty0,
						macroBlkSize ) );

		if ( sameLoc.getDiffValue() <= threshold ) {
			// System.out.println( "same block error < threashold" );
			return sameLoc;
		}

		int startX = ( tx0 - p < 0 ? 0 : tx0 - p ), startY = ( ty0 - p < 0 ? 0
				: ty0 - p );
		int stopX = ( tx0 + p > target[0].length ? target[0].length : tx0 + p ), stopY = ( ty0
				+ p > target.length ? target[0].length : ty0 + p );

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		// ----------------------
		for ( int i = startY; i < stopY; i++ ) {
			for ( int j = startX; j < stopX; j++ ) {

				if ( !( i == ty0 && j == ty0 ) ) {
					// System.out.println( "conditions met @ y,x=i,j=" + i + ","
					// + j );

					diffs.add( new ReferenceFrameBlock( j, i, meanSquareDiff(
							target, reference, tx0, ty0, j, i, macroBlkSize ) ) );
				}
			}
		}

		// debug
		// System.out.print( "[@x,y=" + tx0 + "," + ty0 + "]:" );
		// System.out.println( diffs );
		return findMinDiff( diffs );
	}

	/**
	 * half pixel accuracy MSD
	 * 
	 * @throws IOException
	 */
	public ReferenceFrameBlock sequentialSearchMSD_w_halfPixel_accuracy(
			int[][] target, int[][] reference, int tx0, int ty0, int p,
			int macroBlkSize ) throws IOException
	{

		// return the same block if diff < threshold
		float threshold = (float) 43;
		ReferenceFrameBlock sameLoc = new ReferenceFrameBlock( tx0, ty0,
				meanSquareDiff( target, reference, tx0, ty0, tx0, ty0,
						macroBlkSize ) );

		if ( sameLoc.getDiffValue() <= threshold ) {
			// System.out.println( "same block error < threashold" );
			return sameLoc;
		}

		int startX = ( tx0 - p < 0 ? 0 : tx0 - p ), startY = ( ty0 - p < 0 ? 0
				: ty0 - p );
		int stopX = ( tx0 + p > target[0].length ? target[0].length : tx0 + p ), stopY = ( ty0
				+ p > target.length ? target[0].length : ty0 + p );

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		ReferenceFrameBlock A = null, B = null, C = null, D = null, a = null, b = null, c = null, d = null;

		// half pixel accuracy
		for ( int i = startY; i < stopY; i += 2 ) {
			for ( int j = startX; j < stopX; j += 2 ) {

				A = new ReferenceFrameBlock( j, i, meanSquareDiff( target,
						reference, tx0, ty0, j, i, macroBlkSize ) );

				if ( j + 1 < reference[0].length ) {

					B = new ReferenceFrameBlock( j + 1, i,
							meanSquareDiff( target, reference, tx0, ty0, j + 1,
									i, macroBlkSize ) );
				}
				if ( i + 1 < reference.length ) {
					C = new ReferenceFrameBlock( j, i + 1,
							meanSquareDiff( target, reference, tx0, ty0, j,
									i + 1, macroBlkSize ) );

				}
				if ( i + 1 < reference.length && j + 1 < reference[0].length ) {

					D = new ReferenceFrameBlock( j + 1, i + 1, meanSquareDiff(
							target, reference, tx0, ty0, j + 1, i + 1,
							macroBlkSize ) );
				}

				a = A;
				b = new ReferenceFrameBlock( (float) ( j + .5 ), (float) ( i ),
						( A.getDiffValue() + B.getDiffValue() + 1 ) / 2 );
				c = new ReferenceFrameBlock( (float) ( j ), (float) ( i + .5 ),
						( A.getDiffValue() + C.getDiffValue() + 1 ) / 2 );
				d = new ReferenceFrameBlock( (float) ( j + .5 ), (float) ( i ),
						( A.getDiffValue() + B.getDiffValue()
								+ C.getDiffValue() + D.getDiffValue() + 2 ) / 4 );

				diffs.add( A );

				diffs.add( B );
				diffs.add( C );
				diffs.add( D );
				// diffs.add( a );
				diffs.add( b );
				diffs.add( c );
				diffs.add( d );
			}
		}

		writeDiffValuesToFile( diffs, tx0, ty0 );
		return findMinDiff( diffs );
	}

	/** Search for best maching block using sequesntial search and MSD */
	public ReferenceFrameBlock sequentialSearchMSD_wrong_version(
			int[][] target, int[][] reference, int tx0, int ty0, int p,
			int macroBlkSize )
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
			// System.out.println( "same block error < threashold" );
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

	public ReferenceFrameBlock logarithmicSearchMSD( int[][] target,
			int[][] reference, int tx0, int ty0, int p, int macroBlkSize )
	{/*
	 * If the difference between the target block and the candidate block at the
	 * same position in the past frame is below some threshold then no motion
	 */

		List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
		while ( p > 0 ) {

			float threshold = (float) 3;
			ReferenceFrameBlock sameLoc = new ReferenceFrameBlock( tx0, ty0,
					meanSquareDiff( target, reference, tx0, ty0, tx0, ty0,
							macroBlkSize ) );
			if ( sameLoc.getDiffValue() < threshold ) {
				System.out.println( "same block error < threashold" );
				return sameLoc;
			}

			for ( int i = p; i <= p; i++ ) {

				if ( tx0 - i > -1 && tx0 - i < reference[0].length
						&& ty0 - i > -1 && ty0 - i < reference.length ) {
					diffs.add( new ReferenceFrameBlock( tx0 - i, ty0 - i,
							meanSquareDiff( target, reference, tx0, ty0, tx0
									- i, ty0 - i, macroBlkSize ) ) );
					// System.out.println( "compare 1" );
				}
				if ( ty0 - i > -1 && ty0 - i < reference.length ) {
					diffs.add( new ReferenceFrameBlock( tx0, ty0 - i,
							meanSquareDiff( target, reference, tx0, ty0, tx0,
									ty0 - i, macroBlkSize ) ) );
					// System.out.println( "compare 2" );
				}
				if ( tx0 + i > -1 && tx0 + i < reference[0].length
						&& ty0 - i > -1 && ty0 - i < reference.length ) {
					diffs.add( new ReferenceFrameBlock( tx0 + i, ty0 - i,
							meanSquareDiff( target, reference, tx0, ty0, tx0
									+ i, ty0 - i, macroBlkSize ) ) );
					// System.out.println( "compare 3" );
				}
				if ( tx0 - i > -1 && tx0 - i < reference.length ) {
					diffs.add( new ReferenceFrameBlock( tx0 - i, ty0,
							meanSquareDiff( target, reference, tx0, ty0, tx0
									- i, ty0, macroBlkSize ) ) );
					// System.out.println( "compare 4" );
				}

				diffs.add( sameLoc );

				if ( tx0 + i > -1 && tx0 + i < reference[0].length ) {
					diffs.add( new ReferenceFrameBlock( tx0 + i, ty0,
							meanSquareDiff( target, reference, tx0, ty0, tx0
									+ i, ty0, macroBlkSize ) ) );
					// System.out.println( "compare 6" );
				}
				if ( tx0 - i > -1 && tx0 - i < reference[0].length
						&& ty0 + i > -1 && ty0 + i < reference.length ) {

					diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 + i,
							meanSquareDiff( target, reference, tx0, ty0, tx0
									- i, ty0 + i, macroBlkSize ) ) );
					// System.out.println( "compare 7" );
				}

				if ( ty0 + i > -1 && ty0 + i < reference.length ) {
					diffs.add( new ReferenceFrameBlock( tx0, ty0 + i,
							meanSquareDiff( target, reference, tx0, ty0, tx0,
									ty0 + i, macroBlkSize ) ) );
					// System.out.println( "compare 8" );
				}
				if ( tx0 + i > -1 && tx0 + i < reference[0].length
						&& ty0 + i > -1 && ty0 + i < reference.length ) {
					diffs.add( new ReferenceFrameBlock( tx0 + i, ty0 + i,
							meanSquareDiff( target, reference, tx0, ty0, tx0
									+ i, ty0 + i, macroBlkSize ) ) );
					// System.out.println( "compare 9" );
				}

			}// ends for loop
			p /= 2.;
			ReferenceFrameBlock min = findMinDiff( diffs );
			tx0 = (int) min.getxTopLeft();
			ty0 = (int) min.getyTopLeft();
			System.out.println( "in while loop, p=" + p );
		}// ends while loop

		return findMinDiff( diffs );
	}

	/** DEBUG build a 2d square array */
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

	public void print3DArrayI( int[][][] a )
	{
		for ( int i = 0; i < a.length; i++ ) {
			for ( int j = 0; j < a[i].length; j++ ) {
				System.out.print( "[ " );
				for ( int j2 = 0; j2 < a[0][0].length; j2++ ) {
					System.out.print( a[i][j][j2] + " " );
				}
				System.out.print( "]" );
			}
			System.out.println();
		}
	}

	public void print3DArrayF( float[][][] a )
	{
		for ( int i = 0; i < a.length; i++ ) {
			for ( int j = 0; j < a[i].length; j++ ) {
				System.out.print( "[ " );
				for ( int j2 = 0; j2 < a[0][0].length; j2++ ) {
					System.out.print( a[i][j][j2] + " " );
				}
				System.out.print( "]" );
			}
			System.out.println();
		}
	}

	// /** DEBUG simplified & test version of log search */
	// public ReferenceFrameBlock logSearchJr( int[][] target, int[][]
	// reference,
	// int tx0, int ty0, int p, int macroBlkSize )
	// {/*
	// * If the difference between the target block and the candidate block at
	// the
	// * same position in the past frame is below some threshold then no motion
	// */
	//
	// List<ReferenceFrameBlock> diffs = new ArrayList<ReferenceFrameBlock>();
	// while ( p >= 0 ) {
	//
	// diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 - p,
	// meanAbsDiff( target, reference, tx0, ty0, tx0 - p, ty0 - p,
	// macroBlkSize ) ) );
	//
	// diffs.add( new ReferenceFrameBlock( tx0, ty0 - p, meanAbsDiff(
	// target, reference, tx0, ty0, tx0, ty0 - p, macroBlkSize ) ) );
	//
	// diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 - p,
	// meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 - p,
	// macroBlkSize ) ) );
	//
	// diffs.add( new ReferenceFrameBlock( tx0 - p, ty0, meanAbsDiff(
	// target, reference, tx0, ty0, tx0 - p, ty0, macroBlkSize ) ) );
	//
	// diffs.add( new ReferenceFrameBlock( tx0, ty0, meanAbsDiff( target,
	// reference, tx0, ty0, tx0, ty0, macroBlkSize ) ) );
	//
	// diffs.add( new ReferenceFrameBlock( tx0 + p, ty0, meanAbsDiff(
	// target, reference, tx0, ty0, tx0 + p, ty0, macroBlkSize ) ) );
	//
	// diffs.add( new ReferenceFrameBlock( tx0 - p, ty0 + p,
	// meanSquareDiff( target, reference, tx0, ty0, tx0 - p, ty0
	// + p, macroBlkSize ) ) );
	//
	// diffs.add( new ReferenceFrameBlock( tx0, ty0 + p, meanAbsDiff(
	// target, reference, tx0, ty0, tx0, ty0 + p, macroBlkSize ) ) );
	//
	// diffs.add( new ReferenceFrameBlock( tx0 + p, ty0 + p,
	// meanAbsDiff( target, reference, tx0, ty0, tx0 + p, ty0 + p,
	// macroBlkSize ) ) );
	// p /= 2;
	// ReferenceFrameBlock min = findMinDiff( diffs );
	// tx0 = min.getxTopLeft();
	// ty0 = min.getyTopLeft();
	// }
	// return findMinDiff( diffs );
	// }

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
					rgb[j2] = frm[(int) ref.getxTopLeft() + j][(int) ref
							.getyTopLeft() + i];
				}

				img.setPixel( j, i, rgb );
			}
		}
		ImageJr large = (ImageJr) img.enlargeImg( enlargeFactor );
		// large.display( "macroblock (" + ref.getxTopLeft() + ", "
		// + ref.getyTopLeft() + ")" );
		// Thread.sleep( 3000 );
	}

	public int[][] computeError( ReferenceFrameBlock bestMatch, int[][] target )
	{
		return target;
		// error_pixel_value = |pixel_in_target_block �
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

	public int getSpecifiedHigerPercentileDiffValue( ImageJr residual,
			float percentile )
	{
		List<Integer> list = new LinkedList<Integer>();
		int sum = 0;

		for ( int y = 0; y < residual.getH(); y++ ) {
			for ( int x = 0; x < residual.getW(); x++ ) {
				list.add( residual.getR( x, y ) );
			}
		}
		Collections.sort( list );
		int startIdx = (int) Math.round( list.size() * percentile * 1. - 1 );
		for ( int i = startIdx; i < list.size(); i++ ) {
			sum += list.get( i );
		}

		// DEBUG
		// for ( int i = startIdx; i < list.size(); i++ ) {
		// if(i%10==0){System.out.println();}
		// System.out.print(list.get( i ) + " " );
		// }
		// System.out.println();
		// System.out.println( list.size() );
		// System.out.println( list.size() * ( percentile ) );
		// System.out.println( "sum: " + sum );
		// System.out.println( "startIdx: " + startIdx );

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
		float threshold = getSpecifiedHigerPercentileDiffValue( residual,
				(float) .9/* 98.5 */);
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

	public void writeTask1ResultToFile( int[][][] motionCompensation,
			String imgNameT, String imgNameRef, int[][] targetFrm,
			int macroBlkSize, int targetImgW, int targetImgH, int p )
			throws IOException
	{
		Writer writer = null;
		try {
			writer = new BufferedWriter( new OutputStreamWriter(
					new FileOutputStream( "task1_ksawada_mv.txt" ), "utf-8" ) );

			writer.write( "# Name: Kae Sawada" + "\n# Target image name: "
					+ imgNameT + "\n# Reference image name: " + imgNameRef
					+ "\n# Number of target macro blocks: "
					+ ( targetFrm[0].length / macroBlkSize ) + " x "
					+ targetFrm.length / macroBlkSize + " (image size is "
					+ targetImgW + " x " + targetImgH + ")"
					+ "\n# Macroblock size = " + macroBlkSize + "\t" + "p=" + p );

			writer.write( "\n" );

			// for ( int i = 0; i < motionCompensation.length; i += macroBlkSize
			// ) {
			// for ( int j = 0; j < motionCompensation[0].length; j +=
			// macroBlkSize ) {
			// writer.write( "[ " + motionCompensation[i][j][1] + ", "
			// + motionCompensation[i][j][2] + " ] " );
			// }
			// System.out.println();
			// }
			writer.write( "\n" );

		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}

	}

	public void writeTask1ResultToFile(
			List<MotionCompensation> motionCompensation, String imgNameT,
			String imgNameRef, /* int[][] targetFrm, */int macroBlkSize,
			int targetImgW, int targetImgH, int p ) throws IOException
	{

		Writer writer = null;
		try {
			writer = new BufferedWriter( new OutputStreamWriter(
					new FileOutputStream( "task1_ksawada_mv.txt" ), "utf-8" ) );

			writer.write( "# Name: Kae Sawada" + "\n# Target image name: "
					+ imgNameT + "\n# Reference image name: " + imgNameRef
					+ "\n# Number of target macro blocks: "
					+ ( targetImgW / macroBlkSize ) + " x " + targetImgH
					/ macroBlkSize + " (image size is " + targetImgW + " x "
					+ targetImgH + ")" + "\n# Macroblock size = "
					+ macroBlkSize + "\t" + "p=" + p );

			writer.write( "\n" );

			int loopBoundOuter = targetImgH / macroBlkSize;
			int loopBoundInner = targetImgW / macroBlkSize;
			int idx = 0;
			for ( int i = 0; i < loopBoundOuter; i++ ) {
				for ( int j = 0; j < loopBoundInner; j++ ) {
					writer.write( "[ " + motionCompensation.get( idx ).getMvX()
							+ ", " + motionCompensation.get( idx ).getMvY()
							+ " ] " );
					idx++;
				}
				writer.write( "\n" );
			}

		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}

	}

	public void writeDiffValuesToFile( List<ReferenceFrameBlock> diffs,
			int tx0, int ty0 ) throws IOException
	{

		Writer writer = null;
		try {
			writer = new BufferedWriter( new OutputStreamWriter(
					new FileOutputStream( "diffs.txt" ), "utf-8" ) );

			int i = 0;
			// debug
			for ( ReferenceFrameBlock dif : diffs ) {
				if ( i % 5 == 0 ) {
					writer.write( "\n" );
				}
				writer.write( "[@x,y=" + tx0 + ", " + ty0 + "]"
						+ dif.toString() + " " );
				i++;
			}

		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}

	}

	public void printTask1ResultToConsole(
			List<MotionCompensation> motionCompensation, String imgNameT,
			String imgNameRef, int[][] targetFrm, int macroBlkSize,
			int targetImgW, int targetImgH, int p ) throws IOException
	{

		System.out.println( "something" );
		System.out.println( "# Name: Kae Sawada" + "\n# Target image name: "
				+ imgNameT + "\n# Reference image name: " + imgNameRef
				+ "\n# Number of target macro blocks: "
				+ ( targetFrm[0].length / macroBlkSize ) + " x "
				+ targetFrm.length / macroBlkSize + " (image size is "
				+ targetImgW + " x " + targetImgH + ")"
				+ "\n# Macroblock size = " + macroBlkSize + "\t" + "p=" + p );

		System.out.println( "\n" );

		int loopBoundOuter = targetImgH / macroBlkSize;
		int loopBoundInner = targetImgW / macroBlkSize;
		int idx = 0;
		for ( int i = 0; i < loopBoundOuter; i++ ) {
			for ( int j = 0; j < loopBoundInner; j++ ) {

				System.out
						.print( "[ " + motionCompensation.get( idx ).getMvX()
								+ ", " + motionCompensation.get( idx ).getMvY()
								+ " ] " );
				idx++;
			}
			System.out.println();
		}

	}

	public void printMC( List<MotionCompensation> motionCompensation,
			int macroBlkSize, int targetImgW, int targetImgH )
			throws IOException
	{

		int loopBoundOuter = targetImgH / macroBlkSize;
		int loopBoundInner = targetImgW / macroBlkSize;
		int idx = 0;
		for ( int i = 0; i < loopBoundOuter; i++ ) {
			for ( int j = 0; j < loopBoundInner; j++ ) {

				System.out.print( "[ " + motionCompensation.get( idx ).getMvX()
						+ ", " + motionCompensation.get( idx ).getMvY() + ", "
						+ motionCompensation.get( idx ).getResidual() + " ] " );
				idx++;
			}
			System.out.println();
		}

	}
}
