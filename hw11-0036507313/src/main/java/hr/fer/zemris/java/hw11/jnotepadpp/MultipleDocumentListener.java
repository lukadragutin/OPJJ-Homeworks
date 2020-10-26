package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface that models observers on the {@link MultipleDocumentModel} objects.
 * @author Luka Dragutin
 *
 */
public interface MultipleDocumentListener {

	/**
	 * Called when the currently focused document is changed
	 * @param previousModel {@link SingleDocumentModel} that was focused before
	 * @param currentModel {@link SingleDocumentModel} that is now focused
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	
	
	/**
	 * Called when a new {@link SingleDocumentModel} is added to the {@link MultipleDocumentModel}
	 * @param model Newly added {@link SingleDocumentModel}
	 */
	void documentAdded(SingleDocumentModel model);
	
	
	/**
	 * Called when a {@link SingleDocumentModel} is removed from the {@link MultipleDocumentModel}
	 * @param model The {@link SingleDocumentModel} that has been removed
	 */
	void documentRemoved(SingleDocumentModel model);
}
