package com.sailpoint.plugin.proofpoint.object;

import java.util.List;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad
 *
 *         DTO for vap list request.
 */
public class VAPListRequestDTO {
	
	private List<VAP> vapList;
	
	public VAPListRequestDTO() {
		
		super();
	}
	
	public VAPListRequestDTO(List<VAP> vapList) {
		
		super();
		this.vapList = vapList;
	}
	
	public List<VAP> getVapList() {
		
		return vapList;
	}
	
	public void setVapList(List<VAP> vapList) {
		
		this.vapList = vapList;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
