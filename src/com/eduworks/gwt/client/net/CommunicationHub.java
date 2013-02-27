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

package com.eduworks.gwt.client.net;

import java.net.HttpURLConnection;
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
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public class CommunicationHub implements RemoteService  {
	/* STATIC METHODS */
	
	public static String ROOT_URL	= "http://sphinx.eduworks.com/"; 	// Staging Servers
//	public static final String ROOT_URL	= "http://russel.eduworks.com/";  // Production Server
	public static String BASE_URL	= ROOT_URL+"alfresco/";
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
		var e = (x!=null)? x.replace(/[\r\n]/g," "): "{}";
		return eval('(' + ((e==""||e==null)? '{}' : e) + ')'); 
	}-*/;

	public static native JSONValue xml2json(String xml, String tab) /*-{
	    var dom = $wnd.parseXml(xml);
	    var jsonStr = $wnd.xml2json(dom, tab);
	    return@com.google.gwt.json.client.JSONParser::evaluate(Ljava/lang/String;Z)(jsonStr,false);
	}-*/;
	
	public final native static JsArray<AlfrescoPacket> parseJSA(String x) /*-{ 
		var e = (x!=null)? x.replace(/[\r\n]/g," "): "[]";
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
	    if (binaryData)
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
