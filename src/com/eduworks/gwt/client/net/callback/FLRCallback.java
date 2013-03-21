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
import com.eduworks.gwt.client.net.packet.FLRPacket;

public abstract class FLRCallback<Packet> extends AjaxCallback<Packet>
{
	public FLRCallback ()
	{
		this.packetType = AjaxCallback.FLR_TYPE;
	}
	
	public abstract void onFailure(Throwable caught);

	public abstract void onSuccess(FLRPacket flrPacket);

	public void onSuccess(String result)
	{
		String pack = result.trim();
		if (pack.startsWith("{"))
			onSuccess(FLRPacket.wrap(CommunicationHub.parseJSON(result)));
		else if (pack.startsWith("<"))
			onSuccess(FLRPacket.wrap(CommunicationHub.xml2json(result, "")));
		else if (pack.startsWith("["))
		{
			FLRPacket fp = FLRPacket.makePacket();
			fp.addKeyValue(
					"contentStream",
					CommunicationHub.parseJSA("[\""
							+ result.substring(1, result.length() - 2).replaceAll("w+,w+", "\",\"").trim() + "\"]"));
			onSuccess(fp);
		}
		else
		{
			FLRPacket fp = FLRPacket.makePacket();
			fp.addKeyValue("contentStream", pack);
			onSuccess(fp);
		}
	}

	public void onResponseReceived(String result)
	{

	}

	public void onFileSuccess(String mimeType, Object result)
	{
		
	}
}