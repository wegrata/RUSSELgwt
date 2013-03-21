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

package com.eduworks.gwt.client.net.packet;

import org.vectomatic.arrays.ArrayBuffer;

import com.eduworks.gwt.client.net.api.Adl3DRApi;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class Adl3DRPacket extends AjaxPacket{
	//Required protected constructor
	protected Adl3DRPacket() {};
	
	// Adl3DRPacket private methods

	private final native static String getADLdate0(JsDate date) /*-{
		var fixed = (date.getMonth()+1) + "/"+date.getDate()+"/"+date.getFullYear()+" "+date.getHours()+":"+date.getMinutes()+":00";
		return fixed;
	}-*/;
	
	private final native String getId0() /*-{ 
		var acc = "";
		if (this.PID!=null)
			acc = this.PID;
		return acc;
	}-*/;
	
	private final native String getTitle0() /*-{ 
		var acc="";
		if (this.Title!=null)
			acc = this.Title;
		return acc; 
	}-*/;
	
	private final native String getDataLink0() /*-{ 
		var acc="";
		if (this.DataLink!=null)
			acc = this.DataLink;
		return acc; 
	}-*/;
	
	private final native String getDescription0() /*-{ 
		var acc="";
		if (this.Description!=null)
			acc = this.Description;
		return acc; 
	}-*/;
	
	private final native String getFormat0() /*-{ 
		var acc="";
		if (this.Format!=null)
			acc = this.Format;
		return acc; 
	}-*/;
	
	private final native String getKeywords0() /*-{ 
		var acc="";
		if (this.Keywords!=null)
			acc = this.Keywords;
		return acc; 
	}-*/;
	
	private final native String getOwner0() /*-{ 
		var acc="";
		if (this.DeveloperName!=null && this.DeveloperName != "")
			acc = "Developer: "+this.DeveloperName;
		if (this.ArtistName != null && this.ArtistName != "" && acc != "")
			acc += " / Artist: "+this.ArtistName;
		else if (this.ArtistName != null && this.ArtistName != "")
			acc = "Artist: "+this.ArtistName;
		return acc; 
	}-*/;
	
	private final native String getVersion0() /*-{ 
		var acc="";
		if (this.Revision!=null)
			acc = this.Revision;
		return acc; 
	}-*/;
	
	private final native String getPublisher0() /*-{ 
		var acc="";
		if (this.SponsorName!=null)
			acc = this.SponsorName;
		return acc; 
	}-*/;
	
	private final native String getCreateDate0() /*-{ 
		var acc="";
		if (this.UploadedDate!=null)
			acc = this.UploadedDate;
		return acc; 
	}-*/;
	
	private final native String getThumbnail0() /*-{ 
		var acc="";
		if (this._ThumbnailLink!=null)
			acc = this._ThumbnailLink;
		return acc; 
	}-*/;
	
	private final native String getScreenshot0() /*-{ 
		var acc="";
		if (this._ScreenshotLink!=null)
			acc = this._ScreenshotLink;
		return acc; 
	}-*/;
	
	private final native String getComment0() /*-{ 
		var acc="";
		if (this.ReviewText!=null && this.ReviewText != "")
			acc = this.ReviewText;
		return acc; 
	}-*/;


	
	// Adl3DRPacket public methods

	public static final Adl3DRPacket makePacket() {
		return (Adl3DRPacket)JavaScriptObject.createObject().cast();
	};
	
	public static final Adl3DRPacket makePacket(String mimeType, ArrayBuffer f) {
		Adl3DRPacket packet = makePacket();
		packet.setContent(f);
		packet.addKeyValue("mimeType", mimeType);
		return packet;
	};
	
	public static final Adl3DRPacket makePacketReview(String comment, Integer rating) {
		Adl3DRPacket packet = makePacket();
		// The ADL 3DR UI uses a default rating of 3 for each comment, unless manually adjusted by the reviewer. 
		if (rating == 0) {
			rating = 3;
		}
			
		JsDate now = JsDate.create();
		packet.addKeyValue("DateTime", getADLdate0(now));
		packet.addKeyValue("Rating", rating);
		packet.addKeyValue("ReviewText", comment);
		packet.addKeyValue("Submitter", Adl3DRApi.ADL3DR_RUSSEL_SUBMITTER);
		return packet;
	};

	public static final Adl3DRPacket wrap(JSONValue jo)
	{
		if (jo instanceof JSONObject)
			return ((JSONObject) jo).getJavaScriptObject().cast();
		else if (jo.isArray() != null)
			return jo.isArray().getJavaScriptObject().cast();
		else
			return null;
	}

	public static final Adl3DRPacket wrap(JavaScriptObject jo)
	{
		return jo.cast();
	}
	
	//JSNI methods
	
	public final native String getMimeType() /*-{
		return this.mimeType;
	}-*/;
	
	
	/** ADL 3DR Login JSON */
	
	public final native void setContent(ArrayBuffer f) /*-{
		this.contentStream = f;
	}-*/;
	
	public final native String getHttpStatus() /*-{
		if ((this['status']!=undefined) && (this['status']['code']!=undefined))
			return this['status']['code'];
		else return "";
	}-*/;

	// ADL3DRPacket Public JSON Methods
	public final String getNodeId() {
		String acc = getId0();
		return acc;
	}
	
	public final String getTitle() {
		String acc = getTitle0();
		return acc;
	}
	
	public final String getDataLink() {
		String acc = getDataLink0();
		return acc;
	}
	
	public final String getFilename() {
		String acc = getDataLink0() + ".3dr";
		return acc;
	}
	
	public final String getComment() {
		String acc = getComment0();
//		if (acc == "" || acc == null) {
//			acc = this.getValueString("Revie Text");  // Note: this is temporary while the ADL 3DR is returning an improper Key in Reviews record
//		}
		return acc;
	}

	
	public final native JsArray<Adl3DRPacket> getSearchRecords() /*-{ return this; }-*/;
	public final native Adl3DRPacket getFeedbackRecords() /*-{ return this.feedback; }-*/;	
	public final AlfrescoPacket convert2Russel() {
		AlfrescoPacket newAp = AlfrescoPacket.makePacket();
		newAp.addKeyValue("mimeType", Adl3DRApi.ADL3DR_RUSSEL_MIME_TYPE);	
		if (this.getDescription0() == "") 
			newAp.addKeyValue("cm:description", this.getKeywords0());
		else
			newAp.addKeyValue("cm:description", this.getDescription0());
		newAp.addKeyValue("cm:title", this.getTitle0());
		newAp.addKeyValue("cmis:name", this.getTitle0());		
		newAp.addKeyValue("cmis:createdBy", this.getOwner0());		
		if (this.getFormat0() == "")
			newAp.addKeyValue("cmis:contentStreamMimeType", Adl3DRApi.ADL3DR_RUSSEL_MIME_TYPE);		
		else
			newAp.addKeyValue("cmis:contentStreamMimeType", Adl3DRApi.ADL3DR_RUSSEL_MIME_TYPE +", "+this.getFormat0());					
		newAp.addKeyValue("cmis:versionLabel", this.getVersion0());		
		newAp.addKeyValue("russel:publisher", this.getPublisher0());		
		newAp.addKeyValue("cmis:creationDate", this.getCreateDate0());
		newAp.addKeyValue("russel:FLRtag", "http://3dr.adlnet.gov/Public/Model.aspx?ContentObjectId="+this.getId0());	
		newAp.addKeyValue("keywords", this.getKeywords0());
		newAp.addKeyValue("thumbnail", this.getThumbnail0());		
		newAp.addKeyValue("screenshot", this.getScreenshot0());		
		return newAp;
	}

	public final int getRatingCount() {
		int count = 0;
		for (int x=0;x<getSearchRecords().length();x++) {
			if (getSearchRecords().get(x).getValue("Rating") != null) {
				count++;
			}
		}
		return count;
	}
	
	public final float getAverageRating() {
		int total = 0;
		int count = 0;
		float avg = 0;
		
		for (int x=0;x<getSearchRecords().length();x++) {
			if (getSearchRecords().get(x).getValue("Rating") != null) {
				count++;
				total+=Integer.parseInt(getSearchRecords().get(x).getValueString("Rating"));
			}
		}
		if (count > 0) avg = total/count;
		return avg;
	}
	
	public final int getCommentCount() {
		int count = 0;
		for (int x=0;x<getSearchRecords().length();x++) {
			if (getSearchRecords().get(x).getValue("ReviewText") != null) {
				count++;
			}
		}
		return count;
	}
	
}