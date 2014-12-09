package semestralka;

public class Fronta {
	
	Objednavka prvni;
	
	public Fronta(){
		prvni = null;
	}
	
	public void pridej(Objednavka nova){
		if(prvni == null){
			prvni = nova;
		} else {
			Objednavka pom = prvni;
			while(nova.casDoruceni >= pom.casDoruceni && pom.dalsi != null){
				pom = pom.dalsi;
			}
			if (pom.dalsi == null && nova.casDoruceni >= pom.casDoruceni) {
				pom.dalsi = nova;
				nova.predchozi = pom;
			} else {
				if(pom.predchozi == null){
					nova.dalsi = pom;
					pom.predchozi = nova;
					prvni = nova;
				} else {
					pom.predchozi.dalsi = nova;
					nova.predchozi = pom.predchozi;
					nova.dalsi = pom;
					pom.predchozi = nova;
				}
			}
		}
	}
	
	public void odeber(){
		if(prvni != null){
			if(prvni.dalsi != null){
				prvni = prvni.dalsi;
				prvni.predchozi.dalsi = null;
				prvni.predchozi = null;
			}else{
				prvni = null;
			}
		}
	}
	
	public Objednavka getTop(){		
		return prvni;
	}
	
	public void vypis(){
		Objednavka pom = prvni;
		while(pom != null){
			System.out.println(pom.mnozstvi + " " + pom.indexHospody + " " + pom.vzdalenost + " " + pom.casDoruceni);
			pom = pom.dalsi;
		}
	}
	
	public String vypis2(){
		int i = 0;
		String out = "";

		Objednavka pom = prvni;
		while(pom != null){
			out += "Hospoda" + pom.indexHospody + "; Kolik: " + pom.mnozstvi + "; Kdy: " + pom.casDoruceni + "\n";
			pom = pom.dalsi;
			i++;
		}
		out += "Celkem hospod ve fronte: " + i + "";
		
		return out;
	}	
	
}
