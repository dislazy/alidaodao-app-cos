package com.alidaodao.app.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @desc 日期帮助类
 * @author Jack
 * @date 2020/2/23 23:19
 */
public class DateTimeUtils {

    public static Date changeMinute(Date date,int minutes){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

}
