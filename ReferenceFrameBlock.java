public class ReferenceFrameBlock {
	private int xTopLeft = 0, yTopLeft = 0;
	private float diffValue = 0;

	public ReferenceFrameBlock()
	{
	}

	public ReferenceFrameBlock( int x, int y, float diffVal )
	{
		xTopLeft = x;
		yTopLeft = y;
		diffValue = diffVal;
	}

	public int getxTopLeft()
	{
		return xTopLeft;
	}

	public void setxTopLeft( int xTopLeft )
	{
		this.xTopLeft = xTopLeft;
	}

	public int getyTopLeft()
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
		return "Re<" + xTopLeft + ", "
				+ yTopLeft + "> diffValue=" + diffValue;
	}
	
	public void displayRefFrmBlk(int macroBlkSize, Image refImg){
		ImageJr img = new ImageJr(macroBlkSize, macroBlkSize);
	}
	

}
