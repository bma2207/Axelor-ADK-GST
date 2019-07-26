package com.axelor.gst.controller;

import org.apache.poi.hslf.record.Sound;
import org.hibernate.validator.constraints.Length;

import com.axelor.common.StringUtils;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.services.SequenceImp;
import com.axelor.gst.services.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ibm.icu.text.DecimalFormat;

public class SequenceCreate extends JpaSupport {

	@Inject 
	private SequenceImp service; 
	public void generateSequence(ActionRequest request, ActionResponse response) 
	{
		Sequence seq=request.getContext().asType(Sequence.class);
		String prefix=seq.getPrefix();
		String ssuffix=seq.getSuffix();
		
		int number=seq.getPadding();
		
		
		String num="00";
		int num1 = Integer.parseInt(num) + 1;
		
		int len = number - (num1 + "").length();
		System.out.println("demo :" +len);
		String temp = "" + num1;
		
		for(int i = 0; i < len; i ++) {
			temp = "0" + temp;
		}
		seq.setNextNumber(prefix +""+temp + ""+ ssuffix);
		response.setValue("nextNumber", seq.getNextNumber());	
	
	}
	public void incrementNo(ActionRequest request, ActionResponse response)
	{
		Sequence seqs=Beans.get(SequenceRepository.class).find((Long) request.getContext().get("id"));
		seqs =service.sequenceIncrement(seqs);
		response.setValue("nextNumber", seqs.getNextNumber());
		
	}
	
	@Transactional
	public void setSequence(ActionRequest request, ActionResponse response)
	{ 
		Party partys=request.getContext().asType(Party.class);
		
		if(partys.getReference() == null) {
		Sequence sequence=Beans.get(SequenceRepository.class).all().filter("self.model.name = ?1", "Party").fetchOne();
		String reference= sequence.getNextNumber();
		Party party=new Party();
		party.setReference(reference);
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
		if(invoice.getReference() == null) {
		Sequence sequence=Beans.get(SequenceRepository.class).all().filter("self.model.name = ?1", "Invoice").fetchOne();
		String reference= sequence.getNextNumber();
		Party party=new Party();
		party.setReference(reference);
		response.setValue("reference", sequence.getNextNumber());	
		sequence =service.sequenceIncrement(sequence);
		Beans.get(SequenceRepository.class).save(sequence);
		}
		else
		{
			
		}
	}
	
}
