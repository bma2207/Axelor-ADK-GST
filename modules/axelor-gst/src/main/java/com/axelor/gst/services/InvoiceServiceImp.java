package com.axelor.gst.services;

import java.math.BigDecimal;
import java.util.List;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;

public class InvoiceServiceImp implements InvoiceService {
	@Override
	public Invoice invoiceCalculation(Invoice invoice) {
		List<InvoiceLine> invoiceLines = invoice.getInvoiceItemsList();
		BigDecimal cgst = null, sgst = null, igst = null, netamount = null, grossamount = null;
		invoice.setNetAmount(null);
		invoice.setNetCGST(null);
		invoice.setNetIGST(null);
		invoice.setNetSGST(null);
		invoice.setGrossAmount(null);
		if (invoice.getInvoiceItemsList() != null) {

			for (InvoiceLine invoiceLine : invoiceLines) {
				cgst = invoiceLine.getCGST().add(invoice.getNetCGST());
				sgst = invoiceLine.getSGST().add(invoice.getNetSGST());
				igst = invoiceLine.getIGST().add(invoice.getNetIGST());
				netamount = invoiceLine.getNetAmount().add(invoice.getNetAmount());
				invoice.setNetCGST(cgst);
				grossamount = invoiceLine.getGrossAmount().add(invoice.getGrossAmount());
				invoice.setNetSGST(sgst);
				invoice.setNetIGST(igst);
				invoice.setNetAmount(netamount);
				invoice.setGrossAmount(grossamount);
			}

		}

		return invoice;
	}

	@Override
	public InvoiceLine Calculation(InvoiceLine invoiceline, Invoice invoice) {

		BigDecimal netamount = BigDecimal.ZERO;
		BigDecimal gst = BigDecimal.ZERO;
		BigDecimal sgst = BigDecimal.ZERO;
		BigDecimal cgst = BigDecimal.ZERO;
		BigDecimal igst = BigDecimal.ZERO;
		BigDecimal grossValues = BigDecimal.ZERO;
		BigDecimal valueigst = invoiceline.getNetAmount();
		BigDecimal value = invoiceline.getPrice().multiply(new BigDecimal(invoiceline.getQty()));

		netamount = netamount.add(value);
		invoiceline.setNetAmount(netamount);

		Address companyAddress = invoice.getCompany().getAddress();
		Address invoiceAddress = invoice.getInvoiceAddress();

		gst = gst.add(invoiceline.getGstRate().multiply(netamount)).divide(new BigDecimal(100));
		if (companyAddress.getState().equals(invoiceAddress.getState())) {

			BigDecimal dividevalue = gst.divide(new BigDecimal(2));
			sgst = sgst.add(dividevalue);
			cgst = sgst;
			valueigst = netamount.add(cgst);
			grossValues = cgst.add(valueigst);

		} else {
			igst = igst.add(gst);
			grossValues = netamount.add(igst);

		}
		invoiceline.setCGST(cgst);
		invoiceline.setSGST(sgst);
		invoiceline.setIGST(igst);
		invoiceline.setGrossAmount(grossValues);
		return invoiceline;
	}

	@Override
	public Invoice partyDetails(Invoice invoice) {
		Party party = invoice.getParty();
		Contact contact = null;
		Address invoiceaddress = null;
		Address shippingaddress = null;
		if (party == null) {
			invoice.setInvoiceAddress(null);
			invoice.setShippingAddress(null);
			invoice.setPartyContact(null);

		} else {

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

			if (invoice.getInUseInvoiceAddressAsShipping()) {
				invoice.setShippingAddress(shippingaddress);

			} else {
				List<Address> addresslist1 = party.getAddressList();
				for (Address a : addresslist1) {
					if (a.getType().equals("shipping")) {
						shippingaddress = a;

					}
				}
				invoice.setShippingAddress(shippingaddress);
			}
			invoice.setPartyContact(contact);
			invoice.setInvoiceAddress(invoiceaddress);

		}
		return invoice;
	}

}
