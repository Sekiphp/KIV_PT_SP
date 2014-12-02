package semestralka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Na konci simulace se souhrne udaje vlozi do souboru
 * 
 * @author Lubos Hubacek, Michal Horky
 */
public class VystupSoubor {
	/** Zapisovac do souboru */
	private BufferedWriter bw = null;
	
	/**
	 * Inicializuje vstupni soubor a proud pro zapis
	 * @throws IOException Chyba cteni/zapisu do souboru
	 */
	public VystupSoubor() throws IOException{
		File f = new File("vystup_hubacek_horky.html");
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			bw = new BufferedWriter(new OutputStreamWriter(fos));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Vytvori HTML hlavicku vcetne deklarace CSS
	 * 
	 * @throws IOException Chyba cteni/zapisu do souboru 
	 */
	public void createHTMLHead() throws IOException{
		String head = "<!DOCTYPE html><html lang='cs'>"
				+ "<head><title>KIV/PT - Simulace pivovaru Chmelokvas</title><meta charset='utf-8'>"
				+ "<style type='text/css'>"
				+ "* { margin: 0px; padding: 0px; font-family: sans-serif; }"
				+ "#page { padding: 5px; }"
				+ "table { width: auto; border-collapse: collapse; border: 1px solid #cdcdcd; margin-bottom: 20px; }"
				+ "table td, table th { padding: 7px 10px; border-collapse: collapse; border: 1px solid #cdcdcd; }"
				+ "table thead th { background-color: rgb(217,217,217); color: #333; text-align: center; }"
				+ "table tr:hover { background-color: #eeeeee; }"
				+ "tr[class]:hover + tr { background-color: #eeeeee; }"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<div id='page'>";
		bw.write(head);
		bw.newLine();
	}
	
	/**
	 * Vytvori korektni ukonceni HTML souboru
	 * 
	 * @throws IOException Chyba cteni/zapisu do souboru
	 */
	public void createHTMLFoot() throws IOException{
		bw.write("</div></body></html>");
		bw.newLine();
	}
	
	/**
	 * Vytvori hlavicku tabulky hospod
	 * 
	 * @throws IOException Chyba cteni/zapisu do souboru
	 */
	public void createTableHospod() throws IOException{
		String tableHead = "<table>"
				+ "<thead><tr>"
				+ "<th>Pivnice</th>"
				+ "<th>Kraj</th>"
				+ "<th>Souradnice</th>"
				+ "<th>Den 1</th>"
				+ "<th>Den 2</th>"
				+ "<th>Den 3</th>"
				+ "<th>Den 4</th>"
				+ "<th>Den 5</th>"
				+ "<th>Den 6</th>"
				+ "<th>Den 7</th>"
				+ "<th>Celkem</th>"
				+ "</tr></thead><tbody>";
		bw.write(tableHead);
		bw.newLine();		
	}
	
	/**
	 * Vlozi do stranky ukoncovaci tagy pro tabulku hospod
	 * 
	 * @throws IOException Chyba cteni/zapisu do souboru
	 */
	public void closeTableHospod() throws IOException{
		bw.write("</tbody></table>");
		bw.newLine();		
	}
	
	/**
	 * Vlozi radek do tabulky hospod
	 * 
	 * @param index_hospody Index hospody v globalnim poli hospod (trida Hlavni)
	 * @throws IOException Chyba cteni/zapisu do souboru
	 */
	public void vlozHospodu(int index_hospody) throws IOException{
		Hospoda h = Hlavni.hospody[index_hospody];
		String typ = (h.typ == 'T') ? "hl" : "sudu";
		String row = "<tr class='row'>"
				+ "<td rowspan='2'>Hospoda" + h.oznaceni + "</td>"
				+ "<td rowspan='2'>Oblast " + h.region + "</td>"
				+ "<td rowspan='2'>" + h.x + "," + h.y + "</td>"
				+ "<td>" + h.mnozstvi + " " + typ + " (" + h.hodina +"h)</td>"
				+ "<td>? hl</td>"
				+ "<td>? hl</td>"
				+ "<td>? hl</td>"
				+ "<td>? hl</td>"
				+ "<td>? hl</td>"
				+ "<td>? hl</td>"
				+ "<td rowspan='2'>sum hl</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>" + h.obslouzeno_kdy + "</td>"
				+ "<td></td>"
				+ "<td></td>"
				+ "<td></td>"
				+ "<td></td>"
				+ "<td></td>"
				+ "<td></td>"
				+ "</tr>";
		bw.write(row);
		bw.newLine();	
	}
	
	/**
	 * Ukonci zapis do souboru 
	 * 
	 * @throws IOException Chyba cteni/zapisu do souboru
	 */
	public void neplechaUkoncena() throws IOException{
		bw.close();
	}
}
