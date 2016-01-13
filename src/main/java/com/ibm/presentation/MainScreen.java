package com.ibm.presentation;

/**
 * Created by Jan Valentik on 1/13/2016.
 */

import com.ibm.presentation.views.AccessDeniedView;
import com.ibm.presentation.views.ErrorView;
import com.vaadin.navigator.Navigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter;

/**
 * Full-screen UI component that allows the user to navigate between views, and log out.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@UIScope
@SpringComponent
public class MainScreen extends CustomComponent {

	@Autowired
	public MainScreen(final VaadinSecurity vaadinSecurity, SpringViewProvider springViewProvider, ValoSideBar sideBar) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		setCompositionRoot(layout);
		setSizeFull();

		// By adding a security item filter, only views that are accessible to the user will show up in the side bar.
		sideBar.setItemFilter(new VaadinSecurityItemFilter(vaadinSecurity));
		layout.addComponent(sideBar);

		CssLayout viewContainer = new CssLayout();
		viewContainer.setSizeFull();
		layout.addComponent(viewContainer);
		layout.setExpandRatio(viewContainer, 1f);

		Navigator navigator = new Navigator(UI.getCurrent(), viewContainer);
		// Without an AccessDeniedView, the view provider would act like the restricted views did not exist at all.
		springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		navigator.addProvider(springViewProvider);
		navigator.setErrorView(ErrorView.class);
		navigator.navigateTo(navigator.getState());
	}

}
