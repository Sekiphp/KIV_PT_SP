package semestralka;

public class SpojovySeznamAuta {
	
	Prvek prvni;
	Prvek posledni;
	
	public SpojovySeznamAuta(){
		prvni = null;
		posledni = null;
	}
	
	public void pridej(int indexHospody){
		if (prvni == null) {
			Prvek prvek = new Prvek(indexHospody);
			prvni = prvek;
			posledni = prvek;
		} else {
			Prvek prvek = new Prvek(indexHospody);
			posledni.dalsi = prvek;
			posledni = posledni.dalsi;
		}
	}
	
}
