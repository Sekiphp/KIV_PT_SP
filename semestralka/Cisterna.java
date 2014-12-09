package semestralka;

public class Cisterna {

	String oznaceni;
	int casNavratu;
	
	SpojovySeznamAuta[] pole = new SpojovySeznamAuta[7];
	
	public Cisterna (int poradi) {
		oznaceni = "CISTERNA_" + poradi;
		casNavratu = 0;
		for(int i = 0; i<pole.length; i++){
			pole[i] = new SpojovySeznamAuta();
		}
	}
	
}
