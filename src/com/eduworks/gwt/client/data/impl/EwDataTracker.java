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
package com.eduworks.gwt.client.data.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.eduworks.gwt.client.EwIsAction;
import com.eduworks.gwt.client.data.EwIsDataType;
import com.eduworks.gwt.client.enumeration.EwIsKey;
import com.eduworks.gwt.client.reflection.Instantiable;
import com.eduworks.gwt.client.util.Classes;
import com.eduworks.gwt.client.util.Collections;

public abstract class EwDataTracker implements Instantiable
{
	/* STATIC MEMBERS */

	private final static Map<?, EwDataTracker> factory = Collections.getMap();

	public static <T extends EwDataTracker> T getInstance(Class<T> trackerClass)
	{
		@SuppressWarnings("unchecked")
		final Map<Class<T>, T> map = (Map<Class<T>, T>) factory;

		if (!factory.containsKey(trackerClass))
			map.put(trackerClass, Classes.getInstance(trackerClass));

		return map.get(trackerClass);
	}

	/** Delete all tracked data. */
	public static void delAllTrackedData()
	{
		for (EwDataTracker tracker : factory.values())
			tracker.clearAll();
	}

	/** Add a unique value to the collection. */
	protected static <V, C extends Collection<V>> void add(C coll, V value)
	{
		if (!coll.contains(value)) coll.add(value);
	}

	/** Remove an existing value from the collection. */
	protected static <V, C extends Collection<V>> void del(C coll, V value)
	{
		if (coll.contains(value)) coll.remove(value);
	}

	/** Get an existing set from the map, or return an empty set. */
	protected static <K, V> Set<V> getSet(Map<K, Set<V>> fromMap, K key)
	{
		if (fromMap.containsKey(key))
			return fromMap.get(key);
		else
			return Collections.getSet();
	}

	/** Get an existing map from the map, or return an empty map. */
	protected static <K, V> Map<K, V> getMap(Map<K, Map<K,V>> fromMap, K key)
	{
		if (fromMap.containsKey(key))
			return fromMap.get(key);
		else
			return Collections.getMap();
	}

	/** @return true if the map contains the key, and the collection at key contains the value. */
	protected static <K, V, C extends Collection<V>> boolean has(Map<K, C> map, K key, V value)
	{
		return (map != null && map.containsKey(key) && map.get(key).contains(value));
	}

	/** Add value to the set in map at key. */
	protected static <K, V> void putInSet(Map<K, Set<V>> inMap, K key, V value)
	{
		final Set<V> toSet = getSet(inMap, key);

		add(toSet, value);

		inMap.put(key, toSet);
	}

	/** Add all values in set to the set in map at key. */
	protected static <K, V> void putSet(Map<K, Set<V>> inMap, K key, Set<V> set)
	{
		final Set<V> toSet = getSet(inMap, key);

		for (V val : set) toSet.add(val);

		inMap.put(key, toSet);
	}

	/** Remove an existing value from map. */
	protected static <K, V> void rem(Map<K, V> fromMap, K key)
	{
		if (fromMap.containsKey(key)) fromMap.remove(key);
	}

	/** Remove an existing value from the collection at key in map. */
	protected static <K, V, C extends Collection<V>> void rem(Map<K, C> fromMap, K key, V value)
	{
		if (fromMap.containsKey(key))
		{
			final C from = fromMap.get(key);

			del(from, value);

			if (from.size() == 0)
				fromMap.remove(key);
		}
	}


	/* ABSTRACT METHODS */

	/** Clears tracked data which is no longer relevant for validation. */
	public abstract <T extends EwIsAction> void clear(T action, Map<EwIsKey, String> contextMap);

	/** Clears all tracked data, should leave nothing uncleared. */
	public abstract <T extends EwIsAction> void clearAll();

	/** Clears all tracked data corresponding to the actions specified. */
	public abstract <T extends EwIsAction> void clearAll(String entityId, T ... actions);

	/** Tracks data retrieved from the database for validation when displaying or submitting a form. */
	public abstract <T extends EwIsDataType> void track(T item, Map<EwIsKey, String> contextMap);

}
