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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

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
	
	public final native JavaScriptObject getValue(String key) /*-{
		return this[key];
	}-*/;
	
	public final native String getValueString(String key) /*-{
		if (this[key]!=null)
			return this[key];
		else
			return "";
	}-*/;
	
	public final native JsArrayString getTopKeys() /*-{
		var acc = [];
		for (var key in this)
			acc.push(key);
		return acc;
	}-*/; 
	
	public final native boolean hasKey(String key) /*-{
		var acc = true;
		if (this[key]===undefined) acc = false;
		return acc;
	}-*/;
	
	public final native String toJSONString() /*-{
		var acc = "";
		for (var key in this) 
			acc += ",\"" + key + "\":" + this[key];
		if (acc!="") acc = acc.substring(1);
		
		return "{" + acc + "}";
	}-*/;
	
	public final native String toJSONArrayString() /*-{
		var acc = "";
		for (var key in this) {
			var arrayAcc = "";
			var a = this[key];
			for (var x=0;x<a.length;x++) {
				var accJSON = "";
				for (var objP in a[x]) 
					accJSON += ",\"" + objP + "\":\"" + a[x][objP] + "\"";
				if (accJSON!="") accJSON = accJSON.substring(1);
				arrayAcc += ",{" + accJSON + "}";
			}
			if (arrayAcc!="") arrayAcc = arrayAcc.substring(1);
			acc += ",\"" + key + "\":[" + arrayAcc + "]";
		}
		if (acc!="") acc = acc.substring(1);
		
		return "{" + acc + "}";
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
		else
			this[section][arrayIndex] = obj;
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
	
	public final native String toJSONWrappedString() /*-{
		var acc = "";
		for (var key in this) 
			acc += ",\"" + key + "\":\"" + this[key] + "\"";
		if (acc!="") acc = acc.substring(1);
		
		return "{" + acc + "}";
	}-*/;

	public native final String getPropertyValue(String attribute, String propertyID) /*-{
		var records = [this];
		while (records.length>0) {
			var record = records.pop();
			for (var curNode in record)
				if (record.hasOwnProperty(curNode)) {
					if (curNode==attribute && record[curNode]==propertyID)
						return ((record["cmis:value"]===undefined||record["cmis:value"]==null)? "" : record["cmis:value"]);
					if (typeof record[curNode]=="object")
						records.push(record[curNode]);
				}
		}
		return "";
	}-*/;
	
}