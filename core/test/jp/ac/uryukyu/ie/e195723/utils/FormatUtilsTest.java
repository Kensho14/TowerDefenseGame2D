package jp.ac.uryukyu.ie.e195723.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormatUtilsTest {

    @Test
    public void getFormattedTimeText() {
        assertEquals("3:00", FormatUtils.getFormattedTimeText(3*60*1000));
    }
}