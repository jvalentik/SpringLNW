package com.ibm.backend.repositories;

import com.ibm.backend.domain.Attachment;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jan Valentik on 1/12/2016.
 */
@UIScope
@Repository
public interface AttachmentRepository extends CrudRepository<Attachment, Long> {
	List<Attachment> findByWbsId(String wbsId);
}
