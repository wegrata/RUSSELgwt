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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Collections
{
	public static <T> List<T> getList()
	{
		return new ArrayList<T>();
	}

	public static <T> List<T> getList(int length)
	{
		final ArrayList<T> list = (length < 1)
			? new ArrayList<T>()
			: new ArrayList<T>(length);

		list.ensureCapacity(length);

		return list;
	}

	public static <T, V extends T> List<T> getList(V ... array)
	{
		if (array == null) return new ArrayList<T>();

		final List<T> list = new ArrayList<T>(array.length);

		for (T value : array) list.add(value);

		return list;
	}

	public static <T> Set<T> getSet()
	{
		return new HashSet<T>();
	}

	public static <T, V extends T> Set<T> getSet(Set<V> set)
	{
		return new HashSet<T>(set);
	}

	public static <T, V extends T> Set<T> getSet(V value)
	{
		final Set<T> set = getSet();

		set.add(value);

		return set;
	}

	public static <K, V> Map<K, V> getMap()
	{
		return new HashMap<K, V>();
	}

	public static <K, V> Map<K, V> getMap(Map<? extends K, ? extends V> map)
	{
		return new HashMap<K, V>(map);
	}

	public static boolean isEmpty(Collection<?> coll)
	{
		return (coll == null || coll.size() == 0);
	}

	public static boolean isEmpty(Map<?,?> map)
	{
		return (map == null || map.size() == 0);
	}

	/** Merges unique values from each collection into a single list, sorted if desired. */
	public static <V extends Comparable<V>> List<V> merge(boolean sort, Collection<V> ... colls)
	{
		final Set<V> unique = getSet();
		final List<V> merged = getList();

		for (Collection<V> coll : colls)
			if (coll != null) unique.addAll(coll);

		merged.addAll(unique);

		if (sort) java.util.Collections.sort(merged);

		return merged;
	}

	public static <T> void sort(List<T> list, Comparator<? super T> comparator)
	{
		java.util.Collections.sort(list, comparator);
	}

	public static Set<String> toSet(String string, char delim)
	{
		final Set<String> set = getSet();

		if (!Strings.isEmpty(string))
		{
			final StringBuilder next = new StringBuilder(string.length());

			char c;

			for (int i = 0; i < string.length(); i++)
			{
				if ((c = string.charAt(i)) != delim)
					next.append(c);
				else if (i > 0)
				{
					set.add(next.toString());
					next.setLength(0);
				}
			}

			if (next.length() > 0)
				set.add(next.toString());
		}

		return set;
	}
}
