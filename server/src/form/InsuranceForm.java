package form;

import java.util.HashMap;

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
	
	private HashMap<String, Object> map = new HashMap<String, Object>();
	
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
		for(int i = 0; i<jsarray.size(); i++){
			JsonObject obj = (JsonObject) jsarray.get(i);
			map.put(obj.getString("name"), obj.getField("value"));
		}
		// And now it gets ugly.
		// Kids. Don't try this at home.
		this.address = map.get("address") == null ? "" : map.get("address").toString();
		this.title = map.get("title") == null ? "" : map.get("title").toString();
		this.name = map.get("name") == null ? "" : map.get("name").toString();
		this.surname = map.get("surname") == null ? "" : map.get("surname").toString();
		this.birthday = map.get("birthday") == null ? "" : map.get("birthday").toString();
		this.maritalStatus = map.get("maritalStatus") == null ? "" : map.get("maritalStatus").toString();
		this.streetNr = map.get("streetNr") == null ? "" : map.get("streetNr").toString();
		this.zip = map.get("zip") == null ? "" : map.get("zip").toString();
		this.city = map.get("city") == null ? "" : map.get("city").toString();
		this.career = map.get("career") == null ? "" : map.get("career").toString();
		this.amountInsured = map.get("amountInsured") == null ? -1 : Integer.valueOf(map.get("amountInsured").toString());
		this.ownershipStake = map.get("ownershipStake") == null ? -1 : Integer.valueOf(map.get("ownershipStake").toString());
		
	}

	
	/**
	 * @return true when all form fields are valid.
	 */
	public boolean isValid(){
		return true;
		//TODO: check all fields for validity
	}
	
	public JsonArray getValidatedFields(){
		//TODO: prepare json that can be returned to client
		return new JsonArray();
	}
	
	
	/**
	 * @return the address
	 */
	public final String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public final void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public final void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the surname
	 */
	public final String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public final void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the birthday
	 */
	public final String getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public final void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the maritalStatus
	 */
	public final String getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public final void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the streetNr
	 */
	public final String getStreetNr() {
		return streetNr;
	}

	/**
	 * @param streetNr the streetNr to set
	 */
	public final void setStreetNr(String streetNr) {
		this.streetNr = streetNr;
	}

	/**
	 * @return the zip
	 */
	public final String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public final void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the city
	 */
	public final String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public final void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the career
	 */
	public final String getCareer() {
		return career;
	}

	/**
	 * @param career the career to set
	 */
	public final void setCareer(String career) {
		this.career = career;
	}

	/**
	 * @return the amountInsured
	 */
	public final int getAmountInsured() {
		return amountInsured;
	}

	/**
	 * @param amountInsured the amountInsured to set
	 */
	public final void setAmountInsured(int amountInsured) {
		this.amountInsured = amountInsured;
	}

	/**
	 * @return the ownershipStake
	 */
	public final int getOwnershipStake() {
		return ownershipStake;
	}

	/**
	 * @param ownershipStake the ownershipStake to set
	 */
	public final void setOwnershipStake(int ownershipStake) {
		this.ownershipStake = ownershipStake;
	}
	
	

}
