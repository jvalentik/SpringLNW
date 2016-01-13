package com.ibm.presentation.views;

import com.ibm.backend.domain.User;
import com.ibm.backend.repositories.UserService;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Jan Valentik on 1/13/2016.
 */
public class ResetPasswordForm extends Window {

	public ResetPasswordForm(UserService userService) {
		FormLayout parentLayout = new FormLayout();
		Label userNameField = new Label(SecurityContextHolder.getContext().getAuthentication().getName());
		PasswordField passwordField = new PasswordField("Enter password");
		passwordField.setRequired(true);
		passwordField.addValidator(new BeanValidator(User.class, "password"));
		PasswordField repeatPassword = new PasswordField("Re-enter password");
		repeatPassword.setRequired(true);
		repeatPassword.addValidator(new BeanValidator(User.class, "password"));
		HorizontalLayout buttons = new HorizontalLayout();
		Button okButton = new Button("Reset");
		okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		okButton.addClickListener(clickEvent -> {
			if (passwordField.isValid() && passwordField.getValue().equals(repeatPassword.getValue())) {
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				userService.changeUserPassword(user, passwordField.getValue());
				Notification.show("Your password has been changed", Notification.Type.TRAY_NOTIFICATION);
				close();
			}
			else {
				Notification.show("Passwords do not match", Notification.Type.WARNING_MESSAGE);
				passwordField.setValue("");
				repeatPassword.setValue("");
			}
		});
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(clickEvent -> close());
		buttons.addComponent(okButton);
		buttons.addComponent(cancelButton);
		buttons.setMargin(true);
		buttons.setSpacing(true);
		parentLayout.setSizeFull();
		parentLayout.setMargin(true);
		parentLayout.setSpacing(true);
		parentLayout.addComponent(userNameField);
		parentLayout.addComponent(passwordField);
		parentLayout.addComponent(repeatPassword);
		parentLayout.addComponent(buttons);
		setWidth("400px");
		setHeight("300px");
		setCaption("Reset password");
		setModal(true);
		center();
		setContent(parentLayout);
	}
}
