package com.eduworks.gwt.client.net;

import com.eduworks.gwt.client.ie.IECrossSiteRequestBuilder;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;

public class MultiPartPostRequestBuilderIE extends IECrossSiteRequestBuilder implements MultiPartPostRequestBuilder
{
	public MultiPartPostRequestBuilderIE(Method httpMethod, String url)
	{
		super(httpMethod, url);
	}

	// post body delimiters and headers:
	public static final String	BOUNDARY				= "----fritterboyfgfd4545645";
	public static final String	CONTENT_DISP_LINE		= "Content-Disposition: form-data; name=\"%1\"; filename=\"";
	public static final String	TEXT_PLAIN				= "Content-Type: text/html";
	// http headers:
	public static final String	CONTENT_TYPE_HEADER		= "Content-Type";
	public static final String	CONTENT_LENGTH_HEADER	= "Content-Length";
	public static final String	CONTENT_TYPE			= "multipart/form-data; boundary=" + BOUNDARY;

	// builds the body of the request, and sets appropriate headers
	public void setMultipartFormData(final String key, final String filename, final String value)
	{
		StringBuffer body = new StringBuffer();
		String requestData = getRequestData();
		if (requestData == null) requestData = "";
		body.append("--" + BOUNDARY + "\r\n");
		body.append(CONTENT_DISP_LINE.replace("%1", key) + filename + "\"\r\n");
		body.append(TEXT_PLAIN + "\r\n\r\n");
		body.append(value);
		setHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE);
		setRequestData(requestData+body.toString());
	}
	@Override
	public Request send() throws RequestException
	{
		setRequestData(getRequestData()+"\r\n--" + BOUNDARY + "--\r\n");

		return super.send();
	}

}