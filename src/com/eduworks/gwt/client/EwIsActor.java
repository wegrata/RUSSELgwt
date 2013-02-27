package com.eduworks.gwt.client;

/** Identifies classes that execute an action (i.e. CRUD, etc.) through a UI component */
public interface EwIsActor
{
	/** Execute what is needed: no action specified. */
	public void execute(final Object ... contextArray);

	/** Execute the specified action. */
	public <T extends EwIsAction> void execute(final T action, final Object ... contextArray);

}
