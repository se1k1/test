public class MotionCompensation {
	private int[] motionVector = new int[2];
	private int[][] residual;
	private int macroBlkSize = 16;

	public MotionCompensation()
	{
	}

	// public MotionCompensation( int[] motionVector, ReferenceFrameBlock rfBlk,
	// int macroBlkSize )
	// {
	// this.motionVector = motionVector;
	// this.residual = residual;
	// this.macroBlkSize = macroBlkSize;
	// }

	public MotionCompensation( int[] motionVector, int[][] residual,
			int macroBlkSize )
	{
		this.motionVector = motionVector;
		this.residual = residual;
		this.macroBlkSize = macroBlkSize;
	}

	public int[] getMotionVector()
	{
		return motionVector;
	}

	public void setMotionVector( int[] motionVector )
	{
		this.motionVector = motionVector;
	}

	public int[][] getResidual()
	{
		return residual;
	}

	public void setResidual( int[][] residual )
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
