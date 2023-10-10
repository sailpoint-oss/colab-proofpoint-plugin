package com.sailpoint.plugin.proofpoint.object;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad
 *
 *         DAO for data stored in iplus_proofpoint_vap_data table.
 */
public class VAPDataDAO {
	
	private String	id;
	private String	identityName;
	private String	emailId;
	private String	result;
	private String	status;
	private String	message;
	private long	created;
	private long	lastUpdated;
	
	public VAPDataDAO() {
		
		super();
	}
	
	public VAPDataDAO(String id, String identityName, String emailId, String result, String status, String message,
			long created, long lastUpdated) {
		
		super();
		this.id = id;
		this.identityName = identityName;
		this.emailId = emailId;
		this.result = result;
		this.status = status;
		this.message = message;
		this.created = created;
		this.lastUpdated = lastUpdated;
	}
	
	public String getId() {
		
		return id;
	}
	
	public void setId(String id) {
		
		this.id = id;
	}
	
	public String getIdentityName() {
		
		return identityName;
	}
	
	public void setIdentityName(String identityName) {
		
		this.identityName = identityName;
	}
	
	public String getEmailId() {
		
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		
		this.emailId = emailId;
	}
	
	public String getResult() {
		
		return result;
	}
	
	public void setResult(String result) {
		
		this.result = result;
	}
	
	public String getStatus() {
		
		return status;
	}
	
	public void setStatus(String status) {
		
		this.status = status;
	}
	
	public String getMessage() {
		
		return message;
	}
	
	public void setMessage(String message) {
		
		this.message = message;
	}
	
	public long getCreated() {
		
		return created;
	}
	
	public void setCreated(long created) {
		
		this.created = created;
	}
	
	public long getLastUpdated() {
		
		return lastUpdated;
	}
	
	public void setLastUpdated(long lastUpdated) {
		
		this.lastUpdated = lastUpdated;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
