package com.eduworks.gwt.client.util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduworks.gwt.client.enumeration.EwIsEnum;
import com.eduworks.gwt.client.enumeration.EwIsKey;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public final class Json
{
	public static boolean contains(JSONObject object, EwIsKey key)
	{
		if (object == null || key == null) return false;

		return contains(object, key.getKey());
	}

	public static boolean contains(JSONObject object, String key)
	{
		if (object == null || key == null) return false;

		return (object.size() > 0 && object.containsKey(key));
	}

	/** @return true if object contains all of the specified keys; false otherwise */
	public static boolean containsAll(JSONObject object, EwIsKey ... keys)
	{
		if (object == null || keys == null) return false;

		for (int i = 0; i < keys.length; i++)
			if (!contains(object, keys[i]))
				return false;

		return true;
	}

	/** @return true if object contains all of the specified keys; false otherwise */
	public static boolean containsAll(JSONObject object, String ... keys)
	{
		if (object == null || keys == null) return false;

		for (int i = 0; i < keys.length; i++)
			if (!contains(object, keys[i]))
				return false;

		return true;
	}

	/** @return true if object contains any of the specified keys; false otherwise */
	public static boolean containsAny(JSONObject object, EwIsKey ... keys)
	{
		if (object == null || keys == null) return false;

		for (int i = 0; i < keys.length; i++)
			if (contains(object, keys[i]))
				return true;

		return false;
	}

	/** @return true if object contains any of the specified keys; false otherwise */
	public static boolean containsAny(JSONObject object, String ... keys)
	{
		if (object == null || keys == null) return false;

		for (int i = 0; i < keys.length; i++)
			if (contains(object, keys[i]))
				return true;

		return false;
	}

	/** @return true if array contains a String that matches value; false otherwise */
	public static boolean containsString(JSONArray array, String value)
	{
		return containsString(array, (String)null, value);
	}

	/** @see #containsString(JSONArray,String,String) */
	public static boolean containsString(JSONArray array, EwIsKey key, String value)
	{
		return containsString(array, key.getKey(), value);
	}

	/** @return true if array has a JSONObject element with a value for key that matches value; false otherwise */
	public static boolean containsString(JSONArray array, String key, String value)
	{
		return (indexOfString(array, key, value) > -1);
	}

	/** @return if array contains a String that matches value, the index; -1 otherwise */
	public static int indexOfString(JSONArray array, String value)
	{
		return indexOfString(array, (String)null, value);
	}

	/** @see #indexOfString(JSONArray,String,String) */
	public static int indexOfString(JSONArray array, EwIsKey key, String value)
	{
		return indexOfString(array, key.getKey(), value);
	}

	/** @return if the array has a JSONObject element with a value for key that matches value, the index; -1 otherwise */
	public static int indexOfString(JSONArray array, String key, String value)
	{
		if (array == null || value == null) return -1;

		final boolean nullKey = (key == null);
		String next;

		for (int i = 0; i < array.size(); i++)
		{
			next = (nullKey)
				? getString(array.get(i))
				: getString(array.get(i), key);

			if (value.equalsIgnoreCase(next))
				return i;
		}

		return -1;
	}

	public static JSONArray clone(JSONArray array)
	{
		if (array == null) return null;

		final JSONArray clone = new JSONArray();
		final int size = array.size();

		for (int i = 0; i < size; i++)
			clone.set(i, array.get(i));

		return clone;
	}

	public static JSONObject clone(JSONObject object)
	{
		if (object == null) return null;

		final JSONObject clone = new JSONObject();
		final Set<String> keySet = object.keySet();

		for (String key : keySet)
			clone.put(key, object.get(key));

		return clone;
	}

	/** Calls {@link #drill(JSONValue)} with the value at index in array. */
	public static JSONString drill(JSONArray array, int index)
	{
		if (array == null || array.size() <= index) return null;

		return drill(array.get(index));
	}

	/** Calls {@link #drill(JSONValue)} with the value at key in object. */
	public static JSONString drill(JSONObject object, EwIsKey key)
	{
		if (object == null || key == null) return null;

		return drill(object.get(key.getKey()));
	}

	/** Dereference the first leaf node encountered in a depth-first traversal of value. */
	public static JSONString drill(JSONValue value)
	{
		if (value instanceof JSONString)
			return value.isString();

		else if (value instanceof JSONArray)
			return drill(getFirstValue(value.isArray()));

		else if (value instanceof JSONObject)
			return drill(getFirstValue(value.isObject()));

		return null;
	}

	public static JSONValue getFirstValue(JSONValue value)
	{
		if (value == null)
			return JSONNull.getInstance();

		else if (value instanceof JSONArray)
			return value.isArray().get(0);

		else if (value instanceof JSONObject)
		{
			final JSONObject object = value.isObject();
			return object.get(object.keySet().iterator().next());
		}

		return value;
	}

	public static JSONValue getFromArray(JSONArray array, int index)
	{
		return getFromArray(array, index, true);
	}

	public static JSONValue getFromArray(JSONArray array, int index, boolean logMissing)
	{
		if (array == null) return null;

		final int size = array.size();
		final JSONValue jsonValue = array.get(index);

		if (jsonValue != null) return jsonValue;

		if (logMissing)
		{
			Logger.logWarning("Null JSONValue for index: $(0)", index);
			Logger.logWarning("Maximum Index: $(0)", size-1);
		}

		return null;
	}

	public static JSONValue getFromObject(JSONObject object, EwIsKey key)
	{
		return getFromObject(object, key.getKey());
	}

	public static JSONValue getFromObject(JSONObject object, String key)
	{
		return getFromObject(object, key, true);
	}

	/**
	 * Dereferences a value from nested objects at the location specified by the provided keys.
	 * So, for a {@link JSONObject}:
	 * <p><code>JSONObject obj = "{ "key1": { "key2": { "key3": "nestedValue" } } }"</code>
	 * <p>Calling:
	 * <p><code>getFromObject(obj, "key1", "key2", "key3")</code>
	 * <p>Will return "nestedValue"
	 */
	public static JSONValue getFromObject(JSONObject object, String ... keys)
	{
		if (object == null || keys == null)
			return object;

		JSONValue value = object;

		for (String key : keys)
			if (value != null)
				value = getFromObject(value.isObject(), key, true);
			else return null;

		return value;
	}

	public static JSONValue getFromObject(JSONObject object, String key, boolean logMissing)
	{
		if (object == null || key == null) return null;

		final JSONValue jsonValue = object.get(key);

		if (jsonValue != null) return jsonValue;

		if (logMissing)
		{
			Logger.logWarning("Null JSONValue for Key: $(0)", key);

			if (object.keySet().size() > 0)
				Logger.logWarning("Available Keys: $(0)", Strings.join(object.keySet(), ","));
		}

		return null;
	}

	/** If value is array, return it as a JSONArray; otherwise log warning and return null. */
	public static JSONArray getArray(JSONValue value)
	{
		if (value == null) return null;

		final JSONArray array = value.isArray();

		if (array != null) return array;

		Logger.logWarning(
				"Value is not a JSONArray: $(0):$(1)",
				Classes.getSimpleName(value),
				value
			);

		return null;
	}

	/** @return the JSONArray value of the item at index if it exists, otherwise null */
	public static JSONArray getArray(JSONValue array, int index)
	{
		final JSONValue jsonValue = getFromArray(getArray(array), index);

		if (jsonValue != null) return getArray(jsonValue);

		return null;
	}

	/** @return {@link #getArray(JSONValue, String)} with the key's value */
	public static JSONArray getArray(JSONValue object, EwIsKey key)
	{
		return getArray(object, key.getKey());
	}

	/** @return the JSONArray value at key if it exists, otherwise null */
	public static JSONArray getArray(JSONValue object, String key)
	{
		final JSONValue jsonValue = getFromObject(getObject(object), key);

		if (jsonValue != null) return getArray(jsonValue);

		return null;
	}

	/**
 	 * @return the boolean value of {@link JSONValue#isBoolean()} called on the value passed in
	 * if it is a {@link Number}, or false if it is not
	 */
	public static Boolean getBoolean(JSONValue value)
	{
		if (value != null)
		{
			if (value.isBoolean() != null)
				return value.isBoolean().booleanValue();

			else if (value.isString() != null)
				return Boolean.valueOf(getString(value));

			else Logger.logWarning("Value is not a Boolean: $(0)", value);
		}

		return Boolean.FALSE;
	}

	/** @return the boolean value of the item at index if it exists, otherwise false */
	public static Boolean getBoolean(JSONValue array, int index)
	{
		final JSONValue jsonValue = getFromArray(getArray(array), index);

		if (jsonValue != null) return getBoolean(jsonValue);

		return Boolean.FALSE;
	}

	/** @return {@link #getBoolean(JSONValue, String)} with the key's value */
	public static Boolean getBoolean(JSONValue object, EwIsKey key)
	{
		return getBoolean(object, key.getKey());
	}

	/** @return the boolean value of the value at key if it exists, otherwise false */
	public static Boolean getBoolean(JSONValue object, String key)
	{
		final JSONValue jsonValue = getFromObject(getObject(object), key, false);

		if (jsonValue != null) return getBoolean(jsonValue);

		return Boolean.FALSE;
	}

	/**
 	 * @return the double value of {@link JSONValue#isNumber()} called on the value passed in
	 * if it is a {@link Number}, or zero if it is not
	 */
	public static Double getNumber(JSONValue value)
	{
		if (value != null)
		{
			if (value.isNumber() != null)
				return value.isNumber().doubleValue();

			else if (value.isString() != null)
				return Numbers.parseDouble(getString(value));

			else
				Logger.logWarning("Value is not a Number: $(0)", value);
		}

		return new Double(0);
	}

	/** @return the double value of the item at index if it exists, otherwise zero */
	public static Double getNumber(JSONValue array, int index)
	{
		final JSONValue jsonValue = getFromArray(getArray(array), index);

		if (jsonValue != null) return getNumber(jsonValue);

		return new Double(0);
	}

	/** @return {@link #getNumber(JSONValue, String)} with the key's value */
	public static Double getNumber(JSONValue object, EwIsKey key)
	{
		return getNumber(object, key.getKey());
	}

	/** @return the double value of the value at key if it exists, otherwise zero */
	public static Double getNumber(JSONValue object, String key)
	{
		final JSONValue jsonValue = getFromObject(getObject(object), key);

		if (jsonValue != null) return getNumber(jsonValue);

		return new Double(0);
	}

	/** If value is object, return it as a JSONObject; otherwise log warning and return null. */
	public static JSONObject getObject(JSONValue value)
	{
		if (value == null) return null;

		final JSONObject object = value.isObject();

		if (object != null) return object;

		Logger.logWarning(
				"Value is not a JSONObject: $(0):$(1)",
				Classes.getSimpleName(value),
				value
		);

		return null;
	}

	/** @return the JSONObject value of the item at index if it exists, otherwise null */
	public static JSONObject getObject(JSONValue array, int index)
	{
		final JSONValue jsonValue = getFromArray(getArray(array), index);

		if (jsonValue != null) return getObject(jsonValue);

		return null;
	}

	/** @return {@link #getObject(JSONValue, String)} with the key's value */
	public static JSONObject getObject(JSONValue object, EwIsKey key)
	{
		return getObject(object, key.getKey());
	}

	/** @return the JSONObject value at key if it exists, otherwise null */
	public static JSONObject getObject(JSONValue object, String key)
	{
		final JSONValue jsonValue = getFromObject(getObject(object), key);

		if (jsonValue != null) return getObject(jsonValue);

		return null;
	}

	/**
	 * @return the nested JSONObject value at key if it exists, otherwise null
	 * @see #getFromObject(JSONObject, String[])
	 */
	public static JSONObject getObject(JSONValue object, String ... keys)
	{
		final JSONValue jsonValue = getFromObject(getObject(object), keys);

		if (jsonValue != null) return getObject(jsonValue);

		return null;
	}

	/** @return {@link #getString(JSONValue)} with the key's value */
	public static String decodeString(JSONValue value)
	{
		return Uri.decode(getString(value));
	}

	/** @return the decoded string value of the item at index if it exists, otherwise the empty string */
	public static String decodeString(JSONValue array, int index)
	{
		return Uri.decode(getString(array, index));
	}

	/** @return {@link #getString(JSONValue, String)} with the key's value */
	public static String decodeString(JSONValue object, EwIsKey key)
	{
		return Uri.decode(getString(object, key.getKey()));
	}

	/** @return {@link #getString(JSONValue, String)} with the specified key */
	public static String decodeString(JSONValue object, String key)
	{
		return Uri.decode(getString(object, key));
	}

	/**
 	 * @return the String value of {@link JSONValue#isString()} called on the value passed in
	 * if it is a {@link String}, or the empty string value if it is not
	 */
	public static String getString(JSONValue value)
	{
		if (value != null)
		{
			if (value.isString() != null)
				return value.isString().stringValue();
			else
				Logger.logWarning("Value is not a String: $(0)", value);
		}

		return Strings.EMPTY;
	}

	/** @return the string value of the item at index if it exists, otherwise the empty string */
	public static String getString(JSONValue array, int index)
	{
		final JSONValue jsonValue = getFromArray(getArray(array), index);

		if (jsonValue != null) return getString(jsonValue);

		return Strings.EMPTY;
	}

	/** @return {@link #getString(JSONValue, String)} with the key's value */
	public static String getString(JSONValue object, EwIsKey key)
	{
		return getString(object, key.getKey());
	}

	/** @return the string value of the value at key if it exists, otherwise the empty string */
	public static String getString(JSONValue object, String key)
	{
		final JSONValue jsonValue = getFromObject(getObject(object), key);

		if (jsonValue != null) return getString(jsonValue);

		return Strings.EMPTY;
	}

	public static boolean hasArray(JSONObject object, EwIsKey key)
	{
		return hasArray(object, key.getKey());
	}

	public static boolean hasArray(JSONObject object, String key)
	{
		if (!contains(object, key))
			return false;

		return getFromObject(object, key).isArray() != null;
	}

	public static boolean hasObject(JSONObject object, EwIsKey key)
	{
		return hasObject(object, key.getKey());
	}

	public static boolean hasObject(JSONObject object, String key)
	{
		if (!contains(object, key))
			return false;

		return getFromObject(object, key).isObject() != null;
	}

	public static boolean hasString(JSONObject object, EwIsKey key)
	{
		return hasString(object, key.getKey());
	}

	public static boolean hasString(JSONObject object, String key)
	{
		if (!contains(object, key))
			return false;

		return !Strings.isEmpty(getString(object, key));
	}

	public static boolean hasValue(JSONObject object, EwIsKey key)
	{
		return hasValue(object, key.getKey());
	}

	public static boolean hasValue(JSONObject object, String key)
	{
		if (!contains(object, key))
			return false;

		return !isEmpty(getFromObject(object, key));
	}

	public static boolean isEmpty(JSONValue value)
	{
		if (isNull(value)) return true;

		if (value instanceof JSONArray)
			return ((JSONArray)value).size() == 0;

		if (value instanceof JSONObject)
			return ((JSONObject)value).size() == 0;

		if (value instanceof JSONString)
			return isEmptyString(((JSONString)value).stringValue());

		return false;
	}

	public static boolean isEmptyString(String stringValue)
	{
		if (stringValue == null) return true;

		switch (stringValue.length())
		{
			case 0:
				return true;

			case 2:
				if (stringValue.charAt(0) == '[' && stringValue.charAt(1) == ']')
					return true;
				else if (stringValue.charAt(0) == '{' && stringValue.charAt(1) == '}')
					return true;
				break;
		}

		return false;
	}

	public static boolean isNull(JSONValue value)
	{
		if (value == null || value instanceof JSONNull)
			return true;

		return false;
	}

	/** @return true if value is not null and neither an array nor an object. */
	public static boolean isPrimitive(JSONValue value)
	{
		if (value == null) return false;

		return !(value instanceof JSONArray || value instanceof JSONObject);
	}

	/** Converts value to JSONNumber and add to array. */
	public static JSONValue put(JSONArray array, int index, Number value)
	{
		if (array == null || index < 0 || value == null) return null;

		return array.set(index, new JSONNumber(value.doubleValue()));
	}

	/** Converts value to JSONString and add to array. */
	public static JSONValue put(JSONArray array, int index, String value)
	{
		if (array == null || index < 0 || value == null) return null;

		return array.set(index, new JSONString(value));
	}

	/** Converts value to JSONNumber and puts in object at key. */
	public static JSONValue put(JSONObject object, EwIsKey key, Number value)
	{
		return put(object, key.getKey(), value);
	}

	/** Converts value to JSONNumber and puts in object at key. */
	public static JSONValue put(JSONObject object, String key, Number value)
	{
		if (object == null || key == null || value == null) return null;

		return object.put(key, new JSONNumber(value.doubleValue()));
	}

	/** Converts value to JSONString and puts in object at key. */
	public static JSONValue put(JSONObject object, EwIsKey key, String value)
	{
		return put(object, key.getKey(), value);
	}

	/** Converts value to JSONString and puts in object at key. */
	public static JSONValue put(JSONObject object, String key, String value)
	{
		if (object == null || key == null || value == null) return null;

		return object.put(key, new JSONString(value));
	}

	/** Iterates over object key set, and if any key matches one in the enumeration, put it in the context map. */
	public static <E extends EwIsEnum & EwIsKey, K extends E> Map<E, String> getAll(JSONObject object, K[] keys)
	{
		Map<E, String> contextMap = Collections.getMap();

		if (object != null)
			for (String fromKey : object.keySet())
				for (E toKey : keys)
					if (toKey.getKey().equalsIgnoreCase(fromKey))
					{
						final String value = getString(object, toKey);
						if (value != null) contextMap.put(toKey, value);

						break;
					}

		return contextMap;
	}

	/** Add all the properties from map to object. If object is null a new object is created. */
	public static <T> JSONObject putAll(JSONObject object, Map<T, String> map)
	{
		if (object == null)
			object = new JSONObject();

		if (map == null)
			return object;

		for (T obj : map.keySet())
		{
			final String key = (obj instanceof EwIsKey)
				? ((EwIsKey)obj).getKey()
				: obj.toString();

			object.put(key, new JSONString(map.get(key)));
		}

		return object;
	}

	/**
	 * Creates a new array sorted by keys if they are provided and it contains JSONObjects;
	 * if no keys are provided but the array contains JSONObjects, the keys from each are
	 * merged and sorted into a single key set and the values are compared in that order;
	 * if the array contains either JSONObjects or JSONArrays, the value sorting is done
	 * recursively through all nested JSONObjects and JSONArrays with respect to the keys;
	 * otherwise, keys are ignored and the new array is sorted by the JSONValues it contains.
	 */
	public static JSONArray sort(JSONArray array, final String ... keys)
	{
		final JSONArray jsonArray = new JSONArray();

		if (array == null) return jsonArray;

		final List<JSONValue> elements = Collections.getList();
		final Comparator<JSONValue> comparator = new Comparator<JSONValue>()
		{
			@Override
			public int compare(JSONValue arg0, JSONValue arg1)
			{
				final boolean null0 = (arg0 == null || arg0.isNull() != null);
				final boolean null1 = (arg1 == null || arg1.isNull() != null);

				if (null0 && null1)
					return 0;
				else if (null0)
					return -1;
				else if (null1)
					return 1;

				else if (arg0.getClass() != arg1.getClass())
				{
					Logger.logError(
							"Could not compare values $(0):$(1) and $(2):$(3)",
							Classes.getSimpleName(arg0), arg0,
							Classes.getSimpleName(arg1), arg1
						);

					return 0; // Integer equivalent of Double.NaN
				}

				int compareVal;

				if (arg0.isArray() != null)
				{
					final JSONArray arr0 = arg0.isArray();
					final JSONArray arr1 = arg1.isArray();
					final int size = Math.min(arr0.size(), arr1.size());

					for (int i = 0; i < size; i++)
						switch (compareVal = compare(arr0.get(i), arr1.get(i)))
						{
							case 0:
								continue;
							default:
								return compareVal;
						}

					return (arr0.size() - arr1.size());
				}
				else if (arg0.isBoolean() != null)
				{
					final boolean bool0 = arg0.isBoolean().booleanValue();
					final boolean bool1 = arg1.isBoolean().booleanValue();

					return (new Boolean(bool0)).compareTo(new Boolean(bool1));
				}
				else if (arg0.isObject() != null)
				{
					final JSONObject obj0 = arg0.isObject();
					final JSONObject obj1 = arg1.isObject();

					final List<String> keySet = Collections.getList(keys);

					if (keys.length == 0)
					{
						@SuppressWarnings("unchecked")
						final List<String> sorted = Collections.merge(true, obj0.keySet(), obj1.keySet());

						keySet.addAll(sorted);
					}

					for (String key : keySet)
						switch (compareVal = compare(obj0.get(key), obj1.get(key)))
						{
							case 0:
								continue;
							default:
								return compareVal;
						}

					return (obj0.size() - obj1.size());
				}
				else if (arg0.isNumber() != null)
				{
					final double num0 = arg0.isNumber().doubleValue();
					final double num1 = arg1.isNumber().doubleValue();

					return (new Double(num0)).compareTo(new Double(num1));
				}
				else if (arg0.isString() != null)
				{
					return arg0.toString().compareTo(arg1.toString());
				}

				/* Unreachable for Now */

				return compare(
						new JSONString(arg0.toString()),
						new JSONString(arg1.toString())
					);
			}
		};

		for (int i = 0; i < array.size(); i++)
			elements.add(array.get(i));

		Collections.sort(elements, comparator);

		for (int i = 0; i < elements.size(); i++)
			jsonArray.set(i, elements.get(i));

		return jsonArray;
	}

	/**
	 * Adds a primitive JSONValue to the set, or recursively adds primitive JSONValues
	 * from all nested arrays and objects and returns the set. If key is provided, that
	 * key is used throughout the traversal: values under other keys in nested objects
	 * are ignored. If key is null, all values in nested objects and arrays are added.
	 * If preserveObjects is true, nested objects that don't contain the provided key
	 * are added to the set as they are.
	 */
	public static Set<JSONValue> toJsonSet(JSONValue value, String key, boolean preserveObjects)
	{
		final RecurseOver type = (preserveObjects)
			? RecurseOver.ARRAYS
			: RecurseOver.ALL;

		return toJsonSet(value, key, type);
	}

	/**
	 * Adds a primitive JSONValue to the set, or depending on type, recursively adds
	 * primitive JSONValues from nested arrays and objects and returns the set. If key
	 * is provided, that key is used throughout the traversal: values under other keys
	 * in nested objects are ignored. If key is null, all values in nested objects and
	 * arrays are added depending on type: if type includes ARRAYS, values from arrays
	 * are added; if type includes OBJECTS, likewise but filtered by key; otherwise, if
	 * type is NONE, no recursion is done: arrays and objects are added as they are.
	 */
	private static Set<JSONValue> toJsonSet(JSONValue value, String key, RecurseOver type)
	{
		final Set<JSONValue> set = Collections.getSet();

		if (value == null)
			return set;

		else if (value instanceof JSONArray)
		{
			final JSONArray array = value.isArray();

			switch (type)
			{
				case ALL:
				case ARRAYS:

					if (array.size() > 0)
						for (int i = 0; i < array.size(); i++)
							addJsonTo(set, array.get(i), key, type);
					break;

				default:
					addJsonTo(set, array, key, type);
			}
		}
		else if (value instanceof JSONObject)
		{
			final JSONObject object = value.isObject();

			switch (type)
			{
				case ALL:
				case OBJECTS:

					if (object.containsKey(key))
						addJsonTo(set, object.get(key), key, type);

					else if (key == null && object.size() > 0)
						for (String next : object.keySet())
							addJsonTo(set, object.get(next), null, type);
					break;

				default:

					if (object.containsKey(key))
						addJsonTo(set, object.get(key), key, type);
					else
						addJsonTo(set, object, key, type);
					break;
			}
		}

		return set;
	}

	/** Converts top level key value pairs to a map, and returns it. Nested arrays and objects are ignored. */
	public static List<String> toList(JSONArray array)
	{
		if (array == null) return Collections.getList();

		int length = array.size();
		int index = 0;

		final List<String> list = Collections.getList(length);

		for (int i = 0; i < length; i++)
		{
			final JSONValue val = array.get(i);

			if (!isEmpty(val)) list.set(index++, asString(val));
		}

		return (index < length) ? list.subList(0, index) : list;
	}

	/** Converts top level key value pairs to a map, and returns it. Nested arrays and objects are ignored. */
	public static Map<String, String> toMap(JSONObject object)
	{
		final Map<String, String> map = Collections.getMap();

		if (object != null)
			for (String key : object.keySet())
			{
				final JSONValue val = object.get(key);

				if (!isEmpty(val)) map.put(key, asString(val));
			}

		return map;
	}

	/**
	 * Wraps non-empty values in a JSONArray:
	 * if value is empty, an empty array is returned;
	 * if value is an array, it is just returned as is;
	 * otherwise it is inserted at index zero, and the array is returned.
	 */
	public static JSONArray wrap(JSONValue value)
	{
		final JSONArray array = new JSONArray();

		if (isEmpty(value))
			return array;

		else if (value instanceof JSONArray)
			return value.isArray();

		array.set(0, value);

		return array;
	}

	public static JSONObject wrap(JSONValue value, EwIsKey key)
	{
		return wrap(value, key.getKey());
	}

	/**
	 * Wraps non-empty values in a JSONObject:
	 * if value is empty, object returned is empty;
	 * if value is an object with the specified key, it is returned as is;
	 * otherwise value is inserted in the object at key, which is returned.
	 */
	public static JSONObject wrap(JSONValue value, String key)
	{
		final JSONObject object = new JSONObject();

		if (isEmpty(value) || Strings.isEmpty(key))
			return object;

		else if (value instanceof JSONObject && object.containsKey(key))
			return value.isObject();

		object.put(key, value);

		return object;
	}

	/**
	 * Adds a primitive JSONValue to the set, or recursively adds primitive JSONValues
	 * from all nested arrays and objects and returns the set. Each primitive JSONValue
	 * from all nested objects and arrays are converted to a String and added to the set.
	 */
	public static Set<String> toSet(JSONValue value)
	{
		return toSet(value, (String) null);
	}

	/**
	 * Adds a String to the set, or recursively adds Strings from all nested arrays and
	 * objects and returns the set. Each primitive JSONValue from all nested objects and
	 * arrays are converted to a String and added to the set. If key is provided, that key
	 * is used throughout the traversal: values under other keys in nested objects are
	 * ignored. If null, all values in nested objects and arrays are added to the set.
	 */
	public static Set<String> toSet(JSONValue value, EwIsKey key)
	{
		final String keyVal = (key == null) ? null : key.getKey();

		return toSet(value, keyVal);
	}

	/** @see Json#toSet(JSONValue, EwIsKey) */
	public static Set<String> toSet(JSONValue value, String key)
	{
		final Set<String> set = Collections.getSet();

		if (value == null)
			return set;

		else if (value instanceof JSONArray)
		{
			final JSONArray array = value.isArray();

			if (array.size() > 0)
				for (int i = 0; i < array.size(); i++)
					addStringTo(set, array.get(i), key);
		}
		else if (value instanceof JSONObject)
		{
			final JSONObject object = value.isObject();

			if (object.containsKey(key))
				addStringTo(set, object.get(key), key);

			else if (key == null && object.size() > 0)
				for (String next : object.keySet())
					addStringTo(set, object.get(next), null);
		}

		return set;
	}

	/** Recurse over next depending on the {@link RecurseOver} type, or add it directly to the set. */
	private static Set<JSONValue> addJsonTo(Set<JSONValue> set, JSONValue next, String key, RecurseOver type)
	{
		if (next == null || next.isNull() != null)
			return set;

		else if (next instanceof JSONArray)
			switch (type)
			{
				case ALL:
				case ARRAYS:

					if (next.isArray().size() > 0)
						set.addAll(toJsonSet(next, key, type));
					break;

				default:
					set.add(next);
			}
		else if (next instanceof JSONObject)
			switch (type)
			{
				case ALL:
				case OBJECTS:

					if (next.isObject().size() > 0)
						set.addAll(toJsonSet(next, key, type));
					break;

				default:

					final JSONObject object = next.isObject();

					if (object.containsKey(key))
						set.addAll(toJsonSet(object.get(key), key, type));
					else
						set.add(object);
					break;
			}
		else
			set.add(next);

		return set;
	}

	/** Convert next to String, or iterate over the elements recursively, converting each. */
	private static Set<String> addStringTo(Set<String> set, JSONValue next, String key)
	{
		if (next instanceof JSONBoolean)
			set.add(getBoolean(next).toString());

		else if (next instanceof JSONNumber)
			set.add(getNumber(next).toString());

		else if (next instanceof JSONString)
			set.add(getString(next));

		else if (next instanceof JSONArray && next.isArray().size() > 0)
			set.addAll(toSet(next, key));

		else if (next instanceof JSONObject && next.isObject().size() > 0)
			set.addAll(toSet(next, key));

		return set;
	}

	private static String asString(JSONValue next)
	{
		if (next == null || next instanceof JSONNull)
			return Strings.NULL;

		else if (next instanceof JSONBoolean)
			return getBoolean(next).toString();

		else if (next instanceof JSONNumber)
			return getNumber(next).toString();

		else if (next instanceof JSONString)
			return getString(next);

		Logger.logWarning(
				"Returning $(0) as String: '$(1)'",
				Classes.getSimpleName(next),
				next.toString()
			);

		return Strings.EMPTY;
	}

	private enum RecurseOver
	{
		ALL,
		ARRAYS,
		OBJECTS,
		NONE
	}

}
