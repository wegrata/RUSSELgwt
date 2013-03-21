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

import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.Response;

public class IECrossSiteResponse extends Response
{
	private XDomainRequest	_xhr;

	public static class IEHeader extends Header
	{
		private String	_name;
		private String	_value;

		public IEHeader(String name, String val)
		{
			_name = name;
			_value = val;
		}

		public String getName()
		{
			return _name;
		}

		public String getValue()
		{
			return _value;
		}
	}

	public IECrossSiteResponse(XDomainRequest xhr)
	{
		_xhr = xhr;
	}

	public String getHeader(String header)
	{
		return header.equals("Content-Type") ? _xhr.getContentType() : null;
	}

	public Header[] getHeaders()
	{
		if (_xhr.getContentType() != null)
		{
			Header ret[] = new Header[1];
			ret[0] = new IEHeader("Content-Type", _xhr.getContentType());
			return ret;
		}
		else
		{
			return null;
		}
	}

	public String getHeadersAsString()
	{
		return (_xhr.getContentType() == null) ? "" : ("Content-Type : " + _xhr.getContentType());
	}

	public int getStatusCode()
	{
		return (_xhr != null) ? Response.SC_OK : Response.SC_BAD_REQUEST;
	}

	public String getStatusText()
	{
		return "OK";
	}

	public String getText()
	{
		return _xhr.getResponseText();
	}
}
