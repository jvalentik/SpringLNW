package com.ibm.presentation.views;

import com.ibm.backend.domain.User;
import com.ibm.backend.repositories.UserRepository;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Created by Jan Valentik on 1/13/2016.
*/
@SpringComponent
public class UserForm extends AbstractForm<User> {

	@Autowired
	UserRepository repository;
	TextField firstName = new MTextField("First name").withFullWidth();
	TextField lastName = new MTextField("Last name").withFullWidth();
	TypedSelect<TypeOfAcces> access = new TypedSelect().withCaption("Type of access");
	TextField email = new MTextField("Email").withFullWidth();

	@Override
	protected Component createContent() {

		setStyleName(ValoTheme.LAYOUT_CARD);

		return new MVerticalLayout(
				new Header("Edit user").setHeaderLevel(3),
				new MFormLayout(
						firstName,
						lastName,
						access,
						email

				).withFullWidth(),
				getToolbar()
		).withStyleName(ValoTheme.LAYOUT_CARD);
	}

	@PostConstruct
	void init() {
		setEagerValidation(true);
		access.setWidthUndefined();
		access.setOptions(TypeOfAcces.values());
		setSavedHandler(e -> repository.save(e));
		setResetHandler(e -> ((MHorizontalLayout) this.getParent()).removeComponent(this));
		setDeleteHandler(e -> repository.delete(e));
	}

	@Override
	protected void adjustResetButtonState() {
		getResetButton().setEnabled(true);
		getDeleteButton().setEnabled(getEntity() != null);
	}

}

enum TypeOfAcces {
	ROLE_USER, ROLE_ADMIN
}

