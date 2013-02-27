package com.eduworks.gwt.client.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public final class Images
{
	public static final String DEFAULT_ICON_DIM = "17px";

	/** Create an image from path. If extension is provided, it is appended to file. */
	public static Image getIcon(String path, String ext)
	{
		return getIcon(path, ext, null);
	}

	/**
	 * Create an image from path. If extension is provided, it is appended to file.
	 * If the image doesn't exist, the default icon path is used instead.
	 */
	public static Image getIcon(String path, String ext, String defaultIcon)
	{
		if (path == null)
			path = defaultIcon;

		if (ext != null && ext.length() > 0)
			path = (ext.charAt(0) == '.')
				? Strings.format("$(0)$(1)", path, ext)
				: Strings.format("$(0).$(1)", path, ext);

		final Image image = new Image(path);

		if (isEmpty(image))
			image.getElement().setAttribute(Ui.ATTRB_SRC, defaultIcon);

		setIconSize(image);

		return image;
	}

	public static Image getIconForSite(String url, String defaultIcon)
	{
		return getIcon(Uri.getFaviconFor(url), null, defaultIcon);
	}

	/** @return true if image dimensions are zero or image src is empty; false otherwise */
	public static boolean isEmpty(Image image)
	{
		if (image == null)
			return true;

		final String src = getImageSrc(image);

		/* If this is a favicon, it may not have loaded yet */

		if (image.getHeight() == 0 || image.getWidth() == 0)
			return !src.startsWith(Uri.FAVICON_URL);

		return Strings.isEmpty(src);
	}

	/** @return true if the src attribute of isIt equals the specified src value */
	public static boolean isImage(Image isIt, String src)
	{
		if (isIt == null || src == null) return false;

		return src.equals(getImageSrc(isIt));
	}

	public static String getImageSrc(Image image)
	{
		if (image == null) return Strings.EMPTY;

		return image.getElement().getAttribute(Ui.ATTRB_SRC);
	}

	public static Widget setImageSrc(final Widget image, final String src)
	{
		if (image instanceof Image)
			((Image)image).setUrl(src);

		else if (image instanceof Anchor)
		{
			final Element element = Element.as(image.getElement().getChild(0));

			if (element instanceof ImageElement)
				ImageElement.as(element).setSrc(src);
		}
		else
			Logger.logWarning("Could not set source for $(0)", image.getClass());

		return image;
	}

	public static Widget setImageTitle(Widget image, String title)
	{
		if (image instanceof Image)
			return setImageTitle((Image)image, title);

		else if (image instanceof Anchor)
		{
			final Element element = Element.as(image.getElement().getChild(0));

			if (element instanceof ImageElement)
			{
				ImageElement.as(element).setAlt(title);
				ImageElement.as(element).setTitle(title);
			}
		}
		else
			Logger.logWarning("Could not set title for $(0)", image.getClass());

		return image;
	}

	public static Image setImageTitle(Image image, String title)
	{
		if (image == null) return null;

		image.setAltText(title);
		image.setTitle(title);

		return image;
	}

	public static Image setIconSize(Image image)
	{
		return Images.setIconSize(image, DEFAULT_ICON_DIM);
	}

	public static Image setIconSize(Image image, String iconDim)
	{
		if (image == null) return null;

		image.setSize(iconDim, iconDim);

		return image;
	}

}
