package com.ibm.presentation.views;


import com.ibm.backend.domain.Attachment;
import com.ibm.backend.domain.Contract;
import com.ibm.backend.domain.Request;
import com.ibm.backend.repositories.ContractDAO;
import com.ibm.operation.UploadFileOperation;
import com.vaadin.data.Item;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Jan Valentik on 1/10/2016.
 */
public class AddEntryDialog extends Window {
	private static final String TEMP_FILE_DIR = new File(System.getProperty("java.io.tmpdir")).getPath();
	private TextField wbsField;
	private HashMap<String, File> fileStorage;

	public AddEntryDialog(String contractNumber, Table table, Set<Attachment> attachments, ContractDAO
			contractService) {
		wbsField = new TextField("WBS");
		fileStorage = new HashMap<>();
		init(contractNumber, table, attachments, contractService);
	}

	private void init(String contractNumber, Table table, Set<Attachment> attachments, ContractDAO contractService) {
		Button addButton = new Button("Add");
		addButton.setWidth("90px");
		addButton.setEnabled(false);
		UploadFileOperation uploadField = new UploadFileOperation(fileStorage);
		uploadField.setEnabled(true);
		wbsField = new TextField("WBS");
		wbsField.addValidator(new BeanValidator(Request.class, "leadingWBS"));
		wbsField.addValueChangeListener((valueChangeEvent) -> {
			if (wbsField.isValid()) {
				System.out.println("Field valid: " + contractNumber);
				try {
					Contract contract = contractService.findByWbsId(wbsField.getValue());
					if (!contract.getContractNumber().equals(contractNumber)) {
						Notification.show("Different contract", "Adding WBS from different contract is not allowed", Notification.Type.WARNING_MESSAGE);
						return;
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
					Notification.show("WBS record not found", Notification.Type.WARNING_MESSAGE);
					return;
				}
				addButton.setEnabled(true);
				uploadField.setEnabled(true);
			}
		});
		wbsField.setRequired(true);
		wbsField.focus();
		setCaption("Add Entry");
		setModal(true);
		GridLayout parentLayout = new GridLayout(2, 2);
		parentLayout.setMargin(true);
		parentLayout.setSpacing(true);
		parentLayout.setSizeFull();
		center();
		setWidth("500px");
		setHeight("300px");
		setContent(parentLayout);
		parentLayout.addComponent(wbsField, 0, 0);
		uploadField.setWidth("130px");
		uploadField.setUploadButtonCaption("Browse ...");
		uploadField.setRootDirectory(TEMP_FILE_DIR);
		uploadField.setImmediate(true);
		parentLayout.addComponent(uploadField, 1, 0);
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setMargin(true);
		buttons.setSpacing(true);

		Button cancelButton = new Button("Cancel");
		addButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		addButton.addClickListener((clickEvent) -> {
			if (wbsField.getValue().equals("")) {
				Notification.show("WBS is mandatory", "Please make sure you entered WBS", Notification.Type.WARNING_MESSAGE);
			}
			else {
				fileStorage.forEach((k, v) -> {
					byte[] bytes = new byte[(int) v.length()];
					try {
						Attachment attachment = new Attachment();
						FileInputStream fileInputStream = new FileInputStream(v);
						fileInputStream.read(bytes);
						attachment.setWbsId(wbsField.getValue());
						attachment.setFileName(k);
						attachment.setFileContent(bytes);
						fileInputStream.close();
						attachments.add(attachment);
					} catch (IOException ex) {
						Notification.show("Saving file failed", Notification.Type.WARNING_MESSAGE);
					}
				});
				Object newItemId = table.addItem();
				Item nextRow = table.getItem(newItemId);
				nextRow.getItemProperty("WBS").setValue(wbsField.getValue());
				nextRow.getItemProperty("Files").setValue(fileStorage.size());
				close();
			}
		});
		cancelButton.addClickListener((clickEvent) -> {
			fileStorage.forEach((k, v) -> v.delete());
			close();
		});
		buttons.addComponent(addButton);
		buttons.addComponent(cancelButton);
		parentLayout.addComponent(buttons, 0, 1);
	}


}
