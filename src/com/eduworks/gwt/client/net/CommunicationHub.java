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

package com.eduworks.gwt.client.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vectomatic.file.Blob;

import com.eduworks.gwt.client.net.callback.AjaxCallback;
import com.eduworks.gwt.client.net.packet.AjaxPacket;
import com.eduworks.gwt.client.net.packet.AlfrescoPacket;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public class CommunicationHub implements RemoteService  {
	/* STATIC METHODS */
	
	public static String rootURL = "";
	public static String baseURL = rootURL+"alfresco/";
	public static String siteURL = "";
	public static final String ATOM_XML_PREAMBLE = "atomxml";
	public final static String POST = "POST";
	public final static String GET = "GET";
	public final static String DELETE = "DELETE";
	public final static String Put = "PUT";
	
	public static String randomString() {
		String acc = "";
		for (int x=0;x<20;x++) 
			acc += Character.toString((char)(Math.random() * 25 + 97));
		return acc;
	}	
	
	public static String unwrapJSONString(String raw) {
		return raw.substring(raw.indexOf("{"),raw.lastIndexOf("}")+1);
	}

	/* NATIVE METHODS */
	public final native static JavaScriptObject parseJSON(String x) /*-{ 
		var e = (x!=null)? x.replace(/[\r\n\t]/g," "): "{}";
		return eval('(' + ((e==""||e==null)? '{}' : e) + ')'); 
	}-*/;

	public static native JSONValue xml2json(String xml, String tab) /*-{
	    var dom = $wnd.parseXml(xml);
	    var jsonStr = $wnd.xml2json(dom, tab);
	    return@com.google.gwt.json.client.JSONParser::evaluate(Ljava/lang/String;Z)(jsonStr,false);
	}-*/;
	
	public final native static JsArray<AlfrescoPacket> parseJSA(String x) /*-{ 
		var e = (x!=null)? x.replace(/[\r\n\t]/g," "): "[]";
		return eval('(' + ((e==""||e==null)? '[]' : e) + ')'); 
	}-*/;

	public static void sendFormWithCallbackObject(Object[][] parts, AsyncCallback<JSONObject> callback,String baseUri, String functionName, String... strings)
	{
		String[] params = new String[strings.length+2];
		for (int i = 0;i < strings.length;i++)
			params[i] = strings[i];
		params[params.length-2]="sec";
		params[params.length-1] = Long.toString(EwCommunication.random.nextLong());
		String uri = EwCommunication.buildUrlInternal(baseUri, functionName, params);
		sendForm(uri,parts,EwCommunication.wrapPostCallbackObject(uri,callback));
	}
	
	public static void sendFormWithCallbackArray(Object[][] parts, AsyncCallback<JSONArray> callback,String baseUri, String functionName, String... strings)
	{
		sendFormWithCallbackArray(parts, callback,null, baseUri, functionName, strings);
	}
		
	public static void sendFormWithCallbackArray(Object[][] parts, AsyncCallback<JSONArray> callback,String progressName,String baseUri, String functionName, String... strings)
	{
		String[] params = new String[strings.length+2];
		for (int i = 0;i < strings.length;i++)
			params[i] = strings[i];
		params[params.length-2]="sec";
		params[params.length-1] = Long.toString(EwCommunication.random.nextLong());
		String uri = EwCommunication.buildUrlInternal(baseUri, functionName, params);
		sendForm(uri,parts,EwCommunication.wrapPostCallbackArray(uri,callback),progressName);
	}

	public static Map<String,String> progress = new HashMap<String,String>();
	public static List<VoidListener> listeners = new ArrayList<VoidListener>();

	public static void registerFileListener(VoidListener l)
	{
		listeners.add(l);
	}

	public static void unregisterFileListener(VoidListener l)
	{
		listeners.remove(l);
	}

	public static void executeFileListeners(String key)
	{
		for (VoidListener vl : listeners)
		{
			vl.event();
			vl.eventWithString(key);
		}
	}
	
	public static void registerFileTransfer(String key)
	{
		if (key == null) return;
		progress.put(key, "Connecting...");
		executeFileListeners(key);
	}

	public static void updateFileTransfer(String key, Double progressD)
	{
		if (key == null) return;
		if (progressD < 100)
			progress.put(key, "Transferring: " + progressD + "%");
		else
			progress.put(key, "Computing...");
		executeFileListeners(key);
	}

	public static void unregisterFileTransfer(String key)
	{
		if (key == null) return;
		progress.remove(key);
		executeFileListeners(key);
	}

	public static void sendForm(String uri,Object[][] parts, AjaxCallback<? extends  AjaxPacket> callback)
	{
		sendForm(uri,parts,callback,null);
	}
	public static native void sendForm(String uri,Object[][] parts, AjaxCallback<? extends  AjaxPacket> callback, String progressName) /*-{
	    var xhr = new XMLHttpRequest();
	    var fd = new FormData();
	
		var i = 0;
	    for (i = 0;i < parts.length;i++)
	    {
	    	fd.append(parts[i][0],parts[i][1]);
	    }
	    
	    var key = @com.eduworks.gwt.client.net.CommunicationHub::registerFileTransfer(Ljava/lang/String;)(progressName);
	    (xhr.upload || xhr).addEventListener('progress',function(evt){
  			if (evt.lengthComputable) 
  			{
     			var percentComplete = (evt.loaded / evt.total)*100;  
	    		@com.eduworks.gwt.client.net.CommunicationHub::updateFileTransfer(Ljava/lang/String;Ljava/lang/Double;)(progressName,percentComplete);
  			}
	    },false);
	    xhr.open("POST", uri, true);
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState == 4)
	    		@com.eduworks.gwt.client.net.CommunicationHub::unregisterFileTransfer(Ljava/lang/String;)(progressName);
	        if (xhr.readyState == 4) 
	            callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onResponseReceived(Ljava/lang/String;)(xhr.responseText);
	        if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status <= 206)) 
	            callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onSuccess(Ljava/lang/String;)(xhr.responseText);
	        else if (xhr.readyState == 4) 
	        	callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(xhr.responseText));
	    };
	
	    // Initiate a multipart/form-data upload
	    xhr.send(fd);
	}-*/;

	public static native void sendHTTP(String httpMethod, String url, String outgoingPost, final boolean binaryData, final AjaxCallback<? extends AjaxPacket> callback) /*-{
	    var xhr;
    	if (window.XMLHttpRequest) 
		  	xhr=new XMLHttpRequest();
		else 
			xhr=new ActiveXObject("Microsoft.XMLHTTP");
	
	    xhr.open(httpMethod, url, true);
	    if (binaryData&&$wnd.isIE===undefined)
	    	xhr.responseType = "arraybuffer";
	    if (outgoingPost!=null&&outgoingPost.charAt(0)=="{")
	    	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	    else if (outgoingPost!=null&&
	    		 outgoingPost.length>=@com.eduworks.gwt.client.net.CommunicationHub::ATOM_XML_PREAMBLE.length&&
	    		 outgoingPost.substring(0,@com.eduworks.gwt.client.net.CommunicationHub::ATOM_XML_PREAMBLE.length)==@com.eduworks.gwt.client.net.CommunicationHub::ATOM_XML_PREAMBLE) {
	    	outgoingPost = outgoingPost.substring(@com.eduworks.gwt.client.net.CommunicationHub::ATOM_XML_PREAMBLE.length);
	    	xhr.setRequestHeader("Content-Type", "application/atom+xml;type=entry;charset=UTF-8");
	    }
	    if (@com.eduworks.gwt.client.net.api.AlfrescoApi::basicAuthenicationHeader!=null)
	    	xhr.setRequestHeader("Authorization", "Basic " + @com.eduworks.gwt.client.net.api.AlfrescoApi::basicAuthenicationHeader);
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status <= 206)) 
	        	if (!binaryData||!($wnd.isIE===undefined))
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onSuccess(Ljava/lang/String;)(xhr.responseText);
	        	else 
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFileSuccess(Ljava/lang/String;Ljava/lang/Object;)(xhr.getResponseHeader('content-type'), xhr.response);
	        else if (xhr.readyState == 4) 
	        	if (!binaryData)
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(xhr.responseText));
	        	else 
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(""));
	    };
	    // Initiate a multipart/form-data upload
	    xhr.send(outgoingPost);
	}-*/;

	public static native void sendHTTPBlob(String httpMethod, String url, String outgoingPost, final boolean binaryData, final AjaxCallback<? extends AjaxPacket> callback) /*-{
	    var xhr;
    	if (window.XMLHttpRequest) 
		  	xhr=new XMLHttpRequest();
		else 
			xhr=new ActiveXObject("Microsoft.XMLHTTP");
	
		xhr.open(httpMethod, url, true);
	    if (binaryData)
	    	xhr.responseType = "blob";
	    if (outgoingPost!=null&&outgoingPost.charAt(0)=="{")
	    	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	    if ((callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::packetType == @com.eduworks.gwt.client.net.callback.AjaxCallback::ALFRESCO_TYPE) &&
	    	(@com.eduworks.gwt.client.net.api.AlfrescoApi::basicAuthenicationHeader!=null))
	    	xhr.setRequestHeader("Authorization", "Basic " + @com.eduworks.gwt.client.net.api.AlfrescoApi::basicAuthenicationHeader);
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status <= 206)) 
	        	if (!binaryData)
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onSuccess(Ljava/lang/String;)(xhr.responseText);
	        	else 
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFileSuccess(Ljava/lang/String;Ljava/lang/Object;)(xhr.getResponseHeader('content-type'), xhr.response);
	        else if (xhr.readyState == 4) 
	        	if (!binaryData)
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(xhr.responseText));
	        	else 
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(""));
	    };
	    // Initiate a multipart/form-data upload
	    xhr.send(outgoingPost);
	}-*/;

	public static native void sendForm(String uri,String filename, String destination, Blob data, String aspects, AjaxCallback<? extends  AjaxPacket> callback) /*-{
        var xhr = new XMLHttpRequest();
        var fd = new FormData();

        xhr.open("POST", uri, true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status <= 206)) 
                callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onSuccess(Ljava/lang/String;)(xhr.responseText);
            else if (xhr.readyState == 4) 
            	callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(xhr.responseText));
        };
        
        if (window.MozBlobBuilder) {
        	var bb = new window.MozBlobBuilder();
        	bb.append(data);
        	fd.append('filedata', bb.getFile(filename));
        } else
        	fd.append('filedata', data, filename);
       	fd.append('filename', filename);
        if (destination != null)
        	fd.append('destination', destination);
        fd.append('overwrite', 'false');
        if (aspects!=null)
        	fd.append('aspects', aspects);
        // Initiate a multipart/form-data upload
        xhr.send(fd);
	}-*/;

	public static native void sendFormUpdate(String uri,String filename, String nodeId, Blob data, String aspects, AjaxCallback<? extends  AjaxPacket> callback) /*-{
	    var xhr = new XMLHttpRequest();
	    var fd = new FormData();
	
	    xhr.open("POST", uri, true);
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status <= 206)) 
	            callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onSuccess(Ljava/lang/String;)(xhr.responseText);
	        else if (xhr.readyState == 4) 
	        	callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(xhr.responseText));
	    };
	    
	    if (window.MozBlobBuilder) {
	    	var bb = new window.MozBlobBuilder();
	    	bb.append(data);
	    	fd.append('filedata', bb.getFile(filename));
	    } else
	    	fd.append('filedata', data, filename);
	    fd.append('updatenoderef', nodeId);
	    if (aspects!=null)
	    	fd.append('aspects', aspects);
	    // Initiate a multipart/form-data upload
	    xhr.send(fd);
	}-*/;
	
	public static native void sendFormNoFileData(String uri,String filename, Blob data, AjaxCallback<? extends  AjaxPacket> callback) /*-{
        var xhr = new XMLHttpRequest();
        var fd = new FormData();

        xhr.open("POST", uri, true);
        xhr.onreadystatechange = function() {
	        if (xhr.readyState == 4) 
	            callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onResponseReceived(Ljava/lang/String;)(xhr.responseText);
            if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status <= 206)) 
                callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onSuccess(Ljava/lang/String;)(xhr.responseText);
            else if (xhr.readyState == 4) 
            	callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(xhr.responseText));
        };
        
        if (window.MozBlobBuilder) {
        	var bb = new window.MozBlobBuilder();
        	bb.append(data);
        	fd.append(filename, bb.getFile(filename));
        } else
        	fd.append(filename, data, filename);
        // Initiate a multipart/form-data upload
        xhr.send(fd);
	}-*/;
}
