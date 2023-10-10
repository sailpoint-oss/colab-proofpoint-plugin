package com.sailpoint.plugin.proofpoint.object;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad
 *
 *         DTO for update password response.
 */
public class UpdatePasswordResponseDTO {
	
	private final String	status;
	private final String	message;
	
	public UpdatePasswordResponseDTO(String status, String message) {
		
		super();
		this.status = status;
		this.message = message;
	}
	
	public String getStatus() {
		
		return status;
	}
	
	public String getMessage() {
		
		return message;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
