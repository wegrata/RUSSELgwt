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

import com.eduworks.gwt.client.net.CommunicationHub;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class AlfrescoURL {
	public static final String ALFRESCO_STORE_TYPE			= "workspace";
	public static final String ALFRESCO_STORE_ID			= "SpacesStore";
	
	/* Alfresco API calls */
	public final static String getAlfrescoLoginURL(){return CommunicationHub.baseURL + "service/api/login?nocache=" + CommunicationHub.randomString();}
		
	public final static String getAlfrescoUploadURL() {
		return CommunicationHub.baseURL + "service/api/upload?alf_ticket=" + AlfrescoApi.ticket + "&format=text&nocache=" + CommunicationHub.randomString();
	}

	public final static String getAlfrescoShareSearch(String term, String tags, int maxRecords, String sort, String query) {
		return CommunicationHub.baseURL + "service/russeltools/solrsearch?terms=" + URL.encodeQueryString(term) + 
										   "&rowLimit=" + URL.encodeQueryString(Integer.toString(maxRecords)) +
										   "&page=0" +
										   "&sort=" + URL.encodeQueryString(sort) +
										   "&store_type=" + ALFRESCO_STORE_TYPE +
										   "&store_id=" + ALFRESCO_STORE_ID +
										   "&alf_ticket=" + AlfrescoApi.ticket +
										   "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String buildAlfrescoApiUrl(String serviceId, String path) {
		return CommunicationHub.baseURL + "service/api/"+serviceId+"/"+path+"?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}

	public final static String getAlfrescoDeleteCommentURL(String id) { 
		return CommunicationHub.baseURL + "service/api/comment/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoCommentsURL(String id) {
		return CommunicationHub.baseURL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/comments?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoTagsURL(String id) {
		return CommunicationHub.baseURL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/tags?format=json&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString(); 
	}
	
	public final static String getAlfrescoRatingURL(String id) {
		return CommunicationHub.baseURL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/ratings?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoNodeURL(String id) {
		return CommunicationHub.baseURL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoMetadataURL(String id) {
		return CommunicationHub.baseURL + "service/api/metadata/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoLogoutURL(String id) {
		return CommunicationHub.baseURL + "service/api/login/ticket/" + id + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoCreateNode(String createLocation) {
		return CommunicationHub.baseURL + "service/cmis/p/" + SafeHtmlUtils.htmlEscape(createLocation) + "/children?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoAddAspectURL() {
		return CommunicationHub.baseURL + "service/russeltools/addAspect/?ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoValidationURL() {
		return CommunicationHub.baseURL + "service/api/login/ticket/" + AlfrescoApi.ticket + "?nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoFolderIdURL(String path) {
		return CommunicationHub.baseURL + "service/russeltools/folderId/" + path + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}

	public final static String getAlfrescoDirectoryListing(String id) {
		return CommunicationHub.baseURL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/children?types=Documents&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String buildAlfrescoThumbnailURL(String id) {
		return CommunicationHub.baseURL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/content/thumbnails/doclib?c=force&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getObjectStreamURL(String nodeId, String filename) {
		return CommunicationHub.baseURL + "service/api/node/content/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + nodeId + "/" + filename + "?a=true&c=force&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString(); 
	}
	
	public final static String getObjectStreamAndDelete(String nodeId, String filename) {
		return CommunicationHub.baseURL + "service/russeltools/iecompatibility?action=get&store_type=" + ALFRESCO_STORE_TYPE + "&store_id=" + ALFRESCO_STORE_ID + "&node_id=" + nodeId + "&zipName=" + filename + "&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getUpdateObjectStream(String nodeId) {
		return CommunicationHub.baseURL + "service/russeltools/iecompatibility?action=update&store_type=" + ALFRESCO_STORE_TYPE + "&store_id=" + ALFRESCO_STORE_ID + "&node_id=" + nodeId + "&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getUploadObjectStream() {
		return CommunicationHub.baseURL + "service/russeltools/iecompatibility?action=create&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoFlrDispatchURL() { 
		return CommunicationHub.baseURL + "service/russeltools/dispatchflr?nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoFlrImportURL() { 
		return CommunicationHub.baseURL + "service/russeltools/importflr?nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfresco3drDispatchSearchURL(String terms) { 
		return CommunicationHub.baseURL + "service/russeltools/dispatch3dr?action=search&terms="+terms+"&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfresco3drDispatchIdURL(String action, String id) { 
		return CommunicationHub.baseURL + "service/russeltools/dispatch3dr?action="+action+"&id="+id+"&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfresco3drDispatchReviewURL(String id) { 
		return CommunicationHub.baseURL + "service/russeltools/dispatch3dr?action=uploadReview&id="+id+"&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoImportZipURL(String nodeId) {
		return CommunicationHub.baseURL + "service/russeltools/zipimport?store_type=" + ALFRESCO_STORE_TYPE + "&store_id=" + ALFRESCO_STORE_ID + "&node_id=" + nodeId + "&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}

	public final static String getAlfrescoExportZipURL(String filename) {
		return CommunicationHub.baseURL + "service/russeltools/zipexport?zipName=" + filename + "&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
}
