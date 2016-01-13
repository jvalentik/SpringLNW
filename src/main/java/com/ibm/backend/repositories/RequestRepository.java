package com.ibm.backend.repositories;

import com.ibm.backend.domain.Request;
import com.ibm.backend.domain.RequestStatus;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jan Valentik on 12/25/2015.
 */
@UIScope
@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {
	List<Request> findByStatus(RequestStatus status);
	List<Request> findByCreatedByUser(String createdBy);
	List<Request> findByCreatedByUserAndCustomerNameContaining(String createdBy, String customerName);
	List<Request> findByPmaName(String pmaName);
	List<Request> findAll();


}
