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
		List<InvoiceLine> invoiceList = (List<InvoiceLine>) service.invoiceList(invoice);
		response.setValues(invoice);
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
		invoiceline = service.Calculation(invoiceline,invoice);
		response.setValues(invoiceline);
	}

	public void setPartyContact(ActionRequest request, ActionResponse response) {
		boolean inInvoiceAddShippingAdd = (boolean) request.getContext().get("inUseInvoiceAddressAsShipping");
		Invoice invoice = request.getContext().asType(Invoice.class);
		Party party = invoice.getParty();

		Contact contact = null;
		Address invoiceaddress = null;
		Address shippingaddress = null;
		if (party != null) {
			List<Contact> contactlist = party.getContactList();
			for (Contact c : contactlist) {
				if (c.getType().equals("primary")) {
					contact = c;
				}
			}

			List<Address> addresslist = party.getAddressList();
			for (Address a : addresslist) {
				if (a.getType().equals("invoice")) {
					invoiceaddress = a;
				}
			}

			if (inInvoiceAddShippingAdd) {
				response.setValue("shippingAddress", invoiceaddress);
			} else {
				List<Address> addresslist1 = party.getAddressList();
				for (Address a : addresslist1) {
					if (a.getType().equals("shipping")) {
						shippingaddress = a;
						
					}
				}
				response.setValue("shippingAddress", shippingaddress);
			}

			response.setValue("invoiceAddress", invoiceaddress);
			response.setValue("partyContact", contact);
			response.setAttr("invoiceItemsList", "readonly", false);
		}
		else {
			response.setAttr("invoiceItemsList", "readonly", true);
			response.setValue("partyContact", contact);
			response.setValue("invoiceAddress", invoiceaddress);
			response.setValue("shippingAddress", shippingaddress);
		}
	}

	public void setShippingAddress(ActionRequest request, ActionResponse response) {
		boolean inInvoiceAddShippingAdd = (boolean) request.getContext().get("inUseInvoiceAddressAsShipping");
		Invoice invoice = request.getContext().asType(Invoice.class);
		Address invoiceaddress = invoice.getInvoiceAddress();
		Party party = invoice.getParty();
		Address address = null;
		if (party != null) {
			if (inInvoiceAddShippingAdd) {
				response.setValue("shippingAddress", invoiceaddress);
			} else {
				List<Address> addresslist = party.getAddressList();
				for (Address a : addresslist) {
					if (a.getType().equals("shipping")) {
						address = a;

					}
				}
				response.setValue("shippingAddress", address);
			}
		} else {
			response.setValue("shippingAddress", address);
		}
	}

}
