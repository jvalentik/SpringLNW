package com.ibm.operation;

/**
 * Created by Jan Valentik on 1/9/2016.
 */
/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.ibm.Sections;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * Operation that logs the user out.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Logout")
@FontAwesomeIcon(FontAwesome.POWER_OFF)
public class LogOutOperation implements Runnable {

	private final VaadinSecurity vaadinSecurity;

	@Autowired
	public LogOutOperation(VaadinSecurity vaadinSecurity) {
		this.vaadinSecurity = vaadinSecurity;
	}

	@Override
	public void run() {
		vaadinSecurity.logout();
	}
}
