package com.sailpoint.plugin.proofpoint.object;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @author prashant.kagwad
 *
 *         Class for Click Response DTO from Proofpoint.
 */
public class ClickResponseDTO {
	
	private String		queryEndTime;
	
	@SerializedName(value = "clicksPermitted")
	// @SerializedName(value = "clicksBlocked")
	private List<Click>	clickList;
	
	public ClickResponseDTO() {
		
		super();
	}
	
	public ClickResponseDTO(String queryEndTime, List<Click> clickList) {
		
		super();
		this.queryEndTime = queryEndTime;
		this.clickList = clickList;
	}
	
	public String getQueryEndTime() {
		
		return queryEndTime;
	}
	
	public void setQueryEndTime(String queryEndTime) {
		
		this.queryEndTime = queryEndTime;
	}
	
	public List<Click> getClickList() {
		
		return clickList;
	}
	
	public void setClickList(List<Click> clickList) {
		
		this.clickList = clickList;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
