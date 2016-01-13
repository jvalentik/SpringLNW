package com.ibm.presentation.views;

import com.ibm.backend.domain.UserDTO;
import com.ibm.backend.repositories.UserService;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Jan Valentik on 1/9/2016.
 */
public class AddUserView extends Window implements Property.ValueChangeListener {

	private BeanFieldGroup fields;
	private Button okButton;
	private boolean success;

	public AddUserView(UserService userService) {
		init(userService);
	}

	private void init(UserService userService) {
		addCloseListener((closeEvent) -> {
			if (success) {
				Notification.show("Access granted", "Your access has been created. Please login",
						Notification.Type.TRAY_NOTIFICATION);
			}
		});
		UserDTO userDTO = new UserDTO();
		fields = new BeanFieldGroup(UserDTO.class);
		fields.setItemDataSource(userDTO);
		FormLayout parentLayout = new FormLayout();
		parentLayout.setMargin(true);
		parentLayout.setSpacing(true);
		Field<?> firstName = fields.buildAndBind("firstName");
		firstName.setRequired(true);
		Field<?> lastName = fields.buildAndBind("lastName");
		lastName.setRequired(true);
		Field<?> userName = fields.buildAndBind("IBM Intranet ID", "email");
		userName.setRequired(true);
		Field<?> password = (fields.buildAndBind("Password", "password", PasswordField.class));
		password.setRequired(true);
		Field<?> matchingPassword = (fields.buildAndBind("Repeat password", "matchingPassword", PasswordField.class));
		matchingPassword.setRequired(true);
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.setSpacing(true);
		okButton = new Button("Save");
		okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		Button cancelButton = new Button("Cancel");
		okButton.addClickListener((clickEvent) -> {
			if (!fields.isValid()) {
				Notification.show("Passwords don't match", "Please enter the same passwords",
						Notification.Type.WARNING_MESSAGE);
			}
			else {
				try {
					fields.commit();
					userService.registerNewUserAccount(userDTO);
				} catch (FieldGroup.CommitException ex) {
					Notification.show("Mandatory fields not filled", "Please make sure all mandatory are filled",
							Notification.Type.WARNING_MESSAGE);
					return;
				} catch (Exception ex) {
					ex.printStackTrace();
					Notification.show("Account already exists", "Please make sure you don't have an account yet",
							Notification.Type.WARNING_MESSAGE);
					return;
				}

			}

			success = true;
			close();
			});
		cancelButton.addClickListener((clickEvent) -> close());
		buttonLayout.addComponent(okButton);
		buttonLayout.addComponent(cancelButton);
		parentLayout.addComponent(firstName);
		parentLayout.addComponent(lastName);
		parentLayout.addComponent(userName);
		parentLayout.addComponent(password);
		parentLayout.addComponent(matchingPassword);
		parentLayout.addComponent(buttonLayout);
		okButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		firstName.focus();
		setCaption("Create new account");
		setWidth("400px");
		center();
		setContent(parentLayout);
	}


	@Override
	public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
		if (fields.isValid()) {
			okButton.setEnabled(true);
		}
		else {
			okButton.setEnabled(false);
		}

	}
}

