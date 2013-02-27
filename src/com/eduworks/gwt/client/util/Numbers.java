package com.eduworks.gwt.client.util;

public final class Numbers
{
	public static double parseDouble(String number)
	{
		try
		{
			return Double.parseDouble(number);
		}
		catch(NumberFormatException nfe)
		{
			Logger.logWarning("String is not a number: $(0)", number);
		}
		catch(Exception e)
		{
			Logger.logError("Unhandled Exception while parsing: $(0)", number);
		}

		return Double.NaN;
	}

	public static int parseInt(String number)
	{
		try
		{
			return Integer.parseInt(number);
		}
		catch(NumberFormatException nfe)
		{
			Logger.logWarning("String is not a number: $(0)", number);
		}
		catch(Exception e)
		{
			Logger.logError("Unhandled Exception while parsing: $(0)", number);
		}

		return 0;
	}

}
