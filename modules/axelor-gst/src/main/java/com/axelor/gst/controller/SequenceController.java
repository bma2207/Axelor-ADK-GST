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

public class SequenceController extends JpaSupport {
	@Inject
	private SequenceImp service;

	public void generateSequence(ActionRequest request, ActionResponse response) {
		Sequence seq = request.getContext().asType(Sequence.class);
		seq = service.generateSequence(seq);
		response.setValues(seq);

	}

	@Transactional
	public void setSequence(ActionRequest request, ActionResponse response) {
		Party partys = request.getContext().asType(Party.class);

		if (partys.getReference() == null) {
			Sequence sequence = service.sequenceSet("Party");
			response.setValue("reference", sequence.getNextNumber());
			sequence = service.sequenceIncrement(sequence);
			Beans.get(SequenceRepository.class).save(sequence);
		}
	}

	@Transactional
	public void setSequences(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		if (invoice.getReference() == null) {
			Sequence sequence = service.sequenceSet("Invoice");
			response.setValue("reference", sequence.getNextNumber());
			sequence = service.sequenceIncrement(sequence);
			Beans.get(SequenceRepository.class).save(sequence);
		}
	}
}