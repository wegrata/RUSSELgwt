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
package com.eduworks.gwt.client.enumeration.impl;

import com.eduworks.gwt.client.enumeration.EwIsStyle;
import com.eduworks.gwt.client.util.Arrays;
import com.eduworks.gwt.client.util.Classes;
import com.eduworks.gwt.client.util.Enums;
import com.eduworks.gwt.client.util.Strings;

public enum EwStyle implements EwIsStyle
{
	CHECKED,
	UNCHECKED,
	DISABLED,
	ENABLED,
	ERROR,
	HIDE,
	IDENTIFIER
	;

	/* STATIC MEMBERS */

	public static final String DEFAULT_PREFIX = "ew";
	public static final String DEFAULT_STYLE = getStyle(Strings.NULL);

	public static final char DELIM = '-';

	/** Generates a style for the specified class name. */
	public static String getStyle(String prefix, Class<?> clazz)
	{
		return getStyle(prefix, Classes.getSimpleName(clazz));
	}

	/** Generates a prefixed style from the string provided. If camel-cased, the Style delimiter is inserted. */
	public static String getStyle(String prefix, String style)
	{
		if (Strings.isEmpty(style))
			return DEFAULT_STYLE;

		final String pfx = (Strings.isEmpty(prefix)) ? DEFAULT_PREFIX : prefix;

		return getStyle(pfx, Strings.splitCamel(style));
	}

	/** Combine a series of strings for a proper style name, ensuring the style is prefixed */
	public static String getStyle(String prefix, String ... styles)
	{
		if (Arrays.isEmpty(styles))
			return DEFAULT_STYLE;
		else
		{
			final String pfx = (!Strings.endsWith(prefix, DELIM))
				? Strings.format("$(0)$(1)", prefix, DELIM)
				: prefix;

			final String style = Strings.append(DELIM, styles).toLowerCase();
			final StringBuilder styleBuilder = new StringBuilder(pfx.length() + style.length());

			if (style.startsWith(pfx))
				styleBuilder.append(style);
			else
				styleBuilder.append(pfx).append(style);

			return Strings.append(DELIM, styleBuilder.toString().split("[^0-9A-Za-z]"));
		}
	}

	/* INSTANCE MEMBERS */

	private final String value;

	private EwStyle()
	{
		this(DEFAULT_PREFIX);
	}

	private EwStyle(String prefix)
	{
		this.value = Enums.getStyle(prefix, this);
	}

	@Override
	public String getName()
	{
		return name();
	}

	@Override
	public String getStyleName()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return getStyleName();
	}

}
