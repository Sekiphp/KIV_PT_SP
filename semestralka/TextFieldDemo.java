package semestralka;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.GroupLayout.*;

/**
 * Pro potreby predmetu KIV/PT upravena stazena verze z webu Oraclu + dodelan
 * javadoc
 * 
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 * 
 * @author Luboš Hubáèek, Michal Horký
 */
public class TextFieldDemo implements DocumentListener {
	/** Vstupni policko pro vyhledavani */
	private JTextField entry;
	/** Pole s textem vybizejicim ke hledani */
	private JLabel jLabel1;
	/** Scroller pro pohodlne zobrazeni mnoha textu v JTextFieldu */
	private JScrollPane jScrollPane1;
	/** Vypsani statusu hledani uplne dole */
	private JLabel status;
	/** Pole informujici o stavu programu */
	private JTextArea textArea;
	/** */
	private String input = "";
	/** Barva pro zvyrazneni nalezeneho textu v textove oblasti */
	private final static Color HILIT_COLOR = Color.LIGHT_GRAY;
	/** Barva pro pozadi vstupu ve vyhledavani pri chybe */
	private final static Color ERROR_COLOR = Color.PINK;
	/** Ukoncovaci akce */
	private final static String CANCEL_ACTION = "cancel-search";

	private Color entryBg;
	private Highlighter hilit;
	private Highlighter.HighlightPainter painter;

	public void setStart() {
		// predame text z Hlavni
		textArea.setText(input);


		hilit = new DefaultHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
		textArea.setHighlighter(hilit);

		entryBg = entry.getBackground();
		entry.getDocument().addDocumentListener(this);

		InputMap im = entry.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = entry.getActionMap();
		im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
		am.put(CANCEL_ACTION, new CancelAction());

	}
	
	/**
	 * Prida text o stavu programu do policka
	 * 
	 * @param in Pridavany text
	 */
	public void addTextLn(String in){
		input += in + "\n";
		textArea.setText(input);
	}
	
	/**
	 * Prida text o stavu programu do policka
	 * 
	 * @param in Pridavany text
	 */
	public void addText(String in){
		input += in;
		textArea.setText(input);
	}
	
	
	/**
	 * Vymaze dosavadni vypis
	 */
	public void clear(){
		input = "";
		textArea.setText(input);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */

	public JPanel initComponents() {
		entry = new JTextField();
		textArea = new JTextArea();
		status = new JLabel();
		jLabel1 = new JLabel();

		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		jScrollPane1 = new JScrollPane(textArea);

		jLabel1.setText("Vyhledat retezec:");
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);

		// Create a parallel group for the horizontal axis
		ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

		// Create a sequential and a parallel groups
		SequentialGroup h1 = layout.createSequentialGroup();
		ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

		// Add a container gap to the sequential group h1
		h1.addContainerGap();

		// Add a scroll pane and a label to the parallel group h2
		h2.addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
		h2.addComponent(status, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

		// Create a sequential group h3
		SequentialGroup h3 = layout.createSequentialGroup();
		h3.addComponent(jLabel1);
		h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		h3.addComponent(entry, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE);

		// Add the group h3 to the group h2
		h2.addGroup(h3);
		// Add the group h2 to the group h1
		h1.addGroup(h2);

		h1.addContainerGap();

		// Add the group h1 to the hGroup
		hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);
		// Create the horizontal group
		layout.setHorizontalGroup(hGroup);

		// Create a parallel group for the vertical axis
		ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		// Create a sequential group v1
		SequentialGroup v1 = layout.createSequentialGroup();
		// Add a container gap to the sequential group v1
		v1.addContainerGap();
		// Create a parallel group v2
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
		v2.addComponent(jLabel1);
		v2.addComponent(entry, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		// Add the group v2 tp the group v1
		v1.addGroup(v2);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(status);
		v1.addContainerGap();

		// Add the group v1 to the group vGroup
		vGroup.addGroup(v1);
		// Create the vertical group
		layout.setVerticalGroup(vGroup);

		return panel;
	}

	public void search() {
		hilit.removeAllHighlights();

		String s = entry.getText();
		if (s.length() <= 0) {
			message("");
			return;
		}

		String content = textArea.getText();
		int index = content.indexOf(s, 0);
		if (index >= 0) { // match found
			try {
				int end = index + s.length();
				hilit.addHighlight(index, end, painter);
				textArea.setCaretPosition(end);
				entry.setBackground(entryBg);
				message("'" + s + "' nenalezeno. Stiskni ESC pro konec hledani");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else {
			entry.setBackground(ERROR_COLOR);
			message("'" + s + "' nenalezeno. Stiskni ESC pro konec hledani");
		}
	}

	void message(String msg) {
		status.setText(msg);
	}

	// DocumentListener methods

	public void insertUpdate(DocumentEvent ev) {
		search();
	}

	public void removeUpdate(DocumentEvent ev) {
		search();
	}

	public void changedUpdate(DocumentEvent ev) {
	}

	class CancelAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ev) {
			hilit.removeAllHighlights();
			entry.setText("");
			entry.setBackground(entryBg);
		}
	}

}