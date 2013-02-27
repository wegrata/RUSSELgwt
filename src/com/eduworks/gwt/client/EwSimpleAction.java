package com.eduworks.gwt.client;

import com.eduworks.gwt.client.enumeration.impl.EwStyle;
import com.eduworks.gwt.client.util.Strings;

/** Defines a base implementation of {@link EwIsAction}, requiring only an action name. */
public class EwSimpleAction implements EwIsAction
{
	final String actionName;
	final String actionStyle;

	public EwSimpleAction(String actionFormat, Object ... args)
	{
		this(null, actionFormat, args);
	}

	public EwSimpleAction(String stylePrefix, String actionFormat, Object ... args)
	{
		final String actionName = Strings.format(actionFormat, args);

		this.actionName = Strings.toTitleCase(actionName);
		this.actionStyle = EwStyle.getStyle(stylePrefix, actionName);
	}

	@Override
	public String getActionName()
	{
		return actionName;
	}

	@Override
	public String getActionStyle()
	{
		return actionStyle;
	}

}
