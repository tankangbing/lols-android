package com.example.entity;

import java.util.List;
/**
 * 课件模块的子项
 *
 */
public class Item implements java.io.Serializable{
	/**
	 * 课件节点名称
	 */
	String title;
	/**
	 * 是否可见
	 */
	String isvisible="true";
	/**
	 * 课件架构节点ID
	 */
	String identifier;
	/**
	 * 课件架构引用资源的ID
	 */
	String identifierref;

	/**
	 * 子节点
	 */
	List<Item> item;
	/**完成情况   0：未开始 1：进行中 2：已完成*/
	String finishStatus;
	/**完成百分比或分数*/
	String finishPercent;

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
}