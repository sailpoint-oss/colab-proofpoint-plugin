package com.sailpoint.plugin.proofpoint.object;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad
 *
 *         Class for Click (SIEM API call) from Proofpoint.
 *         https://help.proofpoint.com/Threat_Insight_Dashboard/API_Documentation/SIEM_API
 */
public class Click {
	
	private String	campaignId;
	private String	classification;
	private String	clickIP;
	private String	clickTime;
	private String	GUID;
	private String	recipient;
	private String	sender;
	private String	senderIP;
	private String	threatID;
	private String	threatTime;
	private String	threatURL;
	private String	url;
	private String	userAgent;
	
	public Click() {
		
		super();
	}
	
	public Click(String campaignId, String classification, String clickIP, String clickTime, String gUID,
			String recipient, String sender, String senderIP, String threatID, String threatTime, String threatURL,
			String url, String userAgent) {
		
		super();
		this.campaignId = campaignId;
		this.classification = classification;
		this.clickIP = clickIP;
		this.clickTime = clickTime;
		GUID = gUID;
		this.recipient = recipient;
		this.sender = sender;
		this.senderIP = senderIP;
		this.threatID = threatID;
		this.threatTime = threatTime;
		this.threatURL = threatURL;
		this.url = url;
		this.userAgent = userAgent;
	}
	
	public String getCampaignId() {
		
		return campaignId;
	}
	
	public void setCampaignId(String campaignId) {
		
		this.campaignId = campaignId;
	}
	
	public String getClassification() {
		
		return classification;
	}
	
	public void setClassification(String classification) {
		
		this.classification = classification;
	}
	
	public String getClickIP() {
		
		return clickIP;
	}
	
	public void setClickIP(String clickIP) {
		
		this.clickIP = clickIP;
	}
	
	public String getClickTime() {
		
		return clickTime;
	}
	
	public void setClickTime(String clickTime) {
		
		this.clickTime = clickTime;
	}
	
	public String getGUID() {
		
		return GUID;
	}
	
	public void setGUID(String gUID) {
		
		GUID = gUID;
	}
	
	public String getRecipient() {
		
		return recipient;
	}
	
	public void setRecipient(String recipient) {
		
		this.recipient = recipient;
	}
	
	public String getSender() {
		
		return sender;
	}
	
	public void setSender(String sender) {
		
		this.sender = sender;
	}
	
	public String getSenderIP() {
		
		return senderIP;
	}
	
	public void setSenderIP(String senderIP) {
		
		this.senderIP = senderIP;
	}
	
	public String getThreatID() {
		
		return threatID;
	}
	
	public void setThreatID(String threatID) {
		
		this.threatID = threatID;
	}
	
	public String getThreatTime() {
		
		return threatTime;
	}
	
	public void setThreatTime(String threatTime) {
		
		this.threatTime = threatTime;
	}
	
	public String getThreatURL() {
		
		return threatURL;
	}
	
	public void setThreatURL(String threatURL) {
		
		this.threatURL = threatURL;
	}
	
	public String getUrl() {
		
		return url;
	}
	
	public void setUrl(String url) {
		
		this.url = url;
	}
	
	public String getUserAgent() {
		
		return userAgent;
	}
	
	public void setUserAgent(String userAgent) {
		
		this.userAgent = userAgent;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
