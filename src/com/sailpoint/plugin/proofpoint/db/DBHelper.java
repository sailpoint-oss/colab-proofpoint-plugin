package com.sailpoint.plugin.proofpoint.db;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sailpoint.iplus.DBUtils;
import com.sailpoint.plugin.proofpoint.object.App;
import com.sailpoint.plugin.proofpoint.object.DataDAO;
import com.sailpoint.plugin.proofpoint.object.ProofpointPluginConstants;
import com.sailpoint.plugin.proofpoint.object.SettingsDAO;
import com.sailpoint.plugin.proofpoint.object.VAP;
import com.sailpoint.plugin.proofpoint.object.VAPDataDAO;

import sailpoint.plugin.PluginBaseHelper;
import sailpoint.tools.GeneralException;

/**
 * @author prashant.kagwad
 *
 *         DB Helper class for all database related operations.
 */
public class DBHelper {
	
	private static Log	log		= LogFactory.getLog(DBHelper.class);
	private static Gson	gson	= new Gson();
	
	private DBHelper() {
		
	}
	
	private static void closeResultSet(ResultSet resultSet) {
		
		try {
			
			if (resultSet != null) resultSet.close();
		} catch (Exception e) {
			log.error("Error closing the resultset.");
		}
	}
	
	/**
	 * Function to fetch plugin settings from database.
	 * 
	 * @return SettingsDAO object
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static SettingsDAO getSettings(String setting) throws GeneralException, SQLException {
		
		log.trace("Entering getSettings...");
		
		SettingsDAO settings = new SettingsDAO();
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.GET_SETTINGS;
			log.debug("SQL : " + sql);
			
			preparedStatement = conn.prepareStatement(sql);
			log.debug("Prepared Statement : " + preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();
			
			List<App> appList = null;
			List<VAP> vapList = null;
			while (resultSet.next()) {
				
				Type appListType = new TypeToken<List<App>>() {}.getType();
				appList = gson.fromJson(resultSet.getString(ProofpointPluginConstants.DB_APP_LIST), appListType);
				
				Type vapListType = new TypeToken<List<VAP>>() {}.getType();
				vapList = gson.fromJson(resultSet.getString(ProofpointPluginConstants.DB_VAP_LIST), vapListType);
				
				settings.setLastUpdated(resultSet.getLong(ProofpointPluginConstants.DB_LAST_UPDATED));
			}
			
			if (setting.equalsIgnoreCase(ProofpointPluginConstants.APP_LIST)) {
				
				settings.setAppList(appList);
			} else if (setting.equalsIgnoreCase(ProofpointPluginConstants.VAP_LIST)) {
				
				settings.setVapList(vapList);
			} else if (setting.equalsIgnoreCase(ProofpointPluginConstants.ALL_SETTINGS)) {
				
				settings.setAppList(appList);
				settings.setVapList(vapList);
			}
			
		} catch (Exception e) {
			
			log.error("Error getting settings from database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while fetching settings from database, check system logs.");
		} finally {
			
			DBUtils.closeDBObjects(conn, preparedStatement);
			closeResultSet(resultSet);
		}
		
		log.trace("Exiting getSettings...");
		return settings;
	}
	
	/**
	 * Function to update the plugin settings to database.
	 * 
	 * @param setting
	 *            - APPList for app list, VAPList for app list or null for both.
	 * @param settingsDAO
	 *            SettingsDAO object
	 * @return updated SettingsDAO object.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static SettingsDAO updateSettings(String setting, SettingsDAO settingsDAO)
			throws GeneralException, SQLException {
		
		log.trace("Entering updateSettings...");
		
		SettingsDAO settings = new SettingsDAO();
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			Date now = new Date();
			long currentTimestamp = now.getTime();
			
			if (setting.equalsIgnoreCase(ProofpointPluginConstants.APP_LIST)) {
				
				sql = DBQueryStatements.UPDATE_APP_SETTINGS;
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, settingsDAO.getAppList().toString());
			} else if (setting.equalsIgnoreCase(ProofpointPluginConstants.VAP_LIST)) {
				
				sql = DBQueryStatements.UPDATE_VAP_SETTINGS;
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, settingsDAO.getVapList().toString());
				preparedStatement.setLong(2, currentTimestamp);
			} else if (setting.equalsIgnoreCase(ProofpointPluginConstants.ALL_SETTINGS)) {
				
				sql = DBQueryStatements.UPDATE_SETTINGS;
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, settingsDAO.getAppList().toString());
				preparedStatement.setString(2, settingsDAO.getVapList().toString());
				preparedStatement.setLong(3, currentTimestamp);
			}
			
			log.debug("SQL : " + sql);
			log.debug("Prepared Statement : " + preparedStatement.toString());
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			
			log.error("Error updating settings to database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while updating settings to database, check system logs.");
		} finally {
			
			settings = getSettings(ProofpointPluginConstants.ALL_SETTINGS);
			DBUtils.closeDBObjects(conn, preparedStatement);
		}
		log.trace("Exiting updateSettings...");
		return settings;
	}
	
	/**
	 * Function to get all data (requests) from the database.
	 * 
	 * @return
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static List<DataDAO> getDataList() throws GeneralException, SQLException {
		
		log.trace("Entering getDataList...");
		
		List<DataDAO> dataList = new ArrayList<>();
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.ALL_DATA;
			log.debug("SQL : " + sql);
			
			preparedStatement = conn.prepareStatement(sql);
			log.debug("Prepared Statement : " + preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				
				DataDAO data = new DataDAO();
				
				data.setId(resultSet.getString(ProofpointPluginConstants.DB_ID));
				data.setIdentityId(resultSet.getString(ProofpointPluginConstants.DB_IDENTITY_ID));
				data.setEmailId(resultSet.getString(ProofpointPluginConstants.DB_EMAIL_ID));
				data.setType(resultSet.getString(ProofpointPluginConstants.DB_TYPE));
				data.setResult(resultSet.getString(ProofpointPluginConstants.DB_RESULT));
				data.setStatus(resultSet.getString(ProofpointPluginConstants.DB_STATUS));
				data.setMessage(resultSet.getString(ProofpointPluginConstants.DB_MESSAGE));
				data.setCreated(resultSet.getLong(ProofpointPluginConstants.DB_CREATED));
				data.setLastUpdated(resultSet.getLong(ProofpointPluginConstants.DB_LAST_UPDATED));
				
				dataList.add(data);
			}
		} catch (Exception e) {
			
			log.error("Error getting all data from database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while fetching all data from database, check system logs.");
		} finally {
			
			DBUtils.closeDBObjects(conn, preparedStatement);
			closeResultSet(resultSet);
		}
		
		log.trace("Exiting getDataList...");
		return dataList;
	}
	
	/**
	 * Function to get a single data record.
	 * 
	 * @param id
	 *            UUID of the record.
	 * @return
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static DataDAO getData(String id) throws GeneralException, SQLException {
		
		log.trace("Entering getData...");
		
		DataDAO data = new DataDAO();
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.GET_DATA;
			log.debug("SQL : " + sql);
			
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, id);
			
			log.debug("Prepared Statement : " + preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				
				data.setId(resultSet.getString(ProofpointPluginConstants.DB_ID));
				data.setIdentityId(resultSet.getString(ProofpointPluginConstants.DB_IDENTITY_ID));
				data.setEmailId(resultSet.getString(ProofpointPluginConstants.DB_EMAIL_ID));
				data.setType(resultSet.getString(ProofpointPluginConstants.DB_TYPE));
				data.setResult(resultSet.getString(ProofpointPluginConstants.DB_RESULT));
				data.setStatus(resultSet.getString(ProofpointPluginConstants.DB_STATUS));
				data.setMessage(resultSet.getString(ProofpointPluginConstants.DB_MESSAGE));
				data.setCreated(resultSet.getLong(ProofpointPluginConstants.DB_CREATED));
				data.setLastUpdated(resultSet.getLong(ProofpointPluginConstants.DB_LAST_UPDATED));
				
			}
		} catch (Exception e) {
			
			log.error("Error getting data from database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while fetching data from database, check system logs.");
		} finally {
			
			DBUtils.closeDBObjects(conn, preparedStatement);
			closeResultSet(resultSet);
		}
		
		log.trace("Exiting getData...");
		return data;
	}
	
	/**
	 * Function to insert a single data record.
	 * 
	 * @param data
	 *            DataDAO object.
	 * @return DataDAO object.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static DataDAO insertData(DataDAO data) throws GeneralException, SQLException {
		
		log.trace("Entering insertData...");
		
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.INSERT_DATA;
			log.debug("SQL : " + sql);
			preparedStatement = conn.prepareStatement(sql);
			
			Date now = new Date();
			long currentTimestamp = now.getTime();
			
			preparedStatement.setString(1, data.getId());
			preparedStatement.setString(2, data.getIdentityId());
			preparedStatement.setString(3, data.getEmailId());
			preparedStatement.setString(4, data.getType());
			preparedStatement.setString(5, data.getResult());
			preparedStatement.setString(6, data.getStatus());
			preparedStatement.setString(7, data.getMessage());
			preparedStatement.setLong(8, currentTimestamp);
			preparedStatement.setLong(9, currentTimestamp);
			
			log.debug("Prepared Statement : " + preparedStatement.toString());
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			
			log.error("Error inserting data into database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while inserting data into database, check system logs.");
			
		} finally {
			
			data = getData(data.getId());
			DBUtils.closeDBObjects(conn, preparedStatement);
		}
		
		log.trace("Exiting insertData...");
		return data;
	}
	
	/**
	 * Function to insert a list of data records.
	 * 
	 * @param dataList
	 *            List<DataDAO>.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static void insertDataList(List<DataDAO> dataList) throws GeneralException, SQLException {
		
		log.trace("Entering insertDataList...");
		
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.INSERT_DATA;
			log.debug("SQL : " + sql);
			preparedStatement = conn.prepareStatement(sql);
			
			Date now = new Date();
			long currentTimestamp = now.getTime();
			
			for (DataDAO data : dataList) {
				
				preparedStatement.setString(1, data.getId());
				preparedStatement.setString(2, data.getIdentityId());
				preparedStatement.setString(3, data.getEmailId());
				preparedStatement.setString(4, data.getType());
				preparedStatement.setString(5, data.getResult());
				preparedStatement.setString(6, data.getStatus());
				preparedStatement.setString(7, data.getMessage());
				preparedStatement.setLong(8, currentTimestamp);
				preparedStatement.setLong(9, currentTimestamp);
				
				log.debug("Prepared Statement : " + preparedStatement.toString());
				preparedStatement.executeUpdate();
			}
			
		} catch (Exception e) {
			
			log.error("Error inserting data list into database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while inserting data list into database, check system logs.");
			
		} finally {
			
			DBUtils.closeDBObjects(conn, preparedStatement);
		}
		
		log.trace("Exiting insertDataList...");
	}
	
	/**
	 * Function to update a single data record.
	 * 
	 * @param data
	 *            DataDAO object.
	 * @return updated DataDAO object.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static DataDAO updateData(DataDAO data) throws GeneralException, SQLException {
		
		log.trace("Entering updateData...");
		
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.UPDATE_DATA;
			log.debug("SQL : " + sql);
			preparedStatement = conn.prepareStatement(sql);
			
			Date now = new Date();
			long currentTimestamp = now.getTime();
			
			preparedStatement.setString(1, data.getIdentityId());
			preparedStatement.setString(2, data.getEmailId());
			preparedStatement.setString(3, data.getType());
			preparedStatement.setString(4, data.getResult());
			preparedStatement.setString(5, data.getStatus());
			preparedStatement.setString(6, data.getMessage());
			preparedStatement.setLong(7, currentTimestamp);
			preparedStatement.setString(8, data.getId());
			
			log.debug("Prepared Statement : " + preparedStatement.toString());
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			
			log.error("Error updating data in the database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while updating data in the database, check system logs.");
			
		} finally {
			
			data = getData(data.getId());
			DBUtils.closeDBObjects(conn, preparedStatement);
		}
		
		log.trace("Exiting updateData...");
		return data;
	}
	
	/**
	 * Function to get all VAP data from database.
	 * 
	 * @return List of VAPDataDAO objects.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static List<VAPDataDAO> getVAPDataList() throws GeneralException, SQLException {
		
		log.trace("Entering getVAPDataList...");
		
		List<VAPDataDAO> vapDataList = new ArrayList<>();
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.ALL_DATA;
			log.debug("SQL : " + sql);
			
			preparedStatement = conn.prepareStatement(sql);
			log.debug("Prepared Statement : " + preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				
				VAPDataDAO vapData = new VAPDataDAO();
				
				vapData.setId(resultSet.getString(ProofpointPluginConstants.DB_ID));
				vapData.setIdentityName(resultSet.getString(ProofpointPluginConstants.DB_IDENTITY_ID));
				vapData.setEmailId(resultSet.getString(ProofpointPluginConstants.DB_EMAIL_ID));
				vapData.setResult(resultSet.getString(ProofpointPluginConstants.DB_RESULT));
				vapData.setStatus(resultSet.getString(ProofpointPluginConstants.DB_STATUS));
				vapData.setMessage(resultSet.getString(ProofpointPluginConstants.DB_MESSAGE));
				vapData.setCreated(resultSet.getLong(ProofpointPluginConstants.DB_CREATED));
				vapData.setLastUpdated(resultSet.getLong(ProofpointPluginConstants.DB_LAST_UPDATED));
				
				vapDataList.add(vapData);
			}
		} catch (Exception e) {
			
			log.error("Error getting all VAP data from database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while fetching all VAP data from database, check system logs.");
		} finally {
			
			DBUtils.closeDBObjects(conn, preparedStatement);
			closeResultSet(resultSet);
		}
		
		log.trace("Exiting getVAPDataList...");
		return vapDataList;
	}
	
	/**
	 * Function to get a single VAP data record.
	 * 
	 * @param id
	 *            UUID of the record.
	 * @return VAPDataDAO object.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static VAPDataDAO getVAPData(String id) throws GeneralException, SQLException {
		
		log.trace("Entering getVAPData...");
		
		VAPDataDAO vapData = new VAPDataDAO();
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.GET_DATA;
			log.debug("SQL : " + sql);
			
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, id);
			
			log.debug("Prepared Statement : " + preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				
				vapData.setId(resultSet.getString(ProofpointPluginConstants.DB_ID));
				vapData.setIdentityName(resultSet.getString(ProofpointPluginConstants.DB_IDENTITY_ID));
				vapData.setEmailId(resultSet.getString(ProofpointPluginConstants.DB_EMAIL_ID));
				vapData.setResult(resultSet.getString(ProofpointPluginConstants.DB_RESULT));
				vapData.setStatus(resultSet.getString(ProofpointPluginConstants.DB_STATUS));
				vapData.setMessage(resultSet.getString(ProofpointPluginConstants.DB_MESSAGE));
				vapData.setCreated(resultSet.getLong(ProofpointPluginConstants.DB_CREATED));
				vapData.setLastUpdated(resultSet.getLong(ProofpointPluginConstants.DB_LAST_UPDATED));
				
			}
		} catch (Exception e) {
			
			log.error("Error getting VAP data from database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while fetching VAP data from database, check system logs.");
		} finally {
			
			DBUtils.closeDBObjects(conn, preparedStatement);
			closeResultSet(resultSet);
		}
		
		log.trace("Exiting getVAPData...");
		return vapData;
	}
	
	/**
	 * Function to insert a single VAP data record.
	 * 
	 * @param vapData
	 *            VAPDataDAO object.
	 * @return VAPDataDAO object.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static VAPDataDAO insertVAPData(VAPDataDAO vapData) throws GeneralException, SQLException {
		
		log.trace("Entering insertVAPData...");
		
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.INSERT_DATA;
			log.debug("SQL : " + sql);
			preparedStatement = conn.prepareStatement(sql);
			
			Date now = new Date();
			long currentTimestamp = now.getTime();
			
			preparedStatement.setString(1, vapData.getId());
			preparedStatement.setString(2, vapData.getIdentityName());
			preparedStatement.setString(3, vapData.getEmailId());
			preparedStatement.setString(4, vapData.getResult());
			preparedStatement.setString(5, vapData.getStatus());
			preparedStatement.setString(6, vapData.getMessage());
			preparedStatement.setLong(7, currentTimestamp);
			preparedStatement.setLong(8, currentTimestamp);
			
			log.debug("Prepared Statement : " + preparedStatement.toString());
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			
			log.error("Error inserting VAP data into database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while inserting VAP data into database, check system logs.");
			
		} finally {
			
			vapData = getVAPData(vapData.getId());
			DBUtils.closeDBObjects(conn, preparedStatement);
		}
		
		log.trace("Exiting insertVAPData...");
		return vapData;
	}
	
	/**
	 * Function to insert a list of VAP data records.
	 * 
	 * @param vapData
	 *            VAPDataDAO object.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static void insertVAPDataList(List<VAPDataDAO> vapDataList) throws GeneralException, SQLException {
		
		log.trace("Entering insertVAPDataList...");
		
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.INSERT_VAP_DATA;
			log.debug("SQL : " + sql);
			preparedStatement = conn.prepareStatement(sql);
			
			Date now = new Date();
			long currentTimestamp = now.getTime();
			
			for (VAPDataDAO vapData : vapDataList) {
				
				preparedStatement.setString(1, vapData.getId());
				preparedStatement.setString(2, vapData.getIdentityName());
				preparedStatement.setString(3, vapData.getEmailId());
				preparedStatement.setString(4, vapData.getResult());
				preparedStatement.setString(5, vapData.getStatus());
				preparedStatement.setString(6, vapData.getMessage());
				preparedStatement.setLong(7, currentTimestamp);
				preparedStatement.setLong(8, currentTimestamp);
				
				log.debug("Prepared Statement : " + preparedStatement.toString());
				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			
			log.error("Error inserting VAP data list into database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while inserting VAP data list into database, check system logs.");
			
		} finally {
			
			DBUtils.closeDBObjects(conn, preparedStatement);
		}
		
		log.trace("Exiting insertVAPDataList...");
	}
	
	/**
	 * Function to get a single VAP data record.
	 * 
	 * @param vapData
	 *            VAPDataDAO object.
	 * @return updated VAPDataDAO object.
	 * @throws GeneralException
	 * @throws SQLException
	 */
	public static VAPDataDAO updateVAPData(VAPDataDAO vapData) throws GeneralException, SQLException {
		
		log.trace("Entering updateVAPData...");
		
		String sql = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			conn = PluginBaseHelper.getConnection();
			
			sql = DBQueryStatements.UPDATE_VAP_DATA;
			log.debug("SQL : " + sql);
			preparedStatement = conn.prepareStatement(sql);
			
			Date now = new Date();
			long currentTimestamp = now.getTime();
			
			preparedStatement.setString(1, vapData.getIdentityName());
			preparedStatement.setString(2, vapData.getEmailId());
			preparedStatement.setString(3, vapData.getResult());
			preparedStatement.setString(4, vapData.getStatus());
			preparedStatement.setString(5, vapData.getMessage());
			preparedStatement.setLong(6, currentTimestamp);
			preparedStatement.setString(7, vapData.getId());
			
			log.debug("Prepared Statement : " + preparedStatement.toString());
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			
			log.error("Error updating VAP data in the database : " + e.toString());
			throw new GeneralException(
					"An exception has occured while updating VAP data in the database, check system logs.");
			
		} finally {
			
			vapData = getVAPData(vapData.getId());
			DBUtils.closeDBObjects(conn, preparedStatement);
		}
		
		log.trace("Exiting updateVAPData...");
		return vapData;
	}
}
