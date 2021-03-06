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
