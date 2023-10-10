package com.sailpoint.plugin.proofpoint.object;

import java.util.Objects;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad
 *
 *         Class for VAP settings stored in DB for iplus_proofpoint_settings
 *         table.
 */
public class VAP {
	
	private String	identityName;
	private String	displayName;
	private String	emailId;
	private boolean	selected;
	
	public VAP() {
		
		super();
	}
	
	public VAP(String identityName, String displayName, String emailId, boolean selected) {
		
		super();
		this.identityName = identityName;
		this.displayName = displayName;
		this.emailId = emailId;
		this.selected = selected;
	}
	
	public String getIdentityName() {
		
		return identityName;
	}
	
	public void setIdentityName(String identityName) {
		
		this.identityName = identityName;
	}
	
	public String getDisplayName() {
		
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		
		this.displayName = displayName;
	}
	
	public String getEmailId() {
		
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		
		this.emailId = emailId;
	}
	
	public boolean isSelected() {
		
		return selected;
	}
	
	public void setSelected(boolean selected) {
		
		this.selected = selected;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		VAP vap = (VAP) obj;
		
		return identityName.equals(vap.identityName) && displayName.equals(vap.displayName)
				&& emailId.equals(vap.emailId) && selected == vap.selected;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(identityName, displayName, emailId, selected);
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
