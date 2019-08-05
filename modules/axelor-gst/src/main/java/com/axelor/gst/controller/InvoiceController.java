package com.axelor.gst.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.services.InvoiceServiceImp;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController extends JpaSupport {
	@Inject
	private InvoiceServiceImp service;

	public void RecomputeInvoiceLine(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceList = invoice.getInvoiceItemsList();
		List<InvoiceLine> invoiceUpdateList = new ArrayList<InvoiceLine>();
		Party party = invoice.getParty();

		if (party != null) {
			if (invoice.getInvoiceItemsList() != null) {
				for (InvoiceLine invoiceline : invoiceList) {
					InvoiceLine invoiceLists = service.Calculation(invoiceline, invoice);
					invoiceUpdateList.add(invoiceLists);
				}
				response.setValue("invoiceItemsList", invoiceUpdateList);
			}
		}
	}

	public void ComputeInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		Party party = invoice.getParty();
		if (party != null) {
			invoice = service.invoiceCalculation(invoice);
			response.setValues(invoice);
		} else {
			invoice.setNetAmount(null);
			invoice.setNetCGST(null);
			invoice.setNetIGST(null);
			invoice.setNetSGST(null);
			invoice.setGrossAmount(null);
		}
	}

	public void ComputeNetAmount(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
		Invoice invoice = request.getContext().getParent().asType(Invoice.class);
		if (invoiceline.getProduct() != null) {
			invoiceline = service.Calculation(invoiceline, invoice);
			response.setValues(invoiceline);
		} else {
			invoiceline.setGrossAmount( BigDecimal.ZERO);
			invoiceline.setIGST( BigDecimal.ZERO);
			invoiceline.setCGST( BigDecimal.ZERO);
			invoiceline.setSGST( BigDecimal.ZERO);
			invoiceline.setGstRate( BigDecimal.ZERO);
			invoiceline.setGrossAmount( BigDecimal.ZERO);
			invoiceline.setItem(null);
		}

	}

	public void setPartyContact(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = service.partyDetails(invoice);
		response.setValues(invoice);
	}
}
