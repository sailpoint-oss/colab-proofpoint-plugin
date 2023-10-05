package com.sailpoint.plugin.proofpoint.utils;

import static sailpoint.api.SailPointFactory.getCurrentContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sailpoint.iplus.object.Constants;
import com.sailpoint.plugin.proofpoint.db.DBHelper;
import com.sailpoint.plugin.proofpoint.object.App;
import com.sailpoint.plugin.proofpoint.object.CreateAlertRequestDTO;
import com.sailpoint.plugin.proofpoint.object.DataDAO;
import com.sailpoint.plugin.proofpoint.object.ProofpointPluginConstants;
import com.sailpoint.plugin.proofpoint.object.SettingsDAO;
import com.sailpoint.plugin.proofpoint.object.UpdatePasswordRequestDTO;
import com.sailpoint.plugin.proofpoint.object.VAP;
import com.sailpoint.plugin.proofpoint.object.VAPDataDAO;
import com.sailpoint.plugin.proofpoint.object.VAPListRequestDTO;
import com.sailpoint.plugin.proofpoint.object.VAPListResponseDTO;

import sailpoint.api.IdentityService;
import sailpoint.api.PasswordGenerator;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.api.Workflower;
import sailpoint.object.Alert;
import sailpoint.object.Application;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.PasswordPolicy;
import sailpoint.object.PasswordPolicyHolder;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.QueryOptions;
import sailpoint.object.Workflow;
import sailpoint.object.WorkflowLaunch;
import sailpoint.plugin.PluginBaseHelper;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Util;

/**
 * @author prashant.kagwad
 *
 *         Utility class.
 */
public class Utils {
	
	private static final Log	log		= LogFactory.getLog(Utils.class);
	
	private static Gson			gson	= new Gson();
	
	private Utils() {
		
	}
	
	/**
	 * Function to parse the request object and convert it to an instance of
	 * SettingsDAO object.
	 * 
	 * @param request
	 *            - Request object from the API call.
	 * @return settingsDAO object.
	 * @throws GeneralException
	 */
	public static SettingsDAO parseSettings(Map<String, Object> request) throws GeneralException {
		
		log.trace("Entering parseSettings...");
		
		SettingsDAO settingsDAO = new SettingsDAO();
		String json = "";
		
		try {
			
			json = new ObjectMapper().writeValueAsString(request);
			settingsDAO = gson.fromJson(json, SettingsDAO.class);
		} catch (IOException e) {
			
			log.error("Error parsing settings from the request : " + e.getMessage());
			throw new GeneralException(
					"An exception has occured while parsing settings from the request, check system logs.");
		}
		
		log.trace("Exiting parseSettings...");
		return settingsDAO;
	}
	
	/**
	 * Function to parse the request object and convert it to list of apps.
	 * 
	 * @param request
	 *            - Request object from the API call.
	 * @return List of apps.
	 * @throws GeneralException
	 */
	public static List<App> parseAppList(Map<String, Object> request) throws GeneralException {
		
		log.trace("Entering parseAppList...");
		
		List<App> appList = null;
		
		try {
			
			String json = new ObjectMapper().writeValueAsString(request.get(ProofpointPluginConstants.APP_LIST));
			Type appListType = new TypeToken<List<App>>() {}.getType();
			appList = gson.fromJson(json, appListType);
		} catch (IOException e) {
			
			log.error("Error parsing app list from the request : " + e.getMessage());
			throw new GeneralException(
					"An exception has occured while parsing app list from the request, check system logs.");
		}
		
		log.trace("Exiting parseAppList...");
		return appList;
	}
	
	/**
	 * Function to parse the request object and convert it to list of VAP users.
	 * 
	 * @param request
	 *            - Request object from the API call.
	 * @return List of VAP users.
	 * @throws GeneralException
	 */
	public static List<VAP> parseVAPList(Map<String, Object> request) throws GeneralException {
		
		log.trace("Entering parseVAPList...");
		
		List<VAP> vapList = null;
		
		try {
			
			String json = new ObjectMapper().writeValueAsString(request.get(ProofpointPluginConstants.VAP_LIST));
			Type vapListType = new TypeToken<List<VAP>>() {}.getType();
			vapList = gson.fromJson(json, vapListType);
		} catch (IOException e) {
			
			log.error("Error parsing VAP list from the request : " + e.getMessage());
			throw new GeneralException(
					"An exception has occured while parsing VAP list from the request, check system logs.");
		}
		
		log.trace("Exiting parseVAPList...");
		return vapList;
	}
	
