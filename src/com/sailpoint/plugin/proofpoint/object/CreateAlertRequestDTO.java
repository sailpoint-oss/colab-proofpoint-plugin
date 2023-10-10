package com.sailpoint.plugin.proofpoint.object;

import java.util.List;

import com.google.gson.Gson;

/**
 * @author adam.creaney
 *
 *         DTO for create alert request.
 */
public class CreateAlertRequestDTO {
	
	private List<AlertRequest>	alerts;
	private Integer				count;
	
	public CreateAlertRequestDTO() {
		
	}
	
	public CreateAlertRequestDTO(List<AlertRequest> alerts, int count) {
		
		this.alerts = alerts;
		this.count = count;
	}
	
	public List<AlertRequest> getAlerts() {
		
		return alerts;
	}
	
	public void setAlerts(List<AlertRequest> alerts) {
		
		this.alerts = alerts;
	}
	
	public Integer getCount() {
		
		return count;
	}
	
	public void setCount(Integer count) {
		
		this.count = count;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public class AlertRequest {
		
		private String	email;
		private String	date;
		private String	type;
		
		public AlertRequest() {
			
		}
		
		public AlertRequest(String email, String date, String type) {
			
			this.email = email;
			this.date = date;
			this.type = type;
		}
		
		public String getEmail() {
			
			return email;
		}
		
		public void setEmail(String email) {
			
			this.email = email;
		}
		
		public String getDate() {
			
			return date;
		}
		
		public void setDate(String date) {
			
			this.date = date;
		}
		
		public String getType() {
			
			return type;
		}
		
		public void setType(String type) {
			
			this.type = type;
		}
		
		public String toString() {
			
			Gson gson = new Gson();
			return gson.toJson(this);
		}
	}
}
