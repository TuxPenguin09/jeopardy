package com.example.jeopardy.utils;

import android.content.Context;

import com.example.jeopardy.model.Question;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QuestionLoader {

    public static Map<String, List<Question>> loadQuestions(Context context) {
        Map<String, List<Question>> categoryMap = new HashMap<>();

        try {
            InputStream is = context.getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(json).getJSONObject("categories");

            Iterator<String> keys = root.keys();
            while (keys.hasNext()) {
                String category = keys.next();
                JSONArray questionsArray = root.getJSONArray(category);
                List<Question> questionList = new ArrayList<>();

                for (int i = 0; i < questionsArray.length(); i++) {
                    JSONObject qObj = questionsArray.getJSONObject(i);
                    Question question = new QuestionLoader().parseQuestion(qObj);
                    questionList.add(question);
                }

                categoryMap.put(category, questionList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryMap;
    }

    private Question parseQuestion(JSONObject obj) throws Exception {
        Question q = new Question();

        java.lang.reflect.Field qField = q.getClass().getDeclaredField("question");
        qField.setAccessible(true);
        qField.set(q, obj.getString("question"));

        JSONArray aArray = obj.getJSONArray("answer");
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < aArray.length(); i++) {
            answers.add(aArray.getString(i).toLowerCase());
        }

        java.lang.reflect.Field aField = q.getClass().getDeclaredField("answer");
        aField.setAccessible(true);
        aField.set(q, answers);

        return q;
    }
}

