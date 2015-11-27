public class HelperMethod {

	public HelperMethod()
	{
		// TODO Auto-generated constructor stub
	}

	public void pringArray2D( int min, int max, int[][] array )
	{
		for ( int i = min; i <= max; i++ ) {
			for ( int j = min; j < max; j++ ) {
				System.out.println( array[j][i] + " " );
			}
			System.out.println();
		}
	}
	
	public void printImageJrPixelValues(ImageJr img){
		
		for ( int i = 0; i < img.getH(); i++ ) {
			for ( int j = 0; j < img.getW(); j++ ) {
				System.out.print(img.getR( j, i )+" ");
			}System.out.println();
		}
	}
}
