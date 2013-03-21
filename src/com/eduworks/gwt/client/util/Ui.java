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

import com.eduworks.gwt.client.EwIsAction;
import com.eduworks.gwt.client.EwIsActor;
import com.eduworks.gwt.client.enumeration.EwIsStyle;
import com.eduworks.gwt.client.enumeration.impl.EwStyle;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;

public final class Ui
{
	public enum Side
	{
		BOTTOM,
		LEFT,
		RIGHT,
		TOP;

		public final String value = name().toLowerCase();
	}

	public static final String ATTRB_ALIGN = "align";
	public static final String ATTRB_CLASS = "class";
	public static final String ATTRB_DISABLED = "disabled";
	public static final String ATTRB_ID = "id";
	public static final String ATTRB_NAME = "name";
	public static final String ATTRB_SRC = "src";
	public static final String ATTRB_STYLE = "style";
	public static final String ATTRB_WIDTH = "width";
	public static final String ATTRB_HEIGHT = "height";

	public static final String DEFAULT_ICON_DIM = "17px";
	public static final String DEFAULT_PERCENT = "100%";
	public static final String DEFAULT_PIXEL = "150px";

	public static final String STYLE_HIDE = "display: none;";

	public static final String TAG_DIV = "div";
	public static final String TAG_SPAN = "span";
	public static final String TAG_TR = "tr";

	/** Simulates a click to force focus on a widget. */
	public static void activate(Widget widget)
	{
		if (widget == null) return;

		widget.fireEvent(new GwtEvent<ClickHandler>()
		{
			@Override
			public Type<ClickHandler> getAssociatedType()
			{
				return ClickEvent.getType();
			}

			@Override
			protected void dispatch(ClickHandler handler)
			{
				handler.onClick(null);
			}
		});
	}

	public static Element attachListener(final String tagId, final int eventType, final EwIsActor actor)
	{
		return attachListener(tagId, eventType, new EventListener()
		{
			@Override
			public void onBrowserEvent(Event event)
			{
				if (event.getTypeInt() == eventType)
					Actions.execute(actor);
			}
		});
	}

	public static Element attachListener(String tagId, int eventType, EventListener listener)
	{
		final com.google.gwt.user.client.Element element = getEventElement(tagId);

		if (element == null)
			Logger.logError("No event element for id '$(0)'", tagId);
		else
			attachListener(element, eventType, listener);

		return element;
	}

	public static Element attachListener(Element element, int eventType, EventListener listener)
	{
		final com.google.gwt.user.client.Element eventElement = getEventElement(element);

		if (eventElement == null)
			Logger.logError("Cannot attach listener to element '$(0)'", Classes.getSimpleName(element));
		else
			attachListener(eventElement, eventType, listener);

		return element;
	}

	/** Remove any existing listener for specified event, and then add the listener. */
	private static void attachListener(com.google.gwt.user.client.Element element, int eventType, EventListener listener)
	{
		detachListener(element, eventType);

		DOM.sinkEvents(element, eventType);
		DOM.setEventListener(element, listener);
	}

	public static Element detachListener(Element element, int eventType)
	{
		final com.google.gwt.user.client.Element eventElement = getEventElement(element);

		if (eventElement == null)
			Logger.logError("Cannot detach listener from element '$(0)'", Classes.getSimpleName(element));
		else
			DOM.sinkEvents(eventElement, DOM.getEventsSunk(eventElement) & (~eventType));

		return element;
	}

	public static com.google.gwt.user.client.Element getEventElement(String tagId)
	{
		return getEventElement(Document.get().getElementById(tagId));
	}

	public static com.google.gwt.user.client.Element getEventElement(Element element)
	{
		return (isEventElement(element))
			? (com.google.gwt.user.client.Element)element
			: null;
	}

	public static boolean isEventElement(Element element)
	{
		return (element instanceof com.google.gwt.user.client.Element);
	}

	/** Attempts to collapse all but one of the children widgets for a {@link ComplexPanel}. */
	public static Widget collapseOthers(ComplexPanel panel, int selected)
	{
		if (panel == null) return null;

		if (panel instanceof StackPanel)
			((StackPanel)panel).showStack(selected);

		return collapseOthers((Panel)panel, panel.getWidget(selected));
	}

