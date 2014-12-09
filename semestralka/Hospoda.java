package semestralka;

public class Hospoda extends Stavba{
	
	int region;
	char typ;
	
	int casyObjednani[] = new int[7];
	int casyDoruceni[] = new int[7];
	int poctySudu[] = new int[7];
	String[] auta = new String[7];
	
	public Hospoda(int oznaceni, int x, int y, char typ, int region){
		super(oznaceni, x, y);
		this.typ = typ;
		this.region = region;
	}
		
	public void setDenniData(int den, Objednavka x){
		casyObjednani[den] = x.casPrijetiObjednavky;
		casyDoruceni[den] = x.casDoruceni;
		poctySudu[den] = x.mnozstvi;
		auta[den] = x.obsluhujiciAuto;
	}
	
}
