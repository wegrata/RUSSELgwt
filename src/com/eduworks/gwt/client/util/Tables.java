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

import com.eduworks.gwt.client.enumeration.EwIsStyle;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

public final class Tables
{
	public static final String EMPTY_CELL_HTML = "<span>&nbsp;</span>";

	public static <T extends Widget> T addWidget(FlexTable table, T widget, EwIsStyle ... styles)
	{
		return addWidget(table, widget, table.getRowCount(), styles);
	}

	public static <T extends Widget> T addWidget(FlexTable table, T widget, int row, EwIsStyle ... styles)
	{
		return addWidget(table, widget, row, 0, styles);
	}

	public static <T extends Widget> T addWidget(FlexTable table, T widget, int row, int col, EwIsStyle ... styles)
	{
		return addWidget(table, widget, null, null, row, col, styles);
	}

	/** Adds a widget to the specified cell, and returns it. */
	public static <T extends Widget> T addWidget(FlexTable table, T widget, String width, String height, int row, int col, EwIsStyle ... styles)
	{
		if (table != null && widget != null)
		{
			if (!Strings.isEmpty(width)) widget.setWidth(width);

			if (!Strings.isEmpty(height)) widget.setHeight(height);

			for (EwIsStyle style : styles)
				widget.addStyleName(style.getStyleName());

			table.setWidget(row, col, widget);
		}

		return widget;
	}

	/** Center contents of row, and set colspan to cell count of adjacent row. */
	public static Widget center(FlexTable table, int row)
	{
		return center(table, row, -1);
	}

	/** Center contents of row, and set colspan equal to corresponding cell of adjacent row. */
	public static Widget center(FlexTable table, int row, int col)
	{
		return center(table, row, col, -1);
	}

	/** Center contents of row, and set colspan as specified. */
	public static Widget center(FlexTable table, int row, int col, int colspan)
	{
		final FlexCellFormatter cells = fixColspan(table, row, col, colspan);

		if (cells == null) return null; // Table, row, or col is invalid

		if (col < 0) col = 0; // Set col to default value after colspan is set

		cells.setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
		cells.setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);