	/**
	 * Function to parse the request object and convert it to instance of
	 * VAPListRequestDTO object.
	 * 
	 * @param request
	 *            - Request object from the API call.
	 * @return VAPListRequestDTO object.
	 * @throws GeneralException
	 */
	public static VAPListRequestDTO parseVAPListRequest(Map<String, Object> request) throws GeneralException {
		
		log.trace("Entering parseVAPListRequest...");
		
		VAPListRequestDTO vapListRequestDTO = new VAPListRequestDTO();
		String json = "";
		
		try {
			
			json = new ObjectMapper().writeValueAsString(request);
			vapListRequestDTO = gson.fromJson(json, VAPListRequestDTO.class);
		} catch (IOException e) {
			
			log.error("Error parsing request for vap user list : " + e.getMessage());
			throw new GeneralException(
					"An exception has occured while parsing the vap user list request, check system logs.");
		}
		
		log.trace("Exiting parseVAPListRequest...");
		return vapListRequestDTO;
	}
	
	/**
	 * Function to parse the request object and convert it to instance of
	 * UpdatePasswordRequestDTO object.
	 * 
	 * @param request
	 *            - Request object from the API call.
	 * @return UpdatePasswordRequestDTO object.
	 * @throws GeneralException
	 */
	public static UpdatePasswordRequestDTO parseUpdatePasswordRequest(Map<String, Object> request)
			throws GeneralException {
		
		log.trace("Entering parseLaunchWFRequest...");
		
		UpdatePasswordRequestDTO updatePasswordRequestDTO = new UpdatePasswordRequestDTO();
		String json = "";
		
		try {
			
			json = new ObjectMapper().writeValueAsString(request);
			updatePasswordRequestDTO = gson.fromJson(json, UpdatePasswordRequestDTO.class);
		} catch (IOException e) {
			
			log.error("Error parsing request for launch workflow : " + e.getMessage());
			throw new GeneralException(
					"An exception has occured while parsing the launch workflow request, check system logs.");
		}
		
		log.trace("Exiting parseLaunchWFRequest...");
		return updatePasswordRequestDTO;
	}
	
	/**
	 * Function to fetch an Identity object based on email. It will return null if
	 * an identity with the email does not exist, else will return the identity
	 * matching the email.
	 * 
	 * @param context
	 *            - SailPointContext object.
	 * @param email
	 *            - Email address of the user/identity.
	 * @return Identity object if found else null.
	 * @throws GeneralException
	 */
	public static Identity getIdentity(SailPointContext context, String email) throws GeneralException {
		
		log.trace("Entering getIdentity...");
		
		Identity identity = null;
		QueryOptions queryOptions = new QueryOptions();
		Filter filter = Filter.eq(ProofpointPluginConstants.FILTER_EMAIL, email);
		queryOptions.addFilter(filter);
		Iterator<Identity> itr = context.search(Identity.class, queryOptions);
		
		int counter = 0;
		while (itr.hasNext()) {
			
			identity = itr.next();
			counter++;
		}
		
		if (counter < 1) {
			
			// log.debug("No identity found for email : " + email);
			log.warn("No identity found for the given email.");
		} else if (counter > 1) {
			
			// log.debug("Found multiple identities for email : " + email);
			log.warn("Found multiple identities for the given email.");
		} else {
			
			// log.debug("Found an identity for email : " + email);
			log.debug("Found an identity for the given email.");
		}
		
		log.trace("Exiting getIdentity...");
		return identity;
	}
	
	public static Application getApplication(SailPointContext context, App app) throws GeneralException {
		
		return getApplication(context, app.getAppId());
	}
	
	/**
	 * Function to get an application object based on id.
	 * 
	 * @param context
	 *            - SailPointContext object.
	 * @param applicationId
	 *            - Hibernate id of the application.
	 * @return Application object if found, else null.
	 * @throws GeneralException
	 */
	public static Application getApplication(SailPointContext context, String applicationId) throws GeneralException {
		
		Application application = context.getObjectById(Application.class, applicationId);
		// log.debug("Application : " + application.toString());
		
		return application;
	}
	
