public class Task3 extends Prep {

	public Task3()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * return value: top3[row][0]=frame index, top3[row][1]=similarity
	 * percentage
	 */
	public int[][] getTop3SimilarFrames()
	{
		int[][] sims = new int[200][4];
		radixSortSims( sims );

		int[][] top3 = new int[3][2];
		return top3;
	}

	public float computeSimPercentage( int[] sim )
	{
		return 0;
	}

	public int[] radixSortSims( int[][] sims )
	{
		int[][] copyOfSims = deepCopy2dArray( sims );
		// run the radix sort
		return null;
	}

	/**
	 * sim[0]=mv, sim[1]=diff, sim[2]=distFrm, sim[3]=target frame index ,
	 * sim[4]=reference frame index
	 */
	public int[] measureSimilarity( int[][][] MC, int frameIdxT, int frameIdxR )
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
		int[] sim = new int[4];
		sim[0] = sumMV;
		sim[1] = sumDiff;
		sim[2] = Math.abs( frameIdxT - frameIdxR );
		sim[3] = frameIdxR;

		return sim;
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
