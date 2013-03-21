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

import java.util.ArrayList;

import org.vectomatic.arrays.ArrayBuffer;

import com.eduworks.gwt.client.util.StringTokenizer;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class AlfrescoPacket extends AjaxPacket{
	
	public static final String USAGE_DELIMITER = "|";
	public static final String USAGE_STRATEGY_DELIMITER = "^";
	public static final String USAGE_COUNT_DELIMITER = "#";
	
	//Required protected constructor
	protected AlfrescoPacket() {};
	
	// AlfrescoPacket Private Methods 
	
	private final native String getMimeType0() /*-{
		return this.mimeType;
	}-*/;
	
	private final native String getId0() /*-{ 
		var acc = "";
		if (this.id!=null)
			acc = this.id;
		else if (this.item!=null)
			acc = this.item.nodeRef;
		else
			acc = this.nodeRef;
		return acc;
	}-*/;

	private final native String getDescription0() /*-{ 
		if (this.description != null)
			return this.description;
		else
			return "";
	}-*/;
	
	private final native String getFilename0() /*-{ 
		var acc="";
		if (this.name==null)
			acc = this.fileName;
		else
			acc = this.name;
		if (acc == null) acc = "";
		return acc; 
	}-*/;
	
	private final native String getTitle0() /*-{
		if (this.title != null)
			return this.title;
		else return "";
	}-*/;
	
	
	// AlfrescoPacket Public Methods 
	
	public static final AlfrescoPacket makePacket() {
		return (AlfrescoPacket)JavaScriptObject.createObject().cast();
	};
	
	public static final AlfrescoPacket makePacket(String mimeType, ArrayBuffer f) {
		AlfrescoPacket ap = makePacket();
		ap.setContent(f);
		ap.addKeyValue("mimeType", mimeType);
		return ap;
	};
	
	public static final AlfrescoPacket makePacket(String mimeType, String f) {
		AlfrescoPacket ap = makePacket();
		ap.setContent(f);
		ap.addKeyValue("mimeType", mimeType);
		return ap;
	};

	public static final AlfrescoPacket wrap(JSONValue jo)
	{
		if (jo instanceof JSONObject)
			return ((JSONObject) jo).getJavaScriptObject().cast();
		else if (jo.isArray() != null)
			return jo.isArray().getJavaScriptObject().cast();
		else
			return null;
	}

	public static final AlfrescoPacket wrap(JavaScriptObject jo)
	{
		return jo.cast();
	}
	
	
	// Login JSON 
	public final native String getTicket() /*-{ if (this.data==null) return null; else return this.data.ticket; }-*/;
	
	
	public final native JsArrayString getTags() /*-{
		return (this.contentStream==null)?[]:this.contentStream;
	}-*/;
	
	public final native void setContent(ArrayBuffer f) /*-{
		this.contentStream = f;
	}-*/;
	
	public final native void setContent(String f) /*-{
		this.contentStream = f;
	}-*/;
	
	public final native String getHttpStatus() /*-{
		if ((this['status']!=undefined) && (this['status']['code']!=undefined))
			return this['status']['code'];
		else return "";
	}-*/;
	
	// Alfresco search JSON
	public final native JsArray<AlfrescoPacket> getSearchRecords() /*-{ return this.items; }-*/;
			
	// Alfresco comment JSON	
	public final native int getCommentCount() /*-{ 
		if (this.total==null)
			return this.commentsCount;
		else
			return this.total; 
	}-*/;
	
	public final native JsArray<AlfrescoPacket> getCommentRecords() /*-{ return this.items; }-*/;
	
	public final native Adl3DRPacket getFeedbackRecords() /*-{ return this.feedback; }-*/;	
	
	public final native String getCommentContents() /*-{
		var acc;
		if (this.item!=null)
			acc = this.item.content;
		else
			acc = this.content;
		return acc;
	}-*/;
	
	public final native String getCommentAuthorUsername() /*-{ 
		var acc = "";
		if (this.author!=null)
			acc = this.author.username;
		else if (this.item!=null&&this.item.author!=null)
			acc = this.item.author.username;
		return acc; 
	}-*/;
	
	// Alfresco rating JSON
	public final native double getAverageRating() /*-{ 
		if (this.data!=null)
			if (this.data.nodeStatistics!=null)
				return this.data.nodeStatistics.fiveStarRatingScheme.averageRating; 
			else 
				return this.data.averageRating; 
		else if (this.rating!=null) 
			return this.rating;
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
	
	public final native ArrayBuffer getContents() /*-{ 
		if (this.b64Encoded)
			return $wnd.Base64.decode(this.contentStream);
		else
			return this.contentStream;
	}-*/;
	
	public final native String getContentString() /*-{ 
		if (this.b64Encoded)
			return $wnd.Base64.decode(this.contentStream);
		else
			return this.contentStream;
	}-*/;
	
	
	// Non-JSNI methods
	public final String getFilename() {
		String acc = getFilename0();
		if (acc=="" || acc == null)
			acc = getAlfrescoPropertyValue("cmis:contentStreamFileName");
		return acc;
	}
	
	public final String getTitle() {
		String acc = getTitle0();
		if (acc=="" || acc == null) {
			getAlfrescoPropertyValue("cm:title");
			if (acc == "" || acc == null) {
				acc = getAlfrescoPropertyValue("russel:title");
				if (acc == "" || acc == null) 
					acc = getAlfrescoPropertyValue("cmis:contentStreamFileName");
			}
		}
		return acc.replace("\"","'" );
	}

	public final String getDescription() {
		String acc = getDescription0();
		if (acc=="" || acc == null) 
			getAlfrescoPropertyValue("cm:description");
		return acc.replace("\"","'" );
	}

	public final String getMimeType() {
		String acc = getMimeType0();
		if (acc=="" || acc == null)
			acc = getAlfrescoPropertyValue("cmis:contentStreamMimeType");
		return acc.replace("\"","'");
	}
	
	public final String getNodeId() {
		String acc = getId0();
		if (acc!=null&&acc.lastIndexOf("/")!=-1)
			acc = acc.substring(acc.lastIndexOf("/")+1);
		else if (acc==null)
			acc = getAlfrescoPropertyValue("cmis:objectId");
		if (acc.lastIndexOf("/")!=-1)
			acc = acc.substring(acc.lastIndexOf("/")+1);
		return acc;
	}
	
	public final String getRusselValue(String key) {
		String acc = this.getValueString(key);
		if (acc == null || acc == "")
			acc = getAlfrescoPropertyValue(key);
		return acc;
	}

	public final String getPublisher() {
		return getAlfrescoPropertyValue("russel:publisher").replace("\"","'" );
	}
	
	public final String getCreateDate() {
		String acc = getAlfrescoPropertyValue("cmis:creationDate");
		if (acc.indexOf('T') > -1)
			acc = acc.substring(0, acc.indexOf('T'));
		return acc;
	}
	
	public native final String getAlfrescoPropertyValue(String propertyID) /*-{
		var records = [this];
		while (records.length>0) {
			var record = records.pop();
			for (var curNode in record)
				if (record.hasOwnProperty(curNode)) {
					if (curNode=="@propertyDefinitionId" && record[curNode]==propertyID)
						return ((record["cmis:value"]===undefined||record["cmis:value"]==null)? "" : record["cmis:value"]);
					if (typeof record[curNode]=="object")
						records.push(record[curNode]);
				}
		}
		return "";
	}-*/;
	
	public final native void addAsset(String section, JavaScriptObject obj) /*-{
		var addAsset = true;
		var arrayIndex = 0;
		if (this[section]==null)
			this[section] = [];
		for (var x=0;x<this[section].length;x++)
			if (addAsset&&this[section][x].id == obj.id) {
				addAsset = false;
				arrayIndex = x;
			}
		if (addAsset)
			this[section].push(obj);
		else {
			if (obj.notes==null)
				obj.notes = this[section][arrayIndex].notes;
			this[section][arrayIndex] = obj;
		}
	}-*/;

	public final native void removeAsset(String section, String nodeId) /*-{
		var removeAsset = false;
		var arrayIndex = 0;
		if (this[section]!=null)
			for (var x=0;x<this[section].length;x++) {
				if (this[section][x].id == nodeId) { 
					removeAsset = true;
					arrayIndex = x;
				}
			}
		if (removeAsset)
			this[section].splice(arrayIndex, 1);
	}-*/;
	
	public final ArrayList<AlfrescoPacket> parseIsdUsage() {
		ArrayList<AlfrescoPacket> parsedIsd = new ArrayList<AlfrescoPacket>();
		AlfrescoPacket use;
		StringTokenizer useList, tempList; 
		String useStr = "";
		String templateStr="";
		String strategyStr=""; 
		int count=0;
		String nodeUsage = this.getRusselValue("russel:epssStrategy");
		
		if ((nodeUsage != "") && (nodeUsage != null))  {
			useList = new StringTokenizer(nodeUsage, USAGE_DELIMITER);
			while (useList.hasMoreTokens()) {
				useStr = useList.nextToken();
				use = AlfrescoPacket.makePacket();

				tempList = new StringTokenizer(useStr, USAGE_STRATEGY_DELIMITER); 
				if (tempList.countTokens() == 2) {
					templateStr = tempList.nextToken();
					use.addKeyValue("template", templateStr);
					tempList = new StringTokenizer(tempList.nextToken(), USAGE_COUNT_DELIMITER);
					if (tempList.countTokens() == 2) {
						strategyStr = tempList.nextToken();
						use.addKeyValue("strategy", strategyStr);
						count = (int)Integer.parseInt(tempList.nextToken());
						use.addKeyValue("count",count);
					}
				}
				parsedIsd.add(use);
			}
		}
		return parsedIsd;
	}
}