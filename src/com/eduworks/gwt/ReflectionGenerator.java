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

package com.eduworks.gwt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.eduworks.gwt.client.reflection.Instantiable;
import com.eduworks.gwt.client.reflection.Reflection;
import com.eduworks.gwt.client.reflection.ReflectionExample;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JConstructor;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;


/**
 * A class run by the GWT compiler, that generates the source code for an obfuscated class,
 * ReflectionImpl, that implements the {@link Reflection} interface. For an example of the
 * output, see {@link ReflectionExample}.
 * <p>A list of non-enum, non-interface, {@link Instantiable} classes is populated during
 * compile time, and used to generate the obfuscated implementation of {@link Reflection}.
 * To include a class in this list, it must implement {@link Instantiable}, and exist in
 * a subfolder of the package: com.eduworks.product.ucaster.client.*
 * </p>
 * <p>Then when {@link GWT#create(Class)} is called with the {@link Reflection} class as a
 * parameter, it will return the one generated here, and any of the following methods may
 * be called to reflectively instantiate the class specified:
 * <ul>
 * <li>{@link Reflection#instantiate(Class)} to reference an empty constructor</li>
 * <li>{@link Reflection#instantiate(Class, String)} to reference a single-string constructor</li>
 * </ul>
 * </p>
 *
 * @author dharvey
 *
 * @see Generator
 * @see #generate(TreeLogger, GeneratorContext, String)
 * @see #printFactoryMethod(List, SourceWriter)
 */
public class ReflectionGenerator extends Generator
{
	private final static String genPackageName = "com.eduworks.product.ucaster.client.reflection";
	private final static String genClassName = "ReflectionImpl";

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException
	{
		@SuppressWarnings("unused")
		final PropertyOracle propertyOracle = context.getPropertyOracle();
		final TypeOracle oracle = context.getTypeOracle();

		final JClassType instantiableType = oracle.findType(Instantiable.class.getName());
		final List<JClassType> classes = new ArrayList<JClassType>();

		// Add only instantiable classes to list
		for (JClassType classType : oracle.getTypes())
			if (!classType.equals(instantiableType) && classType.isAssignableTo(instantiableType))
				classes.add(classType);

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(genPackageName, genClassName);
		composer.addImplementedInterface(Reflection.class.getCanonicalName());

		PrintWriter printWriter = context.tryCreate(logger, genPackageName, genClassName);

		if (printWriter != null) {
			SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

			// Create constructor for ReflectionImpl
			sourceWriter.println();
			sourceWriter.println("ReflectionImpl()");
			sourceWriter.println("{");
			sourceWriter.println("}");

			// Create source code for ReflectionImpl
			printFactoryMethod(classes, sourceWriter);

			sourceWriter.commit(logger);
		}

		return composer.getCreatedClassName();
	}

	/**
	 * Generates the source code for a class ReflectionImpl. This method is called by
	 * the overridden method {@link #generate(TreeLogger, GeneratorContext, String)}
	 * with a list of classes instantiable by a call to {@link GWT#create(Class)}.
	 * @param classes the list of classes to make instantiable through ReflectionImpl
	 * @param sourceWriter the source code writer for ReflectionImpl
	 */
	protected void printFactoryMethod(List<JClassType> classes, SourceWriter sourceWriter)
	{
		sourceWriter.println();

		/* Create non-string instantiate method to call string instantiate method */

		sourceWriter.println("public <T, V extends T> T instantiate(Class<V> clazz)");
		sourceWriter.println("{");
		sourceWriter.indent();
		sourceWriter.println("return (T) instantiate(clazz, (String) null);");
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.println();
		sourceWriter.println();

		/* Create string instantiate method to optionally call string constructors */

		sourceWriter.println("public <T, V extends T> T instantiate(Class<V> clazz, String param)");
		sourceWriter.println("{");

		for (JClassType classType : classes)
		{
			if (classType.isAbstract() || classType.isEnum() != null) continue;

			final boolean hasEmptyConstructor = hasEmptyConstructor(classType);
			final boolean hasStringConstructor = hasStringConstructor(classType);

			if (!hasEmptyConstructor && !hasStringConstructor) continue;

			sourceWriter.indent();
			sourceWriter.println("if (clazz.getName().endsWith(\"." + classType.getName() + "\"))");
			sourceWriter.println("{");
			sourceWriter.indent();

			if (hasEmptyConstructor && hasStringConstructor)
			{
				sourceWriter.println("return (param == null)");
				sourceWriter.indent();

				// No string parameter to call constructor with
				sourceWriter.println("? (T) new " + classType.getQualifiedSourceName() + "()");
				// Call string constructor with a non-null string
				sourceWriter.println(": (T) new " + classType.getQualifiedSourceName() + "(param);");

				sourceWriter.outdent();
			}
			else if (hasEmptyConstructor)
			{
				// No string constructor to call for classType
				sourceWriter.println("return (T) new " + classType.getQualifiedSourceName() + "();");
			}
			else if (hasStringConstructor)
			{
				// Call string constructor with a non-null string
				sourceWriter.println("return (T) new " + classType.getQualifiedSourceName() + "(param);");
			}

			sourceWriter.outdent();
			sourceWriter.println("}");
			sourceWriter.outdent();
			sourceWriter.println();
		}

		sourceWriter.indent();
		sourceWriter.println("return (T) null;");
		sourceWriter.outdent();
		sourceWriter.println();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println();
	}

	/** Checks a class type for a visible empty constructor. */
	protected boolean hasEmptyConstructor(JClassType classType)
	{
		for (JConstructor constructor : classType.getConstructors())
			if (constructor.getParameters().length == 0)
				return constructor.isPublic();

		return false;
	}

	/** Checks a class type for a single String constructor. */
	protected boolean hasStringConstructor(JClassType classType)
	{
		for (JConstructor constructor : classType.getConstructors())
			if (isStringConstructor(constructor)) return true;

		return false;
	}

	/** Checks a constructor's parameters to see if it is a single String constructor. */
	protected boolean isStringConstructor(JConstructor constructor)
	{
		if (constructor.getParameters().length != 1) return false;

		final JType paramType = constructor.getParameters()[0].getType();

		return (String.class.getCanonicalName().equals(paramType.getQualifiedSourceName()));
	}
}
