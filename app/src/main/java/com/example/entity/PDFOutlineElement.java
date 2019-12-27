package com.example.entity;

public class PDFOutlineElement {
	private String id;
	//标题
	private String outlineTitle ;
	//是否有父目录
	private boolean mhasParent;
	//是否有子目录
	private boolean mhasChild ;
    //父目录
	private String parent;

	private String identifier;
	private int level;
    //学习行为
	private String behavior;
	private String charterId;

    //选择状态 0未选择 1被选中
    private int statusXz;
    //下载状态 0未下载 1已下载
    private int statusDl;
    private int resourceSize;

	private PDFOutlineElement parentElement;
	//子目录大小
	private int childSize;

	/**
	 * 是否显示进度页
	 */
	String showSchedule ;
	/**
	 * 是否显示提示页
	 */
	String showNotice;
	/**
	 * 是否显示评价页
	 */
	String showEvaluate;
	/**
	 * 是否显示问答页
	 */
	String showAsk;


    /**
     * 标识过期时间
     */
    String expirationDate;
    /**
     * 是否新增
     */
    String isNew;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOutlineTitle() {
		return outlineTitle;
	}

	public void setOutlineTitle(String outlineTitle) {
		this.outlineTitle = outlineTitle;
	}

	public boolean isMhasParent() {
		return mhasParent;
	}

	public void setMhasParent(boolean mhasParent) {
		this.mhasParent = mhasParent;
	}

	public boolean isMhasChild() {
		return mhasChild;
	}

	public void setMhasChild(boolean mhasChild) {
		this.mhasChild = mhasChild;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}


	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	public String getBehavior() {
		return behavior;
	}

	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}

	public String getCharterId() {
		return charterId;
	}
    public int getStatusXz() {
        return statusXz;
    }

    public void setStatusXz(int statusXz) {
        this.statusXz = statusXz;
    }

    public int getStatusDl() {
        return statusDl;
    }

    public void setStatusDl(int statusDl) {
        this.statusDl = statusDl;
    }

	public void setCharterId(String charterId) {
		this.charterId = charterId;
	}

    public int getResourceSize() {
        return resourceSize;
    }

    public void setResourceSize(int resourceSize) {
        this.resourceSize = resourceSize;
    }

	public PDFOutlineElement getParentElement() {
		return parentElement;
	}

	public void setParentElement(PDFOutlineElement parentElement) {
		this.parentElement = parentElement;
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

    //private OutlineElement outlineElement;
	private boolean expanded;

	public int getChildSize() {
		return childSize;
	}

	public void setChildSize(int childSize) {
		this.childSize = childSize;
	}
/**
	 * 下载页面的构造函数
	 * @param id	id
	 * @param outlineTitle	标题
	 * @param level	级
	 * @param identifier	行为id
	 * @param behavior	资源代号
	 * @param charterId	章节id
	 * @param parentElement	父对象
	 */
	/**
	 *
	 * @param id	id
	 * @param outlineTitle	标题
	 * @param level	级
	 * @param identifier	行为id
	 * @param behavior	资源代号
	 * @param charterId	章节id
	 * @param statusXz
	 * @param resourceSize
	 * @param parentElement
	 */
	public PDFOutlineElement(String id, String outlineTitle, int level,String identifier,String behavior,String charterId,
							 int statusXz,int resourceSize,PDFOutlineElement parentElement) {
		super();
		this.id = id;
		this.outlineTitle = outlineTitle;
		this.level = level;
		this.identifier = identifier;
		this.behavior = behavior;
		this.charterId =charterId;
		this.parentElement =parentElement;
		this.statusXz = statusXz;
		this.resourceSize = resourceSize;
	}

	public PDFOutlineElement(String id, String outlineTitle,
							  boolean mhasParent, boolean mhasChild, String parent, int level,
							 boolean expanded ,String identifier,String behavior,String charterId,PDFOutlineElement parentElement,String expirationDate,String isNew) {
		super();
		this.id = id;
		this.outlineTitle = outlineTitle;
		this.mhasParent = mhasParent;
		this.mhasChild = mhasChild;
		this.parent = parent;
		this.level = level;
		this.expanded = expanded;
		this.identifier = identifier;
		this.behavior = behavior;
		this.charterId =charterId;
		this.parentElement =parentElement;
        this.expirationDate =expirationDate;
        this.isNew =isNew;
	}
	public PDFOutlineElement(String id, String outlineTitle,
							 boolean mhasParent, boolean mhasChild, String parent, int level,
							 boolean expanded ,String identifier,String behavior,String charterId) {
		super();
		this.id = id;
		this.outlineTitle = outlineTitle;
		this.mhasParent = mhasParent;
		this.mhasChild = mhasChild;
		this.parent = parent;
		this.level = level;
		this.expanded = expanded;
		this.identifier = identifier;
		this.behavior = behavior;
		this.charterId =charterId;
	}
}
