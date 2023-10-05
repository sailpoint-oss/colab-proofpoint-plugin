package com.sailpoint.plugin.proofpoint;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sailpoint.plugin.proofpoint.db.DBHelper;
import com.sailpoint.plugin.proofpoint.object.Click;
import com.sailpoint.plugin.proofpoint.object.CreateAlertRequestDTO;
import com.sailpoint.plugin.proofpoint.object.CreateAlertRequestDTO.AlertRequest;
import com.sailpoint.plugin.proofpoint.object.DataDAO;
import com.sailpoint.plugin.proofpoint.object.ProofpointPluginConstants;
import com.sailpoint.plugin.proofpoint.utils.Utils;

import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.plugin.PluginBaseHelper;
import sailpoint.task.BasePluginTaskExecutor;

/**
 * @author prashant.kagwad
 *
 *         Task executor class for getting alerts from Proofpoint.
 */
public class ProofpointAlertTaskExecutor extends BasePluginTaskExecutor {
	
	private static Log log = LogFactory.getLog(ProofpointAlertTaskExecutor.class);
	
	@Override
	public String getPluginName() {
		
		return ProofpointPluginConstants.PLUGIN_NAME;
	}
	
	@Override
	public void execute(SailPointContext context, TaskSchedule taskSchedule, TaskResult taskResult,
			Attributes<String, Object> attributes) throws Exception {
		
		log.trace("Entering ProofpointAlertTaskExecutor execute...");
		
		// Get the boolean to check if we can create an alert.
		boolean isAlertEnabled = PluginBaseHelper.getSettingBool(ProofpointPluginConstants.PLUGIN_NAME,
				ProofpointPluginConstants.MF_IS_ALERT_ENABLED);
		
		CreateAlertRequestDTO createAlertRequestDTO = new CreateAlertRequestDTO();
		List<AlertRequest> alertRequestList = new ArrayList<CreateAlertRequestDTO.AlertRequest>();
		
		List<Click> clickList = ProofpointHelper.getClicks(context);
		int numberOfAlerts = 0;
		// Iterate the clicks.
		if (null != clickList && clickList.size() > 0) {
			
			numberOfAlerts = clickList.size();
			for (Click click : clickList) {
				
				// For each click create an alertRequest object.
				// log.debug("The click is: " + click.toString());
				AlertRequest alertRequest = createAlertRequestDTO.new AlertRequest(click.getRecipient(),
						click.getClickTime(), click.getClassification());
				alertRequestList.add(alertRequest);
			}
			
			if (isAlertEnabled) {
				
				if (alertRequestList.isEmpty()) {
					
					log.warn(ProofpointPluginConstants.MESSAGE_ALERT_LIST_EMPTY);
					return;
				}
				
				// Process each user to create dataDAO and alert objects.
				List<DataDAO> dataList = Utils.processAlerts(alertRequestList);
				if (!dataList.isEmpty()) {
					
					// Save the user data for auditing.
					DBHelper.insertDataList(dataList);
					
					// Check if all the user entries were found & processed.
					if (numberOfAlerts != dataList.size()) {
						
						log.debug(ProofpointPluginConstants.MESSAGE_ALERT_LIST_SUCCESS_PARTIAL);
						return;
					} else {
						
						log.debug(ProofpointPluginConstants.MESSAGE_ALERT_LIST_SUCCESS_COMPLETE);
						return;
					}
					
				} else {
					
					log.debug(ProofpointPluginConstants.MESSAGE_ALERT_LIST_EMPTY);
					return;
				}
			} else {
				
				// Alerts are disabled.
				log.debug(ProofpointPluginConstants.ALERT_CREATION_DISABLED);
				return;
			}
		} else {
			
			// No clicks received.
			log.debug("No clicks were recieved. Nothing to do...");
		}
		
		log.trace("Exiting ProofpointAlertTaskExecutor execute...");
	}
	
	@Override
	public boolean terminate() {
		
		return false;
	}
}
