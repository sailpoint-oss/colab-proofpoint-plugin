package com.sailpoint.plugin.proofpoint;

import static sailpoint.api.SailPointFactory.getCurrentContext;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.sailpoint.iplus.HTTPUtils;
import com.sailpoint.iplus.object.ResponseDTO;
import com.sailpoint.plugin.proofpoint.db.DBHelper;
import com.sailpoint.plugin.proofpoint.object.Click;
import com.sailpoint.plugin.proofpoint.object.ClickResponseDTO;
import com.sailpoint.plugin.proofpoint.object.DataDAO;
import com.sailpoint.plugin.proofpoint.object.ProofpointPluginConstants;
import com.sailpoint.plugin.proofpoint.object.SettingsDAO;
import com.sailpoint.plugin.proofpoint.object.VAP;
import com.sailpoint.plugin.proofpoint.object.VAPDataDAO;
import com.sailpoint.plugin.proofpoint.object.VAPListResponseDTO;
import com.sailpoint.plugin.proofpoint.utils.Utils;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Alert;
import sailpoint.object.Application;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.QueryOptions;
import sailpoint.plugin.PluginBaseHelper;
import sailpoint.tools.GeneralException;

/**
 * @author prashant.kagwad
 *
 *         Helper class for the functions that are used by the App & VAP Task
 *         executor classes.
 */

public class ProofpointHelper {
	
	private static Log log = LogFactory.getLog(ProofpointHelper.class);
	
	private ProofpointHelper() {
		
	}
	
	/**
	 * Function to get an Identity object from a given alertId.
	 * 
	 * @param alertId
	 *            - Alert Id of the alert.
	 * @return Identity object associated with the alert.
	 * @throws GeneralException
	 */
	public static Identity getIdentityFromAlertId(String alertId) throws GeneralException {
		
		log.trace("Entering getIdentityFromAlertId...");
		
		// log.debug("AlertId : " + alertId);
		if (com.sailpoint.iplus.Utils.isNullOrEmpty(alertId)) {
			
			log.warn("Alert Id is null!");
			return null;
		}
		
		SailPointContext context = SailPointFactory.getCurrentContext();
		Alert alert = context.getObjectById(Alert.class, alertId);
		if (com.sailpoint.iplus.Utils.isNullOrEmpty(alert.getTargetId())) {
			
			log.warn("Identity Id is null!");
			return null;
		}
		
		// log.debug("IdentityId : " + alert.getTargetId());
		log.trace("Exiting getIdentityFromAlertId...");
		return context.getObjectById(Identity.class, alert.getTargetId());
	}
	
	/**
	 * Function to fetch SIEM clicks from Proofpoint. The API will get all the
	 * events in the past hour from the time its called.
	 * 
	 * @param context
	 *            SailPoint context object.
	 * @return List of Click objects.
	 * @throws GeneralException
	 * @throws IOException
	 */
	public static List<Click> getClicks(SailPointContext context) throws GeneralException, IOException {
		
		log.trace("Entering getClicks...");
		
		Gson gson = new Gson();
		
		List<Click> clickList = null;
		String baseURL = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_BASE_URL);
		
		if (com.sailpoint.iplus.Utils.isNullOrEmpty(baseURL)) {
			
			log.error("Base URL is null!");
			throw new GeneralException("Base URL is null!");
		}
		