	/** Attempts to collapse all but the specified widget for a {@link ComplexPanel}. */
	public static Widget collapseOthers(ComplexPanel panel, Widget selected)
	{
		if (panel == null) return null;

		if (panel instanceof StackPanel)
			((StackPanel)panel).showStack(((StackPanel)panel).getWidgetIndex(selected));

		return collapseOthers((Panel)panel, selected);
	}

	/** Attempts to collapse all but the specified widget for a {@link Panel}. */
	public static Widget collapseOthers(Panel panel, Widget selected)
	{
		if (panel == null) return null;

		for (Widget other : panel)
		{
			// Hides any existing menu content
			if (other != null && other != selected)
				other.setVisible(false);

			refreshSize(other);
		}

		activate(selected);

		return selected;
	}

	public static Widget appendHtml(Widget html, String textBefore, String textAfter, String ... styles)
	{
		if (textBefore == null) textBefore = Strings.EMPTY;
		if (textAfter == null) textAfter = Strings.EMPTY;

		final Element outerSpan = DOM.createSpan();

		outerSpan.setInnerHTML(Strings.append(
				textBefore, convertToSpan(html).toString(), textAfter
			));

		final HTML appended = HTML.wrap(outerSpan);

		appended.setTitle(html.getTitle());

		for (int i = 0; i < styles.length; i++)
			appended.addStyleName(styles[i]);

		return appended;
	}

	public static Widget appendHtml(Widget ... htmls)
	{
		final StringBuilder htmlBuilder = new StringBuilder();

		/* Wrap each in a span, and append their string values */

		for (Widget widget : htmls)
		{
			if (widget instanceof HTML)
				htmlBuilder.append(convertToSpan(widget).toString());
			else
			{
				final Element outerSpan = DOM.createSpan();
				outerSpan.setInnerHTML(widget.getElement().getString());
				htmlBuilder.append(outerSpan.getString());
			}
		}

		return new HTML(htmlBuilder.toString());
	}

	private static Widget convertToSpan(Widget html)
	{
		if (html == null)
			return HTML.wrap(DOM.createSpan());

		else if (TAG_SPAN.equalsIgnoreCase(html.getElement().getTagName()))
			return html;

		final Widget converted;

		final Element div = html.getElement();
		final Element span = DOM.createSpan();

		span.setInnerHTML(div.getInnerHTML());

		if (div.hasAttribute(ATTRB_ID))
			span.setAttribute(ATTRB_ID, div.getAttribute(ATTRB_ID));

		if (div.hasAttribute(ATTRB_NAME))
			span.setAttribute(ATTRB_NAME, div.getAttribute(ATTRB_NAME));

		if (div.hasAttribute(ATTRB_CLASS))
			span.setAttribute(ATTRB_CLASS, div.getAttribute(ATTRB_CLASS));

		converted = HTML.wrap(span);
		converted.setStyleName(html.getStyleName());

		return converted;
	}

	/** Parses the class name of widget, appends the Ew prefix, and applies the style to widget. */
	public static <T extends Widget> T applyStyleTo(T widget)
	{
		if (widget != null)
			widget.addStyleName(EwStyle.getStyle(EwStyle.DEFAULT_PREFIX, widget.getClass()));

		return widget;
	}

	public static <T extends Widget> T applyStyleTo(T widget, EwIsStyle ... styles)
	{
		if (widget != null && styles != null)
			for (EwIsStyle style : styles)
				widget.addStyleName(style.toString());

		return widget;
	}

	/** Applies each style to widget, prefixing it with {@link EwStyle#DEFAULT_PREFIX}. */
	public static <T extends Widget> T applyStyleTo(T widget, String ... styles)
	{
		if (widget != null)
			for (String style : styles)
				applyStyleTo(widget, style, true);

		return widget;
	}

	/** Applies style to widget, optionally prefixing it with {@link EwStyle#DEFAULT_PREFIX}. */
	public static <T extends Widget> T applyStyleTo(T widget, String style, boolean prefix)
	{
		if (prefix) style = EwStyle.getStyle(EwStyle.DEFAULT_PREFIX, style);

		if (widget != null) widget.addStyleName(style);

		return widget;
	}

