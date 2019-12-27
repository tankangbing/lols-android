package com.example.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.example.entity.DLPracticeAnswerModel;
import com.example.entity.ExerciseCard;
import com.example.entity.Item;
import com.example.entity.Paper;
import com.example.entity.Question;

/**
 * 把string转换成json对象
 * @author Amao
 *
 */

public class StringToJson {

	private static ObjectMapper objectMapper = null;

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSON2Map(String jsonStr){
		Map<String, Object> maps  = new HashMap<String, Object>();
		try {
			objectMapper = new ObjectMapper();
			maps = objectMapper.readValue(jsonStr, Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maps;
	}


	@SuppressWarnings("unchecked")
	public static Item parseJSON2TreeNode(String jsonStr){
		Item t  = new Item();
		try {
			objectMapper = new ObjectMapper();
			t = objectMapper.readValue(jsonStr, Item.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public static Paper parseJSON2TreePaper(String jsonStr){
		Paper t  = new Paper();
		try {
			objectMapper = new ObjectMapper();
			t = objectMapper.readValue(jsonStr, Paper.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public static ExerciseCard parseJSON2TreeExerciseCardr(
			String jsonStr) {
		ExerciseCard t  = new ExerciseCard();
		try {
			objectMapper = new ObjectMapper();
			t = objectMapper.readValue(jsonStr, ExerciseCard.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public static Question parseJSON2TreeQuestion(String jsonStr){
		Question t  = new Question();
		try {
			objectMapper = new ObjectMapper();
			t = objectMapper.readValue(jsonStr, Question.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}


	@SuppressWarnings("unchecked")
	public static List<DLPracticeAnswerModel> parseJSON2ListDLPracticeAnswer(String jsonStr){
		List<DLPracticeAnswerModel> list  = new ArrayList<DLPracticeAnswerModel>();
		try {
			list = objectMapper.readValue(jsonStr, List.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static Map<String,List<Map<String, Object>>> parseJSON2MapListMap(String jsonStr){
		Map<String,List<Map<String,Object>>> maps  = new HashMap<String,List<Map<String,Object>>>();
		try {
			objectMapper = new ObjectMapper();
			maps = objectMapper.readValue(jsonStr, Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maps;
	}




}
