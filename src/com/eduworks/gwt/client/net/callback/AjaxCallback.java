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

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AjaxCallback<Packet> implements AsyncCallback<Packet>
{
	public static String AJAX_TYPE = "ajax";
	public static String ALFRESCO_TYPE = "alfresco";
	public static String FLR_TYPE = "flr";
	public static String ADL3DR_TYPE = "3dr";
	
	public String packetType = AJAX_TYPE; 
	
	public abstract void onSuccess(String result);

	public abstract void onResponseReceived (String result);

	public abstract void onFileSuccess(String mimeType, Object result);
}