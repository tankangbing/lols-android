package com.example.spt.jaxb.course;

import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 课件模块的子项
 * @author zxh
 *
 */
@XStreamAlias("item") 
public class Item implements java.io.Serializable{
	/**
	 * 课件节点名称
	 */
	String title;
	/**
	 * 是否可见
	 */
    @XStreamAsAttribute
	String isvisible="true";
	/**
	 * 课件架构节点ID
	 */
    @XStreamAsAttribute
	String identifier;
	/**
	 * 课件架构引用资源的ID
	 */
    @XStreamAsAttribute
	String identifierref;
    /**
     * 标识过期时间
     */
    @XStreamAsAttribute
    String expirationDate;
    /**
     * 是否新增 N Y
     */
    @XStreamAsAttribute
    String isNew;
    /**
     * 课件大小
     */
    @XStreamAsAttribute
    String resourceSize;
	/**
	 * 子节点
	 */
    @XStreamImplicit(itemFieldName="item")
	List<Item> item;
	/**完成情况   0：未开始 1：进行中 2：已完成*/
	String finishStatus;
	/**完成百分比或分数*/
	String finishPercent;

	/**
	 * 是否显示进度页
	 */
	@XStreamAsAttribute
	String showSchedule ;
	/**
	 * 是否显示提示页
	 */
	@XStreamAsAttribute
	String showNotice;
	/**
	 * 是否显示评价页
	 */
	@XStreamAsAttribute
	String showEvaluate;
	/**
	 * 是否显示问答页
	 */
	@XStreamAsAttribute
	String showAsk;


	
	public String getIsvisible() {
		return isvisible;
	}
	public void setIsvisible(String isvisible) {
		this.isvisible = isvisible;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getIdentifierref() {
		return identifierref;
	}
	public void setIdentifierref(String identifierref) {
		this.identifierref = identifierref;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Item> getItem() {
		return item;
	}
	public void setItem(List<Item> item) {
		this.item = item;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getFinishPercent() {
		return finishPercent;
	}
	public void setFinishPercent(String finishPercent) {
		this.finishPercent = finishPercent;
	}

    public String getResourceSize() {
        return resourceSize;
    }

    public void setResourceSize(String resourceSize) {
        this.resourceSize = resourceSize;
    }

	public String getShowSchedule() {
		return showSchedule;
	}

	public void setShowSchedule(String showSchedule) {
		this.showSchedule = showSchedule;
	}

	public String getShowNotice() {
		return showNotice;
	}

	public void setShowNotice(String showNotice) {
		this.showNotice = showNotice;
	}

	public String getShowEvaluate() {
		return showEvaluate;
	}

	public void setShowEvaluate(String showEvaluate) {
		this.showEvaluate = showEvaluate;
	}

	public String getShowAsk() {
		return showAsk;
	}

	public void setShowAsk(String showAsk) {
		this.showAsk = showAsk;
	}

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
}
