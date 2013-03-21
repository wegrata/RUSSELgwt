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
package com.eduworks.gwt.client.net;

import com.eduworks.gwt.client.util.Strings;
import com.google.gwt.http.client.RequestBuilder;

public class EwRequestBuilder extends RequestBuilder
{
	/* STATIC MEMBERS */

	private static final String COMMENT	= "--";
	private static final String NEWLINE	= "\r\n";
	private static final String BOUNDARY	= "----fritterboyfgfd4545645";

	// HTTP headers
	public static final String CONTENT_DISP	= "Content-Disposition";
	public static final String CONTENT_LEN	= "Content-Length";
	public static final String CONTENT_TYPE	= "Content-Type";

	public static final String FORM_FORMAT	= "form-data; name=\"$(0)\"; filename=\"$(0)\"";
	public static final String JSON_VALUE	= "application/json";
	public static final String MULTI_VALUE	= "multipart/form-data; boundary=" + BOUNDARY;
	public static final String TEXT_VALUE	= "text/html";


	/* INSTANCE MEMBERS */

	public EwRequestBuilder(Method httpMethod, String url)
	{
		super(httpMethod, url);
	}

	public EwRequestBuilder(Method httpMethod, String url, int timeout)
	{
		this(httpMethod, url);
		setTimeoutMillis(timeout);
	}

	public EwRequestBuilder(Method httpMethod, String url, String contentType)
	{
		this(httpMethod, url);
		setHeader(CONTENT_TYPE, contentType);
	}

	/**
	 * Appends data to the body of the request and sets the multi-part header.
	 * @param file the name of the file for the data
	 * @param data request data to be appended
	 * @param done true if this is the last one to append
	 */
	public void appendMultipartFormData(final String file, final String data, final boolean done)
	{
		final StringBuilder body = new StringBuilder();
		final String requestData = getRequestData();

		setHeader(CONTENT_TYPE, MULTI_VALUE);

		if (requestData != null) body.append(requestData);

		body.append(COMMENT).append(BOUNDARY).append(NEWLINE);

		body.append("Content-Disposition: ").append(Strings.format(FORM_FORMAT, file)).append(NEWLINE);
		body.append("Content-Type: text/html").append(NEWLINE).append(NEWLINE);

		body.append(data).append(NEWLINE);

		if (done)
			body.append(COMMENT).append(BOUNDARY).append(COMMENT).append(NEWLINE);

		setRequestData(body.toString());
	}
}
