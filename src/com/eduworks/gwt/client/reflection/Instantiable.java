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
