public class ReferenceFrameBlock {
	//private int xTopLeft = 0, yTopLeft = 0;
	private float xTopLeft = 0, yTopLeft = 0;

	private float diffValue = 0;
	static int idSeed = 0;
	private int refFrmId = 0;

	public ReferenceFrameBlock()
	{
		// refFrmId = idSeed++;
	}

	public ReferenceFrameBlock( int x, int y )
	{
		xTopLeft = x;
		yTopLeft = y;
		refFrmId = idSeed++;
	}

	public ReferenceFrameBlock( float x, float y )
	{
		xTopLeft = x;
		yTopLeft = y;
		refFrmId = idSeed++;
	}

	public ReferenceFrameBlock( int x, int y, float diffVal )
	{
		xTopLeft = x;
		yTopLeft = y;
		diffValue = diffVal;
		refFrmId = idSeed++;
	}
	public ReferenceFrameBlock( float x, float y, float diffVal )
	{
		xTopLeft = x;
		yTopLeft = y;
		diffValue = diffVal;
		refFrmId = idSeed++;
	}

	public float getxTopLeft()
	{
		return xTopLeft;
	}

	public void setxTopLeft( int xTopLeft )
	{
		this.xTopLeft = xTopLeft;
	}

	public float getyTopLeft()
	{
		return yTopLeft;
	}

	public void setyTopLeft( int yTopLeft )
	{
		this.yTopLeft = yTopLeft;
	}

	public float getDiffValue()
	{
		return diffValue;
	}

	public void setDiffValue( float diffValue )
	{
		this.diffValue = diffValue;
	}

	@Override
	public String toString()
	{
		return "Re<" + xTopLeft + ", " + yTopLeft + "> diffValue=" + diffValue;
	}

	public void displayRefFrmBlk( int macroBlkSize, Image refImg )
	{
		ImageJr img = new ImageJr( macroBlkSize, macroBlkSize );
	}

}
