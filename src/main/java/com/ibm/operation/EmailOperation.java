package com.ibm.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.backend.domain.Request;
import com.ibm.backend.domain.User;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

import java.util.Map;
import java.util.Set;

/**
 * Created by Jan Valentik on 1/10/2016.
 */
public class EmailOperation {
	private static String userName;
	private static String password;

	public static void sendResetLink(User user, String token) throws Exception {
		VCAPParser();
		if(userName == null || password == null) {
			throw new Exception("VCAP was not parsed correctly");
		}
		SendGrid sendGrid = new SendGrid("powerj", "Laurinka2005");
		SendGrid.Email email = new SendGrid.Email();
		email.addTo("johny.valentik@gmail.com");
		email.setFrom("lnw@sk.ibm.com");
		email.setSubject("Link to reset password to LNW Tool");
		email.setHtml("<body><p><h4>If you did not request to reset password to LNW Tool, please ignore this " +
				"email</h4></p>" +
				"<p><a href=\"?pwd_reset_id=" + user.getId() + "&token=" + token
				 + ">Click here to reset your LNW Tool password</a></p><body>");
		try {
			SendGrid.Response response = sendGrid.send(email);
			System.out.println(response.getMessage());
		}
		catch (SendGridException ex) {
			ex.printStackTrace();
		}
	}

	public static void sendEmail(Request request) throws Exception {
		VCAPParser();
		if(userName == null || password == null) {
			throw new Exception("VCAP was not parsed correctly");
		}
		SendGrid sendGrid = new SendGrid(userName, password);
		SendGrid.Email email = new SendGrid.Email();
		//email.addTo(request.getPmaName());
		email.addTo(request.getPmaName());
		email.addCc(request.getCreatedByUser());
		email.setFrom("lnw@sk.ibm.com.net");
		email.setSubject(request.getCreatedByUser() + " sent you a new request in the LNW Tool");
		email.setHtml("<body><p>There is a new request for you in the LNW Tool --> " +
				"<a href=\"http://lnwtool.eu-gb.mybluemix.net/?request_id=" + request.getId() + "\">Link to this request</a></p>" +
				"<a href=\"http://lnwtool.eu-gb.mybluemix.net\">Link to LNW Tool</a>" +
				"<p><h4>Request details</h4></p>" +
				"<p>Customer: " + request.getCustomerName() + "</p>" +
				"<p>Contract no.: " + request.getContractNumber() + "</p>" +
				"<p>OCPS: " + request.getServices() + "</p>" +
				"<p>PE: " + request.getPexName() + "</p></body>");
		try {
			SendGrid.Response response = sendGrid.send(email);
			System.out.println(response.getMessage());
		}
		catch (SendGridException ex) {
			ex.printStackTrace();
		}
	}

	private static void VCAPParser() {
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		System.out.println(VCAP_SERVICES);
		String serviceName = null;

		if (VCAP_SERVICES != null) {
			// parse the VCAP JSON structure
			JsonObject obj = (JsonObject) new JsonParser().parse(VCAP_SERVICES);
			Map.Entry<String, JsonElement> dbEntry = null;
			Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
			for (Map.Entry<String, JsonElement> eachEntry : entries) {
				System.out.println(eachEntry.getKey());
				if (eachEntry.getKey().toUpperCase().equals("SENDGRID")) {
					dbEntry = eachEntry;
					break;
				}
			}
			if (dbEntry == null) {
				throw new RuntimeException("Could not find sendgrid key in VCAP_SERVICES env variable");
			}

			obj = (JsonObject) ((JsonArray) dbEntry.getValue()).get(0);
			serviceName = (String) dbEntry.getKey();
			System.out.println("Service Name - " + serviceName);

			obj = (JsonObject) obj.get("credentials");

			userName = obj.get("username").getAsString();
			password = obj.get("password").getAsString();
		}
	}
}

