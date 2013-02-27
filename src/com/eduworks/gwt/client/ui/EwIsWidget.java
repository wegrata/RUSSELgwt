package com.eduworks.gwt.client.ui;

import com.eduworks.gwt.client.reflection.Instantiable;

/**
 * An interface to define methods and properties shared by all Eduworks UI components.
 * @author dharvey
 */
public interface EwIsWidget extends Instantiable
{
	/** Hides the widget, not necessarily destroys it. */
	public void hide();

	/** Displays the widget for the first time, or after hiding. */
	public void show();

	public EwIsWidget getInstantiatedBy();

	public <T extends EwIsWidget> void setInstantiatedBy(T instantiatedBy);

	public String getTitle();

	public void setTitle(String format, Object ... args);
}
