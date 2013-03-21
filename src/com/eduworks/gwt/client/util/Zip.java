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

package com.eduworks.gwt.client.util;

import org.vectomatic.file.Blob;
import org.vectomatic.file.File;

import com.eduworks.gwt.client.net.callback.AlfrescoCallback;
import com.eduworks.gwt.client.net.packet.AlfrescoPacket;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Zip
{	
	public static final native void checkSCORMandGrabEntries(File zipBlob, AlfrescoCallback<AlfrescoPacket> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		// use a BlobReader to read the zip from a Blob object
		zip.createReader(new zip.BlobReader(zipBlob), 
						 function(zipReader) {
							// get all entries from the zip
						 	zipReader.getEntries(function(entries) {
						 							var isSCORM = false;
						 							for (var x=0;x<entries.length;x++)
						 								if (entries[x].filename.toLowerCase()=="imsmanifest.xml")
						 									isSCORM = true;
						 									
						 							if (isSCORM) {
						 								var ap = {};
						 								ap["zipEntries"] = [];
						 								for (var i=0;i<entries.length;i++) {
						 									ap["zipEntries"].push({});
						 									ap["zipEntries"][ap["zipEntries"].length-1]["filename"] = entries[i].filename;
						 									ap["zipEntries"][ap["zipEntries"].length-1]["data"] = entries[i];
						 								}
						 								callback.@com.eduworks.gwt.client.net.callback.AlfrescoCallback::onSuccess(Lcom/eduworks/gwt/client/net/packet/AlfrescoPacket;)(ap);
						 							}
						  						 });
						},
						function(error) {
						  alert(error);
						});
	}-*/;
	
	public static final native void grabEntries(File zipBlob, AlfrescoCallback<AlfrescoPacket> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		// use a BlobReader to read the zip from a Blob object
		zip.createReader(new zip.BlobReader(zipBlob), 
						 function(zipReader) {
							// get all entries from the zip
						 	zipReader.getEntries(function(entries) {
					 								var ap = {};
					 								ap["zipEntries"] = [];
					 								for (var i=0;i<entries.length;i++) {
					 									ap["zipEntries"].push({});
					 									ap["zipEntries"][ap["zipEntries"].length-1]["filename"] = entries[i].filename;
					 									ap["zipEntries"][ap["zipEntries"].length-1]["data"] = entries[i];
					 								}
					 								callback.@com.eduworks.gwt.client.net.callback.AlfrescoCallback::onSuccess(Lcom/eduworks/gwt/client/net/packet/AlfrescoPacket;)(ap);
						  						 });
						},
						function(error) {
						  alert(error);
						});
	}-*/;

	public static final native void grabEntries(Blob zipBlob, AlfrescoCallback<AlfrescoPacket> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		// use a BlobReader to read the zip from a Blob object
		zip.createReader(new zip.BlobReader(zipBlob), 
						 function(zipReader) {
							// get all entries from the zip
						 	zipReader.getEntries(function(entries) {
					 								var ap = {};
					 								ap["zipEntries"] = [];
					 								for (var i=0;i<entries.length;i++) {
					 									ap["zipEntries"].push({});
					 									ap["zipEntries"][ap["zipEntries"].length-1]["filename"] = entries[i].filename;
					 									ap["zipEntries"][ap["zipEntries"].length-1]["data"] = entries[i];
					 								}
					 								callback.@com.eduworks.gwt.client.net.callback.AlfrescoCallback::onSuccess(Lcom/eduworks/gwt/client/net/packet/AlfrescoPacket;)(ap);
						  						 });
						},
						function(error) {
						  alert(error);
						});
	}-*/;
	
	public static final native void inflateEntry(JavaScriptObject entry, boolean binary, AlfrescoCallback<AlfrescoPacket> callback) /*-{
		var readFunc;
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		 
		if (binary)
			readFunc = new zip.BlobWriter();
		else
			readFunc = new zip.TextWriter();
			
		entry.data.getData(readFunc, function (data) {
										var ap = {};
				    					ap["zipEntryFilename"] = entry.filename;
				    					ap["zipEntryData"] = data;
				    					callback.@com.eduworks.gwt.client.net.callback.AlfrescoCallback::onSuccess(Lcom/eduworks/gwt/client/net/packet/AlfrescoPacket;)(ap);
								    	if (readFunc.close!=null)
									    	readFunc.close(function () {});
									},
									function (currentProgress, totalProgress) {
									
									});
	}-*/;
	
	public static final native JavaScriptObject getZipFileWriter(AlfrescoCallback<AlfrescoPacket> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		// use a BlobWriter to store the zip into a Blob object
		zip.createWriter(new zip.BlobWriter(), 
						 function(writer) {
							var ap = {};
							ap["zipWriter"] = writer;
							callback.@com.eduworks.gwt.client.net.callback.AlfrescoCallback::onSuccess(Lcom/eduworks/gwt/client/net/packet/AlfrescoPacket;)(ap);
						 }, 
						 function(error) {
  							// onerror callback
						 });
	}-*/;
	
	public static final native void addFileToZipBlob(JavaScriptObject zipWriter, String filename, Blob filedata, AlfrescoCallback<AlfrescoPacket> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		zipWriter.add(filename, 
				      new zip.BlobReader(filedata), 
				      function () {
				      	var ap = {};
				      	ap["zipWriter"] = zipWriter;
				      	callback.@com.eduworks.gwt.client.net.callback.AlfrescoCallback::onSuccess(Lcom/eduworks/gwt/client/net/packet/AlfrescoPacket;)(ap);
				      },
				      function() {
				      	//progress
				      }); 
	}-*/;
	
	public static final native void getZipBlobLocalURL(JavaScriptObject zipWriter, AlfrescoCallback<AlfrescoPacket> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		var createObjURL = window.webkitURL.createObjectURL || window.URL.createObjectURL;
		
		zipWriter.close(function(blob) {
							var ap = {};
							ap["zipURL"] = createObjURL(blob);
							callback.@com.eduworks.gwt.client.net.callback.AlfrescoCallback::onSuccess(Lcom/eduworks/gwt/client/net/packet/AlfrescoPacket;)(ap);
						});
	}-*/;
	
	public static final native void getZipBlob(JavaScriptObject zipWriter, AlfrescoCallback<AlfrescoPacket> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		zipWriter.close(function(blob) {
							var ap = {};
							ap["zipBlob"] = blob;
							callback.@com.eduworks.gwt.client.net.callback.AlfrescoCallback::onSuccess(Lcom/eduworks/gwt/client/net/packet/AlfrescoPacket;)(ap);
						});
	}-*/;
	
	public static final native JsArray<JavaScriptObject> getZipObject(Blob b) /*-{
		var acc = null; 
		var unzip = new $wnd.JSUnzip(b);
		if (unzip.isZipFile()) {
			unzip.readEntries();
			acc = unzip.entries;
		}
		return acc;
	}-*/;
	
//	public static final native Blob cutFile(File f) /*-{
//		var acc;
//		if (f.mozSlice)
//			acc = f.mozSlice(0, f.size);
//		else if (f.webkitSlice)
//			acc = f.webkitSlice(0, f.size);
//		else if (f.slice)
//			acc = f.slice(0, f.size);
//		return acc;
//	}-*/;
	
	public static final native String getZipEntryFilename(JavaScriptObject obj) /*-{
		var acc = ""
		if (obj!=null&&obj.fileName!=null)
			if (obj.fileName.indexOf("/")!=-1) acc = obj.fileName.substring(obj.fileName.lastIndexOf("/")+1);
			else acc = obj.fileName;
		return acc;
	}-*/;
	
//	public static final native File inflateZipEntry(JavaScriptObject obj) /*-{
//		var acc = null;
//		if (obj!=null&&obj.data!=null)
//			acc = obj);
//		return acc;
//	}-*/;
	
//	public static final native Blob getZipData(JavaScriptObject obj) /*-{
//		var acc = null;
//		var bb = new (window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.OBlobBuilder || window.msBlobBuilder);
//		
//		if (obj.compressionMethod === 0) {
//			bb.append(obj.data);
//			acc = bb.getBlob();
//		} else if (obj.compressionMethod === 8) {
//			bb.append($wnd.JSInflate.inflate(obj.data));
//			acc = bb.getBlob();
//		}
//		return acc;
//	}-*/;
}
