package com.eduworks.gwt.client.util;

public class Browser
{

	public static final native boolean isIE() /*-{
		var acc = false;
		if ($wnd.isIE)
			acc = true;
		return acc;
	}-*/;
	
}
