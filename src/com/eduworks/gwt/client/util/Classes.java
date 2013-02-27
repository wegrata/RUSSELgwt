package com.eduworks.gwt.client.util;

import com.eduworks.gwt.ReflectionGenerator;
import com.eduworks.gwt.client.reflection.Instantiable;
import com.eduworks.gwt.client.reflection.Reflection;
import com.eduworks.gwt.client.ui.EwIsWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;

public final class Classes
{
	private static final String EMPTY = Strings.EMPTY;
	private static final String NULL = Strings.NULL;

	/**
	 * Instantiates any class implementing the {@link Instantiable} interface with a single string param,
	 * and stores the form that launched it by calling {@link EwIsWidget#setInstantiatedBy(EwIsWidget)}
	 * before returning the newly instantiated class.
	 * @see #getInstance(Class, String)
	 * @see EwIsWidget#setInstantiatedBy(EwIsWidget)
	 * @see Reflection#instantiate(Class, String)
	 */
	public static <T extends EwIsWidget> T getInstance(Class<T> instantiableClass, String param, EwIsWidget instantiatedBy)
	{
		T widget = getInstance(instantiableClass, param);

		widget.setInstantiatedBy(instantiatedBy);

		return widget;
	}

	/**
	 * Instantiates any class implementing the {@link Instantiable} interface
	 * @see #getInstance(Class, String)
	 * @see Reflection#instantiate(Class)
	 */
	public static <T extends Instantiable> T getInstance(Class<T> instantiableClass)
	{
		return getInstance(instantiableClass, (String) null);
	}

	/**
	 * Since reflection is not supported directly by GWT, this method instantiates and initializes any class implementing
	 * the {@link Instantiable} interface, with an optional single string param.
	 * @see Reflection#instantiate(Class, String)
	 * @see ReflectionGenerator
	 */
	public static <T extends Instantiable> T getInstance(Class<T> instantiableClass, String param)
	{
		// REFLECTION NOT SUPPORTED BY GWT: this approximates instantiableClass.newInstance()
		return ((Reflection) GWT.create(Reflection.class)).instantiate(instantiableClass, param);
	}

	public static <T extends JSONValue> T getJsonInstance(Class<T> jsonClass)
	{
		return getJsonInstance(jsonClass, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends JSONValue> T getJsonInstance(Class<T> jsonClass, JavaScriptObject jso)
	{
		if (jsonClass == JSONArray.class)
			return (jso == null) ? (T) new JSONArray() : (T) new JSONArray(jso);

		if (jsonClass == JSONObject.class)
			return (jso == null) ? (T) new JSONObject() : (T) new JSONObject(jso);

		return null;
	}

	/**
	 * Returns the package for the class of the object passed in, or "null"
	 * @see #getSimpleName(Class)
	 */
	public static String getPackage(Object object)
	{
		return (object != null)
			? getPackage(object.getClass())
			: NULL;
	}

	/** @return the equivalent of {@link Class#getPackage()} since GWT doesn't support it. */
	public static String getPackage(Class<?> clazz)
	{
		final String className = (clazz == null) ? NULL : clazz.getName();

		return (className != null)
			? className.substring(0, className.lastIndexOf('.'))
			: EMPTY;
	}

	/**
	 * Returns the simple class name of the object passed in, or "null"
	 * @see #getSimpleName(Class)
	 */
	public static String getSimpleName(Object object)
	{
		return (object != null)
			? getSimpleName(object.getClass())
			: NULL;
	}

	/** @return the equivalent of {@link Class#getSimpleName()} since GWT doesn't support it. */
	public static String getSimpleName(Class<?> clazz)
	{
		final String className = (clazz == null) ? NULL : clazz.getName();

		return (className != null)
			? className.substring(className.lastIndexOf('.') + 1)
			: EMPTY;
	}

	/** @return true if "to" is assignable from "from"; interfaces are not considered */
	public static boolean isAssignable(Class<?> from, Class<?> to)
	{
		if (from == null || to == null) return false;

		if (from.equals(to)) return true;

		Class<?> superClass = from.getSuperclass();

		while (true)
		{
			if (superClass == null) break;

			if (superClass == Widget.class) break;

			if (superClass.equals(to)) return true;

			superClass = superClass.getSuperclass();
		}

		return false;
	}

	public static boolean isRelated(Class<?> from, Class<?> to)
	{
		if (from == null || to == null) return false;

		if (isAssignable(from, to)) return true;

		return getPackage(to).equals(getPackage(from));
	}

}
