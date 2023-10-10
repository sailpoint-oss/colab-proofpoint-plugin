package com.sailpoint.plugin.proofpoint.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sailpoint.plugin.proofpoint.ProofpointHelper;
import com.sailpoint.plugin.proofpoint.db.DBHelper;
import com.sailpoint.plugin.proofpoint.object.App;
import com.sailpoint.plugin.proofpoint.object.CreateAlertRequestDTO;
import com.sailpoint.plugin.proofpoint.object.CreateAlertResponseDTO;
import com.sailpoint.plugin.proofpoint.object.DataDAO;
import com.sailpoint.plugin.proofpoint.object.ProofpointPluginConstants;
import com.sailpoint.plugin.proofpoint.object.ResponseDTO;
import com.sailpoint.plugin.proofpoint.object.SettingsDAO;
import com.sailpoint.plugin.proofpoint.object.UpdatePasswordRequestDTO;
import com.sailpoint.plugin.proofpoint.object.UpdatePasswordResponseDTO;
import com.sailpoint.plugin.proofpoint.object.VAP;
import com.sailpoint.plugin.proofpoint.utils.Utils;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Application;
import sailpoint.plugin.PluginBaseHelper;
import sailpoint.rest.plugin.BasePluginResource;
import sailpoint.rest.plugin.RequiredRight;
import sailpoint.tools.GeneralException;

/**
 * @author prashant.kagwad
 * 
 *         Class to manage all rest api calls.
 */
