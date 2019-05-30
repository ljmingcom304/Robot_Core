package com.mmednet.library;

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
        HashMap<String, String> params = new HashMap<>();
        params.put("doctorUid", "2c94808c6ae3a01f016ae783e46c0000");
        params.put("queryUid", "2c94808c6ae3a01f016ae783e46c0000");
        params.put("timestamp", "1558692602458");
        params.put("token", "c423aa06715e4da48871aca3f902c759");
        params.put("type", "1");
        String sign = SignUtils.generateSign(params);
        System.out.println(sign);
    }


}