package com.eduworks.gwt.client.util;

import com.google.gwt.core.client.JavaScriptObject;

public class Date {
	JavaScriptObject d = JavaScriptObject.createObject();
	
	public Date() {
		makeDateObject();
	}
	
	public final native void makeDateObject() /*-{
		this.@com.eduworks.gwt.client.util.Date::d = new Date();
	}-*/;
	
	public final native int getYear() /*-{
		return this.@com.eduworks.gwt.client.util.Date::d.getFullYear();
	}-*/; 
	
	public final native int getDate() /*-{
		return this.@com.eduworks.gwt.client.util.Date::d.getDate();
	}-*/;
	
	public final native int getMonth() /*-{
		return this.@com.eduworks.gwt.client.util.Date::d.getMonth();
	}-*/;
	
	public final native void setDate(int date) /*-{
		this.@com.eduworks.gwt.client.util.Date::d.setDate(date);
	}-*/;
}