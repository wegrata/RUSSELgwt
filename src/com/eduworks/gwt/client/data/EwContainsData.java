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
