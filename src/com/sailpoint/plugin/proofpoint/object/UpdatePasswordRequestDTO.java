package com.sailpoint.plugin.proofpoint.object;

import java.util.List;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad
 *
 *         DTO for update password request.
 */
public class UpdatePasswordRequestDTO {
	
	private List<String> emails;
	
	public UpdatePasswordRequestDTO() {
		
		super();
	}
	
	public UpdatePasswordRequestDTO(List<String> emails) {
		
		super();
		this.emails = emails;
	}
	
	public List<String> getEmails() {
		
		return emails;
	}
	
	public void setEmails(List<String> emails) {
		
		this.emails = emails;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
