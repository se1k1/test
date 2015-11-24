import static org.junit.Assert.*;

import org.junit.Test;

public class testStorage {

	@Test
	public void test()
	{
		fail( "Not yet implemented" );
	}

	// @Test
	public void test_howlarge_is_16by16() throws InterruptedException
	{
		Prep pp = new Prep();
		Image img = new ImageJr( 16, 16 );

		img.display( "16 x 16" );
		Thread.sleep( 2000 );
	}

	// @Test
	public void test_togray() throws InterruptedException
	{
		Prep pp = new Prep();
		Image img = new ImageJr( "Dune.ppm" );
		int[][] ia = pp.toGray( img );
		Image grayimg = ( (ImageJr) img ).array2DtoImageJr( ia );
		grayimg.display( "gray dune" );
		Thread.sleep( 2000 );
	}

	// @Test
	public void test_square2dArray()
	{
		Prep pr = new Prep();
		pr.square2dArray( 6 );
	}

}
