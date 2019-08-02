package com.axelor.gst.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;

public class InvoiceServiceImp implements InvoiceService {
	@Override
	public List<InvoiceLine> invoiceList(Invoice invoice) {
		List<InvoiceLine> invoiceList = invoice.getInvoiceItemsList();
		List<InvoiceLine> invoiceUpdateList = new ArrayList<InvoiceLine>();
		Party party = invoice.getParty();
		Address invoiceaddress = null;
		List<Address> addresslist = party.getAddressList();

		for (Address a : addresslist) {
			if (a.getType().equals("invoice")) {
				invoiceaddress = a;
				invoice.setInvoiceAddress(invoiceaddress);

				if (invoice.getInvoiceItemsList() != null) {

					for (InvoiceLine invoiceline : invoiceList) {
						BigDecimal netamount = BigDecimal.ZERO;
						BigDecimal value = invoiceline.getPrice().multiply(new BigDecimal(invoiceline.getQty()));

						netamount = netamount.add(value);
						invoiceline.setNetAmount(netamount);

						Address companyAddress = invoice.getCompany() != null ? invoice.getCompany().getAddress() : null;
						Address invoiceAddress = invoice.getInvoiceAddress();

						BigDecimal gst = BigDecimal.ZERO;
						BigDecimal grossValues = BigDecimal.ZERO;
						BigDecimal sgst = BigDecimal.ZERO;
						BigDecimal cgst = BigDecimal.ZERO;
						BigDecimal igst = BigDecimal.ZERO;
						BigDecimal valueigst = invoiceline.getNetAmount();
						if (companyAddress != null && companyAddress.getState().equals(invoiceAddress.getState())) {
							gst = gst.add(invoiceline.getGstRate().multiply(netamount)).divide(new BigDecimal(100));
							BigDecimal dividevalue = gst.divide(new BigDecimal(2));
							sgst = sgst.add(dividevalue);
							cgst = sgst;
							invoiceline.setCGST(cgst);
							invoiceline.setSGST(sgst);
							invoiceline.setIGST(igst);
							valueigst = valueigst.add(cgst);
							grossValues = cgst.add(valueigst);
							invoiceline.setGrossAmount(grossValues);

						} else {

							gst = gst.add(invoiceline.getGstRate().multiply(valueigst)).divide(new BigDecimal(100));
							igst = igst.add(gst);
							value = valueigst.add(igst);
							invoiceline.setGrossAmount(value);
							invoiceline.setIGST(igst);
							invoiceline.setCGST(cgst);
							invoiceline.setSGST(sgst);

						}
						invoiceUpdateList.add(invoiceline);
					}
					invoice.setInvoiceItemsList(invoiceUpdateList);
				}
			}
		}
		return invoiceUpdateList;
	}

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

		} else {

		}

		return invoice;
	}

	public Invoice calculation(Invoice invoice) {
		List<InvoiceLine> invoiceLines = invoice.getInvoiceItemsList();
		BigDecimal cgst = null, sgst = null, igst = null, netamount = null, grossamount = null;
		for (InvoiceLine invoiceLine : invoiceLines) {
			cgst = invoiceLine.getCGST().add(invoice.getNetCGST());
			sgst = invoiceLine.getSGST().add(invoice.getNetSGST());
			igst = invoiceLine.getIGST().add(invoice.getNetIGST());
			netamount = invoiceLine.getNetAmount().add(invoice.getNetAmount());
			grossamount = invoiceLine.getGrossAmount().add(invoice.getNetCGST());

		}
		invoice.setNetCGST(cgst);
		invoice.setNetSGST(sgst);
		invoice.setNetIGST(igst);
		invoice.setNetAmount(netamount);
		invoice.setGrossAmount(grossamount);
		return invoice;
	}

	@Override
	public InvoiceLine Calculation(InvoiceLine invoiceline, Invoice invoice) {

		BigDecimal netamount = BigDecimal.ZERO;
		BigDecimal gross=BigDecimal.ZERO;
		BigDecimal gst = BigDecimal.ZERO;
		BigDecimal sgst = BigDecimal.ZERO;
		BigDecimal cgst = BigDecimal.ZERO;
		BigDecimal igst = BigDecimal.ZERO;
		BigDecimal valueigst = invoiceline.getNetAmount();
		BigDecimal value = invoiceline.getPrice().multiply(new BigDecimal(invoiceline.getQty()));

		netamount = netamount.add(value);
		invoiceline.setNetAmount(netamount);

		Address companyAddress = invoice.getCompany().getAddress();
		Address invoiceAddress = invoice.getInvoiceAddress();
		if (invoiceline.getProduct() != null) {
			if (companyAddress.getState().equals(invoiceAddress.getState())) {
				BigDecimal grossValues = BigDecimal.ZERO;

				gst = gst.add(invoiceline.getGstRate().multiply(netamount)).divide(new BigDecimal(100));
				BigDecimal dividevalue = gst.divide(new BigDecimal(2));
				sgst = sgst.add(dividevalue);
				cgst = sgst;
				invoiceline.setCGST(cgst);
				invoiceline.setSGST(sgst);
				invoiceline.setIGST(igst);
				valueigst = netamount.add(cgst);
				grossValues = cgst.add(valueigst);
				invoiceline.setGrossAmount(grossValues);

			} else {

				gst = gst.add(invoiceline.getGstRate().multiply(netamount)).divide(new BigDecimal(100));
				igst = igst.add(gst);
				value = netamount.add(igst);
				invoiceline.setGrossAmount(value);
				invoiceline.setIGST(igst);
				invoiceline.setCGST(cgst);
				invoiceline.setSGST(sgst);
			}
		} else {
			invoiceline.setGrossAmount(value);
			invoiceline.setIGST(igst);
			invoiceline.setCGST(cgst);
			invoiceline.setSGST(sgst);
			invoiceline.setGstRate(gst);
			invoiceline.setGrossAmount(gross);
			invoiceline.setItem(null);
		
		}

		return invoiceline;
	}

	@Override
	public Invoice partyDetails(Invoice invoice, boolean invoiceAddressAsShipping) {
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

			if (invoiceAddressAsShipping) {
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

	@Override
	public Invoice onclickAddress(Invoice invoice, boolean invoiceaddressAsShip) {
		Address invoiceaddress = invoice.getInvoiceAddress();
		Party party = invoice.getParty();
		Address address = null;
		if (party == null) {
			invoice.setShippingAddress(null);
		} else {

			if (invoiceaddressAsShip) {
				invoice.setShippingAddress(invoiceaddress);
			} else {
				List<Address> addresslist = party.getAddressList();
				for (Address a : addresslist) {
					if (a.getType().equals("shipping")) {
						address = a;
					}
				}
				invoice.setShippingAddress(address);

			}
		}
		return invoice;
	}
}
