package com.ibm.backend.domain;

/**
 * Created by Jan Valentik on 1/10/2016.
 */

public class Contract {
	private String wbsId;
	private String pmaNotesId;
	private String peNotes;
	private String contractNumber;
	private String sapContract;
	private String customerName;

	public Contract() {}

	public String getWbsId() {
		return wbsId;
	}

	public void setWbsId(String wbsId) {
		this.wbsId = wbsId;
	}

	public String getPmaNotesId() {
		return pmaNotesId;
	}

	public void setPmaNotesId(String pmaNotesId) {
		this.pmaNotesId = pmaNotesId;
	}

	public String getPeNotes() {
		return peNotes;
	}

	public void setPeNotes(String peNotes) {
		this.peNotes = peNotes;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getSapContract() {
		return sapContract;
	}

	public void setSapContract(String sapContract) {
		this.sapContract = sapContract;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
