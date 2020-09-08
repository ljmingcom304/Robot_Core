package com.mmednet.library;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mmednet.library.util.JsonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

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
