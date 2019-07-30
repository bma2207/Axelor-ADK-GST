package com.axelor.gst.services;

import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceService {
 
	public List<InvoiceLine> invoiceList(Invoice invoice);
	public Invoice invoiceCalculation(Invoice invoice);
	public InvoiceLine Calculation(InvoiceLine invoiceline,Invoice invoice);
}
