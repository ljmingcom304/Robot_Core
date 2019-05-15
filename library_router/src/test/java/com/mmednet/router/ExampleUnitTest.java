package com.mmednet.router;

import com.mmednet.router.bean.WesternBean;
import com.mmednet.router.router.WesternRouter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        WesternRouter router = new WesternRouter();
        WesternBean bean = router.buildBean();
        System.out.print(bean.getClass().getSimpleName());
        assertEquals(4, 2 + 2);
    }
}