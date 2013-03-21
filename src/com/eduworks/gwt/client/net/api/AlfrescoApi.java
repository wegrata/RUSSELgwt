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

import org.vectomatic.file.Blob;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.callback.AlfrescoCallback;
import com.eduworks.gwt.client.net.packet.AlfrescoPacket;
import com.eduworks.gwt.client.util.Base64;
import com.eduworks.gwt.client.util.BlobUtils;
import com.eduworks.gwt.client.util.Browser;
import com.google.gwt.user.client.Window;

public class AlfrescoApi {
	public static final String RUSSEL_RATING_SCHEME = "fiveStarRatingScheme";
	public static final String FOLDER_OBJECT = "cmis:folder";
	public static final String DOCUMENT_OBJECT = "cmis:document";
	public static String username = "";
	public static String basicAuthenicationHeader = null;
	public static String currentDirectoryId = "";
	public static String ticket = null;
	public static boolean userHome = true;
	
	// AlfrescoAPI private methods
	
	private static String joinAspects0(String[] aspects) {
		String aspectString = "";
		for (int aspectIndex=0;aspectIndex<aspects.length;aspectIndex++)
			aspectString += "," + aspects[aspectIndex];
		if (aspectString!="")
			aspectString = aspectString.substring(1);
		return aspectString;
	}

	private static String getCurrentDirectoryId0() {
		return currentDirectoryId.split("/")[currentDirectoryId.split("/").length-1];
	}
	
	// AlfrescoAPI public methods
	
	public static String getCurrentDirectoryPath() {
		if (userHome)
			return "User Homes/" + AlfrescoApi.username;
		else
			return "";
	}
	
	public static String[] getWebServiceCallParameters() {
		if (ticket == null) return new String[0];

		return new String[] {"alf_ticket", ticket};
	}

