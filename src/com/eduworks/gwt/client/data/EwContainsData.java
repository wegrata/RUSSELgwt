package com.eduworks.gwt.client.data;

import java.util.Map;

import com.eduworks.gwt.client.EwIsAction;
import com.eduworks.gwt.client.data.impl.EwDataManager;
import com.eduworks.gwt.client.enumeration.EwIsKey;
import com.eduworks.gwt.client.exception.EwException;
import com.eduworks.gwt.client.ui.EwIsWidget;


/**
 * Marks all classes that contain data that needs to be managed. This must be an
 * interface or its children will not be able to extend actual GWT widgets.
 * @author dharvey
 * @since 10/2011
 */
public interface EwContainsData extends EwHasDataType, EwIsWidget
{
	public EwDataManager getDataManager();

	public void setDataManager(EwDataManager manager);

	public void clearTrackedData();

	public <T extends EwIsDataType> void clearTrackedData(T items);

	public <T extends EwIsDataType> Map<EwIsKey, String> track(T items, Object ... contextArray);

	public <T extends EwIsKey> void validate(EwIsAction action, Map<T, String> contextMap) throws EwException;
}
