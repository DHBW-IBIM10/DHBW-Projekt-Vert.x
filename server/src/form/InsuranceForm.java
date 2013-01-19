package form;

import java.util.HashMap;
import java.util.HashSet;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class InsuranceForm {
	private String address;
	private String title;
	private String name;
	private String surname;
	private String birthday;
	private String maritalStatus;
	private String streetNr;
	private String zip;
	private String city;
	private String career;
	private int amountInsured;
	private int ownershipStake;
	
	private static final int INVALID_INT = -1;
	private HashSet<String> invalidFields = new HashSet<String>();
	
	/**
	 * Deserializes a JsonArray into a form instance.
	 * @param jsarray with the field values
	 * 
	 */
	public InsuranceForm(JsonArray jsarray){
		super();
		init(jsarray);
	}

	/**
	 * writes the json fields into a hashmap and then sets
	 * values on this instance.
	 * @param jsarray
	 */
	private void init(JsonArray jsarray){
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i<jsarray.size(); i++){
			JsonObject obj = (JsonObject) jsarray.get(i);
			map.put(obj.getString("name"), obj.getField("value"));
		}
		// And now it gets ugly.
		// What follows is a great example how code should not look like.
		this.setAddress(map.get("address") == null ? "" : map.get("address").toString());
		this.setTitle(map.get("title") == null ? "" : map.get("title").toString());
		this.setName(map.get("name") == null ? "" : map.get("name").toString());
		this.setSurname(map.get("surname") == null ? "" : map.get("surname").toString());
		this.setBirthday(map.get("birthday") == null ? "" : map.get("birthday").toString());
		this.setMaritalStatus(map.get("maritalStatus") == null ? "" : map.get("maritalStatus").toString());
		this.setStreetNr(map.get("streetNr") == null ? "" : map.get("streetNr").toString());
		this.setZip(map.get("zip") == null ? "" : map.get("zip").toString());
		this.setCity(map.get("city") == null ? "" : map.get("city").toString());
		this.setCareer(map.get("career") == null ? "" : map.get("career").toString());
		this.setAmountInsured(map.get("amountInsured") == null ? INVALID_INT : Integer.valueOf(map.get("amountInsured").toString()));
		this.setOwnershipStake(map.get("ownershipStake") == null ? INVALID_INT : Integer.valueOf(map.get("ownershipStake").toString()));
	}

	
	/**
	 * @param address the address to set
	 */
	public final void setAddress(String address) {
		this.address = address;
		if(!Validators.isValidAddress(address)){
			this.invalidFields.add("address");
		}
	}

	/**
	 * @param title the title to set
	 */
	public final void setTitle(String title) {
		this.title = title;
		if(!Validators.isValidTitle(title)){
			this.invalidFields.add("title");
		}
	}



	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
		if(!Validators.isValidString(name)){
			this.invalidFields.add("name");
		}
	}

	/**
	 * @param surname the surname to set
	 */
	public final void setSurname(String surname) {
		this.surname = surname;
		if(!Validators.isValidString(surname)){
			this.invalidFields.add("surname");
		}
	}

	/**
	 * @param birthday the birthday to set
	 */
	public final void setBirthday(String birthday) {
		this.birthday = birthday;
		if(!Validators.isValidDate(birthday)){
			this.invalidFields.add("birthday");
		}
	}

	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public final void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
		if(!Validators.isValidMaritalStatus(maritalStatus)){
			this.invalidFields.add("maritalStatus");
		}
	}

	/**
	 * @param streetNr the streetNr to set
	 */
	public final void setStreetNr(String street) {
		this.streetNr = streetNr;
		if(!Validators.isValidString(street)){
			this.invalidFields.add("streetNr");
		}
	}

	/**
	 * @param zip the zip to set
	 */
	public final void setZip(String zip) {
		this.zip = zip;
		if(zip.length() != 5){
			this.invalidFields.add("zip");
		}
	}

	/**
	 * @param city the city to set
	 */
	public final void setCity(String city) {
		this.city = city;
		if(!Validators.isValidString(city)){
			this.invalidFields.add("city");
		}
	}

	/**
	 * @param career the career to set
	 */
	public final void setCareer(String career) {
		this.career = career;
		if(!Validators.isValidString(career)){
			this.invalidFields.add("career");
		}
	}

	/**
	 * @param amountInsured the amountInsured to set
	 */
	public final void setAmountInsured(int amountInsured) {
		this.amountInsured = amountInsured;
		if(amountInsured == INVALID_INT){
			this.invalidFields.add("amountInsured");
		}
	}

	/**
	 * @param ownershipStake the ownershipStake to set
	 */
	public void setOwnershipStake(int ownershipStake) {
		this.ownershipStake = ownershipStake;
		if(ownershipStake == INVALID_INT){
			this.invalidFields.add("ownerShipStake");
		}
	}

	/**
	 * @return true when all form fields are valid.
	 */
	public boolean isValid(){
		return this.invalidFields.size() == 0;
	}	
	
	public JsonArray getInvalidFields(){
		//transform list into JsonArray
		JsonArray arr = new JsonArray(this.invalidFields.toArray());
		return arr;
		
		
	}
	
	

}
