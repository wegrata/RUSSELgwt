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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class FLRPacket extends AjaxPacket{
	//Required protected constructor
	protected FLRPacket() {};
	
	//public static final HashMap<String, String> outgoing = new HashMap<String, String>();
	public static final FLRPacket makePacket() {
		return (FLRPacket)JavaScriptObject.createObject().cast();
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
	
	public static final FLRPacket makePacketTOS() {
		FLRPacket fp = makePacket();
		fp.addKeyValue("submission_TOS", "http://www.learningregistry.org/tos/cc0/v0-5/");
		return fp;
	};
	
	public static final FLRPacket makePacketIdentity(String owner) {
		FLRPacket fp = makePacket();
		fp.addKeyValue("curator", "ADL RUSSEL");
		fp.addKeyValue("owner", owner);
		fp.addKeyValue("submitter", "ADL RUSSEL");
		fp.addKeyValue("submitter_type", "agent");
		return fp;
	};
	
	// FLR response JSON
	public final native String getResponseStatus() /*-{
		if (this.OK) return "true";
		else return "false";
	}-*/;
	
	public final native String getResponseError() /*-{
		return this.error;
	}-*/;
	
	public final native JsArray<FLRPacket> getResponseDocResults() /*-{
		return this.document_results;
	}-*/;

	public final native String getResponseDocID() /*-{
		return this.doc_ID;
	}-*/;
}