	public static <T extends Widget> T removeStyleFrom(T widget, EwIsStyle ... styles)
	{
		if (widget != null)
			for (EwIsStyle style : styles)
				widget.removeStyleName(style.toString());

		return widget;
	}

	/** Creates a link to execute the action on the actor, with optional context. */
	public static Widget createLink(final EwIsActor actor, final EwIsAction action, final Object ... contextArray)
	{
		return createLink(actor, action, action.getActionName(), contextArray);
	}

	/** Creates a link to execute the action on the actor, with optional context. */
	public static Widget createLink(final EwIsActor actor, final String label, final Object ... contextArray)
	{
		return createLink(actor, null, label, contextArray);
	}

	/** Creates a link to execute the action on the actor, with a customized label and optional context. */
	public static Widget createLink(final EwIsActor actor, final EwIsAction action, String label, final Object ... contextArray)
	{
		final Anchor actionLink = new Anchor(label, true);

		actionLink.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				Actions.execute(actor, action, contextArray);
			}
		});

		if (action != null)
			actionLink.addStyleName(action.getActionStyle());

		if (label != null && label.matches("\\w+"))
			applyStyleTo(actionLink, label);

		return applyStyleTo(actionLink);
	}

	public static HTML getDefaultLabel(String format, Object ... args)
	{
		final HTML label = new HTML(Strings.format(format, args));

		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		return applyStyleTo(label, "label");
	}

	public static FlexTable getDefaultTable()
	{
		return applyStyleTo(new FlexTable(), "table");
	}

	public static TextArea getDefaultTextArea()
	{
		return applyStyleTo(new TextArea());
	}

	public static TextBox getDefaultTextBox()
	{
		return getDefaultTextBox(false);
	}

	public static TextBox getDefaultTextBox(boolean asPassword)
	{
		return applyStyleTo((asPassword) ? new PasswordTextBox() : new TextBox());
	}

	public static HTML getDefaultWidget(String format, Object ... args)
	{
		return applyStyleTo(new HTML(Strings.format(format, args)), "widget");
	}

	public static String getWidth(Widget widget)
	{
		if (widget instanceof Canvas)
			return ((Canvas) widget).getWidthAsString();

		return getProperty(widget, ATTRB_WIDTH);
	}

	public static String getHeight(Widget widget)
	{
		if (widget instanceof Canvas)
			return ((Canvas) widget).getHeightAsString();

		return getProperty(widget, ATTRB_HEIGHT);
	}

	/** Recurse over the widget's nodes and return the first child wihout children. */
	public static Element getFirstChildNode(Widget widget)
	{
		if (widget == null) return null;

		return getFirstChildNode(widget.getElement());
	}

	/** Recurse over the element's nodes and return the first child wihout children. */
	public static Element getFirstChildNode(Element element)
	{
		if (element == null) return null;

		com.google.gwt.dom.client.Element child = element;

		while ((element = element.getFirstChildElement()) != null)
			child = element;

		return child;
	}

	public static Element getParentTag(Widget widget, String tagName)
	{
		if (widget == null || !widget.isAttached()) return null;

		return getParentTag(widget.getElement(), tagName);
	}

	public static Element getParentTag(Element element, String tagName)
	{
		if (element != null && !Strings.isEmpty(tagName))
		{
			Element parent = element.getParentElement();

			while (parent != null)
				if (tagName.equalsIgnoreCase(parent.getTagName()))
					return parent;
				else
					parent = parent.getParentElement();
		}

		return null;
	}

	public static String getProperty(Widget widget, String propName)
	{
		if (widget == null) return null;

		return DOM.getStyleAttribute(widget.getElement(), propName);
	}

	public static boolean hasClassName(Widget widget, EwIsStyle style)
	{
		return hasClassName(widget, style.getStyleName());
	}

	/**
	 * @return if the {@link Element} wrapped by the specified {@link Widget} has the class name
	 * @see #hasClassName(Element, String)
	 */
	public static boolean hasClassName(Widget widget, String className)
	{
		if (widget == null) return false;

		return hasClassName(widget.getElement(), className);
	}

	public static boolean hasClassName(Element element, EwIsStyle style)
	{
		return hasClassName(element, style.getStyleName());
	}

	/** Adapted from {@link Element#addClassName(String)} to determine if className is present. */
	public static boolean hasClassName(Element element, String className)
	{
		if (element == null) return false;

		className = className.trim();

		if (Strings.isEmpty(className)) return false;

	    final String style = element.getClassName();
	    final int classLen = className.length();
	    final int styleLen = style.length();

	    int idx = style.indexOf(className);

	    // Calculate matching index in current style string
		while (idx != -1)
		{
			if (idx == 0 || style.charAt(idx - 1) == ' ')
			{
				int last = idx + classLen;
				int lastPos = styleLen;

				if ((last == lastPos))
					break;
				else if ((last < lastPos) && (style.charAt(last) == ' '))
					break;
			}

			idx = style.indexOf(className, idx + 1);
		}

	    return (idx != -1);
	}

	/** Adds "hide" class to widget. */
	public static void hide(Widget widget)
	{
		if (widget != null) hide(widget.getElement());
	}

	/** Adds "hide" class to element. */
	public static void hide(Element element)
	{
		element.addClassName(EwStyle.HIDE.getStyleName());
	}

	/** Removes "hide" class from widget, or removes "display: none;" from widget style. */
	public static void unhide(Widget widget)
	{
		if (widget != null) unhide(widget.getElement());
	}

	/** Removes "hide" class from element, or removes "display: none;" from element style. */
	public static void unhide(Element element)
	{
		if (element == null) return;

		if (hasClassName(element, EwStyle.HIDE.getStyleName()))
			element.removeClassName(EwStyle.HIDE.getStyleName());

		if (element.hasAttribute(ATTRB_STYLE))
		{
			final String style = element.getAttribute(ATTRB_STYLE);
			setStyleAttribute(element, style.replace(STYLE_HIDE, Strings.EMPTY), true);
		}
	}

	/** @return false if any cell of any row contains a non-null value that is not the loading image; true otherwise */
	public static boolean isEmpty(FlexTable table, boolean recurse)
	{
		if (table == null) return true;

		final int rowCount = table.getRowCount();

		if (!recurse)
			return (rowCount == 0);

		else for (int i = 0; i < rowCount; i++)
			if (table.getCellCount(i) > 0)
				for (int j = 0; j < table.getCellCount(i); j++)
					if (table.getWidget(i, j) == null)
						continue;
					else
						return false;

		return true;
	}

	/** @return true if panel widget is null, a loading image, or an empty FlexTable; false otherwise. */
	public static boolean isEmpty(SimplePanel panel)
	{
		return (panel == null) ? true : isEmpty(panel.getWidget(), true);
	}

	/** @return true if widget is null, or its inner HTML is empty */
	public static boolean isEmpty(Widget widget)
	{
		return isEmpty(widget, false);
	}

	/** @return true if widget is null, a loading image, an empty SimplePanel, or an empty FlexTable; false otherwise */
	public static boolean isEmpty(Widget widget, boolean recurse)
	{
		if (widget == null)
			return true;

		else if (Strings.isEmpty(widget.getElement().getInnerHTML(), true))
			return true;

		else if (widget instanceof FlexTable)
			return isEmpty((FlexTable)widget, recurse);

		else if (widget instanceof Image)
			return isEmpty(widget);

		else if (recurse)
		{
			if (widget instanceof SimplePanel)
				return isEmpty((SimplePanel)widget);
		}

		return false;
	}

	/** Compares index of widget to selected index in the panel. */
	public static boolean isSelected(StackPanel panel, Widget widget)
	{
		if (panel == null || widget == null) return false;

		final int selected = panel.getSelectedIndex();

		return (selected < 0)
			? widget.isVisible()
			: (selected == panel.getWidgetIndex(widget));
	}

	public static <T extends Widget> T refreshSize(T widget)
	{
		if (widget == null)
			return null;

		else if (widget instanceof Canvas)
			Dialogs.refreshSize((Canvas)widget);

		else
		{
			final com.google.gwt.user.client.Element element = widget.getElement();

			final String px = "px";
			final String width = DOM.getStyleAttribute(element, ATTRB_WIDTH);
			final String height = DOM.getStyleAttribute(element, ATTRB_HEIGHT);

			try
			{
				int w = (width.endsWith(px))
					? Integer.parseInt(width.substring(0, width.length()-2))
					: Integer.parseInt(width);

				int h = (height.endsWith(px))
					? Integer.parseInt(height.substring(0, height.length()-2))
					: Integer.parseInt(height);

				resize(widget, --w, --h);
				resize(widget, ++w, ++h);
			}
			catch (Exception e)
			{
			}
		}

		return widget;
	}

	public static void resize(Widget widget, int width, int height)
	{
		if (widget == null) return;

		widget.setPixelSize(width, height);

		widget.fireEvent(new GwtEvent<ResizedHandler>()
		{
			@Override
			public Type<ResizedHandler> getAssociatedType()
			{
				return ResizedEvent.getType();
			}

			@Override
			protected void dispatch(ResizedHandler handler)
			{
				handler.onResized(null);
			}
		});
	}

	public static Element setHtml(String tagId, String format, Object ... args)
	{
		if (tagId == null)
			return null;
		else
			return setHtml(Document.get().getElementById(tagId), format, args);
	}

	public static Element setHtml(Element element, String format, Object ... args)
	{
		if (format == null)
			format = Strings.EMPTY;

		if (element == null)
			Logger.logWarning("Cannot set HTML for null element");
		else
			element.setInnerHTML(Strings.format(format, args));

		return element;
	}

	public static <T extends HasHTML> T setHtml(T hasHtml, String format, Object ... args)
	{
		if (hasHtml == null)
			return hasHtml;

		if (format == null)
			format = Strings.EMPTY;

		hasHtml.setHTML(Strings.format(format, args));

		return hasHtml;
	}

	public static void setStyleAttribute(Widget widget, String newStyle, boolean append)
	{
		if (widget != null) setStyleAttribute(widget.getElement(), newStyle, append);
	}

	public static void setStyleAttribute(Element element, String newStyle, boolean append)
	{
		if (element == null || Strings.isEmpty(newStyle)) return;

		final String oldStyle = element.getAttribute(ATTRB_STYLE);

		final String style = (append)
			? Strings.append(' ', oldStyle, newStyle)
			: newStyle;

		element.setAttribute(ATTRB_STYLE, style);
	}

	/** Forces a {@link ScrollPanel} to scroll on the specified dimensions. */
	public static void setScrollable(ScrollPanel panel, double pixelWidth, double pixelHeight)
	{
		if (panel == null) return;

		final Style panelStyle = panel.getElement().getStyle();

		if (pixelWidth > 0)
		{
			panelStyle.setOverflowX(Overflow.SCROLL);
			panelStyle.setWidth(pixelWidth, Unit.PX);
		}
		else
			panelStyle.setOverflowX(Overflow.HIDDEN);

		if (pixelHeight > 0)
		{
			panelStyle.setOverflowY(Overflow.SCROLL);
			panelStyle.setHeight(pixelHeight, Unit.PX);
		}
		else
			panelStyle.setOverflowY(Overflow.HIDDEN);

		refreshSize(panel);
	}

	public static void setVisible(Element element, boolean visible)
	{
		if (element != null) UIObject.setVisible(element, visible);
	}

	/** Applies the following style names depending on checked: {@link EwStyle#CHECKED} & {@link EwStyle#UNCHECKED}*/
	public static void toggleCheckBox(CheckBox checkbox, boolean checked)
	{
		toggleCheckBox(checkbox, checked, null, null);
	}

	/** Toggles the style for a checkbox, and executes the action with the new checked status as context. */
	public static void toggleCheckBox(CheckBox checkbox, Boolean checked, EwIsActor actor, EwIsAction action)
	{
		if (checked)
		{
			checkbox.removeStyleName(EwStyle.UNCHECKED.getStyleName());
			checkbox.addStyleName(EwStyle.CHECKED.getStyleName());
		}
		else
		{
			checkbox.addStyleName(EwStyle.UNCHECKED.getStyleName());
			checkbox.removeStyleName(EwStyle.CHECKED.getStyleName());
		}

		Actions.execute(actor, action, checked);
	}

	/** Wraps widget in a div element. */
	public static HTML wrapHtml(Widget widget)
	{
		return new HTML(widget.getElement().getString());
	}

}
