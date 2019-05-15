package com.mmednet.library;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.hpl.sparta.Parser;
import com.mmednet.library.http.code.Encrypt;
import com.mmednet.library.util.AESUtils;

import org.junit.Test;

import java.security.Key;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Example local unit test, which will execute on the development machine (intentHost).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String data = "{\"data\":\"abcdefg\"}";
        JsonParser parser = new JsonParser();
        JsonElement parse = parser.parse(data);
        JsonObject jsonObject = parse.getAsJsonObject();
        JsonElement element = jsonObject.get("data");
        System.out.println(element.isJsonArray() + "==" + element.isJsonObject()+"="+element.getAsString()+"="+element.toString());
    }


}