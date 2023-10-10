package com.sailpoint.plugin.proofpoint.db;

import com.sailpoint.plugin.proofpoint.object.ProofpointPluginConstants;

/**
 * @author prashant.kagwad
 *
 *         Query statement class for all database related operations.
 */
public class DBQueryStatements {
	
	/**
	 * Get settings from iplus_proofpoint_settings table.
	 */
	public static final String	GET_SETTINGS		= "SELECT * FROM iplus_proofpoint_settings";
	
	/**
	 * Update app list in plus_proofpoint_settings table.
	 */
	public static final String	UPDATE_APP_SETTINGS	= "UPDATE iplus_proofpoint_settings SET "
			+ ProofpointPluginConstants.DB_APP_LIST + " = ?";
	
	/**
	 * Update vap list in plus_proofpoint_settings table.
	 */
	public static final String	UPDATE_VAP_SETTINGS	= "UPDATE iplus_proofpoint_settings SET "
			+ ProofpointPluginConstants.DB_VAP_LIST + " = ? , " + ProofpointPluginConstants.DB_LAST_UPDATED + " = ?";
	
	/**
	 * Update all settings in plus_proofpoint_settings table.
	 */
	public static final String	UPDATE_SETTINGS		= "UPDATE iplus_proofpoint_settings SET "
			+ ProofpointPluginConstants.DB_APP_LIST + " = ? , " + ProofpointPluginConstants.DB_VAP_LIST + " = ? , "
			+ ProofpointPluginConstants.DB_LAST_UPDATED + " = ?";
	
	/**
	 * Get all data records from iplus_proofpoint_data table.
	 */
	public static final String	ALL_DATA			= "SELECT * FROM iplus_proofpoint_data";
	
	/**
	 * Get a data record from iplus_proofpoint_data table.
	 */
	public static final String	GET_DATA			= "SELECT * FROM iplus_proofpoint_data WHERE "
			+ ProofpointPluginConstants.DB_ID + " = ?";
	
	/**
	 * Add a new record into iplus_proofpoint_data table.
	 */
	public static final String	INSERT_DATA			= "INSERT INTO iplus_proofpoint_data ("
			+ ProofpointPluginConstants.DB_ID + ", " + ProofpointPluginConstants.DB_IDENTITY_ID + ", "
			+ ProofpointPluginConstants.DB_EMAIL_ID + ", " + ProofpointPluginConstants.DB_TYPE + ", "
			+ ProofpointPluginConstants.DB_RESULT + ", " + ProofpointPluginConstants.DB_STATUS + ", "
			+ ProofpointPluginConstants.DB_MESSAGE + ", " + ProofpointPluginConstants.DB_CREATED + ", "
			+ ProofpointPluginConstants.DB_LAST_UPDATED + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Update data in iplus_proofpoint_data table.
	 */
	public static final String	UPDATE_DATA			= "UPDATE iplus_proofpoint_data SET "
			+ ProofpointPluginConstants.DB_IDENTITY_ID + " = ? , " + ProofpointPluginConstants.DB_EMAIL_ID + " = ? , "
			+ ProofpointPluginConstants.DB_TYPE + " = ? , " + ProofpointPluginConstants.DB_RESULT + " = ? , "
			+ ProofpointPluginConstants.DB_STATUS + " = ? , " + ProofpointPluginConstants.DB_MESSAGE + " = ? , "
			+ ProofpointPluginConstants.DB_LAST_UPDATED + " = ? WHERE " + ProofpointPluginConstants.DB_ID + " = ?";
	
	/**
	 * Get all data records from iplus_proofpoint_vap_data table.
	 */
	public static final String	ALL_VAP_DATA		= "SELECT * FROM iplus_proofpoint_vap_data";
	
	/**
	 * Get a data record from iplus_proofpoint_vap_data table.
	 */
	public static final String	GET_VAP_DATA		= "SELECT * FROM iplus_proofpoint_vap_data  WHERE "
			+ ProofpointPluginConstants.DB_ID + " = ?";
	
	/**
	 * Add a new record into iplus_proofpoint_vap_data table.
	 */
	public static final String	INSERT_VAP_DATA		= "INSERT INTO iplus_proofpoint_vap_data ("
			+ ProofpointPluginConstants.DB_ID + ", " + ProofpointPluginConstants.DB_IDENTITY_ID + ", "
			+ ProofpointPluginConstants.DB_EMAIL_ID + ", " + ProofpointPluginConstants.DB_RESULT + ", "
			+ ProofpointPluginConstants.DB_STATUS + ", " + ProofpointPluginConstants.DB_MESSAGE + ", "
			+ ProofpointPluginConstants.DB_CREATED + ", " + ProofpointPluginConstants.DB_LAST_UPDATED
			+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Update data in iplus_proofpoint_vap_data table.
	 */
	public static final String	UPDATE_VAP_DATA		= "UPDATE iplus_proofpoint_vap_data SET "
			+ ProofpointPluginConstants.DB_IDENTITY_ID + " = ? , " + ProofpointPluginConstants.DB_EMAIL_ID + " = ? , "
			+ ProofpointPluginConstants.DB_RESULT + " = ? , " + ProofpointPluginConstants.DB_STATUS + " = ? , "
			+ ProofpointPluginConstants.DB_MESSAGE + " = ? , " + ProofpointPluginConstants.DB_LAST_UPDATED
			+ " = ? WHERE " + ProofpointPluginConstants.DB_ID + " = ?";
	
}
