package com.ibm.backend.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Jan Valentik on 12/24/2015.
 */
@Entity
@Table(name = "ATTACHMENT")
public class Attachment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long attachmentId;

	@Lob
	@Column(name = "content")
	private byte[] fileContent;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "wbs_id")
	private String wbsId;

	public Attachment() {}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getWbsId() {
		return wbsId;
	}

	public void setWbsId(String wbsId) {
		this.wbsId = wbsId;
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}
}
