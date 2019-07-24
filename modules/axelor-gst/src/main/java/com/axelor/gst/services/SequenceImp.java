package com.axelor.gst.services;

import com.axelor.gst.db.Sequence;

public class SequenceImp implements SequenceService{

	@Override
	public Sequence sequenceIncrement(Sequence seqs) {
		String prefix=seqs.getPrefix();
		String ssuffix=seqs.getSuffix();
		String nextNo=seqs.getNextNumber();
		int number=seqs.getPadding();
		int nextNolen=nextNo.length();
		int prefixlen=prefix.length();
		int suffixlen=ssuffix.length();
		
		System.out.println("total len:"+nextNolen);
		System.out.println("len of prefix :" + prefixlen );
		System.out.println("len of suffix :" + suffixlen);
		int endWith=nextNolen - suffixlen;
		String result = "";
		for(int i=prefixlen;i<endWith;i++)
		{
			
			result = result + String.valueOf(nextNo.charAt(i));
			System.out.println(nextNo.charAt(i));
			
		
		}
		String num="00";
		int num1 = Integer.parseInt(result) + 1;
		
		int len = number - (num1 + "").length();
		System.out.println("demo :" +len);
		String temp = "";
		
		for(int i = 0; i < len; i ++) {
			temp = "0" + temp;
		}
		int tm=temp.length();
		System.out.println(tm);
		  String newVersion = prefix + temp ;
		int val=(Integer.parseInt(result) + 1);
		System.out.println("result :" +result);
		System.out.println("result :" +val);
		System.out.println("version :"+newVersion);
		seqs.setNextNumber(newVersion +""+val +""+ssuffix);
		
		return seqs;
		
	
	}

	

}
