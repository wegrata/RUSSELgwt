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

import java.util.Random;

import com.eduworks.gwt.client.enumeration.impl.EwKey;
import com.eduworks.gwt.client.enumeration.impl.EwRedmine;
import com.eduworks.gwt.client.enumeration.impl.EwUrl;
import com.eduworks.gwt.client.exception.EwException;
import com.eduworks.gwt.client.net.callback.AlfrescoCallback;
import com.eduworks.gwt.client.net.packet.AlfrescoPacket;
import com.eduworks.gwt.client.util.Arrays;
import com.eduworks.gwt.client.util.Classes;
import com.eduworks.gwt.client.util.Json;
import com.eduworks.gwt.client.util.Logger;
import com.eduworks.gwt.client.util.Strings;
import com.eduworks.gwt.client.util.Uri;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.jsonp.client.TimeoutException;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class EwCommunication
{
	public static String	callbackParam	= EwUrl.CALLBACK.value;
	public static Random	random			= new Random();

	/**
	 * Concatenates the service name and parameters to the provided url, and
	 * adds a randomized parameter to prevent caching of web content.
	 * 
	 * @return an internal url that will return non-cached web content
	 */
	public static String buildUrlInternal(final String serviceUrl, final String serviceName, final String... parameters)
	{
		return Uri.addRandomParamTo(
				Uri.buildUrl(Uri.appendPathTo(new StringBuilder(serviceUrl), serviceName), parameters)).toString();
	}

	/**
	 * Appends parameters to the provided url, including an optional randomized
	 * value to prevent caching.
	 */
	public static String buildUrlInternal(final String url, final boolean randomString, final String... parameters)
	{
		final StringBuilder urlBuilder = Uri.buildUrl(new StringBuilder(url), parameters);

		if (randomString)
			Uri.addRandomParamTo(urlBuilder);

		return urlBuilder.toString();
	}

	/* HTTP GETS */

	public static void httpGetArray(final AsyncCallback<JSONArray> callback, final String decoratedUrl)
	{
		httpGetArray(callback, decoratedUrl, null, -1);
	}

	public static void httpGetArray(final AsyncCallback<JSONArray> callback, final String serviceUrl,
			final String serviceName, final int timeout, final String... parameters)
	{
		httpGetArray(callback, buildUrlInternal(serviceUrl, serviceName, parameters), serviceName, timeout);
	}

	public static void httpGetArray(final AsyncCallback<JSONArray> callback, final String decoratedUrl,
			final String action, final int timeout)
	{
		httpGet(wrapArrayCallback(callback, action), decoratedUrl, timeout);
	}

	public static void httpGetObject(final AsyncCallback<JSONObject> callback, final String decoratedUrl)
	{
		httpGetObject(callback, decoratedUrl, null, -1);
	}

	public static void httpGetObject(final AsyncCallback<JSONObject> callback, final String serviceUrl,
			final String serviceName, final int timeout, final String... parameters)
	{
		httpGetObject(callback, buildUrlInternal(serviceUrl, serviceName, parameters), serviceName, timeout);
	}

	public static void httpGetObject(final AsyncCallback<JSONObject> callback, final String decoratedUrl,
			final String action, final int timeout)
	{
		httpGet(wrapObjectCallback(callback, action), decoratedUrl, timeout);
	}

	public static void httpGet(AsyncCallback<JavaScriptObject> callback, final String decoratedUrl, final int timeout)
	{
		if (callback == null)
			callback = getDefaultCallback();

		httpGet(getBuilderJsonp(timeout), callback, decoratedUrl);
	}

	public static void httpGetJson(AsyncCallback<String> callback, final String decoratedUrl, final int timeout)
	{
		if (callback == null)
			callback = getDefaultCallback();

		httpRequestJson(getBuilderJson(decoratedUrl, timeout), callback, decoratedUrl);
	}

	/**
	 * Allows a child of {@link EwCommunication} to set headers, etc before
	 * sending a jsonp request.
	 */
	protected static void httpGet(final JsonpRequestBuilder rb, final AsyncCallback<JavaScriptObject> cb,
			String decoratedUrl)
	{
		if (rb == null || cb == null)
		{
			Logger.logError("Null JSONP RequestBuilder or AsyncCallback");
			return;
		}

		try
		{
			rb.requestObject(decoratedUrl, cb);
		}
		catch (Exception e)
		{
			Logger.logError("HTTP GET JSONP Exception: $(0)", e);
			cb.onFailure(e);
		}
	}

	/**
	 * Allows a child of {@link EwCommunication} to set method, headers, etc
	 * before sending a request.
	 */
	protected static void httpRequestJson(final RequestBuilder rb, final AsyncCallback<String> cb, String decoratedUrl)
	{
		if (rb == null || cb == null)
		{
			Logger.logError("Null RequestBuilder or AsyncCallback");
			return;
		}

		try
		{
			rb.sendRequest(decoratedUrl, new RequestCallback()
			{
				@Override
				public void onResponseReceived(Request request, Response response)
				{
					cb.onSuccess(response.getText());
				}

				@Override
				public void onError(Request request, Throwable exception)
				{
					cb.onFailure(exception);
				}
			});
		}
		catch (Exception e)
		{
			Logger.logError("HTTP GET JSON Exception: $(0)", e);
			cb.onFailure(e);
		}
	}

	/* HTTP POSTS */

	public static void httpPostArray(final AsyncCallback<JSONArray> callback, final int timeout, String[][] data,
			String dataUrl, final String serviceUrl, final String serviceName, String... parameters)
	{
		httpGet(wrapArrayCallback(callback, serviceName), serviceUrl, -1);
		if (Arrays.isEmpty(data))
		{
			// No data means page content is not available: pass url of current
			// page to LEVR instead.
			httpGetArray(callback, buildPostUrl(serviceUrl, serviceName, parameters, EwKey.URL, dataUrl), serviceName,
					timeout);
		}
		else
		{
			final String url = buildPostUrl(serviceUrl, serviceName, parameters, "sec", new Long(random.nextLong()));
			final EwRequestBuilder rb = new EwRequestBuilder(RequestBuilder.POST, url, timeout);

			httpPost(rb, wrapPostCallback(callback, url, serviceName, JSONArray.class), data);
		}
	}

	public static void httpPostObject(final AsyncCallback<JSONObject> callback, final String decoratedUrl,
			final String contentType, final String data, final String action)
	{
		final EwRequestBuilder rb = new EwRequestBuilder(RequestBuilder.POST, decoratedUrl, contentType);

		httpPost(rb, wrapPostCallback(callback, decoratedUrl, action, JSONObject.class), data);
	}

	public static void httpPostObject(final AsyncCallback<JSONObject> callback, final int timeout, String[][] data,
			String dataUrl, final String serviceUrl, final String serviceName, String... parameters)
	{
		if (Arrays.isEmpty(data))
		{
			// No data means page content is not available: pass url of current
			// page to LEVR instead.
			httpGetObject(callback, buildPostUrl(serviceUrl, serviceName, parameters, EwKey.URL, dataUrl), serviceName,
					timeout);
		}
		else
		{
			final String url = buildPostUrl(serviceUrl, serviceName, parameters, "sec", new Long(random.nextLong()));
			final EwRequestBuilder rb = new EwRequestBuilder(RequestBuilder.POST, url, timeout);

			httpPost(rb, wrapPostCallback(callback, url, serviceName, JSONObject.class), data);
		}
	}

	public static void httpPostObjectNoFile(final AsyncCallback<JSONObject> callback, final int timeout, String data,
			String mimetype, final String serviceUrl, final String serviceName, String... parameters)
	{

		final String url = buildPostUrl(serviceUrl, serviceName, parameters);
		final EwRequestBuilder rb = new EwRequestBuilder(RequestBuilder.POST, url, mimetype);
		try
		{
			rb.sendRequest(data, wrapPostCallback(callback, "", JSONObject.class));
		}
		catch (RequestException e)
		{
			callback.onFailure(e);
		}
		// httpPost(rb, wrapPostCallback(callback, url, serviceName,
		// JSONObject.class), data);
	}

	private static String buildPostUrl(final String serviceUrl, final String serviceName, String[] parameters,
			Object... addParams)
	{
		final String[] params = new String[parameters.length + addParams.length];

		for (int i = 0, j = addParams.length; i < parameters.length; i++, j++)
			params[j] = parameters[i];

		for (int i = 0; i < addParams.length; i++)
			params[i] = addParams[i].toString();

		return buildUrlInternal(serviceUrl, serviceName, params);
	}

	protected static void httpPost(final EwRequestBuilder rb, final RequestCallback callback, String data)
	{
		try
		{
			rb.sendRequest(data, callback);
		}
		catch (RequestException e)
		{
			Logger.logError("HTTP Post Exception: $(0)", e);
			callback.onError(null, e);
		}
	}

	protected static void httpPost(final EwRequestBuilder rb, final RequestCallback callback, String[][] data)
	{
		final int len = data.length;

		for (int i = 0; i < len; i++)
			rb.appendMultipartFormData(data[i][0], data[i][1], ((i + 1) == len));

		try
		{
			rb.sendRequest(rb.getRequestData(), callback);
		}
		catch (RequestException e)
		{
			Logger.logError("HTTP Post Exception: $(0)", e);
			callback.onError(null, e);
		}
	}

	/* CALLBACKS */

	/**
	 * Submits an issue to Redmine, given a Redmine user key and a project name.
	 * 
	 * @param userKey
	 *            the API access key: a hash configured per Redmine user account
	 * @param projectKey
	 *            the name of the project in Redmine
	 * @param subject
	 *            the subject of the submitted issue
	 * @param category
	 *            the category of the issue: categories are set up per project
	 * @param description
	 *            the text content of the submitted issue
	 */
	public static void submitFeedback(String userKey, String projectKey, String subject, String category,
			String description, final AsyncCallback<JSONObject> after)
	{
		final JSONObject data = new JSONObject();
		final JSONObject issue = new JSONObject();

		for (EwRedmine value : EwRedmine.values())
			switch (value)
			{
			case CUSTOM:
				continue;

			case CAT_ISSUE:
			case CAT_SUGGEST:
			case CAT_OTHER:
				Json.put(issue, value.key, category);
				break;

			case DESCRIPTION:
				Json.put(issue, value.key, description);
				break;

			case PROJECT:
				Json.put(issue, value.key, projectKey);
				break;

			case SUBJECT:
				Json.put(issue, value.key, subject);
				break;

			default:
				Json.put(issue, value.key, value.value);
				break;
			}

		data.put("issue", issue);

		final StringBuilder urlBuilder = Uri.buildUrl(new StringBuilder(EwRedmine.URL), EwRedmine.KEY, userKey,
				EwRedmine.PROJECT.key, projectKey);

		httpPostObject(after, urlBuilder.toString(), EwRequestBuilder.JSON_VALUE, data.toString(), "submitFeedback");
	}

	/**
	 * Wraps "after" in a default {@link JSONArray} callback, using "action" in
	 * the error handling and logging.
	 */
	public static AsyncCallback<JavaScriptObject> wrapArrayCallback(final AsyncCallback<JSONArray> callback,
			final String action)
	{
		return wrapCallback(callback, action, JSONArray.class);
	}

	/**
	 * Wraps "after" in a default {@link JSONObject} callback, using "action" in
	 * the error handling and logging.
	 */
	public static AsyncCallback<JavaScriptObject> wrapObjectCallback(final AsyncCallback<JSONObject> callback,
			final String action)
	{
		return wrapCallback(callback, action, JSONObject.class);
	}

	/**
	 * @return a callback that wraps onSuccess() and onFailure() from "after" if
	 *         it is not null, and incorporates "action" into failure message if
	 *         it is provided.
	 */
	public static <T extends JSONValue> AsyncCallback<JavaScriptObject> wrapCallback(final AsyncCallback<T> callback,
			final String action, final Class<T> jsonClass)
	{
		return new AsyncCallback<JavaScriptObject>()
		{
			@Override
			public void onSuccess(JavaScriptObject result)
			{
				final T wrapped = Classes.getJsonInstance(jsonClass, result);

				final boolean noError = (!Json.contains(wrapped.isObject(), EwKey.ERROR));

				if (callback != null)
					if (noError)
						callback.onSuccess(wrapped);
					else
						callback.onFailure(new EwException(Json.getString(wrapped, EwKey.ERROR)));
			}

			@Override
			public void onFailure(Throwable caught)
			{
				if (caught instanceof TimeoutException)
					caught = new EwException("Internal Server Error: $(0)$(1)", Classes.getSimpleName(caught),
							(action == null) ? Strings.EMPTY : Strings.format("<br/>Requested: $(0)", action));
				else
					Logger.logError("Request '$(0)' failed: $(1)", action, caught);

				if (callback != null)
					callback.onFailure(caught);
			}
		};
	}

	/**
	 * @return a callback that wraps onSuccess() and onFailure() from "after" if
	 *         it is not null, and incorporates "action" into failure message if
	 *         it is provided.
	 */
	public static <T extends JSONValue> RequestCallback wrapPostCallback(final AsyncCallback<T> callback,
			final String url, final String action, final Class<T> jsonClass)
	{
		return wrapPostCallback(callback, url, jsonClass);
	}

	public static AlfrescoCallback<AlfrescoPacket> wrapPostCallbackObject(final String url,
			final AsyncCallback<JSONObject> callback)
	{
		return new AlfrescoCallback<AlfrescoPacket>()
		{
			@Override
			public void onSuccess(AlfrescoPacket alfrescoPacket)
			{
			}

			@Override
			public void onResponseReceived(String data)
			{
				httpGetObject(callback, url);
			}

			@Override
			public void onFailure(Throwable caught)
			{

			}
		};
	}

	public static AlfrescoCallback<AlfrescoPacket> wrapPostCallbackArray(final String url,
			final AsyncCallback<JSONArray> callback)
	{
		return new AlfrescoCallback<AlfrescoPacket>()
		{
			@Override
			public void onSuccess(AlfrescoPacket alfrescoPacket)
			{
			}

			@Override
			public void onResponseReceived(String data)
			{
				httpGetArray(callback, url);
			}

			@Override
			public void onFailure(Throwable caught)
			{

			}
		};
	}

	public static <T extends JSONValue> RequestCallback wrapPostCallback(final AsyncCallback<T> callback,
			final String url, final Class<T> jsonClass)
	{
		return new RequestCallback()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void onResponseReceived(Request request, Response response)
			{
				if (Strings.isEmpty(url) && response.getStatusCode() == 200)
					callback.onSuccess(Classes.getJsonInstance(jsonClass));

				else if (jsonClass == JSONArray.class)
					httpGetArray((AsyncCallback<JSONArray>) callback, url);

				else if (jsonClass == JSONObject.class)
					httpGetObject((AsyncCallback<JSONObject>) callback, url);
			}

			@Override
			public void onError(Request request, Throwable caught)
			{
				Logger.logError("HTTP Post Failure: $(0)", caught);
				callback.onFailure(caught);
			}
		};
	}

	/* PROTECTED METHODS */

	protected static RequestBuilder getBuilderJson(String url, int timeout)
	{
		return getBuilderJson(RequestBuilder.GET, url, timeout);
	}

	protected static RequestBuilder getBuilderJson(Method httpMethod, String url, int timeout)
	{
		final RequestBuilder rb = new RequestBuilder(httpMethod, url);

		if (timeout > 0)
			rb.setTimeoutMillis(timeout);
		else
			rb.setTimeoutMillis(60000);

		return rb;
	}

	protected static JsonpRequestBuilder getBuilderJsonp(int timeout)
	{
		final JsonpRequestBuilder rb = new JsonpRequestBuilder();

		rb.setCallbackParam(callbackParam);

		if (timeout > 0)
			rb.setTimeout(timeout);
		else
			rb.setTimeout(60000);

		return rb;
	}

	protected static <T> AsyncCallback<T> getDefaultCallback()
	{
		return new AsyncCallback<T>()
		{
			@Override
			public void onSuccess(T result)
			{
			}

			@Override
			public void onFailure(Throwable caught)
			{
				Logger.logError("Internal Server Error: $(0)<br/>$(1)", Classes.getSimpleName(caught), caught);
			}
		};
	}

	public static AsyncCallback<JavaScriptObject> nullCallback()
	{
		return new AsyncCallback<JavaScriptObject>()
		{

			@Override
			public void onSuccess(JavaScriptObject result)
			{

			}

			@Override
			public void onFailure(Throwable caught)
			{

			}
		};
	}

}