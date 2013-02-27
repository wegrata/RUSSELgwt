package com.eduworks.gwt.client.net.api;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class AlfrescoURL {
	public static final String ALFRESCO_STORE_TYPE			= "workspace";
	public static final String ALFRESCO_STORE_ID			= "SpacesStore";
	
	/* Alfresco API calls */
	public final static String getAlfrescoLoginURL(){return CommunicationHub.BASE_URL + "service/api/login?nocache=" + CommunicationHub.randomString();}
		
	public final static String getAlfrescoUploadURL() {
		return CommunicationHub.BASE_URL + "service/api/upload?alf_ticket=" + AlfrescoApi.ticket + "&format=text&nocache=" + CommunicationHub.randomString();
	}

	public final static String getAlfrescoShareSearch(String term, String tags, int maxRecords, String sort, String query) {
		return CommunicationHub.BASE_URL + "service/russeltools/solrsearch?terms=" + URL.encodeQueryString(term) + 
										   "&rowLimit=" + URL.encodeQueryString(Integer.toString(maxRecords)) +
										   "&page=0" +
										   "&sort=" + URL.encodeQueryString(sort) +
										   "&store_type=" + ALFRESCO_STORE_TYPE +
										   "&store_id=" + ALFRESCO_STORE_ID +
										   "&alf_ticket=" + AlfrescoApi.ticket +
										   "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String buildAlfrescoApiUrl(String serviceId, String path) {
		return CommunicationHub.BASE_URL + "service/api/"+serviceId+"/"+path+"?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}

	public final static String getAlfrescoCommentsURL(String id) { 
		return CommunicationHub.BASE_URL + "service/api/comment/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoTagsURL(String id) {
		return CommunicationHub.BASE_URL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/tags?format=json&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString(); 
	}
	
	public final static String getAlfrescoRatingURL(String id) {
		return CommunicationHub.BASE_URL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/ratings?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoNodeURL(String id) {
		return CommunicationHub.BASE_URL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoMetadataURL(String id) {
		return CommunicationHub.BASE_URL + "service/api/metadata/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoLogoutURL(String id) {
		return CommunicationHub.BASE_URL + "service/api/login/ticket/" + id + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoCreateNode(String createLocation) {
		return CommunicationHub.BASE_URL + "service/cmis/p/" + SafeHtmlUtils.htmlEscape(createLocation) + "/children?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoAddAspectURL() {
		return CommunicationHub.BASE_URL + "service/russeltools/addAspect/?ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoValidationURL() {
		return CommunicationHub.BASE_URL + "service/api/login/ticket/" + AlfrescoApi.ticket + "?nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getAlfrescoFolderIdURL(String path) {
		return CommunicationHub.BASE_URL + "service/russeltools/folderId/" + path + "?alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}

	public final static String getAlfrescoDirectoryListing(String id) {
		return CommunicationHub.BASE_URL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/children?types=Documents&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String buildAlfrescoThumbnailURL(String id) {
		return CommunicationHub.BASE_URL + "service/api/node/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + id + "/content/thumbnails/doclib?c=force&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
	public final static String getObjectStreamURL(String nodeId, String filename) {
		return CommunicationHub.BASE_URL + "service/api/node/content/" + ALFRESCO_STORE_TYPE + "/" + ALFRESCO_STORE_ID + "/" + nodeId + "/" + filename + "?a=true&c=force&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString(); 
	}
	
	public final static String getAlfrescoDispatchURL(String URL, String post) { 
		return CommunicationHub.BASE_URL + "service/russeltools/dispatchText?targetURL=" + URL + "&httpType=" + post + "&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
//		return CommunicationHub.BASE_URL + "service/russeltools/dispatchText?targetURL=" + URL + "&httpType=" + post;
	}
	
	public final static String getAlfrescoFlrImportURL(String URL) { 
		return CommunicationHub.BASE_URL + "service/russeltools/flrImport?targetURL=" + URL + "obtain&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
//		return CommunicationHub.BASE_URL + "service/russeltools/flrImport?targetURL=" + URL + "obtain";
	}
	
	public final static String getAlfrescoImportZipURL(String nodeId) {
		return CommunicationHub.BASE_URL + "service/russeltools/zipimport?store_type=" + ALFRESCO_STORE_TYPE + "&store_id=" + ALFRESCO_STORE_ID + "&node_id=" + nodeId + "&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}

	public final static String getAlfrescoExportZipURL(String filename) {
		return CommunicationHub.BASE_URL + "service/russeltools/zipexport?zipName=" + filename + "&alf_ticket=" + AlfrescoApi.ticket + "&nocache=" + CommunicationHub.randomString();
	}
	
}
