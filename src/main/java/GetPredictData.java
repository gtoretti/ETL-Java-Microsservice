package main.java;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetPredictData {

	private static final Logger logger = LogManager.getRootLogger();
	
	public void main() {
		
		JSONParser parser = new JSONParser();

		List<List<Object>> AlertKeys = DRCData.getRelatedAlertKeys();
		
		for (List<Object> row : AlertKeys) {

			try {
				
				String NODE = (String)row.get(0);
				String ALERTGROUP = (String)row.get(1);
				String INSTANCEID = (String)row.get(2);
				String ALERTKEY = (String)row.get(3);
				
				long alertId = DRCData.getAlertID(NODE, ALERTGROUP, INSTANCEID, ALERTKEY);
				if (alertId == 0) {
					logger.info("DRCData.insertAlert: NODE: " + NODE + " | ALERTGROUP: " + ALERTGROUP + " | INSTANCEID: " + INSTANCEID + " | ALERTKEY: " + ALERTKEY);
					DRCData.insertAlert(NODE, ALERTGROUP, INSTANCEID, ALERTKEY);
					alertId = DRCData.getAlertID(NODE, ALERTGROUP, INSTANCEID, ALERTKEY);
				}
				logger.info("DRCData.GetWatsonData alertId : " + alertId);

				String WatsonResult = PredictCall.getJSON(ALERTKEY, INSTANCEID, ALERTGROUP, NODE);
				Object obj = parser.parse(WatsonResult);

				JSONObject jsonObject = (JSONObject) obj;

				@SuppressWarnings("unchecked")
				// Process Resolution Codes
				JSONArray resolution_codes = (JSONArray) jsonObject.get("resolution");
				if (resolution_codes != null) {
					logger.info("deleteResolutionCodes: " + alertId);
					DRCData.deleteResolutionCodes(alertId);
					Iterator it = resolution_codes.iterator();
					while (it.hasNext()) {
						JSONObject resolution_code = (JSONObject) it.next();
						String Code = (String) resolution_code.get("name");
						Object conf = resolution_code.get("confidence");
						float Confidence = conf != null ? Float.parseFloat((String) resolution_code.get("confidence")): 0;
						logger.info("insertResolution alertId: " + alertId +",Code: "+Code+",Confidence: "+Confidence);
						DRCData.insertResolution(alertId, Code, Confidence);
					}
				}

				// Process Root Causes
				JSONArray root_causes = (JSONArray) jsonObject.get("root_cause");
				JSONArray root_cause_codes = (JSONArray) jsonObject.get("root_cause_code");
				if (root_causes != null) {
					Iterator it = root_causes.iterator();
					Iterator it2 = root_cause_codes.iterator();

					logger.info("DRCData.deleteRootCauseCodes: ");
					DRCData.deleteRootCauseCodes(alertId);

						while (it.hasNext()) {
						JSONObject root_cause = (JSONObject) it.next();
						JSONObject root_cause_code = (JSONObject) it2.next();
						String Name = (String) root_cause.get("name");
						String Code = (String) root_cause_code.get("name");
						Object frequencyOb = root_cause.get("frequency");
						float frequency = frequencyOb != null ? Float.parseFloat((String) frequencyOb) : 0;
						logger.info("DRCData.insertRootCause  alertId: " + alertId +",Code: "+Code+", name:"+Name+ ", frequency: "+frequency);
						DRCData.insertRootCause(alertId, Code, Name, frequency);
					}
				}

				// Process Nodes
				JSONArray nodes = (JSONArray) jsonObject.get("node");
				if (nodes != null) {
					Iterator it = nodes.iterator();
					DRCData.deleteNodes(alertId);
					while (it.hasNext()) {
						JSONObject node = (JSONObject) it.next();
						String name = (String) node.get("name");
						Object conf = node.get("frequency");
						float frequency = conf != null ? Float.parseFloat((String) node.get("frequency")) : 0;
						logger.info("DRCData.insertNode alertId: " + alertId +",name: "+name+ ", frequency: "+frequency);
						DRCData.insertNode(alertId, name, frequency);
					}
				}

				// Process automatons
				JSONArray objAutomatons = (JSONArray) jsonObject.get("automaton");
				if (objAutomatons != null) {
					Iterator itoa = objAutomatons.iterator();
					DRCData.deleteAutomatons(alertId);
					while (itoa.hasNext()) {
						JSONObject auto = (JSONObject) itoa.next();
						String Name = (String) auto.get("name");
						Object conf = auto.get("confidence");
						float confidence = conf != null ? Float.parseFloat((String) auto.get("confidence")) : 0;
						logger.info("DRCData.insertAutomaton alertId: " + alertId +", Name: "+Name +", confidence: "+confidence);
						DRCData.insertAutomaton(alertId,Name,confidence);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			} finally {
			}
		}
	}
}
