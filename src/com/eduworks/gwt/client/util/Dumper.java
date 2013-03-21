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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public final class Dumper
{
	public final static char INDENT_CHAR = '\t';

	/** Dump a formatted message. */
	public static String dump(String format, Object ... args)
	{
		return dump(0, format, args);
	}

	/** Dump an indented, formatted message. */
	public static String dump(int indent, String format, Object ... args)
	{
		return Logger.debug("$(0)$(1)", getIndent(indent), Strings.format(format, args));
	}

	/** Dump an empty line. */
	public static Object dump()
	{
		return dump(0, Strings.EMPTY);
	}

	/** Dump any object according to its type. */
	public static Object dump(Object value)
	{
		return dump(0, value);
	}

	/** Dump an indented object. */
	public static Object dump(int indent, Object value)
	{
		final Object dump;

		if (!isPrimitive(value))
		{
		  dump("$(0)DUMPING OBJECT: $(1)", getIndent(indent), value.getClass());
		  indent++;
		}

		if (value == null)
			dump = dump("$(0)$(1)", getIndent(indent), Strings.NULL);

		else if (value instanceof Class<?>)
			dump = dump(indent, (Class<?>)value);

		else if (value instanceof Element)
			dump = dump(indent, (Element)value);

		else if (value instanceof Widget)
			dump = dump(indent, (Widget)value);

		else if (value instanceof JSONValue)
			dump = dump(indent, (JSONValue)value);

		else if (value instanceof List)
			dump = dump(indent, (List<?>)value);

		else if (value instanceof Map)
			dump = dump(indent, (Map<?,?>)value);

		else if (value instanceof Set)
			dump = dump(indent, (Set<?>)value);

		else if (value.getClass().isArray())
			try
			{
				dump = dump(indent, (Object[]) value);
			}
			catch (Exception e)
			{
				return Logger.logError("Unable to Dump Array: $(0)", value);
			}
		else
			dump = dump("$(0)$(1)", getIndent(indent), value);


		if (value instanceof Throwable)
			((Throwable) value).printStackTrace();

		return dump;
	}

	public static <T> Class<T> dump(Class<T> clazz)
	{
		return dump(0, clazz);
	}

	public static <T> Class<T> dump(int indent, Class<T> clazz)
	{
		if (clazz == null)
			dump(indent, (T)null);
		else
			dump(indent, Classes.getSimpleName(clazz));

		return clazz;
	}

	public static Element dump(Element element)
	{
		return dump(0, element);
	}

	public static Element dump(int indent, Element element)
	{
		if (element == null)
			dump(indent, "NULL ELEMENT");
		else
		{
			dump(indent++, element.getClass());
			dump(indent, element.getString());
		}

		return element;
	}

	/** Print the contents of a {@link FlexTable} to the console. */
	public static FlexTable dump(FlexTable table)
	{
		return dump(0, table);
	}

	/** Print the contents of a {@link FlexTable} to the console. */
	public static FlexTable dump(int indent, FlexTable table)
	{
		if (table != null)
		{
			final int rowCount = table.getRowCount();
			final String baseIndent = getIndent(indent++);
			final String indentation = getIndent(indent);

			dump(indent, "$(0) of size $(1)", table.getClass(), table.getRowCount());

			for (int i = 0; i < rowCount; i++)
			{
				dump("$(0)ROW: $(1)", baseIndent, i);
				dump("$(0)$(1)", indentation, table.getRowFormatter().getElement(i));

				final int cells = table.getCellCount(i);

				if (cells > 0)
					for (int j = 0; j < cells; j++)
					{
						dump("$(0)CELL: $(1)", baseIndent, j);
						dump("$(0)$(1)", indentation, table.getWidget(j, j));
					}
			}
		}

		return table;
	}

	public static Widget dump(Widget widget)
	{
		return dump(0, widget);
	}

	/** Print the contents of a {@link FlexTable} to the console. */
	public static Widget dump(int indent, Widget widget)
	{
		if (widget != null)
		{
			if (widget instanceof FlexTable)
				dump(indent, (FlexTable)widget);
			else
			{
				dump("$(0)$(1):", getIndent(indent++), widget.getClass());
				dump(indent, widget.toString());
			}
		}

		return widget;
	}

	public static JSONValue dump(JSONValue value)
	{
		return dump(0, value);
	}

	public static JSONValue dump(int indent, JSONValue value)
	{
		if (value == null || value.isNull() != null)
			dump(indent, (Object)null);
		else
		{
			dump("$(0)$(1)", getIndent(indent++), value.getClass());

			if (value instanceof JSONArray)
			{
				final JSONArray array = value.isArray();

				dump(indent, "JSONArray of size $(0)", array.size());

				for (int i = 0; i < array.size(); i++)
					dump(indent, array.get(i));
			}
			else if (value instanceof JSONObject)
			{
				final String indentation = getIndent(indent);
				final JSONObject object = value.isObject();
				final int outerIndent = indent++;
				final int innerIndent = indent;

				dump(outerIndent, "JSONObject of size $(0)", object.size());

				for (String key : object.keySet())
					if (isPrimitive(object.get(key)))
						dump("$(0)$(1):$(2)", indentation, key, object.get(key));
					else
					{
						dump(outerIndent, key);
						dump(innerIndent, object.get(key));
					}
			}

			else dump(indent, value.toString());
		}

		return value;
	}

	public static <T> T[] dump(T[] array)
	{
		return dump(0, array);
	}

	public static <T> T[] dump(int indent, T[] array)
	{
		if (array == null)
			dump(indent, (Object)null);
		else
		{
			String type = Strings.NULL;

			if (array.length == 0)
				type = Strings.toString(array.getClass());
			else for (Object value : array)
				if (value != null)
				{
					type = Strings.toString(value.getClass());
					break;
				}

			dump(indent++, "$(0) Array of size $(1)", type, array.length);

			for (Object value : array)
				dump(indent, Strings.toString(value));
		}

		return array;
	}

	public static <T> Collection<T> dump(Collection<T> coll)
	{
		return dump(0, coll);
	}

	public static <T> Collection<T> dump(int indent, Collection<T> coll)
	{
		if (coll == null)
			dump(indent, (Object)null);
		else if (coll.isEmpty())
			dump("$(0)EMPTY $(1)", getIndent(indent), coll.getClass());
		else
		{
			dump(indent++, "$(0) of size $(1)", coll.getClass(), coll.size());

			final String indentation = getIndent(++indent);

			for (Object value : coll)
				if (isPrimitive(value))
					dump("$(0)$(1)", indentation, Strings.toString(value));
				else
					dump(indent, Strings.toString(value));
		}

		return coll;
	}

	public static <T> Iterator<T> dump(Iterator<T> iter)
	{
		return dump(0, iter);
	}

	public static <T> Iterator<T> dump(int indent, Iterator<T> iter)
	{
		if (iter == null)
			dump(indent, (Object)null);
		else if (!iter.hasNext())
			dump("$(0)EMPTY $(1)", getIndent(indent), iter.getClass());
		else
		{
			dump("$(0)$(1):", getIndent(indent), iter.getClass());

			final String indentation = getIndent(++indent);

			while (iter.hasNext())
			{
				Object value = iter.next();

				if (isPrimitive(value))
					dump("$(0)$(1)", indentation, Strings.toString(value));
				else
					dump(indent, Strings.toString(value));
			}
		}

		return iter;
	}

	public static <K, V> Map<K, V> dump(Map<K, V> map)
	{
		return dump(0, map);
	}

	public static <K, V> Map<K, V> dump(int indent, Map<K, V> map)
	{
		if (map == null)
			dump(indent, (Object)null);
		else if (map.isEmpty())
			dump("$(0)EMPTY $(1)", getIndent(indent), map.getClass());
		else
		{
			dump(indent++, "$(0) of size $(1)", map.getClass(), map.size());

			final String indentation = getIndent(indent);
			final int outerIndent = indent++;
			final int innerIndent = indent;

			for (Entry<?,?> entry : map.entrySet())
				if (isPrimitive(entry.getValue()))
					dump("$(0)$(1):$(2)", indentation, entry.getKey(), entry.getValue());
				else
				{
					dump(outerIndent, entry.getKey());
					dump(innerIndent, entry.getValue());
				}
		}

		return map;
	}

	private static String getIndent(int depth)
	{
		if (depth <= 0) return Strings.EMPTY;

		final StringBuilder indent = new StringBuilder(depth);

		for (int i = 0; i < depth; i++)
			indent.append(INDENT_CHAR);

		return indent.toString();
	}

	private static boolean isPrimitive(Object object)
	{
		return (
				object == null ||
				object instanceof Class<?> ||
				object instanceof Boolean ||
				object instanceof Number ||
				object instanceof String ||
				object instanceof Enum || (
				object instanceof JSONValue &&
				Json.isPrimitive((JSONValue)object)
			));
	}

}
