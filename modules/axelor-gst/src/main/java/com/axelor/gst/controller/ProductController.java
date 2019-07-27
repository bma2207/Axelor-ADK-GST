package com.axelor.gst.controller;

import java.util.ArrayList;
import java.util.List;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.services.ProductServiceIMP;
import com.axelor.i18n.I18n;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class ProductController extends JpaSupport {
	@Inject
	private ProductServiceIMP service;

	public void openPrintWizard(ActionRequest request, ActionResponse response) {
		Product product = request.getContext().asType(Product.class);

		@SuppressWarnings("unchecked")
		List<Integer> lstSelectedLocations = (List<Integer>) request.getContext().get("_ids");
		if (lstSelectedLocations != null) {
			response.setView(ActionView.define(I18n.get("hello")).model("com.axelor.gst.db.Invoice")
					.add("form", "invoice-form").context("productIds", lstSelectedLocations).map());
		}
	}

	public void productIdsList(ActionRequest request, ActionResponse response) {

		if (request.getContext().get("productIds") != null) {
			Invoice invoice = request.getContext().asType(Invoice.class);
			List<Integer> productids = (List<Integer>) request.getContext().get("productIds");
			List<Product> productList = (List<Product>) service.productList(productids);

			List<InvoiceLine> invoiceList = new ArrayList<InvoiceLine>();

			for (Product productObj : productList) {
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
