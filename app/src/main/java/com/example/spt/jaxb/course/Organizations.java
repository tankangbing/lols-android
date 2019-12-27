package com.example.spt.jaxb.course;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.example.spt.jaxb.course.Organization;
/**
 * 
 * @author zxh
 *	璇句欢缁撴瀯
 */
@XStreamAlias("organizations") 
public class Organizations implements java.io.Serializable{
	/**
	 * 默认的显示模块，默认为sy
	 */
    @XStreamAsAttribute
    private String defult="sy";
	/**
	 * 课件结构，一般一个课件包含课件首页、课件架构两个结构
	 */
    @XStreamImplicit(itemFieldName="organization")
    private List<Organization> organization=new ArrayList<Organization>();
	
	public Organizations(){
		
	}
	
	public String getDefult() {
		return defult;
	}
	
	public void setDefult(String defult) {
		this.defult = defult;
	}

	public List<Organization> getOrganization() {
		return organization;
	}

	public void setOrganization(List<Organization> organization) {
		this.organization = organization;
	}
	
}
