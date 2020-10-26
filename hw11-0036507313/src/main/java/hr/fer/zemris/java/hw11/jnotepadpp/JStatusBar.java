package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;


/**
 * Implementation of a status bar in the {@link JNotepadPP}.
 * It shows the length of the currently edited document, 
 * the position of the caret and the time.
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class JStatusBar extends JToolBar {

	/** Date format for the clock in the status bar */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/** Localization provider */
	private ILocalizationProvider prov;
	
	/**
	 * Currently analyzed document
	 */
	private SingleDocumentModel currentDoc;
	
	/**
	 * Listener that monitors changes of the caret in the 
	 * current document
	 */
	private CaretListener caretListener;
	
	/**
	 * Listener that monitors changes of the current document
	 */
	private DocumentListener docListener;
	
	/**
	 * Localization key
	 */
	private String key;
	
	
	/**
	 * Timer for the clock
	 */
	private Timer timer;

	/**Component for the length information*/
	private JLabel length;
	
	/** Length information, character count of the current document */
	private int lengthNum;

	/**
	 * Component for caret info and the time
	 */
	private JLabel rightTime;

	/** Component for the line information 
	 * about the caret */
	private JLabel lineLabel;
	
	/**
	 * Current line index of the current documents caret
	 */
	private int line;

	/** Component for the column information
	 * about the caret */
	JLabel columnLabel;
	
	/**
	 * Current column index of the current
	 * document caret
	 */
	int column;

	/**
	 * Component for the selected text information
	 */
	JLabel selLabel;
	
	/**
	 * Shows the length of selected text
	 */
	int sel;

	/**
	 * Component for showing time
	 */
	JLabel time;
	
	/**
	 * Time value
	 */
	String currentTime;

	public JStatusBar(String key, ILocalizationProvider lp, MultipleDocumentModel notepad) {
		super();
		prov = lp;
		this.key = key;
		this.setPreferredSize(new Dimension(300, 30));
		this.setFloatable(false);

		currentDoc = notepad.getCurrentDocument();
		notepad.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void documentRemoved(SingleDocumentModel model) {
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
			}

			/**
			 * When the current document changes, the listeners are also changed to
			 * monitor the current document
			 */
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if (previousModel != null) {
					previousModel.getTextComponent().removeCaretListener(caretListener);
					previousModel.getTextComponent().getDocument().removeDocumentListener(docListener);
				}
				currentDoc = currentModel;
				if (currentModel == null)
					return;
				currentDoc.getTextComponent().addCaretListener(caretListener);
				currentDoc.getTextComponent().getDocument().addDocumentListener(docListener);
				refresh();
			}
		});
		caretListener = new CaretListener() {

			/**
			 * Refreshes the caret information
			 */
			@Override
			public void caretUpdate(CaretEvent e) {
				refresh();
			}
		};

		docListener = new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshLength();
				refreshCaret();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshLength();
				refreshCaret();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				refreshLength();
				refreshCaret();
			}
		};
		currentDoc.getTextComponent().addCaretListener(caretListener);
		currentDoc.getTextComponent().getDocument().addDocumentListener(docListener);
		init();
	}

	/** Initializes the components and values */
	private void init() {
		super.setLayout(new GridLayout(1, 2));
		length = new JLabel();
		length.setHorizontalAlignment(LEADING);
		this.add(length);

		rightTime = new JLabel();
		rightTime.setLayout(new GridLayout(1,2));
		rightTime.setHorizontalAlignment(LEADING);
		
		JLabel right = new JLabel();
		right.setLayout(new FlowLayout(FlowLayout.LEADING));
		right.setHorizontalAlignment(LEADING);
		
		lineLabel = new JLabel();
		lineLabel.setHorizontalAlignment(LEADING);
		right.add(lineLabel);

		columnLabel = new JLabel();
		columnLabel.setHorizontalAlignment(LEADING);
		right.add(columnLabel);

		selLabel = new JLabel();
		selLabel.setVisible(false);
		selLabel.setHorizontalAlignment(LEADING);
		right.add(selLabel);
		
		rightTime.add(right);
		
		time = new JLabel(sdf.format(new GregorianCalendar().getTime()));
		time.setHorizontalAlignment(JLabel.TRAILING);
		rightTime.add(time);
		
		timer = new Timer(500, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				time.setText(sdf.format(new GregorianCalendar().getTime()));
			}
		});
		this.add(rightTime);
		timer.start();
		refresh();
	}

	/**
	 * Refreshes the values to match the documents
	 */
	private void refresh() {
		refreshLength();
		refreshCaret();
		refreshSelect();
	}

	/**
	 * Refreshes the information about selection
	 */
	private void refreshSelect() {
		var textArea = currentDoc.getTextComponent();

		String selText = textArea.getSelectedText();
		if (selText != null) {
			selLabel.setText("Sel:" + selText.length());
			selLabel.setVisible(true);
		} else {
			selLabel.setVisible(false);
		}
	}

	/**
	 * Refreshes the information about caret position
	 */
	private void refreshCaret() {
		JTextComponent textC = currentDoc.getTextComponent();
		int position = textC.getCaretPosition();
		Document doc = textC.getDocument();
		
		Element root = doc.getDefaultRootElement();
		line = root.getElementIndex(position) + 1;
		column = position - root.getElement(line - 1).getStartOffset() + 1;
		lineLabel.setText("Ln:" + line);
		lineLabel.setVisible(true);
		
		columnLabel.setText("Col:" + column);
		columnLabel.setVisible(true);
		
		rightTime.setVisible(true);
		this.setVisible(true);
	}

	/**
	 * Refreshes the length of the current document text
	 */
	private void refreshLength() {

		var doc = currentDoc.getTextComponent().getDocument();
		lengthNum = doc.getLength();
		length.setText(prov.getString(key) + ": " + lengthNum);
		length.setVisible(true);
	}
	
	/**Getter for the length value  */
	public int getLengthNum() {
		return lengthNum;
	}
	
	/** Stops the clock */
	public void stopTimer() {
		timer.stop();
	}
}
