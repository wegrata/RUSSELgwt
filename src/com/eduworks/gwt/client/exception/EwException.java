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
package com.eduworks.gwt.client.exception;

import com.eduworks.gwt.client.util.Logger;
import com.eduworks.gwt.client.util.Strings;

public class EwException extends Exception
{
	private static final long serialVersionUID = 1L;

	public static void printStackTrace(final Throwable thrown)
	{
		if (thrown == null) return;

		final StackTraceElement[] trace = thrown.getStackTrace();

		for (StackTraceElement ele : trace)
			Logger.logError(ele);
	}

	public EwException(String message)
	{
		super(message);

		Logger.logError("$(0) Exception: $(1)", getProductName(), this);
	}

	public EwException(String format, Object ... args)
	{
		super(Strings.format(format, args));

		Logger.logError("$(0) Exception: $(1)", getProductName(), this);
	}

	public String getProductName()
	{
		return "Eduworks";
	}

}
