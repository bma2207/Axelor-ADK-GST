package com.axelor.gst.services;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;

public class SequenceImp implements SequenceService {

	@Override
	public Sequence sequenceIncrement(Sequence seqs) {
		String prefix = seqs.getPrefix();
		String ssuffix = seqs.getSuffix();
		String nextNo = seqs.getNextNumber();
		String num = "00";
		int number = seqs.getPadding();
		int nextNolen = nextNo.length();
		int prefixlen = prefix.length();
		int suffixlen = ssuffix.length();
		int endWith = nextNolen - suffixlen;
		String result = "";

		for (int i = prefixlen; i < endWith; i++) {
			result = result + String.valueOf(nextNo.charAt(i));
		}

		int num1 = Integer.parseInt(result) + 1;
		int len = number - (num1 + "").length();
		String temp = "";
		for (int i = 0; i < len; i++) {
			temp = "0" + temp;
		}
		int tm = temp.length();
		String newVersion = prefix + temp;
		int val = (Integer.parseInt(result) + 1);
		seqs.setNextNumber(newVersion + "" + val + "" + ssuffix);
		return seqs;
	}

	@Override
	public Sequence generateSequence(Sequence sequence) {

		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		int number = sequence.getPadding();
		String num = "00";
		int num1 = Integer.parseInt(num) + 1;
		int len = number - (num1 + "").length();
		String temp = "" + num1;

		for (int i = 0; i < len; i++) {
			temp = "0" + temp;
		}
		sequence.setNextNumber(prefix + "" + temp + "" + suffix);

		return sequence;
	}

	@Override
	public Sequence sequenceSet(String domainName) {
		Sequence sequence = Beans.get(SequenceRepository.class).all().filter("self.model.name = ?1", domainName)
				.fetchOne();

		return sequence;
	}

}
