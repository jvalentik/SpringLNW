package com.ibm.presentation;

import com.ibm.backend.repositories.UserService;
import com.ibm.presentation.views.AddUserView;
import com.ibm.presentation.views.ResetPassword;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;
import org.vaadin.teemu.VaadinIcons;

/**
 * UI for the login screen.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@PrototypeScope
@SpringComponent
@Theme("mytheme")
public class LoginScreen extends CustomComponent {


	private final VaadinSecurity vaadinSecurity;

	private final EventBus.SessionEventBus eventBus;

	@Autowired
	private UserService userService;

	private TextField userName;

	private PasswordField passwordField;

	private CheckBox rememberMe;

	private Button login;

	@Autowired
	public LoginScreen(VaadinSecurity vaadinSecurity, EventBus.SessionEventBus eventBus) {
		this.vaadinSecurity = vaadinSecurity;
		this.eventBus = eventBus;
		init();
	}

	protected void init() {
		addStyleName("login-screen");
		CssLayout parentLayout = new CssLayout();
		parentLayout.addStyleName("login-screen");
		Component loginForm = buildLoginForm();
		VerticalLayout centeringLayout = new VerticalLayout();
		centeringLayout.setStyleName("centering-layout");
		centeringLayout.addComponent(loginForm);
		centeringLayout.setComponentAlignment(loginForm,
				Alignment.MIDDLE_CENTER);

		// information text about logging in
		CssLayout loginInformation = buildLoginInformation();

		parentLayout.addComponent(centeringLayout);
		parentLayout.addComponent(loginInformation);
		loginInformation.setSizeUndefined();
		centeringLayout.setSizeUndefined();
		parentLayout.setSizeUndefined();
		Responsive.makeResponsive(parentLayout);
		setCompositionRoot(parentLayout);
	}



	private FormLayout buildLoginForm() {
		FormLayout layout = new FormLayout();
		layout.addStyleName("login-form");
		layout.setMargin(true);
		layout.addComponent(userName = new TextField());
		userName.setCaption("IBM Intranet ID");
		userName.setWidth(15, Unit.EM);
		userName.setDescription("Enter your email");
		userName.setIcon(VaadinIcons.USER);
		layout.addComponent(passwordField = new PasswordField());
		passwordField.setCaption("Password");
		passwordField.setWidth(15, Unit.EM);
		passwordField.setDescription("Enter your password");
		passwordField.setIcon(VaadinIcons.KEY);
		CssLayout buttons = new CssLayout();
		buttons.setStyleName("buttons");
		layout.addComponent(buttons);
		buttons.addComponent(login = new Button());
		login.setCaption("Login");
		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		login.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		login.addClickListener(clickEvent -> login());
		layout.addComponent(rememberMe = new CheckBox("Remember me"));

		Button forgottenPwd = new Button("Forgot passsword?");
		forgottenPwd.setDescription("Click to reset your password");
		forgottenPwd.addStyleName(ValoTheme.BUTTON_LINK);
		forgottenPwd.addClickListener(clickEvent ->  {
			ResetPassword dialog = new ResetPassword(userService);
			UI.getCurrent().addWindow(dialog);
		});
		buttons.addComponent(forgottenPwd);
		return layout;
	}

	private CssLayout buildLoginInformation() {
		CssLayout loginInformation = new CssLayout();
		loginInformation.setStyleName("login-information");
		Label loginInfoText = new Label("<h1>Welcome</h1>"
				+ "Please sign-in. If you don't have an account, click at the link bellow to create it",
				ContentMode.HTML);
		loginInformation.addComponent(loginInfoText);
		Button requestAccess = new Button("Create account");
		requestAccess.addStyleName(ValoTheme.BUTTON_LINK);
		requestAccess.addClickListener((clickEvent) -> createAccount());
		loginInformation.addComponent(requestAccess);
		return loginInformation;
	}

	private void createAccount() {
		UI.getCurrent().addWindow(new AddUserView(userService));
	}



	private void login() {
		try {
			final Authentication authentication = vaadinSecurity.login(userName.getValue(), passwordField.getValue());
			eventBus.publish(this, new SuccessfulLoginEvent(getUI(), authentication));
		} catch (AuthenticationException ex) {
			userName.focus();
			userName.selectAll();
			passwordField.setValue("");
			Notification.show("Login faile", "Please check credentials", Notification.Type.HUMANIZED_MESSAGE);
		} catch (Exception ex) {
			Notification.show("An unexpected error occurred", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
			LoggerFactory.getLogger(getClass()).error("Unexpected error while logging in", ex);
		} finally {
			login.setEnabled(true);
		}
	}

}
