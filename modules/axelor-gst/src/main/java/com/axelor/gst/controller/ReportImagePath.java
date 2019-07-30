package com.axelor.gst.controller;

import com.axelor.app.AppSettings;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.io.File;

public class ReportImagePath {

	public void ImagePath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");

		attachmentPath = attachmentPath.endsWith(File.separator) ? attachmentPath : attachmentPath + File.separator;

		request.getContext().put("AttachmentPath", attachmentPath);
	}
}
