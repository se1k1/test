import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Task3 extends Prep {

	public Task3()
	{
		// TODO Auto-generated constructor stub
	}

	// trash
	public Similarity[] getTop3SimilarFrames_sml( int p, int macroBlkSize,
			int targetNum ) throws InterruptedException, IOException
	{

		Similarity[] similarities = new Similarity[5];

		String imgNameT = "", imgNameRef = "";
		ImageJr targetImg, referenceImg, residualImg = null;

		if ( targetNum < 10 ) {
			imgNameT = "Walk_00" + targetNum + ".ppm";
		} else if ( targetNum < 100 ) {
			imgNameT = "Walk_0" + targetNum + ".ppm";
		} else {
			imgNameT = "Walk_" + targetNum + ".ppm";
		}
		targetImg = new ImageJr( imgNameT );
		int[] paddedSize = new int[2];
		targetImg.paddedSize( macroBlkSize, paddedSize );
		float[][][] MC = new float[paddedSize[1]][paddedSize[0]][3];
		// loop through all IDB to get 3 most similar frames
		for ( int i = 1; i <= similarities.length; i++ ) {
			if ( i == targetNum ) {
				continue;
			}

			if ( i < 10 ) {
				imgNameRef = "Walk_00" + i + ".ppm";
			} else if ( i < 100 ) {
				imgNameRef = "Walk_0" + i + ".ppm";
			} else {
				imgNameRef = "Walk_" + i + ".ppm";
			}

			referenceImg = new ImageJr( imgNameRef );

			MC( targetImg, imgNameT, referenceImg, imgNameRef, p, 0,
					residualImg, MC, macroBlkSize );
			System.out.println( "i=" + i + ": "
					+ measureSimilarity( MC, targetNum, i ) );
			similarities[i - 1] = measureSimilarity( MC, targetNum, i );
			System.out.println( similarities[i - 1] );
		}

		// DEBUG
		// System.out.println( "motion compensation[][]:" );
		// for ( int i = 0; i < MC.length; i += macroBlkSize ) {
		// for ( int j = 0; j < MC[0].length; j += macroBlkSize ) {
		// System.out.print( "[ " + MC[i][j][0] + ", " + MC[i][j][1]
		// + ", " + MC[i][j][2] + " ] " );
		// }
		// System.out.println();
		// }

		Arrays.sort( similarities );

		for ( Similarity s : similarities ) {
			System.out.println( s );
		}
		System.out.println( similarities.length );
		if ( similarities.length >= 2 ) {
			System.out.println( "similarities.length > 2" );
			Similarity[] simTop3 = new Similarity[3];

			for ( int i = 0; i < 3; i++ ) {
				simTop3[i] = similarities[i];
				System.out.print( simTop3[i] + " " );
			}
			return simTop3;
		} else
			return null;

	}

	/**
	 * return value: top3[row][0]=frame name, top3[row][1]=simPercentage
	 * percentage
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public List<Similarity> getTop3SimilarFrames_ret_list( int p,
			int macroBlkSize ) throws InterruptedException, IOException
	{

		Scanner sc = new Scanner( System.in );
		// System.out
		// .print(
		// "Please enter the name of the directory where images are stored: " );
		// // String directoryName = sc.nextLine();
		// DEBUG
		String directoryName = "IDB";

		// System.out
		// .print(
		// "Please enter the name of the target image to be compared against (e.g. Wal_055.ppm ): "
		// );
		// String imgNameT = sc.nextLine();
		String imgNameT = "Walk_005.ppm";
		// DEBUG
		// readFilesFromADirectory( "IDB" );
		// readFilesFromADirectory( directoryName );
		File directory = new File( directoryName );
		File[] imageData = directory.listFiles();

		Similarity[] similarities = new Similarity[200];
		String imgNameRef = "";
		ImageJr targetImg, referenceImg, residualImg = null;
		targetImg = new ImageJr( directoryName + "\\" + imgNameT );
		int[] paddedSize = new int[2];
		targetImg.paddedSize( macroBlkSize, paddedSize );
		float[][][] MC = new float[paddedSize[1]][paddedSize[0]][3];
		int targetNum = getIndexOfTargetImage( imgNameT, imageData );

		// loop through all IDB to get 3 most similar frames
		for ( int i = 0; i < similarities.length; i++ ) {

			if ( i == targetNum ) {
				similarities[i] = new Similarity( Integer.MAX_VALUE,
						Integer.MAX_VALUE, i, i );
				continue;
			}

			imgNameRef = "IDB/" + imageData[i].getName();
			referenceImg = new ImageJr( imgNameRef );

			MC( targetImg, imgNameT, referenceImg, imgNameRef, p, 0,
					residualImg, MC, macroBlkSize );
			similarities[i] = measureSimilarity( MC, targetNum, i );
		}
		for ( int i = 1; i <= similarities.length; i++ ) {
			if ( i == targetNum ) {
				// if
			}
		}

		Arrays.sort( similarities );

		for ( Similarity s : similarities ) {
			System.out.println( s );
		}

		System.out.println( similarities.length );
		if ( similarities.length >= 3 ) {

			List<Similarity> simTop3 = new LinkedList<Similarity>();

			for ( int i = 0; i < 3; i++ ) {
				simTop3.add( similarities[i] );
			}
			return simTop3;
		} else
			System.out.println( "" );
		return null;
	}

	/**
	 * return value: top3[row][0]=frame name, top3[row][1]=simPercentage
	 * percentage
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void getTop3SimilarFrames( int p, int macroBlkSize )
			throws InterruptedException, IOException
	{
		Scanner sc = new Scanner( System.in );
		// System.out
		// .print(
		// "Please enter the name of the directory where images are stored: " );
		// // String directoryName = sc.nextLine();
		// DEBUG
		String directoryName = "IDB";

		// System.out
		// .print(
		// "Please enter the name of the target image to be compared against (e.g. Wal_055.ppm ): "
		// );
		// String imgNameT = sc.nextLine();
		String imgNameT = "Walk_005.ppm";
		// DEBUG
		// readFilesFromADirectory( "IDB" );
		// readFilesFromADirectory( directoryName );
		File directory = new File( directoryName );
		File[] imageData = directory.listFiles();

		Similarity[] similarities = new Similarity[200];
		String imgNameRef = "";
		ImageJr targetImg, referenceImg, residualImg = null;
		targetImg = new ImageJr( directoryName + "\\" + imgNameT );
		int[] paddedSize = new int[2];
		targetImg.paddedSize( macroBlkSize, paddedSize );
		float[][][] MC = new float[paddedSize[1]][paddedSize[0]][3];
		int targetNum = getIndexOfTargetImage( imgNameT, imageData );

		// loop through all IDB to get 3 most similar frames
		for ( int i = 0; i < similarities.length; i++ ) {

			if ( i == targetNum ) {
				similarities[i] = new Similarity( Integer.MAX_VALUE,
						Integer.MAX_VALUE, i, i );
				continue;
			}

			imgNameRef = "IDB/" + imageData[i].getName();
			referenceImg = new ImageJr( imgNameRef );

			MC( targetImg, imgNameT, referenceImg, imgNameRef, p, 0,
					residualImg, MC, macroBlkSize );
			similarities[i] = measureSimilarity( MC, targetNum, i );
		}

		Arrays.sort( similarities );

		for ( Similarity s : similarities ) {
			System.out.println( s );
		}

		System.out.println( "Input image name: " + imgNameT );
		System.out.println( "Half-pixel accuracy (optional): " + "Yes" );
		System.out.print( "Top 6 similar images in the DB:" );

		Similarity tempSim;
		double tempSimPercentage = 0;

		for ( int i = 0; i < 6; i++ ) {
			tempSim = similarities[i];
			System.out.print( "\n"
					+ imageData[tempSim.getReferenceFrmIdx()].getName()
					+ " --- " );
			tempSimPercentage = calculateDiffPercentage01( similarities,
					tempSim.getReferenceFrmIdx() );
			System.out.format( "%2f", tempSimPercentage );

		}
	}

	/**
	 * DEBUG --- to be deleted
	 */
	public void getTop6_test( int p, int macroBlkSize )
			throws InterruptedException, IOException
	{

		String directoryName = "testIDB";
		String imgNameT = "Walk_005.ppm";
		File directory = new File( directoryName );
		File[] imageData = directory.listFiles();
		Similarity[] similarities = new Similarity[imageData.length];
		String imgNameRef = "";
		ImageJr targetImg, referenceImg, residualImg = null;
		targetImg = new ImageJr( directoryName + "\\" + imgNameT );
		int[] paddedSize = new int[2];
		targetImg.paddedSize( macroBlkSize, paddedSize );
		float[][][] MC = new float[paddedSize[1]][paddedSize[0]][3];
		int targetNum = getIndexOfTargetImage( imgNameT, imageData );

		// loop through all IDB to get 6 most similar frames
		for ( int i = 0; i < imageData.length; i++ ) {
			if ( i == targetNum ) {
				similarities[i] = new Similarity( Integer.MAX_VALUE,
						Integer.MAX_VALUE, i, i );
				continue;
			}

			imgNameRef = "testIDB/" + imageData[i].getName();
			referenceImg = new ImageJr( imgNameRef );

			MC( targetImg, imgNameT, referenceImg, imgNameRef, p, 0,
					residualImg, MC, macroBlkSize );
			similarities[i] = measureSimilarity( MC, targetNum, i );
		}

		Arrays.sort( similarities );

		for ( Similarity s : similarities ) {
			System.out.println( s );
		}

		System.out.println( "Input image name: " + imgNameT );
		System.out.println( "Half-pixel accuracy (optional): " + "Yes" );
		System.out.print( "Top 6 similar images in the DB:" );

		Similarity tempSim;
		double tempSimPercentage = 0;

		for ( int i = 0; i < 6; i++ ) {
			tempSim = similarities[i];
			System.out.print( "\n"
					+ imageData[tempSim.getReferenceFrmIdx()].getName()
					+ " --- " );
			tempSimPercentage = calculateDiffPercentage01( similarities,
					tempSim.getReferenceFrmIdx() );
			System.out.format( "%2f", tempSimPercentage );

		}
	}

	// relative measure
	public double calculateDiffPercentage01( Similarity[] similarities, int idxR )
	{
		double closest, furthest, curr;
		int idxCurr = getIndexOfTargetImage( idxR, similarities );


		// the most similar
		closest = ( similarities[0].getMv() + 1 ) * similarities[0].getDiff();

		// the least similar
		furthest = ( similarities[similarities.length - 2].getMv() + 1 )
				* similarities[similarities.length - 2].getDiff();

		// current value

		curr = ( similarities[idxCurr].getMv() + 1 )
				* similarities[idxCurr].getDiff();
		curr = ( similarities[0].getReferenceFrmIdx() == curr ) ? closest
				: curr;
		curr = ( similarities[similarities.length - 2].getReferenceFrmIdx() == curr ) ? furthest
				: curr;
		curr = Math.abs( curr );

		return 1-(( curr - closest ) / ( furthest - closest ));
	}

	// absolute measure
	// public int calculateDiffPercentage02( Similarity sim, int idxR )
	// {
	// int percentage = 0;
	//
	// return percentage;
	// }

	public int getIndexOfTargetImage( String imgNameT, File[] imgDirectory )
	{
		for ( int i = 0; i < imgDirectory.length; i++ ) {
			if ( imgDirectory[i].getName().equals( imgNameT ) ) {
				return i;
			}
		}
		return -1;
	}

	public int getIndexOfTargetImage( int referenceFrmIdx,
			Similarity[] imgDirectory )
	{
		for ( int i = 0; i < imgDirectory.length; i++ ) {
			if ( imgDirectory[i].getReferenceFrmIdx() == referenceFrmIdx ) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * sim[0]=mv, sim[1]=diff, sim[2]=distFrm, sim[3]=target frame index ,
	 * sim[4]=reference frame index
	 */
	public Similarity measureSimilarity( float[][][] mC, int frameIdxT,
			int frameIdxR )
	{
		/*
		 * 1st attempt: - MV similarity value = abs(avg(x, y)). The smaller the
		 * value is, the - better.(first priority) - Diff similarity value =
		 * simply the sum of all pixel value. The - smaller the value is, the
		 * better.
		 */
		float sumMV = 0, sumDiff = 0;
		for ( int i = 0; i < mC.length; i++ ) {
			for ( int j = 0; j < mC[0].length; j++ ) {

				// compute mv sim
				sumMV = sumMV + Math.abs( mC[i][j][1] )
						+ Math.abs( mC[i][j][2] );

				// compute diff sim
				sumDiff += mC[i][j][0];
			}
		}

		/** sim[0]=mv, sim[1]=diff, sim[2]=distFrm, sim[3]=reference frame index */
		// int[] sim = new int[4];
		// sim[0] = sumMV;
		// sim[1] = sumDiff;
		// sim[2] = Math.abs( frameIdxT - frameIdxR );
		// sim[3] = frameIdxR;

		Similarity sim = new Similarity( sumMV, sumDiff, frameIdxT, frameIdxR );

		return sim;
		// return (float) ( sim[0] * .5 + sim[1] * .4 + sim[2] * .1 );

	}

	public int[][] deepCopy2dArray( int[][] oldArray )
	{
		int[][] newArray = new int[oldArray.length][oldArray[0].length];
		for ( int i = 0; i < newArray.length; i++ ) {
			for ( int j = 0; j < newArray[0].length; j++ ) {
				newArray[i][j] = oldArray[i][j];
			}
		}
		return newArray;

	}

	public void readFilesFromADirectory( String directoryName )
	{

		File directory = new File( directoryName );
		File[] imageData = directory.listFiles();

		for ( int i = 0; i < imageData.length; i++ ) {
			if ( imageData[i].isFile() ) {
				System.out.println( "File " + imageData[i].getName() );
			} else if ( imageData[i].isDirectory() ) {
				System.out.println( "Directory " + imageData[i].getName() );
			}
		}
		System.out.println( imageData.length );
	}
}
