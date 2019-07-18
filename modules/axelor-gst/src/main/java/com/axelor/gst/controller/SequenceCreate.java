package com.axelor.gst.controller;

import com.axelor.common.StringUtils;
import com.axelor.db.JpaSupport;
import com.axelor.gst.db.Sequence;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.ibm.icu.text.DecimalFormat;

public class SequenceCreate extends JpaSupport {

	public void generateSequence(ActionRequest request, ActionResponse response) 
	{
		Sequence seq=request.getContext().asType(Sequence.class);
		String prefix=seq.getPrefix();
		String ssuffix=seq.getSuffix();
		String nextNo=seq.getNextNumber();
		int number=seq.getPadding();
		
		String num ="00";
		
		int num1 = Integer.parseInt(num) + 1;
		 System.out.println(num1);
		int len = number - (num1 + "").length();
		String temp = "" + num1;
		
		for(int i = 0; i < len; i ++) {
			temp = "0" + temp;
		}
		seq.setNextNumber(prefix +""+temp + ""+ ssuffix);
		response.setValue("nextNumber", seq.getNextNumber());
		System.out.println(prefix +""+temp + ""+ ssuffix);
		
		
	
	}
}
