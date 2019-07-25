package com.axelor.gst.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceListCalculation extends JpaSupport {

	public void CalculateinvoiceList(ActionRequest request, ActionResponse response)
	{
		Invoice invoice=request.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceList=invoice.getInvoiceItemsList();
		List<InvoiceLine> invoiceUpdateList=new ArrayList<InvoiceLine>();
		
		if(invoice.getInvoiceItemsList()!=null)
		{
			
			for (InvoiceLine invoiceline : invoiceList) {
				BigDecimal netamount = BigDecimal.ZERO;
				BigDecimal value = invoiceline.getPrice().multiply(new BigDecimal(invoiceline.getQty()));

				netamount = netamount.add(value);
				invoiceline.setNetAmount(netamount);

				Address companyAddress = invoice.getCompany().getAddress();
				Address invoiceAddress = invoice.getInvoiceAddress();

				BigDecimal gst = BigDecimal.ZERO;

				if (companyAddress.getState().equals(invoiceAddress.getState())) {
					BigDecimal grossValues = BigDecimal.ZERO;
					BigDecimal sgst = BigDecimal.ZERO;
					BigDecimal cgst = BigDecimal.ZERO;
					BigDecimal igst = new BigDecimal("0.00");
					BigDecimal bg1 = new BigDecimal("2");
					BigDecimal valueigst = invoiceline.getNetAmount();
					gst = gst.add(invoiceline.getGstRate().multiply(valueigst));
					BigDecimal dividevalue = gst.divide(bg1);
					sgst = sgst.add(dividevalue);
					cgst = cgst.add(dividevalue);
					invoiceline.setCGST(cgst);
					invoiceline.setSGST(sgst);
					invoiceline.setIGST(igst);
					valueigst = valueigst.add(cgst);
					grossValues = cgst.add(valueigst);
					invoiceline.setGrossAmount(grossValues);
					
				} else {

					BigDecimal igst = BigDecimal.ZERO;
					BigDecimal cgst = new BigDecimal("0.00");
					BigDecimal sgst = new BigDecimal("0.00");
					BigDecimal valueigst = invoiceline.getNetAmount();
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
			response.setValue("invoiceItemsList", invoiceUpdateList);
		}
		else
		{
			System.out.println("wowww");
		}
			
	}
	public void onCalculation(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceLines = invoice.getInvoiceItemsList();
		BigDecimal cgst = null, sgst = null, igst = null, netamount = null, grossamount = null;
		Integer qty = null;
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

		response.setValue("netAmount", invoice.getNetAmount());
		response.setValue("netSGST", invoice.getNetSGST());
		response.setValue("netCGST", invoice.getNetCGST());
		response.setValue("grossAmount", invoice.getGrossAmount());

	}

}
