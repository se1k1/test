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
		String targetName = "", refName = "";
		if ( n < 10 ) {
			targetName = "Walk_00" + n + ".ppm";
		} else if ( n < 100 ) {
			targetName = "Walk_0" + n + ".ppm";
		} else {
			targetName = "Walk_" + n + ".ppm";
		}

		if ( n - 2 < 10 ) {
			refName = "Walk_00" + ( n - 2 ) + ".ppm";
		} else if ( n - 2 < 100 ) {
			refName = "Walk_0" + ( n - 2 ) + ".ppm";
		} else {
			refName = "Walk_" + ( n - 2 ) + ".ppm";
		}

		targetImg = new ImageJr( targetName );
		referenceImg = new ImageJr( refName );

	}

	// public int s( int[][][] array, float percentile )
	// {
	// List<Integer> list = new LinkedList<Integer>();
	// int sum = 0;
	//
	// for ( int y = 0; y < array.length; y++ ) {
	// for ( int x = 0; x < array[0].length; x++ ) {
	// list.add( array[y][x][0] );
	// }
	// }
	// Collections.sort( list );
	// int stopIdx = (int) Math.round( list.size() * percentile * 1. );
	// for ( int i = 0; i < stopIdx; i++ ) {
	// sum += list.get( i );
	// }
	// return sum / ( stopIdx + 1 );
	// }

	/**
	 * compute specified lower percentage average values: difference and motion
	 * vector threshold[0]=avg of upper ?? percentile diff values (dynamic
	 * block) threshold[1]=avg of lower ?? percentile mv values threshold[2]=avg
	 * of lower ?? percentile diff values (static block)
	 */
	public float[] getThresholds( float[][][] MC, float percentileDiffDynamic,
			float percentileDiffStatic, float percentileMV, int macroBlkSize )
	{
		List<Float> diffs = new LinkedList<Float>();
		List<Float> mvs = new LinkedList<Float>();
		int sumDiffDynamic = 0, sumDiffStatic = 0, sumMV = 0;

		for ( int y = 0; y < MC.length; y++ ) {
			for ( int x = 0; x < MC[0].length; x++ ) {
				diffs.add( MC[y][x][0] );
				if ( x % macroBlkSize == 0 && y % macroBlkSize == 0 ) {
					mvs.add( ( MC[y][x][1] + MC[y][x][2] ) / 2 );
				}
			}
		}

		Collections.sort( diffs );
		Collections.sort( mvs );

		// DEBUG
		System.out.println( diffs );
		System.out.println( mvs );

		// lower ??%
		float stopIdxMV = Math.round( mvs.size() * percentileMV * 1. ) - 1;

		// upper ??%
		float startIdxDiffDynamic = Math.round( diffs.size()
				* percentileDiffDynamic * 1. ) - 1;

		// lower ??%
		float stopIdxDiffStatic = Math.round( diffs.size()
				* percentileDiffStatic * 1. ) - 1;

		for ( int i = 0; i <= stopIdxMV; i++ ) {
			sumMV += mvs.get( i );
		}

		for ( int i = 0; i < diffs.size(); i++ ) {
			if ( i > startIdxDiffDynamic ) {
				sumDiffDynamic += diffs.get( i );
			}
			if ( i < stopIdxDiffStatic ) {
				sumDiffStatic += diffs.get( i );
			}
		}

		// DEBUG
		System.out.println( "sumMV=" + sumMV + "\t" + "sumDiff="
				+ sumDiffDynamic + "sumDiffStatic=" + sumDiffStatic );

		System.out.println( "MV size=" + stopIdxMV + 1 + "\t"
				+ "DiffDynamic size=" + startIdxDiffDynamic + "\t"
				+ "DiffStatic size=" + stopIdxDiffStatic + 1 );

		System.out.println( "stopIdxMV=" + ( stopIdxMV + 1 ) + "\t"
				+ "startIdxDiffDynamic="
				+ ( diffs.size() - startIdxDiffDynamic + 1. ) + "\t"
				+ "stopIdxDiffStatic=" + stopIdxDiffStatic );

		float[] thresholds =
		{
				(float) ( sumDiffDynamic / ( diffs.size() - startIdxDiffDynamic + 1. ) ),
				(float) ( sumMV / ( stopIdxMV + 1. ) ),
				(float) ( sumDiffStatic / ( stopIdxDiffStatic + 1. ) ) };

		return thresholds;
	}

	public float[] getThresholds( List<MotionCompensation> MC,
			float percentileDiffDynamic, float percentileDiffStatic,
			float percentileMV, int macroBlkSize )
	{
		List<Float> diffs = new LinkedList<Float>();
		List<Float> mvs = new LinkedList<Float>();
		int sumDiffDynamic = 0, sumDiffStatic = 0, sumMV = 0;

		for ( int y = 0; y < MC.size(); y++ ) {

			diffs.add( MC.get( y ).getResidual() );
			mvs.add( ( MC.get( y ).getMvX() + MC.get( y ).getMvY() ) / 2 );
		}

		Collections.sort( diffs );
		Collections.sort( mvs );

		// DEBUG
		System.out.println( diffs );
		System.out.println( mvs );

		// lower xx%
		float stopIdxMV = Math.round( mvs.size() * percentileMV * 1. ) - 1;

		// upper xx%
		float startIdxDiffDynamic = Math.round( diffs.size()
				* percentileDiffDynamic * 1. ) - 1;

		// lower xx%
		float stopIdxDiffStatic = Math.round( diffs.size()
				* percentileDiffStatic * 1. ) - 1;

		for ( int i = 0; i <= stopIdxMV; i++ ) {
			sumMV += mvs.get( i );
		}

		for ( int i = 0; i < diffs.size(); i++ ) {
			if ( i > startIdxDiffDynamic ) {
				sumDiffDynamic += diffs.get( i );
			}
			if ( i < stopIdxDiffStatic ) {
				sumDiffStatic += diffs.get( i );
			}
		}

		// DEBUG
		System.out.println( "sumMV=" + sumMV + "\t" + "sumDiff="
				+ sumDiffDynamic + "sumDiffStatic=" + sumDiffStatic );

		System.out.println( "MV size=" + stopIdxMV + 1 + "\t"
				+ "DiffDynamic size=" + startIdxDiffDynamic + "\t"
				+ "DiffStatic size=" + stopIdxDiffStatic + 1 );

		System.out.println( "stopIdxMV=" + ( stopIdxMV + 1 ) + "\t"
				+ "startIdxDiffDynamic="
				+ ( diffs.size() - startIdxDiffDynamic + 1. ) + "\t"
				+ "stopIdxDiffStatic=" + stopIdxDiffStatic );

		float[] thresholds =
		{
				(float) ( sumDiffDynamic / ( diffs.size() - startIdxDiffDynamic + 1. ) ),
				(float) Math.abs( sumMV / ( stopIdxMV + 1. ) ),
				(float) ( sumDiffStatic / ( stopIdxDiffStatic + 1. ) ) };

		return thresholds;
	}

	/**
	 * compute specified lower or upper percentage average values: difference
	 * and motion vector threshold[0]=avg of upper ?? percentile diff values
	 * (dynamic block) threshold[1]=avg of lower ?? percentile mv values
	 * threshold[2]=avg of lower ?? percentile diff values (static block)
	 */
	public int[] getThresholds( int[][][] MC, float percentileDiffDynamic,
			float percentileDiffStatic, float percentileMV, int macroBlkSize )
	{
		List<Float> diffs = new LinkedList<Float>();
		List<Float> mvs = new LinkedList<Float>();
		int sumDiffDynamic = 0, sumDiffStatic = 0, sumMV = 0;

		for ( int y = 0; y < MC.length; y++ ) {
			for ( int x = 0; x < MC[0].length; x++ ) {
				diffs.add( (float) MC[y][x][0] );
				if ( x % macroBlkSize == 0 && y % macroBlkSize == 0 ) {
					mvs.add( (float) ( MC[y][x][1] + MC[y][x][2] ) / 2 );
				}
			}
		}

		Collections.sort( diffs );
		Collections.sort( mvs );

		// DEBUG
		System.out.println( diffs );
		System.out.println( mvs );

		// lower ??%
		float stopIdxMV = Math.round( mvs.size() * percentileMV * 1. ) - 1;

		// upper ??%
		float startIdxDiffDynamic = Math.round( diffs.size()
				* percentileDiffDynamic * 1. ) - 1;

		// lower ??%
		float stopIdxDiffStatic = Math.round( diffs.size()
				* percentileDiffStatic * 1. ) - 1;

		for ( int i = 0; i <= stopIdxMV; i++ ) {
			sumMV += mvs.get( i );
		}

		for ( int i = 0; i < diffs.size(); i++ ) {
			if ( i > startIdxDiffDynamic ) {
				sumDiffDynamic += diffs.get( i );
			}
			if ( i < stopIdxDiffStatic ) {
				sumDiffStatic += diffs.get( i );
			}
		}

		// DEBUG
		System.out.println( "sumMV=" + sumMV + "\t" + "sumDiff="
				+ sumDiffDynamic + "sumDiffStatic=" + sumDiffStatic );

		System.out.println( "MV size=" + stopIdxMV + 1 + "\t"
				+ "DiffDynamic size=" + startIdxDiffDynamic + "\t"
				+ "DiffStatic size=" + stopIdxDiffStatic + 1 );

		System.out.println( "stopIdxMV=" + ( stopIdxMV + 1 ) + "\t"
				+ "startIdxDiffDynamic="
				+ ( diffs.size() - startIdxDiffDynamic + 1. ) + "\t"
				+ "stopIdxDiffStatic=" + stopIdxDiffStatic );

		int[] thresholds =
		{
				(int) ( sumDiffDynamic / ( diffs.size() - startIdxDiffDynamic + 1. ) ),
				(int) ( sumMV / ( stopIdxMV + 1. ) ),
				(int) ( sumDiffStatic / ( stopIdxDiffStatic + 1. ) ) };

		return thresholds;
	}

	/**
	 * return values: coordinate[0]=x, coordinate[1]=y of the closest static
	 * neighbor block
	 */
	public float[] sequentiallySearchStaticNeighborBlk(
			float[][][] motionCompensation, int tx0, int ty0, int p,
			int macroBlkSize, float threshold )
	{
		p = p * macroBlkSize;
		int startX = ( tx0 - p < 0 ? 0 : tx0 - p ), startY = ( ty0 - p < 0 ? 0
				: ty0 - p );
		int stopX = ( tx0 + p > motionCompensation[0].length ? motionCompensation[0].length
				: tx0 + p ), stopY = ( ty0 + p > motionCompensation.length ? motionCompensation[0].length
				: ty0 + p );

		float[] coordinate = new float[2];
		// for ( int i = startY; i < stopY; i++ ) {
		// for ( int j = startX; j < stopX; j++ ) {
		for ( int i = startY; i < stopY; i++ ) {
			for ( int j = startX; j < stopX; j++ ) {
				if ( /*
					 * i > -1 && i < motionCompensation.length && j > -1 && j <
					 * motionCompensation[0].length &&
					 */motionCompensation[i][j][1] == 0
						&& motionCompensation[i][j][2] == 0
						&& motionCompensation[i][j][0] <= threshold ) {
					// System.out.println( "conditions met @ y,x=i,j=" + i + ","
					// + j );
					// System.out.println( "threshold=" + threshold );
					coordinate[0] = j;// x coord
					coordinate[1] = i;// y coord
					return coordinate;
				}
			}
		}

		return coordinate;
	}

	/**
	 * return values: coordinate[0]=x, coordinate[1]=y of the closest static
	 * neighbor block
	 */
	public float[] sequentiallySearchStaticNeighborBlk_old(
			float[][][] motionCompensation, int tx0, int ty0, int p,
			int macroBlkSize, float threshold )
	{
		p = p * macroBlkSize;
		int startX = ( tx0 - p < 0 ? 0 : tx0 - p ), startY = ( ty0 - p < 0 ? 0
				: ty0 - p );
		int stopX = ( tx0 + p > motionCompensation[0].length ? motionCompensation[0].length
				: tx0 + p ), stopY = ( ty0 + p > motionCompensation.length ? motionCompensation[0].length
				: ty0 + p );

		float[] coordinate = new float[2];
		// for ( int i = startY; i < stopY; i++ ) {
		// for ( int j = startX; j < stopX; j++ ) {
		for ( int i = startY; i < stopY; i += macroBlkSize ) {
			for ( int j = startX; j < stopX; j += macroBlkSize ) {
				if ( /*
					 * i > -1 && i < motionCompensation.length && j > -1 && j <
					 * motionCompensation[0].length &&
					 */motionCompensation[i][j][1] == 0
						&& motionCompensation[i][j][2] == 0
						&& motionCompensation[i][j][0] <= threshold ) {
					// System.out.println( "conditions met @ y,x=i,j=" + i + ","
					// + j );
					// System.out.println( "threshold=" + threshold );
					coordinate[0] = j;// x coord
					coordinate[1] = i;// y coord
					return coordinate;
				}
			}
		}

		return coordinate;
	}

	/**
	 * return values: coordinate[0]=x, coordinate[1]=y of the closest static
	 * neighbor block
	 */
	public float[] sequentiallySearchStaticNeighborBlk(
			int[][][] motionCompensation, int tx0, int ty0, int p,
			int macroBlkSize, float threshold )
	{
		p = p * macroBlkSize;
		int startX = ( tx0 - p < 0 ? 0 : tx0 - p ), startY = ( ty0 - p < 0 ? 0
				: ty0 - p );
		int stopX = ( tx0 + p > motionCompensation[0].length ? motionCompensation[0].length
				: tx0 + p ), stopY = ( ty0 + p > motionCompensation.length ? motionCompensation[0].length
				: ty0 + p );

		float[] coordinate = new float[2];
		// for ( int i = startY; i < stopY; i++ ) {
		// for ( int j = startX; j < stopX; j++ ) {
		for ( int i = startY; i < stopY; i += macroBlkSize ) {
			for ( int j = startX; j < stopX; j += macroBlkSize ) {
				if ( /*
					 * i > -1 && i < motionCompensation.length && j > -1 && j <
					 * motionCompensation[0].length &&
					 */motionCompensation[i][j][1] == 0
						&& motionCompensation[i][j][2] == 0
						&& motionCompensation[i][j][0] <= threshold ) {
					// System.out.println( "conditions met @ y,x=i,j=" + i + ","
					// + j );
					// System.out.println( "threshold=" + threshold );
					coordinate[0] = j;// x coord
					coordinate[1] = i;// y coord
					return coordinate;
				}
			}
		}

		return coordinate;
	}

	public void removeMovingObj01( ImageJr targetImg, ImageJr refImg,
			String imgNameT, String imgNameRef, int macroBlkSize, int p )
			throws InterruptedException, IOException
	{
		ImageJr copyOftargetImg = targetImg.deep_copy_image_ks();
		List<MotionCompensation> mcList = MC_regular( targetImg, imgNameT,
				refImg, imgNameRef, p, 0, new ImageJr( targetImg.getW(),
						targetImg.getH() ), macroBlkSize, false );

		float[] thresholds = new float[3];
		float[] coordinateNeighborStaticBlk = new float[2];
		thresholds = getThresholds( mcList, (float) .75, (float) .1,
				(float) .3, macroBlkSize );

		// System.out.println("inside removeMovingObj01");
		// printMC( mcList, macroBlkSize, targetImg.getW(), targetImg.getW() );

		int[] paddedImgSize = new int[2];
		targetImg.paddedSize( macroBlkSize, paddedImgSize );
		// thresholds[0] = 100;// dynamic
		// thresholds[1] = 0;// mv
		// thresholds[2] = 4;// neighbor static blk

		System.out.println( "threshold diff dynamic: " + thresholds[0] );
		System.out.println( "threshold mv: " + thresholds[1] );
		System.out.println( "threshold diff static: " + thresholds[2] );

		float[][][] mcArray = mcListTo3dArray( mcList, paddedImgSize[0]
				/ macroBlkSize, paddedImgSize[1] / macroBlkSize );
		// print3DArrayF( mcArray );
		float avgDiff = 0;

		for ( int i = 0; i < mcArray.length; i++ ) {
			for ( int j = 0; j < mcArray[0].length; j++ ) {

				// if MV is larger than threshold, then go ahead and replace
				if ( Math.abs( mcArray[i][j][1] ) > ( thresholds[1] )
						|| Math.abs( mcArray[i][j][2] ) > ( thresholds[1] ) ) {

					System.out.println( "mv larger than threshold[i,j=" + i
							+ "," + j + "]" );
					coordinateNeighborStaticBlk = sequentiallySearchStaticNeighborBlk(
							mcArray, j, i, 2, macroBlkSize, thresholds[2] );
					System.out.println( "naighbor static block: "
							+ coordinateNeighborStaticBlk[0] + ", "
							+ coordinateNeighborStaticBlk[1] );
					replaceABlock( copyOftargetImg, targetImg,
							j * macroBlkSize, i * macroBlkSize,
							(int) coordinateNeighborStaticBlk[0],
							(int) coordinateNeighborStaticBlk[1], macroBlkSize );
					// replaceABlock_debug( copyOftargetImg, j*macroBlkSize,
					// i*macroBlkSize, macroBlkSize );
					continue;
				}
			}
		}
		// copyOftargetImg.display( "replaced image" );
		// Thread.sleep( 5000 );
		// get thre
		// int[][] movingObjCoordinates =
		// sequentialySearchStaticNeighborBlk( MC, tx0, ty0, p, macroBlkSize,
		// threshold )

		// scan through macro blks in MC, and find moving objects
	}

