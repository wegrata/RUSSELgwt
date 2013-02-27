package com.eduworks.gwt.client.util;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;

public class MultiPartPostRequestBuilderImpl extends RequestBuilder implements MultiPartPostRequestBuilder
{
	public MultiPartPostRequestBuilderImpl(Method httpMethod, String url)
	{
		super(httpMethod, url);
	}

	// post body delimiters and headers:
	public static final String	BOUNDARY				= "----fritterboyfgfd4545645";
	public static final String	CONTENT_DISP_LINE		= "Content-Disposition: form-data; name=\"%1\"; filename=\"";
	public static final String	CONTENT_DISP_LINE_NO_FN		= "Content-Disposition: form-data; name=\"%1\"";
	public static final String	TEXT_PLAIN				= "Content-Type: text/html";
	public static final String	CONTENT_TYPE_EMPTY				= "Content-Type: ";
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
		body.append(value+"\r\n\r\n");
		setHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE);
		setRequestData(requestData+body.toString());
	}
	public void setMultipartFormDataWithContentType(final String key, final String filename, final String contentType,final String value)
	{
		StringBuffer body = new StringBuffer();
		String requestData = getRequestData();
		if (requestData == null) requestData = "";
		body.append("--" + BOUNDARY + "\r\n");
		if (!filename.isEmpty())
			body.append(CONTENT_DISP_LINE.replace("%1", key) + filename + "\"\r\n");
		else
			body.append(CONTENT_DISP_LINE_NO_FN.replace("%1", key)+"\r\n");
		if (contentType.isEmpty() == false)
		body.append(CONTENT_TYPE_EMPTY + contentType+"\r\n\r\n");
		body.append(value+"\r\n\r\n");
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
