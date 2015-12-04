import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/*******************************************************
 * CS451 Multimedia Software Systems HW 4 @ Author: Kae Sawada
 *******************************************************/
public class CS451_Sawada {

	static String inputFileName = "";

	public static void main( String[] args ) throws InterruptedException,
			IOException
	{
		// if ( args.length != 1 ) {
		// usage();
		// System.exit( 1 );
		// }
		// String homeworkNum = args[0];
		// if ( !homeworkNum.equals( "4" ) ) {
		// usage();
		// System.exit( 1 );
		// }

		System.out.println( "--Welcome to Multimedia Software System--" );

		// inputFileName = args[1];
		displayMenu();
		System.out.println( "--Good Bye--" );
	}

	public static void usage()
	{
		System.out
				.println( "\nUsage: java CS451_Sawada [homework number = 4]\n" );
		System.out
				.println( "homework number has to be \'4\' for this program to run." );
	}

	public static void displayMenu() throws InterruptedException, IOException
	{
		Scanner sc = new Scanner( System.in );
		String input = "";
		int n = 16, p = 12, subOption = 1, targetImgNum = 10;
		while ( true ) {
			System.out
					.print( "\nMain Menu -----------------------------------\n"
							+ "1. Block-Based Motion Compensation\n"
							+ "2. Removing Moving Objects\n"
							+ "3. Image Retrieval\n" + "4. Quit\n"
							+ "\nPlease enter the task number [1-4]: " );
			input = sc.nextLine();
			if ( input.equals( "4" ) ) {
				sc.close();
				System.out.println( "Good bye." );
				System.exit( 0 );
			} else if ( input.equals( "1" ) ) {
				System.out
						.println( "Please enter a macro block size, n [8, 16, or 32]" );
				// receive a desired macro block size
				n = sc.nextInt();

				System.out
						.println( "Please enter a search window size, p [4, 8, or 12]" );
				// receive a desired macro block size
				p = sc.nextInt();

				System.out.println( "Please choose a search criteria,  [1 - 4]"
						+ "1. Sequential Search\n" + "2. Logarithmic Search\n"
						+ "3. Sequential Search with half-pixel accuracy\n"
						+ "4. Logarithmic Search with half-pixel accuracy\n" );
				subOption = sc.nextInt();
				switch ( subOption )
				{
				case 1:
					mcSequentialSearch_regular( p, n );
					break;
				case 2:
					mcSequentialSearch_w_halfPixelAccuracy( p, n );
					break;
				case 3:
					mcSequentialSearch_regular( p, n );
					break;
				case 4:
					mcSequentialSearch_regular( p, n );
					break;

				default:
					break;
				}
			} else if ( input.equals( "2" ) ) {

				n = 16;
				p = 12;

				System.out
						.println( "Please choose a removal criteria,  [1 - 2]\n"
								+ "1. Replacement with a nearby static macroblock of the same frame\n"
								+ "2. Replacement with a the macroblock on 5th frame\n" );
				subOption = sc.nextInt();
				switch ( subOption )
				{
				case 1:
					movingObjRemoval_1( p, n );
					break;
				case 2:
					movingObjRemoval_2( p, n );
					break;

				default:
					break;
				}
			} else if ( input.equals( "3" ) ) {

				n = 16;
				p = 12;
				imageComparison( p, n );

			} else {
				System.out.println( "Sorry, " + input
						+ " is not one of the options." );
				continue;
			}
		}
	}

	public static void imageComparison( int p, int macroBlkSize )
			throws InterruptedException, IOException
	{
		Scanner sc = new Scanner( System.in );
		System.out.print( "Please enter the path to the image directory: " );
		String directoryName = sc.nextLine();
		System.out
				.print( "Please enter the target image number (the query image): " );
		int num = sc.nextInt();
		Task3 t3 = new Task3();
		// t3.getTop3SimilarFrames( 4, 16, "IDB" );
		t3.getTop3SimilarFrames( p, macroBlkSize, directoryName, num );
		sc.close();
	}

