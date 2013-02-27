package com.eduworks.gwt.client.data.impl;

import java.util.Map;

import com.eduworks.gwt.client.EwIsAction;
import com.eduworks.gwt.client.data.EwContainsData;
import com.eduworks.gwt.client.data.EwIsDataType;
import com.eduworks.gwt.client.enumeration.EwIsKey;
import com.eduworks.gwt.client.exception.EwException;
import com.eduworks.gwt.client.util.Classes;
import com.eduworks.gwt.client.util.Collections;
import com.eduworks.gwt.client.util.Logger;

/**
 * Defines all classes that manage data contained by {@link EwContainsData}'s.
 * @author dharvey
 * @since 10/2011
 */
public abstract class EwDataManager
{
	@SuppressWarnings("unchecked")
	public static <T extends EwIsDataType> Map<EwIsKey, String> track(EwDataManager manager, T dataType, Object ... contextArray)
	{
		if (manager == null)
			return Collections.getMap();

		final Map<EwIsKey, String> contextMap;

		switch (contextArray.length)
		{
			case 0:
				Logger.logWarning(
						"Empty $(0) tracking context for $(1) items",
						Classes.getSimpleName(manager.getContainer()), dataType
					);
				contextMap = Collections.getMap();
				break;

			case 1:
				if (contextArray[0] instanceof Map<?,?>)
				{
					contextMap = (Map<EwIsKey, String>) contextArray[0];
					break;
				}

			default:
				contextMap = manager.getContextFor(dataType, contextArray);
		}

		manager.getTracker().track(dataType, contextMap);

		return contextMap;
	}


	/* ABSTRACT METHODS */

	/** Retrieves the UI component that contains the data. */
	public abstract EwContainsData getContainer();

	/** Retrieves the object responsible for tracking data retrieved from the database. */
	public abstract EwDataTracker getTracker();

	/**
	 * Derive action-specific context from the value passed in. If the action is one requiring context,
	 * but the value is invalid, a {@link EwException} is thrown. If the action does not require
	 * context, then null is returned.
	 */
	public abstract Map<EwIsKey, String> getContextFor(EwIsAction action, Object ... contextArray);

	/**
	 * Derive item-specific context from the value passed in. If the item is one requiring context,
	 * but the value is invalid, a {@link EwException} is thrown. If the item does not require
	 * context, then null is returned.
	 */
	public abstract Map<EwIsKey, String> getContextFor(EwIsDataType item, Object ... contextArray);

	/** Validates a form before submission given the required context (as defined by {@link #getContextFor(EwIsDataType, Object[])} */
	public abstract <K extends EwIsKey> void validate(EwIsAction action, Map<K, String> contextMap) throws EwException;

}