	public static void getChildrenOfCurrentDirectory(AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET,
				AlfrescoURL.getAlfrescoDirectoryListing(getCurrentDirectoryId0()), null, false, callback);
	}
	
	public static void getObjectRatings(final String docResId, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoRatingURL(docResId),
								  null, 
								  false,
								  callback);
	}

	public static void rateObject(final String docResId, final Integer rating, AlfrescoCallback<AlfrescoPacket> callback) {
		AlfrescoPacket ap = AlfrescoPacket.makePacket();
		ap.addKeyValue("rating", rating.toString());
		ap.addKeyValue("ratingScheme", RUSSEL_RATING_SCHEME);
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getAlfrescoRatingURL(docResId),
								  ap.toJSONString(),
								  false,
								  callback);
	}

	public static void login(String uname, String password, AlfrescoCallback<AlfrescoPacket> callback) {
		username = uname;
		basicAuthenicationHeader = Base64.encode(username + ":" + password);
		AlfrescoPacket ap = AlfrescoPacket.makePacket();
		ap.addKeyValue("username", uname);
		ap.addKeyValue("password", password);
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getAlfrescoLoginURL(), 
								  ap.toJSONString(), 
								  false,
								  callback);
	}

	public static void logout(AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.DELETE, 
								  AlfrescoURL.getAlfrescoLogoutURL(getWebServiceCallParameters()[1]), 
								  null, 
								  false,
								  callback);
	}
	
	public static void validateTicket(AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoValidationURL(),
								  null,
								  false,
								  callback);			
	}
	
	public static void updateContentStream(String nodeId, String contents, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getUpdateObjectStream(nodeId),
								  contents, 
								  false, 
								  callback);
	}
	
	public static void uploadContentStream(String contents, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getUploadObjectStream(), 
								  contents, 
								  false, 
								  callback);	
	}
	
	public static void getThumbnail(String id, final AlfrescoCallback<AlfrescoPacket> callback) {
		if (Browser.isIE()) {
			AlfrescoPacket ap = AlfrescoPacket.makePacket();
			ap.addKeyValue("imageURL", AlfrescoURL.buildAlfrescoThumbnailURL(id));
			callback.onSuccess(ap);
		} else {
			CommunicationHub.sendHTTP(CommunicationHub.GET,
								      AlfrescoURL.buildAlfrescoThumbnailURL(id),
									  null,
									  true,
									  new AlfrescoCallback<AlfrescoPacket>() {
										@Override
										public void onSuccess(AlfrescoPacket alfrescoPacket) {
											AlfrescoPacket ap = AlfrescoPacket.makePacket();
											ap.addKeyValue("imageURL", BlobUtils.getBlobURL(BlobUtils.buildBlob("image/png", alfrescoPacket.getContents())));
											callback.onSuccess(ap);
										}
										
										@Override
										public void onFailure(Throwable caught) {
											callback.onFailure(caught);
										}
									});
		}
	}
	
	public static void deleteDocument(String id, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.DELETE, 
								  AlfrescoURL.getAlfrescoNodeURL(id),
								  null, 
								  false,
								  callback);
	}

	public static void updateCurrentDirectory(final String path) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoFolderIdURL(path), 
								  null, 
								  false,
								  new AlfrescoCallback<AlfrescoPacket>() {
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Fooing updating current directory failed - " + caught.getMessage());
										}
							
										@Override
										public void onSuccess(AlfrescoPacket result) {
											if (path.equalsIgnoreCase("Company%20Home"))
												userHome = false;
											currentDirectoryId = result.getNodeId();
										}
									});
	}
	
	public static void updateCurrentDirectory(final String path, final AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoFolderIdURL(path), 
								  null, 
								  false,
								  new AlfrescoCallback<AlfrescoPacket>() {
										@Override
										public void onFailure(Throwable caught) {
											callback.onFailure(caught);
										}
							
										@Override
										public void onSuccess(AlfrescoPacket result) {
											if (path.equalsIgnoreCase("Company%20Home"))
												userHome = false;
											currentDirectoryId = result.getNodeId();
											callback.onSuccess(result);
										}
									});
	}
	
	public static void getMetadata(final String id, final AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoNodeURL(id), 
								  null,
								  false,
								  new AlfrescoCallback<AlfrescoPacket>() {
									@Override
									public void onSuccess(final AlfrescoPacket nodeAP) {
										callback.onSuccess(nodeAP);
									}
									
									public void onFailure(Throwable caught) {
										callback.onSuccess(null);
									}
								});
	}

	public static void getMetadataAndTags(final String id, final AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoNodeURL(id), 
								  null,
								  false,
								  new AlfrescoCallback<AlfrescoPacket>() {
									@Override
									public void onSuccess(final AlfrescoPacket nodeAP) {
										CommunicationHub.sendHTTP(CommunicationHub.GET,
																  AlfrescoURL.getAlfrescoTagsURL(id),
																  null,
																  false, 
																  new AlfrescoCallback<AlfrescoPacket>() {
																	@Override
																	public void onSuccess(AlfrescoPacket tagsAP) {
																		callback.onSuccess((AlfrescoPacket)nodeAP.mergePackets(tagsAP));
																	}
																	
																	public void onFailure(Throwable caught) {
																		callback.onSuccess(nodeAP);
																	}
																  });
									}
									
									public void onFailure(Throwable caught) {
										callback.onSuccess(null);
									}
								});
	}

	public static void search(AlfrescoPacket ap, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoShareSearch(ap.getValueString("terms"), 
																	 ap.getValueString("tags"), 
																	 100, 
																	 ap.getValueString("sort"),
																	 ap.getValueString("query")), 
								  null, 
								  false,
								  callback);	
	}
	
	public static void setObjectMetadata(String id, String postData, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.POST,
						    	  AlfrescoURL.getAlfrescoMetadataURL(id), 
								  postData, 
								  false, 
								  callback);
	}
	
	public static String downloadContentURL(String id, String docName) {
		return AlfrescoURL.getObjectStreamURL(id, docName);
	}
	
	public static void getObjectStream(String id, String docName, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET,
								  AlfrescoURL.getObjectStreamURL(id, docName),
								  null,
								  true,
								  callback);
	}
	
	public static void getObjectStreamBlob(String id, String docName, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTPBlob(CommunicationHub.GET,
								      AlfrescoURL.getObjectStreamURL(id, docName),
									  null,
									  true,
									  callback);
	}
	
	public static void getObjectString(String id, String docName, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET,
							 	  AlfrescoURL.getObjectStreamURL(id, docName),
								  null,
								  false,
								  callback);
	}
	
	public static void getObjectComments(String nodeId, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoCommentsURL(nodeId), 
								  null, 
								  false,
								  callback);
	}
	
	public static void addObjectComment(String nodeId, String title, String commentData, AlfrescoCallback<AlfrescoPacket> callback) {
		AlfrescoPacket ap = AlfrescoPacket.makePacket();
		ap.addKeyValue("title", title);
		ap.addKeyValue("content", commentData);
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getAlfrescoCommentsURL(nodeId), 
								  ap.toJSONString(), 
								  false,
								  callback);
	}
	
	public static void deleteObjectComment(String nodeId, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.DELETE, 
								  AlfrescoURL.getAlfrescoDeleteCommentURL(nodeId), 
				  				  null, 
				  				  false, 
				  				  callback);
	}
	
	public static void importZipPackage(String nodeId, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET,
								  AlfrescoURL.getAlfrescoImportZipURL(nodeId),
							      null,
							      false,
							      callback);
	}
	
	public static void exportZipPackage(String filename, AlfrescoPacket zipPack, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.POST,
								  AlfrescoURL.getAlfrescoExportZipURL(filename),
								  zipPack.toJSONString(),
								  false,
								  callback);
	}
	
	public static void addAspectToNode(String nodeId, String[] aspects, AlfrescoCallback<AlfrescoPacket> callback) {
		AlfrescoPacket ap = AlfrescoPacket.makePacket();
		ap.addKeyValue("nodeRef", "workspace://SpacesStore/" + nodeId);
		ap.addKeyValue("aspects", joinAspects0(aspects));
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getAlfrescoAddAspectURL(), 
								  ap.toJSONString(), 
								  false,
								  callback);	
	}
	
	public static void uploadFile(Blob payload, String name, String[] aspects, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendForm(AlfrescoURL.getAlfrescoUploadURL(), 
								  name, 
								  AlfrescoURL.ALFRESCO_STORE_TYPE + "://" + AlfrescoURL.ALFRESCO_STORE_ID + "/" + AlfrescoApi.currentDirectoryId, 
							  	  payload, 
							  	  joinAspects0(aspects),
							  	  callback);
	}
	
	public static void updateFile(Blob payload, String name, String nodeId, String[] aspects, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendFormUpdate(AlfrescoURL.getAlfrescoUploadURL(),
										name,
										AlfrescoURL.ALFRESCO_STORE_TYPE + "://" + AlfrescoURL.ALFRESCO_STORE_ID + "/" + nodeId,
										payload,
										joinAspects0(aspects),
										callback);
	}
	
	/**
	 * 
	 * @param filename
	 * @param contents for non-html text use a base64 encoding
	 * @param mimeType
	 * @param createLocation Root starts at Company Home
	 * @param objectType
	 * @param callback
	 */
	public static void createObjectNode(String filename, String contents, String mimeType, String createLocation, String objectType, AlfrescoCallback<AlfrescoPacket> callback) {
		String payloadXML = "";
		payloadXML = CommunicationHub.ATOM_XML_PREAMBLE + "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:cmisra=\"http://docs.oasis-open.org/ns/cmis/restatom/200908/\" xmlns:cmis=\"http://docs.oasis-open.org/ns/cmis/core/200908/\">";
		payloadXML += "<title>" + filename + "</title>";
		payloadXML += "<author><name>" + username + "</name></author>";
		payloadXML += "<summary></summary>";
		if (objectType==DOCUMENT_OBJECT)
			payloadXML += "<content type=\"" + mimeType + "\">" + contents + "</content>";
		payloadXML += "<cmisra:object><cmis:properties>";
		payloadXML += "<cmis:propertyId propertyDefinitionId=\"cmis:objectTypeId\">";
		if (objectType==DOCUMENT_OBJECT)
			payloadXML += "<cmis:value>cmis:document</cmis:value>";
		else if (objectType==FOLDER_OBJECT)
			payloadXML += "<cmis:value>cmis:folder</cmis:value>";
		payloadXML += "</cmis:propertyId>";
		payloadXML += "</cmis:properties></cmisra:object></entry>";
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getAlfrescoCreateNode(createLocation), 
								  payloadXML,
								  false,
								  callback);
	}
}
