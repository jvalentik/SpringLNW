package com.ibm.backend.repositories;

import com.ibm.backend.domain.User;
import com.ibm.backend.domain.UserDTO;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

/**
 * Created by Jan Valentik on 1/12/2016.
 */
@UIScope
@Component
public interface IUserService {
	User registerNewUserAccount(UserDTO accountDto)  throws Exception;
}
