package com.eduworks.gwt.client.util;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class Cursor
{
	public static void showWait()
	{
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor", "wait");
	}

	public static void showUnwait()
	{
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor", "default");
	}
}
