package com.axelor.gst.services;

import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface ProductService {

	public List<InvoiceLine> productList(Invoice invoice,List<Integer> pro);
}
