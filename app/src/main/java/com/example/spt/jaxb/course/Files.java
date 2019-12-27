package com.example.spt.jaxb.course;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("file")
public class Files implements java.io.Serializable{
    @XStreamAsAttribute
	String href = "";

	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	
	
}
