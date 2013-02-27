/*
Copyright (c) 2012 Eduworks Corporation
All rights reserved.
 
This Software (including source code, binary code and documentation) is provided by Eduworks Corporation to
the Government pursuant to contract number W31P4Q-12 -C- 0119 dated 21 March, 2012 issued by the U.S. Army 
Contracting Command Redstone. This Software is a preliminary version in development. It does not fully operate
as intended and has not been fully tested. This Software is provided to the U.S. Government for testing and
evaluation under the following terms and conditions:

	--Any redistribution of source code, binary code, or documentation must include this notice in its entirety, 
	 starting with the above copyright notice and ending with the disclaimer below.
	 
	--Eduworks Corporation grants the U.S. Government the right to use, modify, reproduce, release, perform,
	 display, and disclose the source code, binary code, and documentation within the Government for the purpose
	 of evaluating and testing this Software.
	 
	--No other rights are granted and no other distribution or use is permitted, including without limitation 
	 any use undertaken for profit, without the express written permission of Eduworks Corporation.
	 
	--All modifications to source code must be reported to Eduworks Corporation. Evaluators and testers shall
	 additionally make best efforts to report test results, evaluation results and bugs to Eduworks Corporation
	 using in-system feedback mechanism or email to russel@eduworks.com.
	 
THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
THE COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN 
IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
*/

package com.eduworks.gwt.client.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.Window;

public abstract class DragDropHandler implements DropHandler, DragLeaveHandler, DragEnterHandler, DragOverHandler
{

	public List<File>	readQueue	= new ArrayList<File>();
	//private DropPanel		w;

	public DragDropHandler(DropPanel w)
	{
		//this.w = w;
		w.addDragEnterHandler(this);
		w.addDragLeaveHandler(this);
		w.addDragOverHandler(this);
		w.addDropHandler(this);
	}

	@Override
	public void onDragOver(DragOverEvent event)
	{
		// Mandatory handler, otherwise the default
		// behavior will kick in and onDrop will never
		// be called
		event.stopPropagation();
		event.preventDefault();
	}

	@Override
	public void onDragEnter(DragEnterEvent event)
	{
//		enableDragDropStyle(true);
		event.stopPropagation();
		event.preventDefault();
	}

	@Override
	public void onDragLeave(DragLeaveEvent event)
	{
//		enableDragDropStyle(false);
		event.stopPropagation();
		event.preventDefault();
	}

	public void processFiles(FileList files)
	{
		GWT.log("length=" + files.getLength());
		for (File file : files)
		{
			readQueue.add(file);
		}
		readNext();
	}

	public void readNext()
	{
		if (readQueue.size() > 0)
		{
			try
			{
				File file = readQueue.get(0);
				run(file);
				readQueue.remove(0);
			}
			catch (Throwable t)
			{
				// Necessary for FF (see bug
				// https://bugzilla.mozilla.org/show_bug.cgi?id=701154)
				// Standard-complying browsers will to go in this branch
				handleError(readQueue.get(0), t);
				readQueue.remove(0);
				readNext();
			}
		}
	}

	public abstract  void run(File file);

	public void handleError(File file, Throwable t)
	{
		String errorDesc = t.getMessage();
		Window.alert("File loading error for file: " + file.getName() + "\n" + errorDesc);
	}

	@Override
	public void onDrop(DropEvent event)
	{
		processFiles(event.getDataTransfer().<DataTransferExt> cast().getFiles());
//		enableDragDropStyle(false);
		event.stopPropagation();
		event.preventDefault();
	}

//	private void enableDragDropStyle(boolean enable)
//	{
////		if (enable)
////			Ui.applyStyleTo(w, RusselStyle.DRAG_DROP_HOVER);
////		else
////			Ui.removeStyleFrom(w, RusselStyle.DRAG_DROP_HOVER);
//	}
}
