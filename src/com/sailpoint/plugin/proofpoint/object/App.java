package com.sailpoint.plugin.proofpoint.object;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad
 *
 *         Class for app settings stored in DB for iplus_proofpoint_settings
 *         table.
 */
public class App {
	
	private String	appId;
	private String	appName;
	private boolean	selected;
	
	public App() {
		
		super();
	}
	
	public App(String appId, String appName, boolean selected) {
		
		super();
		this.appId = appId;
		this.appName = appName;
		this.selected = selected;
	}
	
	public String getAppId() {
		
		return appId;
	}
	
	public void setAppId(String appId) {
		
		this.appId = appId;
	}
	
	public String getAppName() {
		
		return appName;
	}
	
	public void setAppName(String appName) {
		
		this.appName = appName;
	}
	
	public boolean isSelected() {
		
		return selected;
	}
	
	public void setSelected(boolean selected) {
		
		this.selected = selected;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
