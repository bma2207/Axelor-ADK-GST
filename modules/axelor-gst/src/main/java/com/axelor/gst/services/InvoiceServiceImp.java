package com.axelor.gst.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;

public class InvoiceServiceImp implements InvoiceService{
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

						Address companyAddress = invoice.getCompany().getAddress();
						Address invoiceAddress = invoice.getInvoiceAddress();

						BigDecimal gst = BigDecimal.ZERO;
						BigDecimal grossValues = BigDecimal.ZERO;
						BigDecimal sgst = BigDecimal.ZERO;
						BigDecimal cgst = BigDecimal.ZERO;
						BigDecimal igst = BigDecimal.ZERO;
						BigDecimal valueigst = invoiceline.getNetAmount();
						if (companyAddress.getState().equals(invoiceAddress.getState())) {
							gst = gst.add(invoiceline.getGstRate().multiply(valueigst));
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

							gst = gst.add(invoiceline.getGstRate().multiply(valueigst));
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
		

		if (invoice.getInvoiceItemsList() != null) {

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
			
			
		} else {
			System.out.println("hello");
		}

		return  invoice;
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
			return  invoice;
	}
	@Override
	public InvoiceLine invoiceLineCalculation(Invoice invoice) {
		// TODO Auto-generated method stub
		return null;
	}

}