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
import com.eduworks.gwt.client.net.callback.FLRCallback;
import com.eduworks.gwt.client.net.packet.FLRPacket;
import com.eduworks.gwt.client.util.Base64;
import com.google.gwt.user.client.Window;

public class FLRApi {
	public static final String FLR_RUSSEL_MIME_TYPE = "russel/flr";
	public static final String FLR_SAND_BOX_URL = "http://sandbox.learningregistry.org/";
	public static String FLR_REPOSITORY_URL = FLR_SAND_BOX_URL;  // The default will be the FLR sand box, but within an instance of RUSSEL this can be configured.
	public static final String FLR_PUBLISH_ACTIONS_NONE = "FLR-NoPublish";
	public static final String FLR_PUBLISH_ACTIONS_GENERAL = "FLR-GeneralPublish";
	public static final String FLR_PUBLISH_ACTIONS_ISD = "FLR-IsdPublish";
	public static final String FLR_PUBLISH_ACTIONS_ALL = "FLR-AllPublish";
	public static String FLR_PUBLISH_MODE = FLR_PUBLISH_ACTIONS_ALL;
	public static final String FLR_SEARCH_ACTIONS_NONE = "FLR-NoSearch";
	public static final String FLR_SEARCH_ACTIONS_GENERAL = "FLR-GeneralSearch";
	public static final String FLR_SEARCH_ACTIONS_ISD = "FLR-IsdSearch";
	public static final String FLR_SEARCH_ACTIONS_ALL = "FLR-AllSearch";
	public static String FLR_SEARCH_MODE = FLR_SEARCH_ACTIONS_ALL;
	
//	public static final String RUSSEL_RATING_SCHEME = "fiveStarRatingScheme";
//	public static final String FOLDER_OBJECT = "cmis:folder";
//	public static final String DOCUMENT_OBJECT = "cmis:document";
	public static String username = "";
	public static String password = "";
	public static String basicAuthenicationHeader = null;
	public static String currentDirectoryId = "";
	public static String ticket = null;
	
	public static String[] getWebServiceCallParameters() {
		if (ticket == null) return new String[0];

		return new String[] {"alf_ticket", ticket};
	}

	public static void getFLRdata(FLRCallback<FLRPacket> callback) {
		if (FLR_REPOSITORY_URL == FLR_SAND_BOX_URL) {
			username = "lrdev";
			password = "lrdev";
			basicAuthenicationHeader = Base64.encode(username + ":" + password);
			CommunicationHub.sendHTTP(CommunicationHub.GET,
									  AlfrescoURL.getAlfrescoFlrImportURL(FLR_REPOSITORY_URL),  
									  null,
									  false, 
									  callback);
		}
		else {
			Window.alert("Unrecognized FLR URL -- cannot log in");
		}

	}

