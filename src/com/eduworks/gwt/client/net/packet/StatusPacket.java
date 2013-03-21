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

public class StatusPacket extends AjaxPacket{
	public static final String STATUS_BUSY = "busy";
	public static final String STATUS_DONE = "done";
	public static final String STATUS_ERROR = "error";
	public static final String ALERT_SUCCESS = "success";
	public static final String ALERT_ERROR = "error";
	public static final String ALERT_WARNING = "warning";
	public static final String ALERT_BUSY = "";
	
	//Required protected constructor
	protected StatusPacket() {};
	
	//public static final HashMap<String, String> outgoing = new HashMap<String, String>();
	public static final StatusPacket makePacket() {
		return (StatusPacket)JavaScriptObject.createObject().cast();
	};
	
	//JSNI methods	
	public final native String getStatusMessage() /*-{
		if (this.message!=undefined)
			return this.message;
		else
			return "";
	}-*/;

	public final native String getStatusState() /*-{
		if (this.state!=undefined)
			return this.state;
		else
			return "";
	}-*/;
	
	public final native String getStatusIdPrefix() /*-{
		if (this.idPrefix!=undefined)
			return this.idPrefix;
		else
			return "";
	}-*/;
	
	public final native boolean getStatusRendered() /*-{
		if (this.created!=undefined)
			return this.created;
		else
			return false;
	}-*/;
	
	public final native void setMessage(String message) /*-{
		this["message"] = message;
	}-*/;
	
	public final native void setRendered(boolean created) /*-{
		this["created"] = created;
	}-*/;
	
	public final native void setIdPrefix(String idPrefix) /*-{
		this["idPrefix"] = idPrefix;
	}-*/;
	
	public final native void setState(String state) /*-{
		this["state"] = state;
	}-*/;
}