package com.ibm.backend.repositories;

import com.ibm.backend.domain.User;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jan Valentik on 12/25/2015.
 */
@UIScope
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	User findByEmail(String email);
	List<User> findAll();
	List<User> findByLastName(String lastName);
}