@Path("ProofpointPlugin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredRight("proofpointPluginAdmin")
public class ProofpointPluginRestManager extends BasePluginResource {
	
	private static final Log	log	= LogFactory.getLog(ProofpointPluginRestManager.class);
	private Response			response;
	
	public String getPluginName() {
		
		return ProofpointPluginConstants.PLUGIN_NAME;
	}
	
	@GET
	@Path("settings")
	public Response getSettings() throws GeneralException, SQLException {
		
		log.trace("Entering getSettings...");
		
		SettingsDAO settings = DBHelper.getSettings(ProofpointPluginConstants.ALL_SETTINGS);
		
		String vapListAge = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_VAP_LIST_AGE);
		
		settings.setVapListAge(vapListAge);
		
		response = Response.status(Response.Status.OK).entity(settings).build();
		
		log.trace("Exiting getSettings...");
		
		return response;
	}
	
	@PUT
	@Path("settings")
	public Response updateSettings(Map<String, Object> settingsRequest) throws GeneralException, SQLException {
		
		log.trace("Entering updateSettings...");
		
		SettingsDAO settings = Utils.parseSettings(settingsRequest);
		
		settings = DBHelper.updateSettings(ProofpointPluginConstants.ALL_SETTINGS, settings);
		
		String vapListAge = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_VAP_LIST_AGE);
		
		settings.setVapListAge(vapListAge);
		
		response = Response.status(Response.Status.OK).entity(settings).build();
		
		log.trace("Exiting updateSettings...");
		
		return response;
	}
	
	@PUT
	@Path("appListSettings")
	public Response updateAppListSettings(Map<String, Object> settingsRequest) throws GeneralException, SQLException {
		
		log.trace("Entering updateAppListSettings...");
		
		List<App> appList = Utils.parseAppList(settingsRequest);
		
		SettingsDAO settings = new SettingsDAO(appList, null, null);
		
		settings = DBHelper.updateSettings(ProofpointPluginConstants.APP_LIST, settings);
		
		String vapListAge = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_VAP_LIST_AGE);
		
		settings.setVapListAge(vapListAge);
		
		response = Response.status(Response.Status.OK).entity(settings).build();
		
		log.trace("Exiting updateAppListSettings...");
		
		return response;
	}
	
	@PUT
	@Path("vapListSettings")
	public Response updateVAPListSettings(Map<String, Object> settingsRequest) throws GeneralException, SQLException {
		
		log.trace("Entering updateVAPListSettings...");
		
		List<VAP> vapList = Utils.parseVAPList(settingsRequest);
		
		SettingsDAO settings = new SettingsDAO(null, vapList, null);
		
		settings = DBHelper.updateSettings(ProofpointPluginConstants.VAP_LIST, settings);
		
		String vapListAge = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_VAP_LIST_AGE);
		
		settings.setVapListAge(vapListAge);
		
		response = Response.status(Response.Status.OK).entity(settings).build();
		
		log.trace("Exiting updateVAPListSettings...");
		
		return response;
	}
	
	@GET
	@Path("refreshAppList")
	public Response refreshAppList() throws GeneralException, SQLException {
		
		log.trace("Entering refreshAppList...");
		
		SailPointContext context = SailPointFactory.getCurrentContext();
		
		List<Application> applicationList = ProofpointHelper.getApplicationList(context);
		log.debug("Number of applications the support - PROVISIONING, ENABLE and PASSWORD : " + applicationList.size());
		
		SettingsDAO settings = DBHelper.getSettings(ProofpointPluginConstants.ALL_SETTINGS);
		
		// Use the syncApps method to sync the apps that support - PROVISIONING, ENABLE
		// and PASSWORD
		List<App> appList = Utils.syncApps(settings.getAppList(), applicationList);
		
		if (null != appList && appList.size() > 0) {
			
			log.debug("Retrived/synced the app list from settings.");
			settings.setAppList(appList);
			DBHelper.updateSettings(ProofpointPluginConstants.APP_LIST, settings);
			
			String vapListAge = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
					ProofpointPluginConstants.MF_VAP_LIST_AGE);
			
			settings.setVapListAge(vapListAge);
			
			log.debug("App list in updated!");
			
			response = Response.status(Response.Status.OK).entity(settings).build();
		}
		
		log.trace("Exiting refreshAppList...");
		
		return response;
	}
	
	@GET
	@Path("refreshVAPList")
	public Response refreshVAPList() throws GeneralException, SQLException, IOException {
		
		log.trace("Entering refreshVAPList...");
		
		SailPointContext context = SailPointFactory.getCurrentContext();
		
		try {
			
			List<VAP> vapList = ProofpointHelper.getVAPListFromProofpoint(context);
			
			if (null != vapList && vapList.size() > 0) {
				
				log.debug("Retrived the VAP list from proofpoint. Saving it to settings.");
				
				String vapListAge = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
						ProofpointPluginConstants.MF_VAP_LIST_AGE);
				
				// Save the VAP user list to the DB. This is listed on the plugin settings UI.
				SettingsDAO settings = new SettingsDAO(null, vapList, vapListAge);
				DBHelper.updateSettings(ProofpointPluginConstants.VAP_LIST, settings);
				
				log.debug("VAP list is updated!");
				
				response = Response.status(Response.Status.OK).entity(settings).build();
			} else {
				
				log.warn("VAP list was empty!");
				SettingsDAO settings = DBHelper.getSettings(ProofpointPluginConstants.ALL_SETTINGS);
				response = Response.status(Response.Status.OK).entity(settings).build();
			}
		} catch (Exception e) {
			
			response = Response.status(Response.Status.OK)
					.entity(new ResponseDTO(ProofpointPluginConstants.STATUS_ERROR, e.getMessage())).build();
		}
		
		log.trace("Exiting refreshVAPList...");
		
		return response;
	}
	
	@POST
	@Path("UpdatePassword")
	public Response updatePasswords(Map<String, Object> request) throws GeneralException, SQLException {
		
		log.trace("Entering udpatePasswords...");
		
		boolean isPasswordResetEnabled = PluginBaseHelper.getSettingBool(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_UPDATE_PW_ENABLED);
		
		if (isPasswordResetEnabled) {
			
			UpdatePasswordRequestDTO updatePasswordRequestDTO = Utils.parseUpdatePasswordRequest(request);
			
			// Process each user to create dataDAO and run password reset logic.
			List<DataDAO> dataList = Utils.processUserListForPasswordUpdate(updatePasswordRequestDTO.getEmails());
			
			if (!dataList.isEmpty()) {
				
				// Save the user data for auditing.
				DBHelper.insertDataList(dataList);
				
				response = Response.status(Response.Status.OK)
						.entity(new UpdatePasswordResponseDTO(ProofpointPluginConstants.STATUS_SUCCESS, "")).build();
			} else {
				
				response = Response.status(Response.Status.NOT_MODIFIED)
						.entity(new UpdatePasswordResponseDTO(ProofpointPluginConstants.STATUS_SUCCESS, "")).build();
			}
		} else {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new UpdatePasswordResponseDTO(ProofpointPluginConstants.STATUS_ERROR,
							ProofpointPluginConstants.MESSAGE_PW_UPDATE_NOT_ENABLED))
					.build();
		}
		
		log.trace("Exiting updatePasswords...");
		
		return response;
	}
	
	@POST
	@Path("Alert")
	public Response createAlert(Map<String, Object> request) throws GeneralException, SQLException, ParseException {
		
		log.trace("Entering createAlert...");
		
		boolean isAlertEnabled = PluginBaseHelper.getSettingBool(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_IS_ALERT_ENABLED);
		
		if (isAlertEnabled) {
			
			CreateAlertRequestDTO createAlertRequestDTO = Utils.parseCreateAlertRequest(request);
			
			if (null == createAlertRequestDTO.getAlerts() || createAlertRequestDTO.getAlerts().isEmpty()) {
				
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new CreateAlertResponseDTO(ProofpointPluginConstants.STATUS_ERROR,
								ProofpointPluginConstants.MESSAGE_ALERT_LIST_EMPTY))
						.build();
			}
			
			// Process each user to create dataDAO and alert objects
			List<DataDAO> dataList = Utils.processAlerts(createAlertRequestDTO.getAlerts());
			
			if (!dataList.isEmpty()) {
				
				// Save the user data for auditing.
				DBHelper.insertDataList(dataList);
				
				// Check if all the user entries were found & processed.
				if (createAlertRequestDTO.getCount() != dataList.size()) {
					
					response = Response.status(Response.Status.OK)
							.entity(new CreateAlertResponseDTO(ProofpointPluginConstants.STATUS_SUCCESS,
									ProofpointPluginConstants.MESSAGE_ALERT_LIST_SUCCESS_PARTIAL))
							.build();
				} else {
					
					response = Response.status(Response.Status.OK)
							.entity(new CreateAlertResponseDTO(ProofpointPluginConstants.STATUS_SUCCESS,
									ProofpointPluginConstants.MESSAGE_ALERT_LIST_SUCCESS_COMPLETE))
							.build();
				}
				
			} else {
				
				response = Response.status(Response.Status.OK)
						.entity(new CreateAlertResponseDTO(ProofpointPluginConstants.STATUS_FAILED,
								ProofpointPluginConstants.MESSAGE_ALERT_LIST_EMPTY))
						.build();
			}
		} else {
			
			// Alerts are disabled.
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new CreateAlertResponseDTO(ProofpointPluginConstants.STATUS_ERROR,
							ProofpointPluginConstants.ALERT_CREATION_DISABLED))
					.build();
		}
		
		log.trace("Exiting createAlert...");
		
		return response;
	}
	
	// Currently not being used.
	// @PUT
	// @Path("vapList")
	// public Response updateVAPList(Map<String, Object> request) throws
	// GeneralException, SQLException {
	//
	// log.trace("Entering updateVAPList...");
	//
	// boolean isAlertsEnabled =
	// PluginBaseHelper.getSettingBool(ProofpointPluginConstants.PLUGIN_NAME,
	// ProofpointPluginConstants.MF_IS_ALERTS_ENABLED);
	//
	// VAPListRequestDTO vapListRequestDTO = Utils.parseVAPListRequest(request);
	// if (null == vapListRequestDTO.getVapList() ||
	// vapListRequestDTO.getVapList().isEmpty()) {
	//
	// log.debug("VAP list is empty. Nothing to update.");
	// return Response.status(Response.Status.OK)
	// .entity(new VAPListResponseDTO(ProofpointPluginConstants.STATUS_SUCCESS,
	// ProofpointPluginConstants.MESSAGE_VAP_LIST_EMPTY))
	// .build();
	// }
	//
	// // Save the VAP list to the DB. This is then listed on the UI.
	// SettingsDAO settingsDTO = new SettingsDAO(null,
	// vapListRequestDTO.getVapList());
	// DBHelper.updateSettings(ProofpointPluginConstants.VAP_LIST, settingsDTO);
	//
	// // Process VAP list.
	// List<VAPDataDAO> vapDataList =
	// Utils.processVAPList(vapListRequestDTO.getVapList(), isAlertsEnabled);
	// if (!vapDataList.isEmpty()) {
	//
	// DBHelper.insertVAPDataList(vapDataList);
	//
	// // Check if all the VAP entries were found & processed
	// if (vapListRequestDTO.getVapList().size() != vapDataList.size()) {
	//
	// response = Response.status(Response.Status.OK)
	// .entity(new VAPListResponseDTO(ProofpointPluginConstants.STATUS_SUCCESS,
	// ProofpointPluginConstants.MESSAGE_VAP_LIST_SUCCESS_PARTIAL))
	// .build();
	// } else {
	//
	// response = Response.status(Response.Status.OK)
	// .entity(new VAPListResponseDTO(ProofpointPluginConstants.STATUS_SUCCESS,
	// ProofpointPluginConstants.MESSAGE_VAP_LIST_SUCCESS_COMPLETE))
	// .build();
	// }
	//
	// } else {
	//
	// response = Response.status(Response.Status.OK)
	// .entity(new VAPListResponseDTO(ProofpointPluginConstants.STATUS_FAILED,
	// ProofpointPluginConstants.MESSAGE_VAP_LIST_FAILED))
	// .build();
	// }
	//
	// log.trace("Exiting updateVAPList...");
	//
	// return response;
	// }
}
