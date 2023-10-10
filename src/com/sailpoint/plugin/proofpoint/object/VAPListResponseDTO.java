package com.sailpoint.plugin.proofpoint.object;

import java.util.List;

import com.google.gson.Gson;

/**
 * @author prashant.kagwad & adam.creaney
 *
 *         DTO for vap user list response.
 */
public class VAPListResponseDTO {
	
	private List<User>	users;
	private int			totalVapUsers;
	private float		averageAttackIndex;
	private float		vapAttackIndexThreshold;
	
	public VAPListResponseDTO() {
		
	}
	
	public VAPListResponseDTO(List<User> users, int totalVapUsers, int averageAttackIndex,
			int vapAttackIndexThreshold) {
		
		this.users = users;
		this.totalVapUsers = totalVapUsers;
		this.averageAttackIndex = averageAttackIndex;
		this.vapAttackIndexThreshold = vapAttackIndexThreshold;
	}
	
	public List<User> getUsers() {
		
		return users;
	}
	
	public void setUsers(List<User> users) {
		
		this.users = users;
	}
	
	public Integer getTotalVapUsers() {
		
		return totalVapUsers;
	}
	
	public void setTotalVapUsers(Integer totalVapUsers) {
		
		this.totalVapUsers = totalVapUsers;
	}
	
	public float getAverageAttackIndex() {
		
		return averageAttackIndex;
	}
	
	public void setAverageAttackIndex(float averageAttackIndex) {
		
		this.averageAttackIndex = averageAttackIndex;
	}
	
	public float getVapAttackIndexThreshold() {
		
		return vapAttackIndexThreshold;
	}
	
	public void setVapAttackIndexThreshold(float vapAttackIndexThreshold) {
		
		this.vapAttackIndexThreshold = vapAttackIndexThreshold;
	}
	
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public class User {
		
		private VAPIdentity			identity;
		private ThreatStatistics	threatStatistics;
		
		public User() {
			
		}
		
		public User(VAPIdentity identity, ThreatStatistics threatStatistics) {
			
			this.identity = identity;
			this.threatStatistics = threatStatistics;
		}
		
		public VAPIdentity getIdentity() {
			
			return identity;
		}
		
		public void setIdentity(VAPIdentity identity) {
			
			this.identity = identity;
		}
		
		public ThreatStatistics getThreatStatistics() {
			
			return threatStatistics;
		}
		
		public void setThreatStatistics(ThreatStatistics threatStatistics) {
			
			this.threatStatistics = threatStatistics;
		}
		
		public String toString() {
			
			Gson gson = new Gson();
			return gson.toJson(this);
		}
	}
	
	public class VAPIdentity {
		
		private String			guid;
		private String			customerUserId;
		private List<String>	emails;
		private String			name;
		private String			department;
		private String			location;
		private String			title;
		private boolean			vip;
		
		public VAPIdentity() {
			
		}
		
		public VAPIdentity(String guid, String customerUserId, List<String> emails, String name, String department,
				String location, String title, boolean vip) {
			
			this.guid = guid;
			this.customerUserId = customerUserId;
			this.emails = emails;
			this.name = name;
			this.department = department;
			this.location = location;
			this.title = title;
			this.vip = vip;
		}
		
		public String getGuid() {
			
			return guid;
		}
		
		public void setGuid(String guid) {
			
			this.guid = guid;
		}
		
		public String getCustomerUserId() {
			
			return customerUserId;
		}
		
		public void setCustomerUserId(String customerUserId) {
			
			this.customerUserId = customerUserId;
		}
		
		public List<String> getEmails() {
			
			return emails;
		}
		
		public void setEmails(List<String> emails) {
			
			this.emails = emails;
		}
		
		public String getName() {
			
			return name;
		}
		
		public void setName(String name) {
			
			this.name = name;
		}
		
		public String getDepartment() {
			
			return department;
		}
		
		public void setDepartment(String department) {
			
			this.department = department;
		}
		
		public String getLocation() {
			
			return location;
		}
		
		public void setLocation(String location) {
			
			this.location = location;
		}
		
		public String getTitle() {
			
			return title;
		}
		
		public void setTitle(String title) {
			
			this.title = title;
		}
		
		public boolean isVip() {
			
			return vip;
		}
		
		public void setVip(boolean vip) {
			
			this.vip = vip;
		}
		
		public String toString() {
			
			Gson gson = new Gson();
			return gson.toJson(this);
		}
	}
	
	public class ThreatStatistics {
		
		private int attackIndex;
		
		public ThreatStatistics() {
			
		}
		
		public ThreatStatistics(int attackIndex) {
			
			this.attackIndex = attackIndex;
		}
		
		public int getAttackIndex() {
			
			return attackIndex;
		}
		
		public void setAttackIndex(int attackIndex) {
			
			this.attackIndex = attackIndex;
		}
		
		public String toString() {
			
			Gson gson = new Gson();
			return gson.toJson(this);
		}
	}
}
