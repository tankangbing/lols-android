package com.example.jsonReturn;

public class UpdatePaperReturn {
    private String paperXml;//练习xml
    private String exerciseCardXml;//答题卡xml

	public String getPaperXml() {
		return paperXml;
	}

	public void setPaperXml(String paperXml) {
		this.paperXml = paperXml;
	}

    public String getExerciseCardXml() {
        return exerciseCardXml;
    }

    public void setExerciseCardXml(String exerciseCardXml) {
        this.exerciseCardXml = exerciseCardXml;
    }
}
