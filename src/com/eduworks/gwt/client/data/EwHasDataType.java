package com.eduworks.gwt.client.data;

import com.eduworks.gwt.client.data.impl.EwDataTracker;


/**
 * A class to identify database objects to be {@link EwDataTracker tracked}.
 * @author dharvey
 * @since 05/2012
 */
public interface EwHasDataType
{
	public EwIsDataType getDataType();
}
