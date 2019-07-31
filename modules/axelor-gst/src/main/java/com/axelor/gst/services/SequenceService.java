package com.axelor.gst.services;

import com.axelor.gst.db.Sequence;

public interface SequenceService {

	public Sequence generateSequence(Sequence sequence);

	public Sequence sequenceIncrement(Sequence seqs);
}