	public static void movingObjRemoval_1( int p, int macroBlkSize )
			throws InterruptedException, IOException
	{
		Scanner sc = new Scanner( System.in );
		System.out.print( "Please enter the path to the image directory: " );
		String directoryName = sc.nextLine();
		System.out
				.print( "Please enter the target image number (the query image): " );
		int num = sc.nextInt();

		File directory = new File( directoryName );
		File[] imageData = directory.listFiles();
		String targetImgFilePath = imageData[num].getPath();
		String referenceImgFilePath = imageData[num - 2].getPath();
		Task2 t2 = new Task2();
		ImageJr targetImg = new ImageJr( targetImgFilePath );
		ImageJr referenceImg = new ImageJr(referenceImgFilePath);

		t2.removeMovingObj02( directoryName, targetImg, referenceImg,
				targetImgFilePath, referenceImgFilePath, macroBlkSize, p );
		sc.close();
	}

	public static void movingObjRemoval_2( int p, int macroBlkSize )
			throws InterruptedException, IOException
	{
		Scanner sc = new Scanner( System.in );
		System.out.print( "Please enter the path to the target image: " );
		String targetImgFilePath = sc.nextLine();
		System.out.print( "Please enter the path to the reference image: " );
		String referenceImgFilePath = sc.nextLine();

		Task2 t2 = new Task2();
		ImageJr targetImg = new ImageJr( targetImgFilePath );
		ImageJr referenceImg = new ImageJr( referenceImgFilePath );

		t2.removeMovingObj02( "IDB/", targetImg, referenceImg,
				targetImgFilePath, referenceImgFilePath, macroBlkSize, p );
		sc.close();
	}

	public static void mcSequentialSearch_w_halfPixelAccuracy( int p,
			int macroBlkSize ) throws InterruptedException, IOException
	{
		Scanner sc = new Scanner( System.in );
		System.out.print( "Please enter the path to the target image: " );
		String targetImgFilePath = sc.nextLine();
		System.out.print( "Please enter the path to the reference image: " );
		String referenceImgFilePath = sc.nextLine();

		Prep pp = new Prep();
		// targetImgFilePath = "IDB/Walk_060.ppm";
		// referenceImgFilePath = "IDB/Walk_058.ppm";

		ImageJr imgT = new ImageJr( targetImgFilePath );
		ImageJr imgR = new ImageJr( referenceImgFilePath );
		ImageJr residual = new ImageJr();
		int[] paddedSize = new int[2];
		imgT.paddedSize( macroBlkSize, paddedSize );

		List<MotionCompensation> mc = pp.MC_w_half_pixel_accuracy( imgT,
				targetImgFilePath, imgR, referenceImgFilePath, p, 0, residual,
				macroBlkSize );

		pp.writeTask1ResultToFile( mc, targetImgFilePath, referenceImgFilePath,
				macroBlkSize, imgT.getW(), imgT.getH(), p );
		sc.close();
	}

	public static void mcSequentialSearch_regular( int p, int macroBlkSize )
			throws InterruptedException, IOException
	{
		Scanner sc = new Scanner( System.in );
		System.out.print( "Please enter the path to the target image: " );
		String targetImgFilePath = sc.nextLine();
		System.out.print( "Please enter the path to the reference image: " );
		String referenceImgFilePath = sc.nextLine();

		Prep pp = new Prep();
		// targetImgFilePath = "IDB/Walk_060.ppm";
		// referenceImgFilePath = "IDB/Walk_058.ppm";

		ImageJr imgT = new ImageJr( targetImgFilePath );
		ImageJr imgR = new ImageJr( referenceImgFilePath );
		ImageJr residual = new ImageJr();
		int[] paddedSize = new int[2];
		imgT.paddedSize( macroBlkSize, paddedSize );

		List<MotionCompensation> mc = pp.MC_regular( imgT, targetImgFilePath,
				imgR, referenceImgFilePath, p, 0, residual, macroBlkSize, true );

		pp.writeTask1ResultToFile( mc, targetImgFilePath, referenceImgFilePath,
				macroBlkSize, imgT.getW(), imgT.getH(), p );
		sc.close();
	}

	public static boolean isInt( int x )
	{
		if ( x == (int) x ) {
			return true;
		} else {
			return false;
		}
	}

}
