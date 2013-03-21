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
package com.eduworks.gwt.client.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.KeyCodes;

public class Strings
{
	public static void main(String[] args)
	{
	}

	public static final String ELLIPSIS = "...";
	public static final String EMPTY = "";
	public static final String NULL = "null";
	public static final String SPACE = " ";
	public static final char NCHAR = '\0';

	private static final int MIN_DIGIT = 48;
	private static final int MIN_LOWER = 97;
	private static final int MIN_UPPER = 65;
	private static final int MAX_DIGIT = 57;
	private static final int MAX_LOWER = 122;
	private static final int MAX_UPPER = 90;
	private static final int CHAR_DIFF = (MIN_LOWER - MIN_UPPER);

	private static final char[] UUID_CHARS = {
		'0','1','2','3','4','5','6','7','8','9',
		'A','B','C','D','E','F','G','H','I','J',
		'K','L','M','N','O','P','Q','R','S','T',
		'U','V','W','X','Y','Z',
		'a','b','c','d','e','f','g','h','i','j',
		'k','l','m','n','o','p','q','r','s','t',
		'u','v','w','x','y','z'
	};


	public static String append(String ... strings)
	{
		return append(strings, NCHAR, false);
	}

	public static String append(char delim, String ... strings)
	{
		return append(strings, delim, false);
	}

	public static String appendCamel(String ... strings)
	{
		return append(strings, NCHAR, true);
	}

	private static String append(Object[] strings, char delim, boolean camelCase)
	{
		if (strings == null || strings.length == 0) return EMPTY;

		final int arrLen = strings.length;
		final boolean delimit = (delim != NCHAR);

		final StringBuilder appended = new StringBuilder(arrLen * 32);

		String next;

		for (int i = 0;;)
		{
			if (i == arrLen) break;

			if (isEmpty(next = toString(strings[i++]))) continue;

			appended.append((camelCase) ? capitalize(next) : next);

			if (i == arrLen) break;

			if (delimit && appended.charAt(appended.length()-1) != delim)
				appended.append(delim);
		}

		return appended.toString();
	}

	public static StringBuilder appendFormat(StringBuilder string, String format, Object ... args)
	{
		return (string != null)
			? string.append(format(format, args))
			: new StringBuilder().append(format(format, args));
	}

	public static String appendPaths(String ... paths)
	{
		if (paths == null || paths.length == 0)
			return EMPTY;

		final StringBuilder pathBuilder = new StringBuilder();
		final char delim = '/';

		for (String path : paths)
		{
			if (isEmpty(path))
				continue;

			else if (pathBuilder.length() == 0)
				pathBuilder.append(path);

			else
			{
				char endsWith = pathBuilder.charAt(pathBuilder.length()-1);
				char startsWith = path.charAt(0);

				if (endsWith == delim && startsWith == delim)
					pathBuilder.append(path.substring(1));

				else if (endsWith == delim || startsWith == delim)
					pathBuilder.append(path);

				else pathBuilder.append(delim).append(path);
			}
		}

		return pathBuilder.toString();
	}

	/** Capitalizes the first letter of the string. */
	public static String capitalize(String string)
	{
		return (isEmpty(string)) ? string : capitalize(string.toCharArray());
	}

	/** Capitalizes the first character in the array. */
	public static String capitalize(final char[] chars)
	{
		if (chars == null) return null;

		if (chars.length == 0) return EMPTY;

		if (isLower(chars[0]))
			chars[0] = toUpper(chars[0]);

		return new String(chars);
	}

	public static boolean contains(String string, char ch)
	{
		return contains(string, ch, 0);
	}

	public static boolean contains(String string, char ch, int beginIndex)
	{
		if (string == null) return false;

		// Ensures negative index is not treated as zero
		if (beginIndex < 0 ) return false;

		return (string.indexOf(ch, beginIndex) != -1);
	}

