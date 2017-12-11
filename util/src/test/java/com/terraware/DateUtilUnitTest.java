package com.terraware;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilUnitTest {

    @Test
    public void testConversion() {
        Date date = new Date();

        LocalDateTime localDateTime = DateUtil.toLocalDateTime(date);

        Date convertedDate = DateUtil.toDate(localDateTime);
        assertEquals(date, convertedDate);
    }
}
