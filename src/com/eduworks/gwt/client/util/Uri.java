package com.eduworks.gwt.client.util;

import java.util.List;

import com.eduworks.gwt.client.enumeration.impl.EwUrl;
import com.google.gwt.http.client.URL;

public final class Uri
{
	public static final String DEFAULT_PROTOCOL = "http://";

	public static final String FAVICON_FMT = "http://www.google.com/s2/favicons?domain=$(0)";
	public static final String FAVICON_URL = "http://www.google.com/s2/favicons";

	public static final char SEPARATOR = '/';

	private static final String URL_PROTOCOL= "((http|https)://)";
	private static final String URL_DOMAIN	= "(www\\.)?([\\w\\-]+(\\.[\\w\\-]+|[a-zA-Z]{2,})*)";
	private static final String URL_PORT	= "(:[\\d]+)";
	private static final String URL_PATH	= "(/[~\\w()\\-\\.]*)";
	private static final String URL_FILE	= "(/[~\\w()'\\:\\-\\.,]+)";
	private static final String URL_PARAMS	= "(/?[?#].*)";

	private static final String EMAIL_LOCAL	= "[\\w.\\-!#$%&'*+-/=?^_`{|}~]+";
	private static final String EMAIL_DOMAIN= "[\\w\\-\\.]+\\.[A-Za-z]{2,4}";

	private static final String EMAIL_REGEX		= Strings.format(
			"^$(0)@$(1)$", EMAIL_LOCAL, EMAIL_DOMAIN
		);

	private static final String URL_PROTO_REGEX	= Strings.format("^$(0)", URL_PROTOCOL);

	private static final String URL_PATH_REGEX	= Strings.format(
			"$(0)*$(1)?$(2)*$", URL_PATH, URL_FILE, URL_PARAMS
	);
	private static final String URL_REGEX		= Strings.format(
			"^$(0)?$(1)$(2)?$(3)*$(4)?$(5)*$",
			URL_PROTOCOL, URL_DOMAIN, URL_PORT, URL_PATH, URL_FILE, URL_PARAMS
		);


	/** Adds the default protocol: "http://", to a url with no protocol specified */
	public static String addProtocolTo(String url)
	{
		if (url == null)
			return Strings.NULL;

		if (url.matches(Strings.format("^$(0).*", URL_PROTOCOL)))
			return url;

		return new StringBuilder(url.length() + 8)
			.append(DEFAULT_PROTOCOL)
			.append(url).toString();
	}

	/** Adds a randomized parameter to the url to prevent caching of web content. */
	public static String addRandomParamTo(String url)
	{
		return addRandomParamTo(new StringBuilder(url)).toString();

	}

	/**
	 * Adds a randomized parameter to the {@link StringBuilder} url to prevent caching of web content.
	 * @return the {@link StringBuilder} passed in
	 */
	public static StringBuilder addRandomParamTo(StringBuilder url)
	{
		if (Strings.isEmpty(url)) return url;

		final boolean noParams = (Strings.indexOf(url, '?') == -1);

		return url
			.append((noParams) ? '?' : '&')
			.append(EwUrl.RANDOM_STRING).append('=')
			.append(System.currentTimeMillis());
	}

	/** Append path segments to the specified url string. */
	public static String appendPath(String ... path)
	{
		if (Arrays.isEmpty(path)) return Strings.EMPTY;

		return appendPathTo(new StringBuilder(256), path).toString();
	}

	/** Append path segments to the specified url string. */
	public static String appendPathTo(String url, String ... path)
	{
		return appendPathTo(new StringBuilder(url), path).toString();
	}

	/** Append path segments to the specified {@link StringBuilder} url, and return it. */
	public static StringBuilder appendPathTo(StringBuilder url, String ... path)
	{
		if (url != null)
			for (String segment : path)
				if (Strings.isEmpty(segment))
					continue;
				else if (url.length() == 0 || url.charAt(url.length()-1) == SEPARATOR)
					url.append(Strings.trim(segment, SEPARATOR));
				else
					url.append(SEPARATOR).append(Strings.trim(segment, SEPARATOR));

		return url;
	}

	/** Add parameters to the provided url: params are read as follows: key1, val1, key2, val2... */
	public static String buildUrl(String url, String ... parameters)
	{
		return buildUrl(new StringBuilder(url), parameters).toString();
	}

	/**
	 * Add parameters to the provided {@link StringBuilder} url:
	 * params are read as follows: key1, val1, key2, val2...
	 * @return the {@link StringBuilder} passed in
	 */
	public static StringBuilder buildUrl(StringBuilder url, String ... parameters)
	{
		if (parameters.length > 0)
			for (int i = 0; i < parameters.length; i++)
			{
				switch (i)
				{
					case 0:
						url.append('?');
						break;
					default:
						if (i % 2 == 0) {
							url.append('&');
						} else {
							url.append('=');
						}
				}

				url.append(parameters[i]);
			}

		return url;
	}

