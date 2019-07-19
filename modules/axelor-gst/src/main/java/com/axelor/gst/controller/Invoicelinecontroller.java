package com.axelor.gst.controller;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class Invoicelinecontroller extends JpaSupport {

	public void calnetamount(ActionRequest request, ActionResponse response) {

		InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
		Invoice invoice = request.getContext().getParent().asType(Invoice.class);
		BigDecimal netamount = BigDecimal.ZERO;
		BigDecimal value = invoiceline.getPrice().multiply(new BigDecimal(invoiceline.getQty()));

		netamount = netamount.add(value);
		invoiceline.setNetAmount(netamount);

		response.setValue("netAmount", invoiceline.getNetAmount());

		Address companyAddress = invoice.getCompany().getAddress();
		Address invoiceAddress = invoice.getInvoiceAddress();

		BigDecimal gst = BigDecimal.ZERO;

		if (companyAddress.getState().equals(invoiceAddress.getState())) {
			BigDecimal grossValues = BigDecimal.ZERO;
			BigDecimal sgst = BigDecimal.ZERO;
			BigDecimal cgst = BigDecimal.ZERO;
			BigDecimal bg1 = new BigDecimal("2");
			BigDecimal valueigst = invoiceline.getNetAmount();
			gst = gst.add(invoiceline.getGstRate().multiply(valueigst));
			BigDecimal dividevalue = gst.divide(bg1);
			sgst = sgst.add(dividevalue);
			cgst = cgst.add(dividevalue);
			invoiceline.setCGST(cgst);
			invoiceline.setSGST(sgst);
			valueigst = valueigst.add(cgst);
			grossValues = cgst.add(valueigst);
			invoiceline.setGrossAmount(grossValues);
			response.setValue("grossAmount", invoiceline.getGrossAmount());
			response.setValue("CGST", invoiceline.getCGST());
			response.setValue("SGST", invoiceline.getSGST());
		} else {

			BigDecimal igst = BigDecimal.ZERO;
			BigDecimal valueigst = invoiceline.getNetAmount();
			gst = gst.add(invoiceline.getGstRate().multiply(valueigst));
			igst = igst.add(gst);
			valueigst = valueigst.add(igst);
			invoiceline.setGrossAmount(valueigst);
			invoiceline.setIGST(igst);
			response.setValue("IGST", invoiceline.getIGST());
			response.setValue("grossAmount", invoiceline.getGrossAmount());
		}
	}

	public void onCalculation(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceLines = invoice.getInvoiceItemsList();
		BigDecimal cgst = null, sgst = null, igst = null, netamount = null, grossamount = null;
		Integer qty=null;
		for (InvoiceLine invoiceLine : invoiceLines) {
			cgst=invoiceLine.getCGST().add(invoice.getNetCGST());
			sgst=invoiceLine.getSGST().add(invoice.getNetSGST());
			igst=invoiceLine.getIGST().add(invoice.getNetIGST());
			netamount=invoiceLine.getNetAmount().add(invoice.getNetAmount());
			grossamount=invoiceLine.getGrossAmount().add(invoice.getNetCGST());
	
		}
		invoice.setNetCGST(cgst);
		invoice.setNetSGST(sgst);
		invoice.setNetIGST(igst);
		invoice.setNetAmount(netamount);
		invoice.setGrossAmount(grossamount);

		
		
		response.setValue("netAmount",invoice.getNetAmount());
		  response.setValue("netSGST", invoice.getNetSGST());
		 response.setValue("netCGST", invoice.getNetCGST());
		  response.setValue("grossAmount", invoice.getGrossAmount());
		 
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
		} else {
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