//	public void removeMovingObj01_old_w_searchStaticBlk( ImageJr targetImg,
//			ImageJr refImg, String imgNameT, String imgNameRef, int[][][] MC,
//			int macroBlkSize, int p ) throws InterruptedException, IOException
//	{
//		ImageJr copyOftargetImg = targetImg.deep_copy_image_ks();
//		/*
//		 * scan through the img and replace dynamic blocks with the specified
//		 * corresponding block
//		 */
//		// MC( targetImg, imgNameT, refImg, imgNameRef, p, 0, new ImageJr(), MC,
//		// macroBlkSize );
//
//		MCi( targetImg, imgNameT, refImg, imgNameRef, p, 0, new ImageJr(
//				targetImg.getW(), targetImg.getH() ), MC, macroBlkSize );
//
//		// DEBUG
//		System.out.println( "motion compensation[][]:" );
//		for ( int i = 0; i < MC.length; i += macroBlkSize ) {
//			for ( int j = 0; j < MC[0].length; j += macroBlkSize ) {
//				System.out.print( "[ " + MC[i][j][0] + ", " + MC[i][j][1]
//						+ ", " + MC[i][j][2] + " ] " );
//			}
//			System.out.println();
//		}
//
//		int[] thresholds = new int[3];
//		float[] coordinateNeighborStaticBlk = new float[2];
//		thresholds = getThresholds( MC, (float) .9999, (float) .1, (float) .3,
//				macroBlkSize );
//
//		thresholds[0] = 50;// dynamic
//		thresholds[1] = 0;// mv
//		thresholds[2] = 4;// neightbor static blk
//
//		System.out.println( "threshold diff dynamic: " + thresholds[0] );
//		System.out.println( "threshold mv: " + thresholds[1] );
//		System.out.println( "threshold diff static: " + thresholds[2] );
//
//		// boolean isDynamic = false;
//		float avgMV = 0, avgDiff = 0, sumDiff = 0;
//
//		for ( int i = 0; i < MC.length; i += macroBlkSize ) {
//			sumDiff = 0;
//			for ( int j = 0; j < MC[0].length; j += macroBlkSize ) {
//
//				// 1. is this macroblock dynamic?
//
//				// if MV is larger than threshold, then go ahead and replace
//				// the block
//				avgMV = ( Math.abs( MC[i][j][1] ) + Math.abs( MC[i][j][2] ) ) / 2;
//				if ( avgMV > ( thresholds[1] ) + 1
//						|| avgMV < ( thresholds[1] ) - 1 ) {
//
//					coordinateNeighborStaticBlk = sequentiallySearchStaticNeighborBlk(
//							MC, j, i, 2, macroBlkSize, thresholds[2] );
//
//					// DEBUG
//					System.out.println( "[@x,y=" + j + "," + i
//							+ "] avgMV > thresholds[1]<--" + thresholds[1]
//							+ ": " + avgMV );
//					System.out.println( "replace[" + j + "," + i + "] with ["
//							+ coordinateNeighborStaticBlk[0] + ","
//							+ coordinateNeighborStaticBlk[1] + "]" );
//
//					replaceABlock( copyOftargetImg, targetImg, j, i,
//							(int) coordinateNeighborStaticBlk[0],
//							(int) coordinateNeighborStaticBlk[1], macroBlkSize );
//					continue;
//				} else {
//
//					// get the avg diff of the current macroblock
//					for ( int k0 = 0; k0 < macroBlkSize; k0++ ) {
//						for ( int k1 = 0; k1 < macroBlkSize; k1++ ) {
//							sumDiff += MC[i + k0][j + k1][0];
//						}
//					}
//					avgDiff = sumDiff / ( macroBlkSize * macroBlkSize );
//
//					// replace if the block contains moving obj
//					if ( avgDiff > thresholds[0] ) {
//
//						// DEBUG
//						System.out.println( "[@x,y=" + j + "," + i
//								+ "] avgDff > thresholds[0]<--" + thresholds[0]
//								+ ": " + avgDiff );
//
//						coordinateNeighborStaticBlk = sequentiallySearchStaticNeighborBlk(
//								MC, j, i, 2, macroBlkSize, thresholds[2] );
//
//						replaceABlock( copyOftargetImg, targetImg, j, i,
//								(int) coordinateNeighborStaticBlk[0],
//								(int) coordinateNeighborStaticBlk[1],
//								macroBlkSize );
//					}
//				}
//			}
//		}
//		// copyOftargetImg.display( "replaced image" );
//		// Thread.sleep( 5000 );
//		// get thre
//		// int[][] movingObjCoordinates =
//		// sequentialySearchStaticNeighborBlk( MC, tx0, ty0, p, macroBlkSize,
//		// threshold )
//
//		// scan through macro blks in MC, and find moving objects
//	}

	public void removeMovingObj02( String imgDirName, ImageJr targetImg, ImageJr refImg,
			String imgNameT, String imgNameRef, int macroBlkSize, int p )
			throws InterruptedException, IOException
	{
		ImageJr copyOftargetImg = targetImg.deep_copy_image_ks();
		List<MotionCompensation> mcList = MC_regular( targetImg, imgNameT,
				refImg, imgNameRef, p, 0, new ImageJr( targetImg.getW(),
						targetImg.getH() ), macroBlkSize, false );

		float[] thresholds = new float[3];
		float[] coordinateNeighborStaticBlk = new float[2];
		thresholds = getThresholds( mcList, (float) .75, (float) .1,
				(float) .3, macroBlkSize );

		// System.out.println("inside removeMovingObj01");
		// printMC( mcList, macroBlkSize, targetImg.getW(), targetImg.getW() );

		int[] paddedImgSize = new int[2];
		targetImg.paddedSize( macroBlkSize, paddedImgSize );
		// thresholds[0] = 100;// dynamic
		// thresholds[1] = 0;// mv
		// thresholds[2] = 4;// neighbor static blk

		System.out.println( "threshold diff dynamic: " + thresholds[0] );
		System.out.println( "threshold mv: " + thresholds[1] );
		System.out.println( "threshold diff static: " + thresholds[2] );

		float[][][] mcArray = mcListTo3dArray( mcList, paddedImgSize[0]
				/ macroBlkSize, paddedImgSize[1] / macroBlkSize );
		// print3DArrayF( mcArray );
		float avgDiff = 0;

		for ( int i = 0; i < mcArray.length; i++ ) {
			for ( int j = 0; j < mcArray[0].length; j++ ) {

				// if MV is larger than threshold, then go ahead and replace
				if ( Math.abs( mcArray[i][j][1] ) > ( thresholds[1] )
						|| Math.abs( mcArray[i][j][2] ) > ( thresholds[1] ) ) {

//					System.out.println( "mv larger than threshold[i,j=" + i
//							+ "," + j + "]" );
//					coordinateNeighborStaticBlk = sequentiallySearchStaticNeighborBlk(
//							mcArray, j, i, 2, macroBlkSize, thresholds[2] );
//					System.out.println( "naighbor static block: "
//							+ coordinateNeighborStaticBlk[0] + ", "
//							+ coordinateNeighborStaticBlk[1] );
					replaceABlock( imgDirName, copyOftargetImg, j * macroBlkSize, i
							* macroBlkSize, macroBlkSize );
				}
			}
		}
		 copyOftargetImg.display( "replaced image" );
		 Thread.sleep( 5000 );
	}

