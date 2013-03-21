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

import com.eduworks.gwt.client.EwIsAction;
import com.eduworks.gwt.client.EwIsActor;

public final class Actions
{
	public static void execute(EwIsActor actor, Object ... contextArray)
	{
		execute(actor, null, contextArray);
	}

	public static void execute(EwIsActor actor, EwIsAction action, Object ... contextArray)
	{
		if (actor != null)
			if (action == null)
				actor.execute(contextArray);
			else
				actor.execute(action, contextArray);
	}

}
