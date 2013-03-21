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

import com.google.gwt.http.client.Request;

public class IECrossSiteRequest extends Request
{
	static final int		UNSENT	= 0;
	static final int		OPEN	= 1;
	static final int		SENT	= 2;
	static final int		DONE	= 3;
	private int				_status	= UNSENT;
	private XDomainRequest	_xhr;

	void setStatus(int status)
	{
		_status = status;
	}

	public IECrossSiteRequest(XDomainRequest xhr)
	{
		if (xhr == null)
		{
			throw new NullPointerException();
		}
		_xhr = xhr;
	}

	public void cancel()
	{
		if (isPending())
		{
			_xhr.abort();
		}
	}

	public boolean isPending()
	{
		return (_status == OPEN || _status == SENT);
	}
}