	/**
	 * Approximates {@link String#format(String, Object...)}, which is not available for GWT.
	 * <p>Searches for array indices within parens preceded by a dollar sign: "$(index)" that
	 * correspond to elements in the array of object parameters provided. If index does not
	 * correspond to an argument, the dollar sign, parens and the value they delimit are just
	 * appended to the string as is.
	 * @param format	a format String containing paren-delimited array indices, preceded by
	 * 					a dollar sign: "$(index)"
	 * @param args		a series of values to insert where indices correspond to the delimited
	 * 					values in the format String
	 * @return the format with values inserted where specified
	 */
	public static String format(final String format, final Object... args)
	{
		if (isEmpty(format)) return EMPTY;

		if (args == null) return format;

		// Approximate the result length: format string + 16 character args
	    final StringBuilder sb = new StringBuilder(format.length() + (args.length*16));

	    final char escChar = '\\';
	    final char derefChar = '$';
	    final char openDelim = '(';
	    final char closeDelim = ')';

	    int cur = 0;
	    int len = format.length();
	    int open;
	    int close;

	    while (cur < len)
	    {
	        switch (open = format.indexOf(openDelim, cur))
	        {
	        	case -1:
	        		// No open paren: just append the string as is
	        		return sb.append(format.substring(cur, len)).toString();

	        	default:
	        		// Found open paren: append everything leading up to it
		            sb.append(format.substring(cur, open));

		            switch (close = format.indexOf(closeDelim, open))
		            {
		            	case -1:
			        		// No close paren: append the rest of the string
		            		return sb.append(format.substring(open)).toString();

	            		default:
	            			// Does a dollar sign precede the open paren?
	            			if (open > 0 && format.charAt(open-1) == derefChar)
	            			{
	            				// Is the dollar escaped?
	            				if (open > 1 && format.charAt(open-2) == escChar)
	            				{
            						// Remove escape and dollar sign
            						sb.setLength(sb.length() - 2);

	            					// Append escaped dollar and open, and continue from there
	            					sb.append(derefChar).append(openDelim);

	            					cur = (open + 1);

	            					continue;
	            				}
	            				else
	            				{
	            					// Parse the paren delimited index: "$(index)"
	            					final String argIdx = format.substring(open + 1, close);

	            					try
	            					{
	            						// Remove preceding dollar sign
	            						sb.setLength(sb.length() - 1);

	            						// Append the corresponding argument value
	            						sb.append(toString(args[Integer.parseInt(argIdx)]));
	            					}
	            					catch (Exception e)
	            					{
	            						// Append the dollar sign, the parens and the original delimited value
	            						sb.append(derefChar).append(openDelim).append(argIdx).append(closeDelim);
	            					}
	            				}

		            			cur = (close + 1);
	            			}
	            			else
	            			{
	            				/* No dollar sign before open: find next one and continue from there */

	            				final int nextOpen = format.indexOf(openDelim, open + 1);

	            				if (nextOpen != -1 && nextOpen < close)
									cur = nextOpen;		// Continue at next open paren before close
								else
									cur = (close + 1);	// No open before close: continue after close

	            				sb.append(format.substring(open, cur));
	            			}
		            }
	        }
	    }

	    return sb.toString();
	}

	public static String getPassword(final String password)
	{
		return getPassword(password, null);
	}

	protected static String getPassword(final String password, final String salt)
	{
		try
		{
			final MessageDigest md = MessageDigest.getInstance("MD5");

			md.reset();

			try
			{
				if (salt != null)
					md.update(salt.getBytes("ISO-8859-1"));
				md.update(password.getBytes("ISO-8859-1"));
			}
			catch (UnsupportedEncodingException uee)
			{
				if (salt != null)
					md.update(salt.getBytes("ISO8859_1"));
				md.update(password.getBytes("ISO8859_1"));
			}

			return "MD5%3A" + mdDigest(md);
		}
		catch (NoSuchAlgorithmException nsa)
		{
			return EMPTY;
		}
		catch (UnsupportedEncodingException uee)
		{
			return EMPTY;
		}
	}

	private static String mdDigest(final MessageDigest md)
	{
		if (md == null) return EMPTY;

		final byte[] bytes = md.digest();
		final int base = 16;

		final StringBuilder password = new StringBuilder();
		int bi;
		int ch;

		for (int i = 0; i < bytes.length; i++)
		{
			bi = 0xff & bytes[i];
			ch = '0' + (bi / base) % base;

			if (ch > '9') ch = 'a' + (ch - '0' - 10);

			password.append((char) ch);

			ch = '0' + bi % base;

			if (ch > '9') ch = 'a' + (ch - '0' - 10);

			password.append((char) ch);
		}

		return password.toString();
	}

