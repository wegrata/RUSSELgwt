package com.eduworks.gwt.client.reflection;

import com.eduworks.gwt.ReflectionGenerator;
import com.google.gwt.core.client.GWT;


/**
 * This class defines the interface for a class returned by {@link GWT#create(Class)}. It
 * is referenced to instantiate any {@link Instantiable} non-enum, non-interface class with
 * either an empty or a single-string constructor.
 *
 * @author dharvey
 *
 * @see ReflectionGenerator
 * @see ReflectionExample
 */
public interface Reflection
{
	/**
	 * Instantiates any class implementing the {@link Instantiable} interface
	 * @param clazz the class to instantiate
	 * @see #instantiate(Class, String)
	 */
	public <T, V extends T> T instantiate(Class<V> clazz);

	/**
	 * Instantiates any class implementing the {@link Instantiable} interface, with a single string param
	 * @param clazz the class to instantiate
	 * @param param a String parameter to send to the class's constructor
	 * @see ReflectionGenerator#generate(com.google.gwt.core.ext.TreeLogger, com.google.gwt.core.ext.GeneratorContext, String)
	 */
	public <T, V extends T> T instantiate(Class<V> clazz, String param);
}
