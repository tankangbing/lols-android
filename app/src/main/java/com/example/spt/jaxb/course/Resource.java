package com.example.spt.jaxb.course;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("resource")
public class Resource implements java.io.Serializable{
	/**
	 * 资源
	 */
	Files file;
    @XStreamAsAttribute
	String identifier;
    @XStreamAsAttribute
	String type = "webcontent";
    @XStreamAsAttribute
	String href;
	/**完成情况   0：未开始 1：进行中 2：已完成*/
	String finishStatus;
	
	public Files getFile() {
		return file;
	}
	public void setFile(Files file) {
		this.file = file;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	
}
