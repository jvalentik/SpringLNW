package com.ibm.presentation.views;

import com.ibm.Sections;
import com.ibm.backend.domain.Request;
import com.ibm.backend.repositories.RequestRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * Created by Jan Valentik on 1/9/2016.
 */
@SpringView(name = RequestListView.VIEW_NAME)
@SideBarItem(sectionId = Sections.VIEWS, caption = "Requests", order = 1)
@FontAwesomeIcon(FontAwesome.LIST)
public class RequestListView extends MVerticalLayout implements View{
	@Autowired
	RequestForm requestForm;

	public static final String VIEW_NAME = "request-list";

	MTable<Request> requestMTable = new MTable(Request.class).withFullWidth().
			withFullHeight();

	MHorizontalLayout mainContent = new MHorizontalLayout(requestMTable).
			withFullWidth().withMargin(false);

	TextField filter = new TextField();

	Header header = new Header("My Requests").setHeaderLevel(2);

	@Autowired
	private RequestRepository requestRepository;




	@PostConstruct
	public void init() {
		requestMTable.addMValueChangeListener(mValueChangeEvent -> editRequest(mValueChangeEvent.getValue()));
		filter.setInputPrompt("Filter requests...");
		filter.addTextChangeListener(textChangeEvent -> listRequests(textChangeEvent.getText()));
		layout();
		adjustTableColumns();
		UI.getCurrent().setResizeLazy(true);
		Page.getCurrent().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
			adjustTableColumns();
			layout();
		});
		listRequests(filter.getValue());
	}

	private void layout() {
		removeAllComponents();
		addComponents(new MHorizontalLayout(header, filter)
							.expand(header)
							.alignAll(Alignment.MIDDLE_LEFT),
					mainContent
			);
			filter.setSizeUndefined();


		setMargin(new MarginInfo(false, true, true, true));
		expand(mainContent);
	}

	private void adjustTableColumns() {
		requestMTable.setVisibleColumns(new Object[] {"leadingWBS", "customerName", "status"});
		requestMTable.setColumnHeaders(new String[] {"WBS", "Customer name", "Current status"});
	}

	private void listRequests(String filter) {
		if (filter.isEmpty() || filter.equals("")) {
			requestMTable.setBeans(new ArrayList<>(requestRepository
					     .findByCreatedByUser(SecurityContextHolder
						 .getContext().getAuthentication().getName())));
		}
		else {
			requestMTable.setBeans(new ArrayList<>(requestRepository
					.findByCreatedByUserAndCustomerNameContaining(SecurityContextHolder
							.getContext().getAuthentication().getName(), filter)));

		}
		requestMTable.setBeans(new ArrayList<>(requestRepository.findAll()));

	}



	void editRequest(Request request) {
		if (request != null) {
			openEditor(request);
		} else {
			closeEditor();
		}
	}

	private void openEditor(Request request) {
		requestForm.setEntity(request);
		mainContent.addComponent(requestForm);
		requestForm.focusFirst();
	}

	public void closeEditor() {
		if (requestForm.getParent() == mainContent) {
			mainContent.removeComponent(requestForm);
		} else {
			mainContent.replaceComponent(requestForm, this);
		}
	}

	void saveCustomer() {
		listRequests("");
		closeEditor();
	}


	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		System.out.println("Entered request-list view");

	}
}
