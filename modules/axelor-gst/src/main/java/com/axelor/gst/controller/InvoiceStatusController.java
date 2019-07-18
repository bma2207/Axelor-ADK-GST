package com.axelor.gst.controller;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
/*import com.axelor.gst.db.InvoiceStatus;*/
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceStatusController extends JpaSupport {

	public void onConfirm(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);

		if (invoice.getStatus().equals("draft")) {
			String val = "validated";
			response.setValue("status", val);
		}
	}

	public void onCanceled(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);

		if (invoice.getStatus().equals("draft")) {
			String val = "canceled";
			response.setValue("status", val);

		} else if (invoice.getStatus().equals("validated")) {
			String val = "canceled";
			response.setValue("status", val);
		} else if (invoice.getStatus().equals("paid")) {
			String val = "canceled";
			response.setValue("status", val);
		}

	}

	public void onDraft(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);

		if (invoice.getStatus().equals("validated")) {
			String val = "draft";
			response.setValue("status", val);
		} else if (invoice.getStatus().equals("paid")) {
			String val = "draft";
			response.setValue("status", val);
		}

	}
	public void onPaid(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);

		if (invoice.getStatus().equals("validated")) {
			String val = "paid";
			response.setValue("status", val);
		}
	}
}
