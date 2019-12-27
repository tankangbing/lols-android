package com.example.spt.jaxb.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;
/**
 * 题目
 * @author zxh
 *
 */
@XStreamAlias("question")
public class Question implements Serializable{
    /**
     * 题目ID
     */
    @XStreamAsAttribute
    String id;
    /**
     * 父题目ID
     */
    @XStreamAsAttribute
    String parentid;
    /**
     * 题目类型
     */
    @XStreamAsAttribute
    String type;
    /**
     * 提干
     */
    String content;
    /**
     * 题目选项
     */
    @XStreamImplicit(itemFieldName="option")
    List<Option> option;
    /**
     * 题目参考答案
     */
    Answer answer;
    /**
     * 题目解析
     */
    Explain explain;
    @XStreamImplicit(itemFieldName="coursenodes")
    List<Coursenodes> coursenodes;


    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<Option> getOption() {
		return option;
	}
	public void setOption(List<Option> option) {
		this.option = option;
	}
	public Answer getAnswer() {
		return answer;
	}
	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	public Explain getExplain() {
		return explain;
	}
	public void setExplain(Explain explain) {
		this.explain = explain;
	}
	public List<Coursenodes> getCoursenodes() {
		return coursenodes;
	}
	public void setCoursenodes(List<Coursenodes> coursenodes) {
		this.coursenodes = coursenodes;
	}
	

}
