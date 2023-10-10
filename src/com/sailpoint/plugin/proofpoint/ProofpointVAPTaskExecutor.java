package com.sailpoint.plugin.proofpoint;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sailpoint.plugin.proofpoint.db.DBHelper;
import com.sailpoint.plugin.proofpoint.object.ProofpointPluginConstants;
import com.sailpoint.plugin.proofpoint.object.SettingsDAO;
import com.sailpoint.plugin.proofpoint.object.VAP;

import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.plugin.PluginBaseHelper;
import sailpoint.task.BasePluginTaskExecutor;

/**
 * @author prashant.kagwad
 *
 *         Task executor class for getting VAP list from Proofpoint.
 */
public class ProofpointVAPTaskExecutor extends BasePluginTaskExecutor {
	
	private static Log log = LogFactory.getLog(ProofpointVAPTaskExecutor.class);
	
	@Override
	public String getPluginName() {
		
		return ProofpointPluginConstants.PLUGIN_NAME;
	}
	
	@Override
	public void execute(SailPointContext context, TaskSchedule taskSchedule, TaskResult taskResult,
			Attributes<String, Object> attributes) throws Exception {
		
		log.trace("Entering execute...");
		
		List<VAP> vapList = ProofpointHelper.getVAPListFromProofpoint(context);
		
		if (null != vapList && vapList.size() > 0) {
			
			// for (VAP vap : vapList) {
			
			// log.debug("The VAP is: " + vap.getDisplayName());
			// }
			
			log.debug("Retrived the VAP list from proofpoint. Saving it to settings...");
			
			String vapListAge = PluginBaseHelper.getSettingString(ProofpointPluginConstants.PLUGIN_NAME,
					ProofpointPluginConstants.MF_VAP_LIST_AGE);
			
			// Save the VAP user list to the DB. This is listed on the plugin settings UI.
			SettingsDAO settings = new SettingsDAO(null, vapList, vapListAge);
			DBHelper.updateSettings(ProofpointPluginConstants.VAP_LIST, settings);
			
			taskResult.setAttribute(ProofpointPluginConstants.VAP_LIST_TASK_RESULT_COUNT, vapList.size());
			
			log.debug("VAP list is updated!");
		}
		
		log.trace("Exiting execute...");
	}
	
	@Override
	public boolean terminate() {
		
		return false;
	}
}
