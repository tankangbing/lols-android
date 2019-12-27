package com.example.spt.jaxb.course;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author zxh
 * 资源包Srcom标准XML映射Model
 */
@XStreamAlias("manifest")  
public class Manifest implements java.io.Serializable{
	/**
	 * 课件ID
	 */
    @XStreamAsAttribute
	String identifier="";
	/**
	 * 课件版本号，没有版本控制的课件默认为1.0
	 */
    @XStreamAsAttribute
	String version="1.0";
    /**
     * xml发布时间
     */
    @XStreamAsAttribute
    Timestamp publishTime;
    /**
	 * 课件包描述
	 */
	Metadata metadata;
	/**
	 * 课件结构，一般一个课件包含课件首页、课件架构两个结构
	 */
	Organizations organizations ;
	/**
	 * 课件资源
	 */
	Resources resources;


	
	public Manifest() {
		super();
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Organizations getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Organizations organizations) {
		this.organizations = organizations;
	}
	public Resources getResources() {
		return resources;
	}
	public void setResources(Resources resources) {
		this.resources = resources;
	}

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }
}