//	public void removeMovingObj02_old( ImageJr targetImg, ImageJr refImg,
//			String imgNameT, String imgNameRef, int[][][] MC, int macroBlkSize,
//			int p ) throws InterruptedException, IOException
//	{
//		ImageJr staticImg = new ImageJr( "Walk_005.ppm" );
//		ImageJr copyOftargetImg = targetImg.deep_copy_image_ks();
//		/*
//		 * scan through the img and replace dynamic blocks with the specified
//		 * corresponding block
//		 */
//		MCi( targetImg, imgNameT, refImg, imgNameRef, p, 0, new ImageJr(), MC,
//				macroBlkSize );
//
//		// DEBUG
//		System.out.println( "motion compensation[][]:" );
//		for ( int i = 0; i < MC.length; i += macroBlkSize ) {
//			for ( int j = 0; j < MC[0].length; j += macroBlkSize ) {
//				System.out.print( "[ " + MC[i][j][0] + ", " + MC[i][j][1]
//						+ ", " + MC[i][j][2] + " ] " );
//			}
//			System.out.println();
//		}
//
//		int[] thresholds = new int[3];
//		int[] coordinateNeighborStaticBlk = new int[2];
//		thresholds = getThresholds( MC, (float) .9999, (float) .1, (float) .3,
//				macroBlkSize );
//
//		thresholds[0] = 50;// dynamic
//		thresholds[1] = 0;// mv
//		thresholds[2] = 4;// neightbor static blk
//
//		System.out.println( "threshold diff dynamic: " + thresholds[0] );
//		System.out.println( "threshold mv: " + thresholds[1] );
//		System.out.println( "threshold diff static: " + thresholds[2] );
//
//		// boolean isDynamic = false;
//		int avgMV = 0, avgDiff = 0, sumDiff = 0;
//
//		for ( int i = 0; i < MC.length; i += macroBlkSize ) {
//			sumDiff = 0;
//			for ( int j = 0; j < MC[0].length; j += macroBlkSize ) {
//
//				// 1. is this macroblock dynamic?
//
//				// if MV is larger than threshold, then go ahead and replace
//				// the block
//				avgMV = ( Math.abs( MC[i][j][1] ) + Math.abs( MC[i][j][2] ) ) / 2;
//				if ( avgMV > ( thresholds[1] ) + 1
//						|| avgMV < ( thresholds[1] ) - 1 ) {
//
//					// coordinateNeighborStaticBlk =
//					// sequentiallySearchStaticNeighborBlk(
//					// MC, j, i, 2, macroBlkSize,
//					// thresholds[2] );
//
//					replaceABlock( copyOftargetImg, staticImg, j, i, j, i,
//							macroBlkSize );
//					continue;
//				} else {
//
//					// get the avg diff of the current macroblock
//					for ( int k0 = 0; k0 < macroBlkSize; k0++ ) {
//						for ( int k1 = 0; k1 < macroBlkSize; k1++ ) {
//							sumDiff += MC[i + k0][j + k1][0];
//						}
//					}
//					avgDiff = sumDiff / ( macroBlkSize * macroBlkSize );
//
//					// replace if the block contains moving obj
//					if ( avgDiff > thresholds[0] ) {
//
//						// DEBUG
//						System.out.println( "[@x,y=" + j + "," + i
//								+ "] avgDff > thresholds[0]<--" + thresholds[0]
//								+ ": " + avgDiff );
//
//						// coordinateNeighborStaticBlk =
//						// sequentiallySearchStaticNeighborBlk(
//						// MC, j, i, 2, macroBlkSize,
//						// thresholds[2] );
//
//						replaceABlock( copyOftargetImg, targetImg, j, i, j, i,
//								macroBlkSize );
//					}
//				}
//			}
//		}
//	}

	public void replaceABlock( ImageJr targetImgNew, ImageJr targetImgOld,
			int tx, int ty, int rx, int ry, int macroBlkSize )
			throws InterruptedException
	{
		int[] rgb = new int[3];
		for ( int i = 0; i < macroBlkSize; i++ ) {
			for ( int j = 0; j < macroBlkSize; j++ ) {

				targetImgOld.getPixel( rx + j, ry + i, rgb );
				targetImgNew.setPixel( tx + j, ty + i, rgb );
			}
		}
		targetImgNew.display( "Altered image" );
		Thread.sleep( 5000 );
	}

	/**
	 * replacement with the block in the given 5th frame
	 * 
	 * @throws InterruptedException
	 */
	public void replaceABlock( String imgDirName, ImageJr targetImgNew, int tx, int ty,
			int macroBlkSize ) throws InterruptedException
	{

		ImageJr fifthFrmImg = new ImageJr( imgDirName+"\\Walk_005.ppm" );
		int[] rgb = new int[3];

		for ( int i = 0; i < macroBlkSize; i++ ) {
			for ( int j = 0; j < macroBlkSize; j++ ) {
				fifthFrmImg.getPixel( tx + j, ty + i, rgb );
				targetImgNew.setPixel( tx + j, ty + i, rgb );
			}
		}

//		targetImgNew.display( "Altered image" );
//		Thread.sleep( 5000 );
	}

	public void replaceABlock_debug( ImageJr targetImgNew, int tx, int ty,
			int macroBlkSize ) throws InterruptedException
	{

		ImageJr dummyImg = new ImageJr( "Ducky.ppm" );
		int[] rgb = new int[3];

		for ( int i = 0; i < macroBlkSize; i++ ) {
			for ( int j = 0; j < macroBlkSize; j++ ) {
				dummyImg.getPixel( 50, 50, rgb );
				targetImgNew.setPixel( tx + j, ty + i, rgb );
			}
		}

		targetImgNew.display( "Altered image" );
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

	public float[][][] mcListTo3dArray( List<MotionCompensation> mcList,
			int imgW, int imgH )
	{

		float[][][] mcArray = new float[imgH][imgW][3];
		int idx = 0;
		for ( int i = 0; i < mcArray.length; i++ ) {
			for ( int j = 0; j < mcArray[0].length; j++ ) {
				mcArray[i][j][0] = mcList.get( idx ).getResidual();
				mcArray[i][j][1] = mcList.get( idx ).getMvX();
				mcArray[i][j][2] = mcList.get( idx ).getMvY();
				idx++;
			}
		}
		return mcArray;
	}
}
