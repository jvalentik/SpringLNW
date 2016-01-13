package com.ibm.presentation.views;

import com.ibm.Sections;
import com.ibm.backend.domain.User;
import com.ibm.backend.repositories.UserRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;
import java.util.ArrayList;


/**
 * Created by Jan Valentik on 1/10/2016.
 */
@SpringView(name = AdministratorView.VIEW_NAME)
@SideBarItem(sectionId = Sections.VIEWS, caption = "Administration", order = 2)
@FontAwesomeIcon(FontAwesome.LAPTOP)
public class AdministratorView extends MVerticalLayout implements View {

	public static final String VIEW_NAME = "administrator-view";

	@Autowired
	UserForm userEditor;

	@Autowired
	UserRepository repository;

	MTable<User> userMTable = new MTable(User.class).withFullWidth().withFullHeight();
	MHorizontalLayout mainContent = new MHorizontalLayout(userMTable).
			withFullWidth().withMargin(false);

	TextField filter = new TextField();

	Header header = new Header("Users").setHeaderLevel(2);

	Button addButton = new MButton(FontAwesome.EDIT, clickEvent -> addUser());


	@PostConstruct
	public void init() {
		userMTable.addMValueChangeListener(mValueChangeEvent -> editUser(mValueChangeEvent.getValue()));
		filter.setInputPrompt("Filter users...");
		filter.addTextChangeListener(textChangeEvent -> listUsers(textChangeEvent.getText()));
		layout();
		adjustTableColumns();
		UI.getCurrent().setResizeLazy(true);
		Page.getCurrent().addBrowserWindowResizeListener(e -> {
			adjustTableColumns();
			layout();
		});
		listUsers();
	}

	private void layout() {
		removeAllComponents();
		addComponents(new MHorizontalLayout(header, filter, addButton)
							.expand(header).alignAll(Alignment.MIDDLE_LEFT), mainContent);
		filter.setSizeUndefined();
		setMargin(new MarginInfo(false, true, true, true));
		expand(mainContent);
	}

	private void adjustTableColumns() {
		userMTable.setVisibleColumns("firstName", "lastName", "email", "role");
		userMTable.setColumnHeaders("First name", "Last name", "Email", "Type of access");
	}


	private void listUsers() {
		userMTable.setBeans(new ArrayList<>(repository.findAll()));
	}

	private void listUsers(String filterString) {
		userMTable.setBeans(new ArrayList<>(repository.findByLastName(filterString)));
	}

	void editUser(User user) {
		if (user != null) {
			openEditor(user);
		} else {
			closeEditor();
		}
	}

	void addUser() {
		openEditor(new User());
	}

	private void openEditor(User user) {
		userEditor.setEntity(user);
		mainContent.addComponent(userEditor);
		userEditor.focusFirst();
	}

	private void closeEditor() {
		if (userEditor.getParent() == mainContent) {
			mainContent.removeComponent(userEditor);
		} else {
			replaceComponent(userEditor, this);
		}
	}
	void saveCustomer(User user) {
		listUsers();
		closeEditor();
	}

	void resetCustomer(User user) {
		listUsers();
		closeEditor();
	}

	void deleteCustomer(User user) {
		closeEditor();
		listUsers();
	}


	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {

	}

}

