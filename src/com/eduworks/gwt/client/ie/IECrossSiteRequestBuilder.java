package com.eduworks.gwt.client.ie;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestPermissionException;
import com.google.gwt.http.client.RequestTimeoutException;

public class IECrossSiteRequestBuilder extends RequestBuilder
{
	public IECrossSiteRequestBuilder(RequestBuilder.Method httpMethod, String url)
	{
		super(httpMethod, url);
	}

	public Request send() throws RequestException
	{
		return doSend(getRequestData(), getCallback());
	}

	public Request sendRequest(String data, RequestCallback callback) throws RequestException
	{
		return doSend(data, callback);
	}

	private Request doSend(String data, final RequestCallback callback) throws RequestException
	{
		XDomainRequest xhr = XDomainRequest.create();
		try
		{
			xhr.open(getHTTPMethod(), getUrl());
		}
		catch (JavaScriptException e)
		{
			RequestPermissionException requestPermissionException = new RequestPermissionException(getUrl());
			requestPermissionException.initCause(new RequestException(e.getMessage()));
			throw requestPermissionException;
		}
		// Cannot set content type on IE
		final IECrossSiteRequest req = new IECrossSiteRequest(xhr);
		req.setStatus(IECrossSiteRequest.OPEN);
		final int timeout;
		if ((timeout = getTimeoutMillis()) > 0)
		{
			xhr.setTimeout(getTimeoutMillis());
		}
		// set handlers
		xhr.setHandler(new XDomainRequestHandler()
		{
			public void onLoad(XDomainRequest r)
			{
				req.setStatus(IECrossSiteRequest.DONE);
				callback.onResponseReceived(req, new IECrossSiteResponse(r));
			}

			public void onTimeout(XDomainRequest r)
			{
				req.setStatus(IECrossSiteRequest.DONE);
				callback.onError(req, new RequestTimeoutException(req, timeout));
			}

			public void onProgress(XDomainRequest r)
			{
			}

			public void onError(XDomainRequest r)
			{
				// Assume permission exception since XDomainRequest does not
				// return an error reason
				req.setStatus(IECrossSiteRequest.DONE);
				callback.onError(req, new RequestPermissionException(getUrl()));
			}
		});
		try
		{
			xhr.send(data);
			req.setStatus(IECrossSiteRequest.SENT);
		}
		catch (JavaScriptException e)
		{
			throw new RequestException(e.getMessage());
		}
		return req;
	}
}