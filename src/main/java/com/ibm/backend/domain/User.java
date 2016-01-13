package com.ibm.backend.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Jan Valentik on 12/24/2015.
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "IBM Intranet ID is required")
	@Pattern(regexp = ".*@([a-zA-Z]{2})\\.(ibm|IBM)\\.(com|COM)", message = "IBM Intranet ID is required")
	@Column(name = "user_name", unique = true)
	private String email;

	@Size(min = 5, message = "Password must be at least 5 characters")
	@Column(name = "password")
	private String password;

	@NotNull(message = "First name is required")
	@Column(name = "first_name")
	private String firstName;

	@NotNull(message = "Last name is required")
	@Column(name = "last_name")
	private String lastName;


	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "ROLES", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id") ,
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") )
	private List<Role> roles;

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String username) {
		this.email = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		User user = (User) obj;
		if (!email.equals(user.email)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("User [firstName=").append(firstName).append("]").append("[lastName=").append(lastName).append("]").append("[username").append(email).append("]");
		return builder.toString();
	}

}