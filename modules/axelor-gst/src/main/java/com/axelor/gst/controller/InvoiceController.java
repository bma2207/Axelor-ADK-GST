package com.axelor.gst.controller;

import java.util.List;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
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

	public void CalculateinvoiceList(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);
		Party party = invoice.getParty();
		if (party != null) {
			List<InvoiceLine> invoiceList = (List<InvoiceLine>) service.invoiceList(invoice);
			response.setValues(invoice);

		} else {

		}
	}

	public void onCalculation(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = service.invoiceCalculation(invoice);
		response.setValues(invoice);
	}

	public void calculateInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = service.invoiceCalculation(invoice);
		response.setValues(invoice);
	}

	public void CalculateNetAmount(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
		Invoice invoice = request.getContext().getParent().asType(Invoice.class);
		invoiceline = service.Calculation(invoiceline, invoice);
		response.setValues(invoiceline);
		
		
	}

	public void setPartyContact(ActionRequest request, ActionResponse response) {
		boolean inInvoiceAddShippingAdd = (boolean) request.getContext().get("inUseInvoiceAddressAsShipping");
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = service.partyDetails(invoice, inInvoiceAddShippingAdd);
		response.setValues(invoice);
	}

	public void setShippingAddress(ActionRequest request, ActionResponse response) {
		boolean inInvoiceAddShippingAdd = (boolean) request.getContext().get("inUseInvoiceAddressAsShipping");
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = service.onclickAddress(invoice, inInvoiceAddShippingAdd);
		response.setValues(invoice);

	}
}
