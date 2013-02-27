package com.eduworks.gwt.client.net;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;

public class EwRequestBuilderFactory implements MultiPartPostRequestBuilderFactoryIF
{
	public RequestBuilder get(Method method, String url)
	{
		return new EwRequestBuilder(method, url);
	}
}