	/**
	 * Function to get a map [appId, application], where key is hibernate id & value
	 * is an application object. This map is based on the list of apps stored in the
	 * settings.
	 * 
	 * @param context
	 *            - SailPointContext object.
	 * @param appList
	 *            - List of app stored in the settings object.
	 * @return Map of applications [appId, application].
	 * @throws GeneralException
	 */
	public static Map<String, Application> getApplicationMap(SailPointContext context, List<App> appList)
			throws GeneralException {
		
		log.trace("Entering getApplicationMap...");
		
		List<Application> applicationList = new ArrayList<>();
		for (App app : appList) {
			
			applicationList.add(getApplication(context, app));
		}
		
		log.trace("Exiting getApplicationMap...");
		return applicationList.stream().collect(Collectors.toMap(Application::getId, application -> application));
	}
	
	/**
	 * Function to create dataDAO & force a password reset logic for each user in
	 * the userList(request) & for each application that is selected on the plugin
	 * settings UI.
	 * 
	 * @param emails
	 *            - List of emails that will require a password update
	 * @return List of user dataDAO object that details the execution of that
	 *         request.
	 * @throws SQLException
	 * @throws GeneralException
	 */
	public static List<DataDAO> processUserListForPasswordUpdate(List<String> emails)
			throws GeneralException, SQLException {
		
		log.trace("Entering processUserListForPasswordUpdate...");
		
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.APP_LIST);
		List<App> appList = settingsDAO.getAppList();
		
		List<DataDAO> dataList = new ArrayList<>();
		String status = ProofpointPluginConstants.STATUS_SUCCESS;
		
