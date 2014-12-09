package semestralka;

public class NakladniAutomobil {
	
	String oznaceni;
	int casNavratu;
	int prazdneSudy;
	
	SpojovySeznamAuta[] pole = new SpojovySeznamAuta[7];
	
	public NakladniAutomobil (int region, int poradi) {
		oznaceni = "NAKLADNI_" + region + "_" + poradi;
		casNavratu = 0;
		prazdneSudy = 0;
		for(int i = 0; i<pole.length; i++){
			pole[i] = new SpojovySeznamAuta();
		}
	}

}
