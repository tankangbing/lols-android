package com.example.entity;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TreeNode {

	private Integer index;

	private String name;//节点名称

	private String id;//节点别名(用作ID)

	private boolean open;//是否打开

	private String parentId ;//父ID级别

	private boolean checked;//是否选择

	private Integer level;//层级

	private Set<TreeNode> nodes = new LinkedHashSet<TreeNode>(0);//子节点

	private Map<String, Object> exAttribute = new HashMap<String, Object>();//扩展属性，key - value 形式



	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getExAttribute() {
		return exAttribute;
	}

	public void setExAttribute(Map<String, Object> exAttribute) {
		this.exAttribute = exAttribute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Set<TreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(Set<TreeNode> nodes) {
		this.nodes = nodes;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}




}
