package com.eduworks.gwt.client.net;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;

public interface MultiPartPostRequestBuilderFactoryIF
{
	public RequestBuilder get(Method method, String url);
}
