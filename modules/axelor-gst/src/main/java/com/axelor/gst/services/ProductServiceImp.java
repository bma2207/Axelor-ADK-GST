package com.axelor.gst.services;

import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;

public class ProductServiceImp implements ProductService {

	@Override
	public List<InvoiceLine> productList(Invoice invoice, List<Integer> pro) {
		List<Product> product = Beans.get(ProductRepository.class).all().filter("self.id in (?1) ", pro).fetch();
		List<InvoiceLine> invoiceList = new ArrayList<InvoiceLine>();

		for (Product productObj : product) {
			InvoiceLine invoiceLine = new InvoiceLine();
			invoiceLine.setProduct(productObj);
			invoiceLine.setGstRate(productObj.getGstRate());
			invoiceLine.setPrice(productObj.getSalePrice());
			invoiceLine.setItem('[' + productObj.getCode() + ']' + productObj.getName());
			invoiceLine.setQty(1);
			invoiceList.add(invoiceLine);
		}
		return invoiceList;
	}

}
