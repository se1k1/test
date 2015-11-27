
public class Task2 extends Prep {
/*  Use IDB and implement the following steps to achieve this task:
	- Select the target image.
		o For the target image, receive from the user a frame number n between 19 and 179. 19~179 frames in the data set include moving people.
	- Select the reference image.
		o Given the input n, use (n-2)th as the reference image. Identify dynamic blocks in the target image by performing the block-based motion estimation and checking motion vector as you have done in the task1.
	- Remove moving object from the target image. Apply two different approaches and produce and display two different result images:
		1. For each dynamic block d, find the closest static block s in the nth frame and replace d with s. After processing all dynamic blocks, display the result.
		2. The 5th frame of test data set does not include any moving objects. Utilize the 5th frame to get static block s. For each dynamic block d, find the static block s from 5th frame and replace d with s. After processing all dynamic blocks, display the result.
	
	See the sample outputs for the result format.
*/
	int frmNumMin=19, frmNumMax=179;
	public Task2 ()
	{
		super();
	}
	
	public Task2 (int frmMin, int frmMax)
	{
		super();
		frmNumMin = frmMin;
		frmNumMax = frmMax;
	}
	
	public int getFrmNumMin()
	{
		return frmNumMin;
	}
	public void setFrmNumMin( int frmNumMin )
	{
		this.frmNumMin = frmNumMin;
	}
	public int getFrmNumMax()
	{
		return frmNumMax;
	}
	public void setFrmNumMax( int frmNumMax )
	{
		this.frmNumMax = frmNumMax;
	}

}
