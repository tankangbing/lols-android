package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;
/**
 * 试卷题型
 * @author zxh
 *
 */
@XStreamAlias("questiontypes")
public class Questiontypes implements Serializable {


    @XStreamImplicit(itemFieldName="questiontype")
	List<Questiontype> questiontype;

	public List<Questiontype> getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(List<Questiontype> questiontype) {
		this.questiontype = questiontype;
	}
	
	
	
	
}
