package com.ibm.backend.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by Jan Valentik on 1/12/2016.
 */

public class UserDTO {
	@NotNull
	@NotEmpty
	private String firstName;

	@NotNull
	@NotEmpty
	private String lastName;


	@NotNull
	@NotEmpty
	private String password;
	private String matchingPassword;

	@NotNull(message = "IBM Intranet ID is required")
	@Pattern(regexp = ".*@([a-zA-Z]{2})\\.(ibm|IBM)\\.(com|COM)", message = "IBM Intranet ID is required")
	private String email;

	public UserDTO() {
		firstName=lastName=password=matchingPassword=email="";
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
