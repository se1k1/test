
/*
 * 
 * 	public ImageJr mapResidual( ImageJr residual )
	{
		ImageJr mappedResidual = new ImageJr( residual.getW(), residual.getH() );
		float avg = getAvgPixValue( residual );
		int value = 0;
		for ( int i = 0; i < residual.getH(); i++ ) {
			for ( int j = 0; j < residual.getW(); j++ ) {
				value = residual.getR( j, i ) > avg ? 255 : 0;
				mappedResidual.setPixel( j, i, value );
			}
		}
		return mappedResidual;
	}
 * */
//
//public class CodeStorage {
//
//	public CodeStorage()
//	{
//		// TODO Auto-generated constructor stub
//	}
//	public void MC_MAD( ImageJr targetImg, String imgnamgeT,
//			ImageJr referenceImg, String imgnamgeRef, int p,
//			int matchingCriteria, ImageJr residualImg,
//			int[][][] motionCompensation, int macroBlkSize )
//			throws InterruptedException
//	{
//		ImageJr padTarget = targetImg.padImage();
//		ImageJr padRef = referenceImg.padImage();
//		ImageJr errorImg = new ImageJr( padTarget.getW(), padTarget.getH() );
//		int[][] targetFrm = padTarget.imageJrTo2DArray();
//		int[][] referenceFrm = padRef.imageJrTo2DArray();
//		/* [0] = error value, [1] = motion vector x, [2] = motion vector y */
//		motionCompensation = new int[padTarget.getH()][padTarget.getW()][3];
//		ReferenceFrameBlock bestMatch = new ReferenceFrameBlock();
//
//		for ( int y = 0; y < targetFrm.length; y++ ) {
//			for ( int x = 0; x < targetFrm[y].length; x++ ) {
//
//				if ( x % macroBlkSize == 0 && y % macroBlkSize == 0 ) {
//
//					// find predicted block
//					bestMatch = sequentialSearchMAD( targetFrm, referenceFrm,
//							x, y, p, macroBlkSize );
//
//					// store motion vector x
//					motionCompensation[y][x][1] = x - bestMatch.getxTopLeft();
//
//					// store motion vector y
//					motionCompensation[y][x][2] = y - bestMatch.getyTopLeft();
//				}
//
//				// store error_pixel_value
//				if ( ( bestMatch.getxTopLeft() + x % macroBlkSize ) < referenceFrm[0].length
//						&& ( bestMatch.getxTopLeft() + x % macroBlkSize ) < referenceFrm.length ) {
//					motionCompensation[y][x][0] = targetFrm[y][x]
//							- referenceFrm[bestMatch.getxTopLeft() + y
//									% macroBlkSize][bestMatch.getxTopLeft() + x
//									% macroBlkSize];
//				}
//				// DEBUG
//				System.out.println( "MC(): x,y=" + x + "," + y );
//				System.out.println( "Best Match: " + bestMatch );
//
//				errorImg.setPixel( x, y, motionCompensation[y][x][0] );
//			}
//		}
//
//		ImageJr errorDepadded = errorImg.depadImage( targetImg.getW(),
//				targetImg.getH() );
//		// avgPixValue = getAvgPixValue( errorDepadded );
//		ImageJr mappedError = mapResidual( errorDepadded );
//		/*
//		 * - use appropriate search criteria to get the best matching macroblock
//		 * from the reference frame -
//		 */
//
//		System.out.println( "# Name: Kae Sawada" + "\n# Target image name: "
//				+ imgnamgeT + "\n# Reference image name: " + imgnamgeRef
//				+ "\n# Number of target macro blocks: " + targetFrm[0].length
//				/ macroBlkSize + " x " + targetFrm.length / macroBlkSize
//				+ " (image size is " + targetImg.getW() + " x "
//				+ targetImg.getH() + ")" );
//
//		for ( int i = 0; i < motionCompensation.length; i += macroBlkSize ) {
//			for ( int j = 0; j < motionCompensation[0].length; j += macroBlkSize ) {
//				System.out.print( "[ " + motionCompensation[i][j][1] + ", "
//						+ motionCompensation[i][j][2] + " ] " );
//			}
//			System.out.println();
//		}
//
//		/*
//		 * [ 0, 0] [ 1, 1] [ 10, 10] [ 0, 0] [ 0, 1] [ 1, 1] [ 0, 0] [ 0, -1] [
//		 * -1, 0] [ 1, 1] [ 10, 0] [ -4, 1]
//		 */
//
//		// mappedError.display( "error image" );
//		// Thread.sleep( 5000 );
//	}// note that macroblock sizse would affect the compression ratio
//
//}
