package com.example.entity;

import com.example.view.SlideView;

import java.io.Serializable;

/**
 * 封裝所下載文件的信息 將FileInfo序列化，可用於Intent傳遞對象
 *
 */
public class FileInfo implements Serializable {
	/**
	 * 文件的ID
	 */
	private int id;
	/**
	 * 课程id
	 */
	private String class_id;
	/**
	 * 行为id
	 */
	private String behavior_id;
	/**
	 * 用户id
	 */
	private String student_id;
	/**
	 *存储的文件名
	 */
	private String file_name;
	/**
	 * 原文件名
	 */
	private String attach_name;
	/**
	 * 文件格式
	 */
	private String file_type;
	/**
	 * 文件格式编码
	 */
	private String file_code;
	/**
	 * 文件的总大小
	 */
	private int file_size;
	/**
	 * 文件已完成多少
	 */
	private int file_finish;
	/**
	 * 存储相对路径
	 */
	private String savepath;
	/**
	 * 创建日期
	 */
	private String create_date;

	/**
	 * 文件的下载地址
	 */
	private String url;
	/**
	 * 进度条状态
	 * 0:暂停
	 * 1:下载中
	 * 2:完成
	 */
	private int progress_status;
	/**
	 * 是否被选择
	 */
	private boolean isSelect;

	//视频专属属性
	/**
	 * 文件总时长
	 */
	private int file_time;
	/**
	 * 上一次点击时间
	 */
	private static long lastClickTime;
	/**
	 * 每个线程每秒下载速度
	 */
	private int secondDownloadSize;
	/**
	 * 整个文件的每秒下载速度
	 */
	private int secondDownloadSizeTotal;
	/**
	 * 章节点
	 */
	private String chapter ;
	/**
	 * 章节点名称
	 */
	private String chapterName ;

	private String show_schedule;
	private String show_notice;
	private String show_evaluate;
	private String show_ask;


    public SlideView slideView;

	/**
	 * 对应的xml数据
	 */
	private PDFOutlineElement pdfOutlineElement ;

	public FileInfo() {
		super();
	}


	public FileInfo(String class_id, String student_id,String behavior_id,String chapter, String file_name, String file_code,int progress_status) {
		super();
		this.class_id = class_id;
		this.student_id =student_id;
		this.behavior_id = behavior_id;
		this.file_name = file_name;
		this.progress_status = progress_status;
		this.file_code =file_code;
		this.chapter =chapter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getBehavior_id() {
		return behavior_id ==null? "":behavior_id;
	}

	public void setBehavior_id(String behavior_id) {
		this.behavior_id = behavior_id;
	}

	public String getFile_name() {
		return file_name ==null? "":file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_type() {
		return file_type ==null? "":file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getFile_code() {
		return file_code ==null? "":file_code;
	}

	public void setFile_code(String file_code) {
		this.file_code = file_code;
	}

	public int getFile_size() {
		return file_size;
	}

	public void setFile_size(int file_size) {
		this.file_size = file_size;
	}

	public int getFile_finish() {
		return file_finish;
	}

	public void setFile_finish(int file_finish) {
		this.file_finish = file_finish;
	}

	public String getSavepath() {
		return savepath ==null? "":savepath;
	}

	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getUrl() {
		return url ==null? "":url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getProgress_status() {
		return progress_status;
	}

	public void setProgress_status(int progress_status) {
		this.progress_status = progress_status;
	}


	public String getAttach_name() {
		return attach_name ==null? "":attach_name;
	}

	public void setAttach_name(String attach_name) {
		this.attach_name = attach_name;
	}

	public int getFile_time() {
		return file_time;
	}

	public void setFile_time(int file_time) {
		this.file_time = file_time;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}

	public static long getLastClickTime() {
		return lastClickTime;
	}

	public static void setLastClickTime(long lastClickTime) {
		FileInfo.lastClickTime = lastClickTime;
	}

	public int getSecondDownloadSize() {
		return secondDownloadSize;
	}

	public void setSecondDownloadSize(int secondDownloadSize) {
		this.secondDownloadSize = secondDownloadSize;
	}

	public int getSecondDownloadSizeTotal() {
		return secondDownloadSizeTotal;
	}

	public void setSecondDownloadSizeTotal(int secondDownloadSizeTotal) {
		this.secondDownloadSizeTotal = secondDownloadSizeTotal;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public PDFOutlineElement getPdfOutlineElement() {
		return pdfOutlineElement;
	}

	public void setPdfOutlineElement(PDFOutlineElement pdfOutlineElement) {
		this.pdfOutlineElement = pdfOutlineElement;
	}

	public String getClass_id() {
		return class_id==null? "":class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getShow_schedule() {
		return show_schedule ==null? "":show_schedule;
	}

	public void setShow_schedule(String show_schedule) {
		this.show_schedule = show_schedule;
	}

	public String getShow_notice() {
		return show_notice==null? "":show_notice;
	}

	public void setShow_notice(String show_notice) {
		this.show_notice = show_notice;
	}

	public String getShow_evaluate() {
		return show_evaluate==null? "":show_evaluate;
	}

	public void setShow_evaluate(String show_evaluate) {
		this.show_evaluate = show_evaluate;
	}

	public String getShow_ask() {
		return show_ask==null? "":show_ask;
	}

	public void setShow_ask(String show_ask) {
		this.show_ask = show_ask;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FileInfo)) {
			return false;
		}
		FileInfo b = (FileInfo)obj;
		if(this.id==(b.getId())) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		String code = id + file_name;
		return code.hashCode();
	}

	@Override
	public String toString() {
		return "FileInfo{" +
				"id=" + id +
				", class_id='" + class_id + '\'' +
				", behavior_id='" + behavior_id + '\'' +
				", student_id='" + student_id + '\'' +
				", file_name='" + file_name + '\'' +
				", attach_name='" + attach_name + '\'' +
				", file_type='" + file_type + '\'' +
				", file_code='" + file_code + '\'' +
				", file_size=" + file_size +
				", file_finish=" + file_finish +
				", savepath='" + savepath + '\'' +
				", create_date='" + create_date + '\'' +
				", url='" + url + '\'' +
				", progress_status=" + progress_status +
				", isSelect=" + isSelect +
				", file_time=" + file_time +
				'}';
	}
}
