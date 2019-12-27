package com.example.jsonReturn;

import java.util.List;
import java.util.Map;

import com.example.entity.Item;
import com.example.entity.LearningBehavior;

/**
 * 单个课程返回
 */
public class CourseReturn {
	//是否成功
	private boolean success;
	//xml
	private String xml;

	private List<Item> courseItemList;
	private String courseWareTitle;
	private Map<String, List<LearningBehavior>> LearningBehaviorMap;//video--视频  doc--文档  html--网页 exercise--练习  test--测试 exam--考试
	public List<Item> getCourseItemList() {
		return courseItemList;
	}
	public void setCourseItemList(List<Item> courseItemList) {
		this.courseItemList = courseItemList;
	}
	public String getCourseWareTitle() {
		return courseWareTitle;
	}
	public void setCourseWareTitle(String courseWareTitle) {
		this.courseWareTitle = courseWareTitle;
	}
	public Map<String, List<LearningBehavior>> getLearningBehaviorMap() {
		return LearningBehaviorMap;
	}
	public void setLearningBehaviorMap(
			Map<String, List<LearningBehavior>> learningBehaviorMap) {
		LearningBehaviorMap = learningBehaviorMap;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
