package com.axelor.gst.controller;

import java.math.BigDecimal;

import org.eclipse.birt.data.aggregation.calculator.BigDecimalCalculator;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
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
			sgst=sgst.add(dividevalue);
			cgst = cgst.add(dividevalue);
			invoiceline.setCGST(cgst);
			invoiceline.setSGST(sgst);
			valueigst=valueigst.add(cgst);
			grossValues=cgst.add(valueigst);
			invoiceline.setGrossAmount(grossValues);
			response.setValue("grossAmount",invoiceline.getGrossAmount());
			response.setValue("CGST", invoiceline.getCGST());
			response.setValue("SGST", invoiceline.getSGST());
		}
		else {
			BigDecimal grossValues = BigDecimal.ZERO;
			BigDecimal igst = BigDecimal.ZERO;
			BigDecimal valueigst = invoiceline.getNetAmount();
			gst = gst.add(invoiceline.getGstRate().multiply(valueigst));
			igst = igst.add(gst);
			valueigst=valueigst.add(igst);
			invoiceline.setGrossAmount(valueigst);
			invoiceline.setIGST(igst);
			response.setValue("IGST", invoiceline.getIGST());
			response.setValue("grossAmount",invoiceline.getGrossAmount());
		}
	}
}