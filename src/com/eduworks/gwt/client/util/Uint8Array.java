package com.eduworks.gwt.client.util;

import org.vectomatic.arrays.ArrayBuffer;

import com.google.gwt.core.client.JsArrayInteger;

public class Uint8Array {
  public static final native JsArrayInteger createUint8Array(ArrayBuffer buffer) /*-{
    return new Uint8Array(buffer);
  }-*/;

  public final native int getLength() /*-{
    return this.length;
  }-*/;
  
  public final native byte get(int index) /*-{
    return this[index];
  }-*/;
}