	public static String decode(String encodedValue)
	{
		if (encodedValue == null) return null;

		return URL.decodeQueryString(encodedValue);
	}

	public static String encode(String decodedValue)
	{
		if (decodedValue == null) return null;

		return URL.encodeQueryString(decode(decodedValue));
	}
	
	public static final native String encodeQuery(String s) /*-{
	return encodeURIComponent(s);
}-*/;

	public static boolean isEncoded(String value)
	{
		if (value == null) return false;

		return value.equals(decode(value));
	}

	public static String getFaviconFor(String url)
	{
		if (!isValidUrl(url)) url = Strings.EMPTY;

		return addRandomParamTo(Strings.format(FAVICON_FMT, parseDomain(url, true)));
	}

	/**
	 * Validate an email address by checking it against a reliable regex.
	 * @return true if the address is valid, false otherwise
	 */
	public static boolean isValidEmail(String emailAddr)
	{
		if (Strings.isEmpty(emailAddr)) return false;

		return emailAddr.matches(EMAIL_REGEX);
	}

	/**
	 * Validate a url by checking it against a reliable regex.
	 * @return true if the address is valid, false otherwise
	 */
	public static boolean isValidUrl(String url)
	{
		if (Strings.isEmpty(url)) return false;

 		return url.matches(URL_REGEX);
	}

	/** Removes everything after the domain in the url. */
	public static String parseDomain(String url)
	{
		return parseDomain(url, false);
	}

	/** Removes everything after the domain in the url, and optionally the protocol (http or https). */
	public static String parseDomain(String url, boolean removeProtocol)
	{
		if (url == null) return Strings.EMPTY;

		final String noProtocol = url.replaceAll(URL_PROTO_REGEX, Strings.EMPTY);
		final String protocol = url.substring(0, url.length() - noProtocol.length());

		url = noProtocol.replaceAll(URL_PATH_REGEX, Strings.EMPTY);

		return (removeProtocol) ? url : protocol + url;
	}

	/** @return the url minus the base, minus the parameters */
	public static String parsePath(String url)
	{
		if (Strings.isEmpty(url)) return Strings.EMPTY;

		final String base = parseDomain(url);

		return parsePath(url, base.length());
	}

	/** @return the url minus the base, minus the parameters */
	public static String parsePath(String base, String url)
	{
		if (url == null) return Strings.EMPTY;

		if (base == null) return url;

		if (url.startsWith(base))
			return parsePath(url, base.length());
		else
		{
			Logger.logWarning("Base is not compatible with URL");
			Logger.logWarning("Base: $(0)", base);
			Logger.logWarning("Full: $(0)", url);

			return Strings.EMPTY;
		}
	}

	/** @return the url minus the base, minus the parameters */
	private static String parsePath(String url, int startAt)
	{
		final int fullUrlLen = url.length();

		if (fullUrlLen <= startAt) return Strings.EMPTY;

		final StringBuilder path = new StringBuilder(fullUrlLen - startAt);

		PATH_LOOP:
		for (int i = startAt; i < fullUrlLen; i++)
			switch (url.charAt(i))
			{
				case '?': break PATH_LOOP;

				default: path.append(url.charAt(i));
			}

		return (path.charAt(0) == SEPARATOR) ? path.substring(1) : path.toString();
	}

	/** @return the path components, result of splitting by "/". */
	public static String[] splitPath(String path)
	{
		if (Strings.isEmpty(path)) return new String[0];

		final List<String> result = Collections.getList();
		final int length = path.length();

		int begin = 0;

		for (int i = 0; i < length; i++)
			if (path.charAt(i) == SEPARATOR)
			{
				result.add(path.substring(begin, i));
				begin = i + 1;
			}

		if (begin < length)
			result.add(path.substring(begin));

		return result.toArray(new String[result.size()]);
	}

	/**
	 * Converts list to a parameter array to follow a url name assignment:
	 * <p><code>http://url.com?name=</code>
	 * <p>such that
	 * <p><code>val1&name=val2&name=val3</code>
	 * <p>is returned.
	 */
	public static String toUrlParams(final String name, final List<String> list)
	{
		if (list == null) return Strings.EMPTY;

		final StringBuilder urlParams = new StringBuilder(name.length() * list.size());

		for (int i = 0; i < list.size(); i++)
		{
			if (!Strings.isEmpty(urlParams))
				urlParams.append('&').append(name).append('=');

			urlParams.append(list.get(i));
		}

		return urlParams.toString();
	}

}
