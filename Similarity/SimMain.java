package Similarity;

import java.util.Arrays;

public class SimMain {

	public SimMain()
	{	}

	public static void main( String[] args )
	{

Similarity[] sims = new Similarity[7];
/*
 * sim[0]=mv, sim[1]=diff, sim[2]=distFrm, sim[3]=target frame index ,
 * sim[4]=reference frame index
 */
Similarity f1 = new Similarity(3,30,4,10);
Similarity f2 = new Similarity(3,30,4,11);
Similarity f3 = new Similarity(3,40,4,12);
Similarity f4 = new Similarity(0,150,4,13); 
Similarity f5 = new Similarity(0,30,4,14); 
Similarity f6 = new Similarity(0,160,4,13); 
Similarity f7 = new Similarity(0,160,4,54); 

		
		sims[0]=f1;
		sims[1]=f2;
		sims[2]=f3;
		sims[3]=f4;
		sims[4]=f5;
		sims[5]=f6;
		sims[6]=f7;
		
		Arrays.sort(sims);

		int i=0;
		for(Similarity sim: sims){
		   System.out.println(sim.toString());}

	}

}
