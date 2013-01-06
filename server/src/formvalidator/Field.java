package formvalidator;
/**
 * 
 */

/**
 * @author rocco
 *
 * Field that belongs to a form.
 */
public abstract class Field {
	/** html id of the field. */
	private String id;
	/** label of the field. */
	private String label;
	/** help text. */
	private String helpText;
	/** Value of the field. */
	private String value;
	/** Whether the field is required or not. */
	private boolean required;
	/** Associated validation error. */
	private String error;
	
	/**
	 * @return true if valid, false otherwise.
	 */
	public boolean isValid(){
		return error == null;
	}
	
	/**
	 * Validates the field and returns
	 * an error object when the field is not valid.
	 * Should be overridden to suit the field subclass.
	 * 
	 * When an error occurs it has to be set as instance variable.
	 * @return Errormessage or null when no errors
	 */
	protected abstract String validate();

}
