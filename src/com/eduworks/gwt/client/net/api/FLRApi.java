/*
Copyright 2012-2013 Eduworks Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.eduworks.gwt.client.net.api;

import java.util.ArrayList;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.callback.FLRCallback;
import com.eduworks.gwt.client.net.packet.AlfrescoPacket;
import com.eduworks.gwt.client.net.packet.FLRPacket;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsDate;


public class FLRApi {
	public static final String FLR_RUSSEL_MIME_TYPE = "russel/flr";
	public static final String FLR_SUCCESS = "success";
	public static final String FLR_FAILURE = "failure";
	public static final String FLR_ACTIVITY_RATINGS = "ratings";
	public static final String FLR_ACTIVITY_COMMENTS = "comments";
	public static final String FLR_ACTIVITY_ISD = "isd";
	
	public static String FLR_REPOSITORY_SETTING = "FLR-repository";
	public static String FLR_IMPORT_SETTING = "FLR-import";
	public static String FLR_PUBLISH_SETTING = "FLR-publish";
	public static String FLR_ACTIVITY_SETTING = "FLR-activity";
	
	public static final String FLR_NOT_IN_USE = "None";
    public static final String FLR_SAND_BOX = "FLR-Sandbox";
    public static final String FLR_CUSTOM_URL = "FLR-Custom";
    public static String FLR_REPOSITORY_MODE = FLR_SAND_BOX;

	public static final String FLR_IMPORT_DISABLED = "FLR-NoImport";
	public static final String FLR_IMPORT_ENABLED = "FLR-Import";
	public static String FLR_IMPORT_MODE = FLR_IMPORT_ENABLED;
	
	public static final String FLR_PUBLISH_ACTIONS_NONE = "FLR-NoPublish";
	public static final String FLR_PUBLISH_ACTIONS_GENERAL = "FLR-GeneralPublish";
	public static final String FLR_PUBLISH_ACTIONS_ISD = "FLR-IsdPublish";
	public static final String FLR_PUBLISH_ACTIONS_ALL = "FLR-AllPublish";
	public static String FLR_PUBLISH_MODE = FLR_PUBLISH_ACTIONS_ALL;
	
	public static final String FLR_ACTIVITY_ACTIONS_NONE = "FLR-NoActivity";
	public static final String FLR_ACTIVITY_ACTIONS_FEEDBACK = "FLR-FeedbackActivity";
	public static final String FLR_ACTIVITY_ACTIONS_ISD = "FLR-IsdActivity";
	public static final String FLR_ACTIVITY_ACTIONS_ALL = "FLR-AllActivity";
	public static String FLR_ACTIVITY_MODE = FLR_ACTIVITY_ACTIONS_ALL;
	
	public static String currentDirectoryId = "";
	public static String ticket;

	// FLR API private methods
	private final native static String getISOdate0(JsDate date) /*-{
		return date.toISOString();
	}-*/;
	
	private static String buildNsdlDcRecord0(AlfrescoPacket ap) {
		String nsdl = null;
		String valueTest = null;
		
		nsdl = "<nsdl_dc:nsdl_dc xmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'  " +
		       "                 xmlns:dc=\'http://purl.org/dc/elements/1.1/\'  "+
		       "                 xmlns:dct=\'http://purl.org/dc/terms/\'       "+
		       "                 xmlns:ieee=\'http://www.ieee.org/xsd/LOMv1p0\' "+
		       "                 xmlns:nsdl_dc=\'http://ns.nsdl.org/nsdl_dc_v1.02/\'  schemaVersion=\'1.02.020\'  "+
		       "                 xsi:schemaLocation=\'http://ns.nsdl.org/nsdl_dc_v1.02/ http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc_v1.02.xsd\'> ";
		nsdl += "       <dc:identifier xsi:type=\'dct:URI\'>"+ CommunicationHub.siteURL+"?id="+ ap.getNodeId() + "</dc:identifier>";
		if ((valueTest = ap.getTitle()) != "") {
			nsdl += "       <dc:title>" + valueTest + "</dc:title>";
		}
		if ((valueTest = ap.getDescription()) != "") {
			nsdl += "       <dc:description>" + valueTest + "</dc:description>";
		}
		if ((valueTest = ap.getPublisher()) != "") {
			nsdl += "       <dc:creator>" + valueTest + "</dc:creator>";
		}
		if ((valueTest = ap.getRusselValue("russel:language")) != "") {		
			nsdl += "       <dc:language>" + valueTest + "</dc:language> ";
		}
		if ((valueTest = ap.getRusselValue("russel:agerange")) != "") {		
			nsdl += "       <dct:educationLevel xsi:type=\'nsdl_dc:NSDLEdLevel\'>" + valueTest + "</dct:educationLevel>";
		}
		if ((valueTest = ap.getMimeType()) != "") {		
			nsdl += "       <dc:format>" + valueTest + "</dc:format>";
		}
		if ((valueTest = ap.getCreateDate()) != "") {		
			nsdl += "       <dc:date>" + valueTest + "</dc:date>";			
		}
		nsdl += "</nsdl_dc:nsdl_dc>";

		return nsdl;
	}

	private static FLRPacket buildActivityProperties0(AlfrescoPacket ap, AlfrescoPacket feedback, String type) {
		JsDate date = JsDate.create();
		String dateStr = getISOdate0(date);
		FLRPacket fp = FLRPacket.makePacket();
		
		// Describe the actor
		FLRPacket actor = FLRPacket.makePacket();
		actor.addKeyValue("objectType", "community");
		actor.addKeyArray("description", "ADL RUSSEL user community");		
		fp.addKeyValue("actor", actor);

		// Describe the verb (activity)
		if (type == FLR_ACTIVITY_RATINGS) {
			FLRPacket payload = FLRPacket.makePacket();
			payload.addKeyValue("avg", feedback.getAverageRating());
			payload.addKeyValue("scale min", "1");
			payload.addKeyValue("scale max", "5");
			payload.addKeyValue("sample size", feedback.getRatingCount());
			FLRPacket value = FLRPacket.makePacket();
			value.addKeyValue("measureType", "star average");
			value.addKeyValue("value", payload);
			FLRPacket measure = FLRPacket.makePacket();
			measure.addKeyValue("action", "rated");
			measure.addKeyValue("measure", value);
			measure.addKeyValue("context", "repository");		
			measure.addKeyValue("date", ap.getCreateDate() + "/" + dateStr.substring(0, dateStr.indexOf('T')));	
			fp.addKeyValue("verb", measure);
			fp.addKeyValue("content", feedback.getRatingCount() + " member(s) of the ADL RUSSEL user community gave '"+ap.getTitle()+"' a rating of "+ feedback.getAverageRating() + " out of 5 stars");
		}
		else if (type == FLR_ACTIVITY_COMMENTS) {
			FLRPacket payload = FLRPacket.makePacket();
			payload.addKeyValue("measureType", "count");
			payload.addKeyValue("value", feedback.getCommentCount());
			FLRPacket measure = FLRPacket.makePacket();
			measure.addKeyValue("action", "commented");
			measure.addKeyValue("measure", payload);		
			measure.addKeyValue("date", ap.getCreateDate() + "/" + dateStr.substring(0, dateStr.indexOf('T')));	
			measure.addKeyValue("context", "ADL RUSSEL repository");
			fp.addKeyValue("verb", measure);
			fp.addKeyValue("content", feedback.getCommentCount() + " member(s) of the ADL RUSSEL user community commented on '"+ap.getTitle()+"'.");
		}
		else if (type == FLR_ACTIVITY_ISD) {
			FLRPacket payload = FLRPacket.makePacket();
			payload.addKeyValue("measureType", "count");
			payload.addKeyValue("value", feedback.getValueString("count"));
			FLRPacket measure = FLRPacket.makePacket();
			measure.addKeyValue("action", "aligned");
			measure.addKeyValue("measure", payload);		
			measure.addKeyValue("date", ap.getCreateDate() + "/" + dateStr.substring(0, dateStr.indexOf('T')));	
			measure.addKeyValue("context", feedback.getValueString("template")+" instructional strategy");
			fp.addKeyValue("verb", measure);
			FLRPacket object = FLRPacket.makePacket();
			object.addKeyValue("objectType", "Instructional Strategy");
			object.addKeyValue("description", feedback.getValueString("strategy"));		
			fp.addKeyArray("related", object);
			fp.addKeyValue("content", "'"+ap.getTitle() + "' has been aligned with the '"+feedback.getValueString("strategy")+"' part of the '"+ feedback.getValueString("template") + "' template "+feedback.getValueString("count")+" time(s).");
		}
		
		// Describe the object
		FLRPacket object = FLRPacket.makePacket();
		object.addKeyValue("objectType", "resource");
		if (ap.getRusselValue("russel:FLRid") != null) {
			object.addKeyValue("id", ap.getRusselValue("russel:FLRtag"));	
		} else {
			object.addKeyValue("id", CommunicationHub.siteURL+"?id="+ap.getNodeId());	
		}
		fp.addKeyValue("object", object);

		if (ap.getRusselValue("russel:FLRid") != null) {
			object = FLRPacket.makePacket();
			object.addKeyValue("objectType", "comment");
			object.addKeyValue("id", CommunicationHub.siteURL+"?id="+ap.getNodeId());		
			fp.addKeyArray("related", object);
		}

		return fp;
	}
	
	private static FLRPacket buildActivityRecord0(AlfrescoPacket ap, AlfrescoPacket feedback, String type) { // ISD or RATING
		FLRPacket activity = FLRPacket.makePacket();
		activity.addKeyValue("activity", buildActivityProperties0(ap, feedback, type));
		return activity;
	}
	
	// FLRAPI public methods
	public static String buildFLRResourceDataDescription(AlfrescoPacket ap) {
		JsDate date = JsDate.create();

		FLRPacket fpRdd = FLRPacket.makePacket();
		fpRdd.addKeyValue("doc_type", "resource_data");
		fpRdd.addKeyValue("resource_data_type", "metadata");		
		fpRdd.addKeyValue("node_timestamp", getISOdate0(date));	
		fpRdd.addKeyValue("TOS", FLRPacket.makePacketTOS());	
		fpRdd.addKeyValue("payload_placement", "inline");	
		fpRdd.addKeyArray("payload_schema", "NSDL DC 1.02.020");	
		fpRdd.addKeyValue("payload_schema_locator", "http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc_v1.02.xsd");
		fpRdd.addKeyValue("active", true);	
		fpRdd.addKeyValue("doc_version", "0.23.0");	
		fpRdd.addKeyValue("resource_locator", CommunicationHub.siteURL+"?id="+ap.getNodeId());	
		fpRdd.addKeyValue("publishing_node", "RUSSEL");
		fpRdd.addKeyValue("identity", FLRPacket.makePacketIdentity(ap.getPublisher()));
		fpRdd.addKeyValue("resource_data", buildNsdlDcRecord0(ap));  
		
		return fpRdd.toJSONString();
	}
	
	public static String buildFLRResourceDataActivity(AlfrescoPacket ap, AlfrescoPacket feedback, String type) { 
		JsDate date = JsDate.create();

		FLRPacket fpRdd = FLRPacket.makePacket();
		fpRdd.addKeyValue("doc_type", "resource_data");
		fpRdd.addKeyValue("resource_data_type", "paradata");		// assertion?
		fpRdd.addKeyValue("active", true);	
		fpRdd.addKeyValue("node_timestamp", getISOdate0(date));	
		fpRdd.addKeyValue("create_timestamp", getISOdate0(date));	
		fpRdd.addKeyValue("TOS", FLRPacket.makePacketTOS());	
		fpRdd.addKeyValue("payload_placement", "inline");	
		fpRdd.addKeyArray("payload_schema", "LR Paradata 1.0");	
		fpRdd.addKeyValue("doc_version", "0.23.0");	
		fpRdd.addKeyValue("resource_locator", CommunicationHub.siteURL+"?id="+ap.getNodeId());	
		fpRdd.addKeyValue("publishing_node", "RUSSEL");
		fpRdd.addKeyValue("identity", FLRPacket.makePacketIdentity(ap.getPublisher()));
		fpRdd.addKeyValue("resource_data", buildActivityRecord0(ap, feedback, type));  

		return fpRdd.toJSONString();
	}
	
	public static String buildFLRDocuments(ArrayList<String> docs) {
		FLRPacket fpDocs = FLRPacket.makePacket();
		String docAcc = "";
		for (int docIndex=0;docIndex<docs.size();docIndex++)
			docAcc += "," + docs.get(docIndex);
		if (docAcc!="")
			docAcc = docAcc.substring(1);
		fpDocs.addKeyArray("documents", CommunicationHub.parseJSON(docAcc));

		return fpDocs.toJSONString();
	}

	public static AlfrescoPacket parseFLRResponse(String op, FLRPacket response, AlfrescoPacket ap) {
		AlfrescoPacket status = AlfrescoPacket.makePacket();
		
		if (op.equals(FLR_PUBLISH_SETTING)) {
			if (response.getResponseStatus().equals("true")) {
				JsArray<FLRPacket> results = response.getResponseDocResults();
				for (int i=0; i<results.length(); i++) {  //NOTE: right now we are only publishing one node at a time
					FLRPacket doc = results.get(i);
					if (doc.getResponseStatus().equals("true")) {
						status.addKeyValue("status", FLR_SUCCESS);
						status.addKeyValue("flr_ID", doc.getResponseDocID());
						status.addKeyValue("russel_ID", ap.getNodeId());
					}
					else {
						status.addKeyValue("status", FLR_FAILURE);
						status.addKeyValue("error", doc.getResponseError());
					}
				}
			}
			else {
				status.addKeyValue("status", FLR_FAILURE);
				status.addKeyValue("error", response.getResponseError());
			}
		}
		else if (op.equals(FLR_ACTIVITY_SETTING)) {
			if (response.getResponseStatus().equals("true")) {
				status.addKeyValue("status", FLR_SUCCESS);
				JsArray<FLRPacket> results = response.getResponseDocResults();
				status.addKeyArray("list",results.toString());
			}
			else {
				status.addKeyValue("status", FLR_FAILURE);
				status.addKeyValue("error", response.getResponseError());
			}
		}

		return status;
	}
	
	public static void saveFLRsetting(String setting, String value) {
		// TODO: Need to save these settings to a file or Alfresco node -- they are currently only maintained within a session.
		if (setting.equalsIgnoreCase(FLR_REPOSITORY_SETTING))  {
		    FLR_REPOSITORY_MODE = value;
		}
		else if ((setting.equalsIgnoreCase(FLR_IMPORT_SETTING)) && 
			    (value.equalsIgnoreCase(FLR_IMPORT_ENABLED)||value.equalsIgnoreCase(FLR_IMPORT_DISABLED))) {
			FLR_IMPORT_MODE = value;
		}
		else if ((setting.equalsIgnoreCase(FLR_PUBLISH_SETTING)) && 
		    (value.equalsIgnoreCase(FLR_PUBLISH_ACTIONS_NONE)||value.equalsIgnoreCase(FLR_PUBLISH_ACTIONS_GENERAL)||value.equalsIgnoreCase(FLR_PUBLISH_ACTIONS_ISD)||value.equalsIgnoreCase(FLR_PUBLISH_ACTIONS_ALL))) {
			FLR_PUBLISH_MODE = value;
		}
		else if ((setting.equalsIgnoreCase(FLR_ACTIVITY_SETTING)) && 
			(value.equalsIgnoreCase(FLR_ACTIVITY_ACTIONS_NONE)||value.equalsIgnoreCase(FLR_ACTIVITY_ACTIONS_FEEDBACK)||value.equalsIgnoreCase(FLR_ACTIVITY_ACTIONS_ISD)||value.equalsIgnoreCase(FLR_ACTIVITY_ACTIONS_ALL))) {
			FLR_ACTIVITY_MODE = value;
		}		
	}

	public static String getFLRsetting(String setting) {
		String response = "Unknown FLR setting: "+setting; 
		
		if (setting.equalsIgnoreCase(FLR_REPOSITORY_SETTING))  {
		    response = FLR_REPOSITORY_MODE;
		}
		else if (setting.equalsIgnoreCase(FLR_IMPORT_SETTING)) {
			response = FLR_IMPORT_MODE;
		}
		else if (setting.equalsIgnoreCase(FLR_PUBLISH_SETTING)) {
			response = FLR_PUBLISH_MODE;
		}
		else if (setting.equalsIgnoreCase(FLR_ACTIVITY_SETTING)) {
			response = FLR_ACTIVITY_MODE;
		}
		return response;	
	}
	
	// TODO: This initializeFLRconfig function is not being called until we are properly setting the value. It will need to be called after successful login.
	public static void initializeFLRconfig() {
		// TODO: Retrieve the file or node where configuration is saved, then pull the values from there.
		String value = ""; // TODO: replace this with each relevant value
		
		saveFLRsetting(FLR_REPOSITORY_SETTING, value);
		saveFLRsetting(FLR_IMPORT_SETTING, value);
		saveFLRsetting(FLR_PUBLISH_SETTING, value);
		saveFLRsetting(FLR_ACTIVITY_SETTING, value);
	}
	
	public static void getFLRdata(FLRCallback<FLRPacket> callback) {
		if (FLR_IMPORT_MODE.equals(FLR_IMPORT_ENABLED)) {
			CommunicationHub.sendHTTP(CommunicationHub.GET,
									  AlfrescoURL.getAlfrescoFlrImportURL(),  
									  null,
									  false, 
									  callback);
		} 
		else {
			callback.onFailure(new Throwable("FLR import is disabled.  See Repository Settings to change the configuration."));
		}
	}
	
	public static void putFLRdata(String postData, FLRCallback<FLRPacket> callback) {
		if (!FLR_PUBLISH_MODE.equals(FLR_PUBLISH_ACTIONS_NONE)) {
			CommunicationHub.sendHTTP(CommunicationHub.POST,
									  AlfrescoURL.getAlfrescoFlrDispatchURL(), 
									  postData, 
									  false, 
									  callback);
		}
		else {
			callback.onFailure(new Throwable("FLR publish is disabled.  See Repository Settings to change the configuration."));
		}
	}
	
	public static void putFLRactivity(String activityString, FLRCallback<FLRPacket> callback) {
		if (!FLR_ACTIVITY_MODE.equals(FLR_ACTIVITY_ACTIONS_NONE)) {			
			CommunicationHub.sendHTTP(CommunicationHub.POST,
									  AlfrescoURL.getAlfrescoFlrDispatchURL(), 
									  activityString,
									  false, 
									  callback);
		}
		else {
			callback.onFailure(new Throwable("FLR activity stream publish is disabled.  See Repository Settings to change the configuration."));
		}
	}
	
	

}