	public static void putFLRdata(String postData, FLRCallback<FLRPacket> callback) {
		if (FLR_REPOSITORY_URL == FLR_SAND_BOX_URL) {
			username = "lrdev";
			password = "lrdev";
			basicAuthenicationHeader = Base64.encode(username + ":" + password);
			FLRPacket fp = FLRPacket.makePacketSample();
			String Url = FLR_REPOSITORY_URL + "publish";
			Url = "http://posttestserver.com/post.php";
			
			CommunicationHub.sendHTTP(CommunicationHub.POST,
									  AlfrescoURL.getAlfrescoDispatchURL(Url, "POST"), 
									  fp.getRawString(), 
									  false, 
									  callback);
		}
		else {
			Window.alert("Unrecognized FLR URL -- cannot log in");
		}
	}
	
//	private static String getCurrentDirectoryId() {
//		return currentDirectoryId.split("/")[currentDirectoryId.split("/").length-1];
//	}
//	
//	public static void getObjectRatings(final String docResId, FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.GET, 
//								  CommunicationHub.getFLRRatingURL(docResId),
//								  null, 
//								  false,
//								  callback);
//	}
//
//	public static void login(String uname, String password, FLRCallback<FLRPacket> callback) {
//		username = uname;
//		basicAuthenicationHeader = Base64.encode(username + ":" + password);
//		FLRPacket fp = FLRPacket.makePacket();
//		fp.addKeyValue("username", "\"" + uname + "\"");
//		fp.addKeyValue("password", "\"" + password + "\"");
//		CommunicationHub.sendHTTP(CommunicationHub.POST, 
//								  CommunicationHub.getFLRLoginURL(), 
//								  fp.toJSONString(), 
//								  false,
//								  callback);
//	}
//
//	public static void logout(FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.DELETE, 
//								  CommunicationHub.getFLRLogoutURL(getWebServiceCallParameters()[1]), 
//								  null, 
//								  false,
//								  callback);
//	}
//	
//	private static final native String getThumbnailImageURL(FLRPacket fp) /*-{
//		var bb;
//		if (Blob==undefined) {
//			bb = new (window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.OBlobBuilder || window.msBlobBuilder);
//			bb.append(ap.contentStream);
//			if (window.URL!=undefined&&window.URL.createObjectURL!=undefined)
//				return window.URL.createObjectURL(bb.getBlob("image/png"));
//			else
//				return window.webkitURL.createObjectURL(bb.getBlob("image/png"));
//		} else {
//			bb = new Blob([fp.contentStream], { "type": "image/png" });
//			if (window.URL!=undefined&&window.URL.createObjectURL!=undefined)
//				return window.URL.createObjectURL(bb);
//			else
//				return window.webkitURL.createObjectURL(bb);
//		}
//	}-*/;
//
//	public static String getThumbnail(String id) {
//		return CommunicationHub.buildFLRThumbnailURL(id);
//	}
//	
//	public static void getThumbnail(String id, final FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.GET,
//								  CommunicationHub.buildFLRThumbnailURL(id),
//								  null,
//								  true,
//								  new FLRCallback<FLRPacket>() {
//									@Override
//									public void onSuccess(FLRPacket FLRPacket) {
//										FLRPacket fp = com.eduworks.gwt.russel.ui.client.net.FLRPacket.makePacket();
//										fp.addKeyValue("imageURL", "\"" + getThumbnailImageURL(FLRPacket) + "\"");
//										callback.onSuccess(fp);
//									}
//									
//									@Override
//									public void onFailure(Throwable caught) {
//										callback.onFailure(caught);
//									}
//								});
//	}
//	
//	public static void deleteDocument(String id, FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.DELETE, 
//								  CommunicationHub.getFLRNodeURL(id),
//								  null, 
//								  false,
//								  callback);
//	}
//
//	public static void updateCurrentDirectory(final String path) {
//		CommunicationHub.sendHTTP(CommunicationHub.GET, 
//								  CommunicationHub.getFLRFolderIdURL(path), 
//								  null, 
//								  false,
//								  new FLRCallback<FLRPacket>() {
//										@Override
//										public void onFailure(Throwable caught) {
//											Window.alert("Fooing updating current directory failed - " + caught.getMessage());
//										}
//							
//										@Override
//										public void onSuccess(FLRPacket result) {
//											currentDirectoryId = result.getDirectory();
//										}
//									});
//	}
//	
//	public static void updateCurrentDirectory(final String path, final FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.GET, 
//								  CommunicationHub.getFLRFolderIdURL(path), 
//								  null, 
//								  false,
//								  new FLRCallback<FLRPacket>() {
//										@Override
//										public void onFailure(Throwable caught) {
//											Window.alert("Fooing updating current directory failed - " + caught.getMessage());
//											callback.onFailure(caught);
//										}
//							
//										@Override
//										public void onSuccess(FLRPacket result) {
//											currentDirectoryId = result.getDirectory();
//											callback.onSuccess(result);
//										}
//									});
//	}
//
//	public static void search(FLRPacket fp, FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.GET, 
//				CommunicationHub.getFLRShareSearch(fp.getValueString("terms"), 
//														fp.getValueString("tags"), 
//														100, 
//														fp.getValueString("sort"),
//														fp.getValueString("query")), 
//								  null, 
//								  false,
//								  callback);	
//	}
//	
//	public static void setObjectProperties(String id, String postData, FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.POST,
//								  CommunicationHub.getFLRMetadataURL(id), 
//								  postData, 
//								  false, 
//								  callback);
//	}
//	
//	public static String downloadContentURL(String id, String docName) {
//		return CommunicationHub.getObjectStreamURL(id, docName);
//	}
//	
//	public static void getObjectStream(String id, String docName, FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.GET,
//								  CommunicationHub.getObjectStreamURL(id, docName),
//								  null,
//								  true,
//								  callback);
//	}
//	
//	
//	public static void getObjectString(String id, String docName, FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.GET,
//								  CommunicationHub.getObjectStreamURL(id, docName),
//								  null,
//								  false,
//								  callback);
//	}
//	
//	public static void getAllObjectComments(String nodeId, FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.GET, 
//								  CommunicationHub.getFLRNodeURL(nodeId) + "/comments", 
//								  null, 
//								  false,
//								  callback);
//	}
//	
//	public static void addObjectComment(String nodeId, String title, String commentData, FLRCallback<FLRPacket> callback) {
//		FLRPacket fp = FLRPacket.makePacket();
//		fp.addKeyValue("title", title);
//		fp.addKeyValue("content", commentData);
//		CommunicationHub.sendHTTP(CommunicationHub.POST, 
//								  CommunicationHub.getFLRNodeURL(nodeId) + "/comments", 
//								  fp.toJSONString(), 
//								  false,
//								  callback);
//	}
//	
//	public static void deleteObjectComment(String nodeId, FLRCallback<FLRPacket> callback) {
//		CommunicationHub.sendHTTP(CommunicationHub.DELETE, 
//				  				  CommunicationHub.getFLRCommentsURL(nodeId), 
//				  				  null, 
//				  				  false, 
//				  				  callback);
//	}
	
//	public static void addAspectToNode(String nodeId, String[] aspects, FLRCallback<FLRPacket> callback) {
//		FLRPacket fp = FLRPacket.makePacket();
//		fp.addKeyValue("nodeRef", "workspace://SpacesStore/" + nodeId);
//		String aspectString = "";
//		for (int aspectIndex=0;aspectIndex<aspects.length;aspectIndex++)
//			aspectString += "," + aspects[aspectIndex];
//		if (aspectString!="")
//			aspectString = aspectString.substring(1);
//		fp.addKeyValue("aspects", aspectString);
//		CommunicationHub.sendHTTP(CommunicationHub.POST, 
//								  CommunicationHub.getFLRAddAspectURL(), 
//								  fp.toJSONWrappedString(), 
//								  false,
//								  callback);	
//	}
	
