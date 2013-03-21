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
