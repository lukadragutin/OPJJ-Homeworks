package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Graphical interface program that allows the user to edit, read, create and
 * save documents. It allows the user to have as many documents as he wants. The
 * documents are stored in an instance of {@link MultipleDocumentModel}. The
 * Notepad++ also has a status bar and a clock.
 * 
 * @author Luka Dragutin
 *
 */
@SuppressWarnings("serial")
public class JNotepadPP extends JFrame {

	/** Localization service provider */
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

	/** Model that keeps all the documents */
	private MultipleDocumentModel notepad;

	/** Status bar */
	private JStatusBar statusBar;

	/** Title of the window */
	private final static String TITLE = "JNotepad++";

	private ChangeListener caretListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			var editor = notepad.getCurrentDocument().getTextComponent();
			boolean hasSelection = editor.getCaret().getDot() - editor.getCaret().getMark() != 0;

			copyAction.setEnabled(hasSelection);
			cutAction.setEnabled(hasSelection);
			toLowerCase.setEnabled(hasSelection);
			toUpperCase.setEnabled(hasSelection);
			invertCase.setEnabled(hasSelection);
			sortAscending.setEnabled(hasSelection);
			sortDescending.setEnabled(hasSelection);
			removeDuplicates.setEnabled(hasSelection);
		}
	};
	private final SingleDocumentListener modelListener = new SingleDocumentListener() {
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			updateSaveButtons(model);
		}

		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			setTitle(model);
		}
	};

	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitSequence();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				flp.disconnect();
			}
		});
		initGUI();
		setSize(500, 500);
	}

	
	/**
	 * Initilization of the graphical user interface
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		notepad = new DefaultMultipleDocumentModel();
		notepad.getCurrentDocument().addSingleDocumentListener(modelListener);
		notepad.getCurrentDocument().getTextComponent().getCaret().addChangeListener(caretListener);
		notepad.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				if(notepad.getNumberOfDocuments() == 0) {
					notepad.createNewDocument();
				}
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				previousModel.getTextComponent().getCaret().removeChangeListener(caretListener);
				previousModel.removeSingleDocumentListener(modelListener);
				if (currentModel == null) {
					exitSequence();
				}
				currentModel.getTextComponent().getCaret().addChangeListener(caretListener);
				currentModel.addSingleDocumentListener(modelListener);
				updateSaveButtons(currentModel);
				setTitle(currentModel);
			}
		});
		getContentPane().add((JTabbedPane) notepad, BorderLayout.CENTER);

		setTitle(notepad.getCurrentDocument());

		createActions();
		createMenus();
		cp.add(createToolbar(), BorderLayout.PAGE_START);
		createStatusBar();
	}

	/**
	 * Updates the current title of the {@link JNotepadPP}
	 * @param currentModel Newly focused {@link SingleDocumentModel} whose file name
	 * is going to be put in the title
	 */
	private void setTitle(SingleDocumentModel currentModel) {
		var path = currentModel.getFilePath();
		String titlePath = path == null ? DefaultMultipleDocumentModel.DEFAULT_NAME : path.toString();
		setTitle(titlePath + " - " + TITLE);
	}

	/**
	 * Configures all of the existing actions present in the app
	 */
	private void createActions() {
		openDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		openDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);

		saveDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocument.setEnabled(false);

		saveAsDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		saveAsDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		newDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		removeDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		removeDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

		exit.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
		exit.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		copyAction.setEnabled(false);

		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		cutAction.setEnabled(false);

		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);

		toLowerCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F8"));
		toLowerCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
		toLowerCase.setEnabled(false);

		toUpperCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F9"));
		toUpperCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		toUpperCase.setEnabled(false);

		invertCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F10"));
		invertCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		invertCase.setEnabled(false);

		showStatistics.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F1"));
		showStatistics.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

		english.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 1"));
		german.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 2"));
		croatian.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 3"));

		sortAscending.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F4"));
		sortAscending.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		sortAscending.setEnabled(false);

		sortDescending.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F5"));
		sortDescending.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		sortDescending.setEnabled(false);

		removeDuplicates.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F6"));
		removeDuplicates.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_M);
		removeDuplicates.setEnabled(false);
	}

	/**
	 * Creates and sets all the app's menus
	 */
	private void createMenus() {
		JMenuBar mb = new JMenuBar();

		LJMenu file = new LJMenu("file", flp);
		mb.add(file);
		file.add(new JMenuItem(newDocument));
		file.add(new JMenuItem(openDocument));
		file.addSeparator();

		file.add(new JMenuItem(showStatistics));
		file.add(new JMenuItem(saveDocument));
		file.add(new JMenuItem(saveAsDocument));
		file.addSeparator();

		file.add(new JMenuItem(removeDocument));
		file.add(new JMenuItem(exit));

		LJMenu edit = new LJMenu("edit", flp);
		mb.add(edit);
		new DefaultEditorKit();

		edit.add(new JMenuItem(copyAction));
		edit.add(new JMenuItem(cutAction));
		edit.add(new JMenuItem(pasteAction));

		LJMenu tools = new LJMenu("tools", flp);
		mb.add(tools);

		LJMenu changeCase = new LJMenu("change_case", flp);
		tools.add(changeCase);
		changeCase.add(toLowerCase);
		changeCase.add(toUpperCase);
		changeCase.add(invertCase);
		
		LJMenu sort = new LJMenu("sort_name", flp);
		tools.add(sort);
		sort.add(sortAscending);
		sort.add(sortDescending);

		LJMenu unique = new LJMenu("unique", flp);
		tools.add(unique);
		unique.add(removeDuplicates);
		

		LJMenu languages = new LJMenu("language", flp);
		mb.add(languages);
		languages.add(new JMenuItem(english));
		languages.add(new JMenuItem(german));
		languages.add(new JMenuItem(croatian));

		
		setJMenuBar(mb);
	}

	
	/**
	 * Creates and returns the app's toolbar
	 */
	private JToolBar createToolbar() {
		JToolBar tb = new JToolBar();
		tb.setFloatable(true);

		tb.add(new JButton(exit));
		tb.add(new JButton(newDocument));
		tb.add(new JButton(openDocument));
		tb.add(new JButton(showStatistics));
		tb.add(new JButton(saveDocument));
		tb.add(new JButton(saveAsDocument));
		tb.add(new JButton(removeDocument));
		tb.add(new JButton(copyAction));
		tb.add(new JButton(cutAction));
		tb.add(new JButton(pasteAction));

		return tb;
	}

	/**
	 * Creates the app's status bar
	 */
	private void createStatusBar() {
		statusBar = new JStatusBar("status_length", flp, notepad);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		statusBar.setVisible(true);
	}

	/**
	 * Updates if the save button is enabled or disabled
	 * @param model the current {@link SingleDocumentModel} based on which 
	 * the save button is enabled
	 */
	private void updateSaveButtons(SingleDocumentModel model) {
		saveDocument.setEnabled(model.isModified());
	}

	
	/**
	 * Pops up a file choosing dialog for the user to seelect
	 * a file to open or save depending on the {@code type}
	 * @param type {@link JFileChooser#SAVE_DIALOG} or {@link JFileChooser#OPEN_DIALOG}
	 * @return The path to the selected file
	 */
	private Path promptFile(int type) {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(flp.getString("file_select"));

		int select;
		if (type == JFileChooser.SAVE_DIALOG) {
			select = jfc.showSaveDialog(this);
		} else {
			select = jfc.showOpenDialog(this);
		}
		if (select != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		return jfc.getSelectedFile().toPath();
	}

	/**
	 * Called before closing the app
	 */
	protected void exitSequence() {
		var iter = notepad.iterator();
		while (iter.hasNext()) {
			var next = iter.next();
			if (!next.isModified()) {
				continue;
			}
			notepad.activateDocument(next);
			int status = JOptionPane.showConfirmDialog(JNotepadPP.this, flp.getString("save_prompt"));
			if (status == JOptionPane.CANCEL_OPTION || status == JOptionPane.CLOSED_OPTION) {
				return;
			} else if (status == JOptionPane.YES_OPTION) {
				saveDocument.actionPerformed(null);
			} else {
				continue;
			}
		}
		statusBar.stopTimer();
		dispose();
	}

	/**
	 * Method that modifies the case of the {@code text} argument depending 
	 * on the function given as argument
	 * @param text Text to process
	 * @param f Case altering function
	 * @return Altered text 
	 */
	private String toggleCase(String text, Function<Character, Character> f) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] = f.apply(chars[i]);
		}
		return String.copyValueOf(chars);
	}

	/**
	 * Returns the selected text in the current document
	 */
	private String getSelectedText() {
		var editor = notepad.getCurrentDocument().getTextComponent();
		int start = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

		if (len < 1) {
			return null;
		}

		Document doc = editor.getDocument();
		String text = null;
		try {
			text = doc.getText(start, len);
		} catch (BadLocationException ignorable) {
		}
		return text;
	}

	/**
	 * Action for saving a document to a new location
	 */
	private final Action saveAsDocument = new LocalizableAction("saveAs_name", "saveAs_description", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(notepad.getCurrentDocument() == null) return;
			
			Path path = promptFile(JFileChooser.SAVE_DIALOG);
			if (path == null) {
				JOptionPane.showMessageDialog(JNotepadPP.this, flp.getString("save_cancel"),
						flp.getString("information"), JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			for (SingleDocumentModel s : notepad) {
				if (path.equals(s.getFilePath())) {
					JOptionPane.showMessageDialog(JNotepadPP.this, flp.getString("saveAs_error"));
					return;
				}
			}

			if (Files.exists(path)) {
				int select = JOptionPane.showConfirmDialog(JNotepadPP.this, flp.getString("overwrite_prompt"));
				if (select != JOptionPane.OK_OPTION) {
					return;
				}
			}

			notepad.saveDocument(notepad.getCurrentDocument(), path);
			JOptionPane.showMessageDialog(JNotepadPP.this, flp.getString("file_saved"), flp.getString("information"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Action for opening a document from the file system
	 */
	private final Action openDocument = new LocalizableAction("open_name", "open_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			Path filePath = promptFile(JFileChooser.OPEN_DIALOG);
			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(JNotepadPP.this, String.format(flp.getString("read_error"), filePath),
						flp.getString("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			notepad.loadDocument(filePath);
		}
	};

	/**
	 * Action for creating a new document
	 */
	private final Action newDocument = new LocalizableAction("new_name", "new_description", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			notepad.createNewDocument();
		}
	};

	/**
	 * Action for saving to a already selected location, or if none is selected, then {@link #saveAsDocument} is called
	 */
	private final Action saveDocument = new LocalizableAction("save_name", "save_description", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(notepad.getCurrentDocument() == null) return;
			
			Path path = notepad.getCurrentDocument().getFilePath();
			if (path == null) {
				saveAsDocument.actionPerformed(e);
				return;
			}
			notepad.saveDocument(notepad.getCurrentDocument(), path);
		}
	};

	/**
	 * Action for removing the currently selected document
	 */
	private final Action removeDocument = new LocalizableAction("remove_name", "remove_description", flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			var closing = notepad.getCurrentDocument();
			if (closing.isModified()) {
				int selection = JOptionPane.showConfirmDialog(JNotepadPP.this, flp.getString("save_prompt"));
				if (selection == JOptionPane.CANCEL_OPTION || selection < 0) {
					return;
				} else if (selection == JOptionPane.YES_OPTION) {
					saveDocument.actionPerformed(e);
					if (closing.isModified()) {
						return;
					}
				}
			}
			notepad.closeDocument(closing);
		}
	};

	/**
	 * Action for exiting the application
	 */
	private final Action exit = new LocalizableAction("exit_name", "exit_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			exitSequence();
		}
	};

	/**
	 * Action for switching the case of the selected text to upper case
	 */
	private final Action toUpperCase = new LocalizableAction("upper_name", "upper_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			switchCase(r -> Character.toUpperCase(r));
		};
	};

	/**
	 * Action for switching the case of the selected text to lower case
	 */
	private final Action toLowerCase = new LocalizableAction("lower_name", "lower_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			switchCase(r -> Character.toLowerCase(r));
		}
	};

	/**
	 * Action for switching the case of the selected text to its inverted case (lower case 
	 * becomes upper case, and upper case becomes lower case)
	 */
	private final Action invertCase = new LocalizableAction("invert_name", "invert_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			switchCase(r -> {
				if (Character.isLowerCase(r))
					return Character.toUpperCase(r);
				else
					return Character.toLowerCase(r);
			});
		}
	};

	/**
	 * Action for showing the currently selected documents statistics
	 */
	private final Action showStatistics = new LocalizableAction("stat_name", "stat_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			int chars = statusBar.getLengthNum();
			var doc = notepad.getCurrentDocument().getTextComponent().getDocument();
			String text = null;
			try {
				text = doc.getText(0, chars);
			} catch (BadLocationException ignorable) {
			}
			int noSpaceChars = text.replaceAll("\\s", "").length();
			int lines = text.split("\n").length;
			JOptionPane.showMessageDialog(JNotepadPP.this,
					String.format(flp.getString("stat_format"), chars, noSpaceChars, lines),
					flp.getString("information"), JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Action for switching the language to english
	 */
	private final Action english = new LocalizableAction("en_name", "en_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};

	/**
	 * Action for switching the language to german
	 */
	private final Action german = new LocalizableAction("de_name", "de_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};

	/**
	 * Action for switching the language to croatian
	 */
	private final Action croatian = new LocalizableAction("hr_name", "hr_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};

	/**
	 * Action for copying selected text
	 */
	private final Action copyAction = new LocalizableAction("copy_name", "copy_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			new DefaultEditorKit.CopyAction().actionPerformed(e);
		}
	};

	/**
	 * Action for cutting selected text
	 */
	private final Action cutAction = new LocalizableAction("cut_name", "cut_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			new DefaultEditorKit.CutAction().actionPerformed(e);
		}
	};

	
	/**
	 * Action for pasting previously cut or copied text
	 */
	private final Action pasteAction = new LocalizableAction("paste_name", "paste_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			new DefaultEditorKit.PasteAction().actionPerformed(e);
		}
	};

	
	/**
	 * Action for sorting the selected text in ascending order
	 */
	private final Action sortAscending = new LocalizableAction("asc_name", "asc_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelected(true);
		}
	};

	
	/**
	 * Action for sorting the selected text in descending order
	 */
	private final Action sortDescending = new LocalizableAction("desc_name", "desc_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelected(false);
		}
	};

	
	/**
	 * Action for removing duplicate lines among the selected text
	 */
	private final Action removeDuplicates = new LocalizableAction("duplicate_name", "duplicate_description", flp) {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textC = notepad.getCurrentDocument().getTextComponent();
			int position = textC.getCaretPosition();
			int start = textC.getCaret().getMark();

			if (position - start == 0)
				return;

			Document doc = textC.getDocument();
			Element root = doc.getDefaultRootElement();
			int rowStart = root.getElementIndex(start);
			int rowEnd = root.getElementIndex(position);

			int lastPos = root.getElement(Math.max(rowStart, rowEnd)).getEndOffset();
			int startPos = root.getElement(Math.min(rowStart, rowEnd)).getStartOffset();

			String text = null;
			try {
				text = doc.getText(startPos, lastPos - startPos);
			} catch (BadLocationException ignorable) {
			}

			var lines = Arrays.asList(text.split("\n"));
			LinkedHashSet<String> hashLines = new LinkedHashSet<>(lines);
			lines = new ArrayList<>(hashLines);

			StringBuilder sb = new StringBuilder();
			lines.forEach(s -> sb.append(s + "\n"));

			try {
				doc.remove(startPos, text.length() - 1);
				doc.insertString(startPos, sb.toString(), null);
			} catch (BadLocationException ignorable) {
			}

		}
	};

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}

	/**
	 * Helper method for sorting selected text ascending or descending based on the flag in the argument
	 * @param b If it is true the text is sorted in ascending order, and descending otherwise
	 */
	protected void sortSelected(boolean b) {
		JTextComponent textC = notepad.getCurrentDocument().getTextComponent();
		int position = textC.getCaretPosition();
		int start = textC.getCaret().getMark();

		if (position - start == 0)
			return;

		Document doc = textC.getDocument();
		Element root = doc.getDefaultRootElement();
		int rowStart = root.getElementIndex(start);
		int rowEnd = root.getElementIndex(position);

		int lastPos = root.getElement(Math.max(rowStart, rowEnd)).getEndOffset();
		int startPos = root.getElement(Math.min(rowStart, rowEnd)).getStartOffset();

		String text = null;
		try {
			text = doc.getText(startPos, lastPos - startPos);
		} catch (BadLocationException ignorable) {
		}

		var lines = Arrays.asList(text.split("\n"));

		Locale locale = new Locale(flp.getCurrentLanguage());
		Collator collator = Collator.getInstance(locale);

		lines.sort((l1, l2) -> b ? collator.compare(l1, l2) : collator.compare(l2, l1));
		StringBuilder sb = new StringBuilder();
		lines.forEach(s -> sb.append(s + "\n"));

		try {
			doc.remove(startPos, sb.length() - 1);
			doc.insertString(startPos, sb.toString(), null);
		} catch (BadLocationException ignorable) {
		}
	}

	/**
	 * Method for switching the selected text's case.
	 */
	protected void switchCase(Function<Character, Character> f) {
		String text = getSelectedText();
		if (text == null)
			return;
		var editor = notepad.getCurrentDocument().getTextComponent();
		var doc = editor.getDocument();
		int start = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		try {
			text = toggleCase(text, f);
			doc.remove(start, len);
			doc.insertString(start, text, null);
		} catch (BadLocationException ignorable) {
		}
	}
}
