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

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

public class IECrossSiteRpcRequestBuilder extends RpcRequestBuilder
{

	@Override
	protected RequestBuilder doCreate(java.lang.String serviceEntryPoint)
	{
		return new IECrossSiteRequestBuilder(RequestBuilder.POST, serviceEntryPoint);
	}

	/**
	 * Nothing to do. Cannot set custom headers in XDomainRequest
	 */
	@Override
	protected void doFinish(RequestBuilder rb)
	{
	}
}
