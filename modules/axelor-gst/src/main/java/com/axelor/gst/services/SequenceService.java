package com.axelor.gst.services;

import com.axelor.gst.db.Sequence;

public interface SequenceService {

	public Sequence sequenceIncrement(Sequence seqs);
}
