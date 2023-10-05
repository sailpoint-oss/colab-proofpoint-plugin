package com.sailpoint.plugin.proofpoint.object;

/**
 * @author prashant.kagwad
 *
 *         Constants file for Proofpoint plugin.
 */
public class ProofpointPluginConstants {
	
	// Plugin Constants
	public static final String	PLUGIN_NAME							= "ProofpointPlugin";
	
	// Manifest Constants
	public static final String	MF_BASE_URL							= "baseUrl";
	public static final String	MF_SERVICE_PRINCIPAL				= "servicePrincipal";
	public static final String	MF_SECRET							= "secret";
	public static final String	MF_APPLICATION_NAME					= "applicationName";
	public static final String	MF_VAP_LIST_AGE						= "vapListAge";
	public static final String	MF_UPDATE_PW_ENABLED				= "isUpdatePwEnabled";
	public static final String	MF_IS_ALERT_ENABLED					= "isAlertEnabled";
	public static final String	MF_IS_TEST_MODE						= "isTestMode";
	public static final String	MF_API_TOKEN						= "apiToken";
	
	// DB Constants
	public static final String	DB_ID								= "id";
	public static final String	DB_IDENTITY_ID						= "identity_id";
	public static final String	DB_EMAIL_ID							= "email_id";
	public static final String	DB_TYPE								= "type";
	public static final String	DB_RESULT							= "result";
	public static final String	DB_STATUS							= "status";
	public static final String	DB_MESSAGE							= "message";
	public static final String	DB_CREATED							= "created";
	public static final String	DB_APP_LIST							= "app_list";
	public static final String	DB_VAP_LIST							= "vap_list";
	public static final String	DB_LAST_UPDATED						= "last_updated";
	
	// Status Constants
	public static final String	STATUS_SUCCESS						= "Success";
	public static final String	STATUS_FAILED						= "Failed";
	public static final String	STATUS_ERROR						= "Error";
	public static final String	STATUS_PARTIAL						= "Partial";
	
	// Message Constants
	public static final String	MESSAGE_ALERT_SUCCESS				= "Alerts have been created successfully for user : ";
	public static final String	MESSAGE_ALERT_FAILED				= "Alerts were not created. Check logs for more details.";
	public static final String	MESSAGE_ALERT_ERROR					= "Error creating alerts for the user : ";
	
	public static final String	MESSAGE_VAP_LIST_EMPTY				= "VAP list is empty. Nothing to update.";
	public static final String	MESSAGE_VAP_LIST_SUCCESS_COMPLETE	= "All the VAP entries were successfully processed.";
	public static final String	MESSAGE_VAP_LIST_SUCCESS_PARTIAL	= "Some of the VAP enteries were not processed. Check logs for more details.";
	public static final String	MESSAGE_VAP_LIST_FAILED				= "VAP list processing failed. Check logs for more details.";
	
	public static final String	MESSAGE_PW_SUCCESS					= "Success";
	public static final String	MESSAGE_PW_FAILED					= "Failure";
	public static final String	MESSAGE_PW_ERROR					= "Error";
	public static final String	MESSAGE_PW_PARTIAL					= "Partial success - check logs for details.";
	
	public static final String	MESSAGE_ALERT_LIST_EMPTY			= "Alert list empty - nothing to update.";
	public static final String	MESSAGE_ALERT_LIST_SUCCESS_COMPLETE	= "Alert processing complete - check logs for details.";
	public static final String	MESSAGE_ALERT_LIST_FAILED			= "Alert processing failed - check logs for details.";
	public static final String	MESSAGE_ALERT_LIST_SUCCESS_PARTIAL	= "Alert processing partial - check logs for details.";
	
	public static final String	MESSAGE_PW_UPDATE_NOT_ENABLED		= "Password update not enabled.";
	public static final String	MESSAGE_PW_UPDATE_REQUEST			= "Password update request by Proofpoint";
	
	// Settings Constants
	public static final String	APP_LIST							= "appList";
	public static final String	VAP_LIST							= "vapList";
	public static final String	LAST_UPDATED						= "lastUpdated";
	public static final String	ALL_SETTINGS						= "allSettings";
	
	// Filter (Query Options) Constants
	public static final String	FILTER_FEATURES_STRING				= "featuresString";
	public static final String	FILTER_PW							= "PASSWORD";
	public static final String	FILTER_ENABLE						= "ENABLE";
	public static final String	FILTER_PROVISIONING					= "PROVISIONING";
	public static final String	FILTER_EMAIL						= "email";
	
	// Alert Attribute Constants
	public static final String	ALERT_ATTRIBUTE_TARGET_TYPE			= "Identity";
	public static final String	ALERT_PREFIX						= "Proofpoint Alert";
	public static final String	ALERT_NAME_PREFIX					= "Proofpoint Alert ";
	public static final String	SPACE_CHAR							= " ";
	
	// IIQ Constants
	public static final String	IIQ_IDENTITY_NAME					= "identityName";
	public static final String	IIQ_IDENTITY_DISPLAY_NAME			= "identityDisplayName";
	public static final String	IIQ_PLAN							= "plan";
	public static final String	IIQ_LAUNCHER						= "launcher";
	public static final String	IIQ_SPADMIN							= "spadmin";
	public static final String	IIQ_REQUESTOR						= "requestor";
	public static final String	IIQ_FLOW							= "flow";
	public static final String	IIQ_PWS_REQUEST						= "PasswordsRequest";
	public static final String	IIQ_TARGET_NAME						= "targetName";
	public static final String	IIQ_TARGET_CLASS					= "targetClass";
	public static final String	IIQ_IDENTITY						= "identity";
	public static final String	IIQ_SESSION_OWNER					= "sessionOwner";
	public static final String	IIQ_ATTRIBUTE_PW					= "password";
	
	// Util Constants
	public static final String	VAP_LIST_WINDOW_14					= "14";
	public static final String	VAP_LIST_WINDOW_30					= "30";
	public static final String	VAP_LIST_WINDOW_90					= "90";
	public static final String	ATTR_EMAIL							= "email";
	public static final String	ATTR_ID								= "id";
	public static final String	ATTR_NAME							= "name";
	public static final String	ATTR_DISPLAYNAME					= "displayName";
	public static final String	CLICKS_BLOCKED_URL					= "/v2/siem/clicks/blocked";
	public static final String	CLICKS_PERMITTED_URL				= "/v2/siem/clicks/permitted";
	public static final String	CLICKS_URL_PRARMETERS				= "?format=json&interval=PT60M/";
	public static final String	VAP_URL								= "/v2/people/vap";
	public static final String	VAP_LIST_URL_PARAMETERS				= "?window=";
	public static final String	VAP_LIST_TASK_RESULT_COUNT			= "vapList";
	public static final String	DEFAULT_VAP_LIST_AGE				= "14";
	public static final String	PROOFPOINT_DATE_FORMAT				= "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String	ALERT								= "Alert";
	public static final String	ALERT_CREATION_DISABLED				= "Alert creation disabled - check integration settings";
	public static final String	TYPE_PW								= "Password Update";
	public static final String	OOTB_LCM_MANAGE_PW					= "LCM Manage Passwords";
}