	/**
	 * 
	 * @param filename
	 * @param contents for non-html text use a base64 encoding
	 * @param mimeType
	 * @param createLocation Root starts at Company Home
	 * @param objectType
	 * @param callback
	 */
//	public static void createObjectNode(String filename, String contents, String mimeType, String createLocation, String objectType, FLRCallback<FLRPacket> callback) {
//		String payloadXML = "";
//		payloadXML = CommunicationHub.ATOM_XML_PREAMBLE + "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:cmisra=\"http://docs.oasis-open.org/ns/cmis/restatom/200908/\" xmlns:cmis=\"http://docs.oasis-open.org/ns/cmis/core/200908/\">";
//		payloadXML += "<title>" + filename + "</title>";
//		payloadXML += "<author><name>" + username + "</name></author>";
//		payloadXML += "<summary></summary>";
//		if (objectType==DOCUMENT_OBJECT)
//			payloadXML += "<content type=\"" + mimeType + "\">" + contents + "</content>";
//		payloadXML += "<cmisra:object><cmis:properties>";
//		payloadXML += "<cmis:propertyId propertyDefinitionId=\"cmis:objectTypeId\">";
//		if (objectType==DOCUMENT_OBJECT)
//			payloadXML += "<cmis:value>cmis:document</cmis:value>";
//		else if (objectType==FOLDER_OBJECT)
//			payloadXML += "<cmis:value>cmis:folder</cmis:value>";
//		payloadXML += "</cmis:propertyId>";
//		/* unused currently unsure if needed
//			<cmis:propertyString propertyDefinitionId=\"cmis:name\">\
//			<cmis:value>CMIS Demo</cmis:value>\
//			</cmis:propertyString>\
//		*/
//		payloadXML += "</cmis:properties></cmisra:object></entry>";
//		CommunicationHub.sendHTTP(CommunicationHub.POST, 
//								  CommunicationHub.getFLRCreateNode(createLocation), 
//								  payloadXML,
//								  false,
//								  callback);
//	}
			
}
