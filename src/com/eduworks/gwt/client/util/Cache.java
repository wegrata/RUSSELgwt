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

import java.util.Set;

import com.eduworks.gwt.client.enumeration.EwIsKey;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public final class Cache
{
	/** Called by {@link #getValue(String)}, which eliminates null values */
	private static native String get(String key)/*-{
        return localStorage[key];
	}-*/;

	/** Called by {@link #setValue(String, String)}, which prevents null values */
	private static native void set(String key, String value)/*-{
        localStorage[key] = value;
	}-*/;

	/** Called by {@link #setValue(String, String)}, which prevents null values */
	private static native void remove(String key)/*-{
         delete localStorage[key];
	}-*/;


	/* COLLECTION HELPERS */

	private static final String DELIM = ",";

	/** Adds all values from the {@link Set} to the cached value, and returns the union {@link Set}. */
	public static Set<String> addAll(Set<String> cachedSet, final EwIsKey key)
	{
		if (cachedSet == null)
			return getSet(key);

		cachedSet.addAll(getSet(key));
		setValue(key, Strings.join(cachedSet, DELIM));

		return cachedSet;
	}

	/** Adds the value to the {@link Set}, appends it to the cached value, and returns the {@link Set}. */
	public static Set<String> addTo(Set<String> cachedSet, final EwIsKey key, final String value)
	{
		if (cachedSet == null)
			cachedSet = getSet(key);

		if (!Strings.isNull(value))
		{
			cachedSet.add(value);
			setValue(key, Strings.join(cachedSet, DELIM));
		}

		return cachedSet;
	}

	/** Reads comma-separated string from cache, and return a {@link Set} of unique string values. */
	public static JSONArray getJson(final EwIsKey key)
	{
		final String cached = getValue(key);
		final JSONArray array = new JSONArray();

		if (!Strings.isNull(cached))
			for (String value : cached.split(DELIM))
				if (!Strings.isNull(value))
					Json.put(array, array.size(), value);

		return array;
	}

	/** Reads comma-separated string from cache, and return a {@link Set} of unique string values. */
	public static Set<String> getSet(final EwIsKey key)
	{
		final String cached = getValue(key);
		final Set<String> set = Collections.getSet();

		if (!Strings.isNull(cached))
			for (String value : cached.split(DELIM))
				if (!Strings.isNull(value))
					set.add(value);

		return set;
	}

	/** Removes the string from the cache and set, and returns the updated {@link Set} of cached strings. */
	public static Set<String> removeFrom(Set<String> cachedSet, final EwIsKey key, final String value)
	{
		if (cachedSet == null)
			cachedSet = getSet(key);

		if (cachedSet.contains(value))
			cachedSet.remove(value);

		setValue(key, Strings.join(cachedSet, DELIM));

		return cachedSet;
	}


	/* GETTERS AND SETTERS */

	public static String getValue(EwIsKey key)
	{
		return getValue(key.getKey());
	}
	public static String getValue(String key)
	{
		final String value = get(key);
		return (value == null) ? Strings.EMPTY : value;
	}

	public static boolean hasValue(EwIsKey key)
	{
		return hasValue(key.getKey());
	}
	public static boolean hasValue(String key)
	{
		return (Strings.isNull(get(key))) ? false : true;
	}

	public static void setValue(EwIsKey key, String value)
	{
		setValue(key.getKey(), value);
	}
	public static void setValue(String key, String value)
	{
		if (Strings.isNull(value))
			removeValue(key);
		else
			set(key, value);
	}

	public static void setValues(EwIsKey key, JSONObject values)
	{
		setValues(Json.getObject(values, key));
	}

	public static void setValues(JSONObject values)
	{
		if (values == null) return;

		for (String key : values.keySet())
			setValue(key, Json.decodeString(values, key));
	}

	public static void removeValue(EwIsKey key)
	{
		removeValue(key.getKey());
	}
	public static void removeValue(String key)
	{
		remove(key);
	}

}