		// For all users, for all applications - get the associated identity &
		// application objects from IIQ and create alert for that combo
		for (String email : emails) {
			
			SailPointContext context = SailPointFactory.getCurrentContext();
			// Get the map of applications corresponding to app stored in settings
			Map<String, Application> applicationMap = getApplicationMap(context, appList);
			
			Identity identity = getIdentityFromEmail(context, email);
			if (null != identity) {
				
				// log.debug("Processing email : " + identity.getEmail());
				ProvisioningPlan plan = new ProvisioningPlan();
				plan.setIdentity(identity);
				plan.setComments(ProofpointPluginConstants.MESSAGE_PW_UPDATE_REQUEST);
				
				for (App app : appList) {
					
					// log.debug("Processing app : " + app.getAppName());
					if (app.isSelected()) {
						
						Application application = applicationMap.get(app.getAppId());
						if (application.getPasswordPolicies().isEmpty()) {
							
							log.warn("No password policy set on application. Skipping...");
							status = ProofpointPluginConstants.STATUS_PARTIAL;
						} else {
							
							List<ProvisioningPlan.AccountRequest> acctReqs = getPasswordUpdatePlan(context, identity,
									application);
							for (ProvisioningPlan.AccountRequest acctReq : acctReqs) {
								
								plan.add(acctReq);
							}
						}
					}
				}
				
				// log.debug("The provisioning plan is: " + plan.toXml());
				
				// launch the LCM Manage Passwords Workflow
				Map<String, Object> launchArgsMap = new HashMap<>();
				launchArgsMap.put(ProofpointPluginConstants.IIQ_IDENTITY_NAME, identity.getName());
				launchArgsMap.put(ProofpointPluginConstants.IIQ_IDENTITY_DISPLAY_NAME, identity.getDisplayableName());
				launchArgsMap.put(ProofpointPluginConstants.IIQ_PLAN, plan);
				launchArgsMap.put(ProofpointPluginConstants.IIQ_LAUNCHER, ProofpointPluginConstants.IIQ_SPADMIN);
				launchArgsMap.put(ProofpointPluginConstants.IIQ_REQUESTOR, ProofpointPluginConstants.IIQ_SPADMIN);
				launchArgsMap.put(ProofpointPluginConstants.IIQ_FLOW, ProofpointPluginConstants.IIQ_PWS_REQUEST);
				launchArgsMap.put(ProofpointPluginConstants.IIQ_TARGET_NAME, identity.getName());
				launchArgsMap.put(ProofpointPluginConstants.IIQ_TARGET_CLASS, ProofpointPluginConstants.IIQ_IDENTITY);
				launchArgsMap.put(ProofpointPluginConstants.IIQ_SESSION_OWNER, ProofpointPluginConstants.IIQ_SPADMIN);
				
				String updatePWWorkflow = ProofpointPluginConstants.OOTB_LCM_MANAGE_PW;
				Workflow workflow = context.getObjectByName(Workflow.class, updatePWWorkflow);
				if (workflow == null) {
					
					log.warn("Password update workflow: " + updatePWWorkflow + " not found!");
					status = ProofpointPluginConstants.STATUS_ERROR;
				}
				
				WorkflowLaunch wfLaunch = new WorkflowLaunch();
				wfLaunch.setWorkflow(workflow);
				wfLaunch.setCaseName(workflow.getName() + ProofpointPluginConstants.SPACE_CHAR + identity.getName());
				wfLaunch.setWorkflowName(workflow.getName());
				wfLaunch.setWorkflowRef(workflow.getName());
				wfLaunch.setVariables(launchArgsMap);
				
				// log.debug("Launching workflow to update password for: " +
				// identity.getName());
				log.debug("Launching workflow to update password for the given user.");
				
				Workflower workflower = new Workflower(context);
				workflower.launch(wfLaunch);
				
				String message = "";
				if (status.equalsIgnoreCase(ProofpointPluginConstants.STATUS_SUCCESS)) {
					
					message = ProofpointPluginConstants.MESSAGE_PW_SUCCESS + " - " + email;
				} else if (status.equalsIgnoreCase(ProofpointPluginConstants.STATUS_FAILED)) {
					
					message = ProofpointPluginConstants.MESSAGE_PW_FAILED;
				} else if (status.equalsIgnoreCase(ProofpointPluginConstants.STATUS_PARTIAL)) {
					
					message = ProofpointPluginConstants.MESSAGE_PW_PARTIAL;
				} else {
					
					message = ProofpointPluginConstants.MESSAGE_PW_ERROR + " - " + email;
				}
				
				Date now = new Date();
				long currentTimestamp = now.getTime();
				
				DataDAO data = new DataDAO();
				
				data.setId(UUID.randomUUID().toString());
				data.setIdentityId(UUID.randomUUID().toString());
				data.setEmailId(email);
				data.setType(ProofpointPluginConstants.TYPE_PW);
				data.setResult("[]");
				data.setStatus(ProofpointPluginConstants.STATUS_SUCCESS);
				data.setMessage(message);
				data.setCreated(currentTimestamp);
				data.setLastUpdated(currentTimestamp);
				
				dataList.add(data);
			} else {
				
				// log.debug("No action taken for user with email : " + email);
				log.debug("No action taken for the given email.");
			}
		}
		
