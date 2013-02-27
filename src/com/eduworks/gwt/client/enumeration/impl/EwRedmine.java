package com.eduworks.gwt.client.enumeration.impl;

import com.eduworks.gwt.client.enumeration.EwIsEnum;
import com.eduworks.gwt.client.util.Strings;

public enum EwRedmine implements EwIsEnum
{
	/** No default: identifies the Redmine project id for this project. */
	PROJECT("project_id", Strings.NULL),

	/** The value for this is "apurviance" by default. */
	ASSIGNED_TO("assigned_to_id", "10"),

	/** Not necessarily used: defined per project. */
	CAT_ISSUE("category_id", "1"),
	/** Not necessarily used: defined per project. */
	CAT_SUGGEST("category_id", "2"),
	/** Not necessarily used: defined per project. */
	CAT_OTHER("category_id", "3"),

	/** The value for this is "Immediate" by default. */
	PRIORITY("priority_id", "10"),

	/** The value for this is "New" by default. */
	STATUS("status_id", "1"),

	/** Must be set dynamically depending on issue. */
	SUBJECT("subject", Strings.NULL),

	/** Must be set dynamically with user input. */
	DESCRIPTION("description", Strings.NULL),

	/**
	 * Not necessarily used. This comprises a level of detail that may not be needed.
	 * @see <a href="http://www.redmine.org/projects/redmine/wiki/Rest_api">the Redmine API</a>
	 */
	CUSTOM("custom_field_values", Strings.NULL)
	;

	public static final String KEY = "key";
	public static final String URL = "http://redmine.eduworks.com/redmine/issues.json";

	public final String key;
	public final String value;

	private EwRedmine(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public String getName()
	{
		return name().toLowerCase();
	}

}
