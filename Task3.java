import java.io.IOException;
import java.util.Arrays;

public class Task3 extends Prep {

	public Task3()
	{
		// TODO Auto-generated constructor stub
	}

	public Similarity[] getTop3SimilarFrames_sml( int p, int macroBlkSize,
			int targetNum ) throws InterruptedException, IOException
	{

		Similarity[] similarities = new Similarity[2];

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
		int[] paddedSize = new int[3];
		targetImg.paddedSize( macroBlkSize, paddedSize );
		int[][][] MC = new int[paddedSize[1]][paddedSize[0]][3];
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
System.out.println(similarities.length);
		if ( similarities.length > 2 ) {
			System.out.println("similarities.length > 2");
			Similarity[] simTop3 =
			{ similarities[0], similarities[1], similarities[2] };
			return simTop3;
		} else
			return null;

	}

	/**
	 * return value: top3[row][0]=frame index, top3[row][1]=simPercentage
	 * percentage
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Similarity[] getTop3SimilarFrames( int p, int macroBlkSize,
			int targetNum ) throws InterruptedException, IOException
	{

		Similarity[] simlarities = new Similarity[200];

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
		int[][][] MC = new int[paddedSize[1]][paddedSize[0]][3];
		// loop through all IDB to get 3 most similar frames
		for ( int i = 1; i < simlarities.length; i++ ) {
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
			simlarities[i] = measureSimilarity( MC, targetNum, i );

		}

		Arrays.sort( simlarities );
		Similarity[] simTop3 =
		{ simlarities[0], simlarities[1], simlarities[2] };
		return simTop3;
	}

	/**
	 * sim[0]=mv, sim[1]=diff, sim[2]=distFrm, sim[3]=target frame index ,
	 * sim[4]=reference frame index
	 */
	public Similarity measureSimilarity( int[][][] MC, int frameIdxT,
			int frameIdxR )
	{
		/*
		 * 1st attempt: - MV similarity value = abs(avg(x, y)). The smaller the
		 * value is, the - better.(first priority) - Diff similarity value =
		 * simply the sum of all pixel value. The - smaller the value is, the
		 * better.
		 */
		int sumMV = 0, sumDiff = 0;
		for ( int i = 0; i < MC.length; i++ ) {
			for ( int j = 0; j < MC[0].length; j++ ) {

				// compute mv sim
				sumMV = sumMV + Math.abs( MC[i][j][1] )
						+ Math.abs( MC[i][j][2] );

				// compute diff sim
				sumDiff += MC[i][j][0];
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
}
