package com.ibm.backend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Jan Valentik on 1/9/2016.
 */
public class PasswordEncodeGenerator {

	public static String getHashPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		System.out.println(hashedPassword);
        return hashedPassword;
	}

}
