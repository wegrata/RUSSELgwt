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
package com.eduworks.gwt.client.util;

import com.eduworks.gwt.client.util.Ui.Side;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;

public final class Dialogs
{
	public static boolean isAutoSize(Canvas canvas)
	{
		return (canvas instanceof Window) && ((Window)canvas).getAutoSize();
	}

	public static boolean isOpen(Canvas canvas)
	{
		return (canvas == null) ? false : canvas.isVisible();
	}

	/** Positions a SmartGWT {@link Window} next to a GWT {@link Widget}, on the specified side. */
	public static <T extends Window> T positionWindowNextTo(T window, Widget nextTo, Side side)
	{
		return positionWindowNextTo(window, nextTo, 0, side);
	}

	/**
	 * Positions a SmartGWT {@link Window} next to a GWT {@link Widget}, on the specified side.
	 * @param window	the window to position
	 * @param nextTo	the widget next to which the window should appear
	 * @param offsetY 	The {@link Element#getAbsoluteTop()} may be in reference to a parent {@link Window}.
	 * 					Send the {@link Window#getAbsoluteTop()} as an offset in this case.
	 * @param side		the side of widget on which to display window
	 * @return			the window after it has been positioned
	 */
	public static <T extends Window> T positionWindowNextTo(T window, Widget nextTo, int offsetY, Side side)
	{
		if (window == null)
			return null;

		else if (nextTo instanceof Canvas)
			window.showNextTo(
				(Canvas) nextTo,
				(side == null) ? Side.LEFT.value : side.value
			);

		else if (nextTo == null)
		{
			window.setAutoCenter(Boolean.TRUE);
			window.show();
		}
		else
		{
			final Element base = nextTo.getElement();

			final int left = base.getAbsoluteLeft();
			final int right = base.getAbsoluteRight();
			final int top = base.getAbsoluteTop() + (offsetY - 10);
			final int bottom = base.getAbsoluteBottom() + (offsetY - 10);

			final boolean hasWidth = (window.getWidthAsString() != null);
			final boolean hasHeight = (window.getHeightAsString() != null);

			final int width = (hasWidth) ? window.getWidth() : Math.max(100, (right-left));
			final int height = (hasHeight) ? window.getHeight() : 100;

			/* LEFT and TOP must be set regardless of hasWidth and hasHeight */

			switch (side)
			{
				case LEFT:
					window.setLeft(left - width);
					window.setRight(left);
					window.setTop(top);

					if (hasHeight)
						window.setBottom(top + height);
					break;

				case RIGHT:
					window.setLeft(right);
					window.setTop(top);

					if (hasWidth)
						window.setRight(left + width);
					if (hasHeight)
						window.setBottom(top + height);
					break;

				case TOP:
					window.setLeft(right - width);
					window.setRight(right);

					if (hasHeight)
						window.setTop(Math.max(0, top - height));
					else
						window.setTop(offsetY);

					window.setBottom(top);
					break;

				case BOTTOM:
					window.setLeft(right - width);
					window.setRight(right);
					window.setTop(bottom);

					if (hasHeight)
						window.setBottom(top + height);
					break;

				default:
					window.setAutoCenter(Boolean.TRUE);
					break;
			}

			if (!hasWidth || !hasHeight)
				window.setAutoSize(Boolean.TRUE);

			window.setWidth(width);
			window.setHeight(height);

			window.show();
		}

		return refreshSize(window);
	}

	public static <T extends Canvas> T refreshSize(T canvas)
	{
		if (canvas == null) return null;

		final boolean isAutoSize = isAutoSize(canvas);

		if (isAutoSize)
			((Window)canvas).setAutoSize(Boolean.FALSE);

		canvas.resizeBy(+1, +1);
		canvas.resizeBy(-1, -1);

		if (isAutoSize)
			((Window)canvas).setAutoSize(Boolean.TRUE);

		return canvas;
	}


}
