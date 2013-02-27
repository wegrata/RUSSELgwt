package com.eduworks.gwt.client.util;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;

public class MultiPartPostRequestBuilderFactoryIE implements MultiPartPostRequestBuilderFactoryIF
{
	public MultiPartPostRequestBuilder get(Method method, String url)
	{
		return new MultiPartPostRequestBuilderIE(method, url);
	}
}