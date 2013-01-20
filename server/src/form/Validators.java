/**
 * 
 */
package form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author rocco
 *
 */
public class Validators {
	
	static boolean isValidString(String str) {
		final int MIN_STR_LENGTH = 2;
		if(str.equals("") || str.length() <  MIN_STR_LENGTH){
			return false;
		}
		return true;
	}
	
	static boolean isValidDate(String input) {
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
	     try {
	          format.parse(input);
	          return true;
	     }
	     catch(ParseException e){
	          return false;
	     }
	}
	
	static boolean isValidAddress(String address) {
		final List<String> validAddr = new ArrayList<String>(
			    Arrays.asList("Mr.", "Mrs.", "Ms."));
		if(validAddr.contains(address)){
			return true;
		}
		return false;
	}
	
	static boolean isValidTitle(String title) {
		final List<String> validTitles = new ArrayList<String>(
			    Arrays.asList("Sir", "Dipl.", "Dr.", "Prof. Dr.", "Dr. h.c.", "---"));
		if(validTitles.contains(title)){
			return true;
		}
		return false;
	}
	
	static boolean isValidMaritalStatus(String status) {
		final List<String> validStatus = new ArrayList<String>(
			    Arrays.asList("single", "married", "divorced", "widowed"));
		if(validStatus.contains(status)){
			return true;
		}
		return false;
	}

}
