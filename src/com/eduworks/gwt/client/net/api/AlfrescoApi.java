/*
Copyright (c) 2012 Eduworks Corporation
All rights reserved.
 
This Software (including source code, binary code and documentation) is provided by Eduworks Corporation to
the Government pursuant to contract number W31P4Q-12 -C- 0119 dated 21 March, 2012 issued by the U.S. Army 
Contracting Command Redstone. This Software is a preliminary version in development. It does not fully operate
as intended and has not been fully tested. This Software is provided to the U.S. Government for testing and
evaluation under the following terms and conditions:

	--Any redistribution of source code, binary code, or documentation must include this notice in its entirety, 
	 starting with the above copyright notice and ending with the disclaimer below.
	 
	--Eduworks Corporation grants the U.S. Government the right to use, modify, reproduce, release, perform,
	 display, and disclose the source code, binary code, and documentation within the Government for the purpose
	 of evaluating and testing this Software.
	 
	--No other rights are granted and no other distribution or use is permitted, including without limitation 
	 any use undertaken for profit, without the express written permission of Eduworks Corporation.
	 
	--All modifications to source code must be reported to Eduworks Corporation. Evaluators and testers shall
	 additionally make best efforts to report test results, evaluation results and bugs to Eduworks Corporation
	 using in-system feedback mechanism or email to russel@eduworks.com.
	 
THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
THE COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN 
IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
*/

package com.eduworks.gwt.client.net.api;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.callback.AlfrescoCallback;
import com.eduworks.gwt.client.net.packet.AlfrescoPacket;
import com.eduworks.gwt.client.util.Base64;
import com.eduworks.gwt.client.util.Browser;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;

public class AlfrescoApi {
	public static final String RUSSEL_RATING_SCHEME = "fiveStarRatingScheme";
	public static final String FOLDER_OBJECT = "cmis:folder";
	public static final String DOCUMENT_OBJECT = "cmis:document";
	public static String username = "";
	public static String basicAuthenicationHeader = null;
	public static String currentDirectoryId = "";
	public static String ticket = null;
	
	public static String[] getWebServiceCallParameters() {
		if (ticket == null) return new String[0];

		return new String[] {"alf_ticket", ticket};
	}

	public static void getChildrenOfCurrentDirectory(AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET,
				AlfrescoURL.getAlfrescoDirectoryListing(getCurrentDirectoryId()), null, false, callback);
	}

	private static String getCurrentDirectoryId() {
		return currentDirectoryId.split("/")[currentDirectoryId.split("/").length-1];
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
		ap.addKeyValue("ratingScheme", "\"" + RUSSEL_RATING_SCHEME + "\"");
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
		ap.addKeyValue("username", "\"" + uname + "\"");
		ap.addKeyValue("password", "\"" + password + "\"");
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
	
	private static final native String getThumbnailImageURL(AlfrescoPacket ap) /*-{
		var bb;
		if (Blob==undefined) {
			bb = new (window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.OBlobBuilder || window.msBlobBuilder);
			bb.append(ap.contentStream);
			if (window.URL!=undefined&&window.URL.createObjectURL!=undefined)
				return window.URL.createObjectURL(bb.getBlob("image/png"));
			else
				return window.webkitURL.createObjectURL(bb.getBlob("image/png"));
		} else {
			bb = new Blob([ap.contentStream], { "type": "image/png" });
			if (window.URL!=undefined&&window.URL.createObjectURL!=undefined)
				return window.URL.createObjectURL(bb);
			else
				return window.webkitURL.createObjectURL(bb);
		}
	}-*/;

	public static String getThumbnail(String id) {
		return AlfrescoURL.buildAlfrescoThumbnailURL(id);
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
											ap.addKeyValue("imageURL", "\"" + getThumbnailImageURL(alfrescoPacket) + "\"");
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
											currentDirectoryId = result.getDirectory();
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
											Window.alert("Fooing updating current directory failed - " + caught.getMessage());
											callback.onFailure(caught);
										}
							
										@Override
										public void onSuccess(AlfrescoPacket result) {
											currentDirectoryId = result.getDirectory();
											callback.onSuccess(result);
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
	
	public static void setObjectProperties(String id, String postData, AlfrescoCallback<AlfrescoPacket> callback) {
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
	
	public static void getAllObjectComments(String nodeId, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  AlfrescoURL.getAlfrescoNodeURL(nodeId) + "/comments", 
								  null, 
								  false,
								  callback);
	}
	
	public static void addObjectComment(String nodeId, String title, String commentData, AlfrescoCallback<AlfrescoPacket> callback) {
		AlfrescoPacket ap = AlfrescoPacket.makePacket();
		ap.addKeyValue("title", title);
		ap.addKeyValue("content", commentData);
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getAlfrescoNodeURL(nodeId) + "/comments", 
								  ap.toJSONString(), 
								  false,
								  callback);
	}
	
	public static void deleteObjectComment(String nodeId, AlfrescoCallback<AlfrescoPacket> callback) {
		CommunicationHub.sendHTTP(CommunicationHub.DELETE, 
								  AlfrescoURL.getAlfrescoCommentsURL(nodeId), 
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
		String aspectString = "";
		for (int aspectIndex=0;aspectIndex<aspects.length;aspectIndex++)
			aspectString += "," + aspects[aspectIndex];
		if (aspectString!="")
			aspectString = aspectString.substring(1);
		ap.addKeyValue("aspects", aspectString);
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getAlfrescoAddAspectURL(), 
								  ap.toJSONWrappedString(), 
								  false,
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
		/* unused currently unsure if needed
			<cmis:propertyString propertyDefinitionId=\"cmis:name\">\
			<cmis:value>CMIS Demo</cmis:value>\
			</cmis:propertyString>\
		*/
		payloadXML += "</cmis:properties></cmisra:object></entry>";
		CommunicationHub.sendHTTP(CommunicationHub.POST, 
								  AlfrescoURL.getAlfrescoCreateNode(createLocation), 
								  payloadXML,
								  false,
								  callback);
	}
}
