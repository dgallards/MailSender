package mailSender;

import java.util.ArrayList;

public class Agency {

	String email;
	ArrayList<String> arguments;

	public Agency(String email, ArrayList<String> arguments) {

		this.email = email;
		this.arguments = arguments;

	}

	public String getEmail() {
		return email;
	}

	public ArrayList<String> getArguments() {
		return arguments;
	}

	public String toString() {
		
		String arguments = "";
		
		for (String string : this.arguments) {
			arguments = arguments + string+" ,";
		}
		
		return " Email: "+this.email+ arguments;
		
	}

}
