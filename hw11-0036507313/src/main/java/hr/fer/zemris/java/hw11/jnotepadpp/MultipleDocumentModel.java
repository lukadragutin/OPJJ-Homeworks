package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Implementing this interface allows an object to store and operator on
 * multiple documents that are implemented as {@link SingleDocumentModel}
 * @author Luka Dragutin
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/**
	 * Creates a new, empty document and adds it
	 * to the collection of documents, while also returning
	 * a reference to it.
	 * @return A reference to the newly created document
	 */
	SingleDocumentModel createNewDocument();
	
	/**
	 * Returns the currently selected document that is
	 * being operated on
	 * @return The current document
	 */
	SingleDocumentModel getCurrentDocument();
	
	/**
	 * Reads a document from the disc using the given argument
	 * as path. If the document has already been loaded before then it
	 * only becomes the current document, otherwise it is also added to
	 * the open documents collection.
	 * The argument cannot be <code>null</code>
	 * @param path Path to the wanted document
	 * @return The loaded document
	 */
	SingleDocumentModel loadDocument(Path path);
	
	/**
	 * Saves a document on the disc using the argument as a path.
	 * If the path is <code>null</code> than the documents predefined path is
	 * used for saving.
	 * @param model The document that is being saved
	 * @param newPath Path for saving
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	
	/**
	 * Closes the document and removes it from the collection of documents.
	 * @param model The document model that is being closed
	 */
	void closeDocument(SingleDocumentModel model);
	
	/**
	 * Adds a listener
	 * @param l {@link MultipleDocumentListener} Monitors changes
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Removes a listener
	 * @param l {@link MultipleDocumentListener} Monitors changes
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Returns the number of open documents
	 */
	int getNumberOfDocuments();
	
	/**
	 * Returns the document from the given index
	 * @param index Index of wanted document
	 * @return The document on the given index
	 */
	SingleDocumentModel getDocument(int index);
	
	/**
	 * Shifts the focus to the given document model
	 */
	void activateDocument(SingleDocumentModel model);
}
