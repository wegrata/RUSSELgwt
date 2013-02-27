package com.eduworks.gwt.client.enumeration.impl;

import com.eduworks.gwt.client.enumeration.EwIsEnum;

public enum EwUrl implements EwIsEnum
{
	CALLBACK("callback"),
	RANDOM_STRING("randomString")
	;

	public final String value;

	private EwUrl(String value)
	{
		this.value = value;
	}

	@Override
	public String getName()
	{
		return name();
	}

	@Override
	public String toString()
	{
		return value;
	}
}
