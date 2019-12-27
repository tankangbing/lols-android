package com.example.spt.jaxb.course;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 课件模块的结构
 * @author zxh
 *
 */
@XStreamAlias("organization") 
public class Organization implements java.io.Serializable{
	/**
	 * ID
	 */
    @XStreamAsAttribute
	String identifier;
	/**
	 * 课件模块的结构类型，默认为hierarchical  树形结构
	 */
    @XStreamAsAttribute
	String structure="hierarchical";
	/**
	 * 标题
	 */
	String title;

	/**
	 * 课件模块的子项
	 */
    @XStreamImplicit(itemFieldName="item")
	List<Item> item;

	/**
	 * 显示社区
	 */
	@XStreamAsAttribute
	String showCommunity;
	/**
	 * 课件
	 */
	@XStreamAsAttribute
	String showCourseware;

	/**
	 * 显示通知
	 */
	@XStreamAsAttribute
	String showMessage;

	/**
	 * 显示进度
	 */
	@XStreamAsAttribute
	String showSchedule;

	/**
	 * 显示概况
	 */
	@XStreamAsAttribute
	String showSummarize;

	/**
	 * 显示作业
	 */
	@XStreamAsAttribute
	String showWork;

	public Organization(){
		
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getStructure() {
		return structure;
	}
	public void setStructure(String structure) {
		this.structure = structure;
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

	public String getShowCommunity() {
		return showCommunity;
	}

	public void setShowCommunity(String showCommunity) {
		this.showCommunity = showCommunity;
	}

	public String getShowCourseware() {
		return showCourseware;
	}

	public void setShowCourseware(String showCourseware) {
		this.showCourseware = showCourseware;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public String getShowSchedule() {
		return showSchedule;
	}

	public void setShowSchedule(String showSchedule) {
		this.showSchedule = showSchedule;
	}

	public String getShowSummarize() {
		return showSummarize;
	}

	public void setShowSummarize(String showSummarize) {
		this.showSummarize = showSummarize;
	}

	public String getShowWork() {
		return showWork;
	}

	public void setShowWork(String showWork) {
		this.showWork = showWork;
	}
}
