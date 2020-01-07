package com.mmednet.library;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mmednet.library.util.JsonUtils;
import com.mmednet.library.util.SignUtils;

import org.junit.Test;

import java.util.HashMap;

/**
 * Example local unit test, which will execute on the development machine (intentHost).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String result = "{'data':null}";
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(result);
        JsonObject jsonObject = parse.getAsJsonObject();
        JsonElement dElement = jsonObject.get("data");
        System.out.println(dElement.isJsonArray() + "=" + dElement.isJsonObject() + "=" + dElement.isJsonPrimitive()+"="+ JsonUtils.isJsonEmpty(dElement));
    }


}