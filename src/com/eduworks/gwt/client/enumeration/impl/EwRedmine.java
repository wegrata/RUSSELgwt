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
