package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Default implementation of interface {@link MultipleDocumentModel}
 * A model that offers the possibility to have multiple instances of {@link SingleDocumentModel}
 * open at once that are viewable through the {@link JTabbedPane} and keeps their references in a collection. This class is a Subject in the
 * observer strategy 
 * @see SingleDocumentModel
 * @see JTabbedPane
 * @author Luka Dragutin
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Collection of avaliable, open documents
	 */
	private ArrayList<SingleDocumentModel> documents;

	/** Currently open document */
	private SingleDocumentModel currentDocument;

	/** Collection of listeners */
	private ArrayList<MultipleDocumentListener> listeners;
	
	/** Icon that signifies if an document has not been modified */
	private static ImageIcon unmodified;
	
	/** Icon that signifies if a document has been modified */
	private static ImageIcon modified;
	
	/** Default name for a pathless document */
	public static final String DEFAULT_NAME = "(unnamed)";

	//static block for loading icons
	static {
		unmodified = loadIcon("icons/greenFloppy.png");
		modified = loadIcon("icons/redFloppy.png");
	}

	/**
	 * Constructor instantiates the fields of the object and adds
	 * a change listener to keep track of currently selected tabs
	 */
	public DefaultMultipleDocumentModel() {
		super();
		documents = new ArrayList<>();
		listeners = new ArrayList<>();
		createNewDocument();
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateCurrentDocument();
			}
		});
	}

	/**
	 * Method for loading an icon from the disc using a resource reference
	 * @param resource Resource reference
	 * @return icon from the referenced path
	 * @throws IllegalArgumentException if the path to the icon is not valid
	 * @throws RuntimeException if the icon can't be read
	 */
	private static ImageIcon loadIcon(String resource) {
		byte[] bytes;
		try (InputStream is = DefaultMultipleDocumentModel.class.getResourceAsStream(resource);) {
			if (is == null) {
				throw new IllegalArgumentException();
			}
			bytes = is.readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return new ImageIcon(bytes);
	}

	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	/**
	 * {@inheritDoc}
	 * It also changes the view to this document and
	 * notifies its listeners about it.
	 */
	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, "");

		documents.add(newDocument);

		JScrollPane sp = new JScrollPane(newDocument.getTextComponent());

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(sp, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(50, 20));

		this.addTab(DEFAULT_NAME, modified, panel, DEFAULT_NAME);
		this.setSelectedIndex(this.getTabCount() - 1);

		newDocument.addSingleDocumentListener(new SingleDocumentListenerImpl(this, panel));

		activateDocument(newDocument);
		notifyDocumentAdded(newDocument);
		return newDocument;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	/**
	 * {@inheritDoc}
	 * @throws RuntimeException if the document cannot be loaded
	 */
	@Override
	public SingleDocumentModel loadDocument(Path path) {
		SingleDocumentModel loaded = new DefaultSingleDocumentModel(Objects.requireNonNull(path), "");
		int index = hasThisFile(path);

		if (index >= 0) {
			activateDocument(documents.get(index));
			this.setSelectedIndex(index);
		} else {
			loaded = createNewDocument();
			loaded.setFilePath(path);
			
			try {
				loaded.getTextComponent().setText(Files.readString(path, StandardCharsets.UTF_8));
				loaded.setModified(false);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return currentDocument;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws RuntimeException if the file cannot be written to the disc
	 */
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Path savePath;
		if (newPath == null) {
			if (model.getFilePath() != null) {
				savePath = model.getFilePath();
			} else {
				throw new RuntimeException();
			}
		} else {
			savePath = newPath;
		}

		try {
			Files.writeString(savePath, model.getTextComponent().getText(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		model.setModified(false);
		model.setFilePath(savePath);
	}

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException if the model is not
	 * in the collection of open documents
	 */
	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		this.remove(this.getSelectedComponent());
		documents.remove(index);

		notifyDocumentRemoved(model);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	/**
	 * {@inheritDoc}
	 * @throws IndexOutOfBoundsException if the argument is 
	 * out of bounds (index < 0 || index > documents.size() - 1)
	 */
	@Override
	public SingleDocumentModel getDocument(int index) {
		if (index < 0 || index > documents.size() - 1) {
			throw new IndexOutOfBoundsException();
		}
		return documents.get(index);
	}

	/**
	 * Notifies all the listeners about the current document being changed
	 * @param previousModel The document that was focused before
	 */
	private void notifyCurrentDocChange(SingleDocumentModel previousModel) {
		listeners.forEach(e -> e.currentDocumentChanged(previousModel, this.currentDocument));
	}

	/**
	 * Notifies all the listeners about a new document that has been added
	 * @param model The newly added document
	 */
	private void notifyDocumentAdded(SingleDocumentModel model) {
		listeners.forEach(e -> e.documentAdded(model));
	}

	/**
	 * Notifies all the listener about a document that has been removed
	 * @param model the removed document
	 */
	private void notifyDocumentRemoved(SingleDocumentModel model) {
		listeners.forEach(e -> e.documentRemoved(model));
	}

	/**
	 * Checks if the document collection has a document
	 * from the given path already opened and returns its index or -1 
	 * if there is not a document like that
	 * @param path The path of a document
	 * @return The index of the document that has that path, or -1
	 */
	public int hasThisFile(Path path) {
		int i = 0;
		int index = -1;
		for (var doc : documents) {
			var filePath = doc.getFilePath();
			if (filePath != null && filePath.equals(path)) {
				index = i;
			}
			i++;
		}
		return index;
	}

	/**
	 * Updates the current document model based
	 * on the document selected in the {@link JTabbedPane}
	 */
	private void updateCurrentDocument() {
		int index = this.getSelectedIndex();
		if (index < 0) {
			activateDocument(null);
		} else {
			activateDocument(this.getDocument(index));
		}
	}

	/**
	 * Implementation of a {@link SingleDocumentListener}
	 * @author Luka Dragutin
	 *
	 */
	private static class SingleDocumentListenerImpl implements SingleDocumentListener {

		/** Reference to the tabbed pane where the documents are viewed */
		private JTabbedPane tp;
		
		/** The panel through which the document was added to the tabbed pane */
		private JPanel panel;

		public SingleDocumentListenerImpl(JTabbedPane tp, JPanel panel) {
			this.tp = tp;
			this.panel = panel;
		}

		/**
		 * If the documents modify status changes, the icon of the
		 * document in the tabbed pane changed also accordingly
		 */
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			int index = tp.indexOfComponent(panel);
			if (index >= tp.getTabCount()) {
				return;
			}
			if (model.isModified()) {
				tp.setIconAt(index, modified);
			} else {
				tp.setIconAt(index, unmodified);
			}
		}

		/**
		 * If the file path of the document is updated, the tab text
		 * and tooltip text are updated
		 */
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			int index = tp.indexOfComponent(panel);
			if (index >= tp.getTabCount())
				return;
			tp.setToolTipTextAt(index, model.getFilePath().toString());
			tp.setTitleAt(index, model.getFilePath().getFileName().toString());
		}
	}

	@Override
	public void activateDocument(SingleDocumentModel model) {
		if(getTabCount() == 0) return;
		var previousModel = currentDocument;
		currentDocument = model;
		int index = documents.indexOf(model);
		if(index >= 0) {
			this.setSelectedIndex(index);
		}
		else if(model != null) {
			throw new RuntimeException();
		}
		notifyCurrentDocChange(previousModel);
	}

}
