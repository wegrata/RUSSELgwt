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
package com.eduworks.gwt.client;

/** Identifies classes that execute an action (i.e. CRUD, etc.) through a UI component */
public interface EwIsActor
{
	/** Execute what is needed: no action specified. */
	public void execute(final Object ... contextArray);

	/** Execute the specified action. */
	public <T extends EwIsAction> void execute(final T action, final Object ... contextArray);

}
