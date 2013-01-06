package formvalidator;

import java.util.ArrayList;

/**
 * 
 */

/**
 * @author rocco
 * 
 */
public abstract class Form {

	/** html id of the form. */
	private String id;
	/** fields that belong to this form. */
	private ArrayList<Field> fields = new ArrayList<Field>();
	/** Form validation errors. */
	private ArrayList<String> nonFieldErrors = new ArrayList<String>();

	/** @return true when the form and its fields values are valid. */
	public boolean isValid() {
		// check for non-field-errors
		if(this.nonFieldErrors.size() > 0){
			return false;
		}
		// check field errors
		for(Field field : this.fields){
			if(field.isValid() == false){
				return false;
			}
		}
		return true;
	}

	/**
	 * Runs field and form validations. Form validation is 
	 * skipped when fields contain errors.
	 */
	public void validate() {
		if(!validateFields()) {
			validateForm();
		}
		
	}

	/**
	 * Runs all field validation methods.
	 * 
	 * @return false, when all fields are valid, true when at
	 * 		   least one error occured.
	 */
	private final boolean validateFields() {
		boolean errorflag = false;
		for (Field field : this.fields) {
			String err = field.validate();
			if (err != null) {
				errorflag = true;
			}
		}
		return errorflag;
	}

	/**
	 * Form specific validations that require access to multiple fields.
	 * Errors are appended to the objects
	 * @return
	 */
	protected abstract Error validateForm();

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	public void addField(Field field) {
		this.fields.add(field);
	}

}
