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
