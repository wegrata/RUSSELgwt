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