		log.trace("Exiting processUserListForPasswordUpdate...");
		return dataList;
	}
	
	/**
	 * Function to generate a list of account requests(in case the user has multiple
	 * accounts for this identity then this will be a list else a single request
	 * object) for an identity & for an application.
	 * 
	 * @param context
	 *            - SailPointContext object.
	 * @param identity
	 *            - Identity object.
	 * @param application
	 *            - Application object.
	 * @return List of account request.
	 * @throws GeneralException
	 */
	private static List<ProvisioningPlan.AccountRequest> getPasswordUpdatePlan(SailPointContext context,
			Identity identity, Application application) throws GeneralException {
		
		log.trace("Entering getPasswordUpdatePlan...");
		
		PasswordGenerator pw = new PasswordGenerator(context);
		
		// Creating Account Request
		List<ProvisioningPlan.AccountRequest> acctReqs = new ArrayList<>();
		
		IdentityService is = new IdentityService(context);
		List<Link> links = is.getLinks(identity, application);
		
		// log.trace("the accounts of the user are... " + links);
		for (Link link : links) {
			
			ProvisioningPlan.AccountRequest acctReq = new ProvisioningPlan.AccountRequest();
			acctReq.setNativeIdentity(link.getNativeIdentity());
			acctReq.setApplication(application.getName());
			acctReq.setOperation(ProvisioningPlan.AccountRequest.Operation.Modify);
			
			// Creating Attribute Request
			ProvisioningPlan.AttributeRequest attrReq = new ProvisioningPlan.AttributeRequest();
			attrReq.setOperation(ProvisioningPlan.Operation.Set);
			attrReq.setName(ProofpointPluginConstants.IIQ_ATTRIBUTE_PW);
			
			List<PasswordPolicyHolder> passwordPolicies = application.getPasswordPolicies();
			// log.debug("The password policies are: " + passwordPolicies);
			
			Iterator<?> it = passwordPolicies.iterator();
			while (it.hasNext()) {
				
				PasswordPolicyHolder pph = (PasswordPolicyHolder) it.next();
				PasswordPolicy passwordPolicy = pph.getPolicy();
				// log.debug("The first policy is: " + passwordPolicy.toXml());
				attrReq.setValue(pw.generatePassword(passwordPolicy));
			}
			
			Util.flushIterator(it);
			
			acctReq.add(attrReq);
			acctReqs.add(acctReq);
		}
		
		log.trace("Exiting getPasswordUpdatePlan...");
		
		return acctReqs;
	}
	
	/**
	 * Function to create an alert object based on the application/identity objects
	 * passed. [Not used]
	 * 
	 * @param application
	 *            - Application object.
	 * @param identity
	 *            - Identity object.
	 * @return Alert object.
	 */
	public static Alert createAlerts(Application application, Identity identity) {
		
		log.trace("Entering createAlerts...");
		
		if (null == application || null == identity) {
			
			log.debug("Exiting createAlerts, as either the application or the identity was null.");
			return null;
		}
		
		Alert alert = new Alert();
		alert.setName(UUID.randomUUID().toString());
		alert.setDisplayName(ProofpointPluginConstants.ALERT_PREFIX + " [" + application.getName() + " - "
				+ identity.getDisplayableName() + "]");
		alert.setNativeId(identity.getId());
		// alert.setAttributes();
		alert.setAlertDate(new Date());
		alert.setTargetId(identity.getId());
		alert.setTargetType(ProofpointPluginConstants.ALERT_ATTRIBUTE_TARGET_TYPE);
		alert.setTargetDisplayName(identity.getDisplayableName());
		// alert.setType();
		alert.setSource(application);
		
		// log.debug("Alert created " + alert.getDisplayName());
		
		log.trace("Exiting createAlerts...");
		return alert;
	}
	
	/**
	 * Function to sync apps, i.e. - get the list of apps for settings, verify if it
	 * still exists (if not then remove from the list) and check if new ones are
	 * discovered (then add it to the list).
	 * 
	 * @param appList
	 *            - List of existing apps in the settings object.
	 * @param applicationList
	 *            - List of application objects in IIQ.
	 * @return appList - List of updated apps.
	 */
	public static List<App> syncApps(List<App> appList, List<Application> applicationList) {
		
		log.trace("Entering syncApps...");
		
		// Verify if the app should be still part of the app list in settings.
		Map<String, Object> applicationMap = applicationList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
		for (Iterator<App> itr = appList.iterator(); itr.hasNext();) {
			
			// Using iterator to avoid ConcurrentModificationException.
			App app = itr.next();
			// App not found.
			if (!applicationMap.containsKey(app.getAppId())) {
				
				// log.debug("Removing app : " + app.getAppName());
				itr.remove();
			}
		}
		
		// Verify if any new applications were added that support the operations.
		Map<String, Object> appMap = appList.stream().collect(Collectors.toMap(x -> x.getAppId(), x -> x));
		for (Application application : applicationList) {
			
			// New application found.
			if (!appMap.containsKey(application.getId())) {
				
				// log.debug("Found new application : " + application.getName());
				appList.add(new App(application.getId(), application.getName(), false));
			}
		}
		
		log.trace("Exiting syncApps...");
		return appList;
	}
	
	/**
	 * Function to process the VAP list. It creates vapDataDAO object & alerts for
	 * each user in the VAP list(request) & for each application that is selected on
	 * the plugin settings UI. [Not used]
	 * 
	 * @param vapList
	 *            - List of VAP users.
	 * @param isAlertsEnabled
	 *            - Boolean for alert creation logic. If set to false, it will not
	 *            create an alert object but will still create the dataDAO object.
	 * @return List of VAPDataDAO to be inserted into plugin DB.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static List<VAPDataDAO> processVAPList(List<VAP> vapList, boolean isAlertsEnabled)
			throws GeneralException, SQLException {
		
		log.trace("Entering processVAPList...");
		// log.trace("isAlertsEnabled : " + isAlertsEnabled);
		
		SailPointContext context = SailPointFactory.getCurrentContext();
		SettingsDAO settingsDAO = DBHelper.getSettings(ProofpointPluginConstants.APP_LIST);
		List<App> appList = settingsDAO.getAppList();
		
		// Get the map of applications corresponding to app stored in settings
		Map<String, Application> applicationMap = getApplicationMap(context, appList);
		
		List<VAPDataDAO> vapDataList = new ArrayList<>();
		String status = ProofpointPluginConstants.STATUS_SUCCESS;
		
		// For all users, for all applications - get the associated identity &
		// application objects from IIQ and create alert for that combo
		for (VAP vap : vapList) {
			
			Identity identity = getIdentity(context, vap.getEmailId());
			if (null != identity) {
				
				// log.debug("Processing user : " + identity.getDisplayName());
				if (isAlertsEnabled) {
					
					for (App app : appList) {
						
						// log.debug("Processing app : " + app.getAppName());
						
						if (app.isSelected()) {
							
							Alert alert = createAlerts(applicationMap.get(app.getAppId()), identity);
							if (null != alert) {
								
								log.debug("Alert created for the given user & application.");
								// log.debug("Alert created for user : " + vap.getDisplayName() + ", application
								// : " + app.getAppName());
								context.saveObject(alert);
								context.commitTransaction();
							} else {
								
								log.debug("Alert not created for the given user & application.");
								// log.debug("Alert not created for user : " + vap.getDisplayName() + ",
								// application : " + app.getAppName());
								// If one alert creation was unsuccessful, then the over all status is set to
								// failed.
								status = ProofpointPluginConstants.STATUS_FAILED;
							}
						}
					}
				}
				
				String message = "";
				if (status.equalsIgnoreCase(ProofpointPluginConstants.STATUS_SUCCESS)) {
					
					message = ProofpointPluginConstants.MESSAGE_ALERT_SUCCESS + " - " + vap.getDisplayName();
				} else if (status.equalsIgnoreCase(ProofpointPluginConstants.STATUS_FAILED)) {
					
					message = ProofpointPluginConstants.MESSAGE_ALERT_FAILED;
				} else {
					
					message = ProofpointPluginConstants.MESSAGE_ALERT_ERROR + " - " + vap.getDisplayName();
				}
				
				Date now = new Date();
				long currentTimestamp = now.getTime();
				
				VAPDataDAO vapData = new VAPDataDAO();
				
				vapData.setId(UUID.randomUUID().toString());
				vapData.setIdentityName(identity.getId());
				vapData.setEmailId(vap.getEmailId());
				vapData.setResult("[]");
				vapData.setStatus(status);
				vapData.setMessage(message);
				vapData.setCreated(currentTimestamp);
				vapData.setLastUpdated(currentTimestamp);
				
				vapDataList.add(vapData);
			} else {
				
				// log.debug("No action taken for user with email : " + vap.getEmailId());
				log.debug("No action taken for the given user.");
			}
		}
		
		log.trace("Exiting processVAPList...");
		return vapDataList;
	}
	
	// TODO : Remove this in prod
	/**
	 * Function to create a header map (Postman auth - x-api-key) for the HTTP
	 * request. The map contains x-api-key and Content-Type headers.
	 *
	 * @param apiToken
	 *            - Postman API token.
	 * @return HashMap [String, String] with Authorization and Content-Type headers.
	 */
	public static Map<String, String> createPostmanAuthHeader(String apiHeaderKey, String apiToken) {
		
		log.trace("Entering createPostmanAuthHeader...");
		
		Map<String, String> headers = new HashMap<>();
		
		if (!(null == apiToken || apiToken.isEmpty() || apiToken.equalsIgnoreCase(""))) {
			
			headers.put(apiHeaderKey, apiToken);
		} else {
			
			log.warn("Postman x-api-key missing!!!");
		}
		
		headers.put(Constants.HTTP_CONTENT_TYPE, Constants.HTTP_APPLICATION_JSON);
		
		log.trace("Exiting createPostmanAuthHeader...");
		
		return headers;
	}
	
	/**
	 * Function to correlate identity object by email addresses.
	 *
	 * @param emails
	 *            - List of email addresses to be correlated to their Identity
	 *            objects
	 * @return List of VAP user
	 * @throws GeneralException
	 */
	public static List<VAP> correlateByEmail(List<String> emails) throws GeneralException {
		
		log.trace("Entering correlateByEmail...");
		
		VAP vap = null;
		SailPointContext context = getCurrentContext();
		
		List<VAP> vapMatches = new ArrayList<>();
		
		for (String email : emails) {
			
			// log.debug("The email is: " + email);
			
			QueryOptions qo = new QueryOptions();
			Filter filter = Filter.ignoreCase(Filter.eq(ProofpointPluginConstants.ATTR_EMAIL, email));
			qo.addFilter(filter);
			
			// log.debug("The filter is: " + filter.toXml());
			ArrayList<String> properties = new ArrayList<>();
			properties.add(ProofpointPluginConstants.ATTR_NAME);
			properties.add(ProofpointPluginConstants.ATTR_DISPLAYNAME);
			
			Iterator<Object[]> it = context.search(Identity.class, qo, properties);
			
			while (it != null && it.hasNext()) {
				
				Object[] res = it.next();
				String name = (String) res[0];
				String displayName = (String) res[1];
				// log.debug("Found an identity for email: " + email);
				vap = new VAP(name, displayName, email, true);
				vapMatches.add(vap);
			}
			
			Util.flushIterator(it);
		}
		
		log.trace("Exiting correlateByEmail...");
		return vapMatches;
	}
	
	/**
	 * Function to parse the VAP List response.
	 * 
	 * @param vapListResponseDTO
	 *            - Object built from response from the Proofpoint VAP API
	 * @return List of VAP users.
	 * @throws GeneralException
	 */
	public static List<VAP> parseVAPListResponse(VAPListResponseDTO vapListResponseDTO) throws GeneralException {
		
		log.trace("Entering parseVAPListResponse..");
		
		List<VAP> vapList = new ArrayList<>();
		
		List<VAPListResponseDTO.User> users = vapListResponseDTO.getUsers();
		
		for (VAPListResponseDTO.User user : users) {
			
			VAPListResponseDTO.VAPIdentity vapIdentity = user.getIdentity();
			// log.debug("The VAP identity is: " + vapIdentity.toString());
			
			List<VAP> vapMatches = correlateByEmail(vapIdentity.getEmails());
			if (vapMatches != null) {
				
				for (VAP vapMatch : vapMatches) {
					
					if (!vapList.contains(vapMatch)) {
						
						// log.debug("Adding VAP user to list: " + vapMatch.getDisplayName());
						vapList.add(vapMatch);
					}
				}
			}
		}
		
		log.trace("Exiting parseVAPListResponse...");
		return vapList;
	}
	
	/**
	 * Function to parse the request object and convert it to CreateAlertRequestDTO.
	 *
	 * @param request
	 *            - Request object from the API call.
	 * @return CreateAlertRequestDTO object.
	 */
	public static CreateAlertRequestDTO parseCreateAlertRequest(Map<String, Object> request) throws GeneralException {
		
		log.trace("Entering parseCreateAlertRequest...");
		
		CreateAlertRequestDTO createAlertRequestDTO = new CreateAlertRequestDTO();
		String json = "";
		
		try {
			
			json = new ObjectMapper().writeValueAsString(request);
			createAlertRequestDTO = gson.fromJson(json, CreateAlertRequestDTO.class);
		} catch (IOException e) {
			
			log.error("Error parsing alert request from the request : " + e.getMessage());
			throw new GeneralException(
					"An exception has occured while parsing alert from the request, check system logs.");
		}
		
		log.trace("Exiting parseCreateAlertRequest...");
		return createAlertRequestDTO;
	}
	
	/**
	 * Function to correlate an email to a single identity. If multiple identities
	 * found, or no identity found, null is returned
	 * 
	 * @param context
	 *            - SailPoint context object.
	 * @param email
	 *            - Email Id to be correlated.
	 * @return
	 * @throws GeneralException
	 */
	public static Identity getIdentityFromEmail(SailPointContext context, String email) throws GeneralException {
		
		log.trace("Entering getIdentityFromEmail...");
		
		// log.debug("The email is: " + email);
		
		Identity identity = null;
		QueryOptions qo = new QueryOptions();
		Filter filter = Filter.ignoreCase(Filter.eq(ProofpointPluginConstants.ATTR_EMAIL, email));
		qo.addFilter(filter);
		
		// log.debug("The filter is: " + filter.toXml());
		
		int resultCount = context.countObjects(Identity.class, qo);
		if (resultCount == 1) {
			Iterator<Identity> it = context.search(Identity.class, qo);
			
			while (it != null && it.hasNext()) {
				
				identity = it.next();
				// log.debug("Found an identity for email: " + email);
				log.debug("Found an identity : " + identity.getName());
			}
			
			Util.flushIterator(it);
			
		} else {
			
			// log.warn("Multiple or no identities found that correlate to: " + email);
			log.warn("Multiple or no identities found that correlate to the given email.");
		}
		
		log.trace("Exiting getIdentityFromEmail...");
		return identity;
	}
	
	/**
	 * Function in charge of creating actual IdentityIQ Alert objects by parsing the
	 * request from Proofpoint.
	 * 
	 * @param alerts
	 *            - List of CreateAlertRequestDTO objects to translate to IdentityIQ
	 *            Alert objects
	 * @return List of DataDAO objects.
	 */
	public static List<DataDAO> processAlerts(List<CreateAlertRequestDTO.AlertRequest> alerts)
			throws GeneralException, ParseException {
		
		log.trace("Entering processAlerts...");
		
		SailPointContext context = getCurrentContext();
		List<DataDAO> dataList = new ArrayList<>();
		
		String applicationName = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_APPLICATION_NAME);
		Application application = context.getObjectByName(Application.class, applicationName);
		
		if (application == null) {
			
			log.error("No application set, or application not found for Proofpoint alert creation!");
			throw new GeneralException("No application set, or application not found for Proofpoint alert creation!");
		}
		
		for (CreateAlertRequestDTO.AlertRequest alert : alerts) {
			
			String email = alert.getEmail();
			String date = alert.getDate();
			String type = alert.getType();
			
			Identity identity = Utils.getIdentityFromEmail(context, email);
			if (identity != null) {
				
				DateFormat df = new SimpleDateFormat(ProofpointPluginConstants.PROOFPOINT_DATE_FORMAT);
				Date createDate = df.parse(date);
				
				Date now = new Date();
				long currentTimestamp = now.getTime();
				
				String alertDBId = UUID.randomUUID().toString();
				
				Alert sailpointAlert = new Alert();
				String alertName = ProofpointPluginConstants.ALERT_NAME_PREFIX + email;
				
				sailpointAlert.setName(email + ProofpointPluginConstants.SPACE_CHAR + alertDBId);
				sailpointAlert.setDisplayName(alertName);
				sailpointAlert.setNativeId(email);
				sailpointAlert.setAlertDate(createDate);
				sailpointAlert.setType(type);
				sailpointAlert.setTargetId(identity.getId());
				sailpointAlert.setTargetDisplayName(identity.getDisplayableName());
				sailpointAlert.setTargetType(ProofpointPluginConstants.ALERT_ATTRIBUTE_TARGET_TYPE);
				sailpointAlert.setSource(application);
				
				context.saveObject(sailpointAlert);
				context.commitTransaction();
				
				DataDAO data = new DataDAO();
				String message = ProofpointPluginConstants.STATUS_SUCCESS;
				
				data.setId(alertDBId);
				data.setIdentityId(identity.getId());
				data.setEmailId(email);
				data.setType(ProofpointPluginConstants.ALERT);
				data.setResult("[]");
				data.setStatus(ProofpointPluginConstants.STATUS_SUCCESS);
				data.setMessage(message);
				data.setCreated(currentTimestamp);
				data.setLastUpdated(currentTimestamp);
				
				dataList.add(data);
			} else {
				
				// log.warn("No Identity found matching email <" + email + "> - alert not
					// created!");
				log.warn("No identity found for the given email. Alert not created!");
			}
		}
		
		log.trace("Exiting processAlerts...");
		return dataList;
	}
}
