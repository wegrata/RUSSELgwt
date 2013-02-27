package com.eduworks.gwt.client.util;

import java.util.List;

public final class Arrays
{
	public static String[] attach(String[] ... arrays)
	{
		int length = 0;

		for (String[] array : arrays)
			length += array.length;

		final String[] result = new String[length];

		int position = 0;

		for (String[] array : arrays)
			for (String string : array)
				result[position++] = string;

		return result;
	}

	/**
	 * Copies starting at the first valid index after from, to the last valid index before to.
	 * @param original the array to copy into a new {@link List}
	 * @param from the zero-based index (inclusive) at which to start copying
	 * @param to the zero-based index (inclusive) at which to end copying
	 * @return a list containing array elements between to and from inclusive
	 */
	public static <T, V extends T> List<T> copyOfRange(T[] original, int from, int to)
	{
    	if (original == null) return Collections.getList();


		if (to < from)
			Logger.logError("Invalid range: from=$(0), to=$(1)", from, to);
		else
		{
			if (from < 0)
				from = 0;

			if (to >= original.length)
				to = (original.length - 1);

			final int newLength = ((to - from) + 1);
			final List<T> list = Collections.getList(newLength);

			final int limit = Math.min(original.length - from, newLength);

			for (int i = from, j = 0; j < limit; i++)
				list.add(j++, original[i]);

			return list;
		}

		return Collections.getList(original);
	}

	public static boolean isEmpty(Object[] array)
	{
		return (array == null || array.length == 0);
	}

}
