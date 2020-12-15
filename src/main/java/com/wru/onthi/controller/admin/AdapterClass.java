package com.wru.onthi.controller.admin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.wru.onthi.entity.Question;


import java.lang.reflect.Type;

public class AdapterClass implements JsonSerializer<Question> {
    @Override
    public JsonElement serialize(Question question, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", question.getId());
        jsonObject.addProperty("content", question.getContent());
        jsonObject.addProperty("ans_a", question.getAnsA());
        jsonObject.addProperty("ans_b", question.getAnsB());
        jsonObject.addProperty("ans_c", question.getAnsC());
        jsonObject.addProperty("ans_d", question.getAnsD());
        return jsonObject;
    }
}
