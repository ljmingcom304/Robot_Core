package com.mmednet.library;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mmednet.library.util.JsonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Reader;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith( AndroidJUnit4.class )
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        String json = "{\"data\":[],\"status\":0}";
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(json);
        assertEquals("[]", JsonUtils.isJsonEmpty(parse));
    }
}
