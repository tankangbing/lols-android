package com.example.util;

import com.example.spt.jaxb.paper.ExerciseCard;
import com.example.spt.jaxb.paper.Paper;
import com.example.spt.jaxb.paper.Question;
import com.example.spt.jaxb.paper.QuestionModel;
import com.example.spt.jaxb.paper.Questions;
import com.example.spt.jaxb.paper.ResQuestionTypeRuleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysg on 2017/8/10.
 */

public class PracticUtil {

    //获取练习题目
    public static List<Question> getQuestionList(Paper paper ,String questionIds ,String questionForm){
        List<Question> questionList=new ArrayList<Question>();
        Questions questions=paper.getQuestions();
        if (questions!=null){
            List<Question> question=questions.getQuestion();
            if (!questionForm.equals("0")){
                if (question!=null&&question.size()>0){
                    if (questionForm.equals("1")){
                        for (int i=0;i<question.size();i++){
                            if(questionIds.indexOf(question.get(i).getId())==-1){
                                questionList.add(question.get(i));
                            }
                        }
                    }else if (questionForm.equals("5")){
                        String [] ids=questionIds.split("-");
                        if (ids!=null&&ids.length==2){
                            for (int i=0;i<question.size();i++){
                                if(ids[0].indexOf(question.get(i).getId())!=-1){
                                    if (ids[1].indexOf(question.get(i).getId())!=-1){
                                        questionList.add(question.get(i));
                                    }
                                }
                            }
                        }
                    }else {
                        for (int i=0;i<question.size();i++){
                            if(questionIds.indexOf(question.get(i).getId())!=-1){
                                questionList.add(question.get(i));
                            }
                        }
                    }
                }
            }else {
                questionList=question;
            }
        }
        return questionList;
    }

    //获取练习题目
    public static ExerciseCard getExerciseCard(ExerciseCard exerciseCard ,String questionIds ,String questionForm){
        ExerciseCard exerciseCard1=new ExerciseCard();
        //exerciseCard1=exerciseCard;
        List<ResQuestionTypeRuleModel> questionsList=new ArrayList<ResQuestionTypeRuleModel>();
        //if (!questionForm.equals("0")){
            int index = 0;
            String [] ids=questionIds.split("-");
            for(ResQuestionTypeRuleModel model : exerciseCard.getQuestionsList()) {
                ResQuestionTypeRuleModel question=new ResQuestionTypeRuleModel();
                question.setPaperQuestionType(model.getPaperQuestionType());
                List<QuestionModel> questionList = model.getQuestion();//当前题型所有题目
                List<QuestionModel> tempQuestionModelList = new ArrayList<QuestionModel>();
                if(questionList!=null){
                    for (QuestionModel quesitonModel : questionList) {
                        if (questionForm.equals("1")){
                            if(questionIds.indexOf(quesitonModel.getId())==-1){
                                tempQuestionModelList.add(quesitonModel);
                            }
                        }else if (questionForm.equals("0")){
                            tempQuestionModelList.add(quesitonModel);
                        }else if (questionForm.equals("5")){
                            if(ids[0].indexOf(quesitonModel.getId())!=-1){
                                if (ids[1].indexOf(quesitonModel.getId())!=-1){
                                    tempQuestionModelList.add(quesitonModel);
                                }
                            }
                        }else {
                            if(questionIds.indexOf(quesitonModel.getId())!=-1){
                                tempQuestionModelList.add(quesitonModel);
                            }
                        }
                    }
                }

                question.setQuestion(tempQuestionModelList);
                questionsList.add(question);
                index++;
            }
       // }
        exerciseCard1.setQuestionsList(questionsList);
        return exerciseCard1;
    }
}
