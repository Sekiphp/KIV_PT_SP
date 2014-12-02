package semestralka;

import java.io.BufferedReader;
import java.io.FileReader;

public class NactiData {
	public int [][] hosp_region_1;
	public int [][] hosp_region_2;
	public int [][] hosp_region_3;
	public int [][] hosp_region_4;
	public int [][] hosp_region_5;
	public int [][] hosp_region_6;
	public int [][] hosp_region_7;
	public int [][] hosp_region_8;
	private String [] radka = new String[4000];
	
	public NactiData(){
		int [] count = new int[8]; // pomocny citac
		count[0] = count[1] = count[2] = count[3] = count[4] = count[5] = count[6] = count[7] = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("data_hospody.txt"));
			for(int i = 0; i < 4000; i++){
				String str = br.readLine();
				
				// zapamatujeme si - eliminace druheho cteni souboru
				radka[i] = str;
				
				String[] region = str.split("");
				String[] rnazev = region[0].split("_");
				
				count[Integer.valueOf(rnazev[1])]++;
				


			}
			br.close();
		}catch(Exception e){
			
		}
		
		for(int j = 0; j < 8; j++){
			System.out.println(count[j]);
		}
	}
	
	public static void main(String[] args){
		new NactiData();
	}
}
