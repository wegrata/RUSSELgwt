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
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONObject;

public class AjaxPacket extends JavaScriptObject{
	//Required protected constructor
	protected AjaxPacket() {};
	
	//JSNI methods
	
	public final native String getRawString() /*-{
		return this.contentStream;
	}-*/;
	
	public final native void addKeyValue(String key, Object value) /*-{
		this[key] = value;
	}-*/;
	
	public final native void addKeyArray(String key, Object value) /*-{
		if (value==null)
			this[key] = [];
		else
			this[key] = [value];
	}-*/;
	
	public final native JavaScriptObject getValue(String key) /*-{
		return this[key];
	}-*/;
	
	public final native String getValueString(String key) /*-{
		if (this[key]!=null)
			return this[key];
		else
			return "";
	}-*/;
	
	public final native JsArrayString getRootKeys() /*-{
		var acc = [];
		for (var key in this)
			if (this.hasOwnProperty(key))
				acc.push(key);
		return acc;
	}-*/; 
	
	public final native boolean hasKey(String key) /*-{
		var acc = true;
		if (this[key]===undefined) acc = false;
		return acc;
	}-*/;
	
	public final String toJSONString() {
		return new JSONObject(this).toString().replaceAll("{\"value_0\":(.*?)}", "$1");
	}

	public final AjaxPacket mergePackets(AjaxPacket ap) {
		JsArrayString keys = ap.getRootKeys();
		for (int keyIndex=0;keyIndex<keys.length();keyIndex++)
			this.addKeyValue(keys.get(keyIndex), ap.getValue(keys.get(keyIndex)));
		return this;
	}
}