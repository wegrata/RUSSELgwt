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
import com.eduworks.gwt.client.util.Classes;

/**
 * An interface that defines a reflectively instantiable class. All implementing classes may
 * be instantiated reflectively by implementations of the generated {@link Reflection} class,
 * and must have at least a default constructor, and optionally, one that takes a single String
 * parameter. Enums and abstract classes (of course) will not be instantiable.
 *
 * <p>For more on the {@link Reflection} class, see {@link ReflectionGenerator}</p>
 * <p>For an example of what is generated, see {@link ReflectionExample}</p>
 *
 * @author dharvey
 *
 * @see Classes#getInstance(Class)
 */
public interface Instantiable
{

}
