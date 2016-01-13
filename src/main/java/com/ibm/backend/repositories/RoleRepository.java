package com.ibm.backend.repositories;

import com.ibm.backend.domain.Role;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Jan Valentik on 1/12/2016.
 */
@UIScope
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByName(String name);
}
