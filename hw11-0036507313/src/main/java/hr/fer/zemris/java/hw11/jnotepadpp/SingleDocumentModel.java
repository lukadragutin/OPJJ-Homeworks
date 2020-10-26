package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * A model of a document ready for editing in the GUI.
 * @author Luka Dragutin
 *
 */
public interface SingleDocumentModel {

	/**
	 * Returns the text component which this 
	 * document uses to show its text in the GUI
	 * @return {@link JTextArea} the documents component for text editing
	 */
	JTextArea getTextComponent();
	
	/**
	 * Returns the path to the document
	 * @return the path to the document
	 */
	Path getFilePath();
	
	
	/**
	 * Sets the document path
	 * @param path new path for the document
	 */
	void setFilePath(Path path);
	
	
	/**
	 * Checks if the document has been modified since it
	 * has last been saved
	 * @return <code>true</code> if it was modified, and <code>false</code> otherwise
	 */
	boolean isModified();
	
	
	/**
	 * Sets the flag that signifies it the document has been
	 * modified since its last save on the computer
	 * @param modified <code>true</code> if it was modified, <code>false</code>
	 * it the modifications were saved
	 */
	void setModified(boolean modified);

	/**
	 * Adds a listener
	 * @param l A listener
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	
	/**
	 * Removes a listener
	 * @param l a listener
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
