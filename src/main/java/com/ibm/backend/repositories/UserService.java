package com.ibm.backend.repositories;

import com.ibm.backend.domain.User;
import com.ibm.backend.domain.UserDTO;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@UIScope
public class UserService implements IUserService {
	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User registerNewUserAccount(UserDTO accountDto) throws Exception {
		if (emailExist(accountDto.getEmail())) {
			throw new Exception("There is an account with that email adress: " + accountDto.getEmail());
		}
		User user = new User();

		user.setFirstName(accountDto.getFirstName());
		user.setLastName(accountDto.getLastName());
		user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
		user.setEmail(accountDto.getEmail());
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
		return repository.save(user);
	}


	public void deleteUser(final User user) {
		repository.delete(user);
	}

	public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

	private boolean emailExist(final String email) {
		final User user = repository.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}

	public User findUserByEmail(final String email) {
		return repository.findByEmail(email);
	}

	public User getUserByID(final long id) {
		return repository.findOne(id);
	}

	public void changeUserPassword(final User user, final String password) {
		user.setPassword(passwordEncoder.encode(password));
		repository.save(user);
	}

}