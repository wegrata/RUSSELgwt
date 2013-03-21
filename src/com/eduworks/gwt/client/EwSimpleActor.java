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
