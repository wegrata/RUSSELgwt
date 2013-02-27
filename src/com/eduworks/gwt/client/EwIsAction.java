package com.eduworks.gwt.client;

/** Identifies classes/enums identifying {@link EwIsActor} actions. */
public interface EwIsAction
{
	/** @return a user-readable name of the action to execute */
	public String getActionName();

	/** @return a css style name for elements that execute this action */
	public String getActionStyle();

}
