package com.ibm;

/**
 * Created by Jan Valentik on 1/9/2016.
 */
import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

/**
 * Component that is only used to declare the sections of the side bar.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Component
@SideBarSections({
		@SideBarSection(id = Sections.VIEWS, caption = "Views"),
		@SideBarSection(id = Sections.OPERATIONS, caption = "Operations")
})
public class Sections {

	public static final String VIEWS = "views";
	public static final String OPERATIONS = "operations";
}
