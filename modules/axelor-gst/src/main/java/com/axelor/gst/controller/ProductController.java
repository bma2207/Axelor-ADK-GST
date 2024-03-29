package com.axelor.gst.controller;

import java.io.File;
import java.util.List;

import com.axelor.app.AppSettings;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.services.ProductServiceImp;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class ProductController extends JpaSupport {
	@Inject
	protected ProductServiceImp service;

	public void productIdsList(ActionRequest request, ActionResponse response) {

		if (request.getContext().get("productIds") != null) {
			Invoice invoice = request.getContext().asType(Invoice.class);
			List<Integer> productids = (List<Integer>) request.getContext().get("productIds");
			List<InvoiceLine> invoiceLines = service.productList(invoice, productids);
			response.setValue("invoiceItemsList", invoiceLines);
		}
	}
	public void ImagePath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");

		attachmentPath = attachmentPath.endsWith(File.separator) ? attachmentPath : attachmentPath + File.separator;

		request.getContext().put("AttachmentPath", attachmentPath);
	}
}
