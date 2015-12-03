public class MotionCompensation {
	private float mvX=0,mvY=0;
	private float residual;
	private int macroBlkSize = 16;

	public MotionCompensation()
	{
	}

	public MotionCompensation( float mvX, float mvY,
			int macroBlkSize )
	{
		this.mvX = mvX;
		this.mvY = mvY;
		this.macroBlkSize = macroBlkSize;
	}
	
	public MotionCompensation( float mvX, float mvY, float residual,
			int macroBlkSize )
	{
		this.mvX = mvX;
		this.mvY = mvY;
		this.residual = residual;
		this.macroBlkSize = macroBlkSize;
	}

	public float getMvX()
	{
		return mvX;
	}

	public void setMvX( float mvX )
	{
		this.mvX = mvX;
	}

	public float getMvY()
	{
		return mvY;
	}

	public void setMvY( float mvY )
	{
		this.mvY = mvY;
	}

	public float getResidual()
	{
		return residual;
	}

	public void setResidual( float residual )
	{
		this.residual = residual;
	}

	public int getMacroBlkSize()
	{
		return macroBlkSize;
	}

	public void setMacroBlkSize( int macroBlkSize )
	{
		this.macroBlkSize = macroBlkSize;
	}


	



}
