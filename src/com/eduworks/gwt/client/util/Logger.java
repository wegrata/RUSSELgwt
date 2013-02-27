package com.eduworks.gwt.client.util;

import com.eduworks.gwt.client.exception.EwException;

public final class Logger
{
	public static String debug()
	{
		return log(LogMode.DEBUG, Strings.EMPTY);
	}

	public static String debug(Object value)
	{
		return log(LogMode.DEBUG, value);
	}

	public static String debug(String format, Object ... args)
	{
		return log(LogMode.DEBUG, format, args);
	}

	public static String logError()
	{
		return log(LogMode.ERROR, Strings.EMPTY);
	}

	public static String logError(Object value)
	{
		return log(LogMode.ERROR, value);
	}

	public static String logError(String format, Object ... args)
	{
		final String error = log(LogMode.ERROR, format, args);

		/* Loop over args and print out first stack trace */

		if (args != null && args.length > 0)
			for (int i = 0; i < args.length; i++)
				if (args[i] instanceof Throwable)
					EwException.printStackTrace((Throwable)args[i]);

		return error;
	}

	public static String logInfo()
	{
		return log(LogMode.INFO, Strings.EMPTY);
	}

	public static String logInfo(Object value)
	{
		return log(LogMode.INFO, value);
	}

	public static String logInfo(String format, Object ... args)
	{
		return log(LogMode.INFO, format, args);
	}

	public static String logWarning()
	{
		return log(LogMode.WARN, Strings.EMPTY);
	}

	public static String logWarning(Object value)
	{
		return log(LogMode.WARN, value);
	}

	public static String logWarning(String format, Object ... args)
	{
		return log(LogMode.WARN, format, args);
	}

	public static String log(LogMode mode, Object value)
	{
		final String error = (value == null)
			? nativeLog(mode.prefix(Strings.NULL))
			: nativeLog(mode.prefix(Strings.toString(value)));

		if (value instanceof Throwable)
			EwException.printStackTrace((Throwable)value);

		return error;
	}

	public static String log(LogMode mode, String format, Object ... args)
	{
		final String value = Strings.format(format, args);

		return (value == null)
			? nativeLog(mode.prefix(Strings.NULL))
			: nativeLog(mode.prefix(value.toString()));
	}

	private static native String nativeLog(String message)/*-{

        console.log(message);
        return message;

	}-*/;

	/** Encapsulates differences between log modes. */
	private enum LogMode
	{
		DEBUG("DEBUG: "),
		ERROR("ERROR: "),
		INFO("INFO: "),
		WARN("WARN: ")
		;

		public final String value;

		private LogMode(String value)
		{
			this.value = value;
		}

		public String prefix(String message)
		{
			return new StringBuilder(message.length() + 16)
					.append(this.value)
					.append(message)
					.toString();
		}

	}
}
