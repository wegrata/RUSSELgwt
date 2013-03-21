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
package com.eduworks.gwt.client.ie;

import com.google.gwt.core.client.JavaScriptObject;

public class XDomainRequest extends JavaScriptObject {
	public static native XDomainRequest create() /*-{
        if ($wnd.XDomainRequest) {
            return new XDomainRequest();
        } else {
            $wnd.alert("Cross site requests not supported in this browser");
            return null;
        }
	}-*/;
	protected XDomainRequest() {}
	public final native void abort() /*-{
        this.abort();
	}-*/;
	public final native void clear() /*-{
        var self = this;
        $wnd.setTimeout(function() {
            self.onload = new Function();
            self.onprogress = new Function();
            self.onerror = new Function();
            self.ontimeout = new Function();
        }, 0);
	}-*/;
	public final native String getContentType() /*-{
        return this.contentType;
	}-*/;
	public final native void setTimeout(int value) /*-{
        this.timeout = value;
	}-*/;
	public final native int getTimeout() /*-{
        return this.timeout;
	}-*/;
	public final native String getResponseText() /*-{
        return this.responseText;
	}-*/;
	public final native void open(String httpMethod, String url) /*-{
        this.open(httpMethod, url);
	}-*/;
	public final native void send(String requestData) /*-{
        this.send(requestData);
	}-*/;
	public final native void setHandler(XDomainRequestHandler handler) /*-{
        var _this = this;
        this.onload = $entry(function() {
            handler.@com.eduworks.gwt.client.ie.XDomainRequestHandler::onLoad(Lcom/eduworks/gwt/client/ie/XDomainRequest;)(_this);
        });
        this.onerror = $entry(function() {
            handler.@com.eduworks.gwt.client.ie.XDomainRequestHandler::onError(Lcom/eduworks/gwt/client/ie/XDomainRequest;)(_this);
        });
        this.ontimeout = $entry(function() {
            handler.@com.eduworks.gwt.client.ie.XDomainRequestHandler::onTimeout(Lcom/eduworks/gwt/client/ie/XDomainRequest;)(_this);
        });
        this.onprogress = $entry(function() {
            handler.@com.eduworks.gwt.client.ie.XDomainRequestHandler::onProgress(Lcom/eduworks/gwt/client/ie/XDomainRequest;)(_this);
        });
	}-*/;
	}