	/** Generate a RFC4122 uuid, version 4. Example: "12345A67-8B9C-0123-DEFG-HIJ45678K901" */
	public static String getUuid()
	{
		final char[] uuid = new char[36];

		// Required by RFC4122
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		uuid[14] = '4';

		/*
		 * Fill in random data:
		 *	At i==19 set the high bits of clock sequence
		 *	as per RFC4122, sec. 4.1.5
		 */

		for (int i = 0; i < 36; i++)
			if (uuid[i] == 0)
			{
				final int r = (int) (Math.random() * 16);
				uuid[i] = UUID_CHARS[ (i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
			}

		return new String(uuid);
	}

	/**
	 * Generate a random uuid of the specified length.
	 * @param len the desired number of characters
	 */
	public static String getUuid(int len)
	{
		return getUuid(len, UUID_CHARS.length);
	}

	/**
	 * Generate a random uuid of the specified length, and radix.
	 * @param len the desired number of characters
	 * @param radix the number of allowable values for each character
	 */
	public static String getUuid(int len, int radix)
	{
		if (radix > UUID_CHARS.length)
			radix = UUID_CHARS.length;

		final char[] uuid = new char[len];

		for (int i = 0; i < len; i++)
			uuid[i] = UUID_CHARS[(int) (Math.random() * radix)];

		return new String(uuid);
	}

	/** Index of single character is not supported in {@link StringBuilder}. */
	public static int indexOf(StringBuilder builder, char c)
	{
		final int none = -1;

		if (builder == null) return none;

		for (int i = 0; i < builder.length(); i++)
			if (builder.charAt(i) == c)
				return i;

		return none;
	}

	public static boolean isEmpty(CharSequence value)
	{
		return (value == null || value.length() == 0);
	}

	public static boolean isEmpty(String value)
	{
		return isEmpty(value, false);
	}

	public static boolean isEmpty(String value, boolean trim)
	{
		return (value == null || value.isEmpty() || (trim && value.trim().isEmpty()));
	}

	public static boolean isEmpty(char[] chars)
	{
		return (chars == null || chars.length == 0);
	}

	public static boolean isEmpty(StringBuilder builder)
	{
		return (builder == null || builder.length() == 0);
	}

	public static boolean isEnterKey(char c)
	{
		switch (c)
		{
			case 10:
			case KeyCodes.KEY_ENTER:
				return true;

			default:
				return false;
		}
	}

	/** @return true if value is null, empty or equals "null" without regard to case */
	public static boolean isNull(String value)
	{
		return (isEmpty(value) || value.equalsIgnoreCase(NULL));
	}

	public static String join(Map<?,?> map, String delim)
	{
		if (map == null) return NULL;

		if (map.size() == 0) return EMPTY;

		return join(map.entrySet(), delim);
	}

	public static String join(Collection<?> coll, String delim)
	{
		if (coll == null) return NULL;

		if (coll.size() == 0) return EMPTY;

		return join(coll.iterator(), delim);
	}

	public static String join(Iterator<?> iter, String delim)
	{
		if (iter == null) return NULL;

		if (delim == null) delim = EMPTY;

		final StringBuilder builder = new StringBuilder();

		while (iter.hasNext())
		{
			builder.append(toString(iter.next()));

			if (!iter.hasNext()) break;

			builder.append(delim);
		}

		return builder.toString();
	}

	public static String join(String delim, Object ... array)
	{
		if (array == null) return NULL;

		if (array.length == 0) return EMPTY;

		if (delim == null) delim = EMPTY;

		final StringBuilder builder = new StringBuilder();

		for (Object value : array)
		{
			builder.append(toString(value));
			builder.append(delim);
		}

		final int len = builder.length()-1;

		return (builder.lastIndexOf(delim) == len)
			? builder.substring(0, len)
			: builder.toString();
	}

	/** Title case a word, replacing underscores with spaces, after converting it to lower case. */
	public static String toTitleCase(String string)
	{
		return toTitleCase(string, true);
	}

	/**
	 * Title case sentence, or capitalize a word: if lowerFirst is not
	 * specified, camel-cased words are parsed.
	 * @param string the {@link String} to convert
	 * @param lowerFirst if true, the string is lower-cased first
	 * @return the string with all words in it capitalized as in a title
	 */
	public static String toTitleCase(String string, boolean lowerFirst)
	{
		if (isEmpty(string)) return string;

		char[] chars = (lowerFirst)
			? toLower(string).toCharArray()
			: string.toCharArray();

		int length = chars.length;

		boolean capNext = true;
		boolean skipNext = false;
		boolean spaceBefore = false;
		boolean spaceAfter = false;

		char last;
		char next;

		StringBuilder title = new StringBuilder(length * 2);

		for (int i = 0, n = 1, o = 2; i < length; i++, n++, o++)
		{
			last = (i > 0) ? chars[i-1] : '\0';
			next = (n < length) ? chars[n] : '\0';

			switch (chars[i])
			{
				case '_':
					chars[i] = ' ';
					// Fall through as space
				case ' ':
				case '\t':
					title.append(chars[i]);
					capNext = true;
					skipNext = true;
					break;

				default:
					if (isWordChar(chars[i]))
					{
						if (capNext)
						{
							/* Capitalize only words with more than one letter, excepting 'i' */
							if (i == 0 || i == length - 1 || isWordChar(next) || chars[i] == 'i')
							{
								chars[i] = toUpper(chars[i]);
								capNext = false;
							}
						}
						else if (!skipNext)
						{
							/* Consider previous and following letter case when inserting spaces */
							if (isUpper(chars[i]))
							{
								if (isLower(last) || isLower(next))
									spaceBefore = true;
							}
							else if (isUpper(next))
								spaceAfter = true;

							skipNext = (spaceBefore || spaceAfter);
						}
						else skipNext = false;

						/* Append character and spaces before and/or after */

						if (spaceBefore) title.append(' ');

						title.append(chars[i]);

						if (spaceAfter) title.append(' ');
					}
					else // Not a letter
					{
						title.append(chars[i]);

						/* Insert space after if not a word char and next is a letter */
						if (isLetter(next)) title.append(' ');

						capNext = true;
					}

					spaceBefore = false;
					spaceAfter = false;
					break;
			}
		}

		return title.toString();
	}

	/** Breaks string into space separated words, and capitalizes each. */
	public static String toTitleCaseSimple(String string)
	{
		if (isEmpty(string)) return string;

		final StringBuilder title = new StringBuilder(string.length());
		final String[] words = string.split("\\s");

		for (int i = 0;;)
		{
			title.append(capitalize(words[i].toCharArray()));

			if (++i < words.length)
				title.append(' ');

			else break;
		}

		return title.toString();
	}

	public static boolean isDigit(char ch)
	{
		final int val = ch;
		return (MIN_DIGIT <= val && val <= MAX_DIGIT);
	}

	public static boolean isLetter(char ch)
	{
		return (isLower(ch) || isUpper(ch));
	}

	public static boolean isLower(char ch)
	{
		final int val = ch;
		return (MIN_LOWER <= val && val <= MAX_LOWER);
	}

	public static boolean isLower(String str)
	{
		if (str == null) return false;

		for (int i = 0; i < str.length(); i++)
			if (isUpper(str.charAt(i)))
				return false;

		return true;
	}

    public static boolean isUpper(char ch)
    {
        final int val = ch;
        return (MIN_UPPER <= val && val <= MAX_UPPER);
    }

	/**
	 * @return	true if ch is whitespace according to {@link Character#isWhitespace(char)}
	 * 			as of java 1.6, but excludes localization for performance.
	 */
	public static boolean isWhiteSpace(char ch)
	{
		switch (ch)
		{
			case ' ':
			case '\f':
			case '\n':
			case '\r':
			case '\t':
			case '\u000B':	// Vertical Tabulation
			case '\u001C':	// File Separator
			case '\u001D':	// Group Separator
			case '\u001E':	// Record Separator
			case '\u001F':	// Unit Separator
				return true;

			/* non-breaking */
			case '\u00A0':
			case '\u2007':
			case '\u202F':

			/* non-white */
			default: return false;
		}
	}

	/** @return true if str is not empty and contains only whitespace chars. */
	public static boolean isWhitespace(String str)
	{
    	if (isEmpty(str)) return false;

    	for (int i = 0; i < str.length(); i++)
    		if (!isWhiteSpace(str.charAt(i))) return false;

        return true;
	}

    /** @return true if ch is a character that may appear in a single word. */
	public static boolean isWordChar(char ch)
	{
		switch (ch)
		{
			case '-':
			case '\'':
				return true;
			default:
				return isLetter(ch);
		}
	}

	public static boolean endsWith(String string, char c)
	{
		return (!isEmpty(string) && string.charAt(string.length()-1) == c);
	}

	public static boolean startsWith(String string, char c)
	{
		return (!isEmpty(string) && string.charAt(0) == c);
	}

	public static boolean endsWith(String string, String suffix)
	{
		return !isEmpty(string) && string.endsWith(suffix);
	}

	public static boolean startsWith(String string, String prefix)
	{
		return !isEmpty(string) && string.startsWith(prefix);
	}

	/** Parses camel, inserting a space between segments. */
	public static String parseCamel(String camel)
	{
		return parseCamel(camel, NCHAR);
	}

	/** Parses camel, inserting delim between segments. */
	public static String parseCamel(String camel, char delim)
	{
		if (isEmpty(camel)) return camel;

		if (delim == NCHAR) delim = ' ';

		final StringBuilder parsed = new StringBuilder(camel);

		char last = NCHAR;
		char next;

		for (int i = 0; i < parsed.length(); i++)
		{
			if ((next = parsed.charAt(i)) == delim)
			{
				last = next;
				continue;
			}
			else if (!isLetter(next))
				parsed.setCharAt(i, delim);

			else if (isUpper(next))
			{
				parsed.setCharAt(i, toLower(next));

				if (i > 0 && delim != last && isLetter(last) && !isUpper(last))
					parsed.insert(i++, delim);
			}

			last = next;
		}

		return parsed.toString();
	}

	/** Parses camel cased segments and populates an array fit to size. */
	public static String[] splitCamel(String camel)
	{
		if (isEmpty(camel)) return new String[0];

		if (isLower(camel)) return new String[] { camel };

		return parseCamel(camel, ' ').split("\\s+");
	}

	public static String toString(final Object object)
	{
		if (object == null)
			return EMPTY;

		else if (object instanceof Class)
			return Classes.getSimpleName((Class<?>)object);

		else if (object instanceof Throwable)
			return ((Throwable)object).getMessage();

		else if (object instanceof Collection)
			return join((Collection<?>)object, ",");

		else if (object instanceof Entry)
			return format("$(0):$(1)",
					toString(((Entry<?,?>)object).getKey()),
					toString(((Entry<?,?>)object).getValue())
				);

		else if (object instanceof Iterator)
			return join((Iterator<?>)object, ",");

		else if (object instanceof Map)
			return join((Map<?,?>)object, ",");

		else if (object.getClass().isArray())
			try
			{
				return append((Object[])object, ',', false);
			}
			catch (Exception e)
			{
				Logger.logError("Could not convert $(0) to string.", object.getClass());
			}

		return object.toString();
	}

    public static char toLower(char ch)
    {
    	final int val = ch;
    	return (char) ((isUpper(ch)) ? (val + CHAR_DIFF) : val);
    }

   public static String toLower(String str)
    {
    	if (isEmpty(str)) return str;

    	char[] chars = new char[str.length()];

    	for (int i = 0; i < str.length(); i++)
    		chars[i] = toLower(str.charAt(i));

        return new String(chars);
    }

    public static char toUpper(char ch)
    {
    	final int val = ch;
    	return (char) ((isLower(ch)) ? (val - CHAR_DIFF) : val);
    }

    public static String toUpper(String str)
    {
    	if (isEmpty(str)) return str;

    	char[] chars = new char[str.length()];

    	for (int i = 0; i < str.length(); i++)
    		chars[i] = toUpper(str.charAt(i));

        return new String(chars);
    }

    public static String trim(String str)
    {
    	return (isEmpty(str)) ? str : str.trim();
    }

	public static <T extends CharSequence> String trim(T str, final char c)
    {
    	if (isEmpty(str)) return EMPTY;

    	int first = 0;
    	int last = str.length();

    	while (first < last && str.charAt(first) == c)
    	    first++;

    	while (first < last && str.charAt(last - 1) == c)
    	    last--;

    	return (first > 0 || last < str.length())
    		? str.subSequence(first, last).toString()
    		: str.toString();
    }

    public static <T extends CharSequence> String trimEnd(T str, final char c)
    {
    	if (isEmpty(str)) return EMPTY;

    	int last = str.length();

    	while (0 < last && str.charAt(last - 1) == c) last--;

    	return (last < str.length()) ? str.subSequence(0, last).toString() : str.toString();
    }

    public static <T extends CharSequence> String trimStart(T str, final char c)
    {
    	if (isEmpty(str)) return EMPTY;

    	int first = 0;
    	int last = str.length();

    	while (first < last && str.charAt(first) == c) first++;

    	return (first > 0) ? str.subSequence(first, last).toString() : str.toString();
    }

}
