package com.ibm.presentation.views;

import com.ibm.backend.domain.Request;
import com.ibm.backend.domain.RequestStatus;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Created by Jan Valentik on 1/9/2016.
 */
@SpringComponent
public class RequestForm extends AbstractForm<Request> {
	TextField leadingWBS = new TextField("WBS ID");
	TextField customerName = new TextField("Last name");
	TextField contractNumber = new TextField("Contract number");
	TextField services = new TextField("Services");
	TextField pmaName = new TextField("PMA name");
	TextField pexName = new TextField("PE Name");
	TextArea comments = new TextArea("Comments");
	DateField created = new DateField("Submitted on: ");
	TypedSelect<RequestStatus> status = new TypedSelect().withCaption("Request status");
	Button button = new Button("OK");


	@Override
	protected Component createContent() {
		setStyleName(ValoTheme.LAYOUT_CARD);
		MVerticalLayout formLayout =  new MVerticalLayout(new Header("Request details...").setHeaderLevel(3), new
				MFormLayout
				(leadingWBS,
				customerName,
				contractNumber,
				services,
				pmaName,
				pexName,
				comments,
				status,
				created).withFullWidth()).withStyleName(ValoTheme.LAYOUT_CARD);
		button.addClickListener(clickEvent -> {
			MHorizontalLayout mainContent = ((MHorizontalLayout) this.getParent());
			mainContent.removeComponent(this);
		});
		button.setStyleName(ValoTheme.BUTTON_PRIMARY);
		formLayout.add(button);
		formLayout.setEnabled(false);
		return formLayout;
	}

	@PostConstruct
	void init() {
		setEagerValidation(true);
		status.setWidthUndefined();
		status.setOptions(RequestStatus.values());
	}

}
