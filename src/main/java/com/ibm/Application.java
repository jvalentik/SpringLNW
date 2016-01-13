package com.ibm;

import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity;
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer;


@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
@EnableVaadinManagedSecurity
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	SystemMessagesProvider systemMessagesProvider() {
		return new SystemMessagesProvider() {
			@Override
			public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
				CustomizedSystemMessages systemMessages = new CustomizedSystemMessages();
				systemMessages.setSessionExpiredNotificationEnabled(false);
				return systemMessages;
			}
		};
	}

	/**
	 * Configure the authentication manager.
	 */
	@Configuration
	static class AuthenticationConfiguration implements AuthenticationManagerConfigurer {

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
					.withUser("user").password("user").roles("USER")
					.and()
					.withUser("admin").password("admin").roles("ADMIN");
		}
	}
}
