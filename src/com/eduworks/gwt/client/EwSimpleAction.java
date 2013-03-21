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
