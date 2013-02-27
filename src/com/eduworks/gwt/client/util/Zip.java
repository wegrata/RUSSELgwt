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
