package com.ibm.operation;

import com.vaadin.ui.Notification;
import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Jan Valentik on 1/10/2016.
 */
public class UploadFileOperation extends MultiFileUpload {
	private HashMap<String, File> fileStorage;

	public UploadFileOperation(HashMap<String, File> fileStorage) {
		this.fileStorage = fileStorage;
	}

	public  void setFileStorage(HashMap<String, File> fileStorage) {
		this.fileStorage = fileStorage;
	}

	public HashMap<String, File> getFileStorage() {
		return fileStorage;
	}

	@Override
	protected void handleFile(File file, String fileName, String mimeType, long length) {
		fileStorage.put(fileName , file);
		System.out.println(fileName);
		Notification.show(fileName + " uploaded successfully", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	protected FileBuffer createReceiver() {
		FileBuffer receiver = super.createReceiver();
		receiver.setDeleteFiles(false);
		return receiver;
	}


}
