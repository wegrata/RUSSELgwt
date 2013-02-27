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
