package com.axelor.gst.controller;

import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.services.SequenceImp;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SequenceController extends JpaSupport{
	@Inject 
	private SequenceImp service; 
	public void generateSequence(ActionRequest request, ActionResponse response) 
	{
		Sequence seq=request.getContext().asType(Sequence.class);
		String prefix=seq.getPrefix();
		String suffix=seq.getSuffix();
		int number=seq.getPadding();
		String num="00";
		int num1 = Integer.parseInt(num) + 1;
		int len = number - (num1 + "").length();
		String temp = "" + num1;
		
		for(int i = 0; i < len; i ++) {
			temp = "0" + temp;
		}
		seq.setNextNumber(prefix +""+temp + ""+ suffix);
		response.setValue("nextNumber", seq.getNextNumber());	
	
	}
	@Transactional
	public void setSequence(ActionRequest request, ActionResponse response)
	{ 
		Party partys=request.getContext().asType(Party.class);
		
		if(partys.getReference() == null) {
		Sequence sequence=Beans.get(SequenceRepository.class).all().filter("self.model.name = ?1", "Party").fetchOne();
		String reference= sequence.getNextNumber();
		partys.setReference(reference);
		response.setValue("reference", sequence.getNextNumber());	
		sequence =service.sequenceIncrement(sequence);
		Beans.get(SequenceRepository.class).save(sequence);
		}
		else
		{
			
		}
	}
	@Transactional
	public void setSequences(ActionRequest request, ActionResponse response)
	{
		Invoice invoice = request.getContext().asType(Invoice.class);
		if(invoice.getReference() == null && invoice.getStatus() == "validated") {
			
		Sequence sequence=Beans.get(SequenceRepository.class).all().filter("self.model.name = ?1", "Invoice").fetchOne();
		String reference= sequence.getNextNumber();
		invoice.setReference(reference);
		response.setValue("reference", sequence.getNextNumber());	
		sequence =service.sequenceIncrement(sequence);
		Beans.get(SequenceRepository.class).save(sequence);
		}
		else
		{
			
		}
	}
}