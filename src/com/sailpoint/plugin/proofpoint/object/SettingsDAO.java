package com.sailpoint.plugin.proofpoint.object;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad
 *
 *         DAO/DTO for settings stored in iplus_proofpoint_settings table.
 */
public class SettingsDAO {
	
	private List<App>	appList;
	private List<VAP>	vapList;
	private long		lastUpdated;
	private String		vapListAge;
	
	public SettingsDAO() {
		
		super();
	}
	
	public SettingsDAO(List<App> appList, List<VAP> vapList, String vapListAge) {
		
		super();
		this.appList = appList;
		this.vapList = vapList;
		this.lastUpdated = new Date().getTime();
		this.vapListAge = vapListAge;
	}
	
	public SettingsDAO(List<App> appList, List<VAP> vapList, long lastUpdated, String vapListAge) {
		
		super();
		this.appList = appList;
		this.vapList = vapList;
		this.lastUpdated = lastUpdated;
		this.vapListAge = vapListAge;
	}
	
	public List<App> getAppList() {
		
		return appList;
	}
	
	public void setAppList(List<App> appList) {
		
		this.appList = appList;
	}
	
	public List<VAP> getVapList() {
		
		return vapList;
	}
	
	public void setVapList(List<VAP> vapList) {
		
		this.vapList = vapList;
	}
	
	public long getLastUpdated() {
		
		return lastUpdated;
	}
	
	public void setLastUpdated(long lastUpdated) {
		
		this.lastUpdated = lastUpdated;
	}
	
	public String getVapListAge() {
		
		return vapListAge;
	}
	
	public void setVapListAge(String vapListAge) {
		
		this.vapListAge = vapListAge;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
