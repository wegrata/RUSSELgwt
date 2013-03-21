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
package com.eduworks.gwt.client.util;

import java.util.Map;

import com.eduworks.gwt.client.enumeration.EwIsKey;
import com.eduworks.gwt.client.enumeration.impl.EwStyle;

public final class Enums
{
	public static <T extends EwIsKey> String getFrom(T key, Map<T,String> map)
	{
		return (map == null) ? null : map.get(key);
	}

	/** Lower-case the name, split it on underscores, and camel case it. */
	public static <E extends Enum<E>> String getKey(E value)
	{
		if (value == null) return Strings.NULL;

		final String name = value.name();

		if (name.indexOf('_') == -1)
			return name.toLowerCase();

		/* Split on underscore and camel case */

		final String[] parts = name.toLowerCase().split("_");
		final StringBuilder key = new StringBuilder(name.length());

		for (int i = 0; i < parts.length; i++)
			switch (i)
			{
				case 0:
					key.append(parts[i]);
					break;
				default:
					key.append(Strings.capitalize(parts[i]));
					break;
			}

		return key.toString();
	}

	public static <E extends Enum<E>> String getId(E value)
	{
		return getKey(value);
	}

	/** Lowercase {@link Enum#name()}, and replace underscores with dashes. */
	public static <E extends Enum<E>> String getStyle(E styled)
	{
		return getStyle(EwStyle.DEFAULT_PREFIX, styled);
	}

	/** Lowercase {@link Enum#name()}, replace underscores with dashes, and prepend prefix if provided. */
	public static <E extends Enum<E>> String getStyle(String prefix, E styled)
	{
		final char delim = EwStyle.DELIM;
		final String style = styled.name().replace('_', delim).toLowerCase();

		if (prefix == null)
			return style;
		else if (prefix.charAt(prefix.length()-1) == delim)
			return prefix + style;
		else
			return prefix + delim + style;
	}

	public static <E extends Enum<E>> String getTitle(E labeled)
	{
		return Strings.toTitleCase(labeled.name());
	}

	public static <K extends Enum<K> & EwIsKey> K keyForString(K[] values, String string)
	{
		for (K value : values)
			if (value.getKey().equalsIgnoreCase(string))
				return value;

		return null;
	}

	public static <E extends Enum<E>> E valueForString(E[] values, String string)
	{
		for (E value : values)
			if (value.name().equalsIgnoreCase(string))
				return value;

		return null;
	}

}
