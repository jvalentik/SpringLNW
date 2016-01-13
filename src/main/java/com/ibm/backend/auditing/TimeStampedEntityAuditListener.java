package com.ibm.backend.auditing;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * Created by Jan Valentik on 1/9/2016.
 */
@Configurable
public class TimeStampedEntityAuditListener {

	@PrePersist
	public void touchForCreate(AbstractAuditableEntity target) {
		Date now = new Date();
		target.setCreated(now);
		target.setUpdated(now);
		String createdByUser = getUsernameOfAuthenticatedUser();
		target.setCreatedByUser(createdByUser);
		target.setModifiedByUser(createdByUser);
	}

	@PreUpdate
	public void touchForUpdate(AbstractAuditableEntity target) {
		target.setUpdated(new Date());
		String modifiedByUser = getUsernameOfAuthenticatedUser();
		target.setModifiedByUser(modifiedByUser);
	}

	private String getUsernameOfAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		return ((User) authentication.getPrincipal()).getUsername();

	}

}
