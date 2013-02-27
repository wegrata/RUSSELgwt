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

package com.eduworks.gwt.client.net.packet;

import org.vectomatic.arrays.ArrayBuffer;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class FLRPacket extends AjaxPacket{
	//Required protected constructor
	protected FLRPacket() {};
	
	//public static final HashMap<String, String> outgoing = new HashMap<String, String>();
	public static final FLRPacket makePacket() {
		return (FLRPacket)JavaScriptObject.createObject().cast();
	};
	
	public static final FLRPacket makePacket(String mimeType, ArrayBuffer f) {
		FLRPacket fp = makePacket();
		fp.setContent(f);
		fp.addKeyValue("mimeType", mimeType);
		return fp;
	};
	
	public static final FLRPacket makePacketSample() {
		String sample = "{\"TOS\": {\"submission_TOS\": \"http://www.learningregistry.org/tos/cc0/v0-5/\"},\"active\": true,\"doc_type\": \"resource_data\",\"doc_version\": \"0.23.0\",\"identity\": {\"curator\": \"\",\"owner\": \"\",\"submitter\": \"Eduworks\",\"signer\": \"EduworksTesting\",\"submitter_type\": \"agent\"},\"keys\": [\"science\",\"Newton\",\"apple\",\"RUSSEL\"],\"payload_placement\": \"inline\",\"payload_schema\": [\"hashtags\",\"describing\",\"resource_locator\",\"format\"],\"resource_data\": \"Put_anything_like_metadata, xml_or_whatever_here\",\"resource_data_type\": \"metadata\",\"resource_locator\": \"URI_of_resource\"}";
//		String temp = "{"request": {"HTTP_request": "", "verb": "identify"}, "OK": true, "responseDate": "2013-01-29T23:44:29.582466Z", "identify": {"deletedRecord": "no", "node_id": "node01", "granularity": "YYYY-MM-DDThh:mm:ssZ", "protocolVersion": "2.0", "adminEmail": "admin@learningregistry.org", "repositoryName": "Learning Registry Open, Public Community", "baseURL": "https://node01.public.learningregistry.net/harvest", "service_version": "0.10.0", "earliestDatestamp": "2011-10-26T15:14:02"}, "error": ""}";
//		String sample = "{\"activity\": {\"actor\": {\"objectType\": \"teacher\",\"description\": [\"high school\",\"english\"]},\"verb\": {\"action\": \"taught\",\"measure\": {\"measureType\": \"count\",\"value\": 15},\"date\": \"2011-05-01/2011-05-31\"},\"object\": \"http://URL/to/tale-of-two-cities/\"}}";
		FLRPacket fp = (FLRPacket) CommunicationHub.parseJSON(sample);
		return fp;
	};
	

	public static final FLRPacket wrap(JSONValue jo)
	{
		if (jo instanceof JSONObject)
			return ((JSONObject) jo).getJavaScriptObject().cast();
		else if (jo.isArray() != null)
			return jo.isArray().getJavaScriptObject().cast();
		else
			return null;
	}

	public static final FLRPacket wrap(JavaScriptObject jo)
	{
		return jo.cast();
	}
	//JSNI methods
	
	public final native String getMimeType() /*-{
		return this.mimeType;
	}-*/;
	
	
	/** FLR Login JSON */
	public final native String getTicket() /*-{ if (this.data==null) return null; else return this.data.ticket; }-*/;
	
	
	public final native JsArrayString getTags() /*-{
		return (this.contentStream==null)?[]:this.contentStream;
	}-*/;
	
	public final native void setContent(ArrayBuffer f) /*-{
		this.contentStream = f;
	}-*/;
	
	public final native String getHttpStatus() /*-{
		if ((this['status']!=undefined) && (this['status']['code']!=undefined))
			return this['status']['code'];
		else return "";
	}-*/;
	

	private final native String getId() /*-{ 
		var acc = "";
		if (this.id!=null)
			acc = this.id;
		else if (this.item!=null)
			acc = this.item.nodeRef;
		else
			acc = this.nodeRef;
		return acc;
	}-*/;
	
	
	// FLR search JSON
	public final native int getSearchRecordCount() /*-{ return this.totalRecords; }-*/;
	public final native int getSeachStartIndex() /*-{ return this.startIndex; }-*/;
	public final native JsArray<FLRPacket> getShareSearchRecords() /*-{ return this.items; }-*/;
	public final native JsArray<FLRPacket> getSearchRecords() /*-{ return this.records; }-*/;
	public final native String getIcon() /*-{ return this.icon; }-*/;
	public final native String getUrlContext() /*-{ return this.urlcontext; }-*/;
	public final native String getDescription() /*-{ return this.description; }-*/;
	
	// FLR rating JSON
	public final native double getAverageRating() /*-{ 
		if (this.data!=null)
			if (this.data.nodeStatistics!=null)
				return this.data.nodeStatistics.fiveStarRatingScheme.averageRating; 
			else 
				return this.data.averageRating; 
		else
			return 0;
	}-*/;
	
	public final native int getRatingCount() /*-{ 
		if (this.data!=null)
			if (this.data.nodeStatistics!=null)
				return this.data.nodeStatistics.fiveStarRatingScheme.ratingsCount; 
			else 
				return this.data.ratingsCount;
		else
		 	return 0;
	}-*/;
	
	
	// FLR file JSON
	public final native String getDirectory() /*-{ return this.id; }-*/;
	
	private final native String getFileName() /*-{ 
		var acc="";
		if (this.name==null)
			acc = this.fileName;
		else
			acc = this.name;
		return acc; 
	}-*/;
	
	public final native String getTitle() /*-{
		return this.title;
	}-*/;
	
	public final native ArrayBuffer getContents() /*-{ 
		if (this.b64Encoded)
			return $wnd.Base64.decode(this.contentStream);
		else
			return this.contentStream;
	}-*/;

	// FLR error json
	public final native String getStatusCode() /*-{ if (this.status==null) return null; else return this.status.code; }-*/;
	public final native String getStatusName() /*-{ if (this.status==null) return null; else return this.status.name; }-*/;
	public final native String getStatusDescription() /*-{ if (this.status==null) return null; else return this.status.description; }-*/;

	// Non-JSNI methods
	public final String getFilename() {
		String acc = getFileName();
		if (acc==null)
			acc = getPropertyValue("@propertyDefinitionId", "cmis:contentStreamFileName");
		if (acc=="")
			acc = null;
		return acc;
	}
	
	public final String getNodeId() {
		String acc = getId();
		if (acc!=null&&acc.lastIndexOf("/")!=-1)
			acc = acc.substring(acc.lastIndexOf("/")+1);
		else if (acc==null)
			acc = getPropertyValue("@propertyDefinitionId", "cmis:objectId");
		if (acc=="")
			acc = null;
		else if (acc.lastIndexOf("/")!=-1)
			acc = acc.substring(acc.lastIndexOf("/")+1);
		return acc;
	}
}