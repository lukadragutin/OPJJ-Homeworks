package hr.fer.zemris.java.hw11.jnotepadpp;


/**
 * Listener of the {@link SingleDocumentModel}
 * @author Luka Dragutin
 *
 */
public interface SingleDocumentListener {

	/**
	 * Called when the subject {@link SingleDocumentModel} is updated
	 * @param model The object that was updated
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Called when the file path of the subjet {@link SingleDocumentModel} 
	 * is altered
	 * @param model The object whose file path was altered
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
