

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

	public int getMv()
	{
		return mv;
	}

	public void setMv( int mv )
	{
		this.mv = mv;
	}

	public int getDiff()
	{
		return diff;
	}

	public void setDiff( int diff )
	{
		this.diff = diff;
	}

	public int getTargetFrmIdx()
	{
		return targetFrmIdx;
	}

	public void setTargetFrmIdx( int targetFrmIdx )
	{
		this.targetFrmIdx = targetFrmIdx;
	}

	public int getReferenceFrmIdx()
	{
		return referenceFrmIdx;
	}

	public void setReferenceFrmIdx( int referenceFrmIdx )
	{
		this.referenceFrmIdx = referenceFrmIdx;
	}

	@Override
	public int compareTo( Similarity o )
	{
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
