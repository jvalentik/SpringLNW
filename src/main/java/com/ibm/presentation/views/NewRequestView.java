package com.ibm.presentation.views;

import com.ibm.Sections;
import com.ibm.backend.domain.Attachment;
import com.ibm.backend.domain.Contract;
import com.ibm.backend.domain.Request;
import com.ibm.backend.domain.RequestStatus;
import com.ibm.backend.repositories.ContractDAO;
import com.ibm.backend.repositories.RequestRepository;
import com.ibm.operation.EmailOperation;
import com.ibm.operation.UploadFileOperation;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jan Valentik on 1/10/2016.
 */

@SpringView(name = NewRequestView.VIEW_NAME)
@SideBarItem(sectionId = Sections.VIEWS, caption = "New request", order = 0)
@FontAwesomeIcon(FontAwesome.ENVELOPE)
public class NewRequestView extends CustomComponent implements View {

	public static final String VIEW_NAME = "";

	@Autowired
	RequestRepository requestRepository;


	@Autowired
	ContractDAO contractService;

	private Table table;
	private FieldGroup group;
	private Request request;
	private Contract contract;
	private Set<Attachment> attachments = new HashSet<>();
	private HashMap<String, File> fileStorage = new HashMap<>();
	UploadFileOperation fileUploader = new UploadFileOperation(fileStorage);




	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

	}

	@PostConstruct
	public void init() {
		request = new Request();
		request.setCreatedByUser(SecurityContextHolder.getContext().getAuthentication().getName());
		fileUploader.setEnabled(false);
		BeanItem<Request> item = new BeanItem<>(request);
		group = new FieldGroup(item);
		VerticalLayout parentLayout = new VerticalLayout();

		Field<?> leadingWBS = group.buildAndBind("WBS", "leadingWBS");
		leadingWBS.focus();
		leadingWBS.addValidator(new BeanValidator(Request.class, "leadingWBS"));
		leadingWBS.setRequired(true);
		leadingWBS.addValueChangeListener(valueChangeEvent -> {
			if (leadingWBS.isValid()) {
				try {
					System.out.println(leadingWBS.getValue().toString());
					contract = contractService.findByWbsId(leadingWBS.getValue().toString().trim());
					System.out.println(contract == null ? "not found" : contract.getWbsId());
				} catch (Exception ex) {
					ex.printStackTrace();
					Notification.show("WBS record not found", Notification.Type.WARNING_MESSAGE);
					return;
				}
				group.getField("customerName").setEnabled(true);
				((TextField) group.getField("customerName")).setValue(contract.getCustomerName());
				group.getField("contractNumber").setEnabled(true);
				((TextField) group.getField("contractNumber")).setValue(contract.getContractNumber());
				group.getField("services").setEnabled(true);
				((TextField) group.getField("services")).setValue(contract.getSapContract());
				group.getField("pmaName").setEnabled(true);
				((TextField) group.getField("pmaName")).setValue(contract.getPmaNotesId());
				group.getField("pexName").setEnabled(true);
				((TextField) group.getField("pexName")).setValue(contract.getPeNotes());
				table.setEnabled(true);
				group.getField("comments").setEnabled(true);
				fileUploader.setEnabled(true);
			}
		});

		Field<?> customerName = group.buildAndBind("customerName");
		customerName.addValidator(new StringLengthValidator("Customer name must be entered", 1, 50, false));
		customerName.setRequired(true);
		customerName.setEnabled(false);


		Field<?> contractNumber = group.buildAndBind("Contract Number (OCPS)", "contractNumber");
		contractNumber.addValidator(new StringLengthValidator("Contract number must entered", 1, 50, false));
		contractNumber.setRequired(true);
		contractNumber.setEnabled(false);


		Field<?> servicesContractNumber = group.buildAndBind("Services Contract Number", "services");
		servicesContractNumber.setBuffered(true);
		servicesContractNumber.setEnabled(false);


		Field<?> pmaName = group.buildAndBind("PMA Name", "pmaName");
		pmaName.setRequired(true);
		pmaName.setBuffered(true);
		pmaName.setEnabled(false);

		Field<?> pexName = group.buildAndBind("Project Executive", "pexName");
		contractNumber.setRequired(true);
		contractNumber.setBuffered(true);
		contractNumber.setEnabled(false);

		Field<?> comments = group.buildAndBind("Add Comments", "comments", TextArea.class);
		comments.setHeight("200px");
		comments.setBuffered(true);
		comments.setEnabled(false);


		fileUploader.setEnabled(false);
		fileUploader.setWidth("130px");
		fileUploader.setUploadButtonCaption("Browse ...");
		fileUploader.setRootDirectory(new java.io.File(System.getProperty("java.io.tmpdir")).getPath());
		fileUploader.setImmediate(true);

		table = new Table("Additional WBS and attachments");
		table.setEnabled(false);
		table.setHeight("200px");
		table.setWidth("180px");
		table.addContainerProperty("WBS", String.class, null);
		table.setColumnWidth("WBS", 120);
		table.addContainerProperty("Files", Integer.class, null);
		table.setFooterVisible(true);
		table.setColumnFooter("WBS", "Add more WBS...");
		table.addFooterClickListener((footerClickEvent) -> {
			AddEntryDialog dialogView = new AddEntryDialog(((TextField) group.getField("contractNumber")).getValue(),
					table, attachments, contractService);
			UI.getCurrent().addWindow(dialogView);

		});
		table.setRequired(true);
		table.setBuffered(true);

		Label headLabel = new Label("Create new request");
		headLabel.setWidth(null);
		headLabel.setStyleName(ValoTheme.LABEL_H1);


		parentLayout.addComponent(headLabel);
		parentLayout.setComponentAlignment(headLabel, Alignment.TOP_LEFT);

		Panel panel = new Panel("Request Details");
		panel.setHeight("80%");
		panel.setWidth("90%");

		GridLayout content = new GridLayout(3, 3);
		content.setSizeFull();
		content.setMargin(true);
		content.addComponent(leadingWBS, 0, 0);
		content.addComponent(customerName, 1, 0);
		content.addComponent(contractNumber, 2, 0);
		content.addComponent(servicesContractNumber, 0, 1);
		content.addComponent(pmaName, 1, 1);
		content.addComponent(pexName, 2, 1);
		content.addComponent(table, 0, 2);
		content.addComponent(comments, 1, 2);
		content.addComponent(fileUploader, 2, 2);
		panel.setContent(content);

		Button submit = new Button("Submit");
		submit.setStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addClickListener((clickEvent) -> submitRequest());
		submit.setDescription("Submit the form for creation");
		Button clear = new Button("Clear");
		clear.setDescription("Clear all the data");
		clear.addClickListener((clickEvent) -> clearData());
		HorizontalLayout buttonPanel = new HorizontalLayout();
		buttonPanel.setMargin(true);
		buttonPanel.setSpacing(true);
		buttonPanel.addComponent(submit);
		buttonPanel.addComponent(clear);
		parentLayout.addComponent(panel);
		parentLayout.setComponentAlignment(panel, Alignment.TOP_CENTER);
		parentLayout.setExpandRatio(panel, 1f);
		parentLayout.addComponent(buttonPanel);
		parentLayout.setComponentAlignment(buttonPanel, Alignment.BOTTOM_LEFT);
		setCompositionRoot(parentLayout);

	}

	private void submitRequest() {
		try {
			group.commit();
			request.setStatus(RequestStatus.OPEN);

			if (!fileStorage.isEmpty()) {
				fileStorage.forEach((k, v) -> {
					byte[] bytes = new byte[(int) v.length()];
					try {
						Attachment attachment = new Attachment();
						FileInputStream fileInputStream = new FileInputStream(v);
						fileInputStream.read(bytes);
						attachment.setWbsId(request.getLeadingWBS());
						attachment.setFileName(k);
						attachment.setFileContent(bytes);
						fileInputStream.close();
						attachments.add(attachment);
					} catch (IOException ex) {
						Notification.show("Saving file failed", Notification.Type.WARNING_MESSAGE);
					}
				});
			}
			request.setAttachments(attachments);
			requestRepository.save(request);
			EmailOperation.sendEmail(request);
			Notification.show("Your request has been submitted", Notification.Type.TRAY_NOTIFICATION);
		}
		catch (FieldGroup.CommitException ex) {
			Notification.show("Mandatory fields not filled", "Please make sure all mandatory are filled",
					Notification.Type.WARNING_MESSAGE);
			return;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Notification.show("Email not sent", "Failed to send notification email", Notification.Type.WARNING_MESSAGE);
		}
		request = new Request();
		fileStorage = new HashMap<>();
		attachments = new HashSet<>();
		request.setCreatedByUser(SecurityContextHolder.getContext().getAuthentication().getName());
		group.clear();
		table.removeAllItems();

	}

	private void clearData() {
		group.clear();
		table.removeAllItems();
		request = new Request();
	}


}
