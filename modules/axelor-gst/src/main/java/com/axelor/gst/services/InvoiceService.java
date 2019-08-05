package com.axelor.gst.services;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceService {

	public Invoice invoiceCalculation(Invoice invoice);

	public InvoiceLine Calculation(InvoiceLine invoiceline, Invoice invoice);

	public Invoice partyDetails(Invoice invoice);

}
