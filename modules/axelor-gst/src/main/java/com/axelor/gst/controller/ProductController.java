package com.axelor.gst.controller;

import java.util.ArrayList;
import java.util.List;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;


public class ProductController extends JpaSupport {

	public void productIdsList(ActionRequest request, ActionResponse response) {

		if (request.getContext().get("productIds") != null) {
			Invoice invoice = request.getContext().asType(Invoice.class);
			List<Integer> productids = (List<Integer>) request.getContext().get("productIds");
			List<Product> product = Beans.get(ProductRepository.class).all().filter("self.id in (?1) ", productids)
					.fetch();
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
			response.setValue("invoiceItemsList", invoiceList);
		}
	}
}
