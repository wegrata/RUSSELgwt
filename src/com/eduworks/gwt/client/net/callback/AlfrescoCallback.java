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

package com.eduworks.gwt.client.net.callback;

import org.vectomatic.arrays.ArrayBuffer;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.packet.AlfrescoPacket;
import com.eduworks.gwt.client.util.Browser;

public abstract class AlfrescoCallback<Packet> extends AjaxCallback<Packet>
{
	public AlfrescoCallback ()
	{
		this.packetType = AjaxCallback.ALFRESCO_TYPE;
	}
	
	public abstract void onFailure(Throwable caught);

	public abstract void onSuccess(AlfrescoPacket alfrescoPacket);

	private String processArray(String result) {
		String cleanedArray = "";
		
		String arrayGuts = result.substring(1, result.length()-1).trim();
		
		String[] terms = arrayGuts.split(",");
		for (int termsIndex=0;termsIndex<terms.length;termsIndex++) {
			terms[termsIndex] = terms[termsIndex].trim();
			if (!terms[termsIndex].startsWith("\"")&&!terms[termsIndex].endsWith("\""))
				terms[termsIndex] = "\"" + terms[termsIndex].trim() + "\"";
			else if (terms[termsIndex].startsWith("\"")&&!terms[termsIndex].endsWith("\""))
				terms[termsIndex] = terms[termsIndex].trim() + "\"";
			else if (!terms[termsIndex].startsWith("\"")&&terms[termsIndex].endsWith("\""))
					terms[termsIndex] = "\"" + terms[termsIndex].trim();
		}
		
		for (int termsIndex=0;termsIndex<terms.length;termsIndex++)
			cleanedArray += "," + terms[termsIndex];
		
		if (cleanedArray!="")
			cleanedArray = cleanedArray.substring(1);
		
		return "[" + cleanedArray + "]";
	}
	
	public void onSuccess(String result)
	{
		String pack = result.trim();
		if (pack.startsWith("{"))
			onSuccess(AlfrescoPacket.wrap(CommunicationHub.parseJSON(result)));
		else if (pack.startsWith("<"))
			onSuccess(AlfrescoPacket.wrap(CommunicationHub.xml2json(result, "")));
		else if (pack.startsWith("["))
		{
			AlfrescoPacket ap = AlfrescoPacket.makePacket();
			ap.addKeyValue("contentStream", CommunicationHub.parseJSA(processArray(result)));
			onSuccess(ap);
		}
		else
		{
			AlfrescoPacket ap = AlfrescoPacket.makePacket();
			ap.addKeyValue("contentStream", pack);
			onSuccess(ap);
		}
	}

	public void onResponseReceived(String result)
	{

	}

	public void onFileSuccess(String mimeType, Object result)
	{
		onSuccess(AlfrescoPacket.makePacket(mimeType, (ArrayBuffer) result));
	}
}