		return table.getWidget(row, col);
	}

	/**
	 * Sets the colspan of the first cell in row to the cell count of an adjacent row.
	 * The adjacent row is the previous row, unless the row specified is the first one.
	 * @see #fixColspan(FlexTable, int, int, int)
	 */
	public static FlexCellFormatter fixColspan(FlexTable table, int row)
	{
		return fixColspan(table, row, -1, -1);
	}

	/**
	 * Sets the colspan attribute of the specified cell in table to the specified value. If
	 * colspan is negative, the colspan of the specified cell is synced with the colspan of
	 * a cell from an adjacent row according to the following logic:
	 * <ul><li>If table is null or empty, or the row is out of bounds, null is returned.</li>
	 * <li>If there is no adjacent row, colspan is not set but the formatter is returned.</li>
	 * <li>If col is negative, colspan is set to the greater of<ul>
	 * <li>the cell count of the adjacent row or</li>
	 * <li>the colspan of the first cell in the adjacent row.</li></ul>
	 * <li>Otherwise, colspan is set to the colspan value of the specified cell</li></ul>
	 * The adjacent row is the previous row, unless the row specified is the first one.
	 * <br><br>
	 */
	public static FlexCellFormatter fixColspan(FlexTable table, int row, int col, int colspan)
	{
		if (row < 0 || table == null || table.getRowCount() == 0)
			return null; // No content to center

		else if (row >= table.getRowCount())
			return null; // Row index out of bounds

		else if (col >= table.getCellCount(row))
			return null; // Col index out of bounds

		final FlexCellFormatter cells = table.getFlexCellFormatter();

		/* Set colspan depending on adjacent row */

		if (colspan < 0)
			if (table.getRowCount() == 1)
				return cells; // No adjacent rows
			else
			{
				final int adjacent = (row == 0) ? 1 : (row - 1);

				if (0 <= col)
					colspan = cells.getColSpan(adjacent, col);
				else
					colspan = Math.max(
							table.getCellCount(adjacent),
							cells.getColSpan(adjacent, 0)
						);
			}

		if (col < 0) col = 0; // Set col to default value after colspan is set

		cells.setColSpan(row, col, colspan);

		return cells;
	}

	public static Element getElementFrom(FlexTable table, int row, int cell)
	{
		if (table == null || row < 0 || table.getRowCount() <= row)
			return null;

		if (cell < 0 || table.getCellCount(row) <= cell)
			return null;

		return table.getWidget(row, cell).getElement();
	}

	/** @return the inner HTML of the Widget element at the specified row and column if it exists. */
	public static String getStringFrom(FlexTable table, int row, int cell)
	{
		final Element element = getElementFrom(table, row, cell);

		return (element != null) ? element.getInnerHTML() : Strings.EMPTY;
	}

	/** Sets the inner HTML of the Widget element at the specified row and column if it exists. */
	public static void setStringAt(FlexTable table, int row, int cell, String str)
	{
		final Element element = getElementFrom(table, row, cell);

		if (element != null) element.setInnerHTML(str);
	}

	/** Sets the style on the TD element at the specified row and column if it exists. */
	public static void setStyleAt(FlexTable table, int row, int cell, String style)
	{
		if (table == null || row < 0 || table.getRowCount() <= row)
			return;

		if (cell < 0 || table.getCellCount(row) <= cell)
			return;

		Ui.setStyleAttribute(table.getCellFormatter().getElement(row, cell), style, false);
	}

	/**
	 * Inserts a number of cells before the specified cell, and returns the Element
	 * of the row into which they were inserted.
	 */
	public static Element insertCells(FlexTable table, int row, int beforeCell, int cellCount)
	{
		if (table == null) return null;

		if (row < 0 || table.getRowCount() < row) return null;

		if (table.getRowCount() == row) table.insertRow(row);

		final com.google.gwt.user.client.Element tr = table.getRowFormatter().getElement(row);
		final int lastCell = (beforeCell + cellCount);

		for (int i = beforeCell; i < lastCell; i++)
			DOM.insertChild(tr, DOM.createTD(), i);

		return tr;
	}

	/** Inserts a cell into the specified row, adds an empty span to it, and returns the span. */
	public static Widget insertEmptyBeforeCol(FlexTable table, int row, int beforeCell)
	{
		return insertWidgetBeforeCol(table, new HTML(EMPTY_CELL_HTML), null, null, row, beforeCell);
	}

	/** Inserts a cell into the specified row, adds widget to it, and returns the added widget. */
	public static <T extends Widget> T insertWidgetBeforeCol(FlexTable table, T widget, int row, int beforeCell)
	{
		return insertWidgetBeforeCol(table, widget, null, null, row, beforeCell);
	}

	/** Inserts a cell into the specified row, adds widget to it, and returns the added widget. */
	public static <T extends Widget> T insertWidgetBeforeCol(FlexTable table, T widget, String width, String height, int row, int beforeCell)
	{
		insertCells(table, row, beforeCell, 1);

		return addWidget(table, widget, width, height, row, beforeCell);
	}

	/** Inserts a row at the specified index, adds an empty span to the specified cell, and returns the span. */
	public static Widget insertEmptyBeforeRow(FlexTable table, int beforeRow, int col)
	{
		return insertWidgetBeforeRow(table, new HTML(EMPTY_CELL_HTML), null, null, beforeRow, col);
	}

	/** Inserts a row, adds widget to the specified cell, and returns the added widget. */
	public static <T extends Widget> T insertWidgetBeforeRow(FlexTable table, T widget, int beforeRow, int col, EwIsStyle ... styles)
	{
		return insertWidgetBeforeRow(table, widget, null, null, beforeRow, col, styles);
	}

	/** Inserts a row, adds widget to the specified cell, and returns the added widget. */
	public static <T extends Widget> T insertWidgetBeforeRow(FlexTable table, T widget, String width, String height, int beforeRow, int col, EwIsStyle ... styles)
	{
		if (table == null || beforeRow < 0)
			return null;

		if (table.getRowCount() < beforeRow)
			beforeRow = 0;

		return addWidget(table, widget, width, height, table.insertRow(beforeRow), col, styles);
	}

	/** Replaces specified row with a new one containing widget in the first cell. */
	public static Widget replaceWidget(FlexTable table, Widget widget, int row)
	{
		return replaceWidget(table, widget, row, 0);
	}

	/** Replaces specified row with a new one containing widget in the specified cell. */
	public static Widget replaceWidget(FlexTable table, Widget widget, int row, int col)
	{
		table.removeRow(row);
		table.insertRow(row);
		table.setWidget(row, col, widget);

		return widget;
	}

}
