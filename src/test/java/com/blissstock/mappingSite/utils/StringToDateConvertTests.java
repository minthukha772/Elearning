package com.blissstock.mappingSite.utils;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class StringToDateConvertTests {
    @Test
    @Tag("ValidStringCase")

    public void ValidStringTestCase(){
        String input = "2021-09-04";
        Date localDate = StringToDateConvert.stringToDate(input);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(localDate);
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(8, calendar.get(Calendar.MONTH));
        assertEquals(4, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    @Tag("NullStringCase")

    public void NullStringTestCase(){
        String input = null;
        Date localDate = StringToDateConvert.stringToDate(input);
        assertEquals(null, localDate);
    }

    @Test
    @Tag("EmptyStringCase")

    public void EmptyStringTestCase(){
        String input = "";
        Date localDate = StringToDateConvert.stringToDate(input);
        assertEquals(null, localDate);
    }

    @Test
    @Tag("InvalidDateCase")

    public void InvalidStringTestCase(){
        String input = "04-09-2021";
        Date localDate = StringToDateConvert.stringToDate(input);
        assertEquals(null, localDate);
    }
}
