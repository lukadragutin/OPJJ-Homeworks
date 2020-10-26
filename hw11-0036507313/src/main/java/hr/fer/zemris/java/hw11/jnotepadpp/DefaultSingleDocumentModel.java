package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * Default implementation of a {@link SingleDocumentModel} used
 * to represent an editable file in a {@link MultipleDocumentModel} to 
 * simulate a multi-file editor
 * @see SingleDocumentModel
 * @author Luka Dragutin
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

	/**
	 * Text component for editing in the GUI
	 */
	private JTextArea textComponent;
	
	/** Document path on the disc  */
	private Path filePath;
	
	/** Flag to represent the document being modified
	 * or not since the last save of this document  */
	private boolean modified = false;
	
	/**
	 * Listener collection
	 */
	private ArrayList<SingleDocumentListener> listeners;
	
	/**
	 * Counter to count the number of instances created, and to
	 * differentiate two document without a path
	 */
	private static int counter = 0;
	
	/**
	 * The unique number of each object
	 */
	private int instanceNumber;

	/**
	 * Instantiates the fields and adds listeners to keep track 
	 * of the changes made to the document
	 * @param filePath PAth to the document
	 * @param text Text from the document
	 */
	public DefaultSingleDocumentModel(Path filePath, String text) {
		this.filePath = filePath;
		textComponent = new JTextArea(text);
		textComponent.setVisible(true);
		instanceNumber = counter++;
		textComponent.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}
		});
		listeners = new ArrayList<>();
	}

	@Override
	public JTextArea getTextComponent() {
		return textComponent;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException can't be <code>null</code>
	 */
	@Override
	public void setFilePath(Path path) {
		this.filePath = Objects.requireNonNull(path);
		notifyPathChange();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		notifyModified();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

	/**
	 * Notifies all the listeners about modifications being made to the document
	 */
	private void notifyModified() {
		listeners.forEach(e -> e.documentModifyStatusUpdated(this));
	}

	/**
	 * Notifies all the listeners about the file path of this document being changed
	 */
	private void notifyPathChange() {
		listeners.forEach(e -> e.documentFilePathUpdated(this));
	}

	@Override
	public int hashCode() {
		return Objects.hash(filePath, listeners, modified, textComponent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultSingleDocumentModel))
			return false;
		DefaultSingleDocumentModel other = (DefaultSingleDocumentModel) obj;
		if(filePath == null && other.filePath == null) {
			return instanceNumber == other.instanceNumber;
		}
		return Objects.equals(filePath, other.filePath);
	}

	
}
