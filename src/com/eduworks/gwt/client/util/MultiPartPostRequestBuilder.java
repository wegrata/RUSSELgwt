package com.eduworks.gwt.client.util;

public interface MultiPartPostRequestBuilder
{
	public void setMultipartFormData(final String key, final String filename, final String value);

	void setMultipartFormDataWithContentType(String key, final String filename, String contentType, String value);
}
