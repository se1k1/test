package Similarity;

public class Similarity implements Comparable<Similarity> {

	private int mv;
	private int diff;
	// private int frmDistance;
	private int targetFrmIdx;
	private int referenceFrmIdx;

	public Similarity()
	{
	}

	public Similarity( int mv, int diff, int idxT, int idxR )
	{
		super();
		this.mv = mv;
		this.diff = diff;
		// frmDistance = dist;
		targetFrmIdx = idxT;
		referenceFrmIdx = idxR;
	}

	// public int getSimPercentage()
	// {
	// return 0;
	// }
	@Override
	public int compareTo( Similarity o )
	{
		/*
		 * sim[0]=mv, sim[1]=diff, sim[2]=distFrm, sim[3]=target frame index ,
		 * sim[4]=reference frame index
		 */

		// asc
		if ( mv == o.mv ) {

			if ( o.diff == diff ) {
				if ( targetFrmIdx - referenceFrmIdx == o.targetFrmIdx
						- o.referenceFrmIdx ) {
					return referenceFrmIdx-o.referenceFrmIdx;
				}
				return Math.abs( targetFrmIdx - referenceFrmIdx )
						- Math.abs( o.targetFrmIdx - o.referenceFrmIdx );
			}
			return diff - o.diff;
		}
		return mv - o.mv;
	}

	@Override
	public String toString()
	{
		return "Similarity [mv=" + mv + ", diff=" + diff + ", targetFrmIdx="
				+ targetFrmIdx + ", frm dist="+Math.abs(referenceFrmIdx-targetFrmIdx)+", referenceFrmIdx=" + referenceFrmIdx + "]";
	}

}
