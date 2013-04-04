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

package com.eduworks.gwt.client.pagebuilder;

import java.util.ArrayList;
import java.util.Vector;

import org.w3c.dom.Node;

import com.eduworks.gwt.client.net.callback.EventCallback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class PageAssembler
{
	private static FlowPanel body = new FlowPanel();
	private static ArrayList<Widget> contents = new ArrayList<Widget>();
	private static long iDCounter;
	private static String rootPanelName;
	final public static String A = "a";
	final public static String TEXT = "text";
	final public static String PASSWORD = "password";
	final public static String LABEL = "label";
	final public static String SELECT = "select";
	final public static String HIDDEN = "hidden";
	final public static String FILE = "file";
	final public static String FORM = "form";
	final public static String IMAGE = "image";
	final public static String FRAME = "frame";
	final public static String CONTENT_HEADER = "contentHeader";
	final public static String CONTENT_FOOTER = "contentFooter";
	private static String buildNumber;
	private static String helpUrl;

	public static void setTemplate(String rawHeader, String rawFooter, String rPanelName)
	{
		body.getElement().setId("flowContainer");
		clearContents();
		RootPanel.get(CONTENT_HEADER).add(new HTML(rawHeader));
		RootPanel.get(CONTENT_FOOTER).add(new HTML(rawFooter));
		rootPanelName = rPanelName;

		RootPanel.get(rootPanelName).add(body);
	}

	public static final native void fireOnChange(String elementId) /*-{
		$wnd.$('#' + elementId).change();
	}-*/;
	
	
	public static final long getIdCount() {
		return iDCounter;
	}
	
	public static final void setBuildNumber(String build) {
		buildNumber = build;
	}
	
	private static final void fillBuildNumber() {
		((Label)elementToWidget("buildNumber", LABEL)).setText(buildNumber);										
	}
	
	public static final void setHelp(String help) {
		helpUrl = help;
	}
	
	public static final String getHelp() {
		return helpUrl;										
	}
	
	public static final native void closePopup(String elementName) /*-{
		$wnd.$('#' + elementName).trigger('reveal:close');
	}-*/;

	public static final native Element getElementByClass(String elementClass) /*-{
		return $wnd.$(elementClass)[0];
	}-*/;
	
	public static final native void runCustomJSHooks() /*-{
		$wnd.boxedCustomAppJavascript();
	}-*/;
	
	public static void ready(Widget obj)
	{
		contents.add(obj);
	}

	/** Builds everything that has been readied into the rPanelName given in template setup, clears ready list after. */
	public static void buildContents()
	{
		body.clear();
		Element childNode;
		while ((childNode=DOM.getFirstChild(body.getElement()))!=null)
			DOM.removeChild(body.getElement(), childNode);
			   
		for (int i = 0; i < contents.size(); i++)
			body.add(contents.get(i));

		fillBuildNumber();
		runCustomJSHooks();
		contents.clear();
	}

	public static void clearContents()
	{
		iDCounter = 0;
		body.clear();
		Element childNode;
		while ((childNode=DOM.getFirstChild(body.getElement()))!=null)
			DOM.removeChild(body.getElement(), childNode);
		
		contents.clear();
		RootPanel.get(CONTENT_HEADER).clear();
		RootPanel.get(CONTENT_FOOTER).clear();
		RootPanel.get(rootPanelName).clear();
	}

	/** @Returns a list of IDs that get put into the template */
	public static Vector<String> inject(String elementName, String token, Widget w, boolean inFront) {
		boolean incrementIDCounter = false;
		Vector<String> convertedIDs = new Vector<String>();
		Vector<Element> nodeTree = new Vector<Element>();
		String elementID;
		Element e;
		int indexOfToken=-1;

		nodeTree.add(w.getElement());

		while(nodeTree.size()>0) {
			e = nodeTree.remove(0);
				for (int x=0;x<e.getChildCount();x++) {
					if (e.getChild(x).getNodeType()==Node.ELEMENT_NODE)
						nodeTree.add((Element)e.getChild(x));
				}
			elementID = e.getId();
			if (elementID!=null)
				indexOfToken = elementID.indexOf(token);
			if (indexOfToken!=-1) {
				incrementIDCounter = true;
				e.setId(elementID.substring(0, indexOfToken) + iDCounter + elementID.substring(indexOfToken + token.length()));
				convertedIDs.add(e.getId());
			} else if (e.getId()!="")
				convertedIDs.add(e.getId());
		}

		if (incrementIDCounter) iDCounter++;

		RootPanel.get(elementName).remove(w);
		if (inFront)
			RootPanel.get(elementName).insert(w, 0);
		else
			RootPanel.get(elementName).add(w);

		return convertedIDs;
	}
	
	/** @Returns a list of IDs that get put into the template */
	public static Vector<String> inject(Element element, String token, Widget w, boolean inFront) {
		boolean incrementIDCounter = false;
		Vector<String> convertedIDs = new Vector<String>();
		Vector<Element> nodeTree = new Vector<Element>();
		String elementID;
		Element e;
		int indexOfToken=-1;

		nodeTree.add(w.getElement());

		while(nodeTree.size()>0) {
			e = nodeTree.remove(0);
				for (int x=0;x<e.getChildCount();x++) {
					if (e.getChild(x).getNodeType()==Node.ELEMENT_NODE)
						nodeTree.add((Element)e.getChild(x));
				}
			elementID = e.getId();
			if (elementID!=null)
				indexOfToken = elementID.indexOf(token);
			if (indexOfToken!=-1) {
				incrementIDCounter = true;
				e.setId(elementID.substring(0, indexOfToken) + iDCounter + elementID.substring(indexOfToken + token.length()));
				convertedIDs.add(e.getId());
			} else if (e.getId()!="")
				convertedIDs.add(e.getId());
		}

		if (incrementIDCounter) iDCounter++;
		
		if (DOM.isOrHasChild(element, w.getElement()))
			DOM.removeChild(element, w.getElement());
		if (inFront)
			DOM.insertChild(element, w.getElement(), 0);
		else
			DOM.appendChild(element, w.getElement());

		return convertedIDs;
	}
	
	public static Vector<String> merge(String elementName, String token, Element incomingE) {
		boolean incrementIDCounter = false;
		Vector<String> convertedIDs = new Vector<String>();
		Vector<Element> nodeTree = new Vector<Element>();
		String elementID;
		Element e;
		int indexOfToken=-1;

		nodeTree.add(incomingE);

		while(nodeTree.size()>0) {
			e = nodeTree.remove(0);
				for (int x=0;x<e.getChildCount();x++) {
					if (e.getChild(x).getNodeType()==Node.ELEMENT_NODE)
						nodeTree.add((Element)e.getChild(x));
				}
			elementID = e.getId();
			if (elementID!=null)
				indexOfToken = elementID.indexOf(token);
			if (indexOfToken!=-1) {
				incrementIDCounter = true;
				e.setId(elementID.substring(0, indexOfToken) + iDCounter + elementID.substring(indexOfToken + token.length()));
				convertedIDs.add(e.getId());
			} else if (e.getId()!="")
				convertedIDs.add(e.getId());
		}

		if (incrementIDCounter) iDCounter++;
		
		RootPanel.get(elementName).getElement().appendChild(incomingE);

		return convertedIDs;
	}
	
	public static native JavaScriptObject getIFrameElement(Element iFrame, String objId) /*-{
		var doc = iFrame.contentDocument || iFrame.contentWindow.document;
		if (doc!=null)
			return doc.getElementById(objId);
		else
			return null;
	}-*/;
	
	/** @Returns if it worked */
	public static boolean attachHandler(String elementName, final int eventTypes, final EventCallback callback) {
		boolean result = false; 
		Element e = (Element)Document.get().getElementById(elementName);
		if (e!=null) {
			DOM.sinkEvents(e, eventTypes);
			DOM.setEventListener(e, new EventListener() {
										@Override
										public void onBrowserEvent(Event event) {
											if (event.getTypeInt()==eventTypes&&callback!=null)
												callback.onEvent(event);
										}
									});
			result = true;
		}
		return result;
	}
	
	/** @Returns if it worked */
	public static boolean attachHandler(Element e, final int eventTypes, final EventCallback callback) {
		boolean result = false; 
		if (e!=null) {
			DOM.sinkEvents(e, eventTypes);
			DOM.setEventListener(e, new EventListener() {
										@Override
										public void onBrowserEvent(Event event) {
											if (callback!=null)
												callback.onEvent(event);
										}
									});
			result = true;
		}
		return result;
	}
	
	/** preserves event handlers on element to be wrapped */
	public static Widget elementToWidget(Element e, String typ) {
		Widget result = null;
		if (e!=null) {
			int eventsSunk = DOM.getEventsSunk(e);
			EventListener el = DOM.getEventListener(e);
			if (typ==TEXT)
				 result = TextBox.wrap(e);
			else if (typ==PASSWORD)
				result = PasswordTextBox.wrap(e);
			else if (typ==LABEL)
				result = Label.wrap(e);
			else if (typ==A)
				result = Anchor.wrap(e);
			else if (typ==IMAGE)
				result = Image.wrap(e);
			else if (typ==SELECT)
				result = ListBox.wrap(e);
			else if (typ==HIDDEN)
				result = Hidden.wrap(e);
			else if (typ==FILE)
				result = FileUpload.wrap(e);
			else if (typ==FORM)
				result = FormPanel.wrap(e, true);
			else if (typ==FRAME)
				result = Frame.wrap(e);
			DOM.sinkEvents(e, eventsSunk);
			DOM.setEventListener(e, el);
		} else {
			if (typ==TEXT)
				 result = new TextBox();
			else if (typ==PASSWORD)
				result = new PasswordTextBox();
			else if (typ==LABEL)
				result = new Label();
			else if (typ==A)
				result = new Anchor();
			else if (typ==SELECT)
				result = new ListBox();
			else if (typ==IMAGE)
				result = new Image();
			else if (typ==HIDDEN)
				result = new Hidden();
			else if (typ==FILE)
				result = new FileUpload();
			else if (typ==FORM)
				result = new FormPanel();
			else if (typ==FRAME)
				result = new Frame();;
		}
		return result;
	}
	
	/** preserves event handlers on element to be wrapped  */
	public static Widget elementToWidget(String elementName, String typ) {
		Widget result = null;
		Element e = DOM.getElementById(elementName);
		if (e!=null) {
			int eventsSunk = DOM.getEventsSunk(e);
			EventListener el = DOM.getEventListener(e);
			if (typ==TEXT)
				 result = TextBox.wrap(e);
			else if (typ==PASSWORD)
				result = PasswordTextBox.wrap(e);
			else if (typ==LABEL)
				result = Label.wrap(e);
			else if (typ==A)
				result = Anchor.wrap(e);
			else if (typ==SELECT)
				result = ListBox.wrap(e);
			else if (typ==IMAGE)
				result = Image.wrap(e);
			else if (typ==HIDDEN)
				result = Hidden.wrap(e);
			else if (typ==FILE)
				result = FileUpload.wrap(e);
			else if (typ==FORM)
				result = FormPanel.wrap(e, true);
			else if (typ==FRAME)
				result = Frame.wrap(e);
			DOM.sinkEvents(e, eventsSunk);
			DOM.setEventListener(e, el);
		} else {
			if (typ==TEXT)
				 result = new TextBox();
			else if (typ==PASSWORD)
				result = new PasswordTextBox();
			else if (typ==LABEL)
				result = new Label();
			else if (typ==A)
				result = new Anchor();
			else if (typ==IMAGE)
				result = new Image();
			else if (typ==SELECT)
				result = new ListBox();
			else if (typ==HIDDEN)
				result = new Hidden();
			else if (typ==FILE)
				result = new FileUpload();
			else if (typ==FORM)
				result = new FormPanel();
			else if (typ==FRAME)
				result = new Frame();
		}
		return result;
	}

	public static native void setWidth(Element element, String value) /*-{
		element.style.width = value;
	}-*/;
}
