package com.ibm.backend.domain;

import com.ibm.backend.auditing.AbstractAuditableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * Created by Jan Valentik on 12/24/2015.
 */
@Entity
@Table(name = "REQUEST")
public class Request extends AbstractAuditableEntity {

	@NotNull
	@Pattern(regexp = "([a-zA-Z]\\.)+(\\w{5}\\.)+(\\d{3})", message = "WBS must be in format x.xxxxx.xxx")
	@Column(name = "leading_wbs")
	private String leadingWBS = "";

	@NotNull
	@Column(name = "customer_name")
	private String customerName = "";

	@NotNull
	@Column(name = "contract_number")
	private String contractNumber = "";


	@Column(name = "services")
	private String services = "";

	@Column(name = "pex_name")
	private String pexName = "";

	@Column(name = "comments")
	private String comments = "";

	@NotNull
	@Column(name = "pma_name")
	private String pmaName = "";

	@Enumerated(EnumType.STRING)
	@Column(name = "request_status")
	private RequestStatus status;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "request_id")
	private Set<Attachment> attachments;


	public Request() {

	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName.trim();
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber.trim();
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services.trim();
	}

	public String getPexName() {
		return pexName;
	}

	public void setPexName(String pexName) {
		this.pexName = pexName.trim();
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments.trim();
	}

	public String getLeadingWBS() {
		return leadingWBS;
	}

	public void setLeadingWBS(String leadingWBS) {
		this.leadingWBS = leadingWBS.trim();
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public String getPmaName() {
		return pmaName;
	}

	public void setPmaName(String pmaName) {
		this.pmaName = pmaName;
	}



}
