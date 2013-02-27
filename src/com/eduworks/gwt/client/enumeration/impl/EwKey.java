package com.eduworks.gwt.client.enumeration.impl;

import com.eduworks.gwt.client.enumeration.EwIsKey;
import com.eduworks.gwt.client.util.Enums;

public enum EwKey implements EwIsKey
{
	ERROR,
	ID,
	URL
	;

	private final String key = Enums.getKey(this);

	@Override
	public String getName()
	{
		return name();
	}

	@Override
	public String getKey()
	{
		return key;
	}

}