		String servicePrincipal = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_SERVICE_PRINCIPAL);
		
		if (com.sailpoint.iplus.Utils.isNullOrEmpty(servicePrincipal)) {
			
			log.error("Service Principal is null!");
			throw new GeneralException("Service Principal is null!");
		}
		
		String secret = PluginBaseHelper.getSettingSecret(context, ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_SECRET);
		
		if (com.sailpoint.iplus.Utils.isNullOrEmpty(secret)) {
			
			log.error("Secret is null!");
			throw new GeneralException("Secret is null!");
		}
		
		// Currently only Basic auth is supported by Proofpoint API's.
		Map<String, String> header = HTTPUtils.createBasicHeader(servicePrincipal, secret);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(ProofpointPluginConstants.PROOFPOINT_DATE_FORMAT);
		String clicksURL = baseURL + ProofpointPluginConstants.CLICKS_PERMITTED_URL
				+ ProofpointPluginConstants.CLICKS_URL_PRARMETERS + sdf.format((now.getTime()));
		
		log.debug("Clicks URL : " + clicksURL);
		ResponseDTO response = HTTPUtils.executeGetRequest(clicksURL, header);
		// log.debug("Response : " + response.toString());
		
		if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
			
			String json = response.getResponseData();
			
			if (json.startsWith("\"")) {
				
				log.trace("JSON starts with single quote!");
			}
			
			ClickResponseDTO clickResponseDTO = gson.fromJson(json, ClickResponseDTO.class);
			log.debug("Clicks request was completed successfully. Number of clicks received : "
					+ clickResponseDTO.getClickList().size());
			clickList = clickResponseDTO.getClickList();
		} else {
			
			log.debug("Click list request was not completed. Check the above response code for more details.");
		}
		
		log.trace("Exiting getClicks...");
		return clickList;
	}
	
	/**
	 * Function to fetch list of applications from IdentityIQ that support -
	 * PROVISIONING, ENABLE and PASSWORD as part of its featuresString.
	 * 
	 * @param context
	 *            SailPoint context object.
	 * @return List of Application objects.
	 * @throws GeneralException
	 */
	public static List<Application> getApplicationList(SailPointContext context) throws GeneralException {
		
		log.trace("Entering getApplicationList...");
		
		// Get the list of applications that have - PROVISIONING, ENABLE and PASSWORD in
		// their featuresString.
		QueryOptions query = new QueryOptions();
		List<Filter> filterList = new ArrayList<>();
		
		// Filter for PROVISIONING
		Filter isProvisioning = Filter.like(ProofpointPluginConstants.FILTER_FEATURES_STRING,
				ProofpointPluginConstants.FILTER_PROVISIONING);
		filterList.add(isProvisioning);
		
		// Filter for ENABLE
		Filter isEnabled = Filter.like(ProofpointPluginConstants.FILTER_FEATURES_STRING,
				ProofpointPluginConstants.FILTER_ENABLE);
		filterList.add(isEnabled);
		
		// Filter for PASSWORD
		Filter isPW = Filter.like(ProofpointPluginConstants.FILTER_FEATURES_STRING,
				ProofpointPluginConstants.FILTER_PW);
		filterList.add(isPW);
		
		// Perform an AND on all above filters.
		Filter filter = Filter.and(filterList);
		query.add(filter);
		
		log.trace("Exiting getApplicationList...");
		return context.getObjects(Application.class, query);
	}
	
	/**
	 * Function to update password for a user.
	 * 
	 * @param context
	 *            SailPoint context object.
	 * @return List of Application objects.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static String launchUpdatePasswordWorkFlow(String email) throws GeneralException, SQLException {
		
		log.trace("Entering launchUpdatePasswordWorkFlow...");
		
		boolean isPasswordResetEnabled = PluginBaseHelper.getSettingBool(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_UPDATE_PW_ENABLED);
		
		String message = "";
		if (isPasswordResetEnabled) {
			
			List<String> emails = new ArrayList<>();
			emails.add(email);
			
			// Process each user to create dataDAO and run password reset logic.
			List<DataDAO> dataList = Utils.processUserListForPasswordUpdate(emails);
			
			// Save the user data for auditing.
			DBHelper.insertDataList(dataList);
			
			if (!dataList.isEmpty()) {
				
				log.debug(ProofpointPluginConstants.STATUS_SUCCESS);
				message = ProofpointPluginConstants.STATUS_SUCCESS;
			} else {
				
				log.debug(ProofpointPluginConstants.STATUS_SUCCESS + " - Nothing Modified!");
				message = ProofpointPluginConstants.STATUS_SUCCESS + " - Nothing Modified!";
			}
		} else {
			
			log.warn(ProofpointPluginConstants.MESSAGE_PW_UPDATE_NOT_ENABLED);
			message = ProofpointPluginConstants.MESSAGE_PW_UPDATE_NOT_ENABLED;
		}
		
		log.trace("Exiting launchUpdatePasswordWorkFlow...");
		return message;
	}
	
	/**
	 * Function to get the VAP list from Proofpoint. An external call is made to
	 * fetch this list from Proofpoint based on the connectivity settings done in
	 * the manifest of this plugin.
	 * 
	 * @return List of VAP users.
	 * @throws IOException
	 * @throws GeneralException
	 */
	public static List<VAP> getVAPListFromProofpoint(SailPointContext context) throws IOException, GeneralException {
		
		log.trace("Entering getVAPListFromProofpoint...");
		
		Gson gson = new Gson();
		
		List<VAP> vapList = null;
		String baseURL = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_BASE_URL);
		
		if (com.sailpoint.iplus.Utils.isNullOrEmpty(baseURL)) {
			
			log.error("Base URL is null!");
			throw new GeneralException("Base URL is null!");
		}
		
		String servicePrincipal = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_SERVICE_PRINCIPAL);
		
		if (com.sailpoint.iplus.Utils.isNullOrEmpty(servicePrincipal)) {
			
			log.error("Service Principal is null!");
			throw new GeneralException("Service Principal is null!");
		}
		
		String secret = PluginBaseHelper.getSettingSecret(context, ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_SECRET);
		
		if (com.sailpoint.iplus.Utils.isNullOrEmpty(secret)) {
			
			log.error("Secret is null!");
			throw new GeneralException("Secret is null!");
		}
		
		// Currently only Basic auth is supported by Proofpoint API's.
		Map<String, String> header = HTTPUtils.createBasicHeader(servicePrincipal, secret);
		String vapURL = baseURL + ProofpointPluginConstants.VAP_URL;
		
		// Take into account whether the administrator has specified a VAP list age...
		String vapListAge = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_VAP_LIST_AGE);
		
		List<String> validAges = Arrays.asList("14", "30", "90");
		
		if (vapListAge != null) {
			
			if (!validAges.contains(vapListAge)) {
				
				vapListAge = ProofpointPluginConstants.DEFAULT_VAP_LIST_AGE;
			}
			vapURL = vapURL + ProofpointPluginConstants.VAP_LIST_URL_PARAMETERS + vapListAge;
		}
		
		log.debug("The final URL to retrieve VAP list is: " + vapURL);
		ResponseDTO response = HTTPUtils.executeGetRequest(vapURL, header);
		// log.debug("Response is: " + response.toString());
		
		if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
			
			String json = response.getResponseData();
			if (json.startsWith("\"")) {
				
				log.trace("JSON starts with single quote!");
			}
			
			VAPListResponseDTO vapListResponseDTO = gson.fromJson(json, VAPListResponseDTO.class);
			
			log.debug("VAP list request was completed successfully. Number of VAP's : "
					+ vapListResponseDTO.getUsers().size());
			vapList = com.sailpoint.plugin.proofpoint.utils.Utils.parseVAPListResponse(vapListResponseDTO);
		} else {
			
			log.debug("VAP list request was not completed. Check the above response code for more details.");
		}
		
		log.trace("Exiting getVAPListFromProofpoint...");
		return vapList;
	}
	
	/**
	 * Function to return a list of VAP username's from plugin database.
	 * 
	 * @return List of user names on the VAP list.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<String> getVAPListNames() throws SQLException, GeneralException {
		
		log.trace("Entering getVAPListNames...");
		
		Set<String> vapListNames = new HashSet<>();
		
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.VAP_LIST);
		List<VAP> vapListObjects = settingsDAO.getVapList();
		
		for (VAP vap : vapListObjects) {
			
			vapListNames.add(vap.getIdentityName());
		}
		
		log.trace("Existing getVAPListNames...");
		return vapListNames.stream().collect(Collectors.toList());
	}
	
	/**
	 * Function to return a list of selected VAP username's from plugin database.
	 * 
	 * @return List of user names on the VAP list that are selected from the custom
	 *         plugin UI.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<String> getSelectedVAPListNames() throws SQLException, GeneralException {
		
		log.trace("Entering getSelectedVAPListNames...");
		
		Set<String> vapListNames = new HashSet<>();
		
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.VAP_LIST);
		List<VAP> vapListObjects = settingsDAO.getVapList();
		
		for (VAP vap : vapListObjects) {
			
			if (vap.isSelected()) {
				
				vapListNames.add(vap.getIdentityName());
			}
		}
		
		log.trace("Existing getSelectedVAPListNames...");
		return vapListNames.stream().collect(Collectors.toList());
	}
	
	/**
	 * Function to return a list of VAP email Id's from plugin database.
	 * 
	 * @return List of email Id's on the VAP list.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<String> getVAPListEmails() throws SQLException, GeneralException {
		
		log.trace("Entering getVAPListEmails...");
		
		Set<String> vapListEmails = new HashSet<>();
		
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.VAP_LIST);
		List<VAP> vapListObjects = settingsDAO.getVapList();
		
		for (VAP vap : vapListObjects) {
			
			vapListEmails.add(vap.getEmailId());
		}
		
		log.trace("Existing getVAPListEmails...");
		return vapListEmails.stream().collect(Collectors.toList());
	}
	
	/**
	 * Function to return a list of selected VAP email Id's from plugin database.
	 * 
	 * @return List of selected email Id's on the VAP list.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<String> getSelectedVAPListEmails() throws SQLException, GeneralException {
		
		log.trace("Entering getSelectedVAPListEmails...");
		
		Set<String> vapListEmails = new HashSet<>();
		
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.VAP_LIST);
		List<VAP> vapListObjects = settingsDAO.getVapList();
		
		for (VAP vap : vapListObjects) {
			
			if (vap.isSelected()) {
				
				vapListEmails.add(vap.getEmailId());
			}
		}
		
		log.trace("Existing getSelectedVAPListEmails...");
		return vapListEmails.stream().collect(Collectors.toList());
	}
	
	/**
	 * Function to return a list of identity objects from plugin database that are
	 * on VAP list.
	 * 
	 * @return List of identity objects on the VAP list.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<Identity> getVAPListIdentities() throws SQLException, GeneralException {
		
		log.trace("Entering getVAPListIdentities...");
		
		Set<Identity> vapListIdentities = new HashSet<>();
		
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.VAP_LIST);
		List<VAP> vapListObjects = settingsDAO.getVapList();
		
		// Grab a context so we can fetch Identity objects based on the name
		SailPointContext context = getCurrentContext();
		
		for (VAP vap : vapListObjects) {
			
			Identity identity = context.getObjectByName(Identity.class, vap.getIdentityName());
			vapListIdentities.add(identity);
		}
		
		log.trace("Existing getVAPListIdentities...");
		return vapListIdentities.stream().collect(Collectors.toList());
	}
	
	/**
	 * Function to return a list of selected identity objects from plugin database
	 * that are on VAP list.
	 * 
	 * @return List of selected identity objects on the VAP list.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<Identity> getSelectedVAPListIdentities() throws SQLException, GeneralException {
		
		log.trace("Entering getSelectedVAPListIdentities...");
		
		Set<Identity> vapListIdentities = new HashSet<>();
		
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.VAP_LIST);
		List<VAP> vapListObjects = settingsDAO.getVapList();
		
		// Grab a context so we can fetch Identity objects based on the name
		SailPointContext context = getCurrentContext();
		
		for (VAP vap : vapListObjects) {
			
			if (vap.isSelected()) {
				
				Identity identity = context.getObjectByName(Identity.class, vap.getIdentityName());
				vapListIdentities.add(identity);
			}
		}
		
		log.trace("Existing getSelectedVAPListIdentities...");
		return vapListIdentities.stream().collect(Collectors.toList());
	}
	
	/**
	 * Function to return the last updated timestamp of the VAP list.
	 * 
	 * @return Timestamp in yyyy-MM-dd HH:mm:ss.SSS format.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static String getLastUpdatedDate() throws SQLException, GeneralException {
		
		log.trace("Entering getLastUpdatedDate...");
		
		SimpleDateFormat sdf = new SimpleDateFormat(ProofpointPluginConstants.PROOFPOINT_DATE_FORMAT);
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.VAP_LIST);
		
		log.trace("Exiting getLastUpdatedDate...");
		return sdf.format(new Date(settingsDAO.getLastUpdated()));
	}
	
	/**
	 * Function to return a list of VAP email Id's from plugin database with
	 * "Success" status of execution.
	 * 
	 * @return List of email Id's on the VAP list.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<String> getUsersWithStatusSuccess() throws SQLException, GeneralException {
		
		log.trace("Entering getUsersWithStatusSuccess...");
		
		Set<String> vapListEmails = new HashSet<>();
		
		List<VAPDataDAO> vapDataList = DBHelper.getVAPDataList();
		
		for (VAPDataDAO vapData : vapDataList) {
			
			if (vapData.getStatus().equalsIgnoreCase(ProofpointPluginConstants.STATUS_SUCCESS)) {
				
				vapListEmails.add(vapData.getEmailId());
			}
		}
		
		log.trace("Existing getUsersWithStatusSuccess...");
		return vapListEmails.stream().collect(Collectors.toList());
	}
	
	/**
	 * Function to return a list of VAP email Id's from plugin database with
	 * "Failed" status of execution.
	 * 
	 * @return List of email Id's on the VAP list.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<String> getUsersWithStatusFailure() throws SQLException, GeneralException {
		
		log.trace("Entering getUsersWithStatusFailure...");
		
		Set<String> vapListEmails = new HashSet<>();
		
		List<VAPDataDAO> vapDataList = DBHelper.getVAPDataList();
		
		for (VAPDataDAO vapData : vapDataList) {
			
			if (vapData.getStatus().equalsIgnoreCase(ProofpointPluginConstants.STATUS_FAILED)) {
				
				vapListEmails.add(vapData.getEmailId());
			}
		}
		
		log.trace("Existing getUsersWithStatusFailure...");
		return vapListEmails.stream().collect(Collectors.toList());
	}
}
