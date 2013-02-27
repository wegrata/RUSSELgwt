package com.eduworks.gwt.client;

import com.eduworks.gwt.client.util.Logger;

/** A class to provide a flexible and anonymous implementation for action execution. */
public abstract class EwSimpleActor implements EwIsActor
{
	@Override
	public void execute(Object ... contextArray)
	{
		Logger.logWarning("Execute not implemented: $(0)", this.getClass());
	}

	@Override
	public <T extends EwIsAction> void execute(T action, Object ... contextArray)
	{
		execute(contextArray);
	